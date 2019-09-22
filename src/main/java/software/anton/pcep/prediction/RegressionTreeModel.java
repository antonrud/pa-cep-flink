package software.anton.pcep.prediction;

import org.apache.flink.configuration.Configuration;
import org.apache.flink.shaded.guava18.com.google.common.collect.Iterables;
import org.apache.flink.streaming.api.functions.windowing.ProcessAllWindowFunction;
import org.apache.flink.streaming.api.windowing.windows.GlobalWindow;
import org.apache.flink.util.Collector;
import smile.regression.RegressionTree;
import software.anton.pcep.data.KeyedDataPoint;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static org.apache.commons.lang3.ArrayUtils.toPrimitive;
import static software.anton.pcep.configs.Configuration.RATE;

/**
 * @author Anton Rudacov <anton.rudacov @ gmail.com>
 */
public class RegressionTreeModel extends ProcessAllWindowFunction<KeyedDataPoint<Double>, KeyedDataPoint<Double>, GlobalWindow> {

  private static final int MAX_NODES = 100;

  private RegressionTree model30min;
  private RegressionTree model1hour;

  private List<List<Double>> explanatoryVariables;
  private List<Double> response30min;
  private List<Double> response1hour;

  private int dataPointsCounter;
  private int weekCounter;
  private boolean modelIsReady;

  @Override
  public void open(Configuration parameters) throws Exception {
    super.open(parameters);

    explanatoryVariables = new ArrayList<>();
    response30min = new ArrayList<>();
    response1hour = new ArrayList<>();

    dataPointsCounter = 1;
    weekCounter = 0;
    modelIsReady = false;
  }

  @Override
  public void process(Context context, Iterable<KeyedDataPoint<Double>> elements, Collector<KeyedDataPoint<Double>> out) {

    // Skip processing until 8 data points are received
    if (dataPointsCounter < 8) {
      dataPointsCounter++;
      return;
    }

    if (weekCounter < 336) {
      explanatoryVariables.add(calculateExplanatoryVariables(elements, 0));
      response30min.add(Iterables.get(elements, 6).getValue());
      response1hour.add(Iterables.get(elements, 7).getValue());
      weekCounter++;
    } else {
      trainModels();
      modelIsReady = true;
      weekCounter = 0;
    }

    if (modelIsReady) {

      double[] variables = toPrimitive(calculateExplanatoryVariables(elements, 2).toArray(new Double[0]));

      double predicted30min = model30min.predict(variables);
      double predicted1hour = model1hour.predict(variables);

      long lastTimestamp = Iterables.getLast(elements).getTimeStamp();

      KeyedDataPoint<Double> point30min = new KeyedDataPoint<>("30min", predicted30min, lastTimestamp + RATE);
      KeyedDataPoint<Double> point1hour = new KeyedDataPoint<>("1hour", predicted1hour, lastTimestamp + RATE * 2);

      out.collect(point30min);
      out.collect(point1hour);
    }
  }

  private List<Double> calculateExplanatoryVariables(Iterable<KeyedDataPoint<Double>> elements, int start) {

    double[] variables = new double[6];
    Iterator<KeyedDataPoint<Double>> iterator = Iterables.skip(elements, start).iterator();
    for (int i = 0; i < 6; i++) {
      variables[i] = iterator.next().getValue();
    }
    Arrays.sort(variables);

    double median = (variables[variables.length / 2] + variables[variables.length / 2 - 1]) / 2;
    double sum = Arrays.stream(variables).sum();
    double max = Arrays.stream(variables).max().getAsDouble();
    double min = Arrays.stream(variables).min().getAsDouble();
    double avg = Arrays.stream(variables).average().getAsDouble();
    double diff = variables[5] - variables[0];

    return Arrays.asList(median, sum, max, min, avg, diff);
  }

  private void trainModels() {

    double[][] variables = new double[explanatoryVariables.size()][6];
    for (int index = 0; index < explanatoryVariables.size(); index++) {
      variables[index] = toPrimitive(explanatoryVariables.get(index).toArray(new Double[0]));
    }

    double[] target30min = toPrimitive(response30min.toArray(new Double[0]));
    double[] target1hour = toPrimitive(response1hour.toArray(new Double[0]));

    model30min = new RegressionTree(variables, target30min, MAX_NODES);
    model1hour = new RegressionTree(variables, target1hour, MAX_NODES);
  }
}

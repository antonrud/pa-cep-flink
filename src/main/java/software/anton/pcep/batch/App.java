package software.anton.pcep.batch;

import org.apache.flink.api.java.operators.DataSource;
import org.apache.flink.shaded.guava18.com.google.common.collect.Iterables;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import smile.regression.RegressionTree;
import software.anton.pcep.data.KeyedDataPoint;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static org.apache.commons.lang3.ArrayUtils.toPrimitive;

/**
 * @author Anton Rudacov <anton.rudacov @ gmail.com>
 */
public class App {

  private static final int MAX_NODES = 100;
  private RegressionTree model30min;
  private RegressionTree model1hour;
  private List<List<Double>> explanatoryVariables;
  private List<Double> response30min;
  private List<Double> response1hour;

  public static void main(String[] args) throws Exception {

    final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
    env.setParallelism(1);

    DataStreamSource<String> stream = env.readTextFile("src/main/resources/weekly/week2/predict.txt");

    stream.countWindowAll(8,1)

    env.execute("Batch job");


    RegressionTree model = new RegressionTree();

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

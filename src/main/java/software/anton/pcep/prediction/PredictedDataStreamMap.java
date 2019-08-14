package software.anton.pcep.prediction;

import org.apache.flink.api.common.functions.RichFlatMapFunction;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.util.Collector;
import software.anton.pcep.data.KeyedDataPoint;

/**
 * @author Anton Rudacov <anton.rudacov @ gmail.com>
 */
public class PredictedDataStreamMap extends RichFlatMapFunction<KeyedDataPoint<Double>, KeyedDataPoint<Double>> {

  private static boolean modelIsReady;

  @Override
  public void open(Configuration parameters) throws Exception {
    super.open(parameters);

    modelIsReady = false;
    //TODO
  }

  @Override
  public void flatMap(KeyedDataPoint<Double> value, Collector<KeyedDataPoint<Double>> out) throws Exception {

    out.collect(value);
  }

  public static void setModelIsReady(boolean modelIsReady) {
    PredictedDataStreamMap.modelIsReady = modelIsReady;
  }
}

package software.anton.pcep.prediction;

import org.apache.flink.api.common.functions.RichFlatMapFunction;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.util.Collector;
import software.anton.pcep.data.KeyedDataPoint;

/**
 * @author Anton Rudacov <anton.rudacov @ gmail.com>
 */
public class PredictedDataStreamMap extends RichFlatMapFunction<KeyedDataPoint<Double>, KeyedDataPoint<Double>> {

  @Override
  public void open(Configuration parameters) throws Exception {
    super.open(parameters);

    //TODO
  }

  @Override
  public void flatMap(KeyedDataPoint<Double> value, Collector<KeyedDataPoint<Double>> out) throws Exception {

    out.collect(value);
  }
}

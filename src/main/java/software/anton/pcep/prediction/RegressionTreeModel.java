package software.anton.pcep.prediction;

import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;
import software.anton.pcep.data.KeyedDataPoint;

/**
 * @author Anton Rudacov <anton.rudacov @ gmail.com>
 */
public class RegressionTreeModel extends RichSinkFunction<KeyedDataPoint<Double>> {

  @Override
  public void open(Configuration parameters) throws Exception {
    super.open(parameters);

    //TODO Inits
  }


  @Override
  public void invoke(KeyedDataPoint<Double> value, Context context) throws Exception {

    //TODO
  }

  @Override
  public void close() throws Exception {
    super.close();
  }
}

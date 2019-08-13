package software.anton.pcep.cep;

import org.apache.flink.cep.PatternSelectFunction;
import software.anton.pcep.data.Alert;
import software.anton.pcep.data.KeyedDataPoint;

import java.util.List;
import java.util.Map;

/**
 * @author Anton Rudacov <anton.rudacov @ gmail.com>
 */
public class PatternSelector implements PatternSelectFunction<KeyedDataPoint<Double>, Alert> {

  private String tag;

  public PatternSelector(String tag) {
    this.tag = tag;
  }

  @Override
  public Alert select(Map<String, List<KeyedDataPoint<Double>>> pattern) {

    return new Alert(pattern.get("trigger").get(0).getTimeStamp(), "Overloading", tag);
  }
}

package software.anton.pcep.misc;

import org.apache.flink.streaming.api.functions.AssignerWithPunctuatedWatermarks;
import org.apache.flink.streaming.api.watermark.Watermark;
import software.anton.pcep.data.KeyedDataPoint;

import javax.annotation.Nullable;

/**
 * @author Anton Rudacov <anton.rudacov @ gmail.com>
 */
public class SimpleAssigner implements AssignerWithPunctuatedWatermarks<KeyedDataPoint<Double>> {

  @Override
  public long extractTimestamp(KeyedDataPoint<Double> element, long previousElementTimestamp) {
    return element.getTimeStamp();
  }

  @Nullable
  @Override
  public Watermark checkAndGetNextWatermark(KeyedDataPoint<Double> lastElement, long extractedTimestamp) {
    return new Watermark(lastElement.getTimeStamp());
  }
}

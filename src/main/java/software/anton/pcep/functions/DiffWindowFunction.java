package software.anton.pcep.functions;

import org.apache.flink.streaming.api.functions.windowing.WindowFunction;
import org.apache.flink.streaming.api.windowing.windows.GlobalWindow;
import org.apache.flink.util.Collector;
import software.anton.pcep.data.KeyedDataPoint;
import software.anton.pcep.utils.GrafanaAnnotator;

import static software.anton.pcep.configs.Configuration.*;

/**
 * @author Anton Rudacov <anton.rudacov @ gmail.com>
 */
@Deprecated
public class DiffWindowFunction implements WindowFunction<KeyedDataPoint<Double>, Double, String, GlobalWindow> {

  private final static GrafanaAnnotator ANNOTATOR = new GrafanaAnnotator(GRAFANA_DASHBOARD, GRAFANA_PANEL_PA);

  @Override
  public void apply(String key, GlobalWindow window, Iterable<KeyedDataPoint<Double>> input, Collector<Double> out) throws Exception {

    double sum = 0;
    long lastTimeStamp = 0L;
    for (KeyedDataPoint<Double> point : input) {
      sum += point.getValue();
      lastTimeStamp = point.getTimeStamp();
    }

    if (sum > 15) {
      ANNOTATOR.sendAnnotation(lastTimeStamp, "Alert", "DIFF");
    }

    out.collect(sum);
  }
}

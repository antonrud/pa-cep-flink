package software.anton.pcep.functions;

import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.ProcessFunction;
import org.apache.flink.util.Collector;
import software.anton.pcep.data.Alert;
import software.anton.pcep.utils.GrafanaAnnotator;

import static software.anton.pcep.configs.Configuration.*;

/**
 * @author Anton Rudacov <anton.rudacov @ gmail.com>
 */
@Deprecated
public class AnnotationFunctionPA extends ProcessFunction<Alert, Alert> {

  private GrafanaAnnotator annotator;

  @Override
  public void open(Configuration parameters) throws Exception {
    super.open(parameters);

    annotator = new GrafanaAnnotator(GRAFANA_DASHBOARD, GRAFANA_PANEL_PA);
  }

  @Override
  public void processElement(Alert value, Context ctx, Collector<Alert> out) {

    annotator.sendAlert(value);
    out.collect(value);
  }
}

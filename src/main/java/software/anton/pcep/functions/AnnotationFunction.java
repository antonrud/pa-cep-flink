package software.anton.pcep.functions;

import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.ProcessFunction;
import org.apache.flink.util.Collector;
import software.anton.pcep.data.Alert;
import software.anton.pcep.utils.GrafanaAnnotator;

/**
 * @author Anton Rudacov <anton.rudacov @ gmail.com>
 */
public class AnnotationFunction extends ProcessFunction<Alert, Alert> {

  private int dashboard;
  private int panel;
  private GrafanaAnnotator annotator;

  public AnnotationFunction(int dashboard, int panel) {
    this.dashboard = dashboard;
    this.panel = panel;
  }

  @Override
  public void open(Configuration parameters) throws Exception {
    super.open(parameters);

    annotator = new GrafanaAnnotator(dashboard, panel);
  }

  @Override
  public void processElement(Alert value, Context ctx, Collector<Alert> out) {

    annotator.sendAlert(value);
    out.collect(value);
  }
}

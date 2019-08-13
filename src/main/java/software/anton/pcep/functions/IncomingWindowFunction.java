package software.anton.pcep.functions;

import org.apache.flink.streaming.api.functions.windowing.WindowFunction;
import org.apache.flink.streaming.api.windowing.windows.GlobalWindow;
import org.apache.flink.util.Collector;
import software.anton.pcep.data.KeyedDataPoint;
import software.anton.pcep.utils.GrafanaAnnotator;

import static software.anton.pcep.configs.Configuration.GRAFANA_DASHBOARD;
import static software.anton.pcep.configs.Configuration.GRAFANA_PANEL;

/**
 * @author Anton Rudacov <anton.rudacov @ gmail.com>
 */
public class IncomingWindowFunction implements WindowFunction<KeyedDataPoint<Double>, Double, String, GlobalWindow> {

    private final static GrafanaAnnotator ANNOTATOR_IN = new GrafanaAnnotator(GRAFANA_DASHBOARD, GRAFANA_PANEL);
    private final static GrafanaAnnotator ANNOTATOR_BOTH = new GrafanaAnnotator(GRAFANA_DASHBOARD, 4);

    @Override
    public void apply(String key, GlobalWindow window, Iterable<KeyedDataPoint<Double>> input, Collector<Double> out) throws Exception {

        double sum = 0;
        long lastTimeStamp = 0L;
        for (KeyedDataPoint<Double> point : input) {
            System.out.println(point);
            sum += point.getValue();
            lastTimeStamp = point.getTimeStamp();
        }

        switch (key) {
            case "in":
                if (sum > 25) {
                    ANNOTATOR_IN.sendAnnotation(lastTimeStamp, "Alert", "IN");
                }
                break;
            case "diff":
                if (sum > 15) {
                    ANNOTATOR_BOTH.sendAnnotation(lastTimeStamp, "Alert", "DIFF");
                }
        }

        out.collect(sum);
    }
}

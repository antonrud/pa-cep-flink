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
public class IncomingWindowFunction implements WindowFunction<KeyedDataPoint<Integer>, Integer, String, GlobalWindow> {

    private final static GrafanaAnnotator ANNOTATOR = new GrafanaAnnotator(GRAFANA_DASHBOARD, GRAFANA_PANEL);

    @Override
    public void apply(String s, GlobalWindow window, Iterable<KeyedDataPoint<Integer>> input, Collector<Integer> out) throws Exception {

        int sum = 0;
        long lastTimeStamp = 0L;
        for (KeyedDataPoint<Integer> point : input) {
            sum += point.getValue();
            lastTimeStamp = point.getTimeStamp();
        }

        if (sum > 25) {
            ANNOTATOR.sendAnnotation(lastTimeStamp, "Alert", "IN");
        }

        out.collect(sum);
    }
}

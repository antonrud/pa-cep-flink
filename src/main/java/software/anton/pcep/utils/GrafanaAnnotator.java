package software.anton.pcep.utils;

import static software.anton.pcep.configs.Configuration.GRAFANA_PASS;
import static software.anton.pcep.configs.Configuration.GRAFANA_URL;
import static software.anton.pcep.configs.Configuration.GRAFANA_USER;

import kong.unirest.Unirest;
import org.json.JSONObject;
import software.anton.pcep.data.Alert;

import java.util.Collections;

/**
 * @author Anton Rudacov <anton.rudacov @ gmail.com>
 */
public final class GrafanaAnnotator {

    private final int dashboard;
    private final int panel;

    public GrafanaAnnotator(int dashboard, int panel) {
        this.dashboard = dashboard;
        this.panel = panel;
    }

    public int sendAnnotation(long start, long end, String text, String tag) {

        JSONObject body = new JSONObject();
        body.put("dashboardId", dashboard);
        body.put("panelId", panel);
        body.put("time", start);
        body.put("isRegion", true);
        body.put("timeEnd", end);
        body.put("tags", Collections.singletonList(tag));
        body.put("text", text);

        return Unirest.post(GRAFANA_URL)
                .basicAuth(GRAFANA_USER, GRAFANA_PASS)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .body(body)
                .asJson()
                .getStatus();
    }

    public int sendAnnotation(long start, long end) {
        return sendAnnotation(start, end, "Alert", "general");
    }

    public int sendAnnotation(long start, long end, String text) {
        return sendAnnotation(start, end, text, "general");
    }

    public int sendAnnotation(long start, String text, String tag) {

        JSONObject body = new JSONObject();
        body.put("dashboardId", dashboard);
        body.put("panelId", panel);
        body.put("time", start);
        body.put("isRegion", false);
        body.put("tags", Collections.singletonList(tag));
        body.put("text", text);

        return Unirest.post(GRAFANA_URL)
                .basicAuth(GRAFANA_USER, GRAFANA_PASS)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .body(body)
                .asJson()
                .getStatus();
    }

    public void sendAlert(Alert alert) {
        sendAnnotation(alert.getTimestamp(), alert.getMessage(), alert.getTag());
    }
}

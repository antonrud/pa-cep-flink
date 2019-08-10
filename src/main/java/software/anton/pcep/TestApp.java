package software.anton.pcep;

import software.anton.pcep.utils.GrafanaAnnotator;

import static software.anton.pcep.configs.Configuration.*;

/**
 * @author Anton Rudacov <anton.rudacov @ gmail.com>
 */
public class TestApp {

    public static void main(String[] args) {

        long start = System.currentTimeMillis();

        System.out.println(new GrafanaAnnotator(GRAFANA_DASHBOARD, GRAFANA_PANEL).sendAnnotation(start, start + 1000L));
    }
}

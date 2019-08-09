package software.anton.pcep;

import software.anton.pcep.utils.GrafanaAnnotator;

/**
 * @author Anton Rudacov <anton.rudacov @ gmail.com>
 */
public class TestApp {

    public static void main(String[] args) {

        long start = System.currentTimeMillis();

        System.out.println(new GrafanaAnnotator(7, 2).sendAnnotation(start, start + 1000L));
    }
}

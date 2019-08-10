package software.anton.pcep.configs;

/**
 * @author Anton Rudacov <anton.rudacov @ gmail.com>
 */
public final class Configuration {

    public static final String DATASET = "src/main/resources/dataset/CalIt2.data";

    public static final String KAFKA_BROKER = "localhost:9092";
    public static final String KAFKA_TOPIC = "calit";

    public static final String INFLUX_URL = "http://localhost:8086";
    public static final String INFLUX_USER = "root";
    public static final String INFLUX_PASS = "root";
    public static final String INFLUX_DATABASE = "calit";
    public static final String INFLUX_MEASUREMENT = "data";

    public static final int GRAFANA_DASHBOARD = 5;
    public static final int GRAFANA_PANEL = 2;
}

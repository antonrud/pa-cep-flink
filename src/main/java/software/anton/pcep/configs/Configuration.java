package software.anton.pcep.configs;

import java.util.Properties;

/**
 * @author Anton Rudacov <anton.rudacov @ gmail.com>
 */
public final class Configuration {

    public static final String DATASET = "src/main/resources/dataset/CalIt2.data";
    public static final long RATE = 100L;

    public static final String ZOOKEEPER = "localhost:2181";
    public static final String KAFKA_BROKER = "localhost:9092";
    public static final String KAFKA_TOPIC = "calit";
    public static final Properties KAFKA_PROPERTIES = createKafkaProperties();

    public static final String INFLUX_URL = "http://localhost:8086";
    public static final String INFLUX_USER = "root";
    public static final String INFLUX_PASS = "root";
    public static final String INFLUX_DATABASE = "calit";
    public static final String INFLUX_MEASUREMENT = "data";

    public static final int GRAFANA_DASHBOARD = 5;
    public static final int GRAFANA_PANEL = 2;


    private static Properties createKafkaProperties() {

        Properties kafkaProperties = new Properties();
        kafkaProperties.setProperty("zookeeper.connect", ZOOKEEPER);
        kafkaProperties.setProperty("bootstrap.servers", KAFKA_BROKER);

        return kafkaProperties;
    }
}

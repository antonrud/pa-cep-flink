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
  public static final String KAFKA_TOPIC_IN_OUT = "calit";
  public static final String KAFKA_TOPIC_DIFF = "diff";
  public static final String KAFKA_TOPIC_PA = "predicted";
  public static final Properties KAFKA_PROPERTIES = createKafkaProperties();

  public static final String INFLUX_URL = "http://localhost:8086";
  public static final String INFLUX_USER = "root";
  public static final String INFLUX_PASS = "root";
  public static final String INFLUX_DATABASE = "calit";
  public static final String INFLUX_MEASUREMENT = "data";
  public static final String INFLUX_PREDICTION_MEASUREMENT = "prediction";

  public static final String GRAFANA_URL = "http://localhost:3000/api/annotations";
  public static final String GRAFANA_USER = "admin";
  public static final String GRAFANA_PASS = "password";
  public static final int GRAFANA_DASHBOARD = 5;
  public static final int GRAFANA_PANEL_IN_OUT = 2;
  public static final int GRAFANA_PANEL_DIFF = 4;
  public static final int GRAFANA_PANEL_PA = 8;

  private static Properties createKafkaProperties() {

    Properties kafkaProperties = new Properties();
    kafkaProperties.setProperty("zookeeper.connect", ZOOKEEPER);
    kafkaProperties.setProperty("bootstrap.servers", KAFKA_BROKER);

    return kafkaProperties;
  }
}

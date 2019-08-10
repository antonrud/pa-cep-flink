package software.anton.pcep.jobs;

import static software.anton.pcep.configs.Configuration.*;

import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import software.anton.pcep.data.KeyedDataPoint;
import software.anton.pcep.maps.KeyedDataPointMap;
import software.anton.pcep.sinks.InfluxDBSink;

import java.util.Properties;

/**
 * @author Anton Rudacov <anton.rudacov @ gmail.com>
 */
public class ConsumerJob {

    public static void main(String[] args) throws Exception {

        Properties properties = createKafkaProperties();

        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);

        DataStream<KeyedDataPoint<Integer>> dataStream = env
                .addSource(new FlinkKafkaConsumer<>(KAFKA_TOPIC, new SimpleStringSchema(), properties))
                .flatMap(new KeyedDataPointMap());

        // Persist data in InfluxDB
        dataStream.addSink(new InfluxDBSink<>(INFLUX_DATABASE, INFLUX_MEASUREMENT));

        env.execute("Data consumer");
    }

    private static Properties createKafkaProperties() {

        Properties kafkaProperties = new Properties();
        kafkaProperties.setProperty("zookeeper.connect", "localhost:2181");
        kafkaProperties.setProperty("bootstrap.servers", "localhost:9092");

        return kafkaProperties;
    }
}

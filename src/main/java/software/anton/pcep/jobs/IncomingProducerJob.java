package software.anton.pcep.jobs;

import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer;
import software.anton.pcep.sources.IncomingSource;

/**
 * @author Anton Rudacov <anton.rudacov @ gmail.com>
 */
public class IncomingProducerJob {

    private static final String BROKER = "localhost:9092";
    private static final String TOPIC = "calit";

    public static void main(String[] args) throws Exception {

        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);

        DataStream<String> incomingStream = env.addSource(new IncomingSource(100L));

        incomingStream.addSink(new FlinkKafkaProducer<>(BROKER, TOPIC, new SimpleStringSchema()));
        incomingStream.print();

        env.execute("Incoming Producer");
    }
}

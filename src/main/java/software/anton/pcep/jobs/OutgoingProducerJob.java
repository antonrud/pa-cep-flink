package software.anton.pcep.jobs;

import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer;
import software.anton.pcep.sources.OutgoingSource;

/**
 * @author Anton Rudacov <anton.rudacov @ gmail.com>
 */
public class OutgoingProducerJob {

    private static final String BROKER = "localhost:9092";
    private static final String TOPIC = "calit";

    public static void main(String[] args) throws Exception {

        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);

        DataStream<String> outgoingStream = env.addSource(new OutgoingSource(100L));

        outgoingStream.addSink(new FlinkKafkaProducer<>(BROKER, TOPIC, new SimpleStringSchema()));
        outgoingStream.print();

        env.execute("Outgoing Producer");
    }
}

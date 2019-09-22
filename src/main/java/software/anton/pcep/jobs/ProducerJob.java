package software.anton.pcep.jobs;

import static software.anton.pcep.configs.Configuration.*;

import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer;
import software.anton.pcep.sources.CombinedSource;

/**
 * @author Anton Rudacov <anton.rudacov @ gmail.com>
 */
public class ProducerJob {

  public static void main(String[] args) throws Exception {

    final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
    env.setParallelism(1);
    env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);

    DataStream<String> outgoingStream = env.addSource(new CombinedSource(RATE));

    outgoingStream
            .filter(x -> x.charAt(0) != 'd')
            .addSink(new FlinkKafkaProducer<>(KAFKA_BROKER, KAFKA_TOPIC_IN_OUT, new SimpleStringSchema()));

    outgoingStream
            .filter(x -> x.charAt(0) == 'd')
            .addSink(new FlinkKafkaProducer<>(KAFKA_BROKER, KAFKA_TOPIC_DIFF, new SimpleStringSchema()));

    outgoingStream.print();

    env.execute("Combined Producer");
  }
}

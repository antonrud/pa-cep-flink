package software.anton.pcep.jobs;

import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.cep.CEP;
import org.apache.flink.cep.pattern.Pattern;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import software.anton.pcep.cep.PatternFactory;
import software.anton.pcep.cep.PatternSelector;
import software.anton.pcep.data.KeyedDataPoint;
import software.anton.pcep.functions.AnnotationFunctionPA;
import software.anton.pcep.maps.KeyedDataPointMap;
import software.anton.pcep.misc.SimpleAssigner;

import static software.anton.pcep.configs.Configuration.KAFKA_PROPERTIES;
import static software.anton.pcep.configs.Configuration.KAFKA_TOPIC_PA;

/**
 * @author Anton Rudacov <anton.rudacov @ gmail.com>
 */
public class PredictionJob {

  public static void main(String[] args) throws Exception {

    final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
    env.setParallelism(1);
    env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);

    //Receive predicted data from kafka topic
    DataStream<KeyedDataPoint<Double>> dataStreamPA = env
            .addSource(new FlinkKafkaConsumer<>(KAFKA_TOPIC_PA, new SimpleStringSchema(), KAFKA_PROPERTIES))
            .flatMap(new KeyedDataPointMap())
            .assignTimestampsAndWatermarks(new SimpleAssigner());

    // Load CEP pattern
    final Pattern<KeyedDataPoint<Double>, ?> pattern = PatternFactory.getPattern();

    // Perform CEP on predicted diffs
    CEP.pattern(dataStreamPA, pattern)
            .select(new PatternSelector("predicted"))
            //.print();
            .process(new AnnotationFunctionPA());

    env.execute("Prediction stream consumer");
  }
}

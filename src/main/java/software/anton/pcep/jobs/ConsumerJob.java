package software.anton.pcep.jobs;

import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.cep.CEP;
import org.apache.flink.cep.pattern.Pattern;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer;
import software.anton.pcep.cep.PatternFactory;
import software.anton.pcep.cep.PatternSelector;
import software.anton.pcep.data.KeyedDataPoint;
import software.anton.pcep.functions.AnnotationFunction;
import software.anton.pcep.maps.KeyedDataPointMap;
import software.anton.pcep.misc.SimpleAssigner;
import software.anton.pcep.prediction.RegressionTreeModel;
import software.anton.pcep.sinks.InfluxDBSink;

import static software.anton.pcep.configs.Configuration.*;

/**
 * @author Anton Rudacov <anton.rudacov @ gmail.com>
 */
public class ConsumerJob {

  public static void main(String[] args) throws Exception {

    final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
    env.setParallelism(1);
    env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);

    //Receive in and out data from kafka topic
    DataStream<KeyedDataPoint<Double>> dataStreamInOut = env
            .addSource(new FlinkKafkaConsumer<>(KAFKA_TOPIC_IN_OUT, new SimpleStringSchema(), KAFKA_PROPERTIES))
            .flatMap(new KeyedDataPointMap())
            .assignTimestampsAndWatermarks(new SimpleAssigner());

    // Persist in and out data in InfluxDB
    dataStreamInOut.addSink(new InfluxDBSink<>(INFLUX_DATABASE, INFLUX_MEASUREMENT));

    //Receive diff data from kafka topic
    DataStream<KeyedDataPoint<Double>> dataStreamDiff = env
            .addSource(new FlinkKafkaConsumer<>(KAFKA_TOPIC_DIFF, new SimpleStringSchema(), KAFKA_PROPERTIES))
            .flatMap(new KeyedDataPointMap())
            .assignTimestampsAndWatermarks(new SimpleAssigner());

    // Persist diff data in InfluxDB
    dataStreamDiff.addSink(new InfluxDBSink<>(INFLUX_DATABASE, INFLUX_MEASUREMENT));

    // Load CEP pattern
    final Pattern<KeyedDataPoint<Double>, ?> pattern = PatternFactory.getPattern();

    // Perform CEP on actual diffs
    CEP.pattern(dataStreamDiff, pattern)
            .select(new PatternSelector("actual"))
            .process(new AnnotationFunction());

    // Create stream of predicted values
    DataStream<KeyedDataPoint<Double>> predictedStream = dataStreamDiff
            .countWindowAll(8, 1)
            .process(new RegressionTreeModel());

    // Sink to prediction kafka topic
    predictedStream
            .map(KeyedDataPoint::marshal)
            .addSink(new FlinkKafkaProducer<>(KAFKA_BROKER, KAFKA_TOPIC_PA, new SimpleStringSchema()));

    env.execute("Data stream consumer");
  }
}

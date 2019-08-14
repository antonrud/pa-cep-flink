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

        //Receive data from kafka topic
        DataStream<KeyedDataPoint<Double>> dataStream = env
                .addSource(new FlinkKafkaConsumer<>(KAFKA_TOPIC, new SimpleStringSchema(), KAFKA_PROPERTIES))
                .flatMap(new KeyedDataPointMap())
                .assignTimestampsAndWatermarks(new SimpleAssigner());

        // Persist data in InfluxDB
        dataStream.addSink(new InfluxDBSink<>(INFLUX_DATABASE, INFLUX_MEASUREMENT));

        // Load CEP pattern
        final Pattern<KeyedDataPoint<Double>, ?> pattern = PatternFactory.getPattern();

        // Perform simple CEP
        CEP.pattern(dataStream.filter(dataPoint -> dataPoint.getKey().equals("diff")), pattern)
                .select(new PatternSelector("actual"))
                .process(new AnnotationFunction());

        // Create stream of predicted values
        DataStream<KeyedDataPoint<Double>> predictedStream = dataStream
                .filter(dataPoint -> dataPoint.getKey().equals("diff"))
                .countWindowAll(8, 1)
                .process(new RegressionTreeModel());

        // Persist prediction in InfluxDB
        predictedStream.addSink(new InfluxDBSink<>(INFLUX_DATABASE, INFLUX_PREDICTION_MEASUREMENT));

        // Perform CEP on PA model
        CEP.pattern(predictedStream, pattern)
                .select(new PatternSelector("predicted"))
                .process(new AnnotationFunction());

        env.execute("Data stream consumer");

/*
        // Issue annotations
        dataStream.keyBy(KeyedDataPoint::getKey)
                .countWindow(3, 1)
                .apply(new IncomingWindowFunction())
                .print();

        // Train model
        dataStream.keyBy(KeyedDataPoint::getKey)
                .countWindow(336)   //One week observation
                .apply(new ModelTrainerFunction());
*/
    }
}

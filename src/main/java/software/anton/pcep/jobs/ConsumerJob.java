package software.anton.pcep.jobs;

import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import software.anton.pcep.data.KeyedDataPoint;
import software.anton.pcep.functions.IncomingWindowFunction;
import software.anton.pcep.functions.ModelTrainerFunction;
import software.anton.pcep.maps.KeyedDataPointMap;
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
        DataStream<KeyedDataPoint<Integer>> dataStream = env
                .addSource(new FlinkKafkaConsumer<>(KAFKA_TOPIC, new SimpleStringSchema(), KAFKA_PROPERTIES))
                .flatMap(new KeyedDataPointMap());

        // Persist data in InfluxDB
        dataStream.addSink(new InfluxDBSink<>(INFLUX_DATABASE, INFLUX_MEASUREMENT));

        // Issue annotations
        dataStream.keyBy(KeyedDataPoint::getKey)
                .countWindow(3, 1)
                .apply(new IncomingWindowFunction())
                .print();

        // Train model
        dataStream.keyBy(KeyedDataPoint::getKey)
                .countWindow(336)   //One week observation
                .apply(new ModelTrainerFunction());


//        GrafanaAnnotator annotator = new GrafanaAnnotator(GRAFANA_DASHBOARD, GRAFANA_PANEL);
//
//        CEP.pattern(dataStream, PatternFactory.incomingPattern())
//                .select(new CepSelectFunction("Alert", "IN"))
//                .map(alert -> {
//                    annotator.sendAnnotation(alert.getStart(), alert.getEnd(), alert.getMessage(), alert.getTag());
//                    return alert;
//                })
//                .print();

        env.execute("Data consumer");
    }
}

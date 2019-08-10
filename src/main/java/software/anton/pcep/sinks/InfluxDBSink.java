package software.anton.pcep.sinks;

import static software.anton.pcep.configs.Configuration.*;

import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Point;
import software.anton.pcep.data.KeyedDataPoint;

import java.util.concurrent.TimeUnit;

/**
 * @author Anton Rudacov <anton.rudacov @ gmail.com>
 */
public class InfluxDBSink<T> extends RichSinkFunction<KeyedDataPoint<T>> {

    private InfluxDB influxDB;
    private String database;
    private String measurement;

    public InfluxDBSink(String database, String measurement) {
        this.database = database;
        this.measurement = measurement;
    }

    @Override
    public void open(Configuration parameters) throws Exception {
        super.open(parameters);

        influxDB = InfluxDBFactory.connect(INFLUX_URL, INFLUX_USER, INFLUX_PASS);
        influxDB.setDatabase(database);
        influxDB.enableBatch(1000, 100, TimeUnit.MILLISECONDS);
    }

    @Override
    public void invoke(KeyedDataPoint<T> value, Context context) throws Exception {

        Point point = Point.measurement(measurement)
                .tag("key", value.getKey())
                .time(value.getTimeStamp(), TimeUnit.MILLISECONDS)
                .addField("value", value.getValue().toString()).build();

        influxDB.write(point);
    }

    @Override
    public void close() throws Exception {
        influxDB.close();
        super.close();
    }
}

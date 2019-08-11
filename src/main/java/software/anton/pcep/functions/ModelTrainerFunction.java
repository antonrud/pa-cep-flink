package software.anton.pcep.functions;

import org.apache.flink.streaming.api.functions.windowing.WindowFunction;
import org.apache.flink.streaming.api.windowing.windows.GlobalWindow;
import org.apache.flink.util.Collector;
import software.anton.pcep.data.KeyedDataPoint;


/**
 * @author Anton Rudacov <anton.rudacov @ gmail.com>
 */
public class ModelTrainerFunction implements WindowFunction<KeyedDataPoint<Integer>, Integer, String, GlobalWindow> {

    @Override
    public void apply(String s, GlobalWindow window, Iterable<KeyedDataPoint<Integer>> input, Collector<Integer> out) throws Exception {

        // out.collect();
    }
}

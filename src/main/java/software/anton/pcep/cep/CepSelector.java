package software.anton.pcep.cep;

import org.apache.flink.cep.RichPatternSelectFunction;
import software.anton.pcep.data.AlertArea;
import software.anton.pcep.data.KeyedDataPoint;

import java.util.List;
import java.util.Map;

/**
 * @author Anton Rudacov <anton.rudacov @ gmail.com>
 */

// Not in use
public class CepSelector extends RichPatternSelectFunction<KeyedDataPoint<Integer>, AlertArea> {

    private String message;
    private String tag;

    public CepSelector(String message, String tag) {
        this.message = message;
        this.tag = tag;
    }

    @Override
    public AlertArea select(Map<String, List<KeyedDataPoint<Integer>>> pattern) throws Exception {

        long start = pattern.get("start").get(0).getTimeStamp();
        long end = pattern.get("trigger").get(0).getTimeStamp();

        return new AlertArea(start, end, message, tag);
    }
}

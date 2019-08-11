package software.anton.pcep.patterns;

import org.apache.flink.cep.pattern.Pattern;
import org.apache.flink.cep.pattern.conditions.IterativeCondition;
import org.apache.flink.cep.pattern.conditions.SimpleCondition;
import org.apache.flink.streaming.api.windowing.time.Time;
import software.anton.pcep.data.KeyedDataPoint;

import java.util.concurrent.atomic.AtomicInteger;

import static software.anton.pcep.configs.Configuration.RATE;

/**
 * @author Anton Rudacov <anton.rudacov @ gmail.com>
 */
public class PatternFactory {

    public static Pattern<KeyedDataPoint<Integer>, ?> incomingPattern() {

        return Pattern
                .<KeyedDataPoint<Integer>>begin("start")
                .oneOrMore()
                .followedBy("trigger")
                .where(new IterativeCondition<KeyedDataPoint<Integer>>() {
                    @Override
                    public boolean filter(KeyedDataPoint<Integer> value, Context<KeyedDataPoint<Integer>> ctx) throws Exception {

                        int sum = value.getValue();
                        for (KeyedDataPoint<Integer> point : ctx.getEventsForPattern("start")) {
                            sum += point.getValue();
                        }

                        return sum > 1;
                    }
                })
                .within(Time.milliseconds(RATE * 3 + RATE / 2));
    }
}

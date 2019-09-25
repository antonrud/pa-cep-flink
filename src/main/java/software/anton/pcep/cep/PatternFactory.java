package software.anton.pcep.cep;

import org.apache.flink.cep.pattern.Pattern;
import org.apache.flink.cep.pattern.conditions.IterativeCondition;
import org.apache.flink.cep.pattern.conditions.SimpleCondition;
import org.apache.flink.streaming.api.windowing.time.Time;
import software.anton.pcep.data.KeyedDataPoint;

import static software.anton.pcep.configs.Configuration.RATE;

/**
 * @author Anton Rudacov <anton.rudacov @ gmail.com>
 */
public class PatternFactory {

  public static Pattern<KeyedDataPoint<Double>, ?> getPattern() {

    return Pattern
            .<KeyedDataPoint<Double>>begin("start")
            .times(2)
            .followedBy("trigger")
            .where(new IterativeCondition<KeyedDataPoint<Double>>() {

              @Override
              public boolean filter(KeyedDataPoint<Double> value, Context<KeyedDataPoint<Double>> ctx) throws Exception {

                double sum = value.getValue();
                for (KeyedDataPoint<Double> point : ctx.getEventsForPattern("start")) {
                  sum += point.getValue();
                }

                return sum > 15;
              }
            })
            .within(Time.milliseconds(RATE * 3 + RATE / 2));
  }

  public static Pattern<KeyedDataPoint<Double>, ?> getPattern(String key) {

    return Pattern
            .<KeyedDataPoint<Double>>begin("start")
            .where(new SimpleCondition<KeyedDataPoint<Double>>() {

              @Override
              public boolean filter(KeyedDataPoint<Double> value) throws Exception {
                return value.getKey().equals(key);
              }
            })
            .times(2)
            .followedBy("trigger")
            .where(new IterativeCondition<KeyedDataPoint<Double>>() {

              @Override
              public boolean filter(KeyedDataPoint<Double> value, Context<KeyedDataPoint<Double>> ctx) throws Exception {

                if (!value.getKey().equals(key)) {
                  return false;
                }

                double sum = value.getValue();
                for (KeyedDataPoint<Double> point : ctx.getEventsForPattern("start")) {
                  sum += point.getValue();
                }

                return sum > 15;
              }
            })
            .within(Time.milliseconds(RATE * 3 + RATE / 2));
  }
}

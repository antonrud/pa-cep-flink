package software.anton.pcep.data;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

/**
 * @author Anton Rudacov <anton.rudacov @ gmail.com>
 */
public class DataPoint<T> {

    private T value;
    private long timeStamp;

    public DataPoint(T value, long timeStamp) {
        this.value = value;
        this.timeStamp = timeStamp;
    }

    public KeyedDataPoint<T> withKey(String key) {

        return new KeyedDataPoint<>(key, value, timeStamp);
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}

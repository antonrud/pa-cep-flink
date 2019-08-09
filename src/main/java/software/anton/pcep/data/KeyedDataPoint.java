package software.anton.pcep.data;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

/**
 * @author Anton Rudacov <anton.rudacov @ gmail.com>
 */
public class KeyedDataPoint<T> extends DataPoint<T> {

    private String key;

    public KeyedDataPoint(String key, T value, long timeStamp) {
        super(value, timeStamp);
        this.key = key;
    }

    public DataPoint<T> withoutKey() {
        return new DataPoint<>(this.getValue(), this.getTimeStamp());
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}

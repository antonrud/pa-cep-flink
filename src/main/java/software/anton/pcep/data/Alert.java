package software.anton.pcep.data;

/**
 * @author Anton Rudacov <anton.rudacov @ gmail.com>
 */
public class Alert {

    private long timestamp;
    private String message;
    private String tag;

    public Alert(long timestamp, String message, String tag) {
        this.timestamp = timestamp;
        this.message = message;
        this.tag = tag;
    }

    public Alert(long timestamp, String message) {
        this.timestamp = timestamp;
        this.message = message;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}

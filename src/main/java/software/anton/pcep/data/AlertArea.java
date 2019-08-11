package software.anton.pcep.data;

/**
 * @author Anton Rudacov <anton.rudacov @ gmail.com>
 */
public class AlertArea {

    private long start;
    private long end;
    private String message;
    private String tag;

    public AlertArea(long start, long end, String message, String tag) {
        this.start = start;
        this.end = end;
        this.message = message;
        this.tag = tag;
    }

    public AlertArea(long start, long end, String message) {
        this.start = start;
        this.end = end;
        this.message = message;
    }

    public long getStart() {
        return start;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public long getEnd() {
        return end;
    }

    public void setEnd(long end) {
        this.end = end;
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

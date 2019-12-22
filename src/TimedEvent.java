import java.util.EventObject;

/**
 * [TimedEvent]
 * 2019-12-19
 * @version 0.1
 * @author Kevin Qiao
 */
public class TimedEvent implements Comparable<TimedEvent> {
  private final long time;
  private final EventObject event;

  public TimedEvent(long time, EventObject event) {
    this.time = time;
    this.event = event;
  }

  public int compareTo(TimedEvent other) {
    return (int)(this.time-other.time);
  }

  public long getTime() {
    return this.time;
  }

  public EventObject getEvent() {
    return this.event;
  }
}
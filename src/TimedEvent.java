import java.util.EventObject;

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
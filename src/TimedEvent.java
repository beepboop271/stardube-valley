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

  @Override
  public String toString() {
    long time = this.time/1_000_000_000;
    return "TE"+String.format("%02d:%02d", time/60, time%60);
  }

  public int compareTo(TimedEvent other) {
    return (int)(other.time-this.time);
  }

  public long getTime() {
    return this.time;
  }

  public EventObject getEvent() {
    return this.event;
  }
}
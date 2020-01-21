import java.util.EventObject;

/**
 * [TimedEvent]
 * An event that is timed for the priority queue.
 * 2020-01-20
 * @version 0.5
 * @author Kevin Qiao
 */
public class TimedEvent implements Comparable<TimedEvent> {
  private final long TIME;
  private final EventObject EVENT;

  /**
   * [TimedEvent]
   * Constructor for a new TimedEvent.
   * @param time  The duration for this event.
   * @param event The event that this TimedEvent has.
   */
  public TimedEvent(long time, EventObject event) {
    this.TIME = time;
    this.EVENT = event;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int compareTo(TimedEvent other) {
    // typically would use this.time-other.time
    // however, compareTo must return an int, which will
    // overflow when the time difference is too high
    if (this.TIME > other.TIME) {
      return 1;
    } else if (this.TIME == other.TIME) {
      return 0;
    } else {
      return -1;
    }
  }

  /**
   * [getTime]
   * Retries the time for this event.
   * @return long, the time.
   */
  public long getTime() {
    return this.TIME;
  }

  /**
   * [getEvent]
   * Retrieves the event stored in this TimedEvent.
   * @return EventObject, the event.
   */
  public EventObject getEvent() {
    return this.EVENT;
  }
}
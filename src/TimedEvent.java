import java.util.EventObject;

/**
 * [TimedEvent]
 * An event that is timed for the priority queue.
 * 2019-12-19
 * @version 0.1
 * @author Kevin Qiao
 */
public class TimedEvent implements Comparable<TimedEvent> {
  private final long time;
  private final EventObject event;

  /**
   * [TimedEvent]
   * Constructor for a new TimedEvent.
   * @param time  The duration for this event.
   * @param event The event that this TimedEvent has.
   */
  public TimedEvent(long time, EventObject event) {
    this.time = time;
    this.event = event;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    long time = this.time/1_000_000_000;
    return "TE("+String.format("%02d:%02d:%d, %s)",
                               time/60, time%60, this.time%(1_000_000_000),
                               this.getClass().getName());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int compareTo(TimedEvent other) {
    // System.out.printf("comparing this:%s to other:%s and returning %d\n",
    //                   this.toString(), other.toString(),
    //                   (int)(this.time-other.time));
    
    // typically would use this.time-other.time
    // however, compareTo must return an int, which will
    // overflow when the time difference is too high
    if (this.time > other.time) {
      return 1;
    } else if (this.time == other.time) {
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
    return this.time;
  }

  /**
   * [getEvent]
   * Retrieves the event stored in this TimedEvent.
   * @return EventObject, the event.
   */
  public EventObject getEvent() {
    return this.event;
  }
}
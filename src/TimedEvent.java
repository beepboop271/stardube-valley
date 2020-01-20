import java.util.EventObject;

/**
 * [TimedEvent]
 * 2019-12-19
 * @version 0.1
 * @author Kevin Qiao
 */
public class TimedEvent implements Comparable<TimedEvent> { //TODO: JAVADOCS
  private final long time;
  private final EventObject event;

  public TimedEvent(long time, EventObject event) {
    this.time = time;
    this.event = event;
  }

  @Override
  public String toString() {
    long time = this.time/1_000_000_000;
    return "TE("+String.format("%02d:%02d:%d, %s)",
                               time/60, time%60, this.time%(1_000_000_000),
                               this.getClass().getName());
  }

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
    // return (int)(this.time-other.time);
  }

  public long getTime() {
    return this.time;
  }

  public EventObject getEvent() {
    return this.event;
  }
}
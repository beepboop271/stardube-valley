import java.util.EventObject;

/**
 * [CatchFishEvent]
 * An event that signals when the player tries to catch a fish.
 * 2019-12-30
 * @version 0.1
 * @author Candice Zhang
 */

@SuppressWarnings("serial")
public class CatchFishEvent extends EventObject {
  private long catchNanoTime;

  /**
   * [CatchFishEvent]
   * Constructor for a new CatchFishEvent.
   * @param rodUsed  FishingRod, the rod that has been used to cast.
   */
  public CatchFishEvent(FishingRod rodUsed) {
    super(rodUsed);
    this.catchNanoTime = System.nanoTime();
  }

  /**
   * [getRodUsed]
   * Retrieves the rod used for this event.
   * @return FishingRod, the rod that has been used to cast.
   */
  public FishingRod getRodUsed() {
    return (FishingRod)(super.getSource());
  }

  /**
   * [getCatchNanoTime]
   * Retrieves the time the event was signaled, in nanotime.
   * @return long, the nanotime the event was signaled.
   */
  public long getCatchNanoTime() {
    return this.catchNanoTime;
  }
  
}
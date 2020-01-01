import java.util.EventObject;

/**
 * [CatchFishEvent]
 * 2019-12-30
 * @version 0.1
 * @author Candice Zhang
 */

@SuppressWarnings("serial")
public class CatchFishEvent extends EventObject {
  private long catchNanoTime;

  public CatchFishEvent(FishingRod rodUsed) {
    super(rodUsed);
    this.catchNanoTime = System.nanoTime();
  }

  public FishingRod getRodUsed() {
    return (FishingRod)(super.getSource());
  }

  public long getCatchNanoTime() {
    return this.catchNanoTime;
  }
  
}
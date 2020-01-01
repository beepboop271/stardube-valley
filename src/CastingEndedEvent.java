import java.util.EventObject;

/**
 * [CastingEndedEvent]
 * 2019-12-29
 * @version 0.1
 * @author Candice Zhang
 */

@SuppressWarnings("serial")
public class CastingEndedEvent extends EventObject {
  private int meterPercentage;

  public CastingEndedEvent(FishingRod rodUsed) {
    super(rodUsed);
    this.meterPercentage = rodUsed.getCastingProgressPercentage();
  }

  public FishingRod getRodUsed() {
    return (FishingRod)(super.getSource());
  }

  public int getMeterPercentage() {
    return this.meterPercentage;
  }
  
}
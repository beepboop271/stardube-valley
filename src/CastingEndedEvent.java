import java.util.EventObject;

/**
 * [CastingEndedEvent]
 * An event that signals when a casting action has ended.
 * 2019-12-29
 * @version 0.1
 * @author Candice Zhang
 */

@SuppressWarnings("serial")
public class CastingEndedEvent extends EventObject {
  private int meterPercentage;

  /**
   * [CastingEndedEvent]
   * Constructor for a new CastingEndedEvent.
   * @param rodUsed The rod that has been used to cast.
   */
  public CastingEndedEvent(FishingRod rodUsed) {
    super(rodUsed);
    this.meterPercentage = rodUsed.getCastingProgressPercentage();
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
   * [getMeterPercentage]
   * Retrieves the measure of the casting meter, in percentage.
   * @return int, percentage of the casting meter.
   */
  public int getMeterPercentage() {
    return this.meterPercentage;
  }
}
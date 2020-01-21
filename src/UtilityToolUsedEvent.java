/**
 * [UtilityToolUsedEvent]
 * An event that signals when a utility tool was used.
 * 2019-12-23
 * @version 0.1
 * @author Kevin Qiao
 */

@SuppressWarnings("serial")
public class UtilityToolUsedEvent extends UseableUsedEvent {
  private Point locationUsed;

  /**
   * [UtilityToolUsedEvent]
   * Constructor for a new UtilityToolUsedEvent
   * @param toolUsed     The tool that created this event.
   * @param locationUsed The location where this event was used at.
   */
  public UtilityToolUsedEvent(UtilityTool toolUsed, Point locationUsed) {
    super(toolUsed);
    this.locationUsed = locationUsed;
  }

  /**
   * [getLocationUsed]
   * Retrieves the location where the tool that created this event
   * used the tool at.
   * @return Point, where this event was used at.
   */
  public Point getLocationUsed() {
    return (Point)this.locationUsed.clone();
  }
}
@SuppressWarnings("serial")
public class UtilityToolUsedEvent extends UseableUsedEvent {
  private Point locationUsed;

  public UtilityToolUsedEvent(UtilityTool toolUsed, Point locationUsed) {
    super(toolUsed);
    this.locationUsed = locationUsed;
  }

  public Point getLocationUsed() {
    return (Point)this.locationUsed.clone();
  }
}
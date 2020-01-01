@SuppressWarnings("serial")
public class ToolUsedEvent extends UseableUsedEvent {
  private Point[] locationUsed;

  public ToolUsedEvent(Tool toolUsed, Point[] locationUsed) {
    super(toolUsed);
    this.locationUsed = locationUsed;
  }

  public Point[] getLocationUsed() {
    return this.locationUsed;
  }
}
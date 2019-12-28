import java.util.EventObject;

public class UtilityUsedEvent extends EventObject {
  private Point locationUsed;

  public UtilityUsedEvent(Point locationUsed) {
    super(locationUsed);
    this.locationUsed = locationUsed;
  }

  public Point getLocationUsed() {
    return (Point)this.locationUsed.clone();
  }
}
import java.util.EventObject;

@SuppressWarnings("serial")
public class PlayerInteractEvent extends EventObject {
  private Point locationUsed;

  public PlayerInteractEvent(Point locationUsed) {
    super(locationUsed);
    this.locationUsed = locationUsed;
  }

  public Point getLocationUsed() {
    return (Point)this.locationUsed.clone();
  }
}
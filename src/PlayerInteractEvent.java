import java.util.EventObject;

@SuppressWarnings("serial")
public class PlayerInteractEvent extends EventObject {
  private Point locationUsed;
  private int selectedItemIndex;

  public PlayerInteractEvent(Point locationUsed, int selectedItemIndex) {
    super(locationUsed);
    this.selectedItemIndex = selectedItemIndex;
    this.locationUsed = locationUsed;
  }

  public Point getLocationUsed() {
    return (Point)this.locationUsed.clone();
  }

  public int getSelectedItemIndex() {
    return this.selectedItemIndex;
  }
}
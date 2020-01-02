import java.util.EventObject;

/**
 * [ComponentPlacedEvent]
 * 2019-12-26
 * @version 0.2
 * @author Joseph Wang, Candice Zhang
 */

@SuppressWarnings("serial")
public class ComponentPlacedEvent extends EventObject {
  private Point locationUsed;
  private int componentIndex;
  private TileComponent componentToPlace;

  public ComponentPlacedEvent(TileComponent component, int componentIndex, Point locationUsed) {
    super(component);
    this.componentIndex = componentIndex;
    this.locationUsed = locationUsed;
    this.componentToPlace = component;
  }

  public Point getLocationUsed() {
    return (Point)this.locationUsed.clone();
  }

  public TileComponent getComponentToPlace() {
    return this.componentToPlace;
  }

  public int getComponentIndex() {
    return this.componentIndex;
  }
}
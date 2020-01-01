import java.util.EventObject;

/**
 * [ComponentPlacedEvent]
 * 2019-12-26
 * @version 0.1
 * @author Joseph Wang
 */

@SuppressWarnings("serial")
public class ComponentPlacedEvent extends EventObject {
  private Point locationUsed;
  private TileComponent componentToPlace;

  public ComponentPlacedEvent(TileComponent component, Point locationUsed) {
    super(component); //wth is the source
    this.componentToPlace = component;
    this.locationUsed = locationUsed;
  }

  public Point getLocationUsed() {
    return (Point)this.locationUsed.clone();
  }

  public TileComponent getComponentToPlace() {
    return this.componentToPlace;
  }
}
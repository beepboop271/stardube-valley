import java.util.EventObject;

/**
 * [ComponentPlacedEvent]
 * An event that signals when a component is to be placed onto a tile.
 * 2019-12-26
 * @version 0.2
 * @author Joseph Wang, Candice Zhang
 */

@SuppressWarnings("serial")
public class ComponentPlacedEvent extends EventObject {
  private Point locationUsed;
  private int componentIndex;
  private TileComponent componentToPlace;

  /**
   * [ComponentPlacedEvent]
   * Constructor for a new ComponentPlacedEvent.
   * @author Candice Zhang, Joseph Wang
   * @param component       The TileComponent to be placed on the tile.
   * @param componentIndex  The index of an inventory where the component is located.
   * @param locationUsed    The point where the component is to be placed.
   */
  public ComponentPlacedEvent(TileComponent component, int componentIndex, Point locationUsed) {
    super(component);
    this.componentIndex = componentIndex;
    this.locationUsed = locationUsed;
    this.componentToPlace = component;
  }

  /**
   * [getLocationUsed]
   * Retrieves the location that the component is to be placed at.
   * @author Joseph Wang
   * @return Point, a clone of the point where this component is to be placed at.
   */
  public Point getLocationUsed() {
    return (Point)this.locationUsed.clone();
  }

  /**
   * [getComponentToPlace]
   * Retrieves the TileComponent to be placed.
   * @author Joseph Wang
   * @return TileComponent, the TileComponent to be placed.
   */
  public TileComponent getComponentToPlace() {
    return this.componentToPlace;
  }

  /**
   * [getComponentIndex]
   * Retrieves the index of the inventory where this component is.
   * @author Candice Zhang
   * @return int, the index of the inventory.
   */
  public int getComponentIndex() {
    return this.componentIndex;
  }
}
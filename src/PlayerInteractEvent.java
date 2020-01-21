import java.util.EventObject;

/**
 * [PlayerInteractEvent]
 * An event that signals when the player has interacted with something, whether
 * it is an item in their inventory or something on the tile in front of them.
 * 2020-01-17
 * @version 1.0
 * @author Paula Yuan, Joseph Wang, Kevin Qiao
 */

@SuppressWarnings("serial")
public class PlayerInteractEvent extends EventObject {
  private Point locationUsed;
  private int selectedItemIndex;

  /**
   * [PlayerInteractEvent]
   * Constructor for a new PlayerInteractEvent.
   * @param locationUsed      The point where the player is to interact with.
   * @param selectedItemIndex The index of the inventory with the selected 
   *                          item's (during creation of this instance) index'.
   */
  public PlayerInteractEvent(Point locationUsed, int selectedItemIndex) {
    super(locationUsed);
    this.selectedItemIndex = selectedItemIndex;
    this.locationUsed = locationUsed;
  }

  /**
   * [getLocationUsed]
   * Returns the location that is to be interacted with, if possible.
   * @return Point, the location of the tile where this is to be used.
   */
  public Point getLocationUsed() {
    return (Point)this.locationUsed.clone();
  }

  /**
   * [getSelectedItemIndex]
   * Retrieves the index of the selected item that the 
   * player had during creation of this event.
   * @return int, the index of the selected item.
   */
  public int getSelectedItemIndex() {
    return this.selectedItemIndex;
  }
}
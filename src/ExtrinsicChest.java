/**
 * [ExtrinsicChest]
 * A chest that is able to store information about this specific instance of
 * ExtrinsicChest, but uses an intrinsic component for commonly shared information
 * between chests.
 * 2020-01-07
 * @version 0.2
 * @author Joseph Wang, Candice Zhang, Kevin Qiao
 */

public class ExtrinsicChest extends ExtrinsicHarvestableComponent implements NotWalkable{
  //- IntrinsicChest is just IntrinsicHarvestableComponent
  public static int CHEST_SIZE = 36;

  private HoldableStack[] inventory;

  /**
   * [ExtrinsicChest]
   * Constructor for this ExtrinsicChest.
   */
  public ExtrinsicChest() {
    super("Chest");
    this.inventory = new HoldableStack[ExtrinsicChest.CHEST_SIZE];
  }

  /**
   * [getInventory]
   * Returns this chest's inventory.
   * @return HoldableStack[], the current inventory of this chest.
   */
  public HoldableStack[] getInventory() {
    return this.inventory;
  }

  /**
   * [canAdd]
   * Checks to see if a specific item can be added to this chest, whether by
   * adding to an existing stack or by using a new space.
   * @author Candice Zhang
   * @param item The item to add.
   * @return boolean, true if this chest can add the item, false otherwise.
   */
  public boolean canAdd(Holdable item) {
    if (item == null) {
      return false;
    }
    for (int i = 0; i < ExtrinsicChest.CHEST_SIZE; i++) {
      if (this.inventory[i] != null) {
        if (this.inventory[i].getContainedHoldable() == item) {
          return true;
        }
      }
    }
    for (int i = 0; i < ExtrinsicChest.CHEST_SIZE; i++) {
      if (this.inventory[i] == null) {
        return true;
      }
    }
    return false;
  }

  /**
   * [add]
   * Takes in items and either adds them to an existing HoldableStack or stores them.
   * in an empty spot in inventory.
   * @author Kevin Qiao, Candice Zhang, Joseph Wang
   * @param items The items that are to be added to this inventory.
   */
  public void add(HoldableStack items) {
    if (!this.canAdd(items.getContainedHoldable())) {
      return;
    }
    // if the items of the same type exists, add on to the quantity of the type
    for (int i = 0; i < ExtrinsicChest.CHEST_SIZE; ++i) {
      // can use primitive equals because all holdables are shared
      if ((this.inventory[i] != null)
            && (this.inventory[i].getContainedHoldable() == items.getContainedHoldable())) {
        this.inventory[i].addHoldables(items.getQuantity());
        return;
      }
    }
    // otherwise, store it in a new slot if there is space
    for (int i = 0; i < ExtrinsicChest.CHEST_SIZE; ++i) {
      if (this.inventory[i] == null) {
        this.inventory[i] = items;
        return;
      }
    }
  }

  /**
   * [useAtIndex]
   * Removes one item from the inventory at the specified index, effectively "using" it.
   * @author Candice Zhang
   * @param index The index of the item that is to be used.
   */
  public void useAtIndex(int index) {
    if (this.inventory[index].getQuantity() == 1) {
      this.inventory[index] = null;
    } else {
      this.inventory[index].subtractHoldables(1);
    }
  }

  /**
   * [removeAtIndex]
   * Completely removes everything at the specified index.
   * @author Candice Zhang
   * @param index The index of the item that is to be removed.
   */
  public void removeAtIndex(int index) {
    this.inventory[index] = null;
  }

  /**
   * [isEmpty]
   * Checks if the inventory is completely empty.
   * @author Joseph Wang
   * @return boolean, true if the inventory is empty and false if not.
   */
  public boolean isEmpty() {
    return (this.inventory.length == 0);
  }
}
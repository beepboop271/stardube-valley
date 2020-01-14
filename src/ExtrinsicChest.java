/**
 * [ExtrinsicChest]
 * 2020-01-07
 * @version 0.2
 * @author Joseph Wang, Candice Zhang
 */

public class ExtrinsicChest extends ExtrinsicHarvestableComponent {
  //- IntrinsicChest is just IntrinsicHarvestableComponent
  public static int CHEST_SIZE = 36;

  private HoldableStack[] inventory;

  public ExtrinsicChest() {
    super("Chest");
    this.inventory = new HoldableStack[ExtrinsicChest.CHEST_SIZE];
  }

  public HoldableStack[] getInventory() {
    return this.inventory;
  }

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

  public void useAtIndex(int index) { // maybe change the word "use", idk
    if (this.inventory[index].getQuantity() == 1) {
      this.inventory[index] = null;
    } else {
      this.inventory[index].subtractHoldables(1);
    }
  }

  public void removeAtIndex(int index) {
    this.inventory[index] = null;
  }

  public boolean isEmpty() {
    return (this.inventory.length == 0);
  }

}
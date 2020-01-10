/**
 * [ExtrinsicChest]
 * 2020-01-07
 * @version 0.1
 * @author Joseph Wang
 */

public class ExtrinsicChest extends ExtrinsicHarvestableComponent {
  //- IntrinsicChest is just IntrinsicHarvestableComponent
  private static int CHEST_SIZE = 36;

  private int chestSize;
  private HoldableStack[] inventory;

  public ExtrinsicChest() {
    super("Chest");
    
    this.chestSize = CHEST_SIZE;
    this.inventory = new HoldableStack[this.chestSize];
  }

  public HoldableStack[] getInventory() {
    return this.inventory;
  }

  public void addToChest(HoldableStack item) {
    for (int i = 0; i < this.chestSize; ++i) {
      if ((this.inventory[i] != null)
            && (this.inventory[i].getContainedHoldable() == item.getContainedHoldable())) {
        this.inventory[i].addHoldables(item.getQuantity());
        return;
      }
    }

    for (int i = 0; i < this.chestSize; ++i) {
      if (this.inventory[i] == null) {
        this.inventory[i] = item;
        return;
      }
    }
  }

  public boolean isEmpty() {
    if (this.inventory.length == 0) {
      return true;
    }
    return false;
  }
}
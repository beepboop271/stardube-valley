/**
 * [HoldableDrop]
 * 2019-12-20
 * @version 0.1
 * @author Kevin Qiao
 */
public class HoldableDrop {
  private Holdable holdableToDrop;
  private int minDropQuantity;
  private int maxDropQuantity;

  public HoldableDrop(int minDropQuantity, int maxDropQuantity,
                      String holdableToDrop) {
    this.holdableToDrop = HoldableFactory.getHoldable(holdableToDrop);
    this.minDropQuantity = minDropQuantity;
    this.maxDropQuantity = maxDropQuantity;
  }

  public HoldableStack resolveDrop(double luck) {
    int quantity;
    if (this.minDropQuantity == this.maxDropQuantity) {
      quantity = this.minDropQuantity;
    } else {
      quantity = (int)((Math.random()  // TODO: luck
                        * (this.maxDropQuantity-this.minDropQuantity+1))
                       + this.minDropQuantity);
    }
    return new HoldableStack(this.holdableToDrop, quantity);
  }
}
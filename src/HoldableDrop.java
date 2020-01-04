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
    int quantity = (int)((Math.random()*(this.maxDropQuantity-this.minDropQuantity+1))
                         + this.minDropQuantity);
    if (quantity <= 0) {
      return null;
    } else {
      return new HoldableStack(this.holdableToDrop, quantity);
    }
  }
}
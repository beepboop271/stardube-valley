/**
 * [HoldableDrop]
 * A class that can store a holdable and is indended to be used
 * as an object that can produce a minimum and maximum drop quantity
 * in a new HoldableStack.
 * 2019-12-20
 * @version 0.1
 * @author Kevin Qiao
 */
public class HoldableDrop {
  private Holdable holdableToDrop;
  private int minDropQuantity;
  private int maxDropQuantity;

  /**
   * [HoldableDrop]
   * Constructor for a new HoldableDrop
   * @param minDropQuantity The minimum amount of items that can be dropped.
   * @param maxDropQuantity The maximum amount of items that can be dropped.
   * @param holdableToDrop  The holdable to drop.
   */
  public HoldableDrop(int minDropQuantity, int maxDropQuantity,
                      String holdableToDrop) {
    this.holdableToDrop = HoldableFactory.getHoldable(holdableToDrop);
    this.minDropQuantity = minDropQuantity;
    this.maxDropQuantity = maxDropQuantity;
  }

  /**
   * [resolveDrop]
   * Taking into consider the daily luck, produces a new HoldableStack 
   * with the minimum and maximum drop quantity used during the random
   * calculation of how many drops to produce.
   * @param luck The daily luck to consider during drop calculations.
   * @return HoldableStack, a new stack of items with the random
   *         quantity calculated.
   */
  public HoldableStack resolveDrop(double luck) { 
    int quantity = (int)((Math.min(0.999, (0.3*luck)+Math.random())
                          * (this.maxDropQuantity+1-this.minDropQuantity+1))
                         + this.minDropQuantity);
    if (quantity <= 0) {
      return null;
    } else {
      return new HoldableStack(this.holdableToDrop, quantity);
    }
  }
}
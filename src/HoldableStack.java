
/**
 * [HoldableStack]
 * 2019-12-20
 * @version 0.1
 * @author Kevin Qiao
 */
public class HoldableStack {
  private Holdable containedHoldable;
  private int quantity;

  public HoldableStack(Holdable containedHoldable, int quantity) {
    this.containedHoldable = containedHoldable;
    this.quantity = quantity;
  }

  public HoldableStack(String containedHoldable, int quantity) {
    this.containedHoldable = HoldableFactory.getHoldable(containedHoldable);
    this.quantity = quantity;
  }
}
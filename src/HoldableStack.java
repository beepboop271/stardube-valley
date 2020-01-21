
/**
 * [HoldableStack]
 * A stack of one holdable with a specific quantity.
 * 2019-12-20
 * @version 0.1
 * @author Kevin Qiao
 */
public class HoldableStack {
  private Holdable containedHoldable;
  private int quantity;

  /**
   * [HoldableStack]
   * Constructor for a new HoldableStack.
   * @param containedHoldable The holdable in this HoldableStack.
   * @param quantity          The amount of holdables in this HoldableStack.
   */
  public HoldableStack(Holdable containedHoldable, int quantity) {
    this.containedHoldable = containedHoldable;
    this.quantity = quantity;
  }

  /**
   * [HoldableStack]
   * Constructor for a new HoldableStack.
   * @param containedHoldable A string with the holdable in this HoldableStack
   * @param quantity          The amount of holdables in this HoldableStack
   */
  public HoldableStack(String containedHoldable, int quantity) {
    this(HoldableFactory.getHoldable(containedHoldable), quantity);
  }

  /**
   * [getContainedHoldable]
   * Retrieves this HoldableStack's stored holdable.
   * @return Holdable, the stored holdable.
   */
  public Holdable getContainedHoldable() {
    return this.containedHoldable;
  }

  /**
   * [getQuantity]
   * Retrieves this HoldableStack's stored quantity.
   * @return int, the stored quantity. 
   */
  public int getQuantity() {
    return this.quantity;
  }

  /**
   * [setQuantity]
   * Sets this stored quantity to a new quantity.
   * @param quantity The new quantity for this HoldableStack.
   */
  public void setQuantity(int quantity) {
    this.quantity = quantity;
  }

  /**
   * [addHoldables]
   * Adds a specific amount of holdables to this quantity.
   * @param quantity The amount of holdables to add.
   */
  public void addHoldables(int quantity) {
    this.quantity += quantity;
  }

  /**
   * [subtractHoldables]
   * Subtracts a specific amount of holdable to this quantity.
   * @param quantity The amount of holdables to subtract.
   */
  public void subtractHoldables(int quantity) {
    this.quantity -= quantity;
  }
}
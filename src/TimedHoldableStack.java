/**
 * [TimedHoldableStack]
 * A holdable stack that also stores a time needed for it, used
 * during producion.
 * 2020-01-13
 * @version 0.1
 * @author Joseph Wang
 */

public class TimedHoldableStack extends HoldableStack {
  private long timeNeeded;
  /**
   * [TimedHoldableStack]
   * Constructor for a new TimedHoldableStack.
   * @param quantity       The quantity of the HoldableStack.
   * @param holdableToDrop The holdable to drop.
   * @param timeNeeded     The time needed for this production.
   */
  public TimedHoldableStack(int quantity, String holdableToDrop, long timeNeeded) {
    super(holdableToDrop, quantity);

    this.timeNeeded = timeNeeded;
  }

  /** 
   * [getTimeNeeded]
   * Gets the total amount of time this HoldableDrop needs to wait before being produced.
   * @return long, the total amount of time needed
   */
  public long getTimeNeeded() {
    return this.timeNeeded;
  }
}
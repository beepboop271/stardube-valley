/**
 * [TimedHoldableStack]
 * 2020-01-13
 * @version 0.1
 * @author Joseph Wang
 */


public class TimedHoldableStack extends HoldableStack {
  private long timeNeeded;
  public TimedHoldableStack(int quantity, String holdableToDrop, long timeNeeded) {
    super(holdableToDrop, quantity);

    this.timeNeeded = timeNeeded;
  }

  /** 
   * [getTimeNeeded]
   * Gets the total amount of time this HoldableDrop needs to wait before being produced.
   * @return a long with the total amount of time needed
   */
  public long getTimeNeeded() {
    return this.timeNeeded;
  }
}
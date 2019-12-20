public class HoldableDrop {
  private Holdable holdableToDrop;
  private int minDropQuantity;
  private int maxDropQuantity;

  public HoldableDrop(String holdableToDrop,
                      int minDropQuantity, int maxDropQuantity) {
    this.holdableToDrop = HoldableFactory.getHoldable(holdableToDrop);
    this.minDropQuantity = minDropQuantity;
    this.maxDropQuantity = maxDropQuantity;
  }
}
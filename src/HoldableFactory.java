import java.util.HashMap;

public class HoldableFactory {
  private static HashMap<String, Holdable> holdablePool;

  public static void initializeItems() {
    HoldableFactory.holdablePool = new HashMap<String, Holdable>();
  }

  public static Holdable getHoldable(String holdable) {
    return (Holdable)HoldableFactory.holdablePool.get(holdable);
  }
}
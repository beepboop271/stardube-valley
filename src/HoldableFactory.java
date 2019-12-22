import java.util.HashMap;

/**
 * [HoldableFactory]
 * 2019-12-20
 * @version 0.1
 * @author Kevin Qiao
 */
public class HoldableFactory {
  private static HashMap<String, Holdable> holdablePool;

  private HoldableFactory(){
    // do not allow anyone to create an object of this class
  }

  public static void initializeItems() {
    HoldableFactory.holdablePool = new HashMap<String, Holdable>();
  }

  public static Holdable getHoldable(String holdable) {
    return (Holdable)HoldableFactory.holdablePool.get(holdable);
  }
}
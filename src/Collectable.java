/**
 * Collectable
 * 2019-12-21
 * @version 0.1
 * @author Paula Yuan
 */
public interface Collectable {
  /**
   * [getProducts]
   * Retrieves all the products that this object can drop.
   * @return HoldableDrop[], an array with all the drops.
   */
  public HoldableDrop[] getProducts();
}
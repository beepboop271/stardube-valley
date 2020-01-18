import java.awt.image.BufferedImage;

import java.io.IOException;
/**
 * [CollectableComponent] 
 * 2019-12-20
 * @version 0.1
 * @author Kevin Qiao, Joseph Wang
 */
public class CollectableComponent extends IntrinsicTileComponent 
                                  implements Collectable, Drawable, NotWalkable {
  private final HoldableDrop[] products;

  public CollectableComponent(String name,
                              String imagePath,
                              int numProducts,
                              int[] offsets) throws IOException {
    super(name, imagePath, offsets);

    this.products = new HoldableDrop[numProducts];
  }

  public void setProduct(int i, HoldableDrop product) {
    this.products[i] = product;
  }

  @Override
  public HoldableDrop[] getProducts() {
    return this.products;
  }

  @Override
  public BufferedImage getImage() {
    return this.getImages()[0];
  }
}
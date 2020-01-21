import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * [CollectableComponent] 
 * A component that is able to be on a tile and gets a product to
 * put into a player's inventory if the player collects it.
 * 2019-12-20
 * @version 1.2
 * @author Kevin Qiao, Joseph Wang
 */
public class CollectableComponent extends IntrinsicTileComponent 
                                  implements Collectable, Drawable, NotWalkable {
  private HoldableDrop[] products;

  /**
   * [CollectableComponent]
   * Constructor for a new CollectableComponent.
   * @param name        The name of this collectable.
   * @param imagePath   The path to the images related to this collectable.
   * @param numProducts The amount of different prodcuts this collectable can drop.
   * @param offsets     The offsets, in tiles, which are considered during drawing.
   * @throws IOException
   */
  public CollectableComponent(String name,
                              String imagePath,
                              int numProducts,
                              double[] offsets) throws IOException {
    super(name, imagePath, offsets);

    this.products = new HoldableDrop[numProducts];
  }

  /**
   * [setProduct]
   * Sets a product at a specified index of this collectable's products list.
   * @param i       The index to have a product set at.
   * @param product The product to have the drop set as.
   */
  public void setProduct(int i, HoldableDrop product) {
    this.products[i] = product;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public HoldableDrop[] getProducts() {
    return this.products;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public BufferedImage getImage() {
    return this.getImages()[0];
  }
}
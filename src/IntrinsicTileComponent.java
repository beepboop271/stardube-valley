import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * [SharedTileComponent]
 * 2019-12-17
 * @version 0.1
 * @author Kevin Qiao, Paula Yuan
 */
public abstract class IntrinsicTileComponent extends TileComponent {
  private final String name;
  private final BufferedImage image;
  private final HoldableDrop[] products;

  public IntrinsicTileComponent(String name,
                                String imagePath,
                                int numProducts) throws IOException {
    this.name = name;
    this.products = new HoldableDrop[numProducts];
    this.image = ImageIO.read(new File(imagePath));
  }

  public String getName() {
    return this.name;
  }

  public BufferedImage getImage() {
    return this.image;
  }

  public HoldableDrop[] getProducts() {
    return this.products;
  }

  public void setProduct(int i, HoldableDrop product) {
    this.products[i] = product;
  }
}
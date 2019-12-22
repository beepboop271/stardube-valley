import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * [TileComponent]
 * 2019-12-17
 * @version 0.1
 * @author Kevin Qiao
 */
public abstract class TileComponent {
  private String name;
  private BufferedImage image;
  private HoldableDrop[] products;

  public TileComponent(String name, String imagePath, int numProducts) {
    this.name = name;
    this.products = new HoldableDrop[numProducts];
    try {
      this.image = ImageIO.read(new File(imagePath));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public String getName() {
    return this.name;
  }

  public HoldableDrop[] getProducts() {
    return this.products;
  }

  public void setProduct(int i, HoldableDrop product) {
    this.products[i] = product;
  }
}
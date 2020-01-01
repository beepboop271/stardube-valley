import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import javax.imageio.ImageIO;

/**
 * [IntrinsicTileComponent]
 * 2019-12-17
 * @version 0.1
 * @author Kevin Qiao, Paula Yuan, Joseph Wang
 */

public abstract class IntrinsicTileComponent extends TileComponent 
                                             implements Collectable {
  private final String name;
  private final HoldableDrop[] products;
  private final BufferedImage[] images;

  public IntrinsicTileComponent(String name,
                                String imagesPath,
                                int numProducts) throws IOException {
    this.name = name;
    this.products = new HoldableDrop[numProducts];
    
    File fileSystem = new File(imagesPath);
    String[] allFiles = fileSystem.list();
    this.images = new BufferedImage[allFiles.length];
    try {
      for (int i = 0, j = 0; i < allFiles.length; i++) {
        this.images[j] = ImageIO.read(new File(imagesPath + allFiles[i]));
        j++;
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public String getName() {
    return this.name;
  }

  public BufferedImage[] getImages() {
    return this.images;
  }

  public void setProduct(int i, HoldableDrop product) {
    this.products[i] = product;
  }

  @Override
  public HoldableDrop[] getProducts() {
    return this.products;
  }
}
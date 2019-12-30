import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.Arrays; //temp

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
    // System.out.println(imagesPath);
    String[] allFiles = fileSystem.list();
    // System.out.println(Arrays.toString(allFiles));
    this.images = new BufferedImage[allFiles.length];

    try {
      for (int i = 0, j = 0; i < allFiles.length; i++) {
        // System.out.println(imagesPath + allFiles[i]);
        this.images[j] = ImageIO.read(new File(imagesPath + allFiles[i]));
        j++;
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

    // System.out.println("Done loading " + name);
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
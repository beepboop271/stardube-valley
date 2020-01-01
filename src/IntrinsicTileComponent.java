import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import java.util.ArrayList;
import java.util.regex.Pattern;

/**
 * [SharedTileComponent]
 * 2019-12-17
 * @version 0.1
 * @author Kevin Qiao, Paula Yuan
 */

public abstract class IntrinsicTileComponent extends TileComponent 
                                             implements Collectable {
  private final String name;
  private final HoldableDrop[] products;
  private final ArrayList<BufferedImage> images;

  public IntrinsicTileComponent(String name,
                                String imagesPath,
                                int numProducts) throws IOException {
    this.name = name;
    this.products = new HoldableDrop[numProducts];
    this.images = new ArrayList<BufferedImage>();

    try {
      File fileSystem = new File(imagesPath);
      String[] allFiles = fileSystem.list;

      for (String imagePath : allFiles) {
        if (Pattern.matches(this.name + "(?!Item|Seeds|.Small).*\\.png", imagePath)) {
          images.add(ImageIO.read(new File(imagesPath + imagePath)));
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public String getName() {
    return this.name;
  }

  public ArrayList<BufferedImage> getImages() {
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
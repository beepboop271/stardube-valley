import java.awt.image.BufferedImage;

import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

import java.util.ArrayList;
import java.util.regex.Pattern;

import java.util.Arrays;
/**
 * [TileComponent]
 * 2019-12-17
 * @version 0.1
 * @author Kevin Qiao, Paula Yuan
 */
public abstract class TileComponent {
  private String name;
  private ArrayList<BufferedImage> images;
  private HoldableDrop[] products;

  public TileComponent(String name, String imagesPath, int numProducts) {
    this.name = name;
    this.products = new HoldableDrop[numProducts];
    this.images = new ArrayList<BufferedImage>();
    try {
      File fileSystem = new File(imagesPath);
      String[] allFiles = fileSystem.list();
      for (String imagePath : allFiles) {
        if (Pattern.matches(name + "(?!Item|Seeds|.Small).*\\.png", imagePath)) {
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

  public BufferedImage getImage(); {
    return this.images.get(0);
  }

  public ArrayList<BufferedImage> getImages() {
    return this.images;
  }

  public HoldableDrop[] getProducts() {
    return this.products;
  }

  public void setProduct(int i, HoldableDrop product) {
    this.products[i] = product;
  }
}
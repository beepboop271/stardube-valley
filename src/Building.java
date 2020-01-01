import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * [Building]
 * 2019-12-23
 * @version 0.1
 * @author Joseph Wang
 */

 public abstract class Building {
  private BufferedImage image;

  public Building(String imagePath) {
    try {
      this.image = ImageIO.read(new File(imagePath));
      
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public BufferedImage getImage() {
    return this.image;
  }
 }

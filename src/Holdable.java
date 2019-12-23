import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * [Holdable]
 * 2019-12-20
 * @version 0.1
 * @author Kevin Qiao, Joseph Wang
 */
public abstract class Holdable {
  private String name;
  private String description;
  private BufferedImage image;

  public Holdable(String name, String description, String imagePath) {
    this.name = name;
    this.description = description;
    try {
      this.image = ImageIO.read(new File(imagePath));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public String getName() {
    return this.name;
  }

  public String getDescription() {
    return this.description;
  }

  public BufferedImage getImage() {
    return this.image;
  }
}
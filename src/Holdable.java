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
  private final String name;
  private final String description;
  private final BufferedImage image;
  private final BufferedImage smallImage;

  public Holdable(String name, String description, String imagePath) throws IOException {
    this.name = name;
    this.description = description;
    this.image = ImageIO.read(new File(imagePath));
    this.smallImage = ImageIO.read(new File(imagePath.substring(0, imagePath.length()-3)+"Small.png"));
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

  public BufferedImage getSmallImage() {
    return this.smallImage;
  }
}
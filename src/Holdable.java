import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public abstract class Holdable {
  String name;
  String description;
  BufferedImage image;

  public Holdable(String name, String description, String imagePath) {
    this.name = name;
    this.description = description;
    try {
      this.image = ImageIO.read(new File(imagePath));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
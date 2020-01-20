import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * [Holdable]
 * Anything that can be held by the player at any point of time.
 * 2019-12-20
 * @version 0.2
 * @author Kevin Qiao, Joseph Wang, Candice Zhang
 */
public abstract class Holdable {
  private final String name;
  private final String description;
  private final BufferedImage image;
  private final BufferedImage smallImage;

  /**
   * [Holdable]
   * Constructor for a new Holdable.
   * @param name         String, the name of this holdable.
   * @param description  String, a description about this holdable.
   * @param imagePath    String, the path to the images related to this holdable.
   * @throws IOException
   */
  public Holdable(String name, String description, String imagePath) throws IOException {
    this.name = name;
    this.description = description.replace("-", " "); 
    this.image = ImageIO.read(new File(imagePath));
    this.smallImage = ImageIO.read(new File(imagePath.substring(0, imagePath.length()-3)+"Small.png"));
  }

  /**
   * [getName]
   * Retrieves the name of this holdable.
   * @return String, the name of this holdable.
   */
  public String getName() {
    return this.name;
  }

  /**
   * [getDescription]
   * Retrieves the description of this holdable.
   * @return String, the description of this holdable.
   */
  public String getDescription() {
    return this.description;
  }

  /**
   * [getImage]
   * Returns the proper image that is to be drawn for this object.
   * @return BufferedImage, the image that should be drawn.
   */
  public BufferedImage getImage() {
    return this.image;
  }

  /**
   * [getSmallImage]
   * Returns the proper small image that is to be drawn for this object for drops.
   * @return BufferedImage, the small image that should be drawn.
   */
  public BufferedImage getSmallImage() {
    return this.smallImage;
  }
}
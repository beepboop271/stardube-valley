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
  private final String NAME;
  private final String DESCRIPTION;
  private final BufferedImage IMAGE;
  private final BufferedImage SMALL_IMAGE;

  /**
   * [Holdable]
   * Constructor for a new Holdable.
   * @param name        The name of this holdable.
   * @param description A description about this holdable.
   * @param imagePath   The path to the images related to this holdable.
   * @throws IOException
   */
  public Holdable(String name, String description, String imagePath) throws IOException {
    this.NAME = name;
    this.DESCRIPTION = description.replace("-", " "); 
    this.IMAGE = ImageIO.read(new File(imagePath));
    this.SMALL_IMAGE = ImageIO.read(new File(imagePath.substring(0, imagePath.length()-3)+"Small.png"));
  }

  /**
   * [getName]
   * Retrieves the name of this holdable.
   * @return String, the name of this holdable.
   */
  public String getName() {
    return this.NAME;
  }

  /**
   * [getDescription]
   * Retrieves the description of this holdable.
   * @return String, the description of this holdable.
   */
  public String getDescription() {
    return this.DESCRIPTION;
  }

  /**
   * [getImage]
   * Returns the proper image that is to be drawn for this object.
   * @return BufferedImage, the image that should be drawn.
   */
  public BufferedImage getImage() {
    return this.IMAGE;
  }

  /**
   * [getSmallImage]
   * Returns the proper small image that is to be drawn for this object for drops.
   * @return BufferedImage, the small image that should be drawn.
   */
  public BufferedImage getSmallImage() {
    return this.SMALL_IMAGE;
  }
}
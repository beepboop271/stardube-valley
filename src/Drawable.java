import java.awt.image.BufferedImage;

/**
 * [Drawable]
 * An interface for anything object that can be drawn on the game panel.
 * 2019-12-23
 * @version 0.2
 * @author Joseph Wang
 */

public interface Drawable {
  /**
   * [getImage]
   * Returns the proper image that is to be drawn for this object.
   * @return BufferedImage, the image that should be drawn.
   */
  public BufferedImage getImage();

  /**
   * [getXOffset]
   * Gets the correct X pos offset for this image for proper drawing.
   * @return int, the amount (in tiles) for the X offset for image drawing.
   */
  public int getXOffset();

  /**
   * [getYOffset]
   * Gets the correct Y pos offset for this image for proper drawing.
   * @return int, the amount (in tiles) for the Y offset for image drawing.
   */
  public int getYOffset();
}
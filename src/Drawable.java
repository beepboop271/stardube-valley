import java.awt.image.BufferedImage;

/**
 * [Drawable]
 * 2019-12-23
 * @version 0.2
 * @author Joseph Wang
 */

public interface Drawable {
  public BufferedImage getImage();
  public int getXOffset();
  public int getYOffset();
}
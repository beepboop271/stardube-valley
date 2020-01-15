import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * [TimedStaticImage]
 * 2020-01-15
 * @version 0.1
 * @author Candice Zhang
 */

class TimedStaticImage extends TimedGraphic {
  private BufferedImage image;

  TimedStaticImage(double duration, String imagePath) {
    super(duration);
    try {
      this.image = ImageIO.read(new File(imagePath));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void draw(Graphics g, int x, int y) {
    g.drawImage(this.image, x, y, null);
  }
}
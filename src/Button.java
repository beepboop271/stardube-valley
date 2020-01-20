import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.File;
import javax.imageio.ImageIO;

/**
 * [Button]
 * A class to represent a button by its x, y position and an image.
 * 2020-01-19
 * @version 0.1
 * @author Candice Zhang
 */

class Button {
  private int x, y, w, h;
  private BufferedImage image; //TODO: JAVADOCS

  Button(int x, int y, String imagePath) throws IOException {
    this.x = x;
    this.y = y;
    this.image = ImageIO.read(new File("assets/images/"+imagePath));
    this.w = this.image.getWidth();
    this.h = this.image.getHeight();
  }

  public boolean inButton(int posX, int posY) {
    return ((posX >= this.x) && (posX <= this.x+this.w) && (posY >= this.y) && (posY <= this.y+this.h));
  }

  public int getX() {
    return this.x;
  }

  public int getY() {
    return this.y;
  }

  public int getW() {
    return this.w;
  }

  public int getH() {
    return this.h;
  }

  public BufferedImage getImage() {
    return this.image;
  }
  
}
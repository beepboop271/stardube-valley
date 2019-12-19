import java.awt.image.BufferedImage;

/**
 * [Tile]
 * 2019-12-17
 * @version 0.1
 * @author Kevin Qiao
 */
public abstract class Tile {
  private int x;
  private int y;
  private TileComponent content;

  public Tile(int x, int y) {
    this.x = x;
    this.y = y;
  }

  public int getX() {
    return this.x;
  }

  public int getY() {
    return this.y;
  }
  
  abstract BufferedImage getImage();
}
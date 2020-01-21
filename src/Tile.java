import java.awt.image.BufferedImage;

/**
 * [Tile]
 * A class for all tiles that form the maps.
 * 2019-12-22
 * @version 0.3
 * @author Kevin Qiao, Paula Yuan
 */
public abstract class Tile {
  private static final int SIZE = 64;
  private int x;
  private int y;
  private TileComponent content;

  /**
   * [Tile]
   * Constructor for a Tile.
   * @param x The x position of this tile.
   * @param y The y position of this tile.
   */
  public Tile(int x, int y) {
    this.x = x;
    this.y = y;
  }

  /**
   * [getX]
   * Retrieves the x position of this tile.
   * @return int, this tile's x position.
   */
  public int getX() {
    return this.x;
  }

  /**
   * [getY]
   * Retrieves the y position of this tile.
   * @return int, this tile's y position.
   */
  public int getY() {
    return this.y;
  }

  /**
   * [setContent]
   * Sets this tile's stored content to the specified TileComponent.
   * @param content The TileComponent to be set on this tile.
   */
  public void setContent(TileComponent content) {
    this.content = content;
  }

  /**
   * [getContent]
   * Retrieves the TileComponent stored on this tile.
   * @return TileComponent, the content on this tile.
   */
  public TileComponent getContent() {
    return this.content;
  }

  /**
   * [getSize]
   * Retrieves the size of all tiles.
   * @return int, the tile size.
   */
  public static int getSize() {
    return Tile.SIZE;
  }
  
  /**
   * [getImage]
   * Retrieves the proper image of this tile.
   * @return BufferedImage, the image of this tile.
   */
  abstract BufferedImage getImage();
}
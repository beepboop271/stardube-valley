import java.awt.image.BufferedImage;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * [BuildingArea]
 * A class for any buildings that are created in Stardube Valley.
 * 2020-1-6
 * @version 0.2
 * @author Joseph Wang
 */
public class BuildingArea extends Area implements Drawable {
  private BufferedImage interiorImage;
  private double[] offsets;
  private Point drawLocation;
  
  /**
   * [BuildingArea]
   * Constructor for a new BuildingArea.
   * @param name The name of this area.
   * @param width The tile width of this area.
   * @param height The tile height of this area.
   */
  public BuildingArea(String name, int width, int height) {
    super(name, width, height);
    this.offsets = new double[2];
  }

  /**
   * [hasInteriorImage]
   * Checks if this building area has a loaded interior image
   * to display.
   * @return true if an interior image exists, false otherwise.
   */
  public boolean hasInteriorImage() {
    return (!(interiorImage == null));
  }

  /**
   * [loadImage]
   * Attempts to load the interior image given the image path.
   * @param imagePath    The path to the interior building image.
   * @throws IOException
   */
  public void loadImage(String imagePath) throws IOException {
    try {
      File imageFile = new File(imagePath);
      this.interiorImage = ImageIO.read(imageFile);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * [setOffsets]
   * Sets the interior building offsets to a new offset pair.
   * @param offsets A pair of new offsets for the interior building image.
   */
  public void setOffsets(double[] offsets) {
    this.offsets = offsets;
  }

  /**
   * [getDrawLocation]
   * Retrieves the draw location of this interior image.
   * @return Point, a clone of the point to draw.
   */
  public Point getDrawLocation() {
    return (Point)this.drawLocation.clone();
  }

  /**
   * [setDrawLocation]
   * Takes in a new x and y and sets draw location
   * to a new point using those x and y.
   * @param x  The new x of the draw location.
   * @param y  The new y of the draw location.
   */
  public void setDrawLocation(int x, int y) {
    this.drawLocation = new Point(x, y);
  }

  /**
   * [setDrawLocation]
   * Takes in a point and sets a clone of that point
   * as this building's new interior image draw location.
   * @param point  The new point of where to draw at.
   */
  public void setDrawLocation(Point point) {
    this.drawLocation = (Point)point.clone();
  }

  /**
   * [getImage]
   * Returns the proper image to be drawn for this building's interior.
   * @return BufferedImage, the image to be drawn.
   */
  @Override
  public BufferedImage getImage() {
    return this.interiorImage;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public double getXOffset() {
    return this.offsets[0];
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public double getYOffset() {
    return this.offsets[1];
  }
}
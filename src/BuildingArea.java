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

  public boolean hasInteriorImage() {
    return (!(interiorImage == null));
  }

  public void loadImage(String imagePath) throws IOException {
    try {
      File imageFile = new File(imagePath);
      this.interiorImage = ImageIO.read(imageFile);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void setOffsets(double[] offsets) {
    this.offsets = offsets;
  }

  public Point getDrawLocation() {
    return (Point)this.drawLocation.clone();
  }

  public void setDrawLocation(int x, int y) {
    this.drawLocation = new Point(x, y);
  }

  public void setDrawLocation(Point point) {
    this.drawLocation = (Point)point.clone();
  }

  @Override
  public BufferedImage getImage() {
    return this.interiorImage;
  }

  @Override
  public double getXOffset() {
    return this.offsets[0];
  }

  @Override
  public double getYOffset() {
    return this.offsets[1];
  }
}
import java.awt.image.BufferedImage;

import java.io.IOException;

/**
 * [Building]
 * A class to represent any building that exists in Stardube Valley.
 * 2019-12-23
 * @version 0.2
 * @author Joseph Wang
 */

 public class Building extends IntrinsicTileComponent implements Drawable, NotWalkable {
  /**
   * [Building]
   * Constructor for a new Building.
   * @param name The name of the building.
   * @param imagePath The path to the building's image.
   * @param offsets The offsets (in tiles) that are considered when drawing the building.
   * @throws IOException
   */
  public Building(String name, String imagePath, double[] offsets) throws IOException {
    super(name, imagePath, offsets);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public BufferedImage getImage() {
    return this.getImages()[0];
  }
 }

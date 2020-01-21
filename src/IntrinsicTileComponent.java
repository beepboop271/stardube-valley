import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * [IntrinsicTileComponent]
 * A class for data shared between common TileComponents, like images or the name.
 * 2019-12-17
 * @version 0.3
 * @author Kevin Qiao, Paula Yuan, Joseph Wang, Candice Zhang
 */

public abstract class IntrinsicTileComponent extends TileComponent {
  private final String NAME;
  private final BufferedImage[] IMAGES;
  private final double[] OFFSETS;

  /**
   * [IntrinsicTileComponent]
   * Constructor for a new IntrinsicTileComponent.
   * @author Joseph Wang, Kevin Qiao
   * @param name       String, the name of this IntrinsicTileComponent.
   * @param imagesPath String, the path for the images of this IntrinsicTileComponent.
   * @param offsets    double[], the offsets (in tiles) that should be considered during drawing.
   * @throws IOException
   */
  public IntrinsicTileComponent(String name,
                                String imagesPath,
                                double[] offsets) throws IOException {
    this.NAME = name;
    this.OFFSETS = offsets;

    //- Load images for this component 
    File fileSystem = new File(imagesPath);
    if (fileSystem.isFile()) { //- Loads a single image
      this.IMAGES = new BufferedImage[1];
      this.IMAGES[0] = ImageIO.read(fileSystem);
    } else { //- Loads a group of images
      String[] allFiles = fileSystem.list();
      this.IMAGES = new BufferedImage[allFiles.length];
      try {
        for (int i = 0, j = 0; i < allFiles.length; i++) {
          String extension = allFiles[i].substring(allFiles[i].lastIndexOf("."));
          if (extension.equals(".png")) {
            this.IMAGES[j] = ImageIO.read(new File(imagesPath + allFiles[i]));
            j++;
          }
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  /**
   * [getName]
   * Retrieves the name of this TileComponent.
   * @return String, the name of this TileComponent.
   */
  public String getName() {
    return this.NAME;
  }

  /**
   * [getXOffset]
   * Retrives the x offset of this IntrinsicTileComponent for image-drawing purposes.
   * @return double, the x offset (in tiles) of which to consider during drawing.
   */
  public double getXOffset() {
    return this.OFFSETS[0];
  }

  /**
   * [getYOffset]
   * Retrives the y offset of this IntrinsicTileComponent for image-drawing purposes.
   * @return double, the y offset (in tiles) of which to consider during drawing.
   */
  public double getYOffset() {
    return this.OFFSETS[1];
  }

  /**
   * [getImages]
   * Retrieves all the images associated with this IntrinsicTileComponent.
   * @return BufferedImage[], all the images for this IntrinsicTileComponent.
   */
  public BufferedImage[] getImages() {
    return this.IMAGES;
  }
}
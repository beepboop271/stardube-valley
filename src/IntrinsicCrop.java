import java.io.IOException;

/**
 * [IntrinsicCrop]
 * A crop that stores common data shared between all crops of the same type.
 * 2019-12-23
 * @version 0.2
 * @author Joseph Wang
 */

public class IntrinsicCrop extends IntrinsicGrowableCollectable implements Harvestable {
  private int plantingSeason;
  
  /**
   * [IntrinsicCrop]
   * Constructor for a new IntrinsicCrop.
   * @param name The name of this crop.
   * @param imagesPath The path to the images of this crop.
   * @param requiredTool The tool required to harvest this crop.
   * @param growthData The growing data that is needed by this crop to initialize it.
   * @param offsets The image offsets (in tiles) that should be considered during drawing.
   * @param plantingSeason The season that this crop can grow in.
   * @throws IOException
   */
  public IntrinsicCrop(String name, String imagesPath, String requiredTool,
                       String[] growthData, double[] offsets, 
                       int plantingSeason) throws IOException {
    super(name, imagesPath, requiredTool, growthData, offsets);
    
    this.plantingSeason = plantingSeason;
  }

  /**
   * [getPlantingSeason]
   * Retrieves an int that represents the season that this crop can grow in.
   * @return int, the season that this crop can grow in.
   */
  public int getPlantingSeason() {
    return this.plantingSeason;
  }
}
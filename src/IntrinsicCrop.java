import java.io.IOException;

/**
 * [IntrinsicCrop]
 * 2019-12-23
 * @version 0.1
 * @author Joseph Wang
 */

public class IntrinsicCrop extends IntrinsicGrowableCollectable implements Harvestable {
  private int plantingSeason;
  
  public IntrinsicCrop(String name, String imagesPath, String requiredTool,
                       String[] growthData, int[] offsets, 
                       int plantingSeason) throws IOException {
    super(name, imagesPath, requiredTool, growthData, offsets);
    
    this.plantingSeason = plantingSeason;
  }

  public int getPlantingSeason() {
    return this.plantingSeason;
  }
}
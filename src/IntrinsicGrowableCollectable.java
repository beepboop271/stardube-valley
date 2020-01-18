import java.io.IOException;

/**
 * [IntrinsicGrowableCollectable]
 * A class for storing commonly shared data accross all growable collectable objects of
 * the same type.
 * 2020-01-16
 * @version 0.1
 * @author Paula Yuan, Joseph Wang
 */

public class IntrinsicGrowableCollectable extends CollectableComponent implements Harvestable {
  private String requiredTool;
  private int[] stageToDisplay;
  private int maxGrowthStage, regrowTime;
  
  /**
   * [IntrinsicGrowableCollectable]
   * Constructor for a new IntrinsicGrowableCollectable.
   * @author Joseph Wang
   * @param name The name of this collectable.
   * @param imagesPath The path to the images related to this collectable.
   * @param requiredTool The tool required to harvest this collectable.
   * @param growthData The data used to determine growth, like stages, etc.
   * @param offsets The offsets (in tiles) that are considered during drawing.
   * @throws IOException
   */
  public IntrinsicGrowableCollectable(String name, String imagesPath, String requiredTool,
                                     String[] growthData, int[] offsets) throws IOException {
    super(name, imagesPath, 1, offsets); // All bushes only drop 1 product, the item.
    
    int totalStages = Integer.parseInt(growthData[0]);
    this.maxGrowthStage = Integer.parseInt(growthData[1]);

    /* k is the index for stageToDisplay,
     * i is the actual stage to display, but each stage has a duration.
     * j takes in the duration from the growth data and adds i (the stage) over and over 
     * until the specified duration is over.
    */   
    this.stageToDisplay = new int[this.maxGrowthStage];

    for (int i = 0, k = 0; i < totalStages - 1; ++i) {
      for (int j = 0; j < Integer.parseInt(growthData[2 + i]); j++) {
        this.stageToDisplay[k] = i;
        k++;
      }
    }

    //- totalStages convieniently contains the last stage, which is the finished crop
    this.stageToDisplay[this.maxGrowthStage - 1] = totalStages - 1; 
    this.regrowTime = Integer.parseInt(growthData[growthData.length - 1]);

    this.requiredTool = requiredTool; 
  }

  /**
   * [getStageToDisplay]
   * Using a specified stage of plant, retrieves the proper image stage that should
   * be drawn.
   * @author Joseph Wang
   * @param stage, the growth stage that is used to find the proper image stage.
   * @return int, the index of the image stage that should be drawn.
   */
  public int getStageToDisplay(int stage) {
    return stageToDisplay[stage];
  }

  /**
   * [getMaxGrowthStage]
   * Retrieves this growable's final stage of growing.
   * @author Joseph Wang
   * @return int, the final (max) growth stage.
   */
  public int getMaxGrowthStage() {
    return this.maxGrowthStage;
  }

  /**
   * [getRegrowTime]
   * Retrieves the time it takes for this growable to regrow.
   * @author Joseph Wang
   * @return int, the regrow time of this growable.
   */
  public int getRegrowTime() {
    return this.regrowTime;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getRequiredTool() {
    return this.requiredTool;
  }
}
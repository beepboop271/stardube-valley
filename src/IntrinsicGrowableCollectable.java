import java.io.IOException;

/**
 * [IntrinsicGrowableCollectable]
 * 2020-01-16
 * @version 0.1
 * @author Paula Yuan, Joseph Wang
 */

public class IntrinsicGrowableCollectable extends CollectableComponent implements Harvestable {
  private String requiredTool;
  private int[] stageToDisplay;
  private int maxGrowthStage, regrowTime;
  
  public IntrinsicGrowableCollectable(String name, String imagesPath, String requiredTool,
                                     String[] growthData, double[] offsets) throws IOException {
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

  public int getStageToDisplay(int stage) {
    return stageToDisplay[stage];
  }

  public int getMaxGrowthStage() {
    return this.maxGrowthStage;
  }

  /**
   * @return the regrowTime
   */
  public int getRegrowTime() {
    return this.regrowTime;
  }

  @Override
  public String getRequiredTool() {
    return this.requiredTool;
  }
}
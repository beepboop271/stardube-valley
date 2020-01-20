import java.io.IOException;

/**
 * [IntrinsicTree] 
 * 2020-01-06
 * @version 0.1
 * @author Paula Yuan
 */

public class IntrinsicTree extends IntrinsicHarvestableComponent { //TODO: JAVADOCS
  private int[] stageToDisplay;
  private int maxGrowthStage;

  public IntrinsicTree(String name, String imagesPath, String requiredTool,
                       int hardness, int numProducts, String[] growthData, 
                       double[] offsets) throws IOException {                
    super(name, imagesPath, requiredTool, hardness, numProducts, offsets);
  
    int totalStages = Integer.parseInt(growthData[0]);
    this.maxGrowthStage = Integer.parseInt(growthData[1]);

    /* k is the index for stageToDisplay,
     * i is the actual stage to display, but each stage has a duration.
     * j takes in the duration from the growth data and adds i (the stage) over and over 
     * until the specified duration is over.
    */   
    this.stageToDisplay = new int[this.maxGrowthStage+1];

    for (int i = 0, k = 0; i < totalStages - 2; ++i) {
      for (int j = 0; j < Integer.parseInt(growthData[2 + i]); j++) {
        this.stageToDisplay[k] = i;
        k++;
      }
    }

    //- totalStages convieniently contains the last stage--the mature tree
    // System.out.println(totalStages-1);
    this.stageToDisplay[this.maxGrowthStage - 1] = totalStages-2;
    this.stageToDisplay[this.maxGrowthStage] = totalStages-1;
  }

  public int getStageToDisplay(int stage) {
    return stageToDisplay[stage];
  }

  public int getMaxGrowthStage() {
    return this.maxGrowthStage;
  }
  
}
import java.io.IOException;

/**
 * [IntrinsicTree] 
 * A class for shared information between trees of the same type.
 * 2020-01-06
 * @version 0.1
 * @author Paula Yuan
 */

public class IntrinsicTree extends IntrinsicHarvestableComponent {
  private int[] stageToDisplay;
  private int maxGrowthStage;

  /**
   * [IntrinsicTree]
   * Constructor for a new IntrinsicTree.
   * @param name          The name of the tree.
   * @param imagesPath    The path for the tree image.
   * @param requiredTool  The tool used to harvest this tree.
   * @param hardness      How tough this tree is.
   * @param numProducts   The amount of different products dropped by this tree.
   * @param growthData    The growth data, like stages, of this tree.
   * @param offsets       The offsets in tiles used during drawing.
   * @throws IOException
   */
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
    this.stageToDisplay[this.maxGrowthStage - 1] = totalStages-2;
    this.stageToDisplay[this.maxGrowthStage] = totalStages-1;
  }

  /**
   * [getStageToDisplay]
   * Retrieves the image index to display.
   * @param stage  The current stage of the tree.
   * @return       int, the image index to display.
   */
  public int getStageToDisplay(int stage) {
    return stageToDisplay[stage];
  }

  /**
   * [getMaxGrowthStage]
   * Retrieves the maximum growth stage of this tree.
   * @return int, the maximum growth stage.
   */
  public int getMaxGrowthStage() {
    return this.maxGrowthStage;
  }
  
}
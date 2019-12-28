import java.awt.image.BufferedImage;

public class IntrinsicCrop extends TileComponent implements Harvestable {
  private String requiredTool;
  private int[] stageToDisplay;
  private int maxGrowthStage;
  
  public IntrinsicCrop(String name, String imagesPath, String requiredTool, String[] growthData) {
    super(name, imagesPath, 1);
    
    int totalStages = Integer.parseInt(growthData[0]);
    this.maxGrowthStage = Integer.parseInt(growthData[1]);

    /* k is the index for stageToDisplay,
     * i is the actual stage to display, but each stage has a duration.
     * j takes in the duration from the growth data and adds i (the stage) over and over 
     * until the specified duration is over.
    */   
    this.stageToDisplay = new int[this.maxGrowthStage];
    for (int i = 0, k = 0; i < totalStages - 1; ++i) {
      for (int j = 0; j < Integer.parseInt(growthData[1 + i]); j++) {
        this.stageToDisplay[k] = i;
        k++;
      }
    }

    //- totalStages convieniently contains the last stage, which is the finished crop
    this.stageToDisplay[this.maxGrowthStage - 1] = totalStages; 

    this.requiredTool = requiredTool;
  }

  public int getStageToDisplay(int day) {
    return stageToDisplay[day];
  }

  @Override
  public String getRequiredTool() {
    return this.requiredTool;
  }
}
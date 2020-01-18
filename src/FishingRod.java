import java.io.IOException;

/**
 * [FishingRod]
 * 2019-12-22
 * @version 0.1
 * @author Candice Zhang
 */
public class FishingRod extends Tool {
  public static final int MAX_PROGRESS_NANOTIME = 1_500_000_000;
  public static final int MAX_CASTING_DISTANCE = 3;

  public static final int IDLING_STATUS = 0;
  public static final int CASTING_STATUS = 1;
  public static final int WAITING_STATUS = 2;

  private int currentStatus;
  private long castingBeginNanoTime;
  private WaterTile tileToFish;

  public FishingRod(String name, String description, 
                    String imagePath, int energyCost) throws IOException {
    super(name, description, imagePath, energyCost);
    this.currentStatus = FishingRod.IDLING_STATUS;
  }

  @Override
  public Point[] getUseLocation(Point tileUsed) {
    return null;
  }

  public void startCasting() {
    if(this.currentStatus == FishingRod.CASTING_STATUS){
      return;
    }
    this.currentStatus = FishingRod.CASTING_STATUS;
    this.castingBeginNanoTime = System.nanoTime();
  }

  public int getCastingProgressPercentage() {
    if(this.currentStatus != FishingRod.CASTING_STATUS){
      return 0;
    }
    return 100-(int)((Math.abs(((System.nanoTime()-this.castingBeginNanoTime) % (FishingRod.MAX_PROGRESS_NANOTIME*2.0))
                               - FishingRod.MAX_PROGRESS_NANOTIME) / FishingRod.MAX_PROGRESS_NANOTIME)*100);
  }

  public int getCurrentStatus() {
    return this.currentStatus;
  }

  public void setCurrentStatus(int status) {
    this.currentStatus = status;
  }

  public WaterTile getTileToFish() {
    return this.tileToFish;
  }
  
  public void setTileToFish(WaterTile tileToFish) {
    this.tileToFish = tileToFish;
  }
}
import java.io.IOException;

/**
 * [FishingRod]
 * A tool that is used to cast and fish on water tiles.
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

  /**
   * [FishingRod]
   * Constructor for a new FishingRod.
   * @param name         String, name of the fishing rod.
   * @param description  String, description of the fishing rod.
   * @param imagePath    String, image path of the fishing rod.
   * @param energyCost   int, energy cost for using this rod.
   * @throws IOException
   */
  public FishingRod(String name, String description, 
                    String imagePath, int energyCost) throws IOException {
    super(name, description, imagePath, energyCost);
    this.currentStatus = FishingRod.IDLING_STATUS;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Point[] getUseLocation(Point tileUsed) {
    return null;
  }

  /**
   * [startCasting]
   * Sets this fishing rod to cast and records the time.
   */
  public void startCasting() {
    if(this.currentStatus == FishingRod.CASTING_STATUS){
      return;
    }
    this.currentStatus = FishingRod.CASTING_STATUS;
    this.castingBeginNanoTime = System.nanoTime();
  }

  /**
   * [getCastingProgressPercentage]
   * Retrieves the casting progress, in percentage.
   * @return int, the casting progress in percentage.
   */
  public int getCastingProgressPercentage() {
    if(this.currentStatus != FishingRod.CASTING_STATUS){
      return 0;
    }
    return 100-(int)((Math.abs(((System.nanoTime()-this.castingBeginNanoTime) % (FishingRod.MAX_PROGRESS_NANOTIME*2.0))
                               - FishingRod.MAX_PROGRESS_NANOTIME) / FishingRod.MAX_PROGRESS_NANOTIME)*100);
  }

  /**
   * [getCurrentStatus]
   * Retrieves current status of this fishing rod.
   * @return int, the current status of this fishing rod.
   */
  public int getCurrentStatus() {
    return this.currentStatus;
  }

  /**
   * [setCurrentStatus]
   * Sets the current status of this fishing rod.
   * @param status int, the current status of this fishing rod.
   */
  public void setCurrentStatus(int status) {
    this.currentStatus = status;
  }

  /**
   * [getTileToFish]
   * Retrieves the WaterTile that is used to fish.
   * @return WaterTile, the WaterTile that is used to fish.
   */
  public WaterTile getTileToFish() {
    return this.tileToFish;
  }

  /**
   * [setTileToFish]
   * Sets the WaterTile that is used to fish.
   * @param tileToFish The WaterTile that is used to fish.
   */
  public void setTileToFish(WaterTile tileToFish) {
    this.tileToFish = tileToFish;
  }
}
import java.io.IOException;

/**
 * [FishingRod]
 * 2019-12-22
 * @version 0.1
 * @author Candice Zhang
 */
public class FishingRod extends Tool {
  public final static int MAX_PROGRESS_NANOSEC = 2_000_000_000;;

  private boolean isCasting;
  private long castingBeginNanoTime;

  public FishingRod(String name, String description, String imagePath) throws IOException {
    super(name, description, imagePath);
    this.isCasting = false;
  }

  @Override
  public Point[] getUseLocation(Point tileUsed) {
    return null;
  }

  public void startCasting() {
    if(this.isCasting == true){
      return;
    }
    this.isCasting = true;
    this.castingBeginNanoTime = System.nanoTime();
  }

  public void endCasting() {
    System.out.println(this.getCastingProgressPercentage());
    this.isCasting = false;
    // TODO: determine tile to cast, if is water place a fishing game event (should this be done by the tool itself?)
  }

  public int getCastingProgressPercentage() {
    if(this.isCasting == false){
      return 0;
    }
    return 100-(int)((Math.abs(((System.nanoTime()-this.castingBeginNanoTime) % (FishingRod.MAX_PROGRESS_NANOSEC*2.0))
                               - FishingRod.MAX_PROGRESS_NANOSEC) / FishingRod.MAX_PROGRESS_NANOSEC)*100);
  }

  public boolean isCasting() {
    return this.isCasting;
  }

}
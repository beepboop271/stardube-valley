@SuppressWarnings("serial")
public class SeedsUsedEvent extends UseableUsedEvent {
  private Point locationUsed;
  private String cropToPlant;

  public SeedsUsedEvent(Seeds seedUsed, Point locationUsed, String cropToPlant) {
    super(seedUsed);
    this.locationUsed = locationUsed;
    this.cropToPlant = cropToPlant;
  }

  public Point getLocationUsed() {
    return (Point)this.locationUsed.clone();
  }

  public String getCropToPlant() {
    return this.cropToPlant;
  }
}
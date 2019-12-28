public class Seeds extends Useable {
  private String cropToMake;
  public Seeds(String name, String description, String imagePath, String crop) {
    super(name, description, imagePath);

    this.cropToMake = crop;
  }

  public String getCropToMake() {
    return this.cropToMake;
  }
  
  public Point[] getUseLocation(Point selectedTile) {
    Point[] returnValue = {selectedTile};
    return returnValue;
  }
}
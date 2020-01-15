import java.io.IOException;

/**
 * [Seeds]
 * 2019-12-24
 * @version 0.1
 * @author Joseph Wang
 */

public class Seeds extends Useable implements Placeable {
  private String cropToMake;
  
  public Seeds(String name, String description, String imagePath, String crop) throws IOException{
    super(name, description, imagePath);

    this.cropToMake = crop;
  }

  @Override
  public TileComponent placeItem() {
    return new ExtrinsicCrop(cropToMake);
  }
  
  public Point[] getUseLocation(Point selectedTile) {
    Point[] returnValue = {selectedTile};
    return returnValue;
  }
}
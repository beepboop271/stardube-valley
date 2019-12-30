/**
 * [Seeds]
 * 2019-12-24
 * @version 0.1
 * @author Joseph Wang
 */

import java.io.IOException;

public class Seeds extends Useable {
  private String cropToMake;
  
  public Seeds(String name, String description, String imagePath, String crop) throws IOException{
    super(name, description, imagePath);

    this.cropToMake = crop;
  }

  public TileComponent createCrop() {
    return new ExtrinsicCrop(cropToMake);
  }
  
  public Point[] getUseLocation(Point selectedTile) {
    Point[] returnValue = {selectedTile};
    return returnValue;
  }
}
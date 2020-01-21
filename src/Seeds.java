import java.io.IOException;

/**
 * [Seeds]
 * A class for any seeds that need to be made.
 * 2020-01-17
 * @version 0.4
 * @author Joseph Wang
 */

public class Seeds extends Useable implements Placeable {
  private String cropToMake;
  
  /**
   * [Seeds]
   * Constructor for new seeds. Seeds will spawn a new crop once used and
   * is how crop planting works.
   * @param name        The name of the seeds package.
   * @param description A description about the seeds package.
   * @param imagePath   The image path for the seeds image.
   * @param crop        A String with the crop that is actually produced by these seeds.
   * @throws IOException
   */
  public Seeds(String name, String description, String imagePath, String crop) throws IOException{
    super(name, description, imagePath);

    this.cropToMake = crop;
  }

  /**
   * [getUseLocation]
   * Retrieves the position where these seeds were used.
   * @param selectedTile The current tile selected by the player.
   * @return Point[], an array with all the points that the seeds were used. There is always only one point.
   */
  public Point[] getUseLocation(Point selectedTile) {
    Point[] returnValue = {selectedTile};
    return returnValue;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public TileComponent placeItem() {
    return new ExtrinsicCrop(cropToMake);
  }
}
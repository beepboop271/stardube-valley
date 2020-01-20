import java.io.IOException;

/**
 * [Furniture]
 * Creates and stores any furniture created or spawned.
 * 2020-1-6
 * @version 0.1
 * @author Joseph Wang
 */

public class Furniture extends CollectableComponent implements NotWalkable {
  /**
   * [Furniture]
   * Constructor for a new Furniture.
   * @param name        The name of this furniture.
   * @param imagePath   The path to this furniture's image.
   * @param offsets     The offsets in tiles to consider during drawing.
   * @throws IOException
   */
  public Furniture(String name, String imagePath, double[] offsets) throws IOException {
    super(name, imagePath, 1, offsets);
  }
}
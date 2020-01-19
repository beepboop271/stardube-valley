import java.io.IOException;

/**
 * [Furniture]
 * Creates and stores any furniture created or spawned.
 * 2020-1-6
 * @version 0.1
 * @author Joseph Wang
 */

public class Furniture extends IntrinsicTileComponent {
  public Furniture(String name, String imagePath, double[] offsets) throws IOException {
    super(name, imagePath, offsets);
  }
}
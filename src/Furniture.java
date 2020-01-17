/**
 * [Furniture]
 * Creates and stores any furniture created or spawned.
 * 2020-1-6
 * @version 0.1
 * @author Joseph Wang
 */
//TODO: if we don't use this, destroy this file.
import java.io.IOException;
public class Furniture extends IntrinsicTileComponent {
  public Furniture(String name, String imagePath, int[] offsets) throws IOException {
    super(name, imagePath, offsets);
  }
}
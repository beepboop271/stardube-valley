import java.io.IOException;

public class Furniture extends IntrinsicTileComponent {
  public Furniture(String name, String imagePath, int[] offsets) throws IOException {
    super(name, imagePath, 1, offsets);
  }
}
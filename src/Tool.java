import java.io.IOException;

public abstract class Tool extends Useable {
  public Tool(String name, String description, String imagePath) throws IOException {
    super(name, description, imagePath);
  }

  public abstract Point[] getUseLocation(Point selectedTile);
}
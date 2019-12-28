import java.io.IOException;

public abstract class Useable extends Holdable {
  public Useable(String name, String description, String imagePath) throws IOException {
    super(name, description, imagePath);
  }
}
import java.io.IOException;

public abstract class Useable extends Holdable { //TODO: JAVADOCS
  public Useable(String name, String description, String imagePath) throws IOException {
    super(name, description, imagePath);
  }
}
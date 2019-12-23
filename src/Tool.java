public abstract class Tool extends Useable {
  public Tool(String name, String description, String imagePath) {
    super(name, description, imagePath);
  }

  public abstract Point[] getUseLocation(Point selectedTile);
}
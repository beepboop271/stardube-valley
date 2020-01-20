import java.io.IOException;

/**
 * [Useable]
 * Anything that can be used by the player to do something.
 * 2019-12-20
 * @version 0.2
 * @author Kevin Qiao
 */

public abstract class Useable extends Holdable {
  /**
   * [Useable]
   * Constructor for a new Usable.
   * @param name         The name of the useable.
   * @param description  The description of the useable.
   * @param imagePath    The path to this useable's images.
   * @throws IOException
   */
  public Useable(String name, String description, String imagePath) throws IOException {
    super(name, description, imagePath);
  }
}
import java.io.IOException;

/**
 * [CollectableComponent] 2019-12-20
 * 
 * @version 0.1
 * @author Kevin Qiao
 */
public class CollectableComponent extends IntrinsicTileComponent implements Collectable {
  public CollectableComponent(String name,
                              String imagePath,
                              int numProducts) throws IOException {
    super(name, imagePath, numProducts);
    
  }
}
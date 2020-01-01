import java.awt.image.BufferedImage;

import java.io.IOException;
/**
 * [CollectableComponent] 2019-12-20
 * 
 * @version 0.1
 * @author Kevin Qiao
 */
public class CollectableTileComponent extends IntrinsicTileComponent 
                                      implements Collectable, Drawable {
  public CollectableTileComponent(String name,
                              String imagePath,
                              int numProducts) throws IOException {
    super(name, imagePath, numProducts);
  }

  @Override
  public BufferedImage getImage() {
    return this.getImages().get(0);
  }
}
import java.awt.image.BufferedImage;

/**
 * [ExtrinsicTileComponent]
 * 2019-12-26
 * @version 0.1
 * @author Kevin Qiao, Joseph Wang
 */

public abstract class ExtrinsicTileComponent extends TileComponent 
                                            implements Drawable {
  private final IntrinsicTileComponent intrinsicSelf;

  public ExtrinsicTileComponent(IntrinsicTileComponent intrinsicSelf) {
    super();
    this.intrinsicSelf = intrinsicSelf;
  }

  public ExtrinsicTileComponent(String intrinsicSelf) {
    super();
    this.intrinsicSelf = IntrinsicTileComponentFactory.getComponent(intrinsicSelf);
  }

  public IntrinsicTileComponent getIntrinsicSelf() {
    return this.intrinsicSelf;
  }

  @Override
  public BufferedImage getImage() {
    return this.intrinsicSelf.getImages()[0];
  }
  
  @Override
  public int getXOffset() {
    return this.intrinsicSelf.getXOffset();
  }

  @Override
  public int getYOffset() {
    return this.intrinsicSelf.getYOffset();
  }
}
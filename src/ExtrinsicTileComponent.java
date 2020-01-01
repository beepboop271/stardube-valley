import java.awt.image.BufferedImage;

public abstract class ExtrinsicTileComponent extends TileComponent 
                                            implements Collectable, Drawable {
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
    return this.intrinsicSelf.getImages().get(0);
  }

  @Override
  public HoldableDrop[] getProducts() {
    return this.intrinsicSelf.getProducts();
  }
}
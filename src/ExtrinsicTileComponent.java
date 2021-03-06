import java.awt.image.BufferedImage;

/**
 * [ExtrinsicTileComponent]
 * A component that is able to be on a tile that can store individual data relevant
 * to only this tile component.
 * 2019-12-26
 * @version 0.1
 * @author Kevin Qiao, Joseph Wang
 */

public abstract class ExtrinsicTileComponent extends TileComponent 
                                             implements Drawable {
  private final IntrinsicTileComponent INTRINSIC_SELF;

  /**
   * [ExtrinsicTileComponent]
   * Constructor for a new ExtrinsicTileComponent.
   * @param intrinsicSelf An IntrinsicTileComponent that is the associated intrinsic component.
   */
  public ExtrinsicTileComponent(IntrinsicTileComponent intrinsicSelf) {
    super();
    this.INTRINSIC_SELF = intrinsicSelf;
  }

  /**
   * [ExtrinsicTileComponent]
   * Constructor for a new ExtrinsicTileComponent.
   * @param intrinsicSelf A String with the associated intrinsic component.
   */
  public ExtrinsicTileComponent(String intrinsicSelf) {
    super();
    this.INTRINSIC_SELF = IntrinsicTileComponentFactory.getComponent(intrinsicSelf);
  }

  /**
   * [getIntrinsicSelf]
   * Retrieves the intrinsic version of this TileComponent for shared data.
   * @return IntrinsicTileComponent, the shared version of this object.
   */
  public IntrinsicTileComponent getIntrinsicSelf() {
    return this.INTRINSIC_SELF;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public BufferedImage getImage() {
    return this.INTRINSIC_SELF.getImages()[0];
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public double getXOffset() {
    return this.INTRINSIC_SELF.getXOffset();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public double getYOffset() {
    return this.INTRINSIC_SELF.getYOffset();
  }
}
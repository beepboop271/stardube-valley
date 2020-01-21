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
  private final IntrinsicTileComponent intrinsicSelf;

  /**
   * [ExtrinsicTileComponent]
   * Constructor for a new ExtrinsicTileComponent.
   * @param intrinsicSelf An IntrinsicTileComponent that is the associated intrinsic component.
   */
  public ExtrinsicTileComponent(IntrinsicTileComponent intrinsicSelf) {
    super();
    this.intrinsicSelf = intrinsicSelf;
  }

  /**
   * [ExtrinsicTileComponent]
   * Constructor for a new ExtrinsicTileComponent.
   * @param intrinsicSelf A String with the associated intrinsic component.
   */
  public ExtrinsicTileComponent(String intrinsicSelf) {
    super();
    this.intrinsicSelf = IntrinsicTileComponentFactory.getComponent(intrinsicSelf);
  }

  /**
   * [getIntrinsicSelf]
   * Retrieves the intrinsic version of this TileComponent for shared data.
   * @return IntrinsicTileComponent, the shared version of this object.
   */
  public IntrinsicTileComponent getIntrinsicSelf() {
    return this.intrinsicSelf;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public BufferedImage getImage() {
    return this.intrinsicSelf.getImages()[0];
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public double getXOffset() {
    return this.intrinsicSelf.getXOffset();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public double getYOffset() {
    return this.intrinsicSelf.getYOffset();
  }
}
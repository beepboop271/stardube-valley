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
   * @author Kevin Qiao, Joseph Wang
   * @param intrinsicSelf an IntrinsicTileComponent that is the associated intrinsic component.
   */
  public ExtrinsicTileComponent(IntrinsicTileComponent intrinsicSelf) {
    super();
    this.intrinsicSelf = intrinsicSelf;
  }

  /**
   * [ExtrinsicTileComponent]
   * Constructor for a new ExtrinsicTileComponent.
   * @author Kevin Qiao, Joseph Wang
   * @param intrinsicSelf a String with the associated intrinsic component.
   */
  public ExtrinsicTileComponent(String intrinsicSelf) {
    super();
    this.intrinsicSelf = IntrinsicTileComponentFactory.getComponent(intrinsicSelf);
  }

  /**
   * [getIntrinsicSelf]
   * Retrieves the intrinsic version of this TileComponent for shared data.
   * @author Kevin Qiao
   * @return IntrinsicTileComponent, the shared version of this object.
   */
  public IntrinsicTileComponent getIntrinsicSelf() {
    return this.intrinsicSelf;
  }

  /**
   * {@inheritDoc}
   * @author Joseph Wang
   */
  @Override
  public BufferedImage getImage() {
    return this.intrinsicSelf.getImages()[0];
  }
  
  /**
   * {@inheritDoc}
   * @author Joseph Wang
   */
  @Override
  public int getXOffset() {
    return this.intrinsicSelf.getXOffset();
  }

  /**
   * {@inheritDoc}
   * @author Joseph Wang
   */
  @Override
  public int getYOffset() {
    return this.intrinsicSelf.getYOffset();
  }
}
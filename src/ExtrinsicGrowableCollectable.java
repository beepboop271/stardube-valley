import java.awt.image.BufferedImage;

/**
 * [ExtrinsicGrowableCollectable]
 * A growable collectable object that is able to store information specific
 * to this object. Uses IntrinsicGrowableCollectable for shared data.
 * 2020-01-16
 * @version 0.1
 * @author Paula Yuan, Joseph Wang
 */

public class ExtrinsicGrowableCollectable extends ExtrinsicTileComponent implements Growable, Collectable {
  private int stage, regrowCooldown;

  /**
   * [ExtrinsicGrowableCollectable]
   * Constructor for a new ExtrinsicGrowableCollectable that takes in a predetermined
   * IntrinsicGrowableCollectable and sets that as this object's IntrinsicGrowableCollectable.
   * @param item The predetermined IntrinsicGrowableCollectable which this object will set as its own.
   */
  public ExtrinsicGrowableCollectable(IntrinsicGrowableCollectable item) {
    super(item);
    this.stage = 0;
    this.regrowCooldown = 0;
  }

  /**
   * [ExtrinsicGrowableCollectable]
   * Contructor for a new ExtrinsicGrowableCollectable that takes in a string and
   * finds the IntrinsicGrowableCollectable related to that string.
   * @param item The string with the item's name.
   */
  public ExtrinsicGrowableCollectable(String item) {
    super(item);
    this.stage = 0;
    this.regrowCooldown = 0;
  }

  /**
   * [canHarvest]
   * Checks if this growable can be harvested.
   * @return boolean, true if this growable is harvestable, false otherwise.
   */
  public boolean canHarvest() {
    if (this.stage == ((IntrinsicGrowableCollectable)this.getIntrinsicSelf()).getMaxGrowthStage() - 1) {
      return true;
    }
    return false;
  }

  /**
   * [shouldRegrow]
   * Checks to see if this growable can regrow.
   * @return boolean, true if this growable is able to regrow, false otherwise.
   */
  public boolean shouldRegrow() {
    if (((IntrinsicGrowableCollectable)this.getIntrinsicSelf()).getRegrowTime() == 0) {
      return false;
    }
    return true;
  }

  /**
   * [resetRegrowCooldown]
   * Turns this growable object's regrow cooldown to its max.
   */
  public void resetRegrowCooldown() {
    regrowCooldown = ((IntrinsicGrowableCollectable)this.getIntrinsicSelf()).getRegrowTime();
  }

  /**
   * [getProduct]
   * @return HoldableDrop, the product that this growable produces.
   */
  public HoldableDrop getProduct() {
    return this.getProducts()[0];
  }

  /**
   * [getImage]
   * Retrieves this growable object's image. Dependant on whether it is growing to its
   * max stage or regrowing produce (if applicable).
   * @return BufferedImage, the current image that is associated to this growable's stage.
   */
  @Override
  public BufferedImage getImage() {
    if (regrowCooldown == 0) {
      return this.getIntrinsicSelf().getImages()[
              ((IntrinsicGrowableCollectable)this.getIntrinsicSelf()).getStageToDisplay(this.stage)];
    }

    return this.getIntrinsicSelf().getImages()[
              this.getIntrinsicSelf().getImages().length - 2]; 
              //- .length - 1 gives the full harvested image. We want .length - 2
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void grow() {
    if (this.stage < ((IntrinsicGrowableCollectable)this.getIntrinsicSelf()).getMaxGrowthStage() - 1) {
      this.stage++;
    }

    if (this.regrowCooldown > 0) {
      this.regrowCooldown--;
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public HoldableDrop[] getProducts() {
    return ((CollectableComponent)this.getIntrinsicSelf()).getProducts();
  }
}

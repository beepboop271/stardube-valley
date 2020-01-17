import java.awt.image.BufferedImage;

/**
 * [ExtrinsicGrowableCollectable]
 * 2020-01-16
 * @version 0.1
 * @author Paula Yuan, Joseph Wang
 */

public class ExtrinsicGrowableCollectable extends ExtrinsicTileComponent implements Growable, Collectable {
  private int stage, regrowCooldown;

  public ExtrinsicGrowableCollectable(IntrinsicGrowableCollectable item) {
    super(item);
    this.stage = 0;
    this.regrowCooldown = 0;
  }

  public ExtrinsicGrowableCollectable(String item) {
    super(item);
    this.stage = 0;
    this.regrowCooldown = 0;
  }

  /**
   * canHarvest()
   * @return a boolean o
   */
  public boolean canHarvest() {
    if (this.stage == ((IntrinsicGrowableCollectable)this.getIntrinsicSelf()).getMaxGrowthStage() - 1) {
      return true;
    }
    return false;
  }

  public boolean shouldRegrow() {
    if (((IntrinsicGrowableCollectable)this.getIntrinsicSelf()).getRegrowTime() == 0) {
      return false;
    }
    return true;
  }

  public void resetRegrowCooldown() {
    regrowCooldown = ((IntrinsicGrowableCollectable)this.getIntrinsicSelf()).getRegrowTime();
  }

  public HoldableDrop getProduct() {
    return this.getIntrinsicSelf().getProducts()[0];
  }

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

  @Override
  public void grow() {
    if (this.stage < ((IntrinsicGrowableCollectable)this.getIntrinsicSelf()).getMaxGrowthStage() - 1) { 
      this.stage++;
    }

    if (this.regrowCooldown > 0) {
      this.regrowCooldown--;
    }
  }

  @Override
  public HoldableDrop[] getProducts() {
    return this.getIntrinsicSelf().getProducts();
  }
}

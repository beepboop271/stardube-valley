import java.awt.image.BufferedImage;

/**
 * [ExtrinsicCrop]
 * 2019-12-23
 * @version 0.1
 * @author Joseph Wang
 */

public class ExtrinsicCrop extends ExtrinsicTileComponent implements Growable, Collectable {
  private int stage, regrowCooldown;

  public ExtrinsicCrop(IntrinsicCrop crop) {
    super(crop);
    this.stage = 0;
    this.regrowCooldown = 0;
  }

  public ExtrinsicCrop(String crop) {
    super(crop);
    this.stage = 0;
    this.regrowCooldown = 0;
  }

  /**
   * canHarvest()
   * @return a boolean o
   */
  public boolean canHarvest() {
    if (this.stage == ((IntrinsicCrop)this.getIntrinsicSelf()).getMaxGrowthStage() - 1) {
      return true;
    }
    return false;
  }

  public boolean shouldRegrow() {
    if (((IntrinsicCrop)this.getIntrinsicSelf()).getRegrowTime() == 0) {
      return false;
    }
    return true;
  }

  public void resetRegrowCooldown() {
    regrowCooldown = ((IntrinsicCrop)this.getIntrinsicSelf()).getRegrowTime();
  }

  public HoldableDrop getProduct() {
    return this.getIntrinsicSelf().getProducts()[0];
  }

  @Override
  public BufferedImage getImage() {
    if (regrowCooldown == 0) {
      return this.getIntrinsicSelf().getImages()[
              ((IntrinsicCrop)this.getIntrinsicSelf()).getStageToDisplay(this.stage)];
    }

    return this.getIntrinsicSelf().getImages()[
              this.getIntrinsicSelf().getImages().length - 2]; 
              //- .length - 1 gives the full harvested image. We want .length - 2
  }

  @Override
  public void grow() {
    if (this.stage < ((IntrinsicCrop)this.getIntrinsicSelf()).getMaxGrowthStage() - 1) { 
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
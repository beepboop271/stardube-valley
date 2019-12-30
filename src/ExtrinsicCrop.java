import java.awt.image.BufferedImage;

/**
 * [ExtrinsicCrop]
 * 2019-12-23
 * @version 0.1
 * @author Joseph Wang
 */

public class ExtrinsicCrop extends ExtrinsicTileComponent implements Growable {
  private int stage, regrowCooldown;

  //TODO: make the world harvesting work (look at methods, myself!)

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

  public boolean canHarvest() {
    if (this.stage == ((IntrinsicCrop)this.getIntrinsicSelf()).getMaxGrowthStage()) {
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
    return this.getIntrinsicSelf().getImages()[
            ((IntrinsicCrop)this.getIntrinsicSelf()).getStageToDisplay(stage)];
  }

  @Override
  public void grow() {
    if (this.stage < ((IntrinsicCrop)this.getIntrinsicSelf()).getMaxGrowthStage()) { 
      this.stage++;
    }

    if (this.regrowCooldown > 0) {
      this.regrowCooldown--;
    }
  }
}
import java.awt.image.BufferedImage;

/**
 * [ExtrinsicTree]
 * 2020-01-06
 * @version 0.1
 * @author Paula Yuan
 */

public class ExtrinsicTree extends ExtrinsicHarvestableComponent implements Growable {
  private int stage;

  public ExtrinsicTree(String tree) {
    super(tree);
    this.stage = 0;
  }

  public boolean canHarvest() {
    if (this.stage == ((IntrinsicTree)this.getIntrinsicSelf()).getMaxGrowthStage() - 1) {
      return true;
    }
    return false;
  }

  public HoldableDrop getProduct() {
    return this.getIntrinsicSelf().getProducts()[0];
  }

  @Override
  public BufferedImage getImage() {
    return this.getIntrinsicSelf().getImages()[
           ((IntrinsicTree)this.getIntrinsicSelf()).getStageToDisplay(this.stage)];
  }

  @Override
  public void grow() {
    if (this.stage < ((IntrinsicTree)this.getIntrinsicSelf()).getMaxGrowthStage() - 1) { 
      this.stage++;
    }
  }
}
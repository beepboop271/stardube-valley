import java.awt.image.BufferedImage;

/**
 * [ExtrinsicTree]
 * A tree that is able to store information seperate from other
 * tree instances but still shares some data with other trees.
 * 2020-01-06
 * @version 0.1
 * @author Paula Yuan
 */

public class ExtrinsicTree extends ExtrinsicHarvestableComponent implements Growable {
  private int stage;

  /**
   * [ExtrinsicTree]
   * Constructor for a new ExtrinsicTree.
   * @param tree The name of the tree.
   */
  public ExtrinsicTree(String tree) {
    super(tree);
    this.stage = 0;
  }

  /**
   * [getProduct]
   * Retrieves this tree's first product.
   * @return HoldableDrop, this tree's first product.
   */
  public HoldableDrop getProduct() {
    return ((CollectableComponent)this.getIntrinsicSelf()).getProducts()[0];
  }

  /**
   * [getStage]
   * Retrieves the stage this tree is at.
   * @return int, the stage this tree is at.
   */
  public int getStage() {
    return this.stage;
  }

  /**
   * [setStage]
   * Sets the stage this tree is at to a specified stage.
   * @param stage The new stage for this tree.
   */
  public void setStage(int stage) {
    this.stage = stage;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public BufferedImage getImage() {
    return this.getIntrinsicSelf()
               .getImages()[((IntrinsicTree)this.getIntrinsicSelf())
                                                .getStageToDisplay(this.stage)];
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void grow() {
    if (this.stage < ((IntrinsicTree)this.getIntrinsicSelf()).getMaxGrowthStage() - 1) { 
      this.stage++;
    }
  }
}
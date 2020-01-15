import java.awt.image.BufferedImage;

/**
 * [ExtrinsicMachine]
 * 2020-01-08
 * @version 0.1
 * @author Joseph Wang
 */

public class ExtrinsicMachine extends ExtrinsicHarvestableComponent {
  private int phase;
  private HoldableStack product;
  private String itemToProcess;

  public ExtrinsicMachine(String name) {
    super(name);
  }

  public long getProcessingTime(String item) {
    return ((IntrinsicMachine)this.getIntrinsicSelf()).getProcessingTime(item);
  }

  public int getRequiredQuantity() {
    return ((IntrinsicMachine)this.getIntrinsicSelf()).getRequiredQuantity();
  }

  public Holdable getCatalyst() {
    return ((IntrinsicMachine)this.getIntrinsicSelf()).getCatalyst();
  }

  public HoldableStack getProduct() {
    return this.product;
  }

  public void resetProduct() {
    this.product = null;
  }

  public String getItemToProcess() {
    return this.itemToProcess;
  }

  /**
   * [setItemToProcess]
   * Sets the item that must be processed.
   * @param itemToProcess the itemToProcess to set
   */
  public void setItemToProcess(String itemToProcess) {
    this.itemToProcess = itemToProcess;
  }

  public void increasePhase() {
    this.phase++;
  }

  public boolean canProcess(String item) {
    return ((IntrinsicMachine)this.getIntrinsicSelf()).canProcess(item);
  }

  public void processItem() {
    this.product = ((IntrinsicMachine)this.getIntrinsicSelf())
                                        .getProcessedItem(this.itemToProcess);
    this.itemToProcess = null;
    this.phase = 0; //- every machine should only have 2 images; the 0th image should be an empty object
  }

  @Override
  public BufferedImage getImage() {
    return this.getIntrinsicSelf().getImages()[phase];
  }
}
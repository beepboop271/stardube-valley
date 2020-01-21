import java.awt.image.BufferedImage;

/**
 * [ExtrinsicMachine]
 * A machine that is able to store specific information related to this object,
 * like what is being produced. Uses IntrinsicMachine for information shared between
 * machines of the same type.
 * 2020-01-08
 * @version 0.1
 * @author Joseph Wang
 */

public class ExtrinsicMachine extends ExtrinsicHarvestableComponent implements NotWalkable {
  private int phase;
  private HoldableStack product;
  private String itemToProcess;

  /**
   * [ExtrinsicMachine]
   * Constructor for a new ExtrinsicMachine that takes a string with the name
   * and finds the IntrinsicMachine with the related name.
   * @param name The name of this machine.
   */
  public ExtrinsicMachine(String name) {
    super(name);
  }

  /**
   * [getProcessingTime]
   * Retrieves the processing time required for the specified item.
   * @param item The item to check production time with.
   * @return long, the time to process the item in nanoseconds.
   */
  public long getProcessingTime(String item) {
    return ((IntrinsicMachine)this.getIntrinsicSelf()).getProcessingTime(item);
  }

  /**
   * [getRequiredQuantity]
   * Retrieves the required amount of an item in order to start processing.
   * @return int, the quantity of an item needed.
   */
  public int getRequiredQuantity() {
    return ((IntrinsicMachine)this.getIntrinsicSelf()).getRequiredQuantity();
  }

  /**
   * [getCatalyst]
   * Retrieves the catalyst needed for processing.
   * @return Holdable, the catalyst needed.
   */
  public Holdable getCatalyst() {
    return ((IntrinsicMachine)this.getIntrinsicSelf()).getCatalyst();
  }

  /**
   * [getProduct]
   * Retrieves the finished processed product from this machine.
   * @return HoldableStack, the finished product.
   */
  public HoldableStack getProduct() {
    return this.product;
  }

  /**
   * [resetProduct]
   * Resets the finished product (ie. turn it to null).
   */
  public void resetProduct() {
    this.product = null;
  }

  /**
   * [getItemToProcess]
   * Retrieves the item that is to be processed.
   * @return String, the name of the item that is to be processed
   */
  public String getItemToProcess() {
    return this.itemToProcess;
  }

  /**
   * [setItemToProcess]
   * Sets the item that must be processed.
   * @param itemToProcess the item to be processed.
   */
  public void setItemToProcess(String itemToProcess) {
    this.itemToProcess = itemToProcess;
  }

  /**
   * [increasePhase]
   * Increases the image phase of this machine. Only applicable on machines with multiple image phases.
   */
  public void increasePhase() {
    this.phase = (this.phase + 1) % this.getIntrinsicSelf().getImages().length;
  }

  /**
   * [canProcess]
   * Checks to see whether this machine can process the specified item.
   * @param item The item that is to be checked.
   * @return boolean, true if this machine can process it and false otherwise.
   */
  public boolean canProcess(String item) {
    return ((IntrinsicMachine)this.getIntrinsicSelf()).canProcess(item);
  }

  /**
   * [processItem]
   * Using the item to be generated, finds the finished processed item and sets it at
   * the product. Also resets image and the item to be generated.
   */
  public void processItem() {
    this.product = ((IntrinsicMachine)this.getIntrinsicSelf())
                                          .getProcessedItem(this.itemToProcess);
    this.itemToProcess = null;
    this.phase = 0; //- for every machine, the 0th image should be for a finished product.
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public BufferedImage getImage() {
    return this.getIntrinsicSelf().getImages()[phase];
  }
}
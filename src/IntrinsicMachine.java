import java.io.IOException;

import java.util.HashMap;

/**
 * [IntrinsicMachine]
 * A class for data that can be shared between the same type of machine.
 * 2020-01-13
 * @version 0.2
 * @author Joseph Wang
 */

public class IntrinsicMachine extends IntrinsicHarvestableComponent {
  private HashMap<String, TimedHoldableStack> processableItems;
  private int requiredItemQuantity;
  private Holdable catalyst;

  /**
   * [IntrinsicMachine]
   * Constructor for a new IntrinsicMachine.
   * @param name                  The name of this machine.
   * @param imagePath             The path for the images related to this machine.
   * @param offsets               The offsets (in tiles) of which to be considered
   *                              during the drawing of this image.
   * @param processableItems      An array of all the items that can be process
   *                              by this type of machine.
   * @param totalProcessableItems How many items that this machine can process.
   * @param requiredItemQuantity  How many of said items must be used for processing.
   * @param catalyst              A secondary item that is used to process. Only
   *                              uses 1 of this. If none, is "None".
   * @throws IOException
   */
  public IntrinsicMachine(String name, String imagePath, double[] offsets,
                          String[] processableItems, int totalProcessableItems,
                          int requiredItemQuantity, String catalyst) throws IOException {
    super(name, imagePath, "Pickaxe", 3, 1, offsets);
    
    this.processableItems = new HashMap<String, TimedHoldableStack>();
    for (int i = 0; i < totalProcessableItems; ++i) {
      long time = Long.parseLong(processableItems[1+3*i]) * 1_000_000_000L;
      this.processableItems.put(processableItems[3*i], 
                              new TimedHoldableStack(1, processableItems[2+3*i], time));
    }

    this.requiredItemQuantity = requiredItemQuantity;
    if (!(catalyst.equals("None"))) {
      this.catalyst = HoldableFactory.getHoldable(catalyst);
    } //- catalysts are always 1 so we don't need to specify that
  }

  /**
   * [getProcessedItem]
   * Retrueves the processed item procuced by the provided item.
   * @param item The item that can be processed.
   * @return TimedHoldableStack, the final proccessed item.
   */
  public TimedHoldableStack getProcessedItem(String item) {
    return this.processableItems.get(item);
  }

  /**
   * [getProcessingTime]
   * Retrieves the time it will take to process the specified item.
   * @param item The item to check production time with.
   * @return long, the total time in nanoseconds that production will take.
   */
  public long getProcessingTime(String item) {
    return this.processableItems.get(item).getTimeNeeded();
  }

  /**
   * [getCatalyst]
   * Retrieves the catalyst needed for processing.
   * @return Holdable, the catalyst needed.
   */
  public Holdable getCatalyst() {
    return this.catalyst;
  }

  /**
   * [getRequiredQuantity]
   * Retrieves the required amount of an item in order to start processing.
   * @return int, the quantity of an item needed.
   */
  public int getRequiredQuantity() {
    return this.requiredItemQuantity;
  }

  /**
   * [canProcess]
   * Checks to see whether this machine can process the specified item.
   * @param item The item that is to be checked.
   * @return boolean, true if this machine can process it and false otherwise.
   */
  public boolean canProcess(String item) {
    return this.processableItems.containsKey(item);
  }
}
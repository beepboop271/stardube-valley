import java.io.IOException;

import java.util.HashMap;

/**
 * [IntrinsicMachine]
 * 2020-01-13
 * @version 0.1
 * @author Joseph Wang
 */

public class IntrinsicMachine extends IntrinsicHarvestableComponent {
  private HashMap<String, TimedHoldableStack> processableItems;
  private int requiredItemQuantity;

  public IntrinsicMachine(String name, String imagePath, int[] offsets,
                          String[] processableItems, int totalProcessableItems,
                          int requiredItemQuantity) throws IOException {
    super(name, imagePath, "Pickaxe", 3, 1, offsets);
    
    this.processableItems = new HashMap<String, TimedHoldableStack>();
    for (int i = 0; i < totalProcessableItems; ++i) {
      this.processableItems.put(processableItems[3*i], 
                              new TimedHoldableStack(1, processableItems[2+3*i], 
                                        Integer.parseInt(processableItems[1+3*i]) * 1_000_000_000));
    }
  }

  public TimedHoldableStack getProcessedItem(String item) {
    return this.processableItems.get(item);
  }

  public long getProcessingTime(String item) {
    return this.processableItems.get(item).getTimeNeeded();
  }

  public boolean canProcess(String item) {
    return this.processableItems.containsKey(item);
  }

  public int getRequiredQuantity() {
    return this.requiredItemQuantity;
  }
}
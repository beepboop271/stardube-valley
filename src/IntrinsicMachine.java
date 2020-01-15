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
  private Holdable catalyst;

  public IntrinsicMachine(String name, String imagePath, int[] offsets,
                          String[] processableItems, int totalProcessableItems,
                          int requiredItemQuantity, String catalyst) throws IOException {
    super(name, imagePath, "Pickaxe", 3, 1, offsets);
    
    this.processableItems = new HashMap<String, TimedHoldableStack>();
    for (int i = 0; i < totalProcessableItems; ++i) {
      long time = Long.parseLong(processableItems[1+3*i]) * 1_000_000_000L; //TODO: make items not instantly create stuff
      this.processableItems.put(processableItems[3*i], 
                              new TimedHoldableStack(1, processableItems[2+3*i], time));
    }

    this.requiredItemQuantity = requiredItemQuantity;
    if (!(catalyst.equals("None"))) { //- you might wanna redo this? no clue
      this.catalyst = HoldableFactory.getHoldable(catalyst);
    } //- catalysts are always 1 so we don't need to specify that
  }

  public TimedHoldableStack getProcessedItem(String item) {
    return this.processableItems.get(item);
  }

  public long getProcessingTime(String item) {
    return this.processableItems.get(item).getTimeNeeded();
  }

  public Holdable getCatalyst() {
    return this.catalyst;
  }

  public int getRequiredQuantity() {
    return this.requiredItemQuantity;
  }

  public boolean canProcess(String item) {
    return this.processableItems.containsKey(item);
  }
}
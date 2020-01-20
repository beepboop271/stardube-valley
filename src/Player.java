import java.io.IOException;

/**
 * [Player]
 * 2019-12-19
 * @version 0.4
 * @author Kevin Qiao, Candice Zhang, Joseph Wang, Paula Yuan
 */

public class Player extends Moveable {
  public static final double SIZE = 0.35;
  private static final double MAX_SPEED = 6;
  private static final double ITEM_ATTRACTION_DISTANCE = 2;

  public static final int NO_MENU = -1;
  public static final int INVENTORY_PAGE = 0;
  public static final int CRAFTING_PAGE = 1;
  public static final int MAP_PAGE = 2;
  public static final int SKILLS_PAGE = 3;
  public static final int SOCIAL_PAGE = 4;
  public static final int SHOP_PAGE = 5;
  public static final int CHEST_PAGE = 6;
  public static final int ELEVATOR_PAGE = 7;

  private int inventorySize = 12;
  private HoldableStack[] inventory;
  private int selectedItemIdx;
  private Point selectedTile;
  private boolean isImmutable;
  private boolean isExhausted;
  private FishingGame currentFishingGame;
  private int currentFunds, futureFunds, totalEarnings;
  private int health, maxHealth;
  private int energy, maxEnergy;
  private int currentMenuPage;
  private int amountScrolled;
  private Object currentInteractingMenuObj; // TOOD: rename, if possible :))
  private CraftingMachine craftingMachine;

  public Player(Point position, String filePath, String name) throws IOException {
    super(position, Player.SIZE, filePath, name);
    this.inventory = new HoldableStack[this.inventorySize];
    this.selectedItemIdx = 0;
    this.isImmutable = false;
    this.isExhausted = false;
    this.health = 100;
    this.maxHealth = 100;
    this.energy = 270;
    this.maxEnergy = 270;
    this.currentMenuPage = Player.NO_MENU;
    this.currentInteractingMenuObj = null;
    this.currentFunds = 5_000_000;
    this.totalEarnings = this.currentFunds;
    this.craftingMachine = new CraftingMachine("CraftingRecipes");

    this.inventory[0] = new HoldableStack("Pickaxe", 1);
    this.inventory[1] = new HoldableStack("Axe", 1);
    this.inventory[2] = new HoldableStack("Hoe", 1);
    this.inventory[3] = new HoldableStack("WateringCan", 1);
    this.inventory[4] = new HoldableStack("BambooRod", 1);
    this.inventory[5] = new HoldableStack("ChestItem", 5);
    this.inventory[6] = new HoldableStack("FurnaceItem", 1);
    this.inventory[7] = new HoldableStack("WoodItem", 99);
  }

  @Override
  public Vector2D getMove(long elapsedNanoTime) {
    if (this.isImmutable()) {
      return null;
    }
    double elapsedSeconds = elapsedNanoTime/1_000_000_000.0;
    Vector2D positionChange = this.getVelocity();
    positionChange.setLength(Player.MAX_SPEED*elapsedSeconds);
    // this.translatePos(positionChange);
    return positionChange;
  }

  /**
   * [getInventory]
   * Retrieves this player's inventory.
   * @return HoldableStack, this player's inventory.
   */
  public HoldableStack[] getInventory() {
    return this.inventory;
  }

  public void pickUp(HoldableStack items) {
    // could replace with set, just need to test out
    // hashing holdablestacks later
    if (!this.canPickUp(items.getContainedHoldable())) {
      return;
    }
    // if the items of the same type exists, add on to the quantity of the type
    for (int i = 0; i < this.inventorySize; ++i) {
      // can use primitive equals because all holdables are shared
      if ((this.inventory[i] != null)
            && (this.inventory[i].getContainedHoldable() == items.getContainedHoldable())) {
        this.inventory[i].addHoldables(items.getQuantity());
        return;
      }
    }
    // otherwise, store it in a new slot if there is space
    for (int i = 0; i < this.inventorySize; ++i) {
      if (this.inventory[i] == null) {
        this.inventory[i] = items;
        return;
      }
    }
  }

  public boolean canPickUp(Holdable item) {
    if (item == null) {
      return false;
    }
    for (int i = 0; i < this.inventorySize; ++i) {
      if (this.inventory[i] != null) {
        if (this.inventory[i].getContainedHoldable() == item) {
          return true;
        }
      }
    }
    for (int i = 0; i < this.inventorySize; ++i) {
      if (this.inventory[i] == null) {
        return true;
      }
    }
    return false;
  }

  public void consume() {
    if (this.inventory[this.selectedItemIdx] == null) {
      return;
    }
    if (!(this.inventory[this.selectedItemIdx].getContainedHoldable() instanceof Consumable)) {
      return;
    }
    Consumable thingConsumed = (Consumable)(this.inventory[this.selectedItemIdx].getContainedHoldable());
    this.useAtIndex(this.selectedItemIdx);
    if (thingConsumed instanceof SpecialConsumable) { //TODO: maybe make this better
      this.increaseMaxHealth(((SpecialConsumable)thingConsumed).getMaxHealthGain());
      this.increaseMaxEnergy(((SpecialConsumable)thingConsumed).getMaxEnergyGain());
    }
    
    this.increaseEnergy(thingConsumed.getEnergyGain());
    this.increaseHealth(thingConsumed.getHealthGain());
  }

  public void purchase(Shop shop, String itemName) {
    if ((this.currentFunds < shop.getPriceOf(itemName)) || !(this.canPickUp(HoldableFactory.getHoldable(itemName)))) {
      return;
    }
    this.currentFunds -= shop.getPriceOf(itemName);
    this.pickUp(new HoldableStack(HoldableFactory.getHoldable(itemName), 1));
  }

  public boolean canCraft(String product) {
    if (!(this.canPickUp(HoldableFactory.getHoldable(product)))) {
      return false;
    }
    if (!(this.craftingMachine.hasProduct(product))) {
      return false;
    }

    Recipe recipe = this.craftingMachine.recipeOf(product);
    String[] ingredients = recipe.getIngredients();
    for (int i = 0; i < ingredients.length; i++) {
      if (this.quantityOf(HoldableFactory.getHoldable(ingredients[i])) < recipe.quantityOf(ingredients[i])) {
        return false;
      }
    }
    return true;
  }

  public void craft(String product) {
    if (!(this.canCraft(product))) {
      return;
    }
    Recipe recipe = this.craftingMachine.recipeOf(product);
    String[] ingredients = recipe.getIngredients();
    for (int i = 0; i < ingredients.length; i++) {
      this.decrementHoldable(recipe.quantityOf(ingredients[i]), HoldableFactory.getHoldable(ingredients[i]));
    }
    this.pickUp(new HoldableStack(HoldableFactory.getHoldable(product), 1));
  }
  public int quantityOf(Holdable item) {
    for (int i = 0; i < this.inventorySize; ++i) {
      if (this.inventory[i] != null) {
        if (this.inventory[i].getContainedHoldable() == item) {
          return this.inventory[i].getQuantity();
        }
      }
    }
    return 0;
  }

  /**
   * [useAtIndex]
   * Takes the item at the specified index in the player's inventory and decreases
   * its quantity by 1. If it only has one, remove the item by setting it to null.
   * @author Candice Zhang, Kevin Qiao
   * @param index The index of the item to be used.
   */
  public void useAtIndex(int index) {
    if (this.inventory[index].getQuantity() == 1) {
      this.inventory[index] = null;
    } else {
      this.inventory[index].subtractHoldables(1);
    }
  }

  /**
   * [decrementAtIndex]
   * Takes the item at the specified index and decreases its quantity by a specified amount.
   * If the item at the index is less than the amount decreased, it will set the inventory index as
   * null, effectively removing the item.
   * @author Joseph Wang
   * @param index The index of the item of which to decrease quantity of.
   * @param amount The amount that will be decreased.
   */
  public void decrementAtIndex(int index, int amount) {
    if (this.inventory[index].getQuantity() <= amount) {
      this.inventory[index] = null;
    } else {
      this.inventory[index].subtractHoldables(amount);
    }
  }

  /**
   * [removeAtIndex]
   * Removes the item at the specified index (ie. set it to null).
   * @author Candice Zhang
   * @param index The index of the item to remove.
   */
  public void removeAtIndex(int index) {
    this.inventory[index] = null;
  }

  /**
   * [hasAtIndex]
   * Checks to see if a spot in the inventory at the selected index has an item (ie. is not null).
   * @author Candice Zhang
   * @param index The index to check for an item.
   * @return boolean, true if there is something at the index specified and false if it is null.
   */
  public boolean hasAtIndex(int index) {
    return !(this.inventory[index] == null);
  }

  /**
   * [getAtIndex]
   * Retrieves the item at the specified index in this player's inventory.
   * @author Joseph Wang
   * @param index The index of the item to get.
   * @return HoldableStack, the item at the specified index.
   */
  public HoldableStack getAtIndex(int index) {
    return this.inventory[index];
  }

  /**
   * [hasHoldable]
   * Checks to see if the player already has a specified holdable in their inventory.
   * @author Joseph Wang
   * @param item The specified holdable that is being compared.
   * @return boolean, true if this player's inventory has this holdable already, false otherwise.
   */
  public boolean hasHoldable(Holdable item) {
    for (int i = 0; i < this.inventorySize; ++i) {
      if (this.inventory[i] != null) {
        if (this.inventory[i].getContainedHoldable() == item) {
          return true;
        }
      }
    }
    return false;
  }

  public void setCurrentFishingGame(FishingGame fishingGame){
    this.currentFishingGame = fishingGame;
  }

  public void endCurrentFishingGame() {
    this.currentFishingGame = null;
  }

  public FishingGame getCurrentFishingGame(){
    return this.currentFishingGame;
  }

  public Point getSelectedTile() {
    if (this.selectedTile == null) {
      return null;
    }
    return (Point)this.selectedTile.clone();
  }

  public void setSelectedTile(Point selectedTile) {
    if (this.isImmutable()) {
      return;
    }
    this.selectedTile = selectedTile;
  }

  public boolean isInMenu() {
    return !(this.currentMenuPage == Player.NO_MENU);
  }

  public void enterMenu(int menuPage) {
    this.isImmutable = true;
    this.currentMenuPage = menuPage;
    this.amountScrolled = 0;
  }

  public void exitMenu() {
    this.isImmutable = false;
    this.currentMenuPage = Player.NO_MENU;
    this.amountScrolled = 0;
    this.currentInteractingMenuObj = null;
  }

  public int getCurrentMenuPage() {
    return this.currentMenuPage;
  }

  public int getAmountScrolled() {
    return this.amountScrolled;
  }

  public void incrementAmountScrolled() {
    if ((this.currentMenuPage == Player.SHOP_PAGE) &&
        ((this.amountScrolled+WorldPanel.SHOP_ITEMS_PER_PAGE) < ((Shop)this.currentInteractingMenuObj).getItems().length)) {
      this.amountScrolled += 1;
    } else if ((this.currentMenuPage == Player.CRAFTING_PAGE) &&
              ((this.amountScrolled+WorldPanel.CRAFTING_ITEMS_PER_PAGE) < this.craftingMachine.getProducts().length)) {
      this.amountScrolled += 1;
    }
  }

  public void decrementAmountScrolled() {
    if ((this.currentMenuPage == Player.SHOP_PAGE) && (this.amountScrolled > 0)) {
      this.amountScrolled -= 1;
    } else if ((this.currentMenuPage == Player.CRAFTING_PAGE) && (this.amountScrolled > 0)) {
      this.amountScrolled -= 1;
    }
  }

  /**
   * [getSelectedItemIdx]
   * Retrieves the index number of the current selected item.
   * @author Kevin Qiao
   * @return int, the index number of the selected item.
   */
  public int getSelectedItemIdx() {
    return this.selectedItemIdx;
  }

  /**
   * [getSelectedItem]
   * Retrieves the current selected item using the selected item index.
   * @author Kevin Qiao
   * @return HoldableStack, the current selected item.
   */
  public HoldableStack getSelectedItem() {
    return this.inventory[this.selectedItemIdx];
  }

  public void decrementSelectedItem(int amount) {
    this.inventory[this.selectedItemIdx].subtractHoldables(amount);

    if (this.inventory[this.selectedItemIdx].getQuantity() <= 0) {
      this.inventory[this.selectedItemIdx] = null;
    }
  }

  public void decrementHoldable(int amount, Holdable item) {
    for (int i = 0; i < this.inventorySize; ++i) {
      if (this.inventory[i] != null) {
        if (this.inventory[i].getContainedHoldable() == item) {
          if (this.inventory[i].getQuantity() <= 1) {
            this.inventory[i] = null;
          } else {
            this.inventory[i].subtractHoldables(amount);
          }
        }
      }
    }
  }

  public void incrementSelectedItemIdx() {
    if (this.isImmutable() && !(this.isInMenu())) {
      return;
    }
    this.selectedItemIdx = (this.selectedItemIdx+1)%12;
  }

  public void decrementSelectedItemIdx() {
    if (this.isImmutable() && !(this.isInMenu())) {
      return;
    }
    this.selectedItemIdx = Math.floorMod(this.selectedItemIdx-1, 12);
  }

  public void setSelectedItemIdx(int selectedItemIdx) {
    if (this.isImmutable() && !(this.isInMenu())) {
      return;
    }
    this.selectedItemIdx = selectedItemIdx;
  }

  /**
   * [isImmutable]
   * Checks if this player is currently in its immutable state (ie. cannot move or change items).
   * @author Candice Zhang
   * @return boolean, true if is immutable and false otherwise.
   */
  public boolean isImmutable() {
    return this.isImmutable;
  }

  /**
   * [setImmutable]
   * Sets the immutable state of this player to either true or false.
   * @author Candice Zhang
   * @param isImmutable Whether or not the player is immutable.
   */
  public void setImmutable(boolean isImmutable){
    this.isImmutable = isImmutable;
  }

  public static double getItemAttractionDistance() {
    return Player.ITEM_ATTRACTION_DISTANCE;
  }

  public boolean isInFishingGame() {
    return (this.currentFishingGame != null);
  }

  /**
   * [getHealth]
   * Retrieves this player's current health.
   * @author Candice Zhang
   * @return int, the current health of the player.
   */
  public int getHealth() {
    return this.health;
  }

  /**
   * [increaseHealth]
   * Increases this player's health by a specified amount.
   * @author Candice Zhang
   * @param increment The amount to increase this player's health by.
   */
  public void increaseHealth(int increment) {
    this.health = Math.min(this.health+increment, this.maxHealth);
  }

  /**
   * [decreaseHealth]
   * Decrements this player's health by a specified amount.
   * @author Candice Zhang
   * @param decrement The amount to decrease this player's health by.
   */
  public void decreaseHealth(int decrement) {
    this.health = Math.max(this.health-decrement, 0);
  }

  /**
   * [getMaxHealth]
   * Retrieves this player's maximum health.
   * @author Candice Zhang
   * @return int, the player's total maximum health.
   */
  public int getMaxHealth() {
    return this.maxHealth;
  }

  /**
   * [increaseMaxHealth]
   * Increases this player's maximum health.
   * @author Candice Zhang
   * @param increment The amount to increase this player's health by.
   */
  public void increaseMaxHealth(int increment) {
    this.maxHealth += increment;
  }

  /**
   * [getEnergy]
   * Retrieves this player's current total energy.
   * @author Joseph Wang
   * @return int, this player's current energy.
   */
  public int getEnergy() {
    return this.energy;
  }

  /**
   * [increaseEnergy]
   * Increases this player's total current energy.
   * @author Joseph Wang
   * @param increment The amount to increase the energy by.
   */
  public void increaseEnergy(int increment) {
    this.energy = Math.min(this.energy+increment, this.maxEnergy);
  }

  /**
   * [decreaseEnergy]
   * Decreases this player's total current energy.
   * @author Joseph Wang
   * @param decrement The amount to decrease the energy by.
   */
  public void decreaseEnergy(int decrement) {
    this.energy -= decrement;

    if (this.energy < 0) {
      this.isExhausted = true;
    }
  }

  /**
   * [recover]
   * Using the time, calculates how much energy the player should recover at the end of the day, based
   * on time slept, and whether the player is exhaused or not.
   * Also fully restores health to max.
   * @author Joseph Wang
   * @param time The time that the player ended the day at.
   */
  public void recover(long time) {
    /* Recovered energy takes into consideration sleep time
     * (ie. if the player sleeps after midnight, they will have a penalty on their next day energy).
     * If the player is exhaused, recovered energy is equal to half.
     * The player only recovers energy if they go to bed with less than their determined recovered energy,
     * unless they are exhausted.
     */
    if (this.isExhausted) {
      this.energy = this.maxEnergy/2;
    } else if ((time > 0) && (time < 6*60*1_000_000_000L)) {
      int lostEnergy = (int)(Math.min((long)(this.maxEnergy / 3), time / 1_000_000_000L));
      if (this.energy < this.maxEnergy - lostEnergy) {
        this.energy = this.maxEnergy - lostEnergy;
      }
    } else {
      this.energy = this.maxEnergy;
    }

    this.health = maxHealth;
    this.isExhausted = false;
  }

  /**
   * [getMaxEnergy]
   * Retrieves the max possible energy that this player can have.
   * @author Joseph Wang
   * @return int, this player's max possible energy.
   */
  public int getMaxEnergy() {
    return this.maxEnergy;
  }

  /**
   * [increaseMaxEnergy]
   * Increases the max energy of this player.
   * @author Candice Zhang
   * @param increment the amount to increase this player's maximum energy by.
   */
  public void increaseMaxEnergy(int increment) {
    this.maxEnergy += increment;
  }

  /**
   * [getExhaustionStatus]
   * Retrieves the exhaustion status of this player.
   * @author Joseph Wang
   * @return boolean, whether the player has exhausted themself or not.
   */
  public boolean getExhaustionStatus() {
    return this.isExhausted;
  }

  /**
   * [moveToSpawnPosition]
   * Used if the player dies or passes out and must be moved to
   * a specified spawn area at a specified spawn location.
   * @author Joseph Wang
   * @param spawnLocation  The position to spawn at.
   * @return Area, the new area that the player is in.
   */
  public Area moveToSpawnPosition(Area currentArea, SpawnableArea spawnArea) {
    Point spawnPos = spawnArea.getSpawnLocation();
    return currentArea.moveAreas(this, spawnPos, (Area)spawnArea);
  }

  /**
   * [getInventorySize]
   * Retrieves this player's inventory's total capacity.
   * @author Candice Zhang
   * @return int, the total size of this player's inventory.
   */
  public int getInventorySize() {
    return this.inventorySize;
  }

  /**
   * [setInventorySize]
   * Sets this player's inventory size to a specified quantity.
   * @author Candice Zhang
   * @param size The new capacity of this inventory.
   */
  public void setInventorySize(int size) {
    this.inventorySize = size;
  }

  public Object getCurrentInteractingMenuObj() {
    return this.currentInteractingMenuObj;
  }

  public void setCurrentInteractingMenuObj(Object component) {
    this.currentInteractingMenuObj = component;
  }

  public boolean hasInteractingMenuObj() {
    return !(this.currentInteractingMenuObj == null);
  }

  /**
   * [getCurrentFunds]
   * Retrieves this player's current funds (ie. the amount of spendable money they have at this instance)
   * @author Candice Zhang
   * @return int, the total funds of this player.
   */
  public int getCurrentFunds() {
    return this.currentFunds;
  }

  /**
   * [increaseCurrentFunds]
   * Increases this player's current funds by a specified value.
   * @author Candice Zhang
   * @param value The amount to be increased by.
   */
  public void increaseCurrentFunds(int value) {
    this.currentFunds += value;
    this.totalEarnings += value;
  }

  /**
   * [decreaseCurrentFunds]
   * Decreases this player's current funds by a specified value.
   * @author Candice Zhang
   * @param value The amount to be removed.
   */
  public void decreaseCurrentFunds(int value) {
    this.currentFunds = Math.max(this.currentFunds-value, 0);
  }

  /**
   * [getFutureFunds]
   * Retrives this player's future funds (ie. How much they should earn at the end of this day).
   * @author Joseph Wang
   * @return int, how much this player should earn.
   */
  public int getFutureFunds() {
    return this.futureFunds;
  }

  /**
   * [increaseFutureFunds]
   * Adds a specified value to this player's future funds.
   * @author Joseph Wang
   * @param value The amount to be added.
   */
  public void increaseFutureFunds(int value) {
    this.futureFunds += value;
  }

  /**
   * [resetFutureFunds]
   * Sets this player's future funds (funds to be added) to 0.
   * @author Joseph Wang
   */
  public void resetFutureFunds() {
    this.futureFunds = 0;
  }

  /**
   * [getTotalEarnings]
   * Retrieves this player's total earnings.
   * @author Candice Zhang
   * @return int, how much this player has earned in total.
   */
  public int getTotalEarnings() {
    return this.totalEarnings;
  }

  /**
   * [getCraftingMachine]
   * Retrieves this player's crafting machine.
   * @author Candice Zhang
   * @return CraftingMachine, the crafting machine that belongs to the player.
   */
  public CraftingMachine getCraftingMachine () {
    return this.craftingMachine;
  }
}
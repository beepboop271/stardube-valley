
/**
 * [Player]
 * 2019-12-19
 * @version 0.4
 * @author Kevin Qiao, Candice Zhang, Joseph Wang
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

  private int inventorySize = 12;
  private HoldableStack[] inventory;
  private int selectedItemIdx;
  private Point selectedTile;
  private boolean isImmutable;
  private boolean isExhausted;
  private FishingGame currentFishingGame;
  private int currentFunds;
  private int futureFunds;
  private int totalEarnings;
  private int health;
  private int maxHealth;
  private int energy;
  private int maxEnergy;
  private int currentMenuPage;
  private Object currentInteractingMenuObj; // TOOD: rename, if possible :))

  public Player(Point position, String filePath) {
    super(position, Player.SIZE, filePath);
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
    this.currentFunds = 5_000;
    this.totalEarnings = this.currentFunds;

    this.inventory[0] = new HoldableStack("Pickaxe", 1);
    this.inventory[1] = new HoldableStack("Axe", 1);
    this.inventory[2] = new HoldableStack("Hoe", 1);
    this.inventory[3] = new HoldableStack("WateringCan", 1);
    this.inventory[4] = new HoldableStack("Fishing-Rod", 1);
    this.inventory[5] = new HoldableStack("BokChoySeeds", 15);
    this.inventory[6] = new HoldableStack("WatermelonItem", 99);
    this.inventory[7] = new HoldableStack("CornSeeds", 5);
    this.inventory[8] = new HoldableStack("ChestItem", 5);
    this.inventory[9] = new HoldableStack("FurnaceItem", 1);
    this.inventory[10] = new HoldableStack("IronItem", 10);
    this.inventory[11] = new HoldableStack("CoalItem", 2);
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

  public void useAtIndex(int index) {
    if (this.inventory[index].getQuantity() == 1) {
      this.inventory[index] = null;
    } else {
      this.inventory[index].subtractHoldables(1);
    }
  }

  public void decrementAtIndex(int index, int amount) {
    if (this.inventory[index].getQuantity() <= amount) {
      this.inventory[index] = null;
    } else {
      this.inventory[index].subtractHoldables(amount);
    }
  }

  public void removeAtIndex(int index) {
    this.inventory[index] = null;
  }

  public boolean hasAtIndex(int index) {
    return !(this.inventory[index] == null);
  }

  public HoldableStack getAtIndex(int index) {
    return this.inventory[index];
  }

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
    this.selectedTile = selectedTile;
  }

  public boolean isInMenu() {
    return !(this.currentMenuPage == Player.NO_MENU);
  }

  public void enterMenu(int menuPage) {
    this.isImmutable = true;
    this.currentMenuPage = menuPage;
  }

  public void exitMenu() {
    this.isImmutable = false;
    this.currentMenuPage = Player.NO_MENU;
    this.currentInteractingMenuObj = null;
  }

  public int getCurrentMenuPage() {
    return this.currentMenuPage;
  }
  
  public int getSelectedItemIdx() {
    return this.selectedItemIdx;
  }

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

  public boolean isImmutable() {
    return this.isImmutable;
  }
  
  public void setImmutable(boolean isImmutable){
    this.isImmutable = isImmutable;
  }

  public static double getItemAttractionDistance() {
    return Player.ITEM_ATTRACTION_DISTANCE;
  }

  public boolean isInFishingGame() {
    return (this.currentFishingGame != null);
  }

  public int getHealth() {
    return this.health;
  }

  public void increaseHealth(int increment) {
    this.health = Math.min(this.health+increment, this.maxHealth);
  }

  public void decreaseHealth(int decrement) {
    this.health = Math.max(this.health-decrement, 0);
  }

  public int getMaxHealth() {
    return this.maxHealth;
  }

  public void increaseMaxHealth(int increment) {
    this.maxHealth += increment;
  }

  public int getEnergy() {
    return this.energy;
  }

  public void increaseEnergy(int increment) {
    this.energy = Math.min(this.energy+increment, this.maxEnergy);
  }

  public void decreaseEnergy(int decrement) {
    this.energy -= decrement;

    if (this.energy < 0) {
      this.isExhausted = true;
      System.out.println("This homie is tired");
    }
  }

  public void recover(long time) { // TODO: take in a modifier or smth later
    if (this.isExhausted) {
      System.out.println("This homie is tired");
      this.energy = this.maxEnergy/2;
    } else if ((time > 0) && (time < 6*60*1_000_000_000L)) {
     
      int lostEnergy = (int)(Math.min((long)(this.maxEnergy / 3), time / 1_000_000_000L));
      System.out.println("Oh no this homie will lose " + lostEnergy + " hp bc of speding " + time / 1_000_000_000L + "mins outside");
      if (this.energy < this.maxEnergy - lostEnergy) {
        this.energy = this.maxEnergy - lostEnergy;
      }
    } else {
      System.out.println("Full recovery!");
      this.energy = this.maxEnergy;
    }

    this.health = maxHealth;
    this.isExhausted = false;
  }

  public int getMaxEnergy() {
    return this.maxEnergy;
  }

  public void increaseMaxEnergy(int increment) {
    this.maxEnergy += increment;
  }

  public boolean getExhaustionStatus() {
    return this.isExhausted;
  }

  public int getInventorySize() {
    return this.inventorySize;
  }

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
    this.currentFunds -= value;
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
}
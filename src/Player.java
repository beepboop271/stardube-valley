
/**
 * [Player]
 * 2019-12-19
 * @version 0.1
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
  private FishingGame currentFishingGame;
  private int health;
  private int maxHealth;
  private int energy;
  private int maxEnergy;
  private int currentMenuPage;
  private TileComponent currentInteractingComponent; // TOOD: rename, if possible :))

  public Player(Point position, String filePath) {
    super(position, Player.SIZE, filePath);
    this.inventory = new HoldableStack[this.inventorySize];
    this.selectedItemIdx = 0;
    this.isImmutable = false;
    this.health = 100;
    this.maxHealth = 100;
    this.energy = 270;
    this.maxEnergy = 270;
    this.currentMenuPage = Player.NO_MENU;
    this.currentInteractingComponent = null;

    this.inventory[0] = new HoldableStack("Pickaxe", 1);
    this.inventory[1] = new HoldableStack("Axe", 1);
    this.inventory[2] = new HoldableStack("Hoe", 1);
    this.inventory[3] = new HoldableStack("WateringCan", 1);
    this.inventory[4] = new HoldableStack("Fishing-Rod", 1);
    this.inventory[5] = new HoldableStack("TulipSeeds", 15);
    this.inventory[6] = new HoldableStack("StrawberrySeeds", 10);
    this.inventory[7] = new HoldableStack("CauliflowerSeeds", 5);
    this.inventory[8] = new HoldableStack("ChestItem", 5);
    this.inventory[9] = new HoldableStack("FurnaceItem", 1);
    this.inventory[10] = new HoldableStack("IronItem", 10);
    this.inventory[11] = new HoldableStack("CoalItem", 2);
  }
  
  @Override
  public void makeMove(long elapsedNanoTime) {
    if (this.isImmutable()) {
      return;
    }
    double elapsedSeconds = elapsedNanoTime/1_000_000_000.0;
    Vector2D positionChange = this.getVelocity();
    positionChange.setLength(Player.MAX_SPEED*elapsedSeconds);
    this.translatePos(positionChange);
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

  public void useAtIndex(int index) {
    if (this.inventory[index].getQuantity() == 1) {
      this.inventory[index] = null;
    } else {
      this.inventory[index].subtractHoldables(1);
    }
  }

  public void removeAtIndex(int index) {
    this.inventory[index] = null;
  }

  public boolean hasAtIndex(int index) {
    return !(this.inventory[index] == null);
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
    this.currentMenuPage = menuPage;
  }

  public void exitMenu() {
    this.currentMenuPage = Player.NO_MENU;
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
    if (this.isImmutable()) {
      return;
    }
    this.selectedItemIdx = (this.selectedItemIdx+1)%12;
  }

  public void decrementSelectedItemIdx() {
    if (this.isImmutable()) {
      return;
    }
    this.selectedItemIdx = Math.floorMod(this.selectedItemIdx-1, 12);
  }

  public void setSelectedItemIdx(int selectedItemIdx) {
    if (this.isImmutable()) {
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
    this.energy = Math.max(this.energy-decrement, 0);
  }

  public int getMaxEnergy() {
    return this.maxEnergy;
  }

  public void increaseMaxEnergy(int increment) {
    this.maxEnergy += increment;
  }

  public int getInventorySize() {
    return this.inventorySize;
  }

  public void setInventorySize(int size) {
    this.inventorySize = size;
  }

  public TileComponent getCurrentInteractingComponent() {
    return this.currentInteractingComponent;
  }

  public void setCurrentInteractingComponent(TileComponent component) {
    this.currentInteractingComponent = component;
  }

  public boolean hasInteractingComponent() {
    return !(this.currentInteractingComponent == null);
  }
}
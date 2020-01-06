
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

  private int inventorySize = 12;
  private HoldableStack[] inventory;
  private int selectedItemIdx;
  private Point selectedTile;
  private boolean inMenu = false;
  private boolean isImmutable;
  private FishingGame currentFishingGame;
  private int orientation;
  private int health;
  private int maxHealth;
  private int energy;
  private int maxEnergy;

  public Player(Point position) {
    super(position, Player.SIZE);
    this.inventory = new HoldableStack[this.inventorySize];
    this.selectedItemIdx = 0;
    this.isImmutable = false;
    this.orientation = World.SOUTH;
    this.health = 100;
    this.maxHealth = 100;
    this.energy = 270;
    this.maxEnergy = 270;

    this.inventory[0] = new HoldableStack("Pickaxe", 1);
    this.inventory[1] = new HoldableStack("Hoe", 1);
    this.inventory[2] = new HoldableStack("WateringCan", 1);
    this.inventory[3] = new HoldableStack("Fishing-Rod", 1);
    this.inventory[4] = new HoldableStack("ParsnipSeeds", 15);
    this.inventory[5] = new HoldableStack("WatermelonSeeds", 10);
    this.inventory[6] = new HoldableStack("CornSeeds", 10);
    this.inventory[7] = new HoldableStack("StrawberrySeeds", 10);
    this.inventory[8] = new HoldableStack("GarlicSeeds", 5);
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
    return this.inMenu;
  }

  public void toggleInMenu() {
    this.inMenu = !this.inMenu;
  }

  public void setInMenu(boolean inMenu) {
    this.inMenu = inMenu;
  }

  public int getSelectedItemIdx() {
    return this.selectedItemIdx;
  }

  public HoldableStack getSelectedItem() {
    return this.inventory[this.selectedItemIdx];
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

  public int getOrientation() {
    return this.orientation;
  }

  public void setOrientation(int orientation) {
    this.orientation = orientation;
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

}
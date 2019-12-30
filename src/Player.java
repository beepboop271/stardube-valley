
/**
 * [Player]
 * 2019-12-19
 * @version 0.1
 * @author Kevin Qiao, Candice Zhang, Joseph Wang
 */
public class Player extends Moveable {
  private static final double SIZE = 0.35;
  private static final double MAX_SPEED = 6;
  private static final double ITEM_ATTRACTION_DISTANCE = 3;
  private boolean inMenu = false;
  private int inventorySize = 12;
  private HoldableStack[] inventory;
  private Point selectedTile;
  private int selectedItemIdx;
  private boolean isImmutable;
  private FishingGame currentFishingGame;
  private int orientation;

  public Player(Point position) {
    super(position, Player.SIZE);
    this.inventory = new HoldableStack[this.inventorySize];
    this.selectedItemIdx = 0;
    this.isImmutable = false;
    this.orientation = World.SOUTH;

    this.inventory[0] = new HoldableStack("Pickaxe", 1);
    this.inventory[1] = new HoldableStack("Hoe", 1);
    this.inventory[2] = new HoldableStack("WateringCan", 1);
    this.inventory[3] = new HoldableStack("Fishing-Rod", 1);
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
    if (items == null) {
      return;
    }
    for (int i = 0; i < inventory.length; ++i) {
      // can use primitive equals because all holdables are shared
      if ((this.inventory[i] != null)
            && (this.inventory[i].getContainedHoldable() == items.getContainedHoldable())) {
        this.inventory[i].addHoldables(items.getQuantity());
        //System.out.println(items.getContainedHoldable().getName()+" added to slot index "+i);
        return;
      }
    }
    for (int i = 0; i < inventory.length; ++i) {
      if (this.inventory[i] == null) {
        this.inventory[i] = items;
        //System.out.println(items.getContainedHoldable().getName()+" added to slot index "+i);
        return;
      }
    }
    System.out.println("inventory full");
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

  public static double getSize() {
    return Player.SIZE;
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

}
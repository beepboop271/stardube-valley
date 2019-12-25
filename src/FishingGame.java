/**
 * [FishingGame]
 * 2019-12-24
 * @version 0.1
 * @author Candice Zhang
 */

import java.util.Random;

 // idk this might be a really sketchy approach but ahhHHHhh idkidk
class FishingGame {
  public static final int INGAME_STATUS = 0;
  public static final int WIN_STATUS = 1;
  public static final int LOSE_STATUS = 2;
  public static final int MAX_HEIGHT = 100;

  private int initialProgress;
  private int maxProgress;
  private int currentProgress;
  private boolean mouseDown;
  private long lastPressNanoTime;
  private long mouseHoldNanoTime;
  private int currentStatus;

  private Random random;

  private FishingGameBar playerBar;
  private FishingGameBar targetBar;
  
  //private Player player;
  //private FishingRod fishingRod;
  //private Tile tileToFish;

  public FishingGame() {
    //this.player = player;
    //this.fishingRod = (FishingRod)(player.getInventory()[player.getSelectedItemIdx()].getContainedHoldable());
    //this.tileToFish = tileToFish;
    
    this.initialProgress = 30;
    this.maxProgress = 100;
    this.currentProgress = this.initialProgress;
    this.mouseDown = false;
    this.mouseHoldNanoTime = 0;
    this.lastPressNanoTime = System.nanoTime();

    this.random = new Random();

    this.playerBar = new FishingGameBar(FishingGame.MAX_HEIGHT-FishingGame.MAX_HEIGHT/3, FishingGame.MAX_HEIGHT/3);
    this.targetBar = new FishingGameBar(FishingGame.MAX_HEIGHT-FishingGame.MAX_HEIGHT/7, FishingGame.MAX_HEIGHT/7);
  }


  public void update() {
    // TODO: acceleration and freefall, smoother movement (physics stuff)
    
    // move the player bar according to mouse input
    if (this.mouseDown == false) {
      this.playerBar.setY(Math.min(this.playerBar.getY()+1, FishingGame.MAX_HEIGHT));
      this.mouseHoldNanoTime = 0;
    } else {
      this.playerBar.setY(Math.max(this.playerBar.getY()-1, 0));
      this.mouseHoldNanoTime = System.nanoTime()-this.lastPressNanoTime;
    }

    // move the target bar randomly
    int moveChoice = random.nextInt(2); // 0: up; 1: down
    if (moveChoice == 1) {
      this.targetBar.setY(Math.min(this.targetBar.getY()+1, FishingGame.MAX_HEIGHT));
    } else {
      this.targetBar.setY(Math.max(this.targetBar.getY()-1, 0));
    }

    //System.out.println("mouse hold time:" + mouseHoldNanoTime);

    // compare collision and update progress progress accordingly
    if (targetBar.isInside(playerBar)) {
      this.currentProgress+=1;
    } else {
      this.currentProgress-=1;
    }

    // update status according to the progress
    if (this.currentProgress >= this.maxProgress){
      this.currentStatus = FishingGame.WIN_STATUS;
    } else if (this.currentProgress <= 0) {
      this.currentStatus = FishingGame.LOSE_STATUS;
    } else {
      this.currentStatus = FishingGame.INGAME_STATUS;
    }
    //System.out.println("Game updated. FishingGame progress: "+this.currentProgress+", status: "+this.currentStatus);
  }

  public void setMouseDown( boolean mouseDown ) {
    this.mouseDown = mouseDown;
  }

  public void updateLastPressNanoTime() {
    this.lastPressNanoTime = System.nanoTime();
  }

  public int getCurrentStatus() {
    return this.currentStatus;
  }

  public int getCurrentProgress() {
    return this.currentProgress;
  }

  public int getProgressPercentage() {
    return this.currentProgress*100/this.maxProgress; 
  }

  public FishingGameBar getPlayerBar() {
    return this.playerBar;
  }

  public FishingGameBar getTargetBar() {
    return this.targetBar;
  }

  public void returnFishingProduct() { //Holdable getFishingProduct() {
    
  }
}
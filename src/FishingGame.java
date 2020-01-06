/**
 * [FishingGame]
 * 2019-12-24
 * @version 0.1
 * @author Candice Zhang
 */

class FishingGame {
  public static final int INGAME_STATUS = 0;
  public static final int WIN_STATUS = 1;
  public static final int LOSE_STATUS = 2;
  public static final int MAX_HEIGHT = 200;
  public static final int MAX_PROGRESS = 500;
  public static final int INIT_PROGRESS = 50;
  public static final int PLAYER_MAX_SPEED = 4;
  public static final double PLAYER_ACCELERATION = 4;

  private int currentProgress;
  private int currentStatus;
  private WaterTile tileToFish;

  private FishingGameBar playerBar;
  private FishingGameBar targetBar;
  
  //private Player player;
  //private FishingRod fishingRod;
  //private Tile tileToFish;

  public FishingGame(WaterTile tileToFish) {
    //this.player = player;
    //this.fishingRod = (FishingRod)(player.getInventory()[player.getSelectedItemIdx()].getContainedHoldable());
    //this.tileToFish = tileToFish;
    
    this.currentProgress = FishingGame.INIT_PROGRESS;
    this.tileToFish = tileToFish;

    this.playerBar = new FishingGameBar(FishingGame.MAX_HEIGHT-FishingGame.MAX_HEIGHT/3, FishingGame.MAX_HEIGHT/3, 0.75);
    this.targetBar = new FishingGameBar(FishingGame.MAX_HEIGHT-FishingGame.MAX_HEIGHT/7, FishingGame.MAX_HEIGHT/7);
  }


  public void update(boolean mouseDown, Stopwatch mouseTimer) {
    // hold: go up
    this.playerBar.setVelocity(
        Math.max(0.5, Math.min(FishingGame.PLAYER_MAX_SPEED,
                                FishingGame.PLAYER_ACCELERATION*mouseTimer.getNanoTimeElapsed()/1_000_000_000.0))
    );

    if (!mouseDown) {
      this.playerBar.negateVelocity();
    }

    this.playerBar.setY(Math.max(0,
      Math.min(this.playerBar.getY()-this.playerBar.getVelocity(), MAX_HEIGHT-this.playerBar.getHeight())));
    
    // move the target bar randomly
    if (Math.random() < 0.5) {
      this.targetBar.setVelocity(-1.5);
    } else {
      this.targetBar.setVelocity(1.5);
    }

    this.targetBar.setY(Math.max(0,
      Math.min(this.targetBar.getY()-this.targetBar.getVelocity(), MAX_HEIGHT-this.targetBar.getHeight())));

    // compare collision and update progress progress accordingly
    if (targetBar.isInside(playerBar)) {
      this.currentProgress += 1;
    } else {
      this.currentProgress -= 1;
    }

    // update status according to the progress
    if (this.currentProgress >= FishingGame.MAX_PROGRESS){
      this.currentStatus = FishingGame.WIN_STATUS;
    } else if (this.currentProgress <= 0) {
      this.currentStatus = FishingGame.LOSE_STATUS;
    } else {
      this.currentStatus = FishingGame.INGAME_STATUS;
    }
  }

  public Holdable getFishReturned() {
    String[] fishableFish = this.tileToFish.getFishableFish();
    if(fishableFish.length == 0) {
      return null;
    }
    return HoldableFactory.getHoldable(fishableFish[(int)(Math.random()*fishableFish.length)]);
  }

  public Holdable getTrashReturned() {
    String[] fishableTrash = WaterTile.getFishableTrash();
    if(fishableTrash.length == 0) {
      return null;
    }
    return HoldableFactory.getHoldable(fishableTrash[(int)(Math.random()*fishableTrash.length)]);
  }

  // public void setMouseDown(boolean mouseDown) {
  //   this.mouseDown = mouseDown;
  // }

  // public void updateLastPressNanoTime() {
  //   this.lastPressNanoTime = System.nanoTime();
  // }

  public int getCurrentStatus() {
    return this.currentStatus;
  }

  public int getCurrentProgress() {
    return this.currentProgress;
  }

  public int getProgressPercentage() {
    return this.currentProgress*100/FishingGame.MAX_PROGRESS;
  }

  public FishingGameBar getPlayerBar() {
    return this.playerBar;
  }

  public FishingGameBar getTargetBar() {
    return this.targetBar;
  }

}
/**
 * [FishingGame]
 * 2019-12-24
 * @version 0.1
 * @author Candice Zhang
 */

public class FishingGame {
  public static final int INGAME_STATUS = 0;
  public static final int WIN_STATUS = 1;
  public static final int LOSE_STATUS = 2;
  public static final int MAX_HEIGHT = 200;
  public static final int MAX_PROGRESS = 500;
  public static final int INIT_PROGRESS = 50;
  public static final int PLAYER_MAX_SPEED = 4;
  public static final double PLAYER_ACCELERATION = 4;
  public static final long BITE_ELAPSE_NANOTIME = (long)Math.round(0.75*1_000_000_000);

  private int currentProgress;
  private int currentStatus;
  private WaterTile tileToFish;
  private long biteNanoTime;
  private boolean hasStarted;

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
    this.hasStarted = false;

    this.playerBar = new FishingGameBar(FishingGame.MAX_HEIGHT-FishingGame.MAX_HEIGHT/3, FishingGame.MAX_HEIGHT/3, 0.75);
    this.targetBar = new FishingGameBar(FishingGame.MAX_HEIGHT-FishingGame.MAX_HEIGHT/7, FishingGame.MAX_HEIGHT/7);
    this.generateBiteNanoTime();
  }


  public void update(boolean mouseDown, Stopwatch mouseTimer) {
    // hold: go up
    if(!this.hasStarted) {
      return;
    }
    
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

  public void setHasStarted(boolean hasStarted) {
    this.hasStarted = hasStarted;
  }

  public boolean hasStarted() {
    return this.hasStarted;
  }

  public long getBiteNanoTime() {
    return this.biteNanoTime;
  }

  public void generateBiteNanoTime() {
    this.biteNanoTime = (long)Math.round(System.nanoTime()+(3+Math.random()*5)*1_000_000_000);
  }

  public boolean isBiting() {
    return ((System.nanoTime()>=this.biteNanoTime) && (System.nanoTime()<=this.biteNanoTime+FishingGame.BITE_ELAPSE_NANOTIME));
  }

}
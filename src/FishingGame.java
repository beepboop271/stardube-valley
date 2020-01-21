/**
 * [FishingGame]
 * A fishing game for the player to play that stores essential data and components.
 * 2019-12-24
 * @version 0.2
 * @author Candice Zhang, Kevin Qiao
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

  /**
   * [FishingGame]
   * Constructor for a new FishingGame.
   * @param tileToFish  the water tile that is casted for fishing.
   */
  public FishingGame(WaterTile tileToFish) {
    this.currentProgress = FishingGame.INIT_PROGRESS;
    this.tileToFish = tileToFish;
    this.hasStarted = false;

    this.playerBar = new FishingGameBar(FishingGame.MAX_HEIGHT-FishingGame.MAX_HEIGHT/3, FishingGame.MAX_HEIGHT/3, 0.75);
    this.targetBar = new FishingGameBar(FishingGame.MAX_HEIGHT-FishingGame.MAX_HEIGHT/7, FishingGame.MAX_HEIGHT/7);
    this.generateBiteNanoTime();
  }

  /**
   * [update]
   * Updates the components and status of the game according to the newest mouse events.
   * @author Candice Zhang, Kevin Qiao
   * @param mouseDown  true if the mouse is held down, false otherwise.
   * @param mouseTimer The Stopwatch for the mouse.
   */
  public void update(boolean mouseDown, Stopwatch mouseTimer) {
    if(!this.hasStarted) {
      return;
    }
    
    this.playerBar.setVelocity(
        Math.max(0.5,
                 Math.min(FishingGame.PLAYER_MAX_SPEED,
                          FishingGame.PLAYER_ACCELERATION
                              * mouseTimer.getNanoTimeElapsed()
                              / 1_000_000_000.0))
    );

    if (!mouseDown) {
      this.playerBar.negateVelocity();
    }
    this.playerBar.setY(
        Math.max(0,
                 Math.min(this.playerBar.getY()-this.playerBar.getVelocity(),
                          MAX_HEIGHT-this.playerBar.getHeight()))
    );
    
    // move the target bar randomly
    if (Math.random() < 0.5) {
      this.targetBar.setVelocity(-1.5);
    } else {
      this.targetBar.setVelocity(1.5);
    }

    this.targetBar.setY(
        Math.max(0,
                 Math.min(this.targetBar.getY()-this.targetBar.getVelocity(),
                          MAX_HEIGHT-this.targetBar.getHeight()))
    );

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

  /**
   * [getFishReturned]
   * Randomly selects and retrieves one of the fish products
   * of the water tile used to fish.
   * @author Candice Zhang
   * @return Holdable, a fish product of the water tile.
   */
  public Holdable getFishReturned() {
    String[] fishableFish = this.tileToFish.getFishableFish();
    if (fishableFish.length == 0) {
      return null;
    }
    return HoldableFactory.getHoldable(fishableFish[(int)(Math.random()*fishableFish.length)]);
  }

  /**
   * [getTrashReturned]
   * Randomly selects and retrieves one of the trash products of a water tile.
   * @author Candice Zhang
   * @return Holdable, a trash product of the water tile.
   */
  public Holdable getTrashReturned() {
    String[] fishableTrash = WaterTile.getFishableTrash();
    if (fishableTrash.length == 0) {
      return null;
    }
    return HoldableFactory.getHoldable(fishableTrash[(int)(Math.random()*fishableTrash.length)]);
  }

  /**
   * [getCurrentStatus]
   * Retrieves the current status of the fishing game.
   * @return int, the current status of the fishing game.
   */
  public int getCurrentStatus() {
    return this.currentStatus;
  }

  /**
   * [getCurrentProgress]
   * Retrieves the current progress of the fishing game.
   * @return int, the current status of the fishing game.
   */
  public int getCurrentProgress() {
    return this.currentProgress;
  }

  /**
   * [getProgressPercentage]
   * Retrieves the current progress of the fishing game, in percentage.
   * @return int, the current status of the fishing game, in percentage.
   */
  public int getProgressPercentage() {
    return this.currentProgress*100/FishingGame.MAX_PROGRESS;
  }

  /**
   * [getPlayerBar]
   * Retrieves the FishingGameBar that represents the player.
   * @return FishingGameBar, the FishingGameBar that represents the player.
   */
  public FishingGameBar getPlayerBar() {
    return this.playerBar;
  }

  /**
   * [getTargetBar]
   * Retrieves the FishingGameBar that represents the target of the game.
   * @return FishingGameBar, the FishingGameBar that represents the target of the game.
   */
  public FishingGameBar getTargetBar() {
    return this.targetBar;
  }

  /**
   * [setHasStarted]
   * Sets the hasStarted status of this fishing game.
   * @param hasStarted True if the game has started, false otherwise.
   */
  public void setHasStarted(boolean hasStarted) {
    this.hasStarted = hasStarted;
  }

  /**
   * [hasStarted]
   * Checks if this fishing game has started.
   * @return boolean, true if the game has started, false otherwise.
   */
  public boolean hasStarted() {
    return this.hasStarted;
  }

  /**
   * [getBiteNanoTime]
   * Retrieves the nanotime of the fish bite in this game.
   * @return long, the nanotime of the fish bite.
   */
  public long getBiteNanoTime() {
    return this.biteNanoTime;
  }

  /**
   * [generateBiteNanoTime]
   * Generates/Regenerates a future fish bite in this game, in nanotime.
   * @author Candice Zhang
   */
  public void generateBiteNanoTime() {
    this.biteNanoTime = (long)Math.round(System.nanoTime()+(3+Math.random()*5)*1_000_000_000);
  }

  /**
   * [isBiting]
   * Checks if a fishable product is biting the bait.
   * @author Candice Zhang
   * @return boolean, true if a fishable product is biting, false otherwise.
   */
  public boolean isBiting() {
    return ((System.nanoTime() >= this.biteNanoTime)
            && (System.nanoTime() <= this.biteNanoTime+FishingGame.BITE_ELAPSE_NANOTIME));
  }
}
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

public class StardubeEventListener implements KeyListener,
                                              MouseListener,
                                              MouseMotionListener,
                                              MouseWheelListener {
  private World stardubeWorld;
  private WorldPanel worldPanel;
  private Player stardubePlayer;

  private boolean[] keyStates;
  private boolean[] mouseStates;
  private Stopwatch[] mouseStopwatches;
  private Point mousePos;

  public StardubeEventListener(World stardubeWorld, WorldPanel worldPanel) {
    this.stardubeWorld = stardubeWorld;
    this.stardubePlayer = stardubeWorld.getPlayer();
    this.worldPanel = worldPanel;

    this.keyStates = new boolean[0x7B+1];  // decent amount of keys
    this.mouseStates = new boolean[3+1];
    this.mouseStopwatches = new Stopwatch[3+1];
    for (int i = 1; i <= 3; ++i) {
      this.mouseStopwatches[i] = new Stopwatch();
    }
    this.mousePos = new Point(0, 0);
  }

  public void update() {
    this.updateSelectedTile();

    if (this.stardubePlayer.isInFishingGame()) {
      long biteNanoTime = this.stardubePlayer.getCurrentFishingGame().getBiteNanoTime();
      if ((biteNanoTime+FishingGame.BITE_ELAPSE_NANOTIME)<System.nanoTime()) {
        this.stardubePlayer.getCurrentFishingGame().generateBiteNanoTime();
      }
      this.stardubePlayer.getCurrentFishingGame().update(this.mouseStates[1], this.mouseStopwatches[1]);
      this.stardubePlayer.setImmutable(true);
      if(this.stardubePlayer.getCurrentFishingGame().getCurrentStatus() != FishingGame.INGAME_STATUS) {
        this.stardubeWorld.emplaceFutureEvent((long)(0.5*1_000_000_000),
                                              new FishingGameEndedEvent(this.stardubePlayer.getCurrentFishingGame()));
        this.stardubePlayer.endCurrentFishingGame();
      }
    }
    
    this.stardubePlayer.setVelocity(0, 0, 0);
    if (this.keyStates[KeyEvent.VK_W]) {
      this.stardubePlayer.updateImage();
      if (this.stardubePlayer.getVerticalSpeed() != -1) {
        this.stardubePlayer.setVerticalSpeed(-1);
        this.stardubePlayer.setOrientation(World.NORTH);
      }
    }
    if (this.keyStates[KeyEvent.VK_A]) {
      this.stardubePlayer.updateImage();
      if (this.stardubePlayer.getHorizontalSpeed() != -1) {
        this.stardubePlayer.setHorizontalSpeed(-1);
        this.stardubePlayer.setOrientation(World.WEST);
      }
    }
    if (this.keyStates[KeyEvent.VK_S]) {
      this.stardubePlayer.updateImage();
      if (this.stardubePlayer.getVerticalSpeed() != 1) {
        this.stardubePlayer.setVerticalSpeed(1);
        this.stardubePlayer.setOrientation(World.SOUTH);
      }
    }
    if (this.keyStates[KeyEvent.VK_D]) {
      this.stardubePlayer.updateImage();
      if (this.stardubePlayer.getHorizontalSpeed() != 1) {
        this.stardubePlayer.setHorizontalSpeed(1);
        this.stardubePlayer.setOrientation(World.EAST);
      }
    }
  }

  @Override
  public void keyPressed(KeyEvent e) {
    if (e.getKeyCode() < this.keyStates.length) {
      this.keyStates[e.getKeyCode()] = true;
    }

    if (this.stardubePlayer.isImmutable()) {
      return;
    }
    this.updateSelectedTile();
    switch (e.getKeyCode()) {
      case KeyEvent.VK_E:
        if (this.stardubePlayer.isInMenu()) {
          this.stardubePlayer.exitMenu();
        } else {
          this.stardubePlayer.enterMenu(Player.INVENTORY_PAGE);
        }
        break;
      case KeyEvent.VK_ESCAPE:
        this.stardubePlayer.exitMenu();
        break;
      // temp stuff below this comment
      case KeyEvent.VK_P:
        this.stardubeWorld.doDayEndActions();
        break;
      case KeyEvent.VK_B: // press b to buy cuz S_hop/P_urchase are taken lol
        this.stardubePlayer.enterMenu(Player.SHOP_PAGE);
        break;
      case KeyEvent.VK_M:
        Area dumb = new MineLevel.Builder(1, 5).buildLevel();
        dumb.addMoveable(stardubePlayer);
        stardubeWorld.getPlayerArea().removeMoveable(stardubePlayer);
        stardubeWorld.setPlayerArea(dumb);
        break;
      case KeyEvent.VK_I:
        this.stardubePlayer.setPos(this.stardubePlayer.getPos().translateNew(0, -1));
        break;
      case KeyEvent.VK_J:
        this.stardubePlayer.setPos(this.stardubePlayer.getPos().translateNew(-1, 0));
        break;
      case KeyEvent.VK_K:
        this.stardubePlayer.setPos(this.stardubePlayer.getPos().translateNew(0, 1));
        break;
      case KeyEvent.VK_L:
        this.stardubePlayer.setPos(this.stardubePlayer.getPos().translateNew(1, 0));
        break;
    }
  }

  @Override
  public void keyTyped(KeyEvent e) {
  }

  @Override
  public void keyReleased(KeyEvent e) {
    if (e.getKeyCode() < this.keyStates.length) {
      this.keyStates[e.getKeyCode()] = false;
    }
  }

  @Override
  public void mouseClicked(MouseEvent e) {
  }

  @Override
  public void mousePressed(MouseEvent e) {
    this.mouseStates[e.getButton()] = true;
    this.mouseStopwatches[e.getButton()] = new Stopwatch();
    this.mousePos.x = e.getX();
    this.mousePos.y = e.getY();

    if (e.getButton() == MouseEvent.BUTTON1) {
      if (this.stardubePlayer.isInFishingGame()) {
        // FishingGame fishingGame = this.stardubePlayer.getCurrentFishingGame();
        // fishingGame.setMouseDown(true);
        // fishingGame.updateLastPressNanoTime();
      } else if ((!this.stardubePlayer.isInMenu()) && (!this.stardubePlayer.isImmutable())) {
        if (this.stardubePlayer.getInventory()[this.stardubePlayer.getSelectedItemIdx()] != null) {
          HoldableStack selectedHoldableStack = this.stardubePlayer.getInventory()[this.stardubePlayer.getSelectedItemIdx()];
          if (selectedHoldableStack.getContainedHoldable() instanceof FishingRod) {
            if (!this.worldPanel.isPosInHotbar((int)this.mousePos.x, (int)this.mousePos.y)) {
            ((FishingRod)selectedHoldableStack.getContainedHoldable()).startCasting();
            this.stardubePlayer.setImmutable(true);
            }
          }
        }
      }
    }

  }

  @Override
  public void mouseReleased(MouseEvent e) {
    this.mouseStates[e.getButton()] = false;
    this.mouseStopwatches[e.getButton()] = new Stopwatch();
    this.mousePos.x = e.getX();
    this.mousePos.y = e.getY();

    if (this.stardubePlayer.isInFishingGame()) {
      if (!this.stardubePlayer.getCurrentFishingGame().hasStarted()) {
        this.stardubeWorld.emplaceFutureEvent(0, new CatchFishEvent((FishingRod)(this.stardubePlayer.getSelectedItem().getContainedHoldable())));
      }
    } else if ((!this.stardubePlayer.isInMenu()) && (!this.stardubePlayer.isImmutable())) {
      // general player interactions (AKA doors and foraging)
      if (e.getButton() == MouseEvent.BUTTON3) {
        if (this.stardubePlayer.getSelectedItem().getContainedHoldable() instanceof Consumable) {
          this.stardubePlayer.consume();
        }
        if (this.stardubePlayer.getSelectedTile() != null) {
          this.stardubeWorld.emplaceFutureEvent(
                (long)(0.5*1_000_000_000),
                // idk what to name this lol
                new UtilityUsedEvent(this.stardubePlayer.getSelectedTile()));
          return;
        }
      }
      if (e.getButton() == MouseEvent.BUTTON1) {
        // if the mousepress is within the hotbar, update item selection
        if (this.worldPanel.isPosInHotbar((int)mousePos.x, (int)mousePos.y)) {
          int selectedItemIdx = this.worldPanel.hotbarItemIdxAt((int)Math.round(this.mousePos.x));
          this.stardubePlayer.setSelectedItemIdx(selectedItemIdx);
          return;
        }
        if (this.stardubePlayer.getSelectedItem() != null) {
          Holdable selectedItem = this.stardubePlayer.getSelectedItem().getContainedHoldable();
          if (selectedItem instanceof UtilityTool) {
            this.stardubePlayer.setImmutable(true);
            // TODO: play animation
            // scuffed line
            this.stardubeWorld.emplaceFutureEvent(
                (long)(0.5*1_000_000_000),
                new UtilityToolUsedEvent(
                    (UtilityTool)selectedItem,
                    ((UtilityTool)selectedItem).getUseLocation(this.stardubePlayer.getSelectedTile())[0]
                )
            );
          } else if (selectedItem instanceof Placeable) {
            System.out.println("Trying to place!");
            this.stardubeWorld.emplaceFutureEvent(
              (long)(0.5*1_000_000_000),
              new ComponentPlacedEvent(
                ((Placeable)selectedItem).placeItem(), this.stardubePlayer.getSelectedItemIdx(),
                this.stardubePlayer.getSelectedTile()
              )
            );
          }
         }
        }

    } else if ((!this.stardubePlayer.isInMenu()) && this.stardubePlayer.isImmutable()) {
      if (e.getButton() == MouseEvent.BUTTON1) {
        if (this.stardubePlayer.getSelectedItem() != null) {
          Holdable selectedItem = this.stardubePlayer.getSelectedItem().getContainedHoldable();
          if (selectedItem instanceof FishingRod) { // is casting
            // TODO: play animation
            if (((FishingRod)selectedItem).getCurrentStatus() == FishingRod.CASTING_STATUS) {
              this.stardubeWorld.emplaceFutureEvent((long)(0.5*1_000_000_000),
                                new CastingEndedEvent((FishingRod)selectedItem));
              ((FishingRod)selectedItem).setCurrentStatus(FishingRod.IDLING_STATUS);
            }
          }
        }
      }
    } else if (this.stardubePlayer.isInMenu()) {
      if (worldPanel.isPosInMenuTab((int)this.mousePos.x, (int)this.mousePos.y)) {
        // TODO: process the button (update selected button id or sth);
      } else if ((this.stardubePlayer.getCurrentMenuPage() == Player.INVENTORY_PAGE)&&
                 (worldPanel.isPosInMenuInventory((int)this.mousePos.x, (int)this.mousePos.y))) {
        int selectedItemIdx = this.worldPanel.inventoryMenuItemIdxAt((int)this.mousePos.x, (int)this.mousePos.y);
        if (selectedItemIdx < this.stardubePlayer.getInventory().length) {
          this.stardubePlayer.setSelectedItemIdx(selectedItemIdx);
        }
      }
    }
    
  }

  @Override
  public void mouseEntered(MouseEvent e) {
    this.mousePos.x = e.getX();
    this.mousePos.y = e.getY();
    this.updateSelectedTile();
  }

  @Override
  public void mouseExited(MouseEvent e) {
  }

  @Override
  public void mouseDragged(MouseEvent e) {
    //System.out.println("mouse drag detected");
  }

  @Override
  public void mouseMoved(MouseEvent e) {
    this.mousePos.x = e.getX();
    this.mousePos.y = e.getY();
    this.updateSelectedTile();
    this.worldPanel.updateHoveredItemIdx((int)this.mousePos.x, (int)this.mousePos.y);
  }

  @Override
  public void mouseWheelMoved(MouseWheelEvent e) {
    int rotation = e.getWheelRotation();
    if (rotation < 0) {
      this.stardubePlayer.decrementSelectedItemIdx();
    } else if (rotation > 0) {
      this.stardubePlayer.incrementSelectedItemIdx();
    }
  }

  private void updateSelectedTile() {
    Vector2D mouseOffset = new Vector2D(this.mousePos.x-this.worldPanel.getPlayerScreenPos().x,
                                        this.mousePos.y-this.worldPanel.getPlayerScreenPos().y)
                                       .add(new Vector2D(-Tile.getSize()*Player.SIZE,
                                                         -Tile.getSize()*Player.SIZE))
                                       .setLength(1);
    this.stardubePlayer
        .setSelectedTile(new Point(mouseOffset.getX(), mouseOffset.getY())
                                  .translate(this.stardubePlayer.getPos())
                                  .round());

  }
}
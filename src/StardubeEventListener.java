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
  private Point mousePos;

  public StardubeEventListener(World stardubeWorld, WorldPanel worldPanel) {
    this.stardubeWorld = stardubeWorld;
    this.stardubePlayer = stardubeWorld.getPlayer();
    this.worldPanel = worldPanel;
    this.mousePos = new Point(0, 0);
  }

  @Override
  public void keyPressed(KeyEvent e) {
    this.updateSelectedTile();
    switch (e.getKeyCode()) {
      case KeyEvent.VK_W:
        if (this.stardubePlayer.getVerticalSpeed() != -1) {
          this.stardubePlayer.setVerticalSpeed(-1);
        }
        break;
      case KeyEvent.VK_A:
        if (this.stardubePlayer.getHorizontalSpeed() != -1) {
          this.stardubePlayer.setHorizontalSpeed(-1);
        }
        break;
      case KeyEvent.VK_S:
        if (this.stardubePlayer.getVerticalSpeed() != 1) {
          this.stardubePlayer.setVerticalSpeed(1);
        }
        break;
      case KeyEvent.VK_D:
        if (this.stardubePlayer.getHorizontalSpeed() != 1) {
          this.stardubePlayer.setHorizontalSpeed(1);
        }
        break;
      case KeyEvent.VK_E:
        this.stardubePlayer.toggleInMenu();
        break;
      case KeyEvent.VK_ESCAPE:
        this.stardubePlayer.setInMenu(false);
        break;
    }
  }

  @Override
  public void keyTyped(KeyEvent e) {
    this.updateSelectedTile();
    switch (e.getKeyCode()) {
      case KeyEvent.VK_W:
        if (this.stardubePlayer.getVerticalSpeed() != -1) {
          this.stardubePlayer.setVerticalSpeed(-1);
        }
        break;
      case KeyEvent.VK_A:
        if (this.stardubePlayer.getHorizontalSpeed() != -1) {
          this.stardubePlayer.setHorizontalSpeed(-1);
        }
        break;
      case KeyEvent.VK_S:
        if (this.stardubePlayer.getVerticalSpeed() != 1) {
          this.stardubePlayer.setVerticalSpeed(1);
        }
        break;
      case KeyEvent.VK_D:
        if (this.stardubePlayer.getHorizontalSpeed() != 1) {
          this.stardubePlayer.setHorizontalSpeed(1);
        }
        break;
    }
  }

  @Override
  public void keyReleased(KeyEvent e) {
    switch (e.getKeyCode()) {
      case KeyEvent.VK_W:
        if (this.stardubePlayer.getVerticalSpeed() == -1) {
          this.stardubePlayer.setVerticalSpeed(0);
        }
        break;
      case KeyEvent.VK_A:
        if (this.stardubePlayer.getHorizontalSpeed() == -1) {
          this.stardubePlayer.setHorizontalSpeed(0);
        }
        break;
      case KeyEvent.VK_S:
        if (this.stardubePlayer.getVerticalSpeed() == 1) {
          this.stardubePlayer.setVerticalSpeed(0);
        }
        break;
      case KeyEvent.VK_D:
        if (this.stardubePlayer.getHorizontalSpeed() == 1) {
          this.stardubePlayer.setHorizontalSpeed(0);
        }
        break;
      
      // p r e s s  F  t o  F I S H
      // (temp thing, dont beat me too hard lol - candice)
      case KeyEvent.VK_F:
        if (this.stardubePlayer.getCurrentFishingGame() == null) {
          this.stardubePlayer.setCurrentFishingGame(new FishingGame());
        }
        break;
    }
  }

  @Override
  public void mouseClicked(MouseEvent e) {

  }

  @Override
  public void mousePressed(MouseEvent e) {
    this.mousePos.x = e.getX();
    this.mousePos.y = e.getY();    

    if (this.stardubePlayer.getCurrentFishingGame() != null) { // in fishing game
      FishingGame fishingGame = this.stardubePlayer.getCurrentFishingGame();
      fishingGame.setMouseDown(true);
      fishingGame.updateLastPressNanoTime();
    } else if (!this.stardubePlayer.isInMenu()) {    // && (e.getButton() == MouseEvent.BUTTON1)) {
      // if the mousepress is within the hotbar, update item selection
      if ((this.mousePos.x >= this.worldPanel.getHotbarX()) &&
          (this.mousePos.x <= this.worldPanel.getHotbarX()+12*(WorldPanel.HOTBAR_CELLSIZE+WorldPanel.HOTBAR_CELLGAP)) &&
          (this.mousePos.y >= this.worldPanel.getHotbarY()) &&
          (this.mousePos.y <= this.worldPanel.getHotbarY() + WorldPanel.HOTBAR_CELLSIZE)) {
        int selectedItemIdx = Math.min((int)(Math.floor((this.mousePos.x-this.worldPanel.getHotbarX())/
                              (WorldPanel.HOTBAR_CELLSIZE+WorldPanel.HOTBAR_CELLGAP))), 11);
        this.stardubePlayer.setSelectedItemIdx(selectedItemIdx);
        return;
      }

      // general player interactions (AKA doors and foraging)
      if (this.stardubePlayer.getSelectedTile() != null) {
        this.stardubeWorld.emplaceFutureEvent(
              (long)(0.5*1_000_000_000),
              // idk what to name this lol
              new UtilityUsedEvent(this.stardubePlayer.getSelectedTile()));
    }
      // *to-do
      // - if is entrance, move area, return
      // - if has collectable, get items from collectable, return

      if (this.stardubePlayer.getInventory()[this.stardubePlayer.getSelectedItemIdx()] != null) {
        HoldableStack selectedHoldableStack = this.stardubePlayer.getInventory()[this.stardubePlayer.getSelectedItemIdx()];
        if (selectedHoldableStack.getContainedHoldable() != null){
          // casting
          if (selectedHoldableStack.getContainedHoldable() instanceof FishingRod){
            // System.out.println("fishing rod!");
            // Tile castedTile = something something idk;
            // if tileisfishable {}
          } // else {System.out.println("not rod!");}
        }
      }
    } else {
      // if the mousepress is within the menu tab buttons, change the inventory menu display mode
      if ((this.mousePos.x >= this.worldPanel.getMenuX()) &&
          (this.mousePos.x <= this.worldPanel.getMenuX() + this.worldPanel.getMenuW()) &&
          (this.mousePos.y >= this.worldPanel.getMenuY()) &&
          (this.mousePos.y <= this.worldPanel.getMenuY() + (WorldPanel.HOTBAR_CELLSIZE + WorldPanel.HOTBAR_CELLGAP))) {
        // *TODO: process the button (update selected button id or sth);
      // if the mousepress is within the full inventory area, change the selected item index according to the mouse position
      } else if ((this.mousePos.x >= this.worldPanel.getMenuX()) &&
                 (this.mousePos.x <= this.worldPanel.getMenuX() + this.worldPanel.getMenuW()) &&
                 (this.mousePos.y > this.worldPanel.getMenuY() + (WorldPanel.HOTBAR_CELLSIZE + WorldPanel.HOTBAR_CELLGAP)) &&
                 (this.mousePos.y <= this.worldPanel.getMenuY() + 4*(WorldPanel.HOTBAR_CELLSIZE + WorldPanel.HOTBAR_CELLGAP))) {
        int selectedItemIdx = Math.min((int)(Math.floor(this.mousePos.x-this.worldPanel.getMenuX())/
                                      (WorldPanel.HOTBAR_CELLSIZE+WorldPanel.HOTBAR_CELLGAP)), 11)
                              + 12*Math.min((int)(Math.floor((this.mousePos.y-(this.worldPanel.getMenuY() + (WorldPanel.HOTBAR_CELLSIZE + WorldPanel.HOTBAR_CELLGAP)))/
                                           (WorldPanel.HOTBAR_CELLSIZE+WorldPanel.HOTBAR_CELLGAP))), 2);
        if (selectedItemIdx < this.stardubePlayer.getInventory().length) {
          this.stardubePlayer.setSelectedItemIdx(selectedItemIdx);
        }
      }
    }
  }

  @Override
  public void mouseReleased(MouseEvent e) {
    this.mousePos.x = e.getX();
    this.mousePos.y = e.getY();

    if (this.stardubePlayer.getCurrentFishingGame()!=null) { // in fishing game
      FishingGame fishingGame = this.stardubePlayer.getCurrentFishingGame();
      fishingGame.setMouseDown(false);
    } else if (!(this.stardubePlayer.isInMenu() || this.stardubePlayer.isImmutable())) {
      if (e.getButton() == MouseEvent.BUTTON1) {
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
          }
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
  }

  @Override
  public void mouseWheelMoved(MouseWheelEvent e) {
    int rotation = e.getWheelRotation();
    if (rotation < 0) {
      this.stardubePlayer.incrementSelectedItemIdx();
    } else if (rotation > 0) {
      this.stardubePlayer.decrementSelectedItemIdx();
    }
  }

  private void updateSelectedTile() {
    Vector2D mouseOffset = new Vector2D(this.mousePos.x-this.worldPanel.getPlayerScreenPos().x,
                                        this.mousePos.y-this.worldPanel.getPlayerScreenPos().y)
                                       .add(new Vector2D(-Tile.getSize()*Player.getSize(),
                                                         -Tile.getSize()*Player.getSize()))
                                       .setLength(1);
    this.stardubePlayer
        .setSelectedTile(new Point(mouseOffset.getX(), mouseOffset.getY())
                                  .translate(this.stardubePlayer.getPos())
                                  .round());

  }
  // private void updateSelectedItemIdx() {
  //   int selectedItemIdx = (int)(Math.floor(this.mousePos.x-(this.worldPanel.getWidth()/2-6*(WorldPanel.HOTBAR_CELLSIZE+WorldPanel.HOTBAR_CELLGAP)))
  //                                             /(WorldPanel.HOTBAR_CELLSIZE+WorldPanel.HOTBAR_CELLGAP));
  //   if (selectedItemIdx>=12) {
  //     selectedItemIdx = 11;
  //   }
  //   this.stardubePlayer.setSelectedItemIdx(selectedItemIdx);
  //   //System.out.println("selected item id: "+selectedItemIdx);
  // }
}
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
    }
  }

  @Override
  public void mouseClicked(MouseEvent e) {

  }

  @Override
  public void mousePressed(MouseEvent e) {
  }

  @Override
  public void mouseReleased(MouseEvent e) {
    this.mousePos.x = e.getX();
    this.mousePos.y = e.getY();
    // if (!this.stardubePlayer.isInMenu()) {
    //   if (e.getButton() == MouseEvent.BUTTON1) {
    //     if (this.stardubePlayer.)
    //   }
    // }

    if (!this.stardubePlayer.isInMenu()) {
      // hotbar item selection
      if ( (this.mousePos.x >= this.worldPanel.getHotbarX()) &&
           (this.mousePos.x <= this.worldPanel.getHotbarX()+12*(WorldPanel.HOTBAR_CELLSIZE+WorldPanel.HOTBAR_CELLGAP)) &&
           (this.mousePos.y >= this.worldPanel.getHotbarY()) &&
           (this.mousePos.y <= this.worldPanel.getHotbarY() + WorldPanel.HOTBAR_CELLSIZE) ) {
        this.updateSelectedItemIdx();
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
  private void updateSelectedItemIdx() {
    int selectedItemIdx = (int)(Math.floor(this.mousePos.x-(this.worldPanel.getWidth()/2-6*(WorldPanel.HOTBAR_CELLSIZE+WorldPanel.HOTBAR_CELLGAP)))
                                              /(WorldPanel.HOTBAR_CELLSIZE+WorldPanel.HOTBAR_CELLGAP));
    if (selectedItemIdx>=12) {
      selectedItemIdx = 11;
    }
    this.stardubePlayer.setSelectedItemIdx(selectedItemIdx);
    //System.out.println("selected item id: "+selectedItemIdx);
  }
}
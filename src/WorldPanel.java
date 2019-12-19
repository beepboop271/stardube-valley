import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JPanel;

/**
 * [WorldPanel]
 * 2019-12-19
 * @version 0.1
 * @author Kevin Qiao
 */
public class WorldPanel extends JPanel {
  private World worldToDisplay;
  private int tileWidth, tileHeight;

  public WorldPanel(World worldToDisplay, int width, int height) {
    super();
    this.worldToDisplay = worldToDisplay;
    this.addKeyListener(new WorldPanelKeyListener());
    this.setPreferredSize(new Dimension(width, height));
    this.tileWidth = (int)Math.ceil(((double)width)/Tile.getSize());
    this.tileHeight = (int)Math.ceil(((double)height)/Tile.getSize());
    this.setOpaque(true);
  }

  @Override
  public void paintComponent(Graphics g) {
    g.setColor(Color.BLACK);
    g.fillRect(0, 0, this.getWidth(), this.getHeight());

    // rip

    Point playerPos = this.worldToDisplay.getPlayer().getPos();
    Area playerArea = this.worldToDisplay.getPlayerArea();
    if ((playerPos.x < this.tileWidth/2.0)
          || (playerArea.getWidth()-playerPos.x < this.tileWidth/2.0)
          || (playerPos.y < this.tileHeight/2.0)
          || (playerArea.getHeight()-playerPos.y < this.tileHeight/2.0)) {
    } else {
      int screenX = 0;
      int screenY = 0;
      g.setColor(Color.RED);
      g.fillRect((int)(this.getWidth()/2-Tile.getSize()*Player.getSize()),
                 (int)(this.getHeight()/2-Tile.getSize()*Player.getSize()),
                 (int)(2*Player.getSize()),
                 (int)(2*Player.getSize()));
      for (int y = (int)(playerPos.y-this.tileHeight/2-1); y < playerPos.y+this.tileHeight/2+1; ++y) {
        for (int x = (int)(playerPos.x-this.tileWidth/2-1); x < playerPos.x+this.tileWidth/2+1; ++x) {
          if (playerArea.getMapAt(x, y) != null) {
            g.drawImage(playerArea.getMapAt(x, y).getImage(), )
          }
        }
      }
    }
  }

  private class WorldPanelKeyListener extends KeyAdapter {
    @Override
    public void keyPressed(KeyEvent e) {
      Player p = worldToDisplay.getPlayer();
      switch (e.getKeyCode()) {
        case KeyEvent.VK_W:
          p.setVerticalSpeed(-1);
          break;
        case KeyEvent.VK_A:
          p.setHorizontalSpeed(-1);
          break;
        case KeyEvent.VK_S:
          p.setVerticalSpeed(1);
          break;
        case KeyEvent.VK_D:
          p.setHorizontalSpeed(1);
          break;
        case KeyEvent.VK_E:
          p.toggleInMenu();
          break;
        case KeyEvent.VK_ESCAPE:
          p.setInMenu(false);
          break;
      }
    }

    @Override
    public void keyReleased(KeyEvent e) {
      Player p = worldToDisplay.getPlayer();
      switch (e.getKeyCode()) {
        case KeyEvent.VK_W:
          p.setVerticalSpeed(0);
          break;
        case KeyEvent.VK_A:
          p.setHorizontalSpeed(0);
          break;
        case KeyEvent.VK_S:
          p.setVerticalSpeed(0);
          break;
        case KeyEvent.VK_D:
          p.setHorizontalSpeed(0);
          break;
      }
    }
  }
}
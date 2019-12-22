import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

/**
 * [WorldPanel]
 * 2019-12-19
 * @version 0.1
 * @author Kevin Qiao, Candice Zhang
 */
@SuppressWarnings("serial")
public class WorldPanel extends JPanel {
  public static final int hotbarCellSize = 64;
  public static final int hotbarGap = 4;

  private World worldToDisplay;
  private int tileWidth, tileHeight;
  private Point playerScreenPos;
  
  public WorldPanel(World worldToDisplay, int width, int height) {
    super();
    this.worldToDisplay = worldToDisplay;
    this.playerScreenPos = new Point(0, 0);

    StardubeEventListener listener = new StardubeEventListener(worldToDisplay, this);
    this.addKeyListener(listener);
    this.addMouseListener(listener);
    this.addMouseMotionListener(listener);

    this.setFocusable(true);
    this.grabFocus();

    this.setPreferredSize(new Dimension(width, height));
    this.tileWidth = (int)Math.ceil(((double)width)/Tile.getSize());
    this.tileHeight = (int)Math.ceil(((double)height)/Tile.getSize());

    this.setOpaque(true);
  }

  public static int clamp(int val, int min, int max) {
    return Math.max(min, Math.min(val, max));
  }

  @Override
  public void paintComponent(Graphics g) {
    g.setColor(Color.BLACK);
    g.fillRect(0, 0, this.getWidth(), this.getHeight());

    // rip

    Point playerPos = this.worldToDisplay.getPlayer().getPos();
    Area playerArea = this.worldToDisplay.getPlayerArea();

    int unboundedTileStartX = (int)Math.floor(playerPos.x-this.tileWidth/2);
    int unboundedTileStartY = (int)Math.floor(playerPos.y-this.tileHeight/2);

    int tileStartX = WorldPanel.clamp(unboundedTileStartX, 0, playerArea.getWidth()-this.tileWidth);
    int tileStartY = WorldPanel.clamp(unboundedTileStartY, 0, playerArea.getHeight()-this.tileHeight);

    int originX;
    int originY;

    if (playerPos.x < this.tileWidth/2-0.5) {
      originX = 0;
    } else if (playerPos.x > playerArea.getWidth()-this.tileWidth/2-0.5) {
      originX = 0;
    } else {
      originX = (int)((this.getWidth()/2)-(Tile.getSize()*(playerPos.x-tileStartX)))-(Tile.getSize()/2);
    }

    if (playerPos.y < this.tileHeight/2-0.5 || playerPos.y > playerArea.getHeight()-this.tileHeight/2-0.5) {
      originY = 0;
    } else {
      originY = (int)((this.getHeight()/2)-(Tile.getSize()*(playerPos.y-tileStartY)))-(Tile.getSize()/2);
    }

    // System.out.printf("%d %d, %d %d, %d %d, %.2f %.2f\n",
    //    unboundedTileStartX, unboundedTileStartY, tileStartX, tileStartY, originX, originY, playerPos.x, playerPos.y);
      
    int screenTileX = 0;
    int screenTileY = 0;

    Point selectedTile = this.worldToDisplay.getPlayer().getSelectedTile();
    // System.out.println(selectedTile);

    for (int y = tileStartY; y < Math.max(playerPos.y+this.tileHeight/2+1, tileStartY+this.tileHeight); ++y) {
      for (int x = tileStartX; x < Math.max(playerPos.x+this.tileWidth/2+1, tileStartX+this.tileWidth); ++x) {
        if (playerArea.inMap(x, y) && playerArea.getMapAt(x, y) != null) {
          g.drawImage(playerArea.getMapAt(x, y).getImage(),
                      originX+(screenTileX*Tile.getSize()),
                      originY+(screenTileY*Tile.getSize()), null);
          if (selectedTile != null && (int)selectedTile.x == x && (int)selectedTile.y == y) {
            Graphics2D g2 = (Graphics2D)g;
            g2.setStroke(new BasicStroke(4));
            g2.setColor(Color.RED);
            g2.drawRect(originX+(screenTileX*Tile.getSize())+2,
                        originY+(screenTileY*Tile.getSize())+2,
                        Tile.getSize()-4, Tile.getSize()-6);
          }
        }
        ++screenTileX;
      }
      screenTileX = 0;
      ++screenTileY;
    }
    
    g.setColor(Color.RED);
    this.playerScreenPos.x = (Tile.getSize()*(playerPos.x-tileStartX+(Player.getSize()/2.0))+originX);
    this.playerScreenPos.y = (Tile.getSize()*(playerPos.y-tileStartY+(Player.getSize()/2.0))+originY);
    g.fillRect((int)this.playerScreenPos.x, (int)this.playerScreenPos.y,
               (int)(2*Tile.getSize()*Player.getSize()),
               (int)(2*Tile.getSize()*Player.getSize()));
    g.setColor(Color.BLACK);
    g.fillRect(this.getWidth()/2, this.getHeight()/2, 1, 1);

    // hotbar stuff :))
    g.setColor(Color.GREEN);
    g.fillRect(this.getWidth()/2-6*(WorldPanel.hotbarCellSize + WorldPanel.hotbarGap), this.getHeight()*7/8,
              12*WorldPanel.hotbarCellSize + 13*WorldPanel.hotbarGap, WorldPanel.hotbarCellSize+WorldPanel.hotbarGap);
    for (int i = 0; i < 12; i++) {
      g.setColor(Color.BLACK);
      g.fillRect(this.getWidth()/2-6*(WorldPanel.hotbarCellSize+WorldPanel.hotbarGap)+i*WorldPanel.hotbarCellSize+(i+1)*WorldPanel.hotbarGap,
                this.getHeight()*7/8+WorldPanel.hotbarGap/2, WorldPanel.hotbarCellSize, WorldPanel.hotbarCellSize);
      // outlines selected item
      if (i == this.worldToDisplay.getPlayer().getSelectedItemId()){
        g.setColor(Color.RED);
        g.drawRect(this.getWidth()/2-6*(WorldPanel.hotbarCellSize+WorldPanel.hotbarGap)+i*WorldPanel.hotbarCellSize+(i+1)*WorldPanel.hotbarGap,
                this.getHeight()*7/8+WorldPanel.hotbarGap/2, WorldPanel.hotbarCellSize, WorldPanel.hotbarCellSize);
      }
    }
  }

  public Point getPlayerScreenPos() {
    return ((Point)this.playerScreenPos.clone());
  }
  
}
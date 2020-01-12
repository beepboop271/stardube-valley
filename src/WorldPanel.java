import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.Iterator;

import javax.swing.JPanel;

/**
 * [WorldPanel]
 * 2019-12-19
 * @version 0.3
 * @author Kevin Qiao, Candice Zhang, Paula Yuan
 */
@SuppressWarnings("serial")
public class WorldPanel extends JPanel {
  public static final int HOTBAR_CELLSIZE = 64;
  public static final int HOTBAR_CELLGAP = 4;
  public static final Color INVENTORY_BKGD_COLOR = new Color(155, 60, 0);
  public static final Color INVENTORY_SLOT_COLOR = new Color(255, 200, 120);
  public static final Color INVENTORY_QUANTITY_COLOR = new Color(60, 20, 0);
  public static final Color PALE_YELLOW_COLOR = new Color(250, 230, 200);
  public static final Color DARK_BROWN_COLOR = new Color(92, 55, 13);
  
  private final Font timeFont;
  private final Font quantityFont;
  private final Font letterFont;
  private final Font stringFont;
  private StardubeEventListener listener;
  private World worldToDisplay;
  private int tileWidth, tileHeight;
  private Point playerScreenPos;
  private int hotbarX, hotbarY;
  private int menuX, menuY, menuW, menuH;
  private int hoveredItemIdx;

  public WorldPanel(World worldToDisplay, int width, int height) {
    super();
    this.worldToDisplay = worldToDisplay;
    this.playerScreenPos = new Point(0, 0);
    this.menuW = 13*WorldPanel.HOTBAR_CELLGAP+12*WorldPanel.HOTBAR_CELLSIZE;
    this.menuH = 8*(WorldPanel.HOTBAR_CELLGAP+WorldPanel.HOTBAR_CELLSIZE);
    this.menuX = (width-this.menuW)/2;
    this.menuY = (height-this.menuH)/2;

    this.listener = new StardubeEventListener(worldToDisplay, this);
    this.addKeyListener(this.listener);
    this.addMouseListener(this.listener);
    this.addMouseMotionListener(this.listener);
    this.addMouseWheelListener(this.listener);

    this.setFocusable(true);
    this.grabFocus();

    this.setPreferredSize(new Dimension(width, height));
    this.tileWidth = (int)Math.ceil(((double)width)/Tile.getSize());
    this.tileHeight = (int)Math.ceil(((double)height)/Tile.getSize());

    this.timeFont = new Font("Comic Sans MS", Font.BOLD, 40);
    this.quantityFont = new Font("Comic Sans MS", Font.BOLD, 15);
    this.letterFont = new Font("Comic Sans MS", Font.BOLD, 25);
    this.stringFont = new Font("Comic Sans MS", Font.PLAIN, 20);
    this.hoveredItemIdx = -1;

    this.setOpaque(true);
  }

  public static int clamp(int val, int min, int max) {
    return Math.max(min, Math.min(val, max));
  }

  @Override
  public void paintComponent(Graphics g) {
    g.setColor(Color.BLACK);
    g.fillRect(0, 0, this.getWidth(), this.getHeight());

    Player worldPlayer = this.worldToDisplay.getPlayer();
    Point playerPos = worldPlayer.getPos();
    Area playerArea = this.worldToDisplay.getPlayerArea();
    int unboundedTileStartX = (int)Math.floor(playerPos.x-this.tileWidth/2);
    int unboundedTileStartY = (int)Math.floor(playerPos.y-this.tileHeight/2);

    int tileStartX = WorldPanel.clamp(unboundedTileStartX, 0, playerArea.getWidth()-this.tileWidth);
    int tileStartY = WorldPanel.clamp(unboundedTileStartY, 0, playerArea.getHeight()-this.tileHeight);

    int originX;
    int originY;
    
    if ((playerPos.x < this.tileWidth/2) || (playerPos.x > playerArea.getWidth()-this.tileWidth/2)) {
      originX = 0;
    } else {
      originX = (int)((this.getWidth()/2)-(Tile.getSize()*(playerPos.x-tileStartX)));
    }

    if ((playerPos.y < this.tileHeight/2) || (playerPos.y > playerArea.getHeight()-this.tileHeight/2)) {
      originY = 0;
    } else {
      originY = (int)((this.getHeight()/2)-(Tile.getSize()*(playerPos.y-tileStartY)));
    }

    // System.out.printf("%d %d, %d %d, %d %d, %.2f %.2f\n",
    //    unboundedTileStartX, unboundedTileStartY, tileStartX, tileStartY, originX, originY, playerPos.x, playerPos.y);
      
    int screenTileX = 0;
    int screenTileY = 0;

    Point selectedTile = worldPlayer.getSelectedTile();

    for (int y = tileStartY; y < Math.max(playerPos.y+this.tileHeight/2+1, tileStartY+this.tileHeight); ++y) {
      for (int x = tileStartX; x < Math.max(playerPos.x+this.tileWidth/2+1, tileStartX+this.tileWidth); ++x) {
        if (playerArea.inMap(x, y)) {
          Tile currentTile = playerArea.getMapAt(x, y);
          if (currentTile != null) {
            int drawX = originX+(screenTileX*Tile.getSize());
            int drawY = originY+(screenTileY*Tile.getSize());
            g.drawImage(currentTile.getImage(), drawX, drawY, null);
            TileComponent tileContent = currentTile.getContent();
            
            if (tileContent != null) {
              g.drawImage(((Drawable)tileContent).getImage(), //- consider offsets when you draw the image
                      drawX + ((Drawable)tileContent).getXOffset() * Tile.getSize(),
                      drawY + ((Drawable)tileContent).getYOffset() * Tile.getSize(), 
                      null); 
            }

            if (selectedTile != null && (int)selectedTile.x == x && (int)selectedTile.y == y) {
              Graphics2D g2 = (Graphics2D)g;
              g2.setStroke(new BasicStroke(4));
              g2.setColor(Color.RED);
              g2.drawRect(drawX+2, drawY+2, Tile.getSize()-4, Tile.getSize()-6);
            }
          }
        }
        ++screenTileX;
      }
      screenTileX = 0;
      ++screenTileY;
    }
    
    // draw items
    synchronized (playerArea.getItemsOnGroundList()) {
      Iterator<HoldableStackEntity> items = playerArea.getItemsOnGround();
      HoldableStackEntity nextItem;
      while (items.hasNext()) {
        nextItem = items.next();
        g.drawImage(nextItem.getImage(),
                    (int)(Tile.getSize()*(nextItem.getPos().x-tileStartX+0.5)-8+originX),
                    (int)(Tile.getSize()*(nextItem.getPos().y-tileStartY+0.5)-8+originY),
                    null);
      }
    }
    
    //g.setColor(Color.RED);
    this.playerScreenPos.x = (Tile.getSize()*(playerPos.x-tileStartX+0.5-(Player.SIZE))+originX);
    this.playerScreenPos.y = (Tile.getSize()*(playerPos.y-tileStartY+0.5-(Player.SIZE))+originY);
    //g.fillRect((int)this.playerScreenPos.x, (int)this.playerScreenPos.y,
    //           (int)(Tile.getSize()*2*Player.SIZE),
    //           (int)(Tile.getSize()*2*Player.SIZE));
    g.setColor(Color.BLACK);
    g.fillRect(this.getWidth()/2, this.getHeight()/2, 1, 1);
    g.drawImage(worldPlayer.getImage(), (int)this.playerScreenPos.x, (int)(this.playerScreenPos.y-(64*Player.SIZE)), null);


    // hotbar stuff :))
    hotbarX = this.getWidth()/2-6*(WorldPanel.HOTBAR_CELLSIZE + WorldPanel.HOTBAR_CELLGAP);
    if (this.playerScreenPos.y > this.getHeight()/2){
      this.hotbarY = WorldPanel.HOTBAR_CELLGAP*2;
    } else {
      this.hotbarY = this.getHeight()-WorldPanel.HOTBAR_CELLSIZE-WorldPanel.HOTBAR_CELLGAP*4;
    }
    g.setColor(WorldPanel.INVENTORY_BKGD_COLOR);
    g.fillRect(hotbarX, hotbarY,
               12*WorldPanel.HOTBAR_CELLSIZE + 13*WorldPanel.HOTBAR_CELLGAP,
               WorldPanel.HOTBAR_CELLSIZE+WorldPanel.HOTBAR_CELLGAP);
    for (int i = 0; i < 12; i++) {
      g.setColor(WorldPanel.INVENTORY_SLOT_COLOR);
      g.fillRect(hotbarX+i*WorldPanel.HOTBAR_CELLSIZE+(i+1)*WorldPanel.HOTBAR_CELLGAP,
                 hotbarY+WorldPanel.HOTBAR_CELLGAP/2,
                 WorldPanel.HOTBAR_CELLSIZE, WorldPanel.HOTBAR_CELLSIZE);
      // draw inventory holdables correspondingly
      if (worldPlayer.getInventory()[i] != null) {
        if (worldPlayer.getInventory()[i].getContainedHoldable() != null) {
          g.drawImage(worldPlayer.getInventory()[i].getContainedHoldable().getImage(),
                    hotbarX+i*WorldPanel.HOTBAR_CELLSIZE+(i+1)*WorldPanel.HOTBAR_CELLGAP,
                    hotbarY+WorldPanel.HOTBAR_CELLGAP/2, null);
        }
        // displays the quantity of the holdable (if > 1)
        if (worldPlayer.getInventory()[i].getQuantity() > 1) {
          Graphics2D hotbarQuantityGraphics = (Graphics2D)g;
          hotbarQuantityGraphics.setColor(WorldPanel.INVENTORY_QUANTITY_COLOR);
          hotbarQuantityGraphics.setFont(this.quantityFont);
          hotbarQuantityGraphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                              RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
          hotbarQuantityGraphics.drawString(Integer.toString(worldPlayer.getInventory()[i].getQuantity()),
                        hotbarX+(i+1)*(WorldPanel.HOTBAR_CELLSIZE+WorldPanel.HOTBAR_CELLGAP)-WorldPanel.HOTBAR_CELLSIZE/4,
                        hotbarY+WorldPanel.HOTBAR_CELLSIZE);
        }
      }

      // outlines selected item
      if (i == worldPlayer.getSelectedItemIdx()){
        g.setColor(Color.RED);
        g.drawRect(hotbarX+i*WorldPanel.HOTBAR_CELLSIZE+(i+1)*WorldPanel.HOTBAR_CELLGAP,
                   hotbarY+WorldPanel.HOTBAR_CELLGAP/2, WorldPanel.HOTBAR_CELLSIZE, WorldPanel.HOTBAR_CELLSIZE);
      }
    }

    // time stuff
    // one real world second is one in game minute
    long time = this.worldToDisplay.getInGameNanoTime()/1_000_000_000;
    Graphics2D g2 = (Graphics2D)g;
    g2.setColor(Color.BLACK);
    g2.setFont(this.timeFont);
    g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                        RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
    g2.drawString(String.format("%02d:%02d", time/60, time%60), this.getWidth()-130, 45);
    g2.drawString(World.getSeasons()[this.worldToDisplay.getInGameSeason()],
                  this.getWidth()-500, 45); 
    if (this.worldToDisplay.getInGameDay() % World.getDaysPerSeason() == 0) {
      g2.drawString("28", this.getWidth()-300, 45);
    } else {
      g2.drawString(String.valueOf(this.worldToDisplay.getInGameDay() % World.getDaysPerSeason()), 
                    this.getWidth()-300, 45);
    }
                  
    // inventory menu stuff
    if (worldPlayer.isInMenu()) {
      g.setColor(new Color(0, 0, 0, 100));
      g.fillRect(0, 0, this.getWidth(), this.getHeight());
      g.setColor(new Color(150, 75, 0));
      g.fillRect(this.menuX, this.menuY, this.menuW, this.menuH);
      // TODO: inv tab buttons (y: this.menuY)
      // inventory display (y:this.menuY+1~3(cellgap+cellsize))
      g.setColor(INVENTORY_BKGD_COLOR);
      g.fillRect(this.menuX, this.menuY, this.menuW, 3*(WorldPanel.HOTBAR_CELLSIZE+WorldPanel.HOTBAR_CELLGAP));
      for (int i = 0; i < 3; i++) {
        for (int j = 0; j < 12; j++) {
          // draw inventory item correspondingly
          if ((i*12+j)<worldPlayer.getInventory().length){
            g.setColor(WorldPanel.INVENTORY_SLOT_COLOR);
            g.fillRect(this.menuX+j*WorldPanel.HOTBAR_CELLSIZE+(j+1)*WorldPanel.HOTBAR_CELLGAP,
                      this.menuY+(i+1)*(WorldPanel.HOTBAR_CELLSIZE+WorldPanel.HOTBAR_CELLGAP),
                      WorldPanel.HOTBAR_CELLSIZE, WorldPanel.HOTBAR_CELLSIZE);
            if (worldPlayer.getInventory()[i*12+j] != null) {
              if (worldPlayer.getInventory()[i*12+j].getContainedHoldable() != null) {
                g.drawImage(worldPlayer.getInventory()[i*12+j].getContainedHoldable().getImage(),
                            this.menuX+j*WorldPanel.HOTBAR_CELLSIZE+(j+1)*WorldPanel.HOTBAR_CELLGAP,
                            this.menuY+(i+1)*(WorldPanel.HOTBAR_CELLSIZE+WorldPanel.HOTBAR_CELLGAP), null);
                // displays the quantity of the holdable (if > 1)
                if (worldPlayer.getInventory()[i*12+j].getQuantity() > 1) {
                  Graphics2D invMenuQuantityGraphics = (Graphics2D)g;
                  invMenuQuantityGraphics.setColor(WorldPanel.INVENTORY_QUANTITY_COLOR);
                  invMenuQuantityGraphics.setFont(this.quantityFont);
                  invMenuQuantityGraphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                                      RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                  invMenuQuantityGraphics.drawString(Integer.toString(worldPlayer.getInventory()[i*12+j].getQuantity()),
                                this.menuX+(j+1)*(WorldPanel.HOTBAR_CELLSIZE+WorldPanel.HOTBAR_CELLGAP)-WorldPanel.HOTBAR_CELLSIZE/4,
                                this.menuY+(i+1)*(WorldPanel.HOTBAR_CELLSIZE+WorldPanel.HOTBAR_CELLGAP)+WorldPanel.HOTBAR_CELLSIZE);
                }
              }
            }
          } else { // display locked slots in a different color
            g.setColor(new Color(230, 165, 100));
            g.fillRect(this.menuX+j*WorldPanel.HOTBAR_CELLSIZE+(j+1)*WorldPanel.HOTBAR_CELLGAP,
                      this.menuY+(i+1)*(WorldPanel.HOTBAR_CELLSIZE+WorldPanel.HOTBAR_CELLGAP),
                      WorldPanel.HOTBAR_CELLSIZE, WorldPanel.HOTBAR_CELLSIZE);
          }
          // outline selected item
          if ((i*12+j) == this.worldToDisplay.getPlayer().getSelectedItemIdx()){
            g.setColor(Color.RED);
            g.drawRect(this.menuX+j*WorldPanel.HOTBAR_CELLSIZE+(j+1)*WorldPanel.HOTBAR_CELLGAP,
                       this.menuY+(i+1)*(WorldPanel.HOTBAR_CELLSIZE+WorldPanel.HOTBAR_CELLGAP),
                       WorldPanel.HOTBAR_CELLSIZE, WorldPanel.HOTBAR_CELLSIZE);
          }
        }
      }
      // TODO: character/earning/date display (y:this.menuY+4~7(cellgap+cellsize))
    }
    
    if (worldPlayer.getSelectedItem() != null) {
      Holdable selectedItem = worldPlayer.getSelectedItem().getContainedHoldable();
      if (selectedItem instanceof FishingRod) {
        FishingRod playerCurrentRod = (FishingRod)selectedItem;
        if (playerCurrentRod.getCurrentStatus() == FishingRod.CASTING_STATUS) {
          // if player is casting, draw casting meter
          // TODO: make the display postion beside the player
          int meterW = this.getWidth()/10;
          int meterH = this.getHeight()/25;
          int meterX = (int)Math.round(playerScreenPos.x-meterW/2+Player.SIZE*Tile.getSize());
          int meterY = (int)Math.round(playerScreenPos.y)-meterH-15;
          g.setColor(new Color(0,0,0,175));
          g.fillRect(meterX, meterY, meterW, meterH);
          g.setColor(Color.GREEN);
          g.fillRect(meterX, meterY, (int)Math.round(meterW*playerCurrentRod.getCastingProgressPercentage()/100.0), meterH);
        } else if (playerCurrentRod.getCurrentStatus() == FishingRod.WAITING_STATUS) {
          g.setColor(Color.WHITE);
          int lineX, lineY;
          if (worldPlayer.getOrientation() == World.NORTH) {
            lineX = (int)Math.round(playerScreenPos.x+Player.SIZE*Tile.getSize());
            lineY = (int)Math.round(playerScreenPos.y);
          } else if (worldPlayer.getOrientation() == World.SOUTH) {
            lineX = (int)Math.round(playerScreenPos.x+Player.SIZE*Tile.getSize());
            lineY = (int)Math.round(playerScreenPos.y+Player.SIZE*Tile.getSize()*2);
          } else if (worldPlayer.getOrientation() == World.WEST) {
            lineX = (int)Math.round(playerScreenPos.x);
            lineY = (int)Math.round(playerScreenPos.y);
          } else { // EAST
            lineX = (int)Math.round(playerScreenPos.x+Player.SIZE*Tile.getSize()*2);
            lineY = (int)Math.round(playerScreenPos.y);
          }
          
          g.drawLine(lineX, lineY,
                     Tile.getSize()*(playerCurrentRod.getTileToFish().getX()-tileStartX)+originX+Tile.getSize()/2,
                     Tile.getSize()*(playerCurrentRod.getTileToFish().getY()-tileStartY)+originY+Tile.getSize()/2);

          // display exclamation mark if the fishable is biting
          if (worldPlayer.getCurrentFishingGame().isBiting()) {
            Graphics2D letterGraphics = (Graphics2D)g;
            letterGraphics.setColor(Color.RED);
            letterGraphics.setFont(this.letterFont);
            letterGraphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            letterGraphics.drawString("!", (int)Math.round(playerScreenPos.x), (int)Math.round(playerScreenPos.y));
          }
        }
      }
    }

    // display health and energy
    // TODO: different colors for different ranges of health/energy;
    // also letter color is a hmm idk how to make it stand out with both black and white backgrounds
    // ok i can think of ways that im too lazy to implement so whatever
    g.setColor(Color.YELLOW);
    g.fillRect(this.getWidth()-75, this.getHeight()-250, 45, (int)(worldPlayer.getEnergy()/1.0/worldPlayer.getMaxEnergy()*200));
    Graphics2D letterGraphics = (Graphics2D)g;
    letterGraphics.setColor(Color.WHITE);
    letterGraphics.setFont(this.letterFont);
    letterGraphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
    letterGraphics.drawString("E", this.getWidth()-60, this.getHeight()-20);
    if ((worldToDisplay.getPlayerArea() instanceof MineArea) || (worldPlayer.getHealth() < worldPlayer.getMaxHealth())) {
      g.setColor(Color.RED);
      g.fillRect(this.getWidth()-140, this.getHeight()-250, 45, (int)(worldPlayer.getHealth()/1.0/worldPlayer.getMaxHealth()*200));
      letterGraphics.setColor(Color.WHITE);
      letterGraphics.setFont(this.letterFont);
      letterGraphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
      letterGraphics.drawString("H", this.getWidth()-125, this.getHeight()-20);
    }
    // if player is in fishing game, draw mini game stuff
    // TODO: make the display postion beside the player
    if (worldPlayer.isInFishingGame() && worldPlayer.getCurrentFishingGame().hasStarted()) {
      // draw background
      g.setColor(new Color(255, 255, 255, 100));
      g.fillRect(0, 0, this.getWidth()/20, this.getHeight()/2);
      
      double barScale = 1.0/FishingGame.MAX_HEIGHT*(this.getHeight()/2);

      // draw player fishing bar
      FishingGameBar playerBar = worldPlayer.getCurrentFishingGame().getPlayerBar();
      g.setColor(Color.BLUE);
      g.fillRect(0, (int)(playerBar.getY()*barScale),
                 this.getWidth()/40, (int)(playerBar.getHeight()*barScale));

      // draw target fishing bar
      FishingGameBar targeBar = worldPlayer.getCurrentFishingGame().getTargetBar();
      g.setColor(Color.GREEN);
      g.fillRect(this.getWidth()/160, (int)(targeBar.getY()*barScale),
                 this.getWidth()/80, (int)(targeBar.getHeight()*barScale));

      // draw progress bar
      g.setColor(Color.RED);
      g.fillRect(this.getWidth()/40, this.getHeight()/2-this.getHeight()/2*worldPlayer.getCurrentFishingGame().getProgressPercentage()/100,
                 this.getWidth()/40, this.getHeight()/2*worldPlayer.getCurrentFishingGame().getProgressPercentage()/100);
    }

    // display description of hovered item
    if ((hoveredItemIdx != -1) && (worldPlayer.getInventory()[hoveredItemIdx]!=null)) {
      String name = worldPlayer.getInventory()[hoveredItemIdx].getContainedHoldable().getName();
      String description = worldPlayer.getInventory()[hoveredItemIdx].getContainedHoldable().getDescription();
      Graphics2D descriptionGraphics = (Graphics2D)g;
      descriptionGraphics.setColor(Color.BLACK);
      descriptionGraphics.setFont(this.stringFont);
      descriptionGraphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                          RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
      int stringX = this.hotbarX + this.hoveredItemIdx*(WorldPanel.HOTBAR_CELLSIZE+WorldPanel.HOTBAR_CELLGAP);
      int stringW = Math.max(descriptionGraphics.getFontMetrics().stringWidth(name),
                              descriptionGraphics.getFontMetrics().stringWidth(description));
      int stringH = descriptionGraphics.getFontMetrics().getHeight();
      int stringY;
      if ((!worldPlayer.isInMenu()) && this.hotbarY < this.getHeight()/2) {
        stringY = this.hotbarY + (WorldPanel.HOTBAR_CELLSIZE + WorldPanel.HOTBAR_CELLGAP*2 + stringH + 10);
      } else if ((!worldPlayer.isInMenu()) && this.hotbarY >= this.getHeight()/2) {
        stringY = this.hotbarY - (WorldPanel.HOTBAR_CELLSIZE + WorldPanel.HOTBAR_CELLGAP*2);
      } else {
        stringY = this.menuX - hoveredItemIdx/12*(WorldPanel.HOTBAR_CELLSIZE + WorldPanel.HOTBAR_CELLGAP*2);
      }

      g.setColor(WorldPanel.INVENTORY_BKGD_COLOR);
      g.fillRect(stringX-15, stringY-stringH-5, stringW+30, stringH*3+10);
      g.setColor(WorldPanel.PALE_YELLOW_COLOR);
      g.fillRect(stringX-10, stringY-stringH, stringW+20, stringH*3);
      g.setColor(Color.BLACK);
      descriptionGraphics.drawString(name, stringX, stringY);
      descriptionGraphics.drawString(description, stringX, stringY+stringH+5);
    }

  }

  public void updateHoveredItemIdx(int x, int y) {
    Player worldPlayer = this.worldToDisplay.getPlayer();
    if ((!worldPlayer.isInMenu()) && this.isPosInHotbar(x, y)) {
      this.hoveredItemIdx = this.hotbarItemIdxAt(x);
    } else if (worldPlayer.isInMenu() && this.isPosInMenuInventory(x, y)) {
      if ((this.inventoryMenuItemIdxAt(x, y) < worldPlayer.getInventorySize()) && (this.inventoryMenuItemIdxAt(x, y) >= 0)) {
        this.hoveredItemIdx = this.inventoryMenuItemIdxAt(x, y);
      }
    } else {
      this.hoveredItemIdx = -1;
    }
  }
  
  public int getHotbarX() {
    return this.hotbarX;
  }

  public int getHotbarY() {
    return this.hotbarY;
  }

  public int getMenuX() {
    return this.menuX;
  }

  public int getMenuY() {
    return this.menuY;
  }

  public int getMenuW() {
    return this.menuW;
  }

  public int getMenuH() {
    return this.menuH;
  }

  public Point getPlayerScreenPos() {
    return ((Point)this.playerScreenPos.clone());
  }

  public StardubeEventListener getListener() {
    return this.listener;
  } 

  public int hotbarItemIdxAt(int x) {
    return Math.min((int)(Math.floor((x-this.getHotbarX())/
           (WorldPanel.HOTBAR_CELLSIZE+WorldPanel.HOTBAR_CELLGAP))), 11);
  }

  public int inventoryMenuItemIdxAt(int x, int y) {
    return Math.min((int)(Math.floor(x-this.menuX)/ (WorldPanel.HOTBAR_CELLSIZE+WorldPanel.HOTBAR_CELLGAP)), 11)
           + 12*Math.min((int)(Math.floor((y-(this.menuY + (WorldPanel.HOTBAR_CELLSIZE + WorldPanel.HOTBAR_CELLGAP)))/
           (WorldPanel.HOTBAR_CELLSIZE+WorldPanel.HOTBAR_CELLGAP))), 2);
  }

  public boolean isPosInHotbar(int x, int y) {
    return ((x >= this.hotbarX) &&
            (x <= this.hotbarX+12*(WorldPanel.HOTBAR_CELLSIZE+WorldPanel.HOTBAR_CELLGAP)) &&
            (y >= this.hotbarY) &&
            (y <= this.hotbarY + WorldPanel.HOTBAR_CELLSIZE));
  }

  public boolean isPosInMenuTab(int x, int y) {
    return ((x >= this.menuX) &&
            (x <= this.menuX + this.menuW) &&
            (y >= this.menuY) &&
            (y <= this.menuY + (WorldPanel.HOTBAR_CELLSIZE + WorldPanel.HOTBAR_CELLGAP)));
  }

  public boolean isPosInMenuInventory(int x, int y) {
    return ((x >= this.menuX) &&
            (x <= this.menuX + this.menuW) &&
            (y > this.menuY + (WorldPanel.HOTBAR_CELLSIZE + WorldPanel.HOTBAR_CELLGAP)) &&
            (y <= this.menuY + 4*(WorldPanel.HOTBAR_CELLSIZE + WorldPanel.HOTBAR_CELLGAP)));
  }

}
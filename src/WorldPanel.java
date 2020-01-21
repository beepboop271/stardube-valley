import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JPanel;
import java.awt.AlphaComposite;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.io.BufferedReader;
import java.io.FileReader;
import java.awt.GradientPaint;
import java.util.ArrayList;
import java.util.Collections;

/**
 * [WorldPanel]
 * A JPanel for graphic display of a World.
 * 2020-01-20
 * @version 1.56
 * @author Kevin Qiao, Candice Zhang, Paula Yuan, Joseph Wang
 */
@SuppressWarnings("serial")
public class WorldPanel extends JPanel {
  public static final int INVENTORY_CELLSIZE = 64;
  public static final int INVENTORY_CELLGAP = 4;
  public static final int SHOP_ITEMS_PER_PAGE = 5;
  public static final int CRAFTING_ITEMS_PER_PAGE = 3;
  public static final int NPCS_PER_PAGE = 3;

  public static final Color MENU_BKGD_COLOR = new Color(140, 50, 0);
  public static final Color INVENTORY_SLOT_COLOR = new Color(255, 200, 120);
  public static final Color INVENTORY_TEXT_COLOR = new Color(60, 20, 0);
  public static final Color PROFILE_COLOR = new Color(245, 180, 110);
  public static final Color PALE_YELLOW_COLOR = new Color(250, 230, 200);
  public static final Color DARK_BROWN_COLOR = new Color(92, 55, 13);
  public static final Color PALE_GREEN_COLOR = new Color(130, 230, 0);
  public static final Color DIM_RED_COLOR = new Color(237, 69, 48);
  public static final Color LOCKED_SLOT_COLOR = new Color(230, 165, 100);

  private final Font TITLE_FONT =  new Font("Comic Sans MS", Font.BOLD, 30);
  private final Font QUANTITY_FONT = new Font("Comic Sans MS", Font.BOLD, 15);
  private final Font LETTER_FONT = new Font("Comic Sans MS", Font.BOLD, 25);
  private final Font BIG_LETTER_FONT = new Font("Comic Sans MS", Font.PLAIN, 35);
  private final Font BIG_BOLD_LETTER_FONT = new Font("Comic Sans MS", Font.BOLD, 35);
  private final Font STRING_FONT = new Font("Comic Sans MS", Font.PLAIN, 20);
  private final Font DESCRIPTION_BOLD_FONT = new Font("Comic Sans MS", Font.BOLD, 15);
  private final Font DESCRIPTION_FONT = new Font("Comic Sans MS", Font.PLAIN, 15);
  private final Font PROFILE_FONT = new Font("Comic Sans MS", Font.PLAIN, 30);
  
  private StardubeEventListener listener;
  private World worldToDisplay;
  private int tileWidth, tileHeight;
  private Point playerScreenPos;
  private int hotbarX, hotbarY;
  private int shopX, shopY, shopW, shopItemH;
  private int craftX, craftY, craftW, craftItemH;
  private int elevatorX, elevatorY;
  private int socialX, socialY, socialW, socialCellH;
  private int menuX, menuY, menuW, menuH;
  private int dialogueX, dialogueY, dialogueW, dialogueH;
  private int inventoryMenuInventoryY;
  private int chestMenuInventoryY;
  private int chestMenuChestY;
  private int hoveredItemIdx;
  private BufferedImage[] menuButtonImages;
  private BufferedImage craftButtonImage;

  /**
   * [WorldPanel]
   * Constructor for a new WorldPanel.
   * @param worldToDisplay World, the World used to display.
   * @param width          int, width of the panel.
   * @param height         int, height of the panel.
   */
  public WorldPanel(World worldToDisplay, int width, int height) {
    super();
    this.worldToDisplay = worldToDisplay;
    this.playerScreenPos = new Point(0, 0);

    this.menuW = 13*WorldPanel.INVENTORY_CELLGAP+12*WorldPanel.INVENTORY_CELLSIZE;
    this.menuH = 8*(WorldPanel.INVENTORY_CELLGAP+WorldPanel.INVENTORY_CELLSIZE);
    this.menuX = (width-this.menuW)/2;
    this.menuY = (height-this.menuH)/2;

    this.dialogueW = menuW;
    this.dialogueH = menuH/2;
    this.dialogueX = (width-this.dialogueW)/2;
    this.dialogueY = (height-this.dialogueH)/2;

    this.inventoryMenuInventoryY = this.menuY + (WorldPanel.INVENTORY_CELLSIZE + WorldPanel.INVENTORY_CELLGAP);
    this.chestMenuInventoryY = (int)Math.round(this.menuY + 0.5*(WorldPanel.INVENTORY_CELLSIZE + WorldPanel.INVENTORY_CELLGAP));
    this.chestMenuChestY = (int)Math.round(this.menuY + 4.5*(WorldPanel.INVENTORY_CELLSIZE + WorldPanel.INVENTORY_CELLGAP));

    this.shopX = this.menuX + (WorldPanel.INVENTORY_CELLGAP + WorldPanel.INVENTORY_CELLSIZE)/2;
    this.shopY = (int)Math.round(this.menuY + (WorldPanel.INVENTORY_CELLGAP + WorldPanel.INVENTORY_CELLSIZE)*1.5);
    this.shopW = 11*(WorldPanel.INVENTORY_CELLGAP + WorldPanel.INVENTORY_CELLSIZE);
    this.shopItemH = (int)Math.round((WorldPanel.INVENTORY_CELLGAP + WorldPanel.INVENTORY_CELLSIZE)*1.35);

    this.craftX = this.menuX + (WorldPanel.INVENTORY_CELLGAP + WorldPanel.INVENTORY_CELLSIZE)/2;
    this.craftY = this.menuY + (WorldPanel.INVENTORY_CELLGAP + WorldPanel.INVENTORY_CELLSIZE)*2;
    this.craftW = 11*(WorldPanel.INVENTORY_CELLGAP + WorldPanel.INVENTORY_CELLSIZE);
    this.craftItemH = (WorldPanel.INVENTORY_CELLGAP + WorldPanel.INVENTORY_CELLSIZE)*2;

    this.elevatorX = this.menuX+(WorldPanel.INVENTORY_CELLGAP + WorldPanel.INVENTORY_CELLSIZE)*4;
    this.elevatorY = this.menuY+(WorldPanel.INVENTORY_CELLGAP + WorldPanel.INVENTORY_CELLSIZE)*3;

    this.socialX = this.menuX + (WorldPanel.INVENTORY_CELLGAP + WorldPanel.INVENTORY_CELLSIZE)/2;
    this.socialY = (int)Math.round(this.menuY + (WorldPanel.INVENTORY_CELLGAP + WorldPanel.INVENTORY_CELLSIZE)*1.5);
    this.socialW = 11*(WorldPanel.INVENTORY_CELLGAP + WorldPanel.INVENTORY_CELLSIZE);
    this.socialCellH = (int)Math.round((WorldPanel.INVENTORY_CELLGAP + WorldPanel.INVENTORY_CELLSIZE)*2.25);

    this.menuButtonImages = new BufferedImage[4];

    try {
      BufferedReader input = new BufferedReader(new FileReader("assets/gamedata/Buttons"));
      String imagePath;
      for (int i = 0; i < this.menuButtonImages.length; i++) {
        imagePath = input.readLine();
        BufferedImage buttonImage = ImageIO.read(new File("assets/images/"+imagePath));
        this.menuButtonImages[i] = buttonImage;
      }
      this.craftButtonImage = ImageIO.read(new File("assets/images/"+input.readLine()));
      input.close();
    } catch (IOException e) {
      e.printStackTrace();
    }

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

    this.hoveredItemIdx = -1;

    this.setOpaque(true);
  }

  /**
   * [clamp]
   * Restricts a value to a given range and returns the result.
   * @author Kevin Qiao
   * @param val int, the value to clamp.
   * @param min int, the minimum allowed value.
   * @param max int, the maximum allowed value.
   * @return int, the clamped value.
   */
  public static int clamp(int val, int min, int max) {
    return Math.max(min, Math.min(val, max));
  }

  /**
   * [paintComponent]
   * Paints the graphics of the world on the screen.
   * @author Candice Zhang, Kevin Qiao, Paula Yuan
   * @param g The Graphics object that is used to paint graphics.
   */
  @Override
  public void paintComponent(Graphics g) {
    g.setColor(Color.BLACK);
    g.fillRect(0, 0, this.getWidth(), this.getHeight());

    Player worldPlayer = this.worldToDisplay.getPlayer();
    Point playerPos = worldPlayer.getPos();
    Area playerArea = this.worldToDisplay.getPlayerArea();
    int unboundedTileStartX = (int)Math.floor(playerPos.x-this.tileWidth/2+0.5);
    int unboundedTileStartY = (int)Math.floor(playerPos.y-this.tileHeight/2+0.5);

    int tileStartX = WorldPanel.clamp(unboundedTileStartX, 0, playerArea.getWidth()-this.tileWidth);
    int tileStartY = WorldPanel.clamp(unboundedTileStartY, 0, playerArea.getHeight()-this.tileHeight);

    int originX;
    int originY;
    
    if ((playerPos.x < this.tileWidth/2-0.5) || (playerPos.x > playerArea.getWidth()-this.tileWidth/2-0.5)) {
      originX = 0;
    } else {
      originX = (int)((this.getWidth()/2)-(Tile.getSize()*(playerPos.x-tileStartX+0.5)));
    }

    if ((playerPos.y < this.tileHeight/2-0.5) || (playerPos.y > playerArea.getHeight()-this.tileHeight/2-0.5)) {
      originY = 0;
    } else {
      originY = (int)((this.getHeight()/2)-(Tile.getSize()*(playerPos.y-tileStartY+0.5)));
    }
      
    int screenTileX = 0;
    int screenTileY = 0;

    Point selectedTile = worldPlayer.getSelectedTile();
    
    // draw building layout
    if (playerArea instanceof BuildingArea) {
      if (((BuildingArea)playerArea).hasInteriorImage()) {
        BuildingArea area = (BuildingArea)playerArea;
        g.drawImage(area.getImage(), 
                    (int)((area.getDrawLocation().x*Tile.getSize())+(area.getXOffset()*Tile.getSize())), 
                    (int)((area.getDrawLocation().y*Tile.getSize())+(area.getYOffset()*Tile.getSize())), null);
      }
    }

    ArrayList<HoldableStackEntity> items = playerArea.getItemsOnGroundList();
    Collections.sort(items);
    ArrayList<Moveable> moveables = playerArea.getMoveableList();
    Collections.sort(moveables);

    int moveableIdx = 0;
    while ((moveableIdx < moveables.size())
           && (Math.floor(moveables.get(moveableIdx).getPos().y) < tileStartY-1)) {
      ++moveableIdx;
    }
    int itemIdx = 0;
    while ((itemIdx < items.size())
           && (Math.floor(items.get(itemIdx).getPos().y) < tileStartY-1)) {
      ++itemIdx;
    }
    double nextX, nextY;

    // draw tiles
    for (int y = tileStartY; y < Math.max(playerPos.y+this.tileHeight/2+1, tileStartY+this.tileHeight); ++y) {
      if (!(playerArea instanceof BuildingArea)) {
        for (int x = tileStartX; x < Math.max(playerPos.x+this.tileWidth/2+1, tileStartX+this.tileWidth); ++x) {
          if (playerArea.inMap(x, y)) {
            Tile currentTile = playerArea.getMapAt(x, y);
            if (currentTile != null) {
              int drawX = originX+(screenTileX*Tile.getSize());
              int drawY = originY+(screenTileY*Tile.getSize());
              g.drawImage(currentTile.getImage(), drawX, drawY, null);
            }
          }
          ++screenTileX;
        }
        screenTileX = 0;
      }

      // tile components
      for (int x = tileStartX; x < Math.max(playerPos.x+this.tileWidth/2+1, tileStartX+this.tileWidth); ++x) {
        if (playerArea.inMap(x, y)) {
          Tile currentTile = playerArea.getMapAt(x, y);
          if (currentTile != null) {
            double drawX = originX+(screenTileX*Tile.getSize()); //- consider offsets when you draw the image
            double drawY = originY+(screenTileY*Tile.getSize());
            TileComponent tileContent = currentTile.getContent();
            if (selectedTile != null && (int)selectedTile.x == x && (int)selectedTile.y == y) {
              Graphics2D g2 = (Graphics2D)g;
              g2.setStroke(new BasicStroke(4));
              g2.setColor(Color.RED);
              g2.drawRect((int)Math.round(drawX+2), (int)Math.round(drawY+2), Tile.getSize()-4, Tile.getSize()-6);
            }

            if (tileContent != null) {
              if (tileContent instanceof Drawable) {
                drawX += ((Drawable)tileContent).getXOffset() * Tile.getSize();
                drawY += ((Drawable)tileContent).getYOffset() * Tile.getSize();
                Graphics2D imgGraphics = (Graphics2D)g;
                int playerW = worldPlayer.getImage().getWidth();
                int playerH = worldPlayer.getImage().getHeight();
                int componentW = ((Drawable)tileContent).getImage().getWidth();
                int componentH = ((Drawable)tileContent).getImage().getHeight();
                // if the player is overlapping with the tree or building, set a transparency for it
                if ((tileContent instanceof ExtrinsicTree) &&
                    (playerScreenPos.x < drawX + componentW) &&
                    (playerScreenPos.x + playerW > drawX) &&
                    (playerScreenPos.y < drawY + componentH*2/3) && // only include the top 6 tiles of the tree for overlapping detection
                    (playerScreenPos.y + playerH > drawY)) {
                  imgGraphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float)0.5));
                  imgGraphics.drawImage(((Drawable)tileContent).getImage(), (int)Math.round(drawX), (int)Math.round(drawY), null);
                  imgGraphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float)1)); // reset opacity
                } else if ((tileContent instanceof Building) &&
                          (playerScreenPos.x < drawX + componentW) &&
                          (playerScreenPos.x + playerW > drawX) &&
                          (playerScreenPos.y < drawY + componentH) &&
                          (playerScreenPos.y + playerH > drawY)) {
                  imgGraphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float)0.5));
                  imgGraphics.drawImage(((Drawable)tileContent).getImage(), (int)Math.round(drawX), (int)Math.round(drawY), null);
                  imgGraphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float)1)); // reset opacity
                } else {
                  imgGraphics.drawImage(((Drawable)tileContent).getImage(), (int)Math.round(drawX), (int)Math.round(drawY), null);
                }     
              }
            }
          }
        }
        ++screenTileX;
      }
      screenTileX = 0;

        // draw moveables
        while ((moveableIdx < moveables.size())
               && (Math.floor(moveables.get(moveableIdx).getPos().y) == y-1)) {
          nextX = (Tile.getSize()*(moveables.get(moveableIdx).getPos().x-tileStartX+moveables.get(moveableIdx).getXOffset())+originX);
          nextY = (Tile.getSize()*(moveables.get(moveableIdx).getPos().y-tileStartY+moveables.get(moveableIdx).getYOffset())+originY);
          g.drawImage(moveables.get(moveableIdx).getImage(), (int)nextX, (int)nextY, null);
          ++moveableIdx;
        }

        // draw items
        while ((itemIdx < items.size())
               && (Math.floor(items.get(itemIdx).getPos().y) == y-1)) {
          g.drawImage(items.get(itemIdx).getImage(),
                      (int)(Tile.getSize()*(items.get(itemIdx).getPos().x-tileStartX+0.5)-8+originX),
                      (int)(Tile.getSize()*(items.get(itemIdx).getPos().y-tileStartY+0.5)-8+originY),
                      null);
          ++itemIdx;
        }

      ++screenTileY;
    }

    this.playerScreenPos.x = Tile.getSize()*(playerPos.x-tileStartX+0.5)+originX;
    this.playerScreenPos.y = Tile.getSize()*(playerPos.y-tileStartY+0.5)+originY;

    g.setColor(Color.RED);
    g.drawRect((int)((Tile.getSize()*(playerPos.x-tileStartX+0.5-Player.SIZE)+originX)),
               (int)((Tile.getSize()*(playerPos.y-tileStartY+0.5-Player.SIZE)+originY)), (int)(2*Player.SIZE*Tile.getSize()), (int)(2*Player.SIZE*Tile.getSize()));
   

    // hotbar stuff 
    hotbarX = this.getWidth()/2-6*(WorldPanel.INVENTORY_CELLSIZE + WorldPanel.INVENTORY_CELLGAP);
    if ((this.playerScreenPos.y-Player.SIZE*Tile.getSize()) > this.getHeight()/2){
      this.hotbarY = WorldPanel.INVENTORY_CELLGAP*2;
    } else {
      this.hotbarY = this.getHeight()-WorldPanel.INVENTORY_CELLSIZE-WorldPanel.INVENTORY_CELLGAP*4;
    }
    g.setColor(WorldPanel.MENU_BKGD_COLOR);
    g.fillRect(hotbarX, hotbarY,
               12*WorldPanel.INVENTORY_CELLSIZE + 13*WorldPanel.INVENTORY_CELLGAP,
               WorldPanel.INVENTORY_CELLSIZE+WorldPanel.INVENTORY_CELLGAP);
    for (int i = 0; i < 12; i++) {
      g.setColor(WorldPanel.INVENTORY_SLOT_COLOR);
      g.fillRect(hotbarX+i*WorldPanel.INVENTORY_CELLSIZE+(i+1)*WorldPanel.INVENTORY_CELLGAP,
                 hotbarY+WorldPanel.INVENTORY_CELLGAP/2,
                 WorldPanel.INVENTORY_CELLSIZE, WorldPanel.INVENTORY_CELLSIZE);
      // draw inventory holdables correspondingly
      if (worldPlayer.getInventory()[i] != null) {
        if (worldPlayer.getInventory()[i].getContainedHoldable() != null) {
          g.drawImage(worldPlayer.getInventory()[i].getContainedHoldable().getImage(),
                    hotbarX+i*WorldPanel.INVENTORY_CELLSIZE+(i+1)*WorldPanel.INVENTORY_CELLGAP,
                    hotbarY+WorldPanel.INVENTORY_CELLGAP/2, null);
        }
        // displays the quantity of the holdable (if > 1)
        if (worldPlayer.getInventory()[i].getQuantity() > 1) {
          Graphics2D quantityGraphics = (Graphics2D)g;
          quantityGraphics.setColor(WorldPanel.INVENTORY_TEXT_COLOR);
          quantityGraphics.setFont(this.QUANTITY_FONT);
          quantityGraphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                              RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
          quantityGraphics.drawString(Integer.toString(worldPlayer.getInventory()[i].getQuantity()),
                        hotbarX+(i+1)*(WorldPanel.INVENTORY_CELLSIZE+WorldPanel.INVENTORY_CELLGAP)-WorldPanel.INVENTORY_CELLSIZE/4,
                        hotbarY+WorldPanel.INVENTORY_CELLSIZE);
        }
      }

      // outlines selected item
      if (i == worldPlayer.getSelectedItemIdx()){
        g.setColor(Color.RED);
        g.drawRect(hotbarX+i*WorldPanel.INVENTORY_CELLSIZE+(i+1)*WorldPanel.INVENTORY_CELLGAP,
                   hotbarY+WorldPanel.INVENTORY_CELLGAP/2, WorldPanel.INVENTORY_CELLSIZE, WorldPanel.INVENTORY_CELLSIZE);
      }
    }

    // time, day, current funds
    // (one real world second is one in game minute)
    long time = this.worldToDisplay.getInGameNanoTime()/1_000_000_000;
    Graphics2D g2 = (Graphics2D)g;
    g2.setFont(this.TITLE_FONT);
    String dateString = World.getSeasons()[this.worldToDisplay.getInGameSeason()];
    if (this.worldToDisplay.getInGameDay() % World.getDaysPerSeason() == 0) {
      dateString += ", Day 28";
    } else {
      dateString += ", Day " + String.valueOf(this.worldToDisplay.getInGameDay() % World.getDaysPerSeason());
    }
    String timeString = "Time: " + String.format("%02d:%02d", time/60, time%60);
    String fundsString = "Funds: " + Integer.toString(worldPlayer.getCurrentFunds())+" D$";

    int dateStringWidth = g2.getFontMetrics().stringWidth(dateString);
    int timeStringWidth = g2.getFontMetrics().stringWidth(timeString);
    int fundsStringWidth = g2.getFontMetrics().stringWidth(fundsString);
    int boxWidth = 35 + Math.max(Math.max(dateStringWidth, timeStringWidth), fundsStringWidth);

    g2.setColor(new Color(255, 255, 255, 125));
    g2.fillRect(this.getWidth()-boxWidth, 0, boxWidth, 180);
    g2.setColor(new Color(0, 0, 0, 125));
    g2.drawRect(this.getWidth()-boxWidth, 0, boxWidth, 180);

    g2.setColor(Color.BLACK);
    g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                        RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
    g2.drawString(timeString, this.getWidth()-timeStringWidth-20, 45);
    g2.drawString(dateString, this.getWidth()-dateStringWidth-20, 100);
    g2.drawString(fundsString, 
                  this.getWidth()-g2.getFontMetrics().stringWidth(fundsString)-20,
                  155);
    
    if (worldPlayer.isInMenu()) {
      g.setColor(new Color(0, 0, 0, 100));
      g.fillRect(0, 0, this.getWidth(), this.getHeight());
      g.setColor(WorldPanel.MENU_BKGD_COLOR);
      if ((worldPlayer.getCurrentMenuPage() >= 0) && (worldPlayer.getCurrentMenuPage() <= this.menuButtonImages.length)) {
        g.fillRect(this.menuX, this.menuY+WorldPanel.INVENTORY_CELLSIZE, this.menuW, this.menuH);
        for (int i = 0; i < this.menuButtonImages.length; i++) {
          g.drawImage(this.menuButtonImages[i], this.menuX + i*WorldPanel.INVENTORY_CELLSIZE, this.menuY, null);
        }
      } else {
        g.fillRect(this.menuX, this.menuY, this.menuW, this.menuH);
      }
      
      if (worldPlayer.getCurrentMenuPage() == Player.INVENTORY_PAGE) {
        // inventory display (y:this.menuY+1~3(cellgap+cellsize))
        for (int i = 0; i < 3; i++) {
          for (int j = 0; j < 12; j++) {
            // draw inventory item correspondingly
            if ((i*12+j)<worldPlayer.getInventory().length){
              g.setColor(WorldPanel.INVENTORY_SLOT_COLOR);
              g.fillRect(this.menuX+j*WorldPanel.INVENTORY_CELLSIZE+(j+1)*WorldPanel.INVENTORY_CELLGAP,
                        this.menuY+(i+1)*(WorldPanel.INVENTORY_CELLSIZE+WorldPanel.INVENTORY_CELLGAP),
                        WorldPanel.INVENTORY_CELLSIZE, WorldPanel.INVENTORY_CELLSIZE);
              if (worldPlayer.getInventory()[i*12+j] != null) {
                g.drawImage(worldPlayer.getInventory()[i*12+j].getContainedHoldable().getImage(),
                            this.menuX+j*WorldPanel.INVENTORY_CELLSIZE+(j+1)*WorldPanel.INVENTORY_CELLGAP,
                            this.menuY+(i+1)*(WorldPanel.INVENTORY_CELLSIZE+WorldPanel.INVENTORY_CELLGAP), null);
                // displays the quantity of the holdable (if > 1)
                if (worldPlayer.getInventory()[i*12+j].getQuantity() > 1) {
                  Graphics2D quantityGraphics = (Graphics2D)g;
                  quantityGraphics.setColor(WorldPanel.INVENTORY_TEXT_COLOR);
                  quantityGraphics.setFont(this.QUANTITY_FONT);
                  quantityGraphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                                      RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                  quantityGraphics.drawString(Integer.toString(worldPlayer.getInventory()[i*12+j].getQuantity()),
                                this.menuX+(j+1)*(WorldPanel.INVENTORY_CELLSIZE+WorldPanel.INVENTORY_CELLGAP)-WorldPanel.INVENTORY_CELLSIZE/4,
                                this.menuY+(i+1)*(WorldPanel.INVENTORY_CELLSIZE+WorldPanel.INVENTORY_CELLGAP)+WorldPanel.INVENTORY_CELLSIZE);
                }
              }
            } else { // display locked slots in a different color
              g.setColor(WorldPanel.LOCKED_SLOT_COLOR);
              g.fillRect(this.menuX+j*WorldPanel.INVENTORY_CELLSIZE+(j+1)*WorldPanel.INVENTORY_CELLGAP,
                        this.menuY+(i+1)*(WorldPanel.INVENTORY_CELLSIZE+WorldPanel.INVENTORY_CELLGAP),
                        WorldPanel.INVENTORY_CELLSIZE, WorldPanel.INVENTORY_CELLSIZE);
            }
            // outline selected item
            if ((i*12+j) == worldPlayer.getSelectedItemIdx()){
              g.setColor(Color.RED);
              g.drawRect(this.menuX+j*WorldPanel.INVENTORY_CELLSIZE+(j+1)*WorldPanel.INVENTORY_CELLGAP,
                        this.menuY+(i+1)*(WorldPanel.INVENTORY_CELLSIZE+WorldPanel.INVENTORY_CELLGAP),
                        WorldPanel.INVENTORY_CELLSIZE, WorldPanel.INVENTORY_CELLSIZE);
            }
          }
        }
        int profileX = (int)Math.round(this.menuX+0.5*WorldPanel.INVENTORY_CELLSIZE);
        int profileY = (int)Math.round(this.menuY+4.5*(WorldPanel.INVENTORY_CELLSIZE+WorldPanel.INVENTORY_CELLGAP));
        int profileW = this.menuW-WorldPanel.INVENTORY_CELLSIZE;
        int profileH = 4*(WorldPanel.INVENTORY_CELLSIZE+WorldPanel.INVENTORY_CELLGAP);
        String farmString = "Stardube Farm";
        String fundString = "Current Funds: " + worldPlayer.getCurrentFunds() + " $D";
        String earningString = "Total Earnings: " + worldPlayer.getTotalEarnings() + "$D";

        g.setColor(WorldPanel.PROFILE_COLOR);
        g.fillRect(profileX, profileY, profileW, profileH);
        g.drawImage(worldPlayer.getImage(), profileX+100, profileY+50, null);

        Graphics2D profileGraphics = (Graphics2D)g;
        profileGraphics.setColor(WorldPanel.INVENTORY_TEXT_COLOR);
        profileGraphics.setFont(this.PROFILE_FONT);
        profileGraphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        profileGraphics.drawString(farmString,
                  (int)Math.round(profileX+profileW-g.getFontMetrics().stringWidth(farmString)*1.75), (int)Math.round(profileY+profileH*0.25));
        profileGraphics.drawString(fundString,
                  (int)Math.round(profileX+profileW-g.getFontMetrics().stringWidth(fundString)*1.3), (int)Math.round(profileY+profileH*0.5));
        profileGraphics.drawString(earningString,
                  (int)Math.round(profileX+profileW-g.getFontMetrics().stringWidth(earningString)*1.3), (int)Math.round(profileY+profileH*0.75));
        
      } else if (worldPlayer.getCurrentMenuPage() == Player.CRAFTING_PAGE) {

        if (worldPlayer.getCurrentInteractingObj() instanceof CraftingMachine) {
          CraftingMachine craftingMachine = worldPlayer.getCraftingMachine();
          String[] products = craftingMachine.getProducts();
          g.setFont(this.TITLE_FONT);
          g.setColor(WorldPanel.MENU_BKGD_COLOR);
          g.fillRect(this.menuX, this.menuY+WorldPanel.INVENTORY_CELLSIZE, this.menuW, this.menuH);
          g.setColor(WorldPanel.PALE_YELLOW_COLOR);
          g.drawString("Crafting Menu Page", this.craftX, this.craftY-20);
        
          int startIdx = worldPlayer.getAmountScrolled();
          for (int i = 0; i < WorldPanel.CRAFTING_ITEMS_PER_PAGE; i++) {
            int curDrawY = this.craftY + i*(this.craftItemH+WorldPanel.INVENTORY_CELLGAP);
            g.setColor(WorldPanel.INVENTORY_SLOT_COLOR);
            g.fillRect(this.craftX, curDrawY, this.craftW, this.craftItemH);
            if ((startIdx+i) < products.length) {   
              Holdable product = HoldableFactory.getHoldable(products[startIdx+i]);
              String[] ingredients = craftingMachine.ingredientsOf(products[startIdx+i]);
              g.drawImage(product.getImage(), this.craftX, curDrawY, null);
              Graphics2D textGraphics = (Graphics2D)g;
              textGraphics.setColor(WorldPanel.INVENTORY_TEXT_COLOR);
              textGraphics.setFont(this.STRING_FONT);
              textGraphics.drawString(product.getName() + " - " + product.getDescription(),
                                      this.craftX + WorldPanel.INVENTORY_CELLSIZE*4/3, curDrawY + WorldPanel.INVENTORY_CELLSIZE/2);
              int ingredientWidth = (this.craftW-WorldPanel.INVENTORY_CELLSIZE-this.craftButtonImage.getWidth()) / ingredients.length;
              for ( int j = 0; j < ingredients.length; j++) {
                Holdable ingredient = HoldableFactory.getHoldable(ingredients[j]);
                g.drawImage(ingredient.getImage(), this.craftX+j*ingredientWidth+WorldPanel.INVENTORY_CELLSIZE,
                                                  curDrawY + WorldPanel.INVENTORY_CELLSIZE/2, null);
                g.drawString("x "+Integer.toString(craftingMachine.recipeOf(products[startIdx+i]).quantityOf(ingredients[j])),
                            this.craftX+j*ingredientWidth+WorldPanel.INVENTORY_CELLSIZE+10, (int)Math.round(curDrawY + WorldPanel.INVENTORY_CELLSIZE*1.8));
              }
              g.drawImage(this.craftButtonImage, this.craftX+this.craftW-this.craftButtonImage.getWidth(),
                          curDrawY+this.craftItemH-this.craftButtonImage.getHeight(), null);
            }
          }

        } else if (worldPlayer.getCurrentInteractingObj() instanceof CraftingStore) {        
          CraftingStore craftingStore = (CraftingStore)(worldPlayer.getCurrentInteractingObj());
          String[] storeItems = craftingStore.getItems();
          g.setColor(WorldPanel.MENU_BKGD_COLOR);
          g.fillRect(this.menuX, this.menuY, this.menuW, this.menuH);
          g.setColor(WorldPanel.INVENTORY_SLOT_COLOR);
          g.setFont(this.BIG_LETTER_FONT);
          g.drawString("Welcome to "+craftingStore.getName()+" !", this.shopX, this.menuY+g.getFontMetrics().getHeight()+25);
          
          int startIdx = worldPlayer.getAmountScrolled();
          for (int i = 0; i < WorldPanel.CRAFTING_ITEMS_PER_PAGE; i++) {
            int curDrawY = this.craftY + i*(this.craftItemH+WorldPanel.INVENTORY_CELLGAP);
            g.setColor(WorldPanel.INVENTORY_SLOT_COLOR);
            g.fillRect(this.craftX, curDrawY, this.craftW, this.craftItemH);
            if ((startIdx+i) < storeItems.length) {   
              Holdable product = HoldableFactory.getHoldable(storeItems[startIdx+i]);
              Recipe recipe = craftingStore.recipeOf(storeItems[startIdx+i]);
              String[] ingredients = recipe.getIngredients();
              g.drawImage(product.getImage(), this.craftX, curDrawY, null);
              Graphics2D textGraphics = (Graphics2D)g;
              textGraphics.setColor(WorldPanel.INVENTORY_TEXT_COLOR);
              textGraphics.setFont(this.STRING_FONT);
              textGraphics.drawString(product.getName() + " - " + product.getDescription(),
                                      this.craftX + WorldPanel.INVENTORY_CELLSIZE*4/3, curDrawY + WorldPanel.INVENTORY_CELLSIZE/2);
              int ingredientWidth = (this.craftW-WorldPanel.INVENTORY_CELLSIZE-this.craftButtonImage.getWidth()) / ingredients.length;
              for ( int j = 0; j < ingredients.length; j++) {
                Holdable ingredient = HoldableFactory.getHoldable(ingredients[j]);
                g.drawImage(ingredient.getImage(), this.craftX+j*ingredientWidth+WorldPanel.INVENTORY_CELLSIZE,
                                                  curDrawY + WorldPanel.INVENTORY_CELLSIZE/2, null);
                g.drawString("x "+Integer.toString(craftingStore.recipeOf(storeItems[startIdx+i]).quantityOf(ingredients[j])),
                            this.craftX+j*ingredientWidth+WorldPanel.INVENTORY_CELLSIZE+10, (int)Math.round(curDrawY + WorldPanel.INVENTORY_CELLSIZE*1.8));
              }
              if (recipe.getPrice() > 0) {
                String priceString = "Cost: "+Double.toString(recipe.getPrice())+" $D";
                g.setColor(WorldPanel.DARK_BROWN_COLOR);
                g.setFont(this.STRING_FONT);
                g.drawString(priceString, this.craftX+this.craftW-g.getFontMetrics().stringWidth(priceString)-10,
                             curDrawY+this.craftItemH-this.craftButtonImage.getHeight()-10);
              }
              g.drawImage(this.craftButtonImage, this.craftX+this.craftW-this.craftButtonImage.getWidth(),
                          curDrawY+this.craftItemH-this.craftButtonImage.getHeight(), null);
            }
          }
        }

      } else if (worldPlayer.getCurrentMenuPage() == Player.MAP_PAGE) {
        String[][] worldMap = this.worldToDisplay.getWorldMap();
        int mapTileSize = 4;
        int startX = this.menuX + (WorldPanel.INVENTORY_CELLGAP+WorldPanel.INVENTORY_CELLSIZE)*2;
        int startY = this.menuY + WorldPanel.INVENTORY_CELLGAP*4 + WorldPanel.INVENTORY_CELLSIZE;
        int drawX = startX, drawY = startY;
        int maxHeight;

        int playerMapSize = 15;
        int playerMapX = -playerMapSize, playerMapY = -playerMapSize;

        for(int y = 0; y < worldMap.length; y++) {
          drawX = startX;
          maxHeight = 0;

          for(int x = 0; x < worldMap[y].length; x++) {
            Area curArea = this.worldToDisplay.getArea(worldMap[y][x]);
            if (curArea.getName() == this.worldToDisplay.getPlayerArea().getName()) {
              playerMapX = drawX + (int)Math.round(playerPos.x)*mapTileSize;
              playerMapY = drawY + (int)Math.round(playerPos.y)*mapTileSize;
            }

            Tile[][] areaMap = curArea.getMap();
            if(areaMap.length > maxHeight) {
              maxHeight = areaMap.length;
            }
            
            for(int i = 0; i < maxHeight; i++) {
              for(int j = 0; j < areaMap[0].length; j++) {
                Color colorToDraw;
                Color defaultColor = WorldPanel.PALE_YELLOW_COLOR;
                if ((i < areaMap.length) && (j < areaMap[i].length)) {
                  Tile curTile = areaMap[i][j];
                  colorToDraw = this.colorOf(curTile);
                  if (colorToDraw == null) {
                    colorToDraw = defaultColor;
                  }
                } else {
                  colorToDraw = defaultColor;
                }
                g.setColor(colorToDraw);
                g.fillRect(drawX+j*mapTileSize, drawY+i*mapTileSize, mapTileSize, mapTileSize);
                
              }
            }
            drawX += areaMap[0].length*mapTileSize;
            
          }
          drawY += maxHeight*mapTileSize;
        }
        // draw player
        g.setColor(Color.RED);
        g.fillRect(playerMapX-playerMapSize/2, playerMapY-playerMapSize/2, playerMapSize, playerMapSize);

      } else if (worldPlayer.getCurrentMenuPage() == Player.SOCIAL_PAGE) {
        int startIdx = worldPlayer.getAmountScrolled();
        for (int i = 0; i < WorldPanel.NPCS_PER_PAGE; i++) {
          int curDrawY = this.socialY + i*(this.socialCellH+WorldPanel.INVENTORY_CELLGAP);
          g.setColor(WorldPanel.INVENTORY_SLOT_COLOR);
          g.fillRect(this.socialX, curDrawY, this.socialW, this.socialCellH);
          if ((i+startIdx) < worldToDisplay.getNPCs().length) {
            NPC npcToDraw = worldToDisplay.getNPCs()[i+startIdx];
            String[] descriptionStrings = this.splitParagraph(70, npcToDraw.getProfileDescription());
            // draw image
            g.drawImage(npcToDraw.getProfileImage(), this.socialX+WorldPanel.INVENTORY_CELLSIZE/2, curDrawY, null);
            // draw name
            g.setColor(WorldPanel.DARK_BROWN_COLOR);
            g.setFont(this.STRING_FONT);
            g.drawString(npcToDraw.getName(), this.socialX+WorldPanel.INVENTORY_CELLSIZE*2,
                         curDrawY+WorldPanel.INVENTORY_CELLSIZE/4+g.getFontMetrics().getHeight());
            // draw description
            g.setFont(this.DESCRIPTION_BOLD_FONT);
            for (int idx = 0; idx < descriptionStrings.length; idx++) {
              g.drawString(descriptionStrings[idx], this.socialX+WorldPanel.INVENTORY_CELLSIZE*2,
                curDrawY+WorldPanel.INVENTORY_CELLSIZE+g.getFontMetrics().getHeight()+idx*(g.getFontMetrics().getHeight()+5));
            }
            
          }
        }
            

      } else if (worldPlayer.getCurrentMenuPage() == Player.SHOP_PAGE) {
        Shop shop = (Shop)(worldPlayer.getCurrentInteractingObj());
        String[] shopItems = shop.getItems();
        g.setColor(WorldPanel.MENU_BKGD_COLOR);
        g.fillRect(this.menuX, this.menuY, this.menuW, this.menuH);
        g.setColor(WorldPanel.INVENTORY_SLOT_COLOR);
        g.setFont(this.BIG_LETTER_FONT);
        g.drawString("Welcome to "+shop.getName()+" !", this.shopX, this.menuY+g.getFontMetrics().getHeight()+25);
        
        int startIdx = worldPlayer.getAmountScrolled();
        for (int i = 0; i < WorldPanel.SHOP_ITEMS_PER_PAGE; i++) {
          int curDrawY = this.shopY + i*(this.shopItemH+WorldPanel.INVENTORY_CELLGAP);
          g.setColor(WorldPanel.INVENTORY_SLOT_COLOR);
          g.fillRect(this.shopX, curDrawY, this.shopW, this.shopItemH);
          if ((startIdx+i) < shopItems.length) {
            Holdable itemToDraw = HoldableFactory.getHoldable(shopItems[startIdx+i]);
            g.drawImage(itemToDraw.getImage(), this.shopX, curDrawY, null);
            Graphics2D textGraphics = (Graphics2D)g;
            textGraphics.setColor(WorldPanel.INVENTORY_TEXT_COLOR);
            textGraphics.setFont(this.STRING_FONT);
            textGraphics.drawString(itemToDraw.getName().replace("Item", "") + ":  " + Double.toString(shop.getPriceOf(itemToDraw.getName())) + " $D",
                                    this.shopX + WorldPanel.INVENTORY_CELLSIZE*4/3, curDrawY + WorldPanel.INVENTORY_CELLSIZE/2);

            int yShift = 0;
            if (itemToDraw instanceof Consumable) {
              textGraphics.setFont(this.DESCRIPTION_FONT);
              String consumableText = "";
              if (((Consumable)itemToDraw).getEnergyGain()>0) {
                consumableText += "Energy Gain: " + ((Consumable)itemToDraw).getEnergyGain() + ", ";
              }
              if (((Consumable)itemToDraw).getHealthGain()>0) {
                consumableText += "Health Gain: " + ((Consumable)itemToDraw).getHealthGain() + ", ";
              }
              if (itemToDraw instanceof SpecialConsumable) {
                if (((SpecialConsumable)itemToDraw).getMaxEnergyGain() > 0) {
                  consumableText += "Max Energy Gain: " + (((SpecialConsumable)itemToDraw).getMaxEnergyGain() + ", ");
                }
                if (((SpecialConsumable)itemToDraw).getMaxHealthGain() > 0) {
                  consumableText += "Max Health Gain: " + (((SpecialConsumable)itemToDraw).getMaxHealthGain() + ", ");
                }
              }
              if (consumableText.length() > 0) {
                // add brackets and strip the comma at the end
                consumableText = "( " + consumableText.substring(0, consumableText.length()-2) + " )"; 
              }
              textGraphics.drawString(consumableText, this.shopX + WorldPanel.INVENTORY_CELLSIZE*4/3, curDrawY + WorldPanel.INVENTORY_CELLSIZE/2+25);
              yShift = 25;
            }
            
            textGraphics.setFont(this.DESCRIPTION_BOLD_FONT);
            String[] discriptionStrings = this.splitParagraph(75, "- " + itemToDraw.getDescription());
            for (int idx = 0; idx < discriptionStrings.length; idx++) {
              textGraphics.drawString(discriptionStrings[idx],
                  this.shopX + WorldPanel.INVENTORY_CELLSIZE + 15,
                  (int)Math.round(curDrawY + WorldPanel.INVENTORY_CELLSIZE*0.85) + idx*20 + yShift);
            }
          }
        }

      } else if (worldPlayer.getCurrentMenuPage() == Player.CHEST_PAGE) {
        ExtrinsicChest chest = (ExtrinsicChest)(worldPlayer.getCurrentInteractingObj());
        g.setColor(WorldPanel.MENU_BKGD_COLOR);
        g.fillRect(this.menuX, this.menuY, this.menuW, this.menuH);
        // inventory display (y:this.menuY+1/2~5/2(cellgap+cellsize))
        for (int i = 0; i < 3; i++) {
          for (int j = 0; j < 12; j++) {
            // draw inventory item correspondingly
            if ((i*12+j)<worldPlayer.getInventory().length){
              g.setColor(WorldPanel.INVENTORY_SLOT_COLOR);
              g.fillRect(this.menuX+j*WorldPanel.INVENTORY_CELLSIZE+(j+1)*WorldPanel.INVENTORY_CELLGAP,
                        this.menuY+(int)Math.round((i+0.5)*(WorldPanel.INVENTORY_CELLSIZE+WorldPanel.INVENTORY_CELLGAP)),
                        WorldPanel.INVENTORY_CELLSIZE, WorldPanel.INVENTORY_CELLSIZE);
              if (worldPlayer.getInventory()[i*12+j] != null) {
                g.drawImage(worldPlayer.getInventory()[i*12+j].getContainedHoldable().getImage(),
                            this.menuX+j*WorldPanel.INVENTORY_CELLSIZE+(j+1)*WorldPanel.INVENTORY_CELLGAP,
                            this.menuY+(int)Math.round((i+0.5)*(WorldPanel.INVENTORY_CELLSIZE+WorldPanel.INVENTORY_CELLGAP)), null);
                // displays the quantity of the holdable (if > 1)
                if (worldPlayer.getInventory()[i*12+j].getQuantity() > 1) {
                  Graphics2D quantityGraphics = (Graphics2D)g;
                  quantityGraphics.setColor(WorldPanel.INVENTORY_TEXT_COLOR);
                  quantityGraphics.setFont(this.QUANTITY_FONT);
                  quantityGraphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                                      RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                  quantityGraphics.drawString(Integer.toString(worldPlayer.getInventory()[i*12+j].getQuantity()),
                                this.menuX+(j+1)*(WorldPanel.INVENTORY_CELLSIZE+WorldPanel.INVENTORY_CELLGAP)-WorldPanel.INVENTORY_CELLSIZE/4,
                                this.menuY+(int)Math.round((i+0.5)*(WorldPanel.INVENTORY_CELLSIZE+WorldPanel.INVENTORY_CELLGAP))+WorldPanel.INVENTORY_CELLSIZE);
                }
              }
            } else { // display locked slots in a different color
              g.setColor(WorldPanel.LOCKED_SLOT_COLOR);
              g.fillRect(this.menuX+j*WorldPanel.INVENTORY_CELLSIZE+(j+1)*WorldPanel.INVENTORY_CELLGAP,
                        this.menuY+(int)Math.round((i+0.5)*(WorldPanel.INVENTORY_CELLSIZE+WorldPanel.INVENTORY_CELLGAP)),
                        WorldPanel.INVENTORY_CELLSIZE, WorldPanel.INVENTORY_CELLSIZE);
            }
            // outline selected item
            if ((i*12+j) == worldPlayer.getSelectedItemIdx()){
              g.setColor(Color.RED);
              g.drawRect(this.menuX+j*WorldPanel.INVENTORY_CELLSIZE+(j+1)*WorldPanel.INVENTORY_CELLGAP,
                        this.menuY+(int)Math.round((i+0.5)*(WorldPanel.INVENTORY_CELLSIZE+WorldPanel.INVENTORY_CELLGAP)),
                        WorldPanel.INVENTORY_CELLSIZE, WorldPanel.INVENTORY_CELLSIZE);
            }
          }
        }

        // chest inventory display (y:this.menuY+7/2~11/2(cellgap+cellsize))
        for (int i = 0; i < 3; i++) {
          for (int j = 0; j < 12; j++) {
            // draw inventory item correspondingly
            if ((i*12+j) < ExtrinsicChest.CHEST_SIZE){ // the other case wont happen, just to make sure it does not go out of bound
              g.setColor(WorldPanel.INVENTORY_SLOT_COLOR);
              g.fillRect(this.menuX+j*WorldPanel.INVENTORY_CELLSIZE+(j+1)*WorldPanel.INVENTORY_CELLGAP,
                        this.menuY+(int)Math.round((i+4.5)*(WorldPanel.INVENTORY_CELLSIZE+WorldPanel.INVENTORY_CELLGAP)),
                        WorldPanel.INVENTORY_CELLSIZE, WorldPanel.INVENTORY_CELLSIZE);
              if (chest.getInventory()[i*12+j] != null) {
                g.drawImage(chest.getInventory()[i*12+j].getContainedHoldable().getImage(),
                            this.menuX+j*WorldPanel.INVENTORY_CELLSIZE+(j+1)*WorldPanel.INVENTORY_CELLGAP,
                            this.menuY+(int)Math.round((i+4.5)*(WorldPanel.INVENTORY_CELLSIZE+WorldPanel.INVENTORY_CELLGAP)), null);
                // displays the quantity of the holdable (if > 1)
                if (chest.getInventory()[i*12+j].getQuantity() > 1) {
                  Graphics2D quantityGraphics = (Graphics2D)g;
                  quantityGraphics.setColor(WorldPanel.INVENTORY_TEXT_COLOR);
                  quantityGraphics.setFont(this.QUANTITY_FONT);
                  quantityGraphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                                      RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                                      quantityGraphics.drawString(Integer.toString(chest.getInventory()[i*12+j].getQuantity()),
                                this.menuX+(j+1)*(WorldPanel.INVENTORY_CELLSIZE+WorldPanel.INVENTORY_CELLGAP)-WorldPanel.INVENTORY_CELLSIZE/4,
                                this.menuY+(int)Math.round((i+4.5)*(WorldPanel.INVENTORY_CELLSIZE+WorldPanel.INVENTORY_CELLGAP))+WorldPanel.INVENTORY_CELLSIZE);
                }
              }
            }
          }
        }
      } else if (worldPlayer.getCurrentMenuPage() == Player.ELEVATOR_PAGE) {
        int floorNumber = 0;
        String stringToDisplay = "Mine Elevator: Select Floor Number";
        g.setColor(WorldPanel.INVENTORY_SLOT_COLOR);
        g.fillRect(this.menuX+15, this.menuY+15, this.menuW-30, this.menuH-30);
        g.setColor(WorldPanel.DARK_BROWN_COLOR);
        g.fillRect(this.elevatorX-20, this.elevatorY-20,
                   4*(WorldPanel.INVENTORY_CELLSIZE+WorldPanel.INVENTORY_CELLGAP)+40,
                   3*(WorldPanel.INVENTORY_CELLSIZE+WorldPanel.INVENTORY_CELLGAP)+40);
        g.setFont(this.BIG_LETTER_FONT);
        g.drawString(stringToDisplay,
                     this.menuX+(this.menuW-g.getFontMetrics().stringWidth(stringToDisplay))/2, this.elevatorY-50);

        g.setFont(this.BIG_BOLD_LETTER_FONT);
        for (int i = 0; i < 3; i++) {
          for (int j = 0; j < 4; j++) {
            floorNumber += 5;
            int drawX = this.elevatorX+j*WorldPanel.INVENTORY_CELLSIZE+(j+1)*WorldPanel.INVENTORY_CELLGAP;
            int drawY = this.elevatorY + (int)Math.round(i*(WorldPanel.INVENTORY_CELLSIZE+WorldPanel.INVENTORY_CELLGAP));
            if (this.worldToDisplay.getMines().hasElevatorFloorUnlocked(floorNumber)) {
              g.setColor(WorldPanel.PALE_GREEN_COLOR);
            } else {
              g.setColor(WorldPanel.DIM_RED_COLOR);
            }
            g.fillRect(drawX, drawY, WorldPanel.INVENTORY_CELLSIZE, WorldPanel.INVENTORY_CELLSIZE);
            g.setColor(WorldPanel.DARK_BROWN_COLOR);
            g.drawString(Integer.toString(floorNumber), drawX + 5, drawY + g.getFontMetrics().getHeight());
          }
        }
      }
    }
    
    if (worldPlayer.getSelectedItem() != null) {
      Holdable selectedItem = worldPlayer.getSelectedItem().getContainedHoldable();
      if (selectedItem instanceof FishingRod) {
        FishingRod playerCurrentRod = (FishingRod)selectedItem;
        if (playerCurrentRod.getCurrentStatus() == FishingRod.CASTING_STATUS) {
          // if player is casting, draw casting meter
          int meterW = this.getWidth()/10;
          int meterH = this.getHeight()/25;
          int meterX = (int)Math.round(playerScreenPos.x-meterW/2+Player.SIZE*Tile.getSize());
          int meterY = (int)Math.round(playerScreenPos.y)-worldPlayer.getImage().getHeight();
          g.setColor(new Color(0,0,0,175));
          g.fillRect(meterX, meterY, meterW, meterH);
          g.setColor(Color.GREEN);
          g.fillRect(meterX, meterY, (int)Math.round(meterW*playerCurrentRod.getCastingProgressPercentage()/100.0), meterH);
        } else if (playerCurrentRod.getCurrentStatus() == FishingRod.WAITING_STATUS) {
          g.setColor(Color.WHITE);
          int lineX, lineY;
          if (worldPlayer.getOrientation() == World.NORTH) {
            lineX = (int)Math.round(playerScreenPos.x+Player.SIZE*Tile.getSize()/2);
            lineY = (int)Math.round(playerScreenPos.y+Player.SIZE*Tile.getSize()*2-worldPlayer.getImage().getHeight());
          } else if (worldPlayer.getOrientation() == World.SOUTH) {
            lineX = (int)Math.round(playerScreenPos.x+Player.SIZE*Tile.getSize()/2);
            lineY = (int)Math.round(playerScreenPos.y+Player.SIZE*Tile.getSize()*2)-worldPlayer.getImage().getHeight()/2;
          } else if (worldPlayer.getOrientation() == World.WEST) {
            lineX = (int)Math.round(playerScreenPos.x);
            lineY = (int)Math.round(playerScreenPos.y);
          } else { // EAST
            lineX = (int)Math.round(playerScreenPos.x);
            lineY = (int)Math.round(playerScreenPos.y);
          }
          
          g.drawLine(lineX, lineY,
                     Tile.getSize()*(playerCurrentRod.getTileToFish().getX()-tileStartX)+originX+Tile.getSize()/2,
                     Tile.getSize()*(playerCurrentRod.getTileToFish().getY()-tileStartY)+originY+Tile.getSize()/2);

          // display exclamation mark if the fishable is biting
          if (worldPlayer.getCurrentFishingGame().isBiting()) {
            Graphics2D letterGraphics = (Graphics2D)g;
            letterGraphics.setColor(Color.RED);
            letterGraphics.setFont(this.BIG_BOLD_LETTER_FONT);
            letterGraphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            letterGraphics.drawString("!", (int)Math.round(playerScreenPos.x)+Tile.getSize()/4,
                            (int)Math.round(playerScreenPos.y+Player.SIZE*Tile.getSize()-worldPlayer.getImage().getHeight()));
          }
        }
      }
    }

    // display health and energy
    int barWidth = 45;
    int energyX = this.getWidth()-75;
    int energyH = Math.max(0, (int)Math.round(worldPlayer.getEnergy()/1.0/worldPlayer.getMaxEnergy()*200));
    int energyY = this.getHeight()-50-energyH;
    Graphics2D statusGraphics = (Graphics2D)g;

    // - energy
    g.setColor(new Color(255, 255, 255, 75));
    g.fillRect(energyX, this.getHeight()-250, barWidth, 200);
    statusGraphics.setPaint(new GradientPaint(energyX, this.getHeight()-250, WorldPanel.PALE_GREEN_COLOR, energyX, this.getHeight()-50, Color.YELLOW));
    statusGraphics.fillRect(energyX, energyY, barWidth, energyH);
    statusGraphics.setStroke(new BasicStroke(3));
    statusGraphics.setColor(WorldPanel.DARK_BROWN_COLOR);
    statusGraphics.drawRect(energyX, this.getHeight()-250, barWidth, 200);
    g.setFont(this.LETTER_FONT);
    g.setColor(Color.WHITE);
    g.drawString("E", this.getWidth()-60, this.getHeight()-20);
    
    // - health
    if ((worldToDisplay.getPlayerArea() instanceof MineLevel) || (worldPlayer.getHealth() < worldPlayer.getMaxHealth())) {
      int healthX = this.getWidth()-140;
      int healthH = Math.max(0, (int)Math.round(worldPlayer.getHealth()/1.0/worldPlayer.getMaxHealth()*200));
      int healthY = this.getHeight()-50-healthH;
      g.setColor(new Color(255, 255, 255, 75));
      g.fillRect(healthX, this.getHeight()-250, barWidth, 200);
      statusGraphics.setPaint(new GradientPaint(healthX, this.getHeight()-250, WorldPanel.PALE_GREEN_COLOR, healthX, this.getHeight()-50, Color.RED));
      statusGraphics.fillRect(healthX, healthY, barWidth, healthH);
      statusGraphics.setStroke(new BasicStroke(3));
      statusGraphics.setColor(WorldPanel.DARK_BROWN_COLOR);
      statusGraphics.drawRect(healthX, this.getHeight()-250, barWidth, 200);
      g.setColor(Color.WHITE);
      g.setFont(this.LETTER_FONT);
      g.drawString("H", this.getWidth()-125, this.getHeight()-20);
    }

    // if player is in fishing game, draw mini game stuff
    if (worldPlayer.isInFishingGame() && worldPlayer.getCurrentFishingGame().hasStarted()) {
      int gameX, gameY;
      int gameW = this.getWidth()/20;
      int gameH = this.getHeight()/2;
      int frameWidth = 10;
      if (playerScreenPos.x > this.getWidth()/2) {
        gameX = (int)Math.round(playerScreenPos.x-gameW*2);
      } else {
        gameX = (int)Math.round(playerScreenPos.x+gameW*2);
      }
      if (playerScreenPos.y+gameH/2 > this.getHeight()) {
        gameY = this.getHeight()-gameH;
      } else {
        gameY = (int)Math.round(playerScreenPos.y-gameH/2);
      }

      // draw background
      g.setColor(WorldPanel.PALE_YELLOW_COLOR);
      Graphics2D gameGraphics = (Graphics2D)g;
      gameGraphics.setStroke(new BasicStroke(frameWidth));
      gameGraphics.setColor(WorldPanel.DARK_BROWN_COLOR);
      gameGraphics.drawRect(gameX-frameWidth/2, gameY-frameWidth/2, gameW+frameWidth, gameH+frameWidth);
      gameGraphics.setStroke(new BasicStroke(frameWidth/2));
      gameGraphics.setColor(WorldPanel.PALE_YELLOW_COLOR);
      gameGraphics.drawRect(gameX-frameWidth/2, gameY-frameWidth/2, gameW+frameWidth, gameH+frameWidth);

      // draw game and game bars
      g.setColor(new Color(255, 255, 255, 150));
      g.fillRect(gameX, gameY, gameW, gameH);
      
      double barScale = 1.0/FishingGame.MAX_HEIGHT*(this.getHeight()/2);

      // player fishing bar
      FishingGameBar playerBar = worldPlayer.getCurrentFishingGame().getPlayerBar();
      g.setColor(Color.BLUE);
      g.fillRect(gameX, gameY+(int)(playerBar.getY()*barScale), gameW/2, (int)(playerBar.getHeight()*barScale));

      // target fishing bar
      FishingGameBar targeBar = worldPlayer.getCurrentFishingGame().getTargetBar();
      g.setColor(WorldPanel.PALE_GREEN_COLOR);
      g.fillRect(gameX+this.getWidth()/160, gameY+(int)(targeBar.getY()*barScale), gameW/4, (int)(targeBar.getHeight()*barScale));

      // draw progress bar
      gameGraphics.setPaint(new GradientPaint(gameX+this.getWidth()/40, gameY, WorldPanel.PALE_GREEN_COLOR, gameX+this.getWidth()/40, gameY+gameH, WorldPanel.DIM_RED_COLOR));
      gameGraphics.fillRect(gameX+this.getWidth()/40, (int)Math.round(gameY+gameH-gameH*worldPlayer.getCurrentFishingGame().getProgressPercentage()/100.0),
                 gameW/2, (int)Math.round(gameH*worldPlayer.getCurrentFishingGame().getProgressPercentage()/100.0));
    }

    // display description of hovered item
    if ((hoveredItemIdx != -1) && (worldPlayer.getInventory()[hoveredItemIdx]!=null)) {
      String name = worldPlayer.getInventory()[hoveredItemIdx].getContainedHoldable().getName();
      String description = worldPlayer.getInventory()[hoveredItemIdx].getContainedHoldable().getDescription();
      Graphics2D descriptionGraphics = (Graphics2D)g;
      descriptionGraphics.setColor(Color.BLACK);
      descriptionGraphics.setFont(this.STRING_FONT);
      descriptionGraphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                          RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
      
      int stringW = Math.max(descriptionGraphics.getFontMetrics().stringWidth(name),
                              descriptionGraphics.getFontMetrics().stringWidth(description));
      int stringH = descriptionGraphics.getFontMetrics().getHeight();
      int stringX = Math.min(this.hotbarX + this.hoveredItemIdx*(WorldPanel.INVENTORY_CELLSIZE+WorldPanel.INVENTORY_CELLGAP),
                             this.getWidth()-(stringW+30));
      int stringY = 0;
      if ((!worldPlayer.isInMenu()) && this.hotbarY < this.getHeight()/2) {
        stringY = this.hotbarY + (WorldPanel.INVENTORY_CELLSIZE + WorldPanel.INVENTORY_CELLGAP*2 + stringH + 10);
      } else if ((!worldPlayer.isInMenu()) && this.hotbarY >= this.getHeight()/2) {
        stringY = this.hotbarY - (WorldPanel.INVENTORY_CELLSIZE + WorldPanel.INVENTORY_CELLGAP*2);
      } else if (worldPlayer.getCurrentMenuPage() == Player.INVENTORY_PAGE) {
        stringY = this.inventoryMenuInventoryY + (int)Math.round((hoveredItemIdx/12+1.5)*(WorldPanel.INVENTORY_CELLSIZE + WorldPanel.INVENTORY_CELLGAP*2));
      } else if (worldPlayer.getCurrentMenuPage() == Player.CHEST_PAGE) {
        stringY = this.chestMenuInventoryY + (int)Math.round((hoveredItemIdx/12+1.5)*(WorldPanel.INVENTORY_CELLSIZE + WorldPanel.INVENTORY_CELLGAP*2));
      }

      g.setColor(WorldPanel.MENU_BKGD_COLOR);
      g.fillRect(stringX-15, stringY-stringH-5, stringW+30, stringH*3+10);
      g.setColor(WorldPanel.PALE_YELLOW_COLOR);
      g.fillRect(stringX-10, stringY-stringH, stringW+20, stringH*3);
      g.setColor(Color.BLACK);
      descriptionGraphics.drawString(name, stringX, stringY);
      descriptionGraphics.drawString(description, stringX, stringY+stringH+5);
    }

    if (worldPlayer.getCurrentInteractingObj() instanceof NPC) {
      Graphics2D dialogueGraphics = (Graphics2D)g;
      g.setColor(WorldPanel.DARK_BROWN_COLOR);
      g.fillRect(dialogueX, dialogueY, dialogueW, dialogueH);
      g.setColor(WorldPanel.PALE_YELLOW_COLOR);
      g.fillRect(dialogueX+10, dialogueY+10, dialogueW-20, dialogueH-20);
      
      NPC currentNPC = ((NPC)worldPlayer.getCurrentInteractingObj());

      g.drawImage(currentNPC.getProfileImage(),
                  dialogueX+dialogueW-currentNPC.getProfileImage().getWidth()-25,
                  dialogueY+dialogueH-currentNPC.getProfileImage().getHeight()-25,
                  null);
      g.setColor(Color.BLACK); 
      g.setFont(this.LETTER_FONT);
      dialogueGraphics.drawString(currentNPC.getName(), dialogueX+40, dialogueY+25+g.getFontMetrics().getHeight());

      g.setFont(this.STRING_FONT);
      String[] dialogueStrings = this.splitParagraph(60, currentNPC.getDialogue(currentNPC.getIndex()));
      for (int i = 0; i < dialogueStrings.length; i++) {
        dialogueGraphics.drawString(dialogueStrings[i],
                                    dialogueX+40, dialogueY+70+(i+1)*(g.getFontMetrics().getHeight()+10));
      }
    }
  }

  /**
   * [updateHoveredItemIdx]
   * Updates the index of inventory item that the mouse is currently hovering.
   * @author Candice Zhang
   * @param mouseX int, the x value of the mouse position
   * @param mouseY int, the x value of the mouse position
   */
  public void updateHoveredItemIdx(int mouseX, int mouseY) {
    Player worldPlayer = this.worldToDisplay.getPlayer();
    if ((!worldPlayer.isInMenu()) && this.isPosInHotbar(mouseX, mouseY)) {
      this.hoveredItemIdx = this.hotbarItemIdxAt(mouseX);
    } else if ((worldPlayer.getCurrentMenuPage() == Player.INVENTORY_PAGE)
               && this.isPosInInventory(this.menuX, this.inventoryMenuInventoryY, mouseX, mouseY)) {
      if ((this.inventoryItemIdxAt(this.menuX, this.inventoryMenuInventoryY, mouseX, mouseY) < worldPlayer.getInventorySize())
          && (this.inventoryItemIdxAt(this.menuX, this.inventoryMenuInventoryY, mouseX, mouseY) >= 0)) {
        this.hoveredItemIdx = this.inventoryItemIdxAt(this.menuX, this.inventoryMenuInventoryY, mouseX, mouseY);
      }
    } else if ((worldPlayer.getCurrentMenuPage() == Player.CHEST_PAGE)
               && this.isPosInInventory(this.menuX, this.chestMenuInventoryY, mouseX, mouseY)) {
      if ((this.inventoryItemIdxAt(this.menuX, this.chestMenuInventoryY, mouseX, mouseY) < worldPlayer.getInventorySize())
          && (this.inventoryItemIdxAt(this.menuX, this.chestMenuInventoryY, mouseX, mouseY) >= 0)) {
      this.hoveredItemIdx = this.inventoryItemIdxAt(this.menuX, this.chestMenuInventoryY, mouseX, mouseY);
      }  
    } else {
      this.hoveredItemIdx = -1;
    }
  }
  
  /**
   * [colorOf]
   * Retrieves the color that represents the given tile according to its type.
   * @author      Candice Zhang
   * @param tile  The Tile thats is used to determine the color.
   * @return      Color, the color that represents the given tile.
   */
  public Color colorOf(Tile tile) {
    if (tile == null) {
      return new Color(51, 51, 51);
    } else if (tile instanceof GroundTile) {
      return new Color(245, 177, 38);
    } else if ((tile instanceof GrassTile) ||
               ((tile.getContent() != null) && (tile.getContent() instanceof ExtrinsicTree))) {
      return new Color(33, 214, 82);
    } else if (tile instanceof WaterTile) {
      return new Color(25, 155, 210);
    } else if (tile instanceof MineGatewayTile) {
      return new Color(117, 100, 87);
    } else if (tile instanceof DecorationTile) {
      return new Color(236, 198, 135);
    } else {
      return null;
    }
  }

  /**
   * [getHotbarX]
   * Retrieves the x position of the hotbar.
   * @return int, the x position of the hotbar.
   */
  public int getHotbarX() {
    return this.hotbarX;
  }

  /**
   * [getHotbarY]
   * Retrieves the y position of the hotbar.
   * @return int, the y position of the hotbar.
   */
  public int getHotbarY() {
    return this.hotbarY;
  }

  /**
   * [getMenuX]
   * Retrieves the x position of the menu.
   * @return int, the x position of the menu.
   */
  public int getMenuX() {
    return this.menuX;
  }

  /**
   * [getMenuY]
   * Retrieves the y position of the menu.
   * @return int, the y position of the menu.
   */
  public int getMenuY() {
    return this.menuY;
  }

  /**
   * [getMenuW]
   * Retrieves the width of the menu.
   * @return int, the width of the menu.
   */
  public int getMenuW() {
    return this.menuW;
  }

  /**
   * [getMenuH]
   * Retrieves the height of the menu.
   * @return int, the height of the menu.
   */
  public int getMenuH() {
    return this.menuH;
  }

  /**
   * [getPlayerScreenPos]
   * Retrieves the player position in the panel window.
   * @return Point, the player position in the panel window.
   */
  public Point getPlayerScreenPos() {
    return ((Point)this.playerScreenPos.clone());
  }

  /**
   * [getListener]
   * Retrieves the StardubeEventListener of this panel.
   * @return StardubeEventListener, the StardubeEventListener of this panel.
   */
  public StardubeEventListener getListener() {
    return this.listener;
  } 

  /**
   * [hotbarItemIdxAt]
   * Retrieves the index of the hotbar item at the given x position.
   * @author    Candice Zhang
   * @param x   int, the x position used to calculate index.
   * @return    int, the index of the hotbar item at the given x position.
   */
  public int hotbarItemIdxAt(int x) {
    return Math.min((int)(Math.floor((x-this.hotbarX)/
           (WorldPanel.INVENTORY_CELLSIZE+WorldPanel.INVENTORY_CELLGAP))), 11);
  }

  /**
   * [menuTabButtonAt]
   * Retrieves the index of the menu tab button at the given x position.
   * @author    Candice Zhang
   * @param x   int, the x position used to calculate index.
   * @return    int, the index of the menu tab button at the given x position.
   */
  public int menuTabButtonAt(int x) {
    return Math.min((int)(Math.floor((x-this.menuX)/
           (WorldPanel.INVENTORY_CELLSIZE+WorldPanel.INVENTORY_CELLGAP))), this.menuButtonImages.length-1);
  }

  /**
   * [shopItemIdxAt]
   * Retrieves the index of the shop item at the given y position.
   * @author    Candice Zhang
   * @param y   int, the y position used to calculate index.
   * @return    int, the index of the shop item at the given y position.
   */
  public int shopItemIdxAt(int y) {
    return Math.min((int)(Math.floor((y-this.shopY) / (this.shopItemH+WorldPanel.INVENTORY_CELLGAP))), WorldPanel.SHOP_ITEMS_PER_PAGE-1);
  }

  /**
   * [craftingItemIdxAt]
   * Retrieves the index of the crafting item at the given y position.
   * @author    Candice Zhang
   * @param y   int, the y position used to calculate index.
   * @return    int, the index of the crafting item at the given y position.
   */
  public int craftingItemIdxAt(int y) {
    return Math.min((int)(Math.floor((y-this.craftY) / (this.craftItemH+WorldPanel.INVENTORY_CELLGAP))), WorldPanel.CRAFTING_ITEMS_PER_PAGE-1);
  }

  /**
   * [elevatorButtonIdxAt]
   * Retrieves the index of the elevator button at the given y position.
   * @author    Candice Zhang
   * @param x   int, the x position used to calculate index.
   * @param y   int, the y position used to calculate index.
   * @return    int, the index of the elevator button at the given y position.
   */
  public int elevatorButtonIdxAt(int x, int y) {
    return Math.min((int)(Math.floor(x-this.elevatorX)/ (WorldPanel.INVENTORY_CELLSIZE+WorldPanel.INVENTORY_CELLGAP)), 3)
           + 4*Math.min((int)(Math.floor((y-this.elevatorY)/
                                         (WorldPanel.INVENTORY_CELLSIZE+WorldPanel.INVENTORY_CELLGAP))), 2);
  }

  /**
   * [inventoryItemIdxAt]
   * Asks for the x and y location of the inventory and
   * the x and y location of the targeted point, and
   * retrieves the index of the inventory item at the targeted point.
   * @author    Candice Zhang
   * @param inventoryX   int, the x position of the inventory.
   * @param inventoryY   int, the y position of the inventory.
   * @param targetX      int, the x position of the targeted point.
   * @param targetY      int, the y position of the targeted point.
   * @return             int, the index of the inventory item at the targeted point.
   */
  public int inventoryItemIdxAt(int inventoryX, int inventoryY, int targetX, int targetY) {
    return Math.min((int)(Math.floor(targetX-inventoryX)/ (WorldPanel.INVENTORY_CELLSIZE+WorldPanel.INVENTORY_CELLGAP)), 11)
           + 12*Math.min((int)(Math.floor((targetY-inventoryY)/
                                          (WorldPanel.INVENTORY_CELLSIZE+WorldPanel.INVENTORY_CELLGAP))), 2);
  }
  
  /**
   * [isPosInHotbar]
   * Checks if the given position is inside the hotbar.
   * @author    Candice Zhang
   * @param x   int, the x position used to check.
   * @param y   int, the y position used to check.
   * @return    boolean, true if the given position is inside the hotbar,
   *            false otherwise.
   */
  public boolean isPosInHotbar(int x, int y) {
    return ((x >= this.hotbarX) &&
            (x <= this.hotbarX+12*(WorldPanel.INVENTORY_CELLSIZE+WorldPanel.INVENTORY_CELLGAP)) &&
            (y >= this.hotbarY) &&
            (y <= this.hotbarY + WorldPanel.INVENTORY_CELLSIZE));
  }

  /**
   * [isPosInHotbar]
   * Checks if the given position is inside the menu tab.
   * @author    Candice Zhang
   * @param x   int, the x position used to check.
   * @param y   int, the y position used to check.
   * @return    boolean, true if the given position is in the menu tab,
   *            false otherwise.
   */
  public boolean isPosInMenuTab(int x, int y) {
    return ((x >= this.menuX) &&
            (x <= this.menuX + this.menuW) &&
            (y >= this.menuY) &&
            (y <= this.inventoryMenuInventoryY));
  }

  /**
   * [isPosInInventory]
   * Asks for the x and y location of the inventory and
   * the x and y location of the targeted point, and
   * checks if the targeted point is inside the inventory.
   * @author    Candice Zhang
   * @param inventoryX   int, the x position of the inventory.
   * @param inventoryY   int, the y position of the inventory.
   * @param targetX      int, the x position of the targeted point.
   * @param targetY      int, the y position of the targeted point.
   * @return             boolean, true if the given position is the inventory,
   *                     false otherwise.
   */
  public boolean isPosInInventory(int inventoryX, int inventoryY, int targetX, int targetY) {
    return ((targetX >= inventoryX) &&
            (targetX <= inventoryX + (WorldPanel.INVENTORY_CELLSIZE+WorldPanel.INVENTORY_CELLGAP)*12) &&
            (targetY > inventoryY) &&
            (targetY <= inventoryY + 3*(WorldPanel.INVENTORY_CELLSIZE + WorldPanel.INVENTORY_CELLGAP)));
  }

  /**
   * [isPosInShopItemList]
   * Checks if the given position is inside the shop item list display.
   * @author    Candice Zhang
   * @param x   int, the x position used to check.
   * @param y   int, the y position used to check.
   * @return    boolean, true if the given position is in the shop item list display,
   *            false otherwise.
   */
  public boolean isPosInShopItemList(int x, int y) {
    return ((x >= this.shopX) &&
            (x <= this.shopX + this.shopW) &&
            (y >= this.shopY) &&
            (y <= this.shopY + this.shopItemH * WorldPanel.SHOP_ITEMS_PER_PAGE));
  }

  /**
   * [isPosInCraftButton]
   * Checks if the given position is inside any craft button location on the screen.
   * @author    Candice Zhang
   * @param x   int, the x position used to check.
   * @param y   int, the y position used to check.
   * @return    boolean, true if the given position is inside a craft button,
   *            false otherwise.
   */
  public boolean isPosInCraftButton(int x, int y) {
    for (int i = 0; i < WorldPanel.CRAFTING_ITEMS_PER_PAGE; i++) {
      if ((x >= this.craftX+this.craftW-this.craftButtonImage.getWidth()) &&
          (x <= this.craftX+this.craftW) &&
          (y >= this.craftY + i*(this.craftItemH+WorldPanel.INVENTORY_CELLGAP) + this.craftItemH-this.craftButtonImage.getHeight()) &&
          (y <= this.craftY + i*(this.craftItemH+WorldPanel.INVENTORY_CELLGAP) + this.craftItemH)) {
        return true;
      }
    }
    return false;
  }

  /**
   * [isPosInElevatorButtons]
   * Checks if the given position is inside any elevator button.
   * @author    Candice Zhang
   * @param x   int, the x position used to check.
   * @param y   int, the y position used to check.
   * @return    boolean, true if the given position is in an elevator button,
   *            false otherwise.
   */
  public boolean isPosInElevatorButtons(int x, int y) {
    return ((x >= this.elevatorX) &&
            (x <= this.elevatorX + (WorldPanel.INVENTORY_CELLSIZE+WorldPanel.INVENTORY_CELLGAP)*4) &&
            (y > this.elevatorY) &&
            (y <= this.elevatorY + (WorldPanel.INVENTORY_CELLSIZE + WorldPanel.INVENTORY_CELLGAP)*3));
  }

  /**
   * [getInventoryMenuInventoryY]
   * Retrieves the y position of the player's inventory in the inventory menu.
   * @return int, the y position of the player's inventory in the inventory menu.
   */
  public int getInventoryMenuInventoryY() {
    return this.inventoryMenuInventoryY;
  }

  /**
   * [getChestMenuInventoryY]
   * Retrieves the y position of the player's inventory in the chest menu.
   * @return int, the y position of the player's inventory in the chest menu.
   */
  public int getChestMenuInventoryY() {
    return this.chestMenuInventoryY;
  }

  /**
   * [getChestMenuChestY]
   * Retrieves the y position of the chest's inventory in the chest menu.
   * @return int, the y position of the chest's inventory in the chest menu.
   */
  public int getChestMenuChestY() {
    return this.chestMenuChestY;
  }

  /**
   * [splitParagraph]
   * Split a very long string into lines of at most maxLineWidth characters,
   * and retrieves the splited text in a String array.
   * @author             Kevin Qiao, Candice Zhang
   * @param paragraph    String that represents the string to split.
   * @param maxLineWidth int that represents the width limit.
   * @return             String[], splited text in a String array.
   */
  public String[] splitParagraph(int maxLineWidth, String paragraph) {
    // searching for a space " " to end the line
    int endOffset = -1;
    ArrayList<String> lines = new ArrayList<String>();
    String nextLine = "";
    while(paragraph.length() >= maxLineWidth) {
      nextLine = paragraph.substring(0, maxLineWidth);
      if (nextLine.charAt(nextLine.length()+endOffset) == ' ') {
        lines.add(nextLine.substring(0, nextLine.length()+endOffset));
        paragraph = paragraph.substring(maxLineWidth+endOffset+1);
        endOffset = -1;
      } else {
        --endOffset;
      }
    }
    lines.add(paragraph);
    return lines.toArray(new String[0]);
  }

}
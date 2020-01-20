import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.Iterator;
import javax.swing.JPanel;
import java.awt.AlphaComposite;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.io.BufferedReader;
import java.io.FileReader;
import java.awt.GradientPaint;

/**
 * [WorldPanel]
 * 2019-12-19
 * @version 0.4
 * @author Kevin Qiao, Candice Zhang, Paula Yuan, Joseph Wang
 */
@SuppressWarnings("serial")
public class WorldPanel extends JPanel {
  public static final int INVENTORY_CELLSIZE = 64;
  public static final int INVENTORY_CELLGAP = 4;
  public static final int SHOP_ITEMS_PER_PAGE = 5;
  public static final int CRAFTING_ITEMS_PER_PAGE = 3;

  public static final Color MENU_BKGD_COLOR = new Color(140, 50, 0);
  public static final Color INVENTORY_SLOT_COLOR = new Color(255, 200, 120);
  public static final Color INVENTORY_TEXT_COLOR = new Color(60, 20, 0);
  public static final Color PROFILE_COLOR = new Color(245, 180, 110);
  public static final Color PALE_YELLOW_COLOR = new Color(250, 230, 200);
  public static final Color DARK_BROWN_COLOR = new Color(92, 55, 13);
  public static final Color PALE_GREEN_COLOR = new Color(130, 230, 0);
  public static final Color DIM_RED_COLOR = new Color(237, 69, 48);

  private final Font TIME_FONT =  new Font("Comic Sans MS", Font.BOLD, 40);
  private final Font QUANTITY_FONT = new Font("Comic Sans MS", Font.BOLD, 15);
  private final Font LETTER_FONT = new Font("Comic Sans MS", Font.BOLD, 25);
  private final Font BIG_LETTER_FONT = new Font("Comic Sans MS", Font.PLAIN, 35);
  private final Font BIG_BOLD_LETTER_FONT = new Font("Comic Sans MS", Font.BOLD, 35);
  private final Font STRING_FONT = new Font("Comic Sans MS", Font.PLAIN, 20);
  private final Font DESCRIPTION_FONT = new Font("Comic Sans MS", Font.BOLD, 15);
  private final Font PROFILE_FONT = new Font("Comic Sans MS", Font.PLAIN, 30);
  
  private StardubeEventListener listener;
  private World worldToDisplay;
  private int tileWidth, tileHeight;
  private Point playerScreenPos;
  private int hotbarX, hotbarY;
  private int shopX, shopY, shopW, shopItemH;
  private int craftX, craftY, craftW, craftItemH;
  private int menuX, menuY, menuW, menuH;
  private int inventoryMenuInventoryY;
  private int chestMenuInventoryY;
  private int chestMenuChestY;
  private int hoveredItemIdx;
  private BufferedImage[] menuButtonImages;
  private BufferedImage craftButtonImage;

  public WorldPanel(World worldToDisplay, int width, int height) {
    super();
    this.worldToDisplay = worldToDisplay;
    this.playerScreenPos = new Point(0, 0);
    this.menuW = 13*WorldPanel.INVENTORY_CELLGAP+12*WorldPanel.INVENTORY_CELLSIZE;
    this.menuH = 8*(WorldPanel.INVENTORY_CELLGAP+WorldPanel.INVENTORY_CELLSIZE);
    this.menuX = (width-this.menuW)/2;
    this.menuY = (height-this.menuH)/2;
    this.inventoryMenuInventoryY = this.menuY + (WorldPanel.INVENTORY_CELLSIZE + WorldPanel.INVENTORY_CELLGAP);
    this.chestMenuInventoryY = (int)Math.round(this.menuY + 0.5*(WorldPanel.INVENTORY_CELLSIZE + WorldPanel.INVENTORY_CELLGAP));
    this.chestMenuChestY = (int)Math.round(this.menuY + 4.5*(WorldPanel.INVENTORY_CELLSIZE + WorldPanel.INVENTORY_CELLGAP));
    this.shopX = this.menuX + (WorldPanel.INVENTORY_CELLGAP + WorldPanel.INVENTORY_CELLSIZE)/2;
    this.shopY = (int)Math.round(this.menuY + (WorldPanel.INVENTORY_CELLGAP + WorldPanel.INVENTORY_CELLSIZE)*1.5);
    this.shopW = 11*(WorldPanel.INVENTORY_CELLGAP + WorldPanel.INVENTORY_CELLSIZE);
    this.shopItemH = (WorldPanel.INVENTORY_CELLGAP + WorldPanel.INVENTORY_CELLSIZE)*5/4;
    this.craftX = this.menuX + (WorldPanel.INVENTORY_CELLGAP + WorldPanel.INVENTORY_CELLSIZE)/2;
    this.craftY = this.menuY + (WorldPanel.INVENTORY_CELLGAP + WorldPanel.INVENTORY_CELLSIZE)*2;
    this.craftW = 11*(WorldPanel.INVENTORY_CELLGAP + WorldPanel.INVENTORY_CELLSIZE);
    this.craftItemH = (WorldPanel.INVENTORY_CELLGAP + WorldPanel.INVENTORY_CELLSIZE)*2;
    this.menuButtonImages = new BufferedImage[5];
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

    // draw tiles
    for (int y = tileStartY; y < Math.max(playerPos.y+this.tileHeight/2+1, tileStartY+this.tileHeight); ++y) {
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
    
    // draw building layout
    if (playerArea instanceof BuildingArea) {
      if (((BuildingArea)playerArea).hasInteriorImage()) {
        BuildingArea area = (BuildingArea)playerArea;
        // System.out.println(area.getDrawLocation().x);
        // System.out.println(area.getDrawLocation().y);
        // System.out.println(area.getXOffset() * Tile.getSize());
        // System.out.println(area.getYOffset() * Tile.getSize());
        g.drawImage(area.getImage(), 
                    (int)((area.getDrawLocation().x*Tile.getSize())+(area.getXOffset()*Tile.getSize())), 
                    (int)((area.getDrawLocation().y*Tile.getSize())+(area.getYOffset()*Tile.getSize())), null);
      }
    }

    Iterator<Moveable> moveables = playerArea.getMoveables();
    while (moveables.hasNext()) {
      Moveable currentMoveable = moveables.next(); // TODO: rename playerScreenPos, also clarify use of NPC.SIZE?
      this.playerScreenPos.x = (Tile.getSize()*(currentMoveable.getPos().x-tileStartX+0.5-(NPC.SIZE))+originX);
      this.playerScreenPos.y = (Tile.getSize()*(currentMoveable.getPos().y-tileStartY+0.5-(NPC.SIZE))+originY);
      g.setColor(Color.BLACK);
      g.fillRect(this.getWidth()/2, this.getHeight()/2, 1, 1);
      g.drawImage(currentMoveable.getImage(), (int)this.playerScreenPos.x, (int)(this.playerScreenPos.y-(64*NPC.SIZE)), null);
    }

    // draw tile components
    screenTileX = 0;
    screenTileY = 0;
    for (int y = tileStartY; y < Math.max(playerPos.y+this.tileHeight/2+1, tileStartY+this.tileHeight); ++y) {
      for (int x = tileStartX; x < Math.max(playerPos.x+this.tileWidth/2+1, tileStartX+this.tileWidth); ++x) {
        if (playerArea.inMap(x, y)) {
          Tile currentTile = playerArea.getMapAt(x, y);
          if (currentTile != null) {
            double drawX = originX+(screenTileX*Tile.getSize()); //- consider offsets when you draw the image
            double drawY = originY+(screenTileY*Tile.getSize());
            TileComponent tileContent = currentTile.getContent();
            if (tileContent != null) {
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

            if (selectedTile != null && (int)selectedTile.x == x && (int)selectedTile.y == y) {
              Graphics2D g2 = (Graphics2D)g;
              g2.setStroke(new BasicStroke(4));
              g2.setColor(Color.RED);
              g2.drawRect((int)Math.round(drawX+2), (int)Math.round(drawY+2), Tile.getSize()-4, Tile.getSize()-6);
            }
          }
        }
        ++screenTileX;
      }
      screenTileX = 0;
      ++screenTileY;
    }

    

    // hotbar stuff :))
    hotbarX = this.getWidth()/2-6*(WorldPanel.INVENTORY_CELLSIZE + WorldPanel.INVENTORY_CELLGAP);
    if (this.playerScreenPos.y > this.getHeight()/2){
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

    // time stuff
    // one real world second is one in game minute
    long time = this.worldToDisplay.getInGameNanoTime()/1_000_000_000;
    Graphics2D g2 = (Graphics2D)g;
    String currentSeason = World.getSeasons()[this.worldToDisplay.getInGameSeason()];
    String currentDay;
    if (this.worldToDisplay.getInGameDay() % World.getDaysPerSeason() == 0) {
      currentDay = "Day 28";
    } else {
      currentDay = "Day " + String.valueOf(this.worldToDisplay.getInGameDay() % World.getDaysPerSeason());
    }
    g2.setColor(Color.BLACK);
    g2.setFont(this.TIME_FONT);
    g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                        RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
    g2.drawString(String.format("%02d:%02d", time/60, time%60), this.getWidth()-130, 45);
    g2.drawString(currentSeason, this.getWidth()-g2.getFontMetrics().stringWidth(currentSeason)-20, 100);
    g2.drawString(currentDay, this.getWidth()-g2.getFontMetrics().stringWidth(currentDay)-20, 155);
    g2.drawString(Integer.toString(worldPlayer.getCurrentFunds())+" D$", 
                  this.getWidth()-g2.getFontMetrics().stringWidth(
                                        Integer.toString(worldPlayer.getCurrentFunds()) + " D$")-20,
                  215);
    
    if (worldPlayer.isInMenu()) {
      g.setColor(new Color(0, 0, 0, 100));
      g.fillRect(0, 0, this.getWidth(), this.getHeight());
      g.setColor(WorldPanel.MENU_BKGD_COLOR);
      g.fillRect(this.menuX, this.menuY+WorldPanel.INVENTORY_CELLSIZE, this.menuW, this.menuH);
      for (int i = 0; i < this.menuButtonImages.length; i++) {
        g.drawImage(this.menuButtonImages[i], this.menuX + i*WorldPanel.INVENTORY_CELLSIZE, this.menuY, null);
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
              g.setColor(new Color(230, 165, 100));
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
        CraftingMachine craftingMachine = worldPlayer.getCraftingMachine();
        String[] products = craftingMachine.getProducts();
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

      } else if (worldPlayer.getCurrentMenuPage() == Player.MAP_PAGE) {
        // TODO: insert gui code
        

      } else if (worldPlayer.getCurrentMenuPage() == Player.SKILLS_PAGE) {
        // TODO: insert gui code
        Graphics2D dumbGraphics = (Graphics2D)g;
        dumbGraphics.setColor(Color.WHITE);
        dumbGraphics.setFont(this.PROFILE_FONT);
        dumbGraphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        dumbGraphics.drawString("Skills Page - Coming Soon ;)", 350, 500);

      } else if (worldPlayer.getCurrentMenuPage() == Player.SOCIAL_PAGE) {
        // TODO: insert gui code
        Graphics2D dumbGraphics = (Graphics2D)g;
        dumbGraphics.setColor(Color.WHITE);
        dumbGraphics.setFont(this.PROFILE_FONT);
        dumbGraphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        dumbGraphics.drawString("Social Page - Coming Soon ;)", 350, 500);

      } else if (worldPlayer.getCurrentMenuPage() == Player.SHOP_PAGE) {
        Shop shop = (Shop)(worldPlayer.getCurrentInteractingMenuObj());
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
            textGraphics.drawString(itemToDraw.getName() + ":  " + Double.toString(shop.getPriceOf(itemToDraw.getName())) + " $D",
                                    this.shopX + WorldPanel.INVENTORY_CELLSIZE*4/3, curDrawY + WorldPanel.INVENTORY_CELLSIZE/2);
            textGraphics.setFont(this.DESCRIPTION_FONT);
            textGraphics.drawString(" - " + itemToDraw.getDescription(),
                        this.shopX + WorldPanel.INVENTORY_CELLSIZE*4/3, curDrawY + WorldPanel.INVENTORY_CELLSIZE);
          }
        }
      } else if (worldPlayer.getCurrentMenuPage() == Player.CHEST_PAGE) {
        ExtrinsicChest chest = (ExtrinsicChest)(worldPlayer.getCurrentInteractingMenuObj());
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
              g.setColor(new Color(230, 165, 100));
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
        // hi candy
      }
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
          int meterY = (int)Math.round(playerScreenPos.y)-meterH-35;
          g.setColor(new Color(0,0,0,175));
          g.fillRect(meterX, meterY, meterW, meterH);
          g.setColor(Color.GREEN);
          g.fillRect(meterX, meterY, (int)Math.round(meterW*playerCurrentRod.getCastingProgressPercentage()/100.0), meterH);
        } else if (playerCurrentRod.getCurrentStatus() == FishingRod.WAITING_STATUS) {
          g.setColor(Color.WHITE);
          int lineX, lineY;
          if (worldPlayer.getOrientation() == World.NORTH) {
            lineX = (int)Math.round(playerScreenPos.x+Player.SIZE*Tile.getSize());
            lineY = (int)Math.round(playerScreenPos.y+Player.SIZE*Tile.getSize()*2-worldPlayer.getImage().getHeight());
          } else if (worldPlayer.getOrientation() == World.SOUTH) {
            lineX = (int)Math.round(playerScreenPos.x+Player.SIZE*Tile.getSize());
            lineY = (int)Math.round(playerScreenPos.y+Player.SIZE*Tile.getSize()*2);
          } else if (worldPlayer.getOrientation() == World.WEST) {
            lineX = (int)Math.round(playerScreenPos.x);
            lineY = (int)Math.round(playerScreenPos.y)+5;
          } else { // EAST
            lineX = (int)Math.round(playerScreenPos.x+Player.SIZE*Tile.getSize()*2);
            lineY = (int)Math.round(playerScreenPos.y)+5;
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
            letterGraphics.drawString("!", (int)Math.round(playerScreenPos.x)+Tile.getSize()/4, (int)Math.round(playerScreenPos.y)-25);
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
    if ((worldToDisplay.getPlayerArea() instanceof MineArea) || (worldPlayer.getHealth() < worldPlayer.getMaxHealth())) {
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
  }

  public void updateHoveredItemIdx(int x, int y) {
    Player worldPlayer = this.worldToDisplay.getPlayer();
    if ((!worldPlayer.isInMenu()) && this.isPosInHotbar(x, y)) {
      this.hoveredItemIdx = this.hotbarItemIdxAt(x);
    } else if ((worldPlayer.getCurrentMenuPage() == Player.INVENTORY_PAGE)
               && this.isPosInInventory(this.menuX, this.inventoryMenuInventoryY, x, y)) {
      if ((this.inventoryItemIdxAt(this.menuX, this.inventoryMenuInventoryY, x, y) < worldPlayer.getInventorySize())
          && (this.inventoryItemIdxAt(this.menuX, this.inventoryMenuInventoryY, x, y) >= 0)) {
        this.hoveredItemIdx = this.inventoryItemIdxAt(this.menuX, this.inventoryMenuInventoryY, x, y);
      }
    } else if ((worldPlayer.getCurrentMenuPage() == Player.CHEST_PAGE)
               && this.isPosInInventory(this.menuX, this.chestMenuInventoryY, x, y)) {
      if ((this.inventoryItemIdxAt(this.menuX, this.chestMenuInventoryY, x, y) < worldPlayer.getInventorySize())
          && (this.inventoryItemIdxAt(this.menuX, this.chestMenuInventoryY, x, y) >= 0)) {
      this.hoveredItemIdx = this.inventoryItemIdxAt(this.menuX, this.chestMenuInventoryY, x, y);
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
    return Math.min((int)(Math.floor((x-this.hotbarX)/
           (WorldPanel.INVENTORY_CELLSIZE+WorldPanel.INVENTORY_CELLGAP))), 11);
  }

  public int menuTabButtonAt(int x) {
    return Math.min((int)(Math.floor((x-this.menuX)/
           (WorldPanel.INVENTORY_CELLSIZE+WorldPanel.INVENTORY_CELLGAP))), this.menuButtonImages.length-1);
  }

  public int shopItemIdxAt(int y) {
    return Math.min((int)(Math.floor((y-this.shopY) / (this.shopItemH+WorldPanel.INVENTORY_CELLGAP))), WorldPanel.SHOP_ITEMS_PER_PAGE-1);
  }

  public int craftingItemIdxAt(int y) {
    return Math.min((int)(Math.floor((y-this.craftY) / (this.craftItemH+WorldPanel.INVENTORY_CELLGAP))), WorldPanel.CRAFTING_ITEMS_PER_PAGE-1);
  }

  public int inventoryItemIdxAt(int inventoryX, int inventoryY, int targetX, int targetY) {
    return Math.min((int)(Math.floor(targetX-inventoryX)/ (WorldPanel.INVENTORY_CELLSIZE+WorldPanel.INVENTORY_CELLGAP)), 11)
           + 12*Math.min((int)(Math.floor((targetY-inventoryY)/
                                          (WorldPanel.INVENTORY_CELLSIZE+WorldPanel.INVENTORY_CELLGAP))), 2);
  }
  
  public boolean isPosInHotbar(int x, int y) {
    return ((x >= this.hotbarX) &&
            (x <= this.hotbarX+12*(WorldPanel.INVENTORY_CELLSIZE+WorldPanel.INVENTORY_CELLGAP)) &&
            (y >= this.hotbarY) &&
            (y <= this.hotbarY + WorldPanel.INVENTORY_CELLSIZE));
  }

  public boolean isPosInMenuTab(int x, int y) {
    return ((x >= this.menuX) &&
            (x <= this.menuX + this.menuW) &&
            (y >= this.menuY) &&
            (y <= this.inventoryMenuInventoryY));
  }

  public boolean isPosInInventory(int inventoryX, int inventoryY, int targetX, int targetY) {
    return ((targetX >= inventoryX) &&
            (targetX <= inventoryX + (WorldPanel.INVENTORY_CELLSIZE+WorldPanel.INVENTORY_CELLGAP)*12) &&
            (targetY > inventoryY) &&
            (targetY <= inventoryY + 3*(WorldPanel.INVENTORY_CELLSIZE + WorldPanel.INVENTORY_CELLGAP)));
  }

  public boolean isPosInShopItemList(int x, int y) {
    return ((x >= this.shopX) &&
            (x <= this.shopX + this.shopW) &&
            (y >= this.shopY) &&
            (y <= this.shopY + this.shopItemH * WorldPanel.SHOP_ITEMS_PER_PAGE));
  }

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

  public int getInventoryMenuInventoryY() {
    return this.inventoryMenuInventoryY;
  }

  public int getChestMenuInventoryY() {
    return this.chestMenuInventoryY;
  }

  public int getChestMenuChestY() {
    return this.chestMenuChestY;
  }

}
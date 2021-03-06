import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.Iterator;

/**
 * [StardubeEventListener]
 * Event listener that processes all key and mouse events in a world.
 * 2019-12-20
 * @version 2.16
 * @author Kevin Qiao, Paula Yuan, Candice Zhang, Joseph Wang
 */

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

  /**
   * [StardubeEventListener]
   * Constructor for a new StardubeEventListener.
   * @param stardubeWorld The World that is used to process events.
   * @param worldPanel    The corresponding WorldPanel of the World.
   */
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

  /**
   * [update]
   * Updates and processes events, including player movement and mini game updates.
   * @author Kevin Qiao, Candice Zhang
   */
  public void update() {
    this.updateSelectedTile();

    if (this.stardubePlayer.isInFishingGame()) {
      long biteNanoTime = this.stardubePlayer.getCurrentFishingGame().getBiteNanoTime();
      if ((biteNanoTime+FishingGame.BITE_ELAPSE_NANOTIME) < System.nanoTime()) {
        this.stardubePlayer.getCurrentFishingGame().generateBiteNanoTime();
      }
      this.stardubePlayer.getCurrentFishingGame().update(this.mouseStates[1], this.mouseStopwatches[1]);
      this.stardubePlayer.setImmutable(true);
      if (this.stardubePlayer.getCurrentFishingGame().getCurrentStatus() != FishingGame.INGAME_STATUS) {
        this.stardubeWorld.emplaceFutureEvent((long)(0.5*1_000_000_000),
                                              new FishingGameEndedEvent(this.stardubePlayer.getCurrentFishingGame()));
        this.stardubePlayer.endCurrentFishingGame();
      }
    }
    
    this.stardubePlayer.setVelocity(0, 0, 0);
    if (this.keyStates[KeyEvent.VK_W]) {
      if (this.stardubePlayer.getVerticalSpeed() != -1) {
        this.stardubePlayer.setVerticalSpeed(-1);
        this.stardubePlayer.setOrientation(World.NORTH);
      }
    }
    if (this.keyStates[KeyEvent.VK_A]) {
      if (this.stardubePlayer.getHorizontalSpeed() != -1) {
        this.stardubePlayer.setHorizontalSpeed(-1);
        this.stardubePlayer.setOrientation(World.WEST);
      }
    }
    if (this.keyStates[KeyEvent.VK_S]) {
      if (this.stardubePlayer.getVerticalSpeed() != 1) {
        this.stardubePlayer.setVerticalSpeed(1);
        this.stardubePlayer.setOrientation(World.SOUTH);
      }
    }
    if (this.keyStates[KeyEvent.VK_D]) {
      if (this.stardubePlayer.getHorizontalSpeed() != 1) {
        this.stardubePlayer.setHorizontalSpeed(1);
        this.stardubePlayer.setOrientation(World.EAST);
      }
    }
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public void keyPressed(KeyEvent e) {
    if (e.getKeyCode() < this.keyStates.length) {
      this.keyStates[e.getKeyCode()] = true;
    }
    this.updateSelectedTile();
    switch (e.getKeyCode()) {
      case KeyEvent.VK_E:
        if (!(this.stardubePlayer.isInMenu())) {
          this.stardubePlayer.enterMenu(Player.INVENTORY_PAGE);
        }
        break;
      case KeyEvent.VK_ESCAPE:
        this.stardubePlayer.exitMenu();
        break;
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void keyTyped(KeyEvent e) {
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void keyReleased(KeyEvent e) {
    if (e.getKeyCode() < this.keyStates.length) {
      this.keyStates[e.getKeyCode()] = false;
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void mouseClicked(MouseEvent e) {
    int mouseX = e.getX(), mouseY = e.getY();
    int clickCount = e.getClickCount();
    Player player = this.stardubePlayer;

    // Chest interation:
    // double click to move the item into the other inventory (1 instance at a time); triple click to move the maximum quantity
    if (player.getCurrentMenuPage() == Player.CHEST_PAGE) {
      ExtrinsicChest chest = (ExtrinsicChest)(this.stardubePlayer.getCurrentInteractingObj());
      if (this.worldPanel.isPosInInventory(this.worldPanel.getMenuX(),
                                           this.worldPanel.getChestMenuInventoryY(),
                                           mouseX, mouseY)) {
        // if click is in player inventory, place item into chest
        int itemIdx = this.worldPanel.inventoryItemIdxAt(
            this.worldPanel.getMenuX(),
            this.worldPanel.getChestMenuInventoryY(),
            mouseX, mouseY
        );
        if ((itemIdx < player.getInventorySize())
              && (player.getInventory()[itemIdx] != null)
              && (chest.canAdd(player.getInventory()[itemIdx].getContainedHoldable()))) {
          if (e.getButton() == MouseEvent.BUTTON3) { 
            chest.add(new HoldableStack(player.getInventory()[itemIdx].getContainedHoldable(), 1));
            player.useAtIndex(itemIdx);
          } else if ((e.getButton() == MouseEvent.BUTTON1) && (clickCount >= 2)) {
            chest.add(player.getInventory()[itemIdx]);
            player.removeAtIndex(itemIdx);
          }
        }
      } else if (this.worldPanel.isPosInInventory(this.worldPanel.getMenuX(),
                                                  this.worldPanel.getChestMenuChestY(),
                                                  mouseX, mouseY)) {
        // if click is in player inventory, pickup item from chest
        int itemIdx = this.worldPanel.inventoryItemIdxAt(
            this.worldPanel.getMenuX(),
            this.worldPanel.getChestMenuChestY(), mouseX, mouseY
        );
        if ((chest.getInventory()[itemIdx] != null)
              && (player.canPickUp(chest.getInventory()[itemIdx].getContainedHoldable()))) {
          if (e.getButton() == MouseEvent.BUTTON3) { 
            player.pickUp(new HoldableStack(chest.getInventory()[itemIdx].getContainedHoldable(), 1));
            chest.useAtIndex(itemIdx);
          } else if ((e.getButton() == MouseEvent.BUTTON1) && (clickCount >= 2)) {
            player.pickUp(chest.getInventory()[itemIdx]);
            chest.removeAtIndex(itemIdx);
          }
        }
      } 

    } else if ((player.getCurrentMenuPage() == Player.SHOP_PAGE)
               && (e.getButton() == MouseEvent.BUTTON1)
               && (clickCount >= 2)) {
      if (worldPanel.isPosInShopItemList(mouseX, mouseY)) {
        Shop shop = (Shop)(this.stardubePlayer.getCurrentInteractingObj());
        int itemIdx = this.stardubePlayer.getAmountScrolled() + worldPanel.shopItemIdxAt(mouseY);
        if (itemIdx < shop.getItems().length) {
          player.purchase(shop, shop.getItems()[itemIdx]);
        }
      }
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void mousePressed(MouseEvent e) {
    this.mouseStates[e.getButton()] = true;
    this.mouseStopwatches[e.getButton()] = new Stopwatch();
    this.mousePos.x = e.getX();
    this.mousePos.y = e.getY();

    if (e.getButton() == MouseEvent.BUTTON1) {
      if (!(this.stardubePlayer.isInFishingGame())
            && (!this.stardubePlayer.isInMenu())
            && (!this.stardubePlayer.isImmutable())) {
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
    } else if ((e.getButton() == MouseEvent.BUTTON3)
               && !(this.stardubePlayer.getCurrentInteractingObj() instanceof NPC)) {
      Iterator<Moveable> allMoveables = this.stardubeWorld.getPlayerArea().getMoveables();
      while (allMoveables.hasNext()) {
        Moveable currentMoveable = allMoveables.next();
        if ((currentMoveable instanceof NPC)
              && (this.mousePos.x/Tile.getSize() >= currentMoveable.getPos().x)
              && (this.mousePos.x/Tile.getSize() <= currentMoveable.getPos().x+1)
              && (this.mousePos.y/Tile.getSize() >= currentMoveable.getPos().y)
              && (this.mousePos.y/Tile.getSize() <= currentMoveable.getPos().y+2)) {
          this.stardubePlayer.setImmutable(true);
          this.stardubePlayer.setCurrentInteractingObj((NPC)currentMoveable);
        }
      }
    } else if ((e.getButton() == MouseEvent.BUTTON3)
               && (this.stardubePlayer.getCurrentInteractingObj() instanceof NPC)) {
      this.stardubePlayer.setImmutable(false);
      NPC currentNPC = ((NPC)this.stardubePlayer.getCurrentInteractingObj());
      currentNPC.setIndex((currentNPC.getIndex()+1) % 5);
      this.stardubePlayer.setCurrentInteractingObj(null);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void mouseReleased(MouseEvent e) {
    this.mouseStates[e.getButton()] = false;
    this.mouseStopwatches[e.getButton()] = new Stopwatch();
    this.mousePos.x = e.getX();
    this.mousePos.y = e.getY();
    
    if (this.stardubePlayer.isInFishingGame()) {
      if (!this.stardubePlayer.getCurrentFishingGame().hasStarted()) {
        this.stardubeWorld.emplaceFutureEvent(
            0,
            new CatchFishEvent((FishingRod)(this.stardubePlayer.getSelectedItem().getContainedHoldable()))
        );
      }
    } else if ((!this.stardubePlayer.isInMenu())
               && (!this.stardubePlayer.isImmutable())) {
      // general player interactions (AKA doors and foraging)
      if (e.getButton() == MouseEvent.BUTTON3) {
        this.stardubeWorld.emplaceFutureEvent(
            (long)(25_000_000),
            new PlayerInteractEvent(this.stardubePlayer.getSelectedTile(),
                                    this.stardubePlayer.getSelectedItemIdx())
        );
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
            if (this.stardubePlayer.getEnergy() > 0) {
              this.stardubePlayer.setImmutable(true);
              this.stardubeWorld.emplaceFutureEvent(
                  (long)(0.5*1_000_000_000),
                  new UtilityToolUsedEvent(
                      (UtilityTool)selectedItem,
                      ((UtilityTool)selectedItem).getUseLocation(this.stardubePlayer.getSelectedTile())[0]
                  )
              );
            }
          } else if (selectedItem instanceof Placeable) {
            this.stardubeWorld.emplaceFutureEvent(
                (long)(25_000_000),
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
            if (this.stardubePlayer.getEnergy() > 0) {
              if (((FishingRod)selectedItem).getCurrentStatus() == FishingRod.CASTING_STATUS) {
                this.stardubeWorld.emplaceFutureEvent(
                    (long)(0.5*1_000_000_000),
                    new CastingEndedEvent((FishingRod)selectedItem)
                );
                ((FishingRod)selectedItem).setCurrentStatus(FishingRod.IDLING_STATUS);
              }
            }
          }
        }
      }
    } else if (this.stardubePlayer.isInMenu()) {
      int menuPage = this.stardubePlayer.getCurrentMenuPage();
      
      if ((menuPage >= 0) && (menuPage <= 3) // for menu tab buttons; 0-3: INVENTORY, CRAFTING, MAP, SOCIAL
            && (this.worldPanel.isPosInMenuTab((int)this.mousePos.x, (int)this.mousePos.y))) {
        this.stardubePlayer.enterMenu(this.worldPanel.menuTabButtonAt((int)this.mousePos.x));

        if((this.worldPanel.menuTabButtonAt((int)this.mousePos.x)) == Player.CRAFTING_PAGE) {
          this.stardubePlayer.setCurrentInteractingObj(this.stardubePlayer.getCraftingMachine());
        }
      } else if ((menuPage == Player.INVENTORY_PAGE)
                 && (e.getButton() == MouseEvent.BUTTON1)
                 && (this.worldPanel.isPosInInventory(this.worldPanel.getMenuX(),
                                                      this.worldPanel.getInventoryMenuInventoryY(),
                                                      (int)this.mousePos.x, (int)this.mousePos.y))) {
        int selectedItemIdx = this.worldPanel.inventoryItemIdxAt(
            this.worldPanel.getMenuX(), this.worldPanel.getInventoryMenuInventoryY(),
            (int)this.mousePos.x, (int)this.mousePos.y
        );
        if (selectedItemIdx < this.stardubePlayer.getInventorySize()) {
          this.stardubePlayer.setSelectedItemIdx(selectedItemIdx);
        }
      } else if (this.stardubePlayer.getCurrentMenuPage() == Player.CRAFTING_PAGE) {
        if (this.worldPanel.isPosInCraftButton((int)this.mousePos.x, (int)this.mousePos.y)) {
          String product;
          if ((this.stardubePlayer.hasInteractingObj())
                && (this.stardubePlayer.getCurrentInteractingObj() instanceof CraftingStore)) {
            product = ((CraftingStore)this.stardubePlayer.getCurrentInteractingObj()).getItems()[
                this.worldPanel.craftingItemIdxAt((int)this.mousePos.y) + this.stardubePlayer.getAmountScrolled()
            ];
          } else {
            product = this.stardubePlayer.getCraftingMachine().getProducts()[
                this.worldPanel.craftingItemIdxAt((int)this.mousePos.y) + this.stardubePlayer.getAmountScrolled()
            ];
          }
          this.stardubePlayer.craft(product);
        }
      } else if ((this.stardubePlayer.getCurrentMenuPage() == Player.CHEST_PAGE)
                 && (e.getButton() == MouseEvent.BUTTON1)
                 && (this.worldPanel.isPosInInventory(this.worldPanel.getMenuX(),
                                                      this.worldPanel.getChestMenuInventoryY(),
                                                      (int)this.mousePos.x, (int)this.mousePos.y))) {
        int selectedItemIdx = this.worldPanel.inventoryItemIdxAt(
            this.worldPanel.getMenuX(),
            this.worldPanel.getMenuY()+(WorldPanel.INVENTORY_CELLSIZE+WorldPanel.INVENTORY_CELLGAP),
            (int)this.mousePos.x, (int)this.mousePos.y
        );
        if (selectedItemIdx < this.stardubePlayer.getInventory().length) {
          this.stardubePlayer.setSelectedItemIdx(selectedItemIdx);
        }
      } else if (this.stardubePlayer.getCurrentMenuPage() == Player.ELEVATOR_PAGE) {
        if (this.worldPanel.isPosInElevatorButtons((int)this.mousePos.x, (int)this.mousePos.y)) {
          // get the button# clicked (button 1 -> floor 5, button 2 -> floor 10)
          int buttonSelected = this.worldPanel.elevatorButtonIdxAt((int)this.mousePos.x, (int)this.mousePos.y)+1;
          if (this.stardubeWorld.getMines().hasElevatorFloorUnlocked(buttonSelected*5)) {
            this.stardubeWorld.getMines().setElevatorDestination(buttonSelected*5);
            this.stardubePlayer.exitMenu();
            this.stardubeWorld.emplaceFutureEvent(
                0,
                new PlayerInteractEvent(this.stardubeWorld.getMines().getElevatorPosition(),
                                        this.stardubePlayer.getSelectedItemIdx())
            );
          }
        }        
      }
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void mouseEntered(MouseEvent e) {
    this.mousePos.x = e.getX();
    this.mousePos.y = e.getY();
    this.updateSelectedTile();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void mouseExited(MouseEvent e) {
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void mouseDragged(MouseEvent e) {
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void mouseMoved(MouseEvent e) {
    this.mousePos.x = e.getX();
    this.mousePos.y = e.getY();
    this.updateSelectedTile();
    this.worldPanel.updateHoveredItemIdx((int)this.mousePos.x, (int)this.mousePos.y);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void mouseWheelMoved(MouseWheelEvent e) {
    int rotation = e.getWheelRotation();
    if ((this.stardubePlayer.getCurrentMenuPage() == Player.SHOP_PAGE)
          || (this.stardubePlayer.getCurrentMenuPage() == Player.CRAFTING_PAGE)
          || (this.stardubePlayer.getCurrentMenuPage() == Player.SOCIAL_PAGE)) {
      if (rotation < 0) {
        this.stardubeWorld.decrementPlayerAmountScrolled();
      } else if (rotation > 0) {
        this.stardubeWorld.incrementPlayerAmountScrolled();
      }
    } else {
      if (rotation < 0) {
        this.stardubePlayer.decrementSelectedItemIdx();
      } else if (rotation > 0) {
        this.stardubePlayer.incrementSelectedItemIdx();
      }
    }
  }

  /**
   * [updateSelectedTile]
   * Updates the selected tile of the player according to current mouse position.
   * @author Kevin Qiao
   */
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
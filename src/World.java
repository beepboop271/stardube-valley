import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.EventObject;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.ListIterator;

/**
 * [World]
 * Contains essential components and methods for a world to function.
 * 2019-12-19
 * @version 0.1
 * @author Kevin Qiao, Paula Yuan, Candice Zhang, Joseph Wang
 */

public class World {
  public static final int NORTH = 0;
  public static final int EAST = 1;
  public static final int SOUTH = 2;
  public static final int WEST = 3;
  public static final int getOppositeDirection(int direction) {
    if (direction == World.NORTH) {
      return World.SOUTH;
    } else if (direction == World.EAST) {
      return World.WEST;
    } else if (direction == World.SOUTH) {
      return World.NORTH;
    } else if (direction == World.WEST) {
      return World.EAST;
    } else {
      throw new IllegalArgumentException("not a valid direction");
    }
  }

  private static final String[] SEASONS = {"Spring", "Summer", "Fall", "Winter"};
  private static final int DAYS_PER_SEASON = 28;

  private LinkedHashMap<String, Area> locations;
  private PriorityBlockingQueue<TimedEvent> eventQueue;
  private Area playerArea;
  private Player player;
  private MineArea mines;
  private long lastUpdateTime = System.nanoTime();
  private long inGameNanoTime;
  private long inGameDay;
  private int inGameSeason;
  private double luckOfTheDay;
  private String[][] worldMap;
  private NPC[] npcs;

  /**
   * [World]
   * Constructor for a new World.
   * @throws IOException
   */
  public World() throws IOException {
    this.locations = new LinkedHashMap<String, Area>();
    try {
      this.loadAreas();
    } catch (IOException e) {
      e.printStackTrace();
    }
    this.eventQueue = new PriorityBlockingQueue<TimedEvent>();
    
    this.player = new Player(new Point(13, 13), "player");
    this.playerArea = this.locations.get("Farm");
    // this.playerArea.addMoveable(this.player);

    this.loadWorldMap("assets/gamedata/map/WorldMap");
    this.loadNPCS();

    this.inGameDay = 0;
    this.inGameSeason = 0;

    // spawn first day items, init mines
    this.doDayEndActions();
  }

  /**
   * [update]
   * Updates the world, including all its components and events.
   * @author Kevin Qiao
   */
  public void update() {
    long currentUpdateTime = System.nanoTime();
    this.processEvents();
    
    if (this.player.isInMenu()) {
      this.lastUpdateTime = currentUpdateTime;
      return;
    }
    this.inGameNanoTime += currentUpdateTime-this.lastUpdateTime;
    this.inGameNanoTime %= 24*60*1_000_000_000L;

    // check for end of day
    if (this.inGameNanoTime >= 2*60*1_000_000_000L && this.inGameNanoTime <= 6*60*1_000_000_000L) {
      this.doDayEndActions();
    }

    // move holdable entities on the ground
    // towards the player in the current area
    // synchronized (this.playerArea.getItemsOnGroundList()) {
      Iterator<HoldableStackEntity> itemsNearPlayer = this.playerArea.getItemsOnGround();
      HoldableStackEntity nextItemEntity;
      double itemDistance;
      while (itemsNearPlayer.hasNext()) {
        nextItemEntity = itemsNearPlayer.next();
        // itemsNearPlayer.remove();
        itemDistance = nextItemEntity.getPos().distanceTo(this.player.getPos());
        if ((itemDistance < Player.SIZE)
              && (this.player.pickUp(nextItemEntity.getStack()))) {
          itemsNearPlayer.remove();
        } else if (itemDistance < Player.getItemAttractionDistance()) {
            nextItemEntity.translatePos(nextItemEntity.getMove(currentUpdateTime-this.lastUpdateTime,
                                                               this.player.getPos()));
            // this.playerArea.addItemOnGround(nextItemEntity);
          // itemsNearPlayer.add(nextItemEntity);
        }
      }
      // this.playerArea.sortItemsOnGround();
    // }
    
    System.out.println("tick");
    Iterator<Area> areas = this.locations.values().iterator();
    Area nextArea;
    if (this.locations.get(this.playerArea.getName()) == null) {
      // synchronized (this.playerArea.getMoveableList()) {
        this.moveMoveablesInArea(this.playerArea, currentUpdateTime);
        // this.playerArea.sortMoveables();
      // }
    }
    while (areas.hasNext()) {
      nextArea = areas.next();
      // synchronized (nextArea.getMoveableList()) {
        this.moveMoveablesInArea(nextArea, currentUpdateTime);
        // nextArea.sortMoveables();
      // }
    }

    this.lastUpdateTime = currentUpdateTime;
  }

  /**
   * [moveMoveablesInArea]
   * Moves all the moveables in an area.
   * @author Kevin Qiao
   * @param a, the area the moving is happening in.
   * @param updateTime, the long time the update will be happening
   */
  public void moveMoveablesInArea(Area a, long updateTime) {
    Iterator<Moveable> moveables = a.getMoveables();
    Moveable nextMoveable;
    Vector2D move;
    while (moveables.hasNext()) {
      nextMoveable = moveables.next();
      // if (nextMoveable instanceof Enemy) {
      //   Enemy nextEnemy = ((Enemy)nextMoveable);
      //   if (a.hasLineOfSight(nextEnemy, this.player)) {
      //     move = nextEnemy.getMove(updateTime-this.lastUpdateTime, this.player.getPos());
      //   } else {
      //     move = nextEnemy.getMove(updateTime-this.lastUpdateTime);
      //   }
      // } else {
        move = nextMoveable.getMove(updateTime-this.lastUpdateTime);
      // }
      if ((move != null) && (move.getLength() > 0)) {
        // moveables.remove();
        if (nextMoveable instanceof LoopAnimatedMoveable) {
          ((LoopAnimatedMoveable)nextMoveable).updateImage();
        }
        World.doCollision(a, nextMoveable, move.getXVector(), true);
        World.doCollision(a, nextMoveable, move.getYVector(), false);
        
        if (nextMoveable instanceof Player) {
          Area newArea = a.moveAreas(nextMoveable, nextMoveable.getIntersectingTiles(move).iterator());
          if (newArea != a) {
            this.playerArea = newArea;
            moveables.remove();
          }
        } else if (a.moveAreas(nextMoveable, nextMoveable.getIntersectingTiles(move).iterator()) != a) {
            moveables.remove();
        }
      }
    }
  }

  /**
   * [doCollision]
   * Process an individual collision.
   * @author Kevin Qiao
   * @param a, the area the collision is happening in
   * @param m, the moveable that is being collided
   * @param move, the vector move being made
   * @param isHorizontal, whether the collision is horizontal
   */
  public static void doCollision(Area a, Moveable m, Vector2D move, boolean isHorizontal) {
    LinkedHashSet<Point> intersectingTiles;
    int collideDirection;
    intersectingTiles = m.getIntersectingTiles(move);
    collideDirection = a.collides(intersectingTiles,
                                  m.getPos().translateNew(move.getX(), move.getY()),
                                  isHorizontal);
    // System.out.println(collideDirection);
    if (collideDirection == -1) {
      // System.out.printf("translated by %s\n", move.toString());
      m.translatePos(move);
    } else {
      // System.out.printf("fixing from %s\n", move.toString());
      World.fixCollision(m, collideDirection);
    }
  }

  /**
   * [fixCollision]
   * Fix collisions of a moveable entity, given the direction it is colliding in
   * @author Kevin Qiao
   * @param m, the moveable that is colliding
   * @param collideDirection, the direction of the collision, with ints 0-1 representing cardinal directions
   */
  public static void fixCollision(Moveable m, int collideDirection) {
    if (collideDirection == World.EAST) {
      // subtract 0.0001 to prevent rounding from counting it as still colliding
      m.translatePos(
          m.getPos().round().x + 0.5-m.getSize() - m.getPos().x - 0.0001,
          0
      );
    } else if (collideDirection == World.WEST) {
      m.translatePos(
          m.getPos().round().x - 0.5+m.getSize() - m.getPos().x,
          0
      );
    } else if (collideDirection == World.NORTH) {
      m.translatePos(
          0,
          m.getPos().round().y - 0.5+m.getSize() - m.getPos().y
      );
    } else if (collideDirection == World.SOUTH) {
      // subtract 0.0001 to prevent rounding from counting it as still colliding
      m.translatePos(
          0,
          m.getPos().round().y + 0.5-m.getSize() - m.getPos().y - 0.0001
      );
    }
  }

  /**
   * [processEvents]
   * Process all events with the time that is lower than or equal to the current game time.
   * @author Kevin Qiao, Paula Yuan, Candice Zhang, Joseph Wang
   */
  public void processEvents() {
    EventObject event;
    while (!this.eventQueue.isEmpty()
           && (this.eventQueue.peek().getTime() <= this.inGameNanoTime)) {
      event = this.eventQueue.poll().getEvent();
      if (event instanceof UtilityToolUsedEvent) {
        this.player.setImmutable(false);
        UtilityToolUsedEvent toolEvent = (UtilityToolUsedEvent)event;
        this.player.decreaseEnergy(((Tool)toolEvent.getHoldableUsed()).getEnergyCost());
        Tile selectedTile = this.playerArea.getMapAt(toolEvent.getLocationUsed());

        Point treePos = toolEvent.getLocationUsed().translateNew(2, 1);
        Tile treeTile;
        if (this.playerArea.inMap(treePos)) {
          treeTile = this.playerArea.getMapAt(treePos);
        } else {
          treeTile = null;
        }

        // prioritize harvesting the actual tile before using the tree tile
        TileComponent componentToHarvest;
        if (selectedTile == null) {
          if (treeTile != null) {
            componentToHarvest = treeTile.getContent();
            if (!(componentToHarvest instanceof ExtrinsicTree)) {
              componentToHarvest = null;
            }
          } else {
            componentToHarvest = null;
          }
        } else {
          componentToHarvest = selectedTile.getContent();
          if (!(componentToHarvest instanceof ExtrinsicHarvestableComponent)) {
            if (treeTile != null && treeTile.getContent() instanceof ExtrinsicTree) {
              componentToHarvest = treeTile.getContent();
            } else {
              componentToHarvest = null;
            }
          }
        }

        if (componentToHarvest != null) {
          IntrinsicHarvestableComponent ic = ((IntrinsicHarvestableComponent)(((ExtrinsicHarvestableComponent)componentToHarvest).getIntrinsicSelf()));
          String requiredTool = ic.getRequiredTool();
                  
          if (requiredTool.equals("Any")
                || requiredTool.equals(((Tool)toolEvent.getHoldableUsed()).getType())) {
            ExtrinsicHarvestableComponent ec = ((ExtrinsicHarvestableComponent)componentToHarvest);
            if (ec.damageComponent(((UtilityTool)toolEvent.getHoldableUsed()).getEffectiveness())) {
              if (ec instanceof ExtrinsicTree) {
                if (((ExtrinsicTree)ec).getStage() == 17) {
                  ((ExtrinsicTree)ec).setStage(18);
                  ec.setHardnessLeft(5); 
                } else {
                  this.playerArea.removeComponentAt(treePos);
                }
              } else {
                this.playerArea.removeComponentAt(toolEvent.getLocationUsed());
              }

              HoldableDrop[] drops = ic.getProducts();
              HoldableStack product;
              for (int i = 0; i < drops.length; ++i) {
                product = drops[i].resolveDrop(this.luckOfTheDay);
                if (product != null) {
                  this.playerArea.addItemOnGround(
                    new HoldableStackEntity(
                        product,
                        toolEvent.getLocationUsed().translateNew(Math.random()-0.5, Math.random()-0.5)
                    )
                  );
                }
              }
            }
          }
        } else if (selectedTile instanceof GroundTile) {
          if (((Tool)toolEvent.getHoldableUsed()).getType().equals("WateringCan")
                &&  (((GroundTile)selectedTile).getTilledStatus() == true)) {
            ((GroundTile)selectedTile).setLastWatered(this.inGameDay);
            ((FarmArea)this.playerArea).addEditedTile((GroundTile)selectedTile);

          } else if ((this.playerArea instanceof FarmArea)
                     && (selectedTile.getContent() == null)) {
            if (((Tool)toolEvent.getHoldableUsed()).getType().equals("Hoe")) {
              if (this.playerArea instanceof FarmArea) {
                ((GroundTile)selectedTile).setTilledStatus(true);
                ((FarmArea)this.playerArea).addEditedTile((GroundTile)selectedTile);
              }
            } else if (((Tool)toolEvent.getHoldableUsed()).getType().equals("Pickaxe")) {
              if (((FarmArea)this.playerArea).hasTile((GroundTile)selectedTile)) { 
                ((GroundTile)selectedTile).setTilledStatus(false); 
                ((FarmArea)this.playerArea).removeEditedTile((GroundTile)selectedTile);
              }
            }
          } else if (((Tool)toolEvent.getHoldableUsed()).getType().equals("Pickaxe")) {
              if ((this.playerArea instanceof FarmArea)
                    && ((FarmArea)this.playerArea).hasTile((GroundTile)selectedTile)) {
                ((FarmArea)this.playerArea).removeEditedTile((GroundTile)selectedTile);
                ((GroundTile)selectedTile).setTilledStatus(false); 
                selectedTile.setContent(null);
              }
            }
            ((GroundTile)selectedTile).determineImage(this.inGameDay);
          } //- Ground tile changes image based on what happened
      } else if (event instanceof PlayerInteractEvent) {
        int itemIndex = ((PlayerInteractEvent)event).getSelectedItemIndex();
        Point useLocation = ((PlayerInteractEvent)event).getLocationUsed();
        Gateway interactedGateway = this.playerArea.getGateway(useLocation);
        Tile currentTile = this.playerArea.getMapAt(useLocation);
        TileComponent[] bushContents = new TileComponent[3];
        if (currentTile != null &&
            this.playerArea.inMap(currentTile.getX()+1, currentTile.getY()) &&
            this.playerArea.inMap(currentTile.getX(), currentTile.getY()+1) &&
            this.playerArea.getMapAt(currentTile.getX()+1, currentTile.getY()) != null &&
            this.playerArea.getMapAt(currentTile.getX(), currentTile.getY()+1) != null &&
            this.playerArea.getMapAt(currentTile.getX()+1, currentTile.getY()+1) != null) {
          bushContents[0] = this.playerArea.getMapAt(
                            currentTile.getX()+1, currentTile.getY()).getContent();
          bushContents[1] = this.playerArea.getMapAt(
                            currentTile.getX()+1, currentTile.getY()+1).getContent();
          bushContents[2] = this.playerArea.getMapAt(
                            currentTile.getX(), currentTile.getY()+1).getContent();
        }
        if ((interactedGateway != null)
              && interactedGateway.requiresInteractToMove()
              && ((this.player.getPos().round().x == interactedGateway.getOrigin().x)
                  || (interactedGateway.getOrientation() == Gateway.OMNIDIRECTIONAL))) {
          if (interactedGateway instanceof ElevatorGateway) {
            if (interactedGateway.getDestinationGateway() == null) {
              this.player.enterMenu(Player.ELEVATOR_PAGE);
            } else {
              this.playerArea = this.playerArea.moveAreas(this.player, interactedGateway);
              this.mines.loadLevel(((MineLevel)this.playerArea).getLevel()+1);
              ((ElevatorGateway)interactedGateway).resetElevatorDestination();
            }
          } else {
            this.playerArea = this.playerArea.moveAreas(this.player, interactedGateway);
            if (this.playerArea instanceof MineLevel) {
              this.mines.loadLevel(((MineLevel)this.playerArea).getLevel()+1);
            }
          }
        } else if (currentTile != null) {

          if (currentTile.getContent() == null) {
            if ((this.player.hasAtIndex(itemIndex)) &&
                (this.player.getAtIndex(itemIndex)
                                        .getContainedHoldable() instanceof Consumable)) {
              this.player.consume();
            } 
          }

          TileComponent currentContent = currentTile.getContent();

          if (currentContent instanceof ExtrinsicCrop) {
            if (((ExtrinsicCrop)currentContent).canHarvest()) {
              HoldableDrop productDrop = ((ExtrinsicCrop)currentContent).getProduct();
              HoldableStack product = productDrop.resolveDrop(this.luckOfTheDay);
              if ((product != null)
                    && this.player.canPickUp(product.getContainedHoldable())) {
                this.player.pickUp(product);
                if (((ExtrinsicCrop)currentContent).shouldRegrow()) {
                  ((ExtrinsicCrop)currentContent).resetRegrowCooldown();
                } else {
                  currentTile.setContent(null);
                }
              }
            }
          } else if (currentContent instanceof ExtrinsicGrowableCollectable) {
            HoldableDrop productDrop = ((ExtrinsicGrowableCollectable)currentContent).getProduct();
            HoldableStack product = productDrop.resolveDrop(this.luckOfTheDay);
            if (product !=null) {
              new HoldableStackEntity(product, null);
              if (this.player.canPickUp(product.getContainedHoldable())) {
                this.player.pickUp(product);
                ((ExtrinsicGrowableCollectable)currentContent).resetRegrowCooldown();
              }
            }
          } else if (currentContent instanceof ExtrinsicChest) {
            this.player.setCurrentInteractingObj((ExtrinsicChest)currentContent);
            this.player.enterMenu(Player.CHEST_PAGE);
            
          } else if (currentContent instanceof ExtrinsicMachine) {
            ExtrinsicMachine machine = ((ExtrinsicMachine)currentContent);
            if (machine.getProduct() != null) {
              if (this.player.canPickUp(machine.getProduct().getContainedHoldable())) {
                this.player.pickUp(machine.getProduct());
                machine.resetProduct();
              }
            } else if ((machine.getProduct() == null) && (machine.getItemToProcess() == null)){
              if (this.player.hasAtIndex(itemIndex)) {
                HoldableStack selectedItem = this.player.getAtIndex(itemIndex); //okay honestly this can be deleted this is just to make the next statement short
                if (machine.canProcess(selectedItem.getContainedHoldable().getName()) 
                    && selectedItem.getQuantity() >= machine.getRequiredQuantity()) {
                  if ((machine.getCatalyst() == null)  
                      || (this.player.hasHoldable(machine.getCatalyst()))) {
                    machine.setItemToProcess(selectedItem.getContainedHoldable().getName());
                    machine.increasePhase();   
                    player.decrementAtIndex(itemIndex, machine.getRequiredQuantity());
                    player.decrementHoldable(1, machine.getCatalyst());
                    this.emplaceFutureEvent(machine.getProcessingTime(selectedItem
                                                                      .getContainedHoldable()
                                                                      .getName()), 
                                            new MachineProductionFinishedEvent(machine));   
                  }                                   
                }
              } 
            } 
          } else if (currentContent instanceof ShippingContainer) {
            if (this.player.hasAtIndex(itemIndex)) {
              if (!(this.player.getAtIndex(itemIndex).getContainedHoldable() instanceof Tool)) {
                HoldableStack currentItem = this.player.getAtIndex(itemIndex);
                this.player.increaseFutureFunds(((ShippingContainer)currentContent).sellItem(currentItem));
                this.player.removeAtIndex(itemIndex);
              }
            }
          } else if (currentContent instanceof Shop) {
            this.player.enterMenu(Player.SHOP_PAGE);
            this.player.setCurrentInteractingObj((Shop)currentContent);
          } else if (currentContent instanceof CraftingStore) {
            this.player.setCurrentInteractingObj((CraftingStore)currentContent);
            this.player.enterMenu(Player.CRAFTING_PAGE);
          } else if (currentContent instanceof Bed) {
            this.doDayEndActions();
          } else if (currentContent instanceof Collectable) {
            if (this.playerArea instanceof WorldArea) {
              ((WorldArea)this.playerArea).setNumForageableTiles(((WorldArea)this.playerArea).getNumForageableTiles()-1);
            }
            HoldableDrop[] currentProducts = ((Collectable)currentContent).getProducts();
            HoldableStack drop = (currentProducts[0].resolveDrop(this.luckOfTheDay));
            if (drop != null) {
              new HoldableStackEntity(drop, null); 
              if (this.player.canPickUp(drop.getContainedHoldable())) {
                this.player.pickUp(drop);
                currentTile.setContent(null);
              }
            }
          } else if (bushContents[0] != null || bushContents[1] != null || bushContents[2] != null) {
            for (int i = 0; i < bushContents.length; i++) {
              if (bushContents[i] == null || bushContents[i] instanceof ExtrinsicCrop) {
                continue;
              } else if (bushContents[i] instanceof ExtrinsicGrowableCollectable) {
                HoldableDrop productDrop = ((ExtrinsicGrowableCollectable)bushContents[i]).getProduct();
                HoldableStack product = productDrop.resolveDrop(this.luckOfTheDay);
                if (product != null) {
                  new HoldableStackEntity(product, null);
                  if (this.player.canPickUp(product.getContainedHoldable())) {
                      this.player.pickUp(product);
                  ((ExtrinsicGrowableCollectable)bushContents[i]).resetRegrowCooldown();
                  } 
                }
              }
            }
          } 
        }

      } else if (event instanceof MachineProductionFinishedEvent) {
        ((ExtrinsicMachine)event.getSource()).processItem();

      } else if (event instanceof CastingEndedEvent) {
        FishingRod rodUsed = ((CastingEndedEvent)event).getRodUsed(); 
        this.player.decreaseEnergy(rodUsed.getEnergyCost());
        int meterPercentage = ((CastingEndedEvent)event).getMeterPercentage();
        int castDistance = (int)(Math.round(FishingRod.MAX_CASTING_DISTANCE*(meterPercentage/100.0)));
        Point roundedPlayerPos = player.getPos().round();
        int destX = (int)roundedPlayerPos.x;
        int destY = (int)roundedPlayerPos.y;
        if (this.player.getOrientation() == World.NORTH) {
          destY -= castDistance;
        } else if (this.player.getOrientation() == World.SOUTH) {
          destY += castDistance;
        } else if (this.player.getOrientation() == World.WEST) {
          destX -= castDistance;
        } else {
          destX += castDistance;
        }
        if (playerArea.inMap(destX, destY)) {
          if (playerArea.getMapAt(destX, destY) instanceof WaterTile) {
            rodUsed.setTileToFish((WaterTile)(playerArea.getMapAt(destX, destY)));
            rodUsed.setCurrentStatus(FishingRod.WAITING_STATUS);
            this.player.setCurrentFishingGame(new FishingGame(rodUsed.getTileToFish()));
          } else {
            this.player.setImmutable(false);
          }
        } else {
          this.player.setImmutable(false);
        }
        
      } else if (event instanceof CatchFishEvent) {
        FishingRod rodUsed = ((CatchFishEvent)event).getRodUsed();
        long catchNanoTime = ((CatchFishEvent)event).getCatchNanoTime();
        if ((catchNanoTime >= this.player.getCurrentFishingGame().getBiteNanoTime()) &&
            (catchNanoTime <= this.player.getCurrentFishingGame().getBiteNanoTime()+FishingGame.BITE_ELAPSE_NANOTIME)) {
          // as luck increases, chance to get trash decreases (minimum chance to get a fish is 25%)
          if ((rodUsed.getTileToFish().getFishableFish().length==0)
              || (Math.random() >= (this.luckOfTheDay+0.25))) {
            Holdable trashEarned = HoldableFactory.getHoldable(
                                   WaterTile.getFishableTrash()[(int)(Math.round(Math.random()*(WaterTile.getFishableTrash().length-1)))]);
            if (this.player.canPickUp(trashEarned)) {
              this.player.pickUp(new HoldableStack(trashEarned, 1));
            }
            this.player.endCurrentFishingGame();
            this.player.setImmutable(false);
          } else {
            this.player.getCurrentFishingGame().setHasStarted(true);
          }
        } else {
          this.player.endCurrentFishingGame();
          this.player.setImmutable(false);
        }
        rodUsed.setCurrentStatus(FishingRod.IDLING_STATUS);
      } else if (event instanceof FishingGameEndedEvent) {
        FishingGame gameEnded = ((FishingGameEndedEvent)event).getGameEnded();
        if (gameEnded.getCurrentStatus() == FishingGame.WIN_STATUS) {
          Holdable fishEarned = ((FishingGameEndedEvent)event).getFishReturned();
          if (this.player.canPickUp(fishEarned)) {
            this.player.pickUp(new HoldableStack(fishEarned, 1));
          }
        }
        this.player.setImmutable(false);

      } else if (event instanceof ComponentPlacedEvent) {
        //- Make sure the player literally has the object
        if (this.player.hasAtIndex(((ComponentPlacedEvent)event).getComponentIndex())) {
          Tile currentTile = this.playerArea.getMapAt(((ComponentPlacedEvent)event)
                                                      .getLocationUsed());
          TileComponent currentContent = currentTile.getContent();
          //- Anything that you can place must not be placed over something and not over something you can't walk over.
          if ((currentContent == null) && (!(currentTile instanceof NotWalkable))) { 
            //- We need to make sure that the tile is both a ground tile and is tilled if
            //- we're trying to plant a crop in that tile
            if (((ComponentPlacedEvent)event).getComponentToPlace() instanceof ExtrinsicCrop) {
              if (currentTile instanceof GroundTile) {
                if (((GroundTile)currentTile).getTilledStatus()) {
                  if (this.playerArea instanceof FarmArea) {
                    currentTile.setContent(((ComponentPlacedEvent)event).getComponentToPlace());
                    ((FarmArea)this.playerArea).addEditedTile((GroundTile)currentTile);
                    this.player.useAtIndex(((ComponentPlacedEvent)event).getComponentIndex());
                  }
                }
              }
            } else { //- If it's not a crop, you can place it anywhere
              currentTile.setContent(((ComponentPlacedEvent)event).getComponentToPlace());
              this.player.useAtIndex(((ComponentPlacedEvent)event).getComponentIndex());
            }
          }
        }
      } else if (event instanceof AutoMovementEvent) {
        int nextDirection = (int)(Math.random()*4);
        NPC npcToMove = ((NPC)event.getSource());
        if (nextDirection == 0 && npcToMove.getVerticalSpeed() != -1) {
          npcToMove.setHorizontalSpeed(0);
          npcToMove.setVerticalSpeed(-1);
          npcToMove.setOrientation(World.NORTH);
        } else if (nextDirection == 1 && npcToMove.getHorizontalSpeed() != 1) {
          npcToMove.setHorizontalSpeed(1);
          npcToMove.setVerticalSpeed(0);
          npcToMove.setOrientation(World.EAST);
        } else if (nextDirection == 2 && npcToMove.getVerticalSpeed() != 1) {
          npcToMove.setHorizontalSpeed(0);
          npcToMove.setVerticalSpeed(1);
          npcToMove.setOrientation(World.SOUTH);
        } else if (npcToMove.getHorizontalSpeed() != -1) {
          npcToMove.setHorizontalSpeed(-1);
          npcToMove.setVerticalSpeed(0);
          npcToMove.setOrientation(World.WEST);
        }
        this.emplaceFutureEvent((long)(Math.random()*1_000_000_000L*10),
                                new AutoMovementEvent(npcToMove));
      }
    }
  }

  /**
   * [doDayEndActions]
   * Does actions that need to be performed at the end of day.
   * @author Kevin Qiao, Paula Yuan, Joseph Wang, Candice Zhang
   * 
   */
  public void doDayEndActions() {
    //- handle the player position, whether they passed out or went to bed and
    //- whereso they passed out or slept. If they passed out outside of the farm
    //- area, they go to the clinic instead of the farmhouse and are charged money.
    Area spawnArea;
    if ((this.playerArea instanceof FarmArea) || (this.playerArea.getName().equals("Farmhouse"))) {
      spawnArea = this.locations.get("Farmhouse");
    } else {
      spawnArea = this.locations.get("Clinic");
      this.player.decreaseCurrentFunds(1000);
    }
    this.playerArea = this.player.moveToSpawnPosition(this.playerArea, (SpawnableArea)spawnArea);
    
    this.player.recover(this.inGameNanoTime);
    // day starts at 6 am
    this.inGameNanoTime = 6*60*1_000_000_000L;
    ++this.inGameDay;
    if ((this.inGameDay % DAYS_PER_SEASON == 1) && (this.inGameDay > 1)) {
      this.inGameSeason = (this.inGameSeason + 1) % 4;
    }
    this.luckOfTheDay = Math.random();
    Iterator<Area> areas = this.locations.values().iterator();
    Area nextArea;
    while (areas.hasNext()) {
      nextArea = areas.next();
      nextArea.updateDay(); //- someone can improve this i guess
      nextArea.updateSeason();
      nextArea.doDayEndActions();
    }

    this.player.increaseCurrentFunds(this.player.getFutureFunds());
  }

  /**
   * [emplaceFutureEvent]
   * Constructs and adds a TimedEvent that will happen at
   * a future time to this world's event queue.
   * @author Kevin Qiao
   * @param nanoTimeIntoFuture long that represents the time the event
   *                           will be processed, in nanotime into future.
   * @param event              EventObject, the event to emplace.
   */
  public void emplaceFutureEvent(long nanoTimeIntoFuture, EventObject event) {
    this.eventQueue.offer(new TimedEvent(this.inGameNanoTime+nanoTimeIntoFuture, event));
  }

  /**
   * [loadAreas]
   * Loads all areas this world contains.
   * @author Kevin Qiao, Candice Zhang, Joseph Wang
   * @throws IOException
   */
  public void loadAreas() throws IOException {
    String[] splitLine;
    String nextLine;

    // declare all areas first, then fill them in later
    BufferedReader input = new BufferedReader(new FileReader("assets/gamedata/map/Areas"));
    nextLine = input.readLine();
    while (nextLine != null) {
      splitLine = nextLine.split(" ");
      this.locations.put(splitLine[1],
                         Area.constructArea(splitLine[0], splitLine[1],
                                            Integer.parseInt(splitLine[2]),
                                            Integer.parseInt(splitLine[3])));
      nextLine = input.readLine();
    }
    input.close();

    input = new BufferedReader(new FileReader("assets/gamedata/map/Mines"));
    splitLine = input.readLine().split(" ");
    this.mines = new MineArea(splitLine[0],
                              Integer.parseInt(splitLine[1]),
                              Integer.parseInt(splitLine[2]),
                              new Point(input.readLine().split(" ")),
                              new Point(input.readLine().split(" ")),
                              new Point(input.readLine().split(" ")));
    this.locations.put(splitLine[0], this.mines);
    input.close();

    // load the tiles
    Iterator<Area> locationAreas = this.locations.values().iterator();
    while (locationAreas.hasNext()) {
      this.loadAreaMap(locationAreas.next());
    }

    // add gateways
    input = new BufferedReader(new FileReader("assets/gamedata/map/Gateways"));
    nextLine = input.readLine();
    // (\w++)               group 1: area1 name
    // \((\d++), (\d++)\)   group 2,3: coordinates of gateway in area1, with parentheses
    //  (I?)<->(I?)         group 4,5: <-> and whether or not interaction is required to travel
    // (\w++)               group 6: area2 name
    // \((\d++), (\d++)\)   group 7,8: coordinates of gateway in area2, with parentheses
    // (?: (\\w++))?        group 9: optional: name of the building for the second area to travel into
    Pattern gatewayPattern = Pattern.compile("^(\\w++) \\((\\d++), (\\d++)\\) (I?)<->(I?) (\\w++) \\((\\d++), (\\d++)\\)(?: (\\w++))?$");
    Matcher gatewayMatch;
    Gateway gateway1, gateway2;
    while (!nextLine.equals("")) {
      gatewayMatch = gatewayPattern.matcher(nextLine);
      gatewayMatch.matches();
      //System.out.println("Connecting " + gatewayMatch.group(1) + " to " + gatewayMatch.group(6));
      gateway1 = new Gateway(Integer.parseInt(gatewayMatch.group(2)),
                             Integer.parseInt(gatewayMatch.group(3)),
                             World.NORTH,
                             gatewayMatch.group(5).equals("I"));
      gateway2 = new Gateway(Integer.parseInt(gatewayMatch.group(7)),
                             Integer.parseInt(gatewayMatch.group(8)),
                             World.SOUTH,
                             gatewayMatch.group(4).equals("I"));

      gateway1.setDestinationArea(this.locations.get(gatewayMatch.group(6)));
      gateway1.setDestinationGateway(gateway2);
      gateway2.setDestinationArea(this.locations.get(gatewayMatch.group(1)));
      gateway2.setDestinationGateway(gateway1);

      if (gatewayMatch.group(9) != null) {
        this.locations.get(gatewayMatch.group(1)).getMapAt(gateway1.getOrigin())
                                                 .setContent(IntrinsicTileComponentFactory
                                                            .getComponent(gatewayMatch.group(9)));
        ((BuildingArea)this.locations.get(gatewayMatch.group(6)))
                                     .setDrawLocation(gateway2.getOrigin()); //-prep for layout design. (if its in a building, it has a layout)
      }

      this.locations.get(gatewayMatch.group(1)).addGateway(gateway1);
      this.locations.get(gatewayMatch.group(6)).addGateway(gateway2);
      nextLine = input.readLine();
    }
    input.close();

    //- add layouts to buildings that need one
    input = new BufferedReader(new FileReader("assets/gamedata/BuildingLayouts"));
    nextLine = input.readLine();
    while (nextLine.length() > 0) {
      splitLine = nextLine.split("\\s+");
      ((BuildingArea)this.locations.get(splitLine[0])).loadImage("assets/images" + splitLine[1]);
      double[] offsets = {Double.parseDouble(splitLine[2]), Double.parseDouble(splitLine[3])};
      ((BuildingArea)this.locations.get(splitLine[0])).setOffsets(offsets);

      nextLine = input.readLine();
    }
    input.close();

    //- add spawning locations
    input = new BufferedReader(new FileReader("assets/gamedata/SpawningLocations"));
    nextLine = input.readLine();
    while (nextLine.length() > 0) {
      splitLine = nextLine.split("\\s+");
      ((SpawnBuildingArea)this.locations.get(splitLine[0]))
                                        .setSpawnLocation(new Point(Double.parseDouble(splitLine[1]),
                                                                    Double.parseDouble(splitLine[2])));

      nextLine = input.readLine();
    }
    input.close();

    input.close();
    // add gateway zones
    input = new BufferedReader(new FileReader("assets/gamedata/map/Connections"));
    nextLine = input.readLine();
    while (nextLine != null) {
      splitLine = input.readLine().split(" ");
      for (int i = 0; i < 4; ++i) {
        if (!splitLine[i].equals("null")) {
          this.locations.get(nextLine)
              .getNeighbourZone(i)
              .setDestinationArea(this.locations.get(splitLine[i]));
        }
      }
      nextLine = input.readLine();
    }
    input.close();

    // add shops
    input = new BufferedReader(new FileReader("assets/gamedata/Shops"));
    nextLine = input.readLine();
    while (nextLine.length() > 0) {
      splitLine = nextLine.split("\\s+");
      Shop shopToAdd = (Shop)(IntrinsicTileComponentFactory.getComponent(splitLine[0]));
      int x = Integer.parseInt(splitLine[6]), y = Integer.parseInt(splitLine[7]);
      this.locations.get(splitLine[5]).getMapAt(x, y).setContent(shopToAdd);
      nextLine = input.readLine();
    }
    input.close();

    input = new BufferedReader(new FileReader("assets/gamedata/CraftingStores"));
    nextLine = input.readLine();
    while (nextLine.length() > 0) {
      splitLine = nextLine.split("\\s+");
      CraftingStore storeToAdd = (CraftingStore)(IntrinsicTileComponentFactory.getComponent(splitLine[0]));
      int x = Integer.parseInt(splitLine[6]), y = Integer.parseInt(splitLine[7]);
      this.locations.get(splitLine[5]).getMapAt(x, y).setContent(storeToAdd);
      nextLine = input.readLine();
    }
    input.close();
  }

  /**
   * [loadAreaMap]
   * Loads the map of the given area.
   * @author    Kevin Qiao, Paula Yuan, Joseph Wang, Candice Zhang
   * @param a   Area, the area used to load map.
   * @throws IOException
   */
  public void loadAreaMap(Area a) throws IOException {
    BufferedReader input = new BufferedReader(new FileReader("assets/maps/"
                                                             + a.getName()));
    String nextLine;
    
    //System.out.println("Initializing " + a.getName());
    Tile createdTile;
    for (int y = 0; y < a.getHeight(); ++y) {
      nextLine = input.readLine();
      for (int x = 0; x < a.getWidth(); ++x) {
        switch (nextLine.charAt(x)) {
          case '.':
            a.setMapAt(new GroundTile(x, y));
            break;
          case ',':
            a.setMapAt(new UnwalkableGround(x, y));
            break;
          case 'x':
            a.setMapAt(new GrassTile(x, y));
            break;
          case 'p':
            a.setMapAt(new PondTile(x, y));
            break;
          case 'r':
            a.setMapAt(new RiverTile(x, y));
            break;
          case 'l':
            a.setMapAt(new LakeTile(x, y));
            break;
          case 'o':
            a.setMapAt(new OceanTile(x, y));
            break;
          case '-':
            a.setMapAt(new DecorationTile(x, y,nextLine.charAt(x)));
            break;
          case 'b':
            a.setMapAt(new DecorationTile(x, y,nextLine.charAt(x)));
            break;
          case 'f':
            a.setMapAt(new DecorationTile(x, y,nextLine.charAt(x)));
            break;
          case 'g':
            a.setMapAt(new GroundTile(x, y));
            break;
          case 'd':
            a.setMapAt(new MineGatewayTile(x, y, MineGatewayTile.DOWNWARDS_LADDER));
            break;
          case 'e':
            a.setMapAt(new MineGatewayTile(x, y, MineGatewayTile.ELEVATOR));
            break;
          case 's':
            createdTile = new GroundTile(x, y);
            createdTile.setContent(IntrinsicTileComponentFactory.getComponent("ShippingContainer"));
            a.setMapAt(createdTile);
            break;
          case 'z':
            createdTile = new DecorationTile(x, y, 'f');
            createdTile.setContent(new Bed());
            a.setMapAt(createdTile);
            break;
        }
      }
    }

    a.setNeighbourZone(World.WEST,
                       World.findNeighbourZone(a, 0, 1, World.WEST));
    a.setNeighbourZone(World.EAST,
                       World.findNeighbourZone(a, a.getWidth()-1, 1, World.EAST));
    a.setNeighbourZone(World.NORTH,
                       World.findNeighbourZone(a, 1, 0, World.NORTH));
    a.setNeighbourZone(World.SOUTH,
                       World.findNeighbourZone(a, 1, a.getHeight()-1, World.SOUTH));

    input.close();
  }

  /**
   * [findNeighbourZone]
   * Finds and retrieves the neighbour zone of the area.
   * @author            Kevin Qiao
   * @param a           The Area used to find neighbour zone.
   * @param x           The int that represents the x posistion of the point.
   * @param y           The int that represents the y posistion of the point.
   * @param orientation The int that represents the orientation for searching.
   */
  public static GatewayZone findNeighbourZone(Area a,
                                              int x, int y,
                                              int orientation) {
    if (orientation == World.NORTH || orientation == World.SOUTH) {
      while (x < a.getWidth()-1 && a.getMapAt(x, y) == null) {
        ++x;
      }
      if (a.getMapAt(x, y) != null) {
        return new GatewayZone(x, y, orientation);
      }
    } else {
      while (y < a.getHeight()-1 && a.getMapAt(x, y) == null) {
        ++y;
      }
      if (a.getMapAt(x, y) != null) {
        return new GatewayZone(x, y, orientation);
      }
    }
    return null;
  }

  /**
   * [loadNPCS]
   * Loads in all the NPC data, and creates and stores NPCs accordingly
   * @author Paula Yuan
   */
  public void loadNPCS() throws IOException {
    BufferedReader input = new BufferedReader(new FileReader("assets/gamedata/NPCdata"));
    String lineToRead = input.readLine();
    String[] nextLineData = lineToRead.split("\\s+");
    String name;
    String[] dialogue = new String[5];
    String profileDescription;
    Area npcArea;
    NPC newNPC;
    int totalNPCs = 8;
    this.npcs = new NPC[totalNPCs];
    for (int i = 0; i < totalNPCs; i++) { 
      name = nextLineData[0];
      npcArea = this.locations.get(nextLineData[1]);
      for (int j = 0; j < 5; j++) {
        lineToRead = input.readLine();
        dialogue[j] = lineToRead;
      }
      profileDescription = input.readLine();
      input.readLine(); // consume blank line

      Point spawnPoint;
      if (nextLineData[1].equals("GeneralStore")) {
        spawnPoint = new Point(2, 5);
      } else if (nextLineData[1].equals("Blacksmith")) {
        spawnPoint = new Point(6, 2);
      } else {
        spawnPoint = new Point(5, 3);
      }

      newNPC = new NPC(spawnPoint,
                       name,
                       1, 
                       dialogue.clone(),
                       profileDescription);
      npcArea.addMoveable(newNPC);      
      this.npcs[i] = newNPC;
      this.emplaceFutureEvent((long)(Math.random()*1_000_000_000L*10),
                              new AutoMovementEvent(newNPC));
      lineToRead = input.readLine();
      nextLineData = lineToRead.split("\\s+");
    }

    input.close();
  }

  /**
   * [loadWorldMap]
   * Loads the areas of the world into a 2D array.
   * @author           Candice Zhang
   * @param filePath   String, the file path of the world map
   * @throws IOException
   */
  private void loadWorldMap(String filePath) throws IOException {
    BufferedReader input = new BufferedReader(new FileReader(filePath));
    String[] nextLineData = input.readLine().split("\\s+");
    int rows = Integer.parseInt(nextLineData[0]), cols = Integer.parseInt(nextLineData[1]);

    String[][] worldMap = new String[cols][rows];
    for (int i = 0; i < cols; i++) {
      String[] rowData = input.readLine().split("\\s+");
      for (int j = 0; j < rows; j++) {
        worldMap[i][j] = rowData[j];
      }
    }
    input.close();

    this.worldMap = worldMap;
  }

  /**
   * [getWorldMap]
   * Retrieves the areas of the world, in a 2D array.
   * @return String[][], map of the areas of the world.
   */
  public String[][] getWorldMap() {
    return this.worldMap;
  }

  /**
   * [getPlayer]
   * Retrieves the player in this world.
   * @return Player, the player in this world.
   */
  public Player getPlayer() {
    return this.player;
  }

  /**
   * [incrementPlayerAmountScrolled]
   * Checks if the player should increase the amount scrolled,
   * and increase its amount scrolled if should increase.
   * @author Candice Zhang
   */
  public void incrementPlayerAmountScrolled() {
    boolean shouldIncrese = false;
    if ((this.player.getCurrentMenuPage() == Player.SHOP_PAGE) &&
        ((this.player.getAmountScrolled()+WorldPanel.SHOP_ITEMS_PER_PAGE) < ((Shop)(this.player.getCurrentInteractingObj())).getItems().length)) {
      shouldIncrese = true;
    } else if ((this.player.getCurrentMenuPage() == Player.CRAFTING_PAGE) &&
              ((this.player.getAmountScrolled()+WorldPanel.CRAFTING_ITEMS_PER_PAGE) < this.player.getCraftingMachine().getProducts().length)) {
      shouldIncrese = true;
    } else if ((this.player.getCurrentMenuPage() == Player.SOCIAL_PAGE) &&
              ((this.player.getAmountScrolled()+WorldPanel.NPCS_PER_PAGE < this.npcs.length))) {
      shouldIncrese = true;
    }
    if (shouldIncrese) {
      this.player.incrementAmountScrolled();
    }
  }

  /**
   * [decrementPlayerAmountScrolled]
   * Checks if the player should decrease the amount scrolled,
   * and decrease its amount scrolled if should decrease.
   * @author Candice Zhang
   */
  public void decrementPlayerAmountScrolled() {
    boolean shouldDecrese = false;
    if ((this.player.getCurrentMenuPage() == Player.SHOP_PAGE) && (this.player.getAmountScrolled() > 0)) {
      shouldDecrese = true;
    } else if ((this.player.getCurrentMenuPage() == Player.CRAFTING_PAGE) && (this.player.getAmountScrolled() > 0)) {
      shouldDecrese = true;
    } else if ((this.player.getCurrentMenuPage() == Player.SOCIAL_PAGE) && (this.player.getAmountScrolled() > 0)) {
      shouldDecrese = true;
    }
    if (shouldDecrese) {
      this.player.decrementAmountScrolled();
    }
  }

  /**
   * [getPlayerArea]
   * Retrieves the area that the player is currently in.
   * @return Area, the area that the player is currently in.
   */
  public Area getPlayerArea() {
    return this.playerArea;
  }

  /**
   * [getMines]
   * Retrieves the mine areas.
   * @return MineArea, the mines
   */
  public MineArea getMines() {
    return this.mines;
  }

  /**
   * [getArea]
   * Retrieves the Area with the given name.
   * @param name String that represents the area's name.
   * @return     The Area with the given name.
   */
  public Area getArea(String name) {
    return this.locations.get(name);
  }

  /**
   * [getInGameNanoTime]
   * Retrieves the number of nanoseconds passed since last midnight.
   * @return long, the number of nanoseconds passed since last midnight.
   */
  public long getInGameNanoTime() {
    return this.inGameNanoTime;
  }

  /**
   * [getInGameDay]
   * Retrieves the current day of a season.
   * @return long, the current day of the season.
   */
  public long getInGameDay() {
    return this.inGameDay;
  }

  /**
   * [getInGameSeason]
   * Retrieves the current season in this world.
   * @return an integer that represents the current season.
   */
  public int getInGameSeason() {
    return this.inGameSeason;
  }

  /**
   * [getNPCs]
   * Retrieves all the NPCs in this world.
   * @return NPC[], the NPCs in this world in a String array.
   */
  public NPC[] getNPCs() {
    return this.npcs;
  }

  /**
   * [getSeasons]
   * Retrieves all seasons this world can have.
   * @return String[], a string array containing all seasons.
   */
  public static String[] getSeasons() {
    return World.SEASONS;
  }

  /**
   * [getDaysPerSeason]
   * Retrieves the number of days per season in this world.
   * @return int, number of days per season in this world.
   */
  public static int getDaysPerSeason() {
    return DAYS_PER_SEASON;
  }

 
}
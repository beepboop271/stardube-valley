/**
 * [Gateway]
 * 2020-01-15
 * A location that takes you to another gateway in another area.
 * @version 0.6
 * @author Kevin Qiao
 */

public class Gateway {
  public static final int OMNIDIRECTIONAL = 5;
  public static final int ONE_WAY = 6;

  private Gateway destinationGateway;
  private Area destinationArea;
  private Point origin;
  private int orientation;
  private boolean interactToMove;

  /**
   * [Gateway]
   * Constructor for a new Gateway.
   * @param originX         The x of this gateway.
   * @param originY         The y of this gateway.
   * @param orientation     The orientation of the gateway in int.
   * @param interactToMove  Whetner you need to interact to move or not.
   */
  public Gateway(int originX, int originY,
                 int orientation, boolean interactToMove) {
    this.origin = new Point(originX, originY);
    this.orientation = orientation;
    this.interactToMove = interactToMove;
  }

  /**
   * [Gateway]
   * Constructor for a new Gateway.
   * @param originX         The x of this gateway.
   * @param originY         The y of this gateway.
   * @param orientation     The orientation of the gateway as String.
   * @param interactToMove  Whetner you need to interact to move or not.
   */
  public Gateway(int originX, int originY,
                 String orientation, boolean interactToMove) {
    this.origin = new Point(originX, originY);
    if (orientation.equals("OMNIDIRECTIONAL")) {
      this.orientation = Gateway.OMNIDIRECTIONAL;
    } else if (orientation.equals("ONEWAY")) {
      this.orientation = Gateway.ONE_WAY;
    } else {
      throw new IllegalArgumentException("nonexistent orientation");
    }
    this.interactToMove = interactToMove;
  }

  /**
   * [canMove]
   * Checks if the moveable can move through the gateway.
   * @param m  The moveable that is being checked.
   * @return true if the moveable can move, false otherwise.
   */
  public boolean canMove(Moveable m) {
    Point pos = m.getPos();
    if ((Math.round(pos.x) == this.origin.x)
          && (Math.round(pos.y) == this.origin.y)) {
      if (this.orientation == World.NORTH) {
        return (pos.y <= this.origin.y-0.5+Player.SIZE);
      } else if (this.orientation == World.SOUTH) {
        return (pos.y >= this.origin.y+0.5-Player.SIZE-0.1);
      }
    }
    return false;
  }

  /**
   * [toDestinationPoint]
   * Gets the destination point of the destination gateway.
   * @param p     The Point of the moveable.
   * @param size  The size of the moveable.
   * @return      Point, the destination point.
   */
  public Point toDestinationPoint(Point p, double size) {
    if (this.orientation == Gateway.OMNIDIRECTIONAL) {
      if (this.destinationGateway.orientation == World.NORTH) {
        return this.destinationGateway.origin.translateNew(0, 0.5+size);
      } else {
        return this.destinationGateway.origin;
      }
    }
    if (this.destinationGateway.orientation == Gateway.ONE_WAY) {
      return this.destinationGateway.origin;
    }
    p.translate(-this.origin.x, -this.origin.y);
    if (this.destinationGateway.requiresInteractToMove()) {
      p.y = Math.copySign(0.5+size, p.y);
    } else {
      p.y = Math.copySign(0.5-size-0.1, -p.y);
    }
    p.translate(this.destinationGateway.origin);
    return p;
  }

  /**
   * [getDestinationArea]
   * Retrieves the destination area.
   * @return Area, the destination area.
   */
  public Area getDestinationArea() {
    return this.destinationArea;
  }

  /**
   * [setDestinationArea]
   * Assigns this destination area to a specified area.
   * @param destinationArea  The new destination area.
   */
  public void setDestinationArea(Area destinationArea) {
    this.destinationArea = destinationArea;
  }

  /**
   * [getDestinationGateway]
   * Retrieves the destination gateway.
   * @return Gateway, the destination gateway.
   */
  public Gateway getDestinationGateway() {
    return this.destinationGateway;
  }

  /**
   * [setDestinationGateway]
   * Assigns this destination area to a specified gateway.
   * @param destinationgateway  The new destination gateway.
   */
  public void setDestinationGateway(Gateway destinationGateway) {
    this.destinationGateway = destinationGateway;
  }

  /**
   * [getOrigin]
   * Retrieves this origin.
   * @return Point, the Point of this origin.
   */
  public Point getOrigin() {
    return this.origin;
  }

  /**
   * [getOrientation]
   * Retrieves this orientation.
   * @return int, the orientation of this gateway.
   */
  public int getOrientation() {
    return this.orientation;
  }

  /**
   * [requriesInteractToMove]
   * Checks if you need to interact to move.
   * @return true if you need to interact to move, false otherwise.
   */
  public boolean requiresInteractToMove() {
    return this.interactToMove;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode() {
    // auto generated
    final int prime = 31;
    int result = 1;
    if (this.origin == null) {
      result = prime * result;
    } else {
      result = prime * result + this.origin.hashCode();
    }
    return result;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean equals(Object obj) {
    // auto generated
    if (this == obj) {
      return true;
    } else if (obj == null) {
      return false;
    } else if (this.getClass() != obj.getClass()) {
      return false;
    }
    Gateway other = (Gateway)obj;
    if (this.origin == null) {
      if (other.origin != null) {
        return false;
      }
    } else if (!this.origin.equals(other.origin)) {
      return false;
    }
    return true;
  }
}
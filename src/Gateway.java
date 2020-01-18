public class Gateway {
  private Gateway destinationGateway;
  private Area destinationArea;
  private Point origin;
  private int orientation;
  private boolean interactToMove;

  public Gateway(int originX, int originY,
                 int orientation, boolean interactToMove) {
    this.origin = new Point(originX, originY);
    this.orientation = orientation;
    this.interactToMove = interactToMove;
  }

  public boolean canMove(Moveable m) {
    Point pos = m.getPos();
    if ((Math.round(pos.x) == this.origin.x)
          && (Math.round(pos.y) == this.origin.y)) {
      System.out.println(pos);
      if (this.orientation == World.NORTH) {
        return (pos.y <= this.origin.y-0.5+Player.SIZE);
      } else if (this.orientation == World.SOUTH) {
        return (pos.y >= this.origin.y+0.5-Player.SIZE-0.1);
      }
    }
    return false;
  }

  public Point toDestinationPoint(Point p, double size) {
    p.translate(-this.getOrigin().x, -this.getOrigin().y);
    if (this.getDestinationGateway().requiresInteractToMove()) {
      // p.y = Math.copySign(0.5-size-9.1, -p.y);
      p.y = Math.copySign(0.5+size, p.y);
    } else {
      p.y = Math.copySign(0.5-size-0.1, -p.y);
    }
    p.translate(this.getDestinationGateway().getOrigin());
    return p;
  }

  public Area getDestinationArea() {
    return this.destinationArea;
  }

  public void setDestinationArea(Area destinationArea) {
    this.destinationArea = destinationArea;
  }

  public Gateway getDestinationGateway() {
    return this.destinationGateway;
  }

  public void setDestinationGateway(Gateway destinationGateway) {
    this.destinationGateway = destinationGateway;
  }

  public Point getOrigin() {
    return this.origin;
  }

  public int getOrientation() {
    return this.orientation;
  }

  public boolean requiresInteractToMove() {
    return this.interactToMove;
  }

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
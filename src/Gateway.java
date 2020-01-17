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

  public Point toDestinationPoint(Point p, double size) {
    if (this.interactToMove) {
      p.x += -this.origin.x+this.getDestinationGateway().getOrigin().x;
      p.y = this.getDestinationGateway().getOrigin().y;
    } else {
      p.translate(-this.origin.x, -this.origin.y);
      p.y = Math.copySign(0.5-size, -p.y);
      p.translate(this.getDestinationGateway().getOrigin());
      if (this.orientation == World.NORTH) {
        p.translate(0, -0.51);
      } else if (this.orientation == World.SOUTH) {
        p.translate(0, 0.51);
      }
    }
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
public class Gateway {
  private Gateway destinationGateway;
  private Area destinationArea;
  private Point origin;

  public Gateway(int originX, int originY) {
    this.origin = new Point(originX, originY);
  }

  public Point toDestinationPoint(Point p, double size) {
    p.translate(-this.origin.x, -this.origin.y);
    p.y = Math.copySign(0.5-size, -p.y);
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
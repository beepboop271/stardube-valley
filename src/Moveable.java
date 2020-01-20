import java.util.LinkedHashSet;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.io.BufferedReader;
import java.io.FileReader;
/**
 * [Moveable]
 * 2019-12-19
 * @version 0.2
 * @author Kevin Qiao, Candice Zhang, Paula Yuan
 */
public abstract class Moveable {
  public final static int WALKSTEP_FRAMES = 8;
  public final static int RUNSTEP_FRAMES = 12;

  private Point position;
  private Vector2D velocity;
  private double size;
  private int orientation;
  private BufferedImage[][] images;
  private int framesPerSecond;
  //the vars below are a bit sketchy, i'll see if i can get rid of them - candice
  private long lastImgUpdateTime;
  private int[] curImgIdx;

  // should probably have made new abstract subclass
  // for moveables that follow certain velocity bc
  // not all moveables will be like this but thats
  // for another time

  public Moveable(Point position, double size) {
    this.position = position;
    this.velocity = new Vector2D(0, 0);
    this.size = size;
    this.orientation = World.SOUTH;
  }

  public Moveable(Point position, double size, String filePath, String name) {
    this.position = position;
    this.velocity = new Vector2D(0, 0);
    this.size = size;
    this.orientation = World.SOUTH;
    
    this.images = new BufferedImage[4][];
    this.framesPerSecond = Moveable.WALKSTEP_FRAMES;
    this.lastImgUpdateTime = System.nanoTime();
    this.curImgIdx = new int[]{2,0};
    try {
      BufferedReader input = new BufferedReader(new FileReader(filePath));
      for (int i = 0; i < 4; i++) {
        String folderPath = name + input.readLine();
        String[] folderFiles = new File("assets/images/"+folderPath).list();
        BufferedImage[] folderimgs = new BufferedImage[folderFiles.length];
        for (int j = 0; j < folderFiles.length; j++) {
          folderimgs[j] = ImageIO.read(new File("assets/images/"+folderPath+"/"+folderFiles[j]));
        }
        this.images[i] = folderimgs;
      }
      input.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public LinkedHashSet<Point> getIntersectingTiles(Vector2D offset) {
    LinkedHashSet<Point> intersections = new LinkedHashSet<Point>();
    // size will always be < 2 so this is enough
    // System.out.println(this.position);
    Point movedPosition = this.position.translateNew(offset.getX(), offset.getY());
    intersections.add(movedPosition.translateNew(-this.size, -this.size).round());
    intersections.add(movedPosition.translateNew(this.size, -this.size).round());
    intersections.add(movedPosition.translateNew(this.size, this.size).round());
    intersections.add(movedPosition.translateNew(-this.size, this.size).round());
    return intersections;
  }

  public Point getPos() {
    return ((Point)this.position.clone());
  }

  public void translatePos(double dx, double dy) {
    this.position.translate(dx, dy);
  }

  public void translatePos(Vector2D positionChange) {
    this.position.translate(positionChange.getX(), positionChange.getY());
  }

  public void setPos(Point position) {
    this.position = ((Point)position.clone());
  }

  public BufferedImage getImage() {
    return this.images[this.curImgIdx[0]][this.curImgIdx[1]];
  }

  public void updateImage() {
    if ((System.nanoTime()-this.lastImgUpdateTime) >= (1_000_000_000/this.framesPerSecond)) {
      this.curImgIdx[1] = (this.curImgIdx[1]+1) % this.images[this.curImgIdx[0]].length;
      this.lastImgUpdateTime = System.nanoTime();
    }
  }
  
  public int getOrientation() {
    return this.orientation;
  }

  public void setOrientation(int orientation) {
    this.orientation = orientation;

    for (int i = 0; i < 4; i++) {
      if ((this.orientation == i) && (this.curImgIdx[0] != i)) {
        this.curImgIdx[0] = i;
      }
    }
  }

  public Vector2D getVelocity() {
    return (Vector2D)this.velocity.clone();
  }

  public void setVelocity(double dx, double dy, double length) {
    this.velocity.setPos(dx, dy);
    this.velocity.setLength(length);
  }

  public int getHorizontalSpeed() {
    return (int)this.velocity.getX();
  }

  public void setHorizontalSpeed(double dx) {
    this.velocity.setPos(dx, this.velocity.getY());
  }

  public int getVerticalSpeed() {
    return (int)this.velocity.getY();
  }

  public void setVerticalSpeed(double dy) {
    this.velocity.setPos(this.velocity.getX(), dy);
  }
  
  public double getSize() {
    return this.size;
  }

  public int getFramesPerSecond() {
    return this.framesPerSecond;
  }

  public void setFramesPerSecond(int fps) {
    this.framesPerSecond = fps;
  }

  abstract Vector2D getMove(long elapsedNanoTime);
}
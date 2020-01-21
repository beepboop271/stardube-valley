import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * [LoopAnimatedMoveable]
 * 2020-01-19
 * A Moveable that has images for looped animation.
 * @version 0.2
 * @author Candice Zhang, Kevin Qiao, Paula Yuan
 */

public abstract class LoopAnimatedMoveable extends Moveable { 
  public static final int WALKSTEP_FRAMES = 8;
  public static final int RUNSTEP_FRAMES = 12;
  private static final String[] DIRECTIONS = {"/north", "/east", "/south", "/west"};

  private int orientation;
  private BufferedImage[][] images;
  private int framesPerSecond;
  private long lastImgUpdateTime;
  private int currentImgIdx;

  /**
   * [LoopAnimatedMoveable]
   * Constructor for a new LoopAnimatedMoveable.
   * @param position        A Point representing the moveable's position.
   * @param size            A double representing the moveable's size.
   * @param name            A String representing the moveable's name.
   * @param framesPerSecond An int representing the moveable's number of frames per second.
   */
  public LoopAnimatedMoveable(Point position, double size, String name,
                              int framesPerSecond) {
    super(position, size);
    this.setVelocity(0, 0, 0);
    this.orientation = World.SOUTH;
    
    this.images = new BufferedImage[4][];
    this.framesPerSecond = framesPerSecond;
    this.lastImgUpdateTime = System.nanoTime();
    this.currentImgIdx = 0;
    try {
      String[] directionImages;
      String folderPath;
      for (int i = 0; i < 4; i++) {
        folderPath = "assets/images/"+name+LoopAnimatedMoveable.DIRECTIONS[i];
        directionImages = new File(folderPath).list();

        this.images[i] = new BufferedImage[directionImages.length];
        for (int j = 0; j < directionImages.length; j++) {
          this.images[i][j] = ImageIO.read(new File(folderPath+"/"+directionImages[j]));
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * [getImage]
   * Retrieves the current image of this moveale.
   * @return BufferedImage, the current image of this moveale.
   */
  public BufferedImage getImage() {
    return this.images[this.orientation][this.currentImgIdx];
  }

  /**
   * [updateImage]
   * Orients the current image according to the fps of this moveable.
   * @author Candice Zhang
   */
  public void updateImage() {
    if ((System.nanoTime()-this.lastImgUpdateTime) >= (1_000_000_000/this.framesPerSecond)) {
      this.currentImgIdx = (this.currentImgIdx+1) % this.images[this.orientation].length;
      this.lastImgUpdateTime = System.nanoTime();
    }
  }
  
  /**
   * [getOrientation]
   * Retrieves the current orientation of this moveable.
   * @return int, the current orientation of this moveable.
   */
  public int getOrientation() {
    return this.orientation;
  }

  /**
   * [setOrientation]
   * Sets the current orientation of this moveale.
   * @param orientation The orientation of this moveale.
   */
  public void setOrientation(int orientation) {
    this.orientation = orientation;
  }

  /**
   * [getFramesPerSecond]
   * Retrieves the number of frames per second of this moveable.
   * @return int, the number of frames per second of this moveable.
   */
  public int getFramesPerSecond() {
    return this.framesPerSecond;
  }

  /**
   * [setFramesPerSecond]
   * Sets the number of frames per second of this moveale.
   * @param fps The number of frames per second of this moveale.
   */
  public void setFramesPerSecond(int fps) {
    this.framesPerSecond = fps;
  }

  /**
   * [getProfileImage]
   * Retrieves the image of this moveable to display in profiles.
   * @return BufferedImage, the profile image of this moveable.
   */
  public BufferedImage getProfileImage() {
    return this.images[World.SOUTH][0];
  }
}
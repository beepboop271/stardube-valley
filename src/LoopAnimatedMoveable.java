import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.imageio.ImageIO;

public abstract class LoopAnimatedMoveable extends Moveable {
  public final static int WALKSTEP_FRAMES = 8;
  public final static int RUNSTEP_FRAMES = 12;

  private int orientation;
  private BufferedImage[][] images;
  private int framesPerSecond;
  private long lastImgUpdateTime;
  private int[] curImgIdx;

  public LoopAnimatedMoveable(Point position, double size, String filePath, String name) {
    super(position, size);
    this.setVelocity(0, 0, 0);
    this.orientation = World.SOUTH;
    
    this.images = new BufferedImage[4][];
    this.framesPerSecond = LoopAnimatedMoveable.WALKSTEP_FRAMES;
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

  public int getFramesPerSecond() {
    return this.framesPerSecond;
  }

  public void setFramesPerSecond(int fps) {
    this.framesPerSecond = fps;
  }
}
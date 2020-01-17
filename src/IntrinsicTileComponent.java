import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * [IntrinsicTileComponent]
 * 2019-12-17
 * @version 0.1
 * @author Kevin Qiao, Paula Yuan, Joseph Wang
 */

public abstract class IntrinsicTileComponent extends TileComponent {
  private final String name;
  private final BufferedImage[] images;
  private final int[] offsets;

  public IntrinsicTileComponent(String name,
                                String imagesPath,
                                int[] offsets) throws IOException {
    this.name = name;
    this.offsets = offsets;
    
    File fileSystem = new File(imagesPath);
    if (fileSystem.isFile()) {
      this.images = new BufferedImage[1];
      this.images[0] = ImageIO.read(fileSystem);
    } else {
      String[] allFiles = fileSystem.list();
      this.images = new BufferedImage[allFiles.length];
      try {
        for (int i = 0, j = 0; i < allFiles.length; i++) {
          String extension = allFiles[i].substring(allFiles[i].lastIndexOf("."));
          if (extension.equals(".png")) {
            this.images[j] = ImageIO.read(new File(imagesPath + allFiles[i]));
            j++;
          }
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  public String getName() {
    return this.name;
  }

  public int getXOffset() {
    return this.offsets[0];
  }

  public int getYOffset() {
    return this.offsets[1];
  }

  public BufferedImage[] getImages() {
    return this.images;
  }
}
import java.io.IOException;

/**
 * [Player] 2020-01-19
 * 
 * @version 0.1
 * @author Paula Yuan
 */

public class NPC extends Moveable{
  private static final double MAX_SPEED = 3;
  public static final double SIZE = 0.35;
  private int index;
  private String[] dialogueRotation = new String[5]; // possibly change size?

  public NPC(Point position, String filePath, String name, int index,
            String[] dialogueRotation) throws IOException {
    super(position, SIZE, filePath, name);
    this.dialogueRotation = dialogueRotation;
    this.index = index;
  }

  public String getDialogue(int index) {
    return this.dialogueRotation[index];
  }
    
  @Override
  public Vector2D getMove(long elapsedNanoTime) {
    double elapsedSeconds = elapsedNanoTime/1_000_000_000.0;
    Vector2D positionChange = this.getVelocity();
    positionChange.setLength(NPC.MAX_SPEED*elapsedSeconds);
    // this.translatePos(positionChange);
    return positionChange;
  }

  public int getIndex() {
    return this.index;
  }
}
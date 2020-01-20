import java.io.IOException;

/**
 * [NPC]
 * 2020-01-19
 * 
 * @version 0.1
 * @author Paula Yuan, Candcie Zhang
 */

public class NPC extends LoopAnimatedMoveable { //TODO: JAVADOCS
  private static final double MAX_SPEED = 1;
  public static final double SIZE = 0.35;
  private int index;
  private String name;
  private String[] dialogueRotation = new String[5]; // possibly change size?
  private String profileDescription;

  public NPC(Point position, String name, int index,
            String[] dialogueRotation, String profileDescription) throws IOException {
    super(position, SIZE, "npcs/"+name, LoopAnimatedMoveable.WALKSTEP_FRAMES);
    this.dialogueRotation = dialogueRotation;
    this.name = name;
    this.profileDescription = profileDescription;
    this.index = index;
    this.name = name;
  }

  public String getDialogue(int index) {
    return this.dialogueRotation[index];
  }
  
  public String getName() {
    return this.name;
  }

  public String getProfileDescription() {
    return this.profileDescription;
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

  public void setIndex(int index) {
    this.index = index;
  }

  public String getName() {
    return this.name;
  }

  @Override
  public double getXOffset() {
    return 0;
  }

  @Override
  public double getYOffset() {
    return -1;
  }
}
import java.io.IOException;

/**
 * [NPC]
 * A LoopAnimatedMoveable to represent an NPC in the game.
 * 2020-01-19
 * @version 0.11
 * @author Kevin Qiao, Paula Yuan, Candice Zhang
 */

public class NPC extends LoopAnimatedMoveable {
  private static final double MAX_SPEED = 1;
  public static final double SIZE = 0.35;
  private int index;
  private String name;
  private String[] dialogueRotation = new String[5];
  private String profileDescription;

  /**
   * [NPC]
   * Constructor for a new NPC.
   * @param position           A Point that represents the position of the NPC.
   * @param name               A String that represents the name of the NPC.
   * @param index              An int that represents the index of this NPC.
   * @param dialogueRotation   An String[] that holds all dialogues of this NPC.
   * @param profileDescription An String that represents the description of this NPC.
   * @throws IOException
   */
  public NPC(Point position, String name, int index,
            String[] dialogueRotation, String profileDescription) throws IOException {
    super(position, SIZE, "npcs/"+name, LoopAnimatedMoveable.WALKSTEP_FRAMES);
    this.dialogueRotation = dialogueRotation;
    this.name = name;
    this.profileDescription = profileDescription;
    this.index = index;
    this.name = name;
  }

  /**
   * [getDialogue]
   * Retrieves a string dialogue based on specified index.
   * @param index The index to retrieve dialogue with.
   * @return String, the dialogue.
   */
  public String getDialogue(int index) {
    return this.dialogueRotation[index];
  }
  
  /**
   * [getName]
   * Retrieves this NPC's name.
   * @return String, the name of this NPC.
   */
  public String getName() {
    return this.name;
  }

  /**
   * [getProfileDescription]
   * Retrieves this NPC's profile description.
   * @return String, the profile description of this NPC.
   */
  public String getProfileDescription() {
    return this.profileDescription;
  }
  
  /**
   * [getMove]
   * Calculates this NPC's movement based on its current velocity
   * and the elapsed time.
   * @author Kevin Qiao, Paula Yuan
   * @param elapsedNanoTime The elapsed time.
   * @return Vector2D, this NPC's movement.
   */
  @Override
  public Vector2D getMove(long elapsedNanoTime) {
    double elapsedSeconds = elapsedNanoTime/1_000_000_000.0;
    Vector2D positionChange = this.getVelocity();
    positionChange.setLength(NPC.MAX_SPEED*elapsedSeconds);
    return positionChange;
  }

  /**
   * [getIndex]
   * Retrieves the index of this NPC, used for finding dialogue.
   * @return int, the current dialogue index.
   */
  public int getIndex() {
    return this.index;
  }

  /**
   * [setIndex]
   * Sets the dialogue index of this NPC.
   * @param index The new dialogue index.
   */
  public void setIndex(int index) {
    this.index = index;
  }
  
  /**
   * {@inheritDocs}
   */
  @Override
  public double getXOffset() {
    return 0;
  }

  /**
   * {@inheritDocs}.
   */
  @Override
  public double getYOffset() {
    return -1;
  }
}
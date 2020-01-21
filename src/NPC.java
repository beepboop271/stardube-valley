import java.io.IOException;

/**
 * [NPC]
 * 2020-01-19
 * A LoopAnimatedMoveable to represent an NPC in the game.
 * @version 0.1
 * @author Paula Yuan, Candice Zhang
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
   * @param position            A Point that represents the position of the NPC.
   * @param name                A Srting that represents the name of the NPC.
   * @param index               An int that represents the index of this NPC.
   * @param dialogueRotation    An String[] that holds all dialogues of this NPC.
   * @param profileDescription  An String that represents the description of this NPC.
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
   * Retrieves a string dialogue.
   * @author Paula Yuan
   * @param index, int index
   * @return String the dialogue
   */
  public String getDialogue(int index) {
    return this.dialogueRotation[index];
  }
  
  /**
   * [getName]
   * Retrieves the NPC's name
   * @author Paula Yuan
   * @return the string name of the NPC.
   */
  public String getName() {
    return this.name;
  }

  /**
   * [getProfileDescription]
   * Retrieves the NPC's profile description
   * @return String, the profile description
   */
  public String getProfileDescription() {
    return this.profileDescription;
  }
  
  @Override
  /**
   * {@inheritDocs}
   */
  public Vector2D getMove(long elapsedNanoTime) {
    double elapsedSeconds = elapsedNanoTime/1_000_000_000.0;
    Vector2D positionChange = this.getVelocity();
    positionChange.setLength(NPC.MAX_SPEED*elapsedSeconds);
    return positionChange;
  }

  /**
   * [getIndex]
   * Retrieves the index of this NPC.
   * @author Paula Yuan
   * @return int index
   */
  public int getIndex() {
    return this.index;
  }

  /**
   * [setIndex]
   * Sets the index of this NPC.
   * @author Paula Yuan
   * @param int index
   */
  public void setIndex(int index) {
    this.index = index;
  }
  
  @Override
  /**
   * {@inheritDocs}
   */
  public double getXOffset() {
    return 0;
  }

  @Override
  /**
   * {@inheritDocs}.
   */
  public double getYOffset() {
    return -1;
  }
}
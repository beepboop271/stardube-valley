import java.awt.Graphics;

/**
 * [Button]
 * An abstract class to represent a button by its x, y position, width and height.
 * 2020-01-19
 * @version 0.1
 * @author Candice Zhang
 */

public abstract class Button {
  private int x, y, width, height;

  /**
   * [Button]
   * Constructor for a new Button.
   * @param x An int representing the x position of the button.
   * @param y An int representing the y position of the button.
   * @param w An int representing the width of the button.
   * @param h An int representing the height of the button.
   */
  Button(int x, int y, int w, int h) {
    this.x = x;
    this.y = y;
    this.width = w;
    this.height = h;
  }

  /**
   * [inButton]
   * Checks if a given location is inside this button.
   * @param posX An int representing the x position of the location to check.
   * @param posY An int representing the y position of the location to check.
   * @return     boolean, true if the given location is inside this button,
   *             false otherwise.
   */
  public boolean inButton(int posX, int posY) {
    return ((posX >= this.x) && (posX <= this.x+this.width) && (posY >= this.y) && (posY <= this.y+this.height));
  }

  /**
   * [getX]
   * Retrieves the x position of this button.
   * @return int, the x position of this button.
   */
  public int getX() {
    return this.x;
  }

  /**
   * [getY]
   * Retrieves the y position of this button.
   * @return int, the y position of this button.
   */
  public int getY() {
    return this.y;
  }

  /**
   * [getW]
   * Retrieves the width of this button.
   * @return int, the width of this button.
   */
  public int getW() {
    return this.width;
  }

  /**
   * [getH]
   * Retrieves the height of this button.
   * @return int, the height of this button.
   */
  public int getH() {
    return this.height;
  }

  /**
   * [drawButton]
   * Draws the button with a Graphics object.
   * @param g The Graphics object used to draw.
   */
  public abstract void drawButton(Graphics g);
}
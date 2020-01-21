import java.awt.Font;
import java.awt.Graphics;
import java.awt.Color;

/**
 * [ButtonWithText]
 * An class to represent a button by its x, y position, width, height,
 * text on the button and the font of the text.
 * 2020-01-19
 * @version 0.1
 * @author Candice Zhang
 */

public class ButtonWithText extends Button {
  private String textOnButton;
  private Font textFont;

  /**
   * [ButtonWithText]
   * Constructor for a new ButtonWithText.
   * @param x            An int representing the x position of the button.
   * @param y            An int representing the y position of the button.
   * @param w            An int representing the width of the button.
   * @param h            An int representing the height of the button.
   * @param textOnButton The String the button has.
   * @param textFont     The Font of the text on button.
   */
  public ButtonWithText(int x, int y, int w, int h, String textOnButton, Font textFont) {
    super(x, y, w, h);
    this.textOnButton = textOnButton;
    this.textFont = textFont;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void drawButton(Graphics g) {
    g.setFont(this.textFont);

    g.setColor(new Color(253, 230, 170));
    g.fillRect(this.getX(), this.getY(), this.getW(), this.getH());

    g.setColor(new Color(100, 20, 0));
    g.drawRect(this.getX(), this.getY(), this.getW(), this.getH());

    g.setColor(new Color(103, 35, 0));
    g.drawString(this.textOnButton, this.getX() + (this.getW()-g.getFontMetrics().stringWidth(this.textOnButton))/2,
                                    this.getY() + g.getFontMetrics().getHeight());
  }
}
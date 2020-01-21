import javax.swing.JFrame;

/**
 * [StardubeFrame]
 * The JFrame that holds the world panel.
 * 2020-01-13
 * @version 0.6
 * @author Kevin Qiao, Candice Zhang
 */

@SuppressWarnings("serial")
public class StardubeFrame extends JFrame {
  /**
   * [StardubeFrame]
   * Constructor for a new StardubeFrame.
   * @author Kevin Qiao, Candice Zhang
   * @param stardubeWorld The world of stardube.
   */
  public StardubeFrame(World stardubeWorld) {
    super("Stardube Valley");
    this.setContentPane(new WorldPanel(stardubeWorld, 1408, 768));
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.pack();
    this.setVisible(true);
  }

  /**
   * [refresh]
   * Repaints the screen.
   */
  public void refresh() {
    this.repaint();
  }
}
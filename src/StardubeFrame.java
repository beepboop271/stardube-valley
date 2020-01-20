import javax.swing.JFrame;

/**
 * [StardubeFrame]
 * The JFrame that holds the world panel.
 * 2019-12-19
 * @version 0.1
 * @author Kevin Qiao
 */

@SuppressWarnings("serial")
public class StardubeFrame extends JFrame {
  /**
   * [StardubeFrame]
   * Constructor for a new StardubeFrame.
   * @param stardubeWorld  The world of stardube.
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
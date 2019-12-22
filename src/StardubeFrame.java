import javax.swing.JFrame;

/**
 * [StardubeFrame]
 * 2019-12-19
 * @version 0.1
 * @author Kevin Qiao
 */
@SuppressWarnings("serial")
public class StardubeFrame extends JFrame {
  public StardubeFrame(World stardubeWorld) {
    super("Stardube Valley");
    this.setContentPane(new WorldPanel(stardubeWorld, 1408, 768));
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.pack();
    this.setVisible(true);
  }

  public void refresh() {
    this.repaint();
  }
}
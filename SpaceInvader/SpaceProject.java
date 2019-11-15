package SpaceInvader;
 
import java.awt.EventQueue;
import javax.swing.JFrame;
 
public class SpaceProject extends JFrame implements Commons {
 
    public SpaceProject() {
 
        initUI();
    }
 
    private void initUI() {
 
        add(new Board());
        setTitle("Space Invaders");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(BOARD_WIDTH, BOARD_HEIGHT);
        setLocationRelativeTo(null);
        setResizable(false);
    }
 
    public static void main(String[] args) {
       
        EventQueue.invokeLater(() -> {
            SpaceProject ex = new SpaceProject();
            ex.setVisible(true);
        });
    }
}
 

package SpaceInvader;
 
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
 
public class SpaceProject extends JFrame implements Commons {
	
	private static final long serialVersionUID = -3707317083523991011L;
	public SpaceProject() { 
        initUI();
		
    }
 
    private void initUI() {
    	//add(new HighScoreMenu(true));
        add(new Board());
    	//add(new TitleScreen());
        setTitle("Space Project");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(BOARD_WIDTH, BOARD_HEIGHT);
        setLocationRelativeTo(null);
        setResizable(false);
		
    }
    public static SpaceProject ex;
    public static HighScoreMenu hs;
    public static void main(String[] args) {
       
        //EventQueue.invokeLater(() -> {
    		ex = new SpaceProject();
            ex.setVisible(true);
            //hs = new HighScoreMenu();
            //hs.vis(true);
        //});
    }
    public static void setHSVis(boolean yn) {
    	hs.setVisible(yn);
    }
}
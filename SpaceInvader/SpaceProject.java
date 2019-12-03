package SpaceInvader;
 
import java.awt.EventQueue;
import javax.swing.JFrame;
 
public class SpaceProject extends JFrame implements Commons {

	private static final long serialVersionUID = -3707317083523991011L;
	public SpaceProject() { 
        initUI();
		
    }
 
    private void initUI() {
 
        add(new Board());
    	//add(new TitleScreen());
        setTitle("Space Project");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(BOARD_WIDTH, BOARD_HEIGHT);
        setLocationRelativeTo(null);
        setResizable(false);
        
    }
 
    public static void main(String[] args) {
       
        //EventQueue.invokeLater(() -> {
            SpaceProject ex = new SpaceProject();
            ex.setVisible(true);
        //});
    }
}
package SpaceInvader;

import javax.swing.JFrame;

import SpaceInvader.Systems.Commons;
import SpaceInvader.Systems.ImagePaths;

public class SpaceProject extends JFrame implements Commons {

	private static final long serialVersionUID = -3707317083523991011L;
	public static String texturePackPath = "DefaultTextures";
	public static SpaceProject spaceProject;
	
	public SpaceProject() {
		initUI();
	}

	private void initUI() {
		ImagePaths.setImagePath("src\\images\\"+ texturePackPath +"\\");
		add(new GameBoard());
		setTitle("Space Project");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(BOARD_WIDTH, BOARD_HEIGHT);
		setLocationRelativeTo(null);
		setResizable(false);
	}

	public static void main(String[] args) {
		spaceProject = new SpaceProject();
		spaceProject.setVisible(true);
	}
}
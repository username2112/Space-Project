package SpaceInvader;

import javax.swing.JFrame;

public class SpaceProject extends JFrame implements Commons {

	private static final long serialVersionUID = -3707317083523991011L;

	public SpaceProject() {
		initUI();
	}

	private void initUI() {
		ImagePaths.setImagePath("src\\images\\AlexSprites\\");
		add(new GameBoard());
		setTitle("Space Project");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(BOARD_WIDTH, BOARD_HEIGHT);
		setLocationRelativeTo(null);
		setResizable(false);

	}

	public static void main(String[] args) {
		SpaceProject spaceProject = new SpaceProject();
		spaceProject.setVisible(true);
	}
}
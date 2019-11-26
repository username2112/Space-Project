package SpaceInvader;

import javax.swing.ImageIcon;

public class BombShot extends Sprite {
	private final String shotImg = "src/images/shot.png";
	private final int H_SPACE = 6;
	private final int V_SPACE = 4;

	public BombShot() {

	}

	public BombShot(int x, int y) {
		init(x, y);

	}

	private void init(int x, int y) {
		ImageIcon ii = new ImageIcon(shotImg);
		setImage(ii.getImage());

		this.setVisible(true);

		setX(x + H_SPACE);
		setY(y + V_SPACE);
	}
}

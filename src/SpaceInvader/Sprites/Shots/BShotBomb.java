package SpaceInvader.Sprites.Shots;

import javax.swing.ImageIcon;

import SpaceInvader.Sprites.Sprite;
import SpaceInvader.Systems.ImagePaths;

public class BShotBomb extends Sprite {

	private final String shotImg = ImagePaths.getSBombPath();
	private final int H_SPACE = 64;
	private final int V_SPACE = 64;

	public BShotBomb() {
	}

	public BShotBomb(int x, int y) {
		initShot(x, y);
		height = 16;
		width = 16;
	}

	private void initShot(int x, int y) {
		ImageIcon ii = new ImageIcon(shotImg);
		setImage(ii.getImage());

		this.setVisible(true);

		setX(x + H_SPACE);
		setY(y + V_SPACE);
	}
}

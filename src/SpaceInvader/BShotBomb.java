package SpaceInvader;

import javax.swing.ImageIcon;

public class BShotBomb extends Sprite {

	private final String shotImg = "src\\images\\SBomb.png";
	private final int H_SPACE = 64;
	private final int V_SPACE = 64;
	private final int BS = 20;

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

	private void explodeInRange(int TargetX, int TargetY) {
		if (TargetX <= this.x - BS && TargetX >= this.dx + BS) {

		}
	}
}

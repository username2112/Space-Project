package SpaceInvader;

import javax.swing.ImageIcon;

public class Title extends Sprite {

	public Title(int x, int y) {
		makeTitle(x, y);
	}

	private void makeTitle(int x, int y) {
		this.x = x;
		this.y = y;
		this.setVisible(true);
		ImageIcon ii = new ImageIcon(ImagePaths.getTitlePath());
		setImage(ii.getImage());
	}
}

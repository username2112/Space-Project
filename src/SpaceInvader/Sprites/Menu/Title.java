package SpaceInvader.Sprites.Menu;

import javax.swing.ImageIcon;

import SpaceInvader.Sprites.Sprite;
import SpaceInvader.Systems.ImagePaths;

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

package SpaceInvader;

import javax.swing.ImageIcon;

public class Asteroid extends Sprite {
	private final String alienImg = ImagePaths.getAsteroidPath();

	public Asteroid(int x, int y) {
		initAlien(x, y);
	}

	private void initAlien(int x, int y) {
		this.x = x;
		this.y = y;
		ImageIcon ii = new ImageIcon(alienImg);
		setImage(ii.getImage());
	}
}

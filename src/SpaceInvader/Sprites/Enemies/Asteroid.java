package SpaceInvader.Sprites.Enemies;

import javax.swing.ImageIcon;

import SpaceInvader.ImagePaths;
import SpaceInvader.Sprites.Sprite;

public class Asteroid extends Sprite {
	private final String alienImg = ImagePaths.getAsteroidPath();

	public Asteroid(int x, int y) {
		initAlien(x, y);
		width = 32;
		height = 32;
	}

	private void initAlien(int x, int y) {
		this.x = x;
		this.y = y;
		ImageIcon ii = new ImageIcon(alienImg);
		setImage(ii.getImage());
	}
}

package SpaceInvader.Sprites.Enemies;

import javax.swing.ImageIcon;

import SpaceInvader.Sprites.Sprite;
import SpaceInvader.Systems.ImagePaths;

public class Alien extends Sprite {

	private Bomb bomb;
	private final String alienImg = ImagePaths.getAlienPath();

	public Alien(int x, int y) {
		initAlien(x, y);
		height = 32;
		width = 32;
	}

	private void initAlien(int x, int y) {
		this.x = x;
		this.y = y;
		bomb = new Bomb(x, y);
		ImageIcon ii = new ImageIcon(alienImg);
		setImage(ii.getImage());
	}

	public void act(int direction) {

		this.x += direction;
	}

	public Bomb getBomb() {
		return bomb;
	}

	public class Bomb extends Sprite {
		private final String bombImg = ImagePaths.getBombPath();
		private boolean destroyed;

		public Bomb(int x, int y) {
			initBomb(x, y);
		}

		private void initBomb(int x, int y) {
			setDestroyed(true);
			this.x = x;
			this.y = y;
			ImageIcon ii = new ImageIcon(bombImg);
			setImage(ii.getImage());
		}

		public void setDestroyed(boolean destroyed) {
			this.destroyed = destroyed;
		}

		public boolean isDestroyed() {
			return destroyed;
		}
	}
}
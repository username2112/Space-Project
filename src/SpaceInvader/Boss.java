package SpaceInvader;

import java.util.Random;

import javax.swing.ImageIcon;

@SuppressWarnings("unused")
public class Boss extends Sprite {

	private int direction = 0;// 0 = right 1 = left
	private Bomb bomb;
	private String[] imgList = new String[] { ImagePaths.getBossPath(), ImagePaths.getBoss0Path(),
			ImagePaths.getBoss0GlowPath(), ImagePaths.getBoss1Path(), ImagePaths.getBoss2Path(),
			ImagePaths.getCthulhuPath() };
	public boolean shoot = false;
	public int Boss_Type;
	public Boolean topTouch = false;

	public Boss(int x, int y) {
		initBoss(x, y);
		height = 128;
		width = 128;
	}

	private void initBoss(int x, int y) {

		this.x = x;
		this.y = y;

		Random r = new Random();
		Boss_Type = r.nextInt(2);
		String bossImg = imgList[r.nextInt(imgList.length - 1)];

		bomb = new Bomb(x, y);

		if (Boss_Type == 0) {
			ImageIcon ii = new ImageIcon(bossImg);
			setImage(ii.getImage());
			// System.out.println("boss type 1");
		} else {
			ImageIcon ii = new ImageIcon(bossImg);
			setImage(ii.getImage());
			// System.out.println("boss type 2");
		}
	}

	public void act() {
		if (this.isVisible()) {
			switch (Boss_Type) {
			// type 1
			case 0:
				if (direction == 0)
					moveRightNoStop(3);
				else if (direction == 1)
					moveLeftNoStop(3);
				else if (direction > 1)
					direction = 0;
				break;

			// type 2 center bounce
			case 1:
				if (direction == 0) {
					moveRightNoStop(3);
					moveDown(1);
				} else if (direction == 1) {
					moveLeftNoStop(3);
					moveUpNoStop(1);
				} else if (direction == 2) {
					moveLeftNoStop(3);
					moveDown(1);
				} else if (direction == 3) {
					moveRightNoStop(3);
					moveUpNoStop(1);
				} else if (direction > 3) {
					direction = 0;
				}

				break;
			}
			Random ra = new Random();
			int r = ra.nextInt(1000) + 1;
			if (r <= 6 && this.isVisible()) {
				shoot = true;
			}
		}
	}

	// no stop movements
	private void moveRightNoStop(int speed) {
		if (x < Commons.BOARD_WIDTH - 128)
			this.x += speed;
		else if (x >= Commons.BOARD_WIDTH - 128) {
			direction++;
		}
	}

	private void moveLeftNoStop(int speed) {
		if (x > 0)
			this.x -= speed;
		else if (x <= 0) {
			direction++;
		}
	}

	private void moveUpNoStop(int speed) {
		if (y >= 30)
			this.y -= speed;
		else if (y <= 30) {
			direction++;
		}
	}

	private void moveDownNoStop(int speed) {
		if (y < Commons.GROUND - 100)
			this.y += speed;
		else if (y >= Commons.GROUND - 100) {
			direction++;
		}
	}

	// step movements
	private void moveUp(int d) {
		this.y -= d;
	}

	private void moveDown(int d) {
		this.y += d;
	}

	private void moveRight(int d) {
		this.x += d;
	}

	private void moveLeft(int d) {
		this.x -= d;
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

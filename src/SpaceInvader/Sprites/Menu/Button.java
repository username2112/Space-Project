package SpaceInvader.Sprites.Menu;

import java.awt.Point;

import javax.swing.ImageIcon;

import SpaceInvader.Sprites.Sprite;

public class Button extends Sprite {
	public boolean isPressed = false;

	public Button(int x, int y, String buttonImage) {
		makeButton(x, y, buttonImage);
		width = 256;
		height = 64;
	}

	public void checkMouse(Point p, Button b) {
		if (p.x > b.x && p.x < b.x + 256 && p.y > b.y && p.y < b.y + 64) {
			this.isPressed = true;
		} else {
			this.isPressed = false;
		}
	}

	private void makeButton(int x, int y, String buttonImage) {
		this.x = x;
		this.y = y;
		this.setVisible(true);
		ImageIcon ii = new ImageIcon(buttonImage);
		setImage(ii.getImage());
	}

}

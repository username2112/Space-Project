package SpaceInvader;

import javax.swing.ImageIcon;

import SpaceInvader.Alien.Bomb;

public class Asteroid extends Sprite{
	 private int startX;
	   private final String alienImg = "src/images/Asteroid.png";

	   public Asteroid(int x, int y) {
	       initAlien(x, y);
	       startX = x;
	   }

	   private void initAlien(int x, int y) {

	       this.x = x;
	       this.y = y;
	       ImageIcon ii = new ImageIcon(alienImg);
	       setImage(ii.getImage());
	   }

	   public void act(int direction) {
	   }
}

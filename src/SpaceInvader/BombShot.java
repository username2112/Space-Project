package SpaceInvader;

import javax.swing.ImageIcon;

public class BombShot extends Sprite{
	 private final String shotImg = "src\\images\\shot .png";
	   private final int H_SPACE = 6;
	   private final int V_SPACE = 4;
	   
	   public BombShot() {
	   }
	   
	   public BombShot(int x, int y) {
		   width = 8;
		   height = 8;
		   initBombShot(x,y);
		   
	   }
	   
	   private void explode() {
		   width = 64;
		   height = 64;
	   }
	   
	   private void initBombShot(int x, int y) {
	       ImageIcon ii = new ImageIcon(shotImg);
	       setImage(ii.getImage());
	       
	       setX(x + H_SPACE);
	       setY(y - V_SPACE);
	       
	   }


}

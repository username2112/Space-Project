package SpaceInvader;

import javax.swing.ImageIcon;

public class BShotBomb extends Sprite {

   private final String shotImg = "src\\images\\SBomb.png";
   private final int H_SPACE = 64;
   private final int V_SPACE = 64;
   private final int BS = 20;
   private int iterator;
   private Long ST;
   public boolean hit;
   
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
   
   private void explode(Sprite target, long clock) {
	   if(iterator == 0) {
		   iterator++;
		   ST = Long.valueOf(clock);
		   explode(target, clock);
	   }
	   
   }
   
   private void explAnim() {
	   
   }
   
   public void reset() {
	   ST = (long) 0;
	   iterator = 0;
	   hit = false;
   }
}

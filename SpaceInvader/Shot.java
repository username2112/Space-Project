package SpaceInvader;

import javax.swing.ImageIcon;

public class Shot extends Sprite {

   private final String shotImg = "images/shot .png";
   private final int H_SPACE = 6;
   private final int V_SPACE = 1;
   public static int ST;
   public static int BD = 0;

   public Shot() {
   }

   public Shot(int x, int y, int type) {
       switch(type) {
    	   case 0:
    		   initShot(x, y);
    		   break;
    	   case 1:
    		   initRicochet(x, y, type); 
    		   break;
    	   case 2:
    		   initRicochet(x, y, type); 
    		   break;
       }
   }

   private void initShot(int x, int y) {
       ImageIcon ii = new ImageIcon(shotImg);
       setImage(ii.getImage());
       
       setX(x + H_SPACE);
       setY(y - V_SPACE);
       ST = 0;
   }
   private void initRicochet(int x, int y, int dir) {
       ImageIcon ii = new ImageIcon(shotImg);
       setImage(ii.getImage());
       if(dir == 1) {//left
           setX(x + H_SPACE);
           setY(y - V_SPACE);
           ST = 1;
       } else {//right
           setX(x + H_SPACE);
           setY(y - V_SPACE);
           ST = 2;
       }
   }
   public static int getType() {
	   return ST;
   }
   public void ricL() {
	   if(x >= Commons.BOARD_WIDTH - 6) {
		   BD = 0;   
	   } else if(x <= 0) {
		   BD = 1;
	   }
	   if(BD == 0) {
		   setX(x - 3);
		   setY(y - 3);	
	   } else {
		   setX(x + 3);
		   setY(y - 3);	
	   }
   }
   public void ricR() {
	   if(x >= Commons.BOARD_WIDTH - 6) {
		   BD = 1;   
	   } else if(x <= 0) {
		   BD = 0;
	   }
	   if(BD == 1) {
		   setX(x - 3);
		   setY(y - 3);	
	   } else {
		   setX(x + 3);
		   setY(y - 3);	
	   }   }
	public void resetBD() {
		BD = 0;
	}
}

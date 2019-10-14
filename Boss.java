package SpaceInvader;

import javax.swing.ImageIcon;

public class Boss extends Sprite {

   private String direction = "left";
   private Bomb bomb;
   private int startX;
   private final String bossImg = "src/images/Boss.png";

   public Boss(int x, int y) {
       initBoss(x, y);
       startX = x;
   }

   private void initBoss(int x, int y) {

       this.x = x;
       this.y = y;

       bomb = new Bomb(x, y);
       ImageIcon ii = new ImageIcon(bossImg);
       setImage(ii.getImage());
   }

   public void act(){
       direction = "right";
	   if(x < Commons.BOARD_WIDTH - 128) {
		   this.x += 3;
		   //System.out.println("right");
	   	   direction = "left";
	   	   System.out.println("can move left " + direction + ", " + x);
	   } else if(x > 128) {
		   this.x -= 3;
		   System.out.println("left");
	   	   direction = "right";
	   }
   }
   
   
   public Bomb getBomb() {
       return bomb;
   }

   public class Bomb extends Sprite {
       private final String bombImg = "src/images/bomb.png";
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
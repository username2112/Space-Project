package SpaceInvader;

import java.util.Random;

import javax.swing.ImageIcon;

public class Boss extends Sprite {

   private String direction = "right";
   private Bomb bomb;
   private int startX;
   private final String bossImg = "src/images/Boss.png";
   public boolean shoot = false;
   
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
       if(this.isVisible()) {
    	   if(direction == "right") {
    		   moveRight(3);
    	   } else {
    		   moveLeft(3);
    	   }
       }
       Random ra = new Random() ;
       int r = ra.nextInt(1000) + 1;
	   if(r <= 6 && this.isVisible()){
		   shoot = true;
	   }
   }
   
   private void moveRight(int speed) {
	   if(x < Commons.BOARD_WIDTH - 128)
		   this.x += speed;
	   	else if(x >= Commons.BOARD_WIDTH - 128)
		   direction = "left";
   }
   private void moveLeft(int speed) {
	   if(x > 0)
		   this.x -= speed;
	   else if(x <= 0)
		   direction = "right";
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

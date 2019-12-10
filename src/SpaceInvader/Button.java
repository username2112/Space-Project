package SpaceInvader;

import java.awt.Point;

import javax.swing.ImageIcon;


public class Button extends Sprite {
	public boolean isPressed = false;
   private int startX;
   private String[] path = {"src\\images\\button0.png", "src\\images\\button1.png", "src\\images\\button2.png"};
   
   public Button(int x, int y, int type) {
       makeButton(x, y, type);
       startX = x;
   }
   public void checkMouse(Point p, Button b) {
   	if(p.x > b.x && p.x < b.x+256 && p.y > b.y && p.y < b.y+64) {
   		this.isPressed = true;
   	} else {
   		this.isPressed = false;
   	}
   }
   

   private void makeButton(int x, int y, int type) {
	   
       this.x = x;
       this.y = y;
       this.setVisible(true);
       ImageIcon ii = new ImageIcon(path[type]);
       setImage(ii.getImage());
   }

}

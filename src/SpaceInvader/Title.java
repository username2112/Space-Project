package SpaceInvader;

import java.awt.Point;

import javax.swing.ImageIcon;


public class Title extends Sprite {
   private int startX;
   private String path = "src\\images\\title.png";
   
   public Title(int x, int y) {
       makeTitle(x, y);
       startX = x;
   }
   

   private void makeTitle(int x, int y) {
	   
       this.x = x;
       this.y = y;
       this.setVisible(true);
       ImageIcon ii = new ImageIcon(path);
       setImage(ii.getImage());
   }

}

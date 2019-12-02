package SpaceInvader;

import javax.swing.ImageIcon;

public class BShot extends Sprite {

   private final String shotImg = "src\\images\\SBomb.png";
   private final int H_SPACE = 64;
   private final int V_SPACE = 64;

   public BShot() {
   }

   public BShot(int x, int y) {
       initShot(x, y);
   }

   private void initShot(int x, int y) {

       ImageIcon ii = new ImageIcon(shotImg);
       setImage(ii.getImage());
       
       this.setVisible(true);
       
       setX(x + H_SPACE);
       setY(y + V_SPACE);
   }
}

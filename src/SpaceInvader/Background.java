package SpaceInvader;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;

import SpaceInvader.Sprites.Sprite;
import SpaceInvader.Systems.Commons;
import SpaceInvader.Systems.ImagePaths;
 

public class Background extends Sprite implements Commons {
	private int StartLocation = BOARD_HEIGHT;
	private String bgImg1 = ImagePaths.getBackgroundPath();
    private String bgImg2 = ImagePaths.getBackgroundPath();
    
    
    public Background(int startLoc) {
    	initBackgrounds();
    	height = BOARD_HEIGHT;
    }
    
   public void act() {
	               // Two copies of the background image to scroll
   }
   
   public void initBackgrounds() {
	   ImageIcon ii = new ImageIcon(bgImg1);
	   setImage(ii.getImage());
	   setY(StartLocation);
   }
   public void reset() {
	   Background b1 = new Background(BOARD_HEIGHT);
   }
 
}

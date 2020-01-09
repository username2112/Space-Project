package SpaceInvader;

import java.awt.Canvas;


import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import SpaceInvader.Sprites.Sprite;
import SpaceInvader.Systems.Commons;
import SpaceInvader.Systems.ImagePaths;
 
public class Background extends Sprite implements Commons {
	private int y;
	private String bgImg1 = ImagePaths.getBackgroundPath();
    private String bgImg2 = ImagePaths.getBackgroundPath();
  
    public Background() {
    	initBackgrounds();
       
    }
 
   
    public void act() {
		y += 5;
        if (y == getHeight())
            y = 0;

    }
    
   public void initBackgrounds() { // Two copies of the background image to scroll
       Background b1 = new Background(); //dont know where to set this yet
       Background b2 = new Background();
    }
 
}
package SpaceInvader;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;

import javax.swing.ImageIcon;

import SpaceInvader.Sprites.Sprite;
import SpaceInvader.Systems.Commons;
import SpaceInvader.Systems.ImagePaths;
 

public class Background extends Sprite implements Commons {
	
	private String bgImg = ImagePaths.getBackgroundPath();
    public static ImageIcon ii;
    public static int speed = 2;
	public static Background back1;
	public static Background back2;
    
	public Background() {
    	initBackground();
	}
	
	public Background(int yPos) {
		setY(yPos);
	}
    
   public void act() {
	   Background.back1.setY(Background.back1.getY() + speed);
		Background.back2.setY(Background.back2.getY() + speed);
		if(Background.back1.getY() >= BOARD_HEIGHT) {
			Background.back1.setY(-1*ii.getIconHeight());
		}
		if(Background.back2.getY() >= BOARD_HEIGHT) {
			Background.back2.setY(-1*ii.getIconHeight());
		}
   }
	public void initBackground() {
		ii = new ImageIcon(bgImg);
		setImage(ii.getImage());
		back1 = new Background(0);
		back2 = new Background(0-ii.getIconHeight());
	}
}

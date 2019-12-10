package SpaceInvader;

import java.awt.Image;

public class Sprite {

    private boolean visible;
    private Image image;
    protected int x;
    protected int y;
    protected boolean dying;
    protected int dx;
    protected int dy;
    protected int width;
    protected int height;
    
    public Sprite() {
        visible = true;
    }
    
    public Sprite(int H, int W) {
    	width = W;
    	height = H;
    }

    public void die() {
        visible = false;
    }

    public boolean isVisible() {
        return visible;
    }

    protected void setVisible(boolean visible) {
        this.visible = visible;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public Image getImage() {
        return image;
    }

    public void setDying(boolean dying) {
        this.dying = dying;
    }
    public boolean isDying() {
        return this.dying;
    }
    
    //getters and setters\\
    
    public int getWidth() {
    	return width;
    }
    public int getHeight() {
    	return height;
    }
    
    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getY() {
        return y;
    }

    public int getX() {
        return x;
    }
    
    //hit detection\\
    
    public int rightX() {
    	return x + this.width;
    }
    
    public int leftX() {
    	return x;
    }
    
    public int topY() {
    	return y;
    }
    
    public int bottomY() {
    	return y - height;
    }
    
    public boolean isTouching(Sprite target) {
    	if(this.rightX() >= target.leftX() && this.rightX() <= target.rightX() && 	//top right
    			this.topY() >= target.bottomY() && this.topY() <= target.topY() ||
    			
    	   this.leftX() >= target.leftX() && this.leftX() <= target.rightX() &&		//top left
    			this.topY() >= target.bottomY() && this.topY() <= target.topY() ||
    			
    	   this.rightX() >= target.leftX() && this.rightX() <= target.rightX() && 	//bottom right
    			this.bottomY() >= target.bottomY() && this.bottomY() <= target.topY() ||
    			
    	   this.leftX() >= target.leftX() && this.leftX() <= target.rightX() &&		//bottom left
    			this.bottomY() >= target.bottomY() && this.bottomY() <= target.topY())
    	{
    		return true;
    	}
    	return false;
    }
    public boolean inSpriteRange(Sprite target, int range) {
       	range = range / 2;   
    	if(this.rightX() + range >= target.leftX() && this.rightX() + range <= target.rightX() && 	//top right
       			this.topY() - range >= target.bottomY() && this.topY() - range <= target.topY() ||
       			
       	   this.leftX() - range >= target.leftX() && this.leftX() - range <= target.rightX() &&		//top left
       			this.topY() - range >= target.bottomY() && this.topY() - range <= target.topY() ||
       			
       	   this.rightX() + range >= target.leftX() && this.rightX() + range <= target.rightX() && 	//bottom right
       			this.bottomY() + range >= target.bottomY() && this.bottomY() + range <= target.topY() ||
       			
       	   this.leftX() - range >= target.leftX() && this.leftX() - range <= target.rightX() &&		//bottom left
       			this.bottomY() + range >= target.bottomY() && this.bottomY() + range <= target.topY())
       	{
       		return true;
       	}
       	return false;
       }
}
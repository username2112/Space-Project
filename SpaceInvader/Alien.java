package SpaceInvader;

import javax.swing.ImageIcon;

public class Alien extends Sprite {
	
    private String direction = "up";
    private Bomb bomb;
    private int startX;
    private final String alienImg = "src/images/newalien.png";

    public Alien(int x, int y) {
        initAlien(x, y);
        startX = x;
    }

    private void initAlien(int x, int y) {

        this.x = x;
        this.y = y;

        bomb = new Bomb(x, y);
        ImageIcon ii = new ImageIcon(alienImg);
        setImage(ii.getImage());
    }

    public void act(int direction) {
        
        this.x += direction;
    }
    
    private void moveUp(int speed) {
    	if (x < Commons.BOARD_HEIGHT - 128) 
        	this.x += speed;
        else if(x >= Commons.BOARD_HEIGHT- 32)
        	direction = "down";
        	        	
    }
    
    private void moveDown(int speed) {
    	if(x > 0)
    		this.x -= speed;
    	else if (x <= 0)
    		direction = "up";
    	
    	
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
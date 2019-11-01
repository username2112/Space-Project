package SpaceInvader;

import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;

public class Player extends Sprite implements Commons {

    private final int START_Y = 280;
    private final int START_X = 270;

    private final String playerImg = "src/images/player.png";
    private int width;
    private int height;

    public Player() {
        initPlayer();
    }

    private void initPlayer() {
        
        ImageIcon ii = new ImageIcon(playerImg);

        width = ii.getImage().getWidth(null);
        height = ii.getImage().getHeight(null);
        
        setImage(ii.getImage());
        setX(START_X);
        setY(START_Y);
    }

    public void act() {
        
        x += dx;
        y += dy;
        
        //left
        if (x <= 2) {
            x = 2;
        }
        //top
        if (y <= 2) {
            y = 2;
        }
        //right
        if (x >= BOARD_WIDTH - 2 * width) {
            x = BOARD_WIDTH - 2 * width;
        }
        //bottom
        if (y >= BOARD_HEIGHT - 2 * height) {
            y = BOARD_HEIGHT - 2 * height;
        }
    }

    public void keyPressed(KeyEvent e) {
      
        int key = e.getKeyCode();
        
        if (key == KeyEvent.VK_LEFT) {
            dx = -2;
        }
        if (key == KeyEvent.VK_RIGHT) {
            dx = 2;
        }
        if (key == KeyEvent.VK_UP) {   
            dy = -2;
        }
        if (key == KeyEvent.VK_DOWN) {
            dy = 2;
        }
    }

    
    public void keyReleased(KeyEvent e) {
        
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_LEFT) {
            dx = 0;
        }
        if (key == KeyEvent.VK_RIGHT) {
            dx = 0;
        }
        if (key == KeyEvent.VK_UP) {   
            dy = 0;
        }
        if (key == KeyEvent.VK_DOWN) {
            dy = 0;
        }
    }
}

package SpaceInvader;

import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;

public class Player extends Sprite implements Commons {

    private final int START_Y = BOARD_HEIGHT - 200;
    private final int START_X = BOARD_WIDTH / 2 - 32;

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
        
        if (x <= 2) {
            x = 2;
        }
        if (y <= Board.lowest_y) {
            y = Board.lowest_y;
        }
        
        if (x >= BOARD_WIDTH - 2 * width) {
            x = BOARD_WIDTH - 2 * width;
        }
        if (y >= BOARD_HEIGHT - 2 * height) {
            y = BOARD_HEIGHT - 2 * height;
        }
    }

    public void keyPressed(KeyEvent e) {
        
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_A) {
        
            dx = -2;
        }

        if (key == KeyEvent.VK_D) {
        
            dx = 2;
        }
        if (key == KeyEvent.VK_W) {
            
            dy = -2;
        }

        if (key == KeyEvent.VK_S) {
        
            dy = 2;
        }
    }

    
    public void keyReleased(KeyEvent e) {
        
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_A) {
        
            dx = 0;
        }

        if (key == KeyEvent.VK_D) {
        
            dx = 0;
        }
        if (key == KeyEvent.VK_W) {
            
            dy = 0;
        }

        if (key == KeyEvent.VK_S) {
        
            dy = 0;
        }
    }
}
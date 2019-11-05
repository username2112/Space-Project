package SpaceInvader;

 
//imports
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

//visual imports
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class Board extends JPanel implements Runnable, Commons {
 
    private Dimension d;

    //TODO OBJECT/LIST DECLARATIONS
    private ArrayList<Alien> aliens;
    private ArrayList<Boss> bosss;
    
    private Player player;
    private Shot shot;
    
    private Boss boss;
    private BShot bshot;
    private int Blives = 3;
    
    //INITIAL VALUES
    private final int BOSS_INIT_X = 150;
    private final int BOSS_INIT_Y = 60;
    
    private final int ALIEN_INIT_X = 150;
    private final int ALIEN_INIT_Y = 5;
    
    private int direction = -1;
    
    //CORE VARIABLES
    public static int lowest_y = 0;
    private int deaths = 0;
    private int aliencount = 0;
    private int bosscount = 1;
    private boolean ingame = true; //MAIN LOOP VAR
    private int level = 1; //LEVELS COMPLETED + 1
    private int delay = 10;
    //FAIL
    private final String explImg = "src/images/explosion.png";
    private String message = "Game Over";
    
    //ANIMATOR
    private Thread animator;
    
    //CONSTRUCTOR
    public Board() {
        initBoard();
    }
    
    //---------\\
    //TODO INIT\\
    //---------\\
    
    private void initBoard() {
        addKeyListener(new TAdapter());
        setFocusable(true);
        d = new Dimension(BOARD_WIDTH, BOARD_HEIGHT);
        setBackground(Color.black);
        gameInit();
        setDoubleBuffered(true);
        GAME_SOUND.Background();
    }
    
    public void gameInit() {      
        
    	//array list declarations
        bosss = new ArrayList<>();
        aliens = new ArrayList<>();
        //number of aliens doubles / increment aliencount
        //alien spawning
        for (int i = 0; i <= 1 * level; i++) {
            aliencount++;
            for (int j = 0; j <= 1 * level; j++) {
            	//alien spacing
            	Alien alien = new Alien(ALIEN_INIT_X + (ALIEN_WIDTH + 4) * j, ALIEN_INIT_Y + 18 * i);
                lowest_y = alien.getY() + 16;
                aliens.add(alien);
           
                
            }
        }
        if(level > 1)
        {
               aliencount *= aliencount;
              
        }
        //define player, shot, Bshot, and the boss
        player = new Player();
        shot = new Shot();
        bshot = new BShot();
        boss = new Boss(BOSS_INIT_X, BOSS_INIT_Y);
        
        //idk why but boss doesn't have this by default
        boss.setVisible(false);
        
        //animator loop
        if (animator == null || !ingame) {
            animator = new Thread(this);
            animator.start();
        }
    }
    
    @Override
    public void addNotify() {
        super.addNotify();
        gameInit();
    }

    //-----------------\\
    //TODO DRAW METHODS\\
    //-----------------\\
    
    public void drawBoss(Graphics g) {
        if (boss.isVisible()) {
            g.drawImage(boss.getImage(), boss.getX(), boss.getY(), this);
        }
        if (boss.isDying()) {
        	bosscount = 0;
        	boss.setVisible(false);
        	boss.die();
        }
    }
 	
    public void drawAliens(Graphics g) {
        Iterator it = aliens.iterator();
        for (Alien alien: aliens) {
            if (alien.isVisible()) {
                g.drawImage(alien.getImage(), alien.getX(), alien.getY(), this);
            }
            if (alien.isDying()) {
                alien.die();
            }
        }
    }

    public void drawPlayer(Graphics g) {
        if (player.isVisible()) {
            g.drawImage(player.getImage(), player.getX(), player.getY(), this);

        }
        if (player.isDying()) {
            player.die();
            ingame = false;
        }
    }
 
    public void drawShot(Graphics g) {
        if (shot.isVisible()) {
            g.drawImage(shot.getImage(), shot.getX(), shot.getY(), this);
        }
    }
    
    public void drawBShot(Graphics g) {
        if (bshot.isVisible()) {
            g.drawImage(bshot.getImage(), bshot.getX(), bshot.getY(), this);
        }
    } 

    public void drawBombing(Graphics g) {
        for (Alien a : aliens) {
            Alien.Bomb b = a.getBomb();
            if (!b.isDestroyed()) {
                g.drawImage(b.getImage(), b.getX(), b.getY(), this);
            }
        }
    }

    //-----------------\\
    //TODO CORE SYSTEMS\\
    //-----------------\\
    
    @Override

    public void paintComponent(Graphics g) {

        super.paintComponent(g);
        //draw line on screen
        g.setColor(Color.black);
        g.fillRect(0, 0, d.width, d.height);
        g.setColor(Color.green);

        if (ingame) {
            g.drawLine(0, GROUND, BOARD_WIDTH, GROUND);
            
            //TODO DRAW
            drawAliens(g);
            drawPlayer(g);
            drawShot(g);
            drawBShot(g);
            drawBombing(g);
            if(level == 3) {
          	  	lowest_y = boss.getY() + 105;
                bosss.add(boss);
            	drawBoss(g);
            	if(bosscount == 1) {
                    boss.setVisible(true);
            	}
            }
            //display score and level in bottom left
            Font small = new Font("ZapfDingbats", Font.BOLD, 20);
            FontMetrics metr = this.getFontMetrics(small);
            g.setColor(Color.white);
            g.setFont(small);
            g.drawString("score: " + deaths + "00", metr.stringWidth(message) - 100,
                    GROUND + 50);
            g.drawString("level: " + level, metr.stringWidth(message) - 100,
                    GROUND + 30);
            g.drawString("Aliens Remaining: " + aliencount, metr.stringWidth(message) - 100,
                    GROUND + 70);
           
           
        }
        Toolkit.getDefaultToolkit().sync();
        g.dispose();
    }

    public void gameOver() {
    	//RUNS ON FAIL
        Graphics g = this.getGraphics();
        g.setColor(Color.black);
        g.fillRect(0, 0, BOARD_WIDTH, BOARD_HEIGHT);
        g.setColor(new Color(0, 32, 48));
        g.fillRect(50, BOARD_WIDTH / 2 - 30, BOARD_WIDTH - 100, 50);
        g.setColor(Color.white);
        g.drawRect(50, BOARD_WIDTH / 2 - 30, BOARD_WIDTH - 100, 50);
        
        //game fail text display
        Font small = new Font("Helvetica", Font.BOLD, 14);
        FontMetrics metr = this.getFontMetrics(small);
 
        //set text color
        g.setColor(Color.white);
        g.setFont(small);
        g.drawString(message, (BOARD_WIDTH - metr.stringWidth(message)) / 2,
                BOARD_WIDTH / 2);
    }

    public void checkIfLevelComplete()
    {
    	//TODO LEVEL CONDITIONS
    	if(level != 3)
        {
    		if(aliencount == 0) {
	    		level++;
	    		if(level < 2147483647)
	            {
	    			JOptionPane.showMessageDialog(null, "Level " + (level - 1) + " Completed");
	            }
	    		if(level == 3) {
	    			JOptionPane.showMessageDialog(null, "One more wave...");
	    		}
	            gameInit();
    		}
        } else if(level == 3) 
        	if(aliencount == 0 && bosscount <= 0) {
        		level++;
        		//ERR not working
        		if(level >= 4) {
        			delay -= 2;
        		}
        		if(level < 2147483647)
        		{
        			JOptionPane.showMessageDialog(null, "Boss defeated");
        		}
        		
       		gameInit();
        	}
    	
    }

    public void animationCycle() {
        //if kills = num aliens to destroy
    	if (deaths == NUMBER_OF_ALIENS_TO_DESTROY) {
            ingame = false;
            message = "Game won!";
        }
        // player
        player.act();

        //boss
        boss.act();
        
        //boss shot
        if(boss.isVisible()) {
        	int bombX = bshot.getX();
        	int bombY = bshot.getY();
        	
        	int playerX = player.getX();
        	int playerY = player.getY();
        			
      	//boss hit detection w/ player
        	if (player.isVisible() && bshot.isVisible()) {
                if (bombX >= (playerX)
                        && bombX <= (playerX + PLAYER_WIDTH)
                        && bombY >= (playerY)
                        && bombY <= (playerY + PLAYER_HEIGHT)) {
                	
                    player.setDying(true);
                    bshot.setDying(true);
                	
                }
        	}   
       }
        
        //shot
        if (shot.isVisible()) {
            int shotX = shot.getX();
            int shotY = shot.getY();

            for (Alien alien: aliens) {
            	//alien hit detection
                int alienX = alien.getX();
                int alienY = alien.getY();

                if (alien.isVisible() && shot.isVisible()) {
                	
                    if (shotX >= (alienX)
                            && shotX <= (alienX + ALIEN_WIDTH)
                            && shotY >= (alienY) &&  shotY <= (alienY + ALIEN_HEIGHT)) {

                        ImageIcon ii = new ImageIcon(explImg);
                        alien.setImage(ii.getImage());
                        alien.setDying(true);
                        deaths++;
                        shot.die();
                        aliencount--;
                    }
              }
            if(shotY >= GROUND) {
            	shot.die();
            }
              if(boss.isVisible()) {
            	  //boss hit detection
                	if (boss.isVisible() && shot.isVisible()) {
                        if (shotX >= (boss.getX())
                                && shotX <= (boss.getX() + BOSS_WIDTH)
                                && shotY >= (boss.getY()) &&  shotY <= (boss.getY() + BOSS_HEIGHT)) {

                        	Blives--;
                            if(Blives <= 0) {
                                boss.setDying(true);
                            }
                            shot.die();
                        }
                	}   
                }
            
            }
            
            //TODO BULLET SPEED / SHOT SPEED
            int y = shot.getY();
            y -= 12;
            //shot border 
            if (y < 0) {
                shot.die();
            } else {
                shot.setY(y);
            }
        }
        
        if(bshot.isVisible()) {
	        //BOSS BULLET SPEED / BOSS SHOT SPEED
	        int by = bshot.getY();
	        by += 3;
	        //shot border
	        if (by > BOARD_HEIGHT) {
	            bshot.die();
	        } else {
	            bshot.setY(by);
	        }
        }
        // aliens
        for (Alien alien: aliens) {
            int x = alien.getX();
            //set movement limits
            if (x >= BOARD_WIDTH - BORDER_RIGHT && direction != -1) {
                direction = -1;
                Iterator i1 = aliens.iterator();
                while (i1.hasNext()) {
                    Alien a2 = (Alien) i1.next();
                    a2.setY(a2.getY());
                }
            }
            if (x <= BORDER_LEFT && direction != 1) {
                direction = 1;
                Iterator i2 = aliens.iterator();
                while (i2.hasNext()) {
                    Alien a = (Alien) i2.next();
                    a.setY(a.getY());
                }
            }
        }
 

        Iterator it = aliens.iterator();
        //TODO LOSE CONDITION (ALIENS TOUCH GREEN LINE)
        while (it.hasNext()) {
            Alien alien = (Alien) it.next();
            if (alien.isVisible()) {
                int y = alien.getY();
                if (y > GROUND - ALIEN_HEIGHT) {
                    ingame = false;
                    message = "Invasion!";
                }
                alien.act(direction);
            }
        }

        // bombs
        Random generator = new Random();
        
        if(boss.shoot == true && boss.isVisible()) {
        	boss.shoot = false;
            bshot = new BShot(boss.x, boss.y);
        }
        
        //will alien bomb?
        for (Alien alien: aliens) {
        	//creates bomb
            int shot = generator.nextInt(15);
            Alien.Bomb b = alien.getBomb();
            //teleport bomb
            if (shot == CHANCE && alien.isVisible() && b.isDestroyed()) {
                b.setDestroyed(false);
                b.setX(alien.getX());
                b.setY(alien.getY());
            }

            int bombX = b.getX();
            int bombY = b.getY();

            int playerX = player.getX();
            int playerY = player.getY();

            //player hit
            if (player.isVisible() && !b.isDestroyed()) { 

            	//bomb hit detection
                if (bombX >= (playerX)
                        && bombX <= (playerX + PLAYER_WIDTH)
                        && bombY >= (playerY)
                        && bombY <= (playerY + PLAYER_HEIGHT)) {

                	//player dying
                    ImageIcon ii = new ImageIcon(explImg);
                    player.setImage(ii.getImage());
                    player.setDying(true);
                    b.setDestroyed(true);

                }

            }
            //bomb movement
            if (!b.isDestroyed()) {
                b.setY(b.getY() + 1);                 
                if (b.getY() >= GROUND - BOMB_HEIGHT) {
                    b.setDestroyed(true);
                }
            }
        }
    }

    @Override

    public void run() {

        long beforeTime, timeDiff, sleep;
        beforeTime = System.currentTimeMillis();

 

        while (ingame) {
        	//looping methods
            checkIfLevelComplete();
            //visuals
            repaint();
            animationCycle();

            //time
            timeDiff = System.currentTimeMillis() - beforeTime;
            sleep = delay - timeDiff;

            if (sleep < 0) {
                sleep = 2;
            }
            try {
                Thread.sleep(sleep);

            } catch (InterruptedException e) {
                System.out.println("interrupted");

            }

            beforeTime = System.currentTimeMillis();

        }
        
        //runs if exiting ingame
        gameOver();

    }

    private class TAdapter extends KeyAdapter {

        @Override

        public void keyReleased(KeyEvent e) {
            player.keyReleased(e);
        }
        @Override

        public void keyPressed(KeyEvent e) {

            player.keyPressed(e);

            int x = player.getX();
            int y = player.getY();

            int key = e.getKeyCode();
            
            if (key == KeyEvent.VK_SPACE) {
                if (ingame) {
                    if (!shot.isVisible()) {
                        shot = new Shot(x, y);
                        GAME_SOUND.shot();
                    }
                }
            }
        }//end key pressed
    }//end TAdapter
}//class closure



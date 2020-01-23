package SpaceInvader;

//imports
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.Scanner;

//visual imports
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import SpaceInvader.Sprites.Enemies.Alien;
import SpaceInvader.Sprites.Enemies.Asteroid;
import SpaceInvader.Sprites.Enemies.Boss;
import SpaceInvader.Sprites.Menu.Button;
import SpaceInvader.Sprites.Menu.Title;
import SpaceInvader.Sprites.Player.Player;
import SpaceInvader.Sprites.Shots.BShot;
import SpaceInvader.Sprites.Shots.BombShot;
import SpaceInvader.Sprites.Shots.Shot;
import SpaceInvader.Systems.Commons;
import SpaceInvader.Systems.GameSounds;
import SpaceInvader.Systems.ImagePaths;
import SpaceInvader.Background;

public class GameBoard extends JPanel implements Runnable, Commons {

	private static final long serialVersionUID = -8479829684389979540L;

	private Dimension d;

	// TODO OBJECT/LIST DECLARATIONS
	private ArrayList<Alien> aliens;
	private ArrayList<Boss> bosss;
	private ArrayList<Asteroid> asteroids;

	private Player player;
	private Shot shot;
	private Asteroid asteroid;
	private final int Plives_Init = 3;
	private int Plives = 3;

	private Boss boss;
	private BShot bshot;
	private BombShot bombc;
	private final int Blives_Init = 5;
	public static int Blives = 5;
	


	private Background background1;
	private Button bGame;
	private Button highScores;
	private Button ammo;
	private Button health;
	private Title title;
	private Button menu;
	// INITIAL VALUES
	private final int BOSS_INIT_X = 269;
	private final int BOSS_INIT_Y = 30;

	private final int ALIEN_INIT_X = 150;
	private final int ALIEN_INIT_Y = 15;

	private int direction = -1;
	private int Bspeed = 12;

	// CORE VARIABLES
	public static int lowest_y = 0;
	private int deaths = 0;
	private int aliencount = 2;
	private int bosscount = 1;
	private boolean ingame = true; // MAIN LOOP VAR
	private boolean inpause = true;
	private boolean inhs = false;
	private boolean isRunning = false; 
	private int level; // LEVELS COMPLETED + 1
	private int pausedI = 1;
	private boolean paused = false;
	private int DELAY = Commons.DELAY;
	private int exk = 0;
	public static Button restart;
	public static int bombAmmo;
	private boolean pauseDrawn;
	private int shopI;
	private boolean shopDrawn;
	private boolean shopping;
	public Background background;
	// TIME / FPS
	private static long ST = System.currentTimeMillis(); // time at start of run
	private static long time; // current time
	private static long oldTime; // time last checked
	private static long FPS;
	private static long DFPS;
	
	// FAIL
	private final String explImg = "src\\images\\explosion.png";
	private String message = "GAME OVER";

	// ANIMATOR
	private Thread animator;
	private Thread animator2;

	// CONSTRUCTOR
	public GameBoard() {
		initTS();
	}

	// ---------\\
	// TODO INIT\\
	// ---------\\

	private void initTS() {
		addKeyListener(new TAdapter());
		setFocusable(true);
		d = new Dimension(BOARD_WIDTH, BOARD_HEIGHT);
		setBackground(Color.black);
		bInit();
		setDoubleBuffered(true);
		//GameSounds.background();
		level = 1;
		bombAmmo = 5;
		Plives = Plives_Init;
	}

	public void bInit() {

		inhs = false;

		// array list declarations
		bGame = new Button(201, 200, ImagePaths.getButtonPath());
		highScores = new Button(201, 300, ImagePaths.getButtonPath());
		title = new Title(73, 40);
		this.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				bGame.checkMouse(e.getPoint(), bGame);
				highScores.checkMouse(e.getPoint(), highScores);
				
				if(shopping) {
					health.checkMouse(e.getPoint(), health);
					ammo.checkMouse(e.getPoint(), ammo);
					
					//System.out.println(health.isPressed);	
					if (health.isPressed) {
						Plives = 3;
						//System.out.println("c");
					}
					
					if (ammo.isPressed) {
						bombAmmo = 5;
					}
				}
				
				if (bGame.isPressed && (isRunning == false && inhs == false)) {
					gameInit();

				}
				if (highScores.isPressed && (isRunning == false && inhs == false)) {

					try {
						hsInit();
					} catch (FileNotFoundException e1) {
						e1.printStackTrace();
					}

				}

			}
		});
		if (animator2 == null || !ingame) {
			animator2 = new Thread(this);
			animator2.start();
		}
	}

	private int[] hss = new int[3];

	public void hsInit() throws FileNotFoundException {
		inhs = true;

		int num = 0;
		Scanner input = new Scanner(new File("src\\High Scores.txt"));
		while (input.hasNextLine() && num < 3) {
			int temp = input.nextInt();

			hss[num] = temp;
			num++;
		}
		input.close();
		menu = new Button(201, 500, ImagePaths.getButtonPath());
		
		//TODO main menu
		
		this.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				menu.checkMouse(e.getPoint(), menu);
				if (menu.isPressed && (isRunning == false)) {
					bInit();

				}

			}
		});

	}

	public void gameInit() {
		if(level % 3 == 0)
			bosscount = 1;
		animator2.stop();
		isRunning = true;
		inpause = false;
		// array list declarations
		bosss = new ArrayList<>();
		aliens = new ArrayList<>();
		asteroids = new ArrayList<>();
		// number of aliens doubles / increment aliencount
		// alien spawning
		exk = 0;
		for (int i = 0; i <= 1 * level; i++) {
			aliencount++;
			for (int j = 0; j <= 1 * level; j++) {
				Alien alien = new Alien(ALIEN_INIT_X + (ALIEN_WIDTH + 4) * j, ALIEN_INIT_Y + 30 * i);
				lowest_y = alien.getY() + 16;
				aliens.add(alien);

			}
		}
		
		//asteroids
		for (int i = 0; i <= 1 * (level / 2); i++) {
			for (int j = 0; j <= 1 * (level / 2); j++) {
				int randomx = (int) (Math.random() * BOARD_WIDTH);
				int randomy = (int) (Math.random() * 50 + 10);
				if (randomx >= 630) {
					randomx -= 20;
				} else if (randomx <= 30) {
					randomx += 20;
				}
				Asteroid a = new Asteroid(randomx, (GROUND / 2) + randomy);
				asteroids.add(a);

			}
		}
		if (level > 1) {
			aliencount *= aliencount;

		}

		// define player, shot, Bshot, and the boss
		player = new Player();
		shot = new Shot();
		bshot = new BShot();
		bombc = new BombShot();
		background = new Background();
		boss = new Boss(BOSS_INIT_X, BOSS_INIT_Y);

		// idk why but boss doesn't have this by default
		boss.setVisible(false);

		// animator loop
		if (animator == null || !ingame) {
			animator = new Thread(this);
			animator.start();
		}

	}

	@Override
	public void addNotify() {
		super.addNotify();
		// gameInit();
	}

	// -----------------\\
	// TODO DRAW METHODS\\
	// -----------------\\

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
		Iterator<Alien> it = aliens.iterator();
		for (Alien alien : aliens) {
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

	public void drawAsteroid(Graphics g) {
		Iterator<Asteroid> it = asteroids.iterator();
		for (Asteroid asteroid : asteroids) {
			if (asteroid.isVisible()) {
				g.drawImage(asteroid.getImage(), asteroid.getX(), asteroid.getY(), this);
			}
			if (asteroid.isDying()) {
				asteroid.die();
			}
		}
	}
	
	public void drawBombC(Graphics g) {	
 		if (bombc.isVisible()) {	
			g.drawImage(bombc.getImage(), bombc.getX(), bombc.getY(), this);	
		}	
		if (bombc.isDying()) {
			bombc.setVisible(false);
			bombc.die();
			exk = 0;
		}
	}
	
	public void drawBombAmmo(Graphics g) {	
		if (bombAmmo > -1) {
			g.setColor(Color.black);
			g.fillRect(BOARD_WIDTH / 2 - ((5 * 30) / 2) - 3, GROUND + 37, 30 * 5 + 6, 16);
			g.setColor(Color.yellow);
			g.fillRect(BOARD_WIDTH / 2 - ((5 * 30) / 2), GROUND + 40, bombAmmo * 30, 10);
			g.setColor(Color.black);
			g.fillRect(BOARD_WIDTH / 2 - ((5 * 30) / 2) + 28, GROUND + 37, 5, 16);
			g.fillRect(BOARD_WIDTH / 2 - ((5 * 30) / 2) + 58, GROUND + 37, 5, 16);
			g.fillRect(BOARD_WIDTH / 2 - ((5 * 30) / 2) + 88, GROUND + 37, 5, 16);
			g.fillRect(BOARD_WIDTH / 2 - ((5 * 30) / 2) + 118, GROUND + 37, 5, 16);
		}
		
	}
	
	public void drawBackgrounds(int startlocation) {
		
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

	public void drawTitle(Graphics g) {
		if (title.isVisible()) {
			g.drawImage(title.getImage(), title.getX(), title.getY(), this);
		}

	}
	
	public void drawBGame(Graphics g) {
		if (bGame.isVisible()) {
			g.drawImage(bGame.getImage(), bGame.getX(), bGame.getY(), this);
			
			Font small = new Font("Helvetica", Font.BOLD, 20);
			FontMetrics metr = this.getFontMetrics(small);
			metr = this.getFontMetrics(small);
			g.setColor(Color.BLACK);
			g.setFont(small);
			g.drawString("BEGIN GAME", (bGame.getX() + (bGame.getWidth() / 2) - (metr.stringWidth("BEGIN GAME") / 2)), (bGame.getY() + bGame.getHeight() - metr.getHeight()));
		}

	}
	
	public void drawRestart(Graphics g) {
		if (restart.isVisible()) {
			g.drawImage(restart.getImage(), restart.getX(), restart.getY(), this);
			
			Font small = new Font("Helvetica", Font.BOLD, 20);
			FontMetrics metr = this.getFontMetrics(small);
			metr = this.getFontMetrics(small);
			g.setColor(Color.BLACK);
			g.setFont(small);
			g.drawString("RESTART", (restart.getX() + (restart.getWidth() / 2) - (metr.stringWidth("RESTART") / 2)), (restart.getY() + restart.getHeight() - metr.getHeight()));
		}

	}

	public void drawHS(Graphics g) {
		if (highScores.isVisible()) {
			g.drawImage(highScores.getImage(), highScores.getX(), highScores.getY(), this);
			Font small = new Font("Helvetica", Font.BOLD, 20);
			FontMetrics metr = this.getFontMetrics(small);
			metr = this.getFontMetrics(small);
			g.setColor(Color.BLACK);
			g.setFont(small);
			g.drawString("HIGH SCORES", (highScores.getX() + (highScores.getWidth() / 2) - (metr.stringWidth("HIGH SCORES") / 2)), (highScores.getY() + highScores.getHeight() - metr.getHeight()));
		
		}

	}
	
	public void drawBackground(Graphics g) { 
			/*
			j = 0;
			for(int i = <array>.length; i >= 0; i--) {
				temparray[j] = array[i];
				j++;
			}
			return temparray[];
			*/
			g.drawImage(background.getImage(), Background.back1.getX(), Background.back1.getY(), this);
			g.drawImage(background.getImage(), Background.back2.getX(), Background.back2.getY(), this);
	}
	
	public void drawAmmoBox(Graphics g) {
		if (ammo.isVisible()) {
			g.drawImage(ammo.getImage(), ammo.getX(), ammo.getY(), this);
		}
	}
	
	public void drawHealthBox(Graphics g) {
		if (health.isVisible()) {
			g.drawImage(health.getImage(), health.getX(), health.getY(), this);
		}
	}
	
	public void drawMenu(Graphics g) {
		if (menu.isVisible()) {
			g.drawImage(menu.getImage(), menu.getX(), menu.getY(), this);
			Font small = new Font("Helvetica", Font.BOLD, 20);
			FontMetrics metr = this.getFontMetrics(small);
			metr = this.getFontMetrics(small);
			g.setColor(Color.BLACK);
			g.setFont(small);
			g.drawString("MAIN MENU", (menu.getX() + (menu.getWidth() / 2) - (metr.stringWidth("MAIN MENU") / 2)), (menu.getY() + menu.getHeight() - metr.getHeight()));
		
		}

	}
	
	public void drawPause(Graphics g) {
		//ok i know how bad this is but it works so it's fine
		if(!pauseDrawn) {
			g.setColor(Color.white);
			g.fillRect(BOARD_WIDTH / 2 - 128, BOARD_HEIGHT / 2 - 178, 256, 356);
			g.setColor(Color.black);
			g.fillRect(BOARD_WIDTH / 2 - 125, BOARD_HEIGHT / 2 - 175, 250, 350);
			
			Font big = new Font("Helvetica", Font.BOLD, 50);
			Font small = new Font("Helvetica", Font.BOLD, 20);
			
			FontMetrics metr = this.getFontMetrics(big);
			metr = this.getFontMetrics(big);
			g.setColor(Color.white);
			g.setFont(big);
			g.drawString("PAUSED", BOARD_WIDTH / 2 - (metr.stringWidth("PAUSED") / 2),  BOARD_HEIGHT / 2- metr.getHeight() - 20);
	
			metr = this.getFontMetrics(small);
			g.setColor(Color.white);
			g.setFont(small);
			g.drawString("1: Restart", BOARD_WIDTH / 2 - (metr.stringWidth("1: Restart") / 2),  BOARD_HEIGHT / 2 - (metr.getHeight() * -1) - 50);				
			g.drawString("2: Texture Pack Input", BOARD_WIDTH / 2 - (metr.stringWidth("2: Texture Pack Input") / 2),  BOARD_HEIGHT / 2 - (metr.getHeight() * -2) - 50);				
			g.drawString("3: Shop", BOARD_WIDTH / 2 - (metr.stringWidth("3: Shop") / 2),  BOARD_HEIGHT / 2 - (metr.getHeight() * -3) - 50);	
			g.drawString("4: Exit Game", BOARD_WIDTH / 2 - (metr.stringWidth("4: Exit Game") / 2),  BOARD_HEIGHT / 2 - (metr.getHeight() * -4) - 50);	
			pauseDrawn = true;
		}
	}
	
	public void drawShop(Graphics g) {
		//ok i know how bad this is but it works so it's fine
		if(!shopDrawn) {
			g.setColor(Color.black);
			g.fillRect(0,0, BOARD_WIDTH, BOARD_HEIGHT);
			
			Font big = new Font("Helvetica", Font.BOLD, 50);
			
			FontMetrics metr = this.getFontMetrics(big);
			metr = this.getFontMetrics(big);
			g.setColor(Color.white);
			g.setFont(big);
			g.drawString("SHOP", BOARD_WIDTH / 2 - (metr.stringWidth("SHOP") / 2),  metr.getHeight() + 50);
			
			//draw buttons here
			//TODO SOMETHING
			health = new Button(BOARD_WIDTH / 2 - 64, BOARD_HEIGHT / 2 - 150, ImagePaths.getHealthPath());
			ammo = new Button(BOARD_WIDTH / 2 - 64, BOARD_HEIGHT / 2, ImagePaths.getAmmoPath());
			drawAmmoBox(g);
			drawHealthBox(g);
			
			shopDrawn = true;
		}
	}


	// -----------------\\
	// TODO CORE SYSTEMS\\
	// -----------------\\

	@Override

	public void paintComponent(Graphics g) {

		// visuals + some boss code

		super.paintComponent(g);
		// draw line on screen
		g.setColor(Color.black);
		g.fillRect(0, 0, d.width, d.height);
		g.setColor(Color.green);
		if (inhs) {
			// TODO
			Font small = new Font("ZapfDingbats", Font.BOLD, 20);
			FontMetrics metr = this.getFontMetrics(small);
			g.setColor(Color.white);
			g.setFont(small);
			g.drawString("High Scores: ", 50, 80);
			for (int i = 0; i < hss.length; i++) {
				g.drawString("#" + (i + 1 + ": " + hss[i]), 50, 120 + (i * 40));
			}
			drawMenu(g);
		} else if (inpause) {
			drawBGame(g);
			drawTitle(g);
			drawHS(g);
			Font small = new Font("ZapfDingbats", Font.BOLD, 20);
			FontMetrics metr = this.getFontMetrics(small);
			g.setColor(Color.white);
			g.drawString("CONTROLS:", (bGame.getX() + (bGame.getWidth() / 2) - (metr.stringWidth("CONTROLS:") / 2)), BOARD_HEIGHT / 2 + 100);
			g.drawString("W / A / S / D - MOVE", (bGame.getX() + (bGame.getWidth() / 2) - (metr.stringWidth("W / A / S / D - MOVE") / 2)), BOARD_HEIGHT / 2 + 150);
			g.drawString("Q / E / SPACE - SHOOT", (bGame.getX() + (bGame.getWidth() / 2) - (metr.stringWidth("Q / E / SPACE - SHOOT") / 2)), BOARD_HEIGHT / 2 + 175);
			g.drawString("B - BOMB SHOT", (bGame.getX() + (bGame.getWidth() / 2) - (metr.stringWidth("B - BOMB SHOT") / 2)), BOARD_HEIGHT / 2 + 200);
		
		} else if (ingame) {
			// manage fps
			time = getTime();
			FPS = 1000 / getRunTime();
			long DFPS = 60;
			if (FPS >= 66) {
				DELAY += 3;
			} else if (FPS < 60) {
				DELAY -= 2;
			} else {
				DFPS = FPS;
			}
			
			drawBackground(g);
			
			g.drawLine(0, GROUND, BOARD_WIDTH, GROUND);

			// TODO DRAW
			drawAliens(g);
			drawAsteroid(g);
			drawPlayer(g);
			drawShot(g);
			drawBShot(g);
			drawBombC(g);
			drawBombing(g);
			drawBombAmmo(g);
			//drawBackgrounds(650);
			
			if (level % 3 ==0) {
				g.setColor(Color.gray);
				g.fillRect(BOARD_WIDTH / 2 - ((Blives_Init * 100) / 2) - 3, 3, Blives_Init * 100 + 6, 16);
				g.setColor(Color.red);

				// health bar
				bosss.add(boss);
				drawBoss(g);
				if (bosscount == 1) {
					boss.setVisible(true);
				}
				g.fillRect(BOARD_WIDTH / 2 - (Blives * 100 / 2), 6, Blives * 100, 10);
			}
			if (player.isVisible()) {
				g.setColor(Color.gray);
				g.fillRect(BOARD_WIDTH / 2 - ((Plives_Init * 100) / 2) - 3, GROUND + 77, Plives_Init * 100 + 6, 16);
				g.setColor(Color.green);
				g.fillRect(BOARD_WIDTH / 2 - (Plives * 100 / 2), GROUND + 80, Plives * 100, 10);

			}
			
			// display score and level in bottom left
			Font small = new Font("ZapfDingbats", Font.BOLD, 20);
			FontMetrics metr = this.getFontMetrics(small);
			g.setColor(Color.white);
			g.setFont(small);

			g.drawString("Time: " + (getTime() / 1000), 20, GROUND + 90);
			g.drawString("Aliens Remaining: " + aliencount, metr.stringWidth(message) - 100, GROUND + 70);
			g.drawString("Score: " + deaths + "00", metr.stringWidth(message) - 100, GROUND + 50);
			g.drawString("Level: " + level, metr.stringWidth(message) - 100, GROUND + 30);
			g.drawString("FPS: " + FPS, BOARD_WIDTH - metr.stringWidth(message), GROUND + 30);
		}

		Toolkit.getDefaultToolkit().sync();
		g.dispose();
	}

	public void gameOver() throws FileNotFoundException {
		// RUNS ON FAIL
		Graphics g = this.getGraphics();
		g.setColor(Color.black);
		g.fillRect(0, 0, BOARD_WIDTH, BOARD_HEIGHT);
		g.setColor(new Color(0, 32, 48));
		g.fillRect(50, BOARD_WIDTH / 2 - 30, BOARD_WIDTH - 100, 50);
		g.setColor(Color.white);
		g.drawRect(50, BOARD_WIDTH / 2 - 30, BOARD_WIDTH - 100, 50);

		// scanner for high score
		File t = new File("src\\High Scores.txt");
		Scanner sc = new Scanner(t);

		// game fail text display
		Font small = new Font("Helvetica", Font.BOLD, 14);
		FontMetrics metr = this.getFontMetrics(small);

		// set text color and font
		g.setColor(Color.white);
		g.setFont(small);
		g.drawString(message, (BOARD_WIDTH - metr.stringWidth(message)) / 2, BOARD_WIDTH / 2);
		// set score font
		Font ssmall = new Font("Helvetica", Font.BOLD, 14);
		FontMetrics mmetr = this.getFontMetrics(small);

		int sci = sc.nextInt();
		int sci1 = sc.nextInt();
		int sci2 = sc.nextInt();

		PrintStream pr = new PrintStream("src\\High Scores.txt");

		if (sci2 >= level) {
			// no new high score
			g.setColor(Color.white);
			g.setFont(ssmall);
			g.drawString("HIGH SCORE: " + sci, ((BOARD_WIDTH - mmetr.stringWidth("HIGH SCORE: " + sci)) / 2) - 5,
					(BOARD_WIDTH / 2) + 40);
			g.drawString("2nd: " + sci1, ((BOARD_WIDTH - mmetr.stringWidth("2nd: " + sci1)) / 2) - 5,
					(BOARD_WIDTH / 2) + 60);
			g.drawString("3rd: " + sci2, ((BOARD_WIDTH - mmetr.stringWidth("3rd: " + sci2)) / 2) - 5,
					(BOARD_WIDTH / 2) + 80);
			
			pr.println(sci);
			pr.println(sci1);
			pr.println(sci2);
			
			g.drawString("PRESS R TO RESTART", (BOARD_WIDTH / 2) - mmetr.stringWidth("PRESS R TO RESTART") / 2, mmetr.getHeight() + 10);
			g.drawString("PRESS ESC TO EXIT", (BOARD_WIDTH / 2) - mmetr.stringWidth("PRESS ESC TO EXIT") / 2, mmetr.getHeight() + 30);			
		} else if (sci < level) {
			// new high score

			pr.println(level);
			pr.println(sci);
			pr.println(sci1);

			g.setColor(Color.white);
			g.setFont(ssmall);
			g.drawString("NEW HIGH SCORE: " + level,
					((BOARD_WIDTH - mmetr.stringWidth("NEW HIGH SCORE: " + level)) / 2) - 5, (BOARD_WIDTH / 2) + 13);
			// SET NEW 2ND TO CURRENT FIRST
			g.drawString("PRESS R TO RESTART", (BOARD_WIDTH / 2) - mmetr.stringWidth("PRESS R TO RESTART") / 2, mmetr.getHeight() + 10);
			g.drawString("PRESS ESC TO EXIT", (BOARD_WIDTH / 2) - mmetr.stringWidth("PRESS ESC TO EXIT") / 2, mmetr.getHeight() + 30);		
		} else if (sci1 < level) {
			// new high score
			pr.println(sci);
			pr.println(level);
			pr.println(sci1);

			g.setColor(Color.white);
			g.setFont(ssmall);
			g.drawString("NEW 2ND PLACE: " + level,
					((BOARD_WIDTH - mmetr.stringWidth("NEW 2ND PLACE: " + level)) / 2) - 5, (BOARD_WIDTH / 2) + 13);
			g.drawString("PRESS R TO RESTART", (BOARD_WIDTH / 2) - mmetr.stringWidth("PRESS R TO RESTART") / 2, mmetr.getHeight() + 10);
			g.drawString("PRESS ESC TO EXIT", (BOARD_WIDTH / 2) - mmetr.stringWidth("PRESS ESC TO EXIT") / 2, mmetr.getHeight() + 30);		

		} else if (sci2 < level) {
			// new high score
			pr.println(sci);
			pr.println(sci1);
			pr.println(level);

			g.setColor(Color.white);
			g.setFont(ssmall);
			g.drawString("NEW 3RD PLACE: " + level,
					((BOARD_WIDTH - mmetr.stringWidth("NEW 3RD PLACE: " + level)) / 2) - 5, (BOARD_WIDTH / 2) + 13);
			g.drawString("PRESS R TO RESTART", (BOARD_WIDTH / 2) - mmetr.stringWidth("PRESS R TO RESTART") / 2, mmetr.getHeight() + 10);
			g.drawString("PRESS ESC TO EXIT", (BOARD_WIDTH / 2) - mmetr.stringWidth("PRESS ESC TO EXIT") / 2, mmetr.getHeight() + 30);		
		}
	}

	public void checkIfLevelComplete() {
		// TODO LEVEL CONDITIONS
		if (level != 3 && inpause == false) {
			if (aliencount <= 0) {
				level++;
				bombAmmo = 5;
				if (level < 2147483647) {
					JOptionPane.showMessageDialog(null, "Level " + (level - 1) + " Completed");
				}
				gameInit();
			}
		} else if (level % 3 == 0)
			if (aliencount == 0 && bosscount <= 0) {
				level++;
				bombAmmo = 5;
				if (level < 2147483647) {
					JOptionPane.showMessageDialog(null, "Boss defeated");
				}

				gameInit();
			}

	}

	public void animationCycle() {
		if (isRunning == true && !paused) {
			// if kills = num aliens to destroy
			if (deaths == NUMBER_OF_ALIENS_TO_DESTROY) {
				ingame = false;
				message = "Game won!";
			}
			// player
			player.act();

			// boss
			boss.act();
			
			background.act();
			
			// bomb consumable
			if (bombc.isVisible()) {	
				int bombcX = bombc.getX();	
				int bombcY = bombc.getY();	

 				for (Alien alien : aliens) {	
					// alien hit detection for new shot	
 					if (alien.isVisible() && bombc.isVisible()) {	

 						if (bombc.isTouching(alien) || alien.isTouching(bombc)) {	
 							exk++;
 							bombc.explode();
							if(exk >= 1) {	
								exk = 0;
								bombc.setDying(true);	
 						   	}	
							aliencount--;	
							alien.setDying(true);
							alien.die();
 						}	
					}	

 					if (bombcY >= GROUND - 10) {	
						bombc.setDying(true);	
					}
					if (boss.isVisible()) {	
						// boss hit detection for new shot	
						if (boss.isVisible() && bombc.isVisible()) {	
							if (bombcX >= (boss.getX()) && bombcX <= (boss.getX() + BOSS_WIDTH)	
									&& bombcY >= (boss.getY()) && bombcY <= (boss.getY() + BOSS_HEIGHT)) {	

 								Blives -= 2;	
								if (Blives <= 0) {	
									boss.setDying(true);	
								}	
								bombc.die();	
							}	
						}	
					}	

 				}
	 			for(Asteroid asteroid : asteroids) {
	 				if(asteroid.isVisible()) {
						int asteroidX = asteroid.getX();
						int asteroidY = asteroid.getY();
						if (bombcX >= (asteroidX) && bombcX <= (asteroidX + ALIEN_WIDTH) && bombcY >= (asteroidY)
								&& bombcY <= (asteroidY + ALIEN_HEIGHT)) {
							bombc.explode();
							bombc.setDying(true);
							asteroid.die();
							deaths++;
							shot.die();
						}
	 				}
 				}
 				
 				if(!(exk >= 1)) {
					int y = bombc.getY();	
					Bspeed = 8;	
					y -= Bspeed;	
					// shot border	
					if (y < 0) {	
						bombc.die();	
					} else {	
						bombc.setY(y);	
					}
 				}
			}
			
			// boss shot
			if (boss.isVisible()) {
				int bombX = bshot.getX();
				int bombY = bshot.getY();

				// boss hit detection w/ player
				if (player.isVisible() && bshot.isVisible()) {
					if (player.isTouching(bshot) || bshot.isTouching(player)) {

						player.setDying(true);
						bshot.setDying(true);

					}
				}
				for(Asteroid asteroid : asteroids) {
					if(asteroid.isVisible()) {
						int asteroidX = asteroid.getX();
						int asteroidY = asteroid.getY();
						if (bombX >= (asteroidX) && bombX <= (asteroidX + ALIEN_WIDTH) && bombY >= (asteroidY)
								&& bombY <= (asteroidY + ALIEN_HEIGHT)) {
							
							asteroid.setDying(true);
							bshot.die();
						}
					}
				}
			}

			// shot
			if (shot.isVisible()) {
				int shotX = shot.getX();
				int shotY = shot.getY();
				for (Asteroid asteroid : asteroids) {
					if (asteroid.isVisible() && shot.isVisible()) {
						if (shot.isTouching(asteroid)) {
							shot.die();
						}
					}
				}

				for (Alien alien : aliens) {
					// alien hit detection
					int alienX = alien.getX();
					int alienY = alien.getY();

					if (alien.isVisible() && shot.isVisible()) {

						if (shotX >= (alienX) && shotX <= (alienX + ALIEN_WIDTH) && shotY >= (alienY)
								&& shotY <= (alienY + ALIEN_HEIGHT)) {

							ImageIcon ii = new ImageIcon(explImg);
							alien.setImage(ii.getImage());
							alien.setDying(true);
							deaths++;
							shot.die();
							shot.resetBD();
							aliencount--;
						}
					}
					if (shotY >= GROUND - 10) {
						shot.die();
						shot.resetBD();
					}
					if (boss.isVisible()) {
						// boss hit detection
						if (boss.isVisible() && shot.isVisible()) {
							if (shotX >= (boss.getX()) && shotX <= (boss.getX() + BOSS_WIDTH) && shotY >= (boss.getY())
									&& shotY <= (boss.getY() + BOSS_HEIGHT)) {

								Blives--;
								if (Blives <= 0) {
									boss.setDying(true);
								}
								shot.die();
								shot.resetBD();
							}
						}
					}

				}
				// TODO BULLET SPEED / SHOT SPEED
				int y = shot.getY();
				Bspeed = 8;
				y -= Bspeed;
				// shot border
				if (y < 0) {
					shot.die();
					shot.resetBD();
				} else if (Shot.getType() == 0) {
					shot.setY(y);
				} else if (Shot.getType() == 1) {
					shot.ricL();
				} else if (Shot.getType() == 2) {
					shot.ricR();
				}
			}
			// player alien collision / player boss collision
			if (player.isVisible()) {
				int playerX = player.getX();
				int playerY = player.getY();
				for (Alien alien : aliens) {
					// alien hit detection
					int alienX = alien.getX();
					int alienY = alien.getY();

					if (alien.isVisible()) {
						if (player.isTouching(alien)) {
							player.setDying(true);
						}
					}
				}
				if (boss.isVisible())
					if (playerX >= (boss.getX()) && playerX <= (boss.getX() + BOSS_WIDTH) && playerY >= (boss.getY())
							&& playerY <= (boss.getY() + BOSS_HEIGHT)) {

						player.setDying(true);

					}
				
				for (Asteroid asteroid : asteroids) {
						if(player.isTouching(asteroid) && asteroid.isVisible()) {
							asteroid.die();
							Plives--;
							if(Plives <= 0) {
								player.setDying(true);
							}
						}
				}
			}

			if (bshot.isVisible()) {
				// BOSS BULLET SPEED / BOSS SHOT SPEED
				int by = bshot.getY();
				by += 3;
				// shot border
				if (by > BOARD_HEIGHT) {
					bshot.die();
				} else {
					bshot.setY(by);
				}
			}
			// aliens
			for (Alien alien : aliens) {
				int x = alien.getX();
				// irregular movement
				if (x % 28 == 0) {
					if (alien.getY() < GROUND - 40) {
						// alien.setY(alien.getY()+ 10);
						alien.setY(alien.getY() + 10);
					} else if (alien.getY() >= GROUND - 40)// BOARD_HEIGHT/2
					{
						while (alien.getY() > ALIEN_INIT_Y + 16) {
							alien.setY(16);
						}
						if (level == 3) {
							while (alien.getY() > ALIEN_INIT_Y + 40) {
								alien.setY(40);
							}
						}

					}

				}
				if (x >= BOARD_WIDTH - BORDER_RIGHT && direction != -1) {
					direction = -1;
					Iterator<Alien> i1 = aliens.iterator();
					while (i1.hasNext()) {
						Alien a2 = i1.next();
						a2.setY(a2.getY());
					}
				}
				if (x <= BORDER_LEFT && direction != 1) {
					direction = 1;
					Iterator<Alien> i2 = aliens.iterator();
					while (i2.hasNext()) {
						Alien a = i2.next();
						a.setY(a.getY());
					}
				}
			}

			Iterator<Alien> it = aliens.iterator();
			// TODO LOSE CONDITION (ALIENS TOUCH GREEN LINE)
			while (it.hasNext()) {
				Alien alien = it.next();
				if (alien.isVisible()) {
					int y = alien.getY();
					alien.act(direction);
				}
			}

			// bombs
			Random generator = new Random();

			// boss shooting
			if(boss.isVisible() && !bshot.isVisible()) {
				if (player.getX() >= boss.getX() || player.getX() <= boss.getX() + 128) {
					bshot = new BShot(boss.getX(), boss.getY());
				}
			}

			// will alien bomb?
			for (Alien alien : aliens) {
				// creates bomb
				int shot = generator.nextInt(15);
				Alien.Bomb b = alien.getBomb();
				// teleport bomb
				if (shot == CHANCE && alien.isVisible() && b.isDestroyed()) {
					b.setDestroyed(false);
					b.setX(alien.getX());
					b.setY(alien.getY());
				}

				int bombX = b.getX();
				int bombY = b.getY();

				int playerX = player.getX();
				int playerY = player.getY();

				// player hit
				if (player.isVisible() && !b.isDestroyed()) {

					// bomb hit detection
					if (bombX >= (playerX) && bombX <= (playerX + PLAYER_WIDTH) && bombY >= (playerY)
							&& bombY <= (playerY + PLAYER_HEIGHT)) {

						// player dying
						Plives--;
						if (Plives <= 0) {
							ImageIcon ii = new ImageIcon(explImg);
							player.setImage(ii.getImage());
							player.setDying(true);
						}
						b.setDestroyed(true);

					}

				}
				for(Asteroid asteroid : asteroids) {
					int asteroidX = asteroid.getX();
					int asteroidY = asteroid.getY();
					if(asteroid.isVisible() && !b.isDestroyed()) {
						if (bombX >= (asteroidX) && bombX <= (asteroidX + asteroid.getHeight()) && bombY >= (asteroidY)
								&& bombY <= (asteroidY + asteroid.getHeight())) {
							b.setDestroyed(true);
						}
					}
				}
				// bomb movement
				if (!b.isDestroyed()) {
					b.setY(b.getY() + 1);
					if (b.getY() >= GROUND - BOMB_HEIGHT) {
						b.setDestroyed(true);
					}
				}
			
			}
		} else {
			// this is for transitions from the title screen to hs menu to avoid a crap ton
			// of nullpointerexceptions
		}

	}

	@Override

	public void run() {

		long beforeTime, timeDiff, sleep;
		beforeTime = System.currentTimeMillis();
		Graphics g = this.getGraphics();
		
		
		Thread pause = new Thread(() -> {
			try {
				if(shopping) {
					drawShop(g);
				} else {
					drawPause(g);
				}
				Thread.sleep(10);
			} catch (InterruptedException e) {
				System.err.println("pause interrupted");
			}

		});

		while (ingame) {
			if (!paused) {
				// looping methods
				checkIfLevelComplete();
				// visuals
				animationCycle();
				repaint();
				pauseDrawn = false;
				shopDrawn = false;
			}
			
			// timE
			sleep = DELAY;
			try {
				if (paused) {
					pause.run();
				} else {
					Thread.sleep(sleep);
					pause.stop();
				}

			} catch (InterruptedException e) {
				System.err.println("main interrupted");
			}
		}

		// runs if exiting ingame
		try {
			gameOver();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static long getTime() {
		long time2 = System.currentTimeMillis() - ST;
		return time2;
	}

	public static long getRunTime() {
		if (oldTime == 0) {
			oldTime = ST;
		}
		long tt = time - oldTime;
		oldTime = time;
		return tt;
	}

	public static long getFPS() {
		FPS = 1000 / getRunTime();
		return FPS;
	}
	
	// -----------------\\
	// TODO KEY LISTENER\\
	// -----------------\\

	private class TAdapter extends KeyAdapter {

		@Override

		public void keyReleased(KeyEvent e) {
			try {
				player.keyReleased(e);
			} catch(Exception ea) {
				//doesn't give us a bunch of errors when we press a key in the menu
			}
		}

		@Override
		public void keyPressed(KeyEvent e) {
			try {
				player.keyPressed(e);
	
				int x = player.getX();
				int y = player.getY();
	
				int key = e.getKeyCode();
				
				if (key == KeyEvent.VK_SPACE) {
					if (ingame) {
						if (!shot.isVisible() && !bombc.isVisible()) {
							shot = new Shot(x, y, 0);
							if(y < GROUND) {
								GameSounds.shot();
							}
						}
					}
				}
				if (key == KeyEvent.VK_Q || key == KeyEvent.VK_V || key == KeyEvent.VK_PAGE_UP) {
					if (ingame) {
						if (!shot.isVisible() && !bombc.isVisible()) {
							shot = new Shot(x, y, 1);
							if(y < GROUND) {
								GameSounds.shot();
							}
						}
					}
				}
				if (key == KeyEvent.VK_E || key == KeyEvent.VK_N || key == KeyEvent.VK_PAGE_DOWN) {
					if (ingame) {
						if (!shot.isVisible() && !bombc.isVisible()) {
							shot = new Shot(x, y, 2);
							if(y < GROUND) {
								GameSounds.shot();
							}
						}
					}
				}
				if (key == KeyEvent.VK_B)	
					if (ingame) {	
							if (!bombc.isVisible() && !shot.isVisible() && bombAmmo > 0) {
								bombAmmo--;
								bombc = new BombShot(x, y);	
								if(y < GROUND) {
									GameSounds.shot();
								}
							}	
				} 	
	
				if (key == KeyEvent.VK_ESCAPE) {
					pausedI++;
					if (pausedI % 2 == 0) {
						paused = true;
					} else {
						paused = false;
						shopping = false;
					}
					if(!player.isVisible()) {
						SpaceProject.spaceProject.dispose();
					}
				}
				if (key == KeyEvent.VK_R) {
					if(!player.isVisible()) {
						SpaceProject.spaceProject.dispose();
						SpaceProject.spaceProject = new SpaceProject();
						SpaceProject.spaceProject.setVisible(true);
					}
				}

				
				if(paused) {
					//restart
					if (key == KeyEvent.VK_1) {
						SpaceProject.spaceProject.dispose();
						SpaceProject.spaceProject = new SpaceProject();
						SpaceProject.spaceProject.setVisible(true);
					}
					//texture packs
					if (key == KeyEvent.VK_2) {
						JFrame f=new JFrame("Recpurce Packs"); 
						//submit button
						JButton b=new JButton("Submit");    
						b.setBounds(10,60,230, 30); 
						
						//enter name label
						JLabel label = new JLabel();		
						label.setText("Pack Name :");
						label.setBounds(10, 10, 100, 50);
						
						//empty label which will show event after button clicked
						JLabel label1 = new JLabel();
						label1.setBounds(10, 5, 200, 50);
						
						//textfield to enter name
						JTextField textfield= new JTextField();
						textfield.setBounds(110, 25, 130, 30);
						
						//add to frame
						f.add(label1);
						f.add(textfield);
						f.add(label);
						f.add(b);    
						f.setSize(270, 150);   
						f.setLocationRelativeTo(null);
						f.setLayout(null);    
						f.setVisible(true);    
						f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);   
						
						//action listener
						b.addActionListener(new ActionListener() {
					        
							@Override
							public void actionPerformed(ActionEvent arg0) {
									label1.setText("Name has been submitted.");		
									ImagePaths.setImagePath("src\\images\\"+ textfield.getText() +"\\");
									f.dispose();
							}
			
					      });
					}
					//exit game
					if (key == KeyEvent.VK_3) {
						shopI++;
						if (shopI % 2 == 0) {
							shopping = true;
						} else {
							shopping = false;
						}
						if(!player.isVisible()) {
							SpaceProject.spaceProject.dispose();
						}
					}
					
					if (key == KeyEvent.VK_4) {
						SpaceProject.spaceProject.dispose();
					}
				}
			} catch(Exception ea) {
				//doesn't give us a bunch of errors when we press a key in the menu
			}//end of error catch
		}// end key pressed
	}// end TAdapter
}// class closure

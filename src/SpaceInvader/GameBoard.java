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
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class GameBoard extends JPanel implements Runnable, Commons {

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
	private final int Blives_Init = 5;
	private int Blives = 5;

	private Button bGame;
	private Button highScores;
	private Button resourcePacks;
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
	private int level = 1; // LEVELS COMPLETED + 1
	private int pausedI = 1;
	private boolean paused = false;
	private int DELAY = Commons.DELAY;

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
		GameSounds.background();

	}

	public void bInit() {

		inhs = false;
		// array list declarations
		bGame = new Button(201, 200, ImagePaths.getButton0Path());
		highScores = new Button(201, 300, ImagePaths.getButton1Path());
		title = new Title(73, 40);
		this.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				bGame.checkMouse(e.getPoint(), bGame);
				highScores.checkMouse(e.getPoint(), highScores);
				if (bGame.isPressed && (isRunning == false && inhs == false)) {
					gameInit();

				}
				if (highScores.isPressed && (isRunning == false && inhs == false)) {

					try {
						hsInit();
					} catch (FileNotFoundException e1) {
						// TODO Auto-generated catch block
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

		// TODO add interpretation of the txt file
		int num = 0;
		Scanner input = new Scanner(new File("src\\High Scores.txt"));
		while (input.hasNextLine() && num < 3) {
			int temp = input.nextInt();

			hss[num] = temp;
			num++;
		}
		input.close();
		menu = new Button(201, 500, ImagePaths.getButton2Path());
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
		animator2.stop();
		isRunning = true;
		inpause = false;
		// array list declarations
		bosss = new ArrayList<>();
		aliens = new ArrayList<>();
		asteroids = new ArrayList<>();
		// number of aliens doubles / increment aliencount
		// alien spawning

		for (int i = 0; i <= 1 * level; i++) {
			aliencount++;
			for (int j = 0; j <= 1 * level; j++) {
				// alien spacing
				int randomx = (int) (Math.random() * BOARD_WIDTH);
				int randomy = (int) (Math.random() * 50 + 10);
				if (randomx >= 630) {
					randomx -= 20;
				} else if (randomx <= 30) {
					randomx += 20;
				}
				System.out.println(randomy);
				Alien alien = new Alien(ALIEN_INIT_X + (ALIEN_WIDTH + 4) * j, ALIEN_INIT_Y + 18 * i);
				lowest_y = alien.getY() + 16;
				Asteroid a = new Asteroid(randomx, (GROUND / 2) + randomy);
				aliens.add(alien);
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
		Iterator it = aliens.iterator();
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
		Iterator it = asteroids.iterator();
		for (Asteroid asteroid : asteroids) {
			if (asteroid.isVisible()) {
				g.drawImage(asteroid.getImage(), asteroid.getX(), asteroid.getY(), this);
			}
			if (asteroid.isDying()) {
				asteroid.die();
			}
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

	public void drawBGame(Graphics g) {
		if (bGame.isVisible()) {
			g.drawImage(bGame.getImage(), bGame.getX(), bGame.getY(), this);
		}

	}

	public void drawTitle(Graphics g) {
		if (title.isVisible()) {
			g.drawImage(title.getImage(), title.getX(), title.getY(), this);
		}

	}

	public void drawHS(Graphics g) {
		if (highScores.isVisible()) {
			g.drawImage(highScores.getImage(), highScores.getX(), highScores.getY(), this);
		}

	}

	public void drawMenu(Graphics g) {
		if (menu.isVisible()) {
			g.drawImage(menu.getImage(), menu.getX(), menu.getY(), this);
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
				// System.out.println(FPS);
				DFPS = FPS;
			}
			g.drawLine(0, GROUND, BOARD_WIDTH, GROUND);

			// TODO DRAW
			drawAliens(g);
			drawAsteroid(g);
			drawPlayer(g);
			drawShot(g);
			drawBShot(g);
			drawBombing(g);
			if (level == 3) {
				g.setColor(Color.gray);
				g.fillRect(BOARD_WIDTH / 2 - ((Blives_Init * 100) / 2) - 3, 3, Blives_Init * 100 + 6, 16);
				g.setColor(Color.red);

				// health bar
				lowest_y = boss.getY() + 105;
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
			g.drawString("FPS: " + DFPS, BOARD_WIDTH - metr.stringWidth(message), GROUND + 30);
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
			// SET NEW 3RD TO CURRENT 2ND
		} else if (sci1 < level) {
			// new high score
			pr.println(sci);
			pr.println(level);
			pr.println(sci1);

			g.setColor(Color.white);
			g.setFont(ssmall);
			g.drawString("NEW 2ND PLACE: " + level,
					((BOARD_WIDTH - mmetr.stringWidth("NEW 2ND PLACE: " + level)) / 2) - 5, (BOARD_WIDTH / 2) + 13);
			// SET 3RD TO CURRENT 2ND

		} else if (sci2 < level) {
			// new high score
			pr.println(sci);
			pr.println(sci1);
			pr.println(level);

			g.setColor(Color.white);
			g.setFont(ssmall);
			g.drawString("NEW 3RD PLACE: " + level,
					((BOARD_WIDTH - mmetr.stringWidth("NEW 3RD PLACE: " + level)) / 2) - 5, (BOARD_WIDTH / 2) + 13);
		}
	}

	public void checkIfLevelComplete() {
		// TODO LEVEL CONDITIONS
		if (level != 3 && inpause == false) {
			if (aliencount == 0) {
				level++;
				if (level < 2147483647) {
					JOptionPane.showMessageDialog(null, "Level " + (level - 1) + " Completed");
				}
				gameInit();
			}
		} else if (level == 3)
			if (aliencount == 0 && bosscount <= 0) {
				level++;
				if (level < 2147483647) {
					JOptionPane.showMessageDialog(null, "Boss defeated");
				}

				gameInit();
			}

	}

	public void animationCycle() {
		if (isRunning == true) {
			// if kills = num aliens to destroy
			if (deaths == NUMBER_OF_ALIENS_TO_DESTROY) {
				ingame = false;
				message = "Game won!";
			}
			// player
			player.act();

			// boss
			boss.act();

			// boss shot
			if (boss.isVisible()) {
				int bombX = bshot.getX();
				int bombY = bshot.getY();

				int playerX = player.getX();
				int playerY = player.getY();

				// boss hit detection w/ player
				if (player.isVisible() && bshot.isVisible()) {
					if (bombX >= (playerX) && bombX <= (playerX + PLAYER_WIDTH) && bombY >= (playerY)
							&& bombY <= (playerY + PLAYER_HEIGHT)) {

						player.setDying(true);
						bshot.setDying(true);

					}
				}
			}

			// shot
			if (shot.isVisible()) {
				int shotX = shot.getX();
				int shotY = shot.getY();
				for (Asteroid asteroid : asteroids) {
					// alien hit detection
					int asteroidX = asteroid.getX();
					int asteroidY = asteroid.getY();

					if (asteroid.isVisible() && shot.isVisible()) {
						if (shotX >= (asteroidX) && shotX <= (asteroidX + ALIEN_WIDTH) && shotY >= (asteroidY)
								&& shotY <= (asteroidY + ALIEN_HEIGHT)) {

							ImageIcon ii = new ImageIcon(explImg);
							asteroid.setImage(ii.getImage());
							asteroid.setDying(true);
							deaths++;
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
						/*
						 * if (playerX >= (alienX) && playerX <= (alienX + ALIEN_WIDTH) && playerY >=
						 * (alienY) && playerY <= (alienY + ALIEN_HEIGHT)) {
						 */
						/*
						 * System.out.println("player X: " + player.leftX() + " " + player.rightX());
						 * System.out.println("Alien X: " + alien.leftX() + " " + alien.rightX());
						 * System.out.println("player Y: " + player.topY() + " " + player.bottomY());
						 * System.out.println("Alien Y: " + alien.topY() + " " + alien.bottomY());
						 */

						if (player.isTouching(alien)) {
							/*
							 * System.err.println( "\nplayer X: \t" + "left: " + player.leftX() +
							 * "\t right: " + player.rightX() +"\nAlien X: \t" + "left: " +alien.leftX() +
							 * "\t right: " + alien.rightX() +"\nplayer Y: \t"+ "top: " + player.topY() +
							 * " \t bottom: " + player.bottomY() +"\nAlien Y: \t" + "top: " + alien.topY() +
							 * "\t bottom: " + alien.bottomY() + "\n\n");
							 */
							player.setDying(true);
						}
						// }
					}
				}
				if (boss.isVisible())
					if (playerX >= (boss.getX()) && playerX <= (boss.getX() + BOSS_WIDTH) && playerY >= (boss.getY())
							&& playerY <= (boss.getY() + BOSS_HEIGHT)) {

						player.setDying(true);

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
					if (alien.getY() < GROUND - 20) {
						// alien.setY(alien.getY()+ 10);
						alien.setY(alien.getY() + 10);
					} else if (alien.getY() >= GROUND - 20)// BOARD_HEIGHT/2
					{
						while (alien.getY() > ALIEN_INIT_Y + 16) {
							alien.setY(16);
						}
						if (level == 3) {
							while (alien.getY() > ALIEN_INIT_Y + 40) {
								alien.setY(40);
								System.out.println(alien.getY());
							}
						}

					}

				}
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
			// TODO LOSE CONDITION (ALIENS TOUCH GREEN LINE)
			while (it.hasNext()) {
				Alien alien = (Alien) it.next();
				if (alien.isVisible()) {
					int y = alien.getY();
					/*
					 * if (y > GROUND - ALIEN_HEIGHT) { ingame = false; message = "Invasion!"; }
					 */
					alien.act(direction);
				}
			}

			// bombs
			Random generator = new Random();

			// boss shooting
			if (boss.shoot == true && boss.isVisible() && !bshot.isVisible()) {
				boss.shoot = false;
				// System.out.println("Boss(" + boss.x + ", " + boss.y +")" + "\t" + "bShot(" +
				// bshot.x + ", " + bshot.y +")");
				// System.err.println("Boss(" + boss.getX() + ", " + boss.getY() +")" + "\t" +
				// "bShot(" + bshot.x + ", " + bshot.y +")");
				System.out.println(boss.getWidth());
				bshot = new BShot(boss.getX(), boss.getY());
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

		Thread pause = new Thread(() -> {

			try {
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
				repaint();
				animationCycle();
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
						shot = new Shot(x, y, 0);
						GameSounds.shot();
					}
				}
			}
			if (key == KeyEvent.VK_Q || key == KeyEvent.VK_V || key == KeyEvent.VK_PAGE_UP) {
				if (ingame) {
					if (!shot.isVisible()) {
						shot = new Shot(x, y, 1);
						GameSounds.shot();
					}
				}
			}
			if (key == KeyEvent.VK_E || key == KeyEvent.VK_N || key == KeyEvent.VK_PAGE_DOWN) {
				if (ingame) {
					if (!shot.isVisible()) {
						shot = new Shot(x, y, 2);
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
				}
			}
		}// end key pressed
	}// end TAdapter
}// class closure

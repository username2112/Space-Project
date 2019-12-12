package SpaceInvader;

//imports
import java.awt.Color;
import java.awt.Dimension;
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
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.time.Clock;
import java.util.Scanner;

//visual imports
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class Board extends JPanel implements Runnable, Commons {

	private Dimension d;

	// TODO OBJECT/LIST DECLARATIONS
	private ArrayList<Alien> aliens;
	private ArrayList<Boss> bosss;

	private Player player;
	private Shot shot;
	private final int Plives_Init = 3;
	private int Plives = 3;

	private Boss boss;
	private BShot bshot;
	private final int Blives_Init = 5;
	private int Blives = -1;

	private Button bGame;
	private Button highScores;

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

	private boolean sh = true;
	private boolean cl = false;
	private static long ST = System.currentTimeMillis(); // time at start of run
	private static long time; // current time
	private static long oldTime; // time last checked
	private static long FPS;
	private static long DFPS;
	// FAIL
	private final String explImg = "images/explosion.png";
	private String message = "GAME OVER";

	// ANIMATOR
	private Thread animator;
	private Thread animator2;
	private Thread animator3;

	// buttons
	private HighScoreMenu hs;
	// CONSTRUCTOR
	public Board() {
		initTS();
		// setVisible(false);
	}

	// ---------\\
	// TODO INIT\\
	// ---------\\

	private void initTS() {
		inhs = false;
		
		addKeyListener(new TAdapter());

		setFocusable(true);

		d = new Dimension(BOARD_WIDTH, BOARD_HEIGHT);

		setBackground(Color.black);

		setDoubleBuffered(true);

		GAME_SOUND.Background();
		title = new Title(73, 40);
		bGame = new Button("Begin Game", 200, 200, 256, 64);
		highScores = new Button("High Scores", 200, 300, 256, 64);
		bGame.thisButton.setVisible(true);
		highScores.thisButton.setVisible(true);

		bGame.thisButton.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (bGame.isPressed()) {
					gameInit();
				}
			}

		});
		highScores.thisButton.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (highScores.pressed) {
					try {
						hsInit();
					} catch (FileNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}

		});
		menu = new Button("Back", 200, 400, 256, 64);
		menu.thisButton.setVisible(false);
		menu.thisButton.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (menu.pressed) {
					initTS();
					hs.setVisible(false);
					highScores.thisButton.setVisible(false);
				}
			}

		});
		// setSize(d);
		setLayout(null);

	}

	private int[] hss = new int[3];

	public void hsInit() throws FileNotFoundException {
		bGame.thisButton.setVisible(false);
		highScores.thisButton.setVisible(false);
		menu.thisButton.setVisible(true);
		// SpaceProject.hs.setVisible(true);
		add(new HighScoreMenu(true));
		hs = new HighScoreMenu(true);
		hs.setVisible(true);
		// title.setVisible(false);
		inhs = true;
		addKeyListener(new TAdapter());

		setFocusable(true);

		d = new Dimension(BOARD_WIDTH, BOARD_HEIGHT);

		// HS.setBackground(Color.GRAY);

		setDoubleBuffered(true);
		System.out.println("hi");

	}

	public void gameInit() {
		bGame.thisButton.setVisible(false);
		highScores.thisButton.setVisible(false);

		// animator2.stop();
		isRunning = true;
		inpause = false;
		// array list declarations
		bosss = new ArrayList<>();
		aliens = new ArrayList<>();
		// number of aliens doubles / increment aliencount
		// alien spawning

		for (int i = 0; i <= 1 * level; i++) {
			aliencount++;
			for (int j = 0; j <= 1 * level; j++) {
				// alien spacing
				Alien alien = new Alien(ALIEN_INIT_X + (ALIEN_WIDTH + 4) * j, ALIEN_INIT_Y + 18 * i);
				lowest_y = alien.getY() + 16;
				aliens.add(alien);

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
			add(bGame.thisButton);
			add(highScores.thisButton);

		}
		add(menu.thisButton);
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
			drawTitle(g);
		} else if (inpause) {
			drawTitle(g);

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
			drawPlayer(g);
			drawShot(g);
			drawBShot(g);
			drawBombing(g);

			if (level % 3 == 0) {

				int bTHealth = (int) (Math.pow(2, (level / 3 - 1)) * Blives_Init);
				if (Blives == -1) {
					Blives = bTHealth;
				}
				g.setColor(Color.gray);
				// g.fillRect(BOARD_WIDTH / 2 - ((Blives_Init * 100) / 2) - 3, 3, Blives_Init *
				// 100 + 6, 16);
				int bSide = 600;
				int tkaw = 0;

				g.fillRect(BOARD_WIDTH / 2 - bSide / 2 - 6, 3, bSide + 6, 16);

				g.setColor(Color.red);

				// health bar
				lowest_y = boss.getY() + 105;
				bosss.add(boss);
				drawBoss(g);
				if (level > 2) {
					if (bosscount == 1 / (level / 3)) {
						boss.setVisible(true);

						tkaw = bSide / bTHealth;
					}
				}

				g.fillRect(BOARD_WIDTH / 2 - tkaw / 2 * Blives - 3, 6, tkaw * Blives - 3, 10);

				System.out.println(Blives);
				System.out.println(tkaw);
				System.out.println(bTHealth);
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

	public void gameOver() throws FileNotFoundException, InterruptedException {
		// RUNS ON FAIL
		Graphics g = this.getGraphics();
		g.setColor(Color.black);
		g.fillRect(0, 0, BOARD_WIDTH, BOARD_HEIGHT);
		g.setColor(new Color(0, 32, 48));
		g.fillRect(50, BOARD_WIDTH / 2 - 30, BOARD_WIDTH - 100, 50);
		g.setColor(Color.white);
		g.drawRect(50, BOARD_WIDTH / 2 - 30, BOARD_WIDTH - 100, 50);

		// scanner for high score
		File t = new File("High Scores.txt");
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

		PrintStream pr = new PrintStream("High Scores.txt");

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
		} else if (level % 3 == 0) {
			if (aliencount == 0 && bosscount <= 0) {
				level++;
				if (level < 2147483647) {
					JOptionPane.showMessageDialog(null, "Boss defeated");
				}

				gameInit();
			}
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
								if (Blives == 0) {
									Blives--;
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

						if (playerX >= (alienX) && playerX <= (alienX + ALIEN_WIDTH) && playerY >= (alienY)
								&& playerY <= (alienY + ALIEN_HEIGHT)) {

							player.setDying(true);
						}
					}
				}
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
				bshot = new BShot(boss.x, boss.y);
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
		} catch (InterruptedException e) {
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
			if (key == KeyEvent.VK_T) {
				if (ingame) {
					cl = true;
					System.out.println("skipping level");
					level++;
					gameInit();
				}
			}
			if (key == KeyEvent.VK_SPACE) {
				if (ingame) {
					if (!shot.isVisible()) {
						shot = new Shot(x, y, 0);
						GAME_SOUND.shot();
					}
				}
			}
			if (key == KeyEvent.VK_Q || key == KeyEvent.VK_V || key == KeyEvent.VK_PAGE_UP) {
				if (ingame) {
					if (!shot.isVisible()) {
						shot = new Shot(x, y, 1);
						GAME_SOUND.shot();
					}
				}
			}
			if (key == KeyEvent.VK_E || key == KeyEvent.VK_N || key == KeyEvent.VK_PAGE_DOWN) {
				if (ingame) {
					if (!shot.isVisible()) {
						shot = new Shot(x, y, 2);
						GAME_SOUND.shot();
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

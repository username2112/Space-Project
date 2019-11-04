import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Point;
import java.io.Serializable;
import java.util.HashSet;

public class Enemy extends GameObject implements Ship, Serializable {
	private static final long serialVersionUID = 6037236323540109415L;

	private transient Image img;
	private int movement = 2;
	
	public static HashSet<Laser> laserList = new HashSet<Laser>();
	
	public Enemy(String imageLocation, Point position, Dimension size) {
		super(imageLocation, position, size);
		GraphicsConfiguration gc = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
		
		try {
			img = GameCanvas.getImage(imageLocation, gc);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public boolean switchDirection() {
		if ((getPosition().x + getSize().width) > SpaceInvaders.WIDTH || (getPosition().x <= 0)) {
			SpaceInvaders.setDirection(SpaceInvaders.getDirection() * -1);
			return true;
		}
		return false;
	}

	@Override
	public void move(int x, int y) {
		getPosition().x += (SpaceInvaders.getDirection()) * movement;
		getPosition().y += y;
		getRectangle().setRect(getPosition().x, getPosition().y, getSize().width, getSize().height);
	}

	@Override
	public void fire() {
		Point pos = new Point(getPosition());
		pos.x = pos.x + getSize().width/2;
		
		Laser l = new Laser(pos, Color.GREEN);
		l.setPosition(new Point(pos.x, pos.y + 40));
		l.setySpeed(-5);
		
		GameCanvas.addGameObject(l);
		laserList.add(l);
	}
	
	@Override
	public void draw(Graphics g) {
		Point position = getPosition();
		Dimension size = getSize();
		
		g.drawImage(img, position.x, position.y, size.width, size.height, null);		
	}
	
	public void increaseSpeed() {
		movement++;
	}
}
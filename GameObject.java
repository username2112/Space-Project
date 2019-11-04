import java.awt.*;
import java.io.Serializable;
import java.awt.geom.Rectangle2D;

public abstract class GameObject implements Serializable {
	private static final long serialVersionUID = 6037236323540109415L;
	
	private String imageLocation;
	private Point position;
	private Dimension size;
	private Rectangle2D rectangle;
	private boolean isActive;

	public GameObject(String imageLocation, Point position, Dimension size) {
		this.size = size;
		this.imageLocation = imageLocation;
		this.position = position;
		rectangle = new Rectangle(position.x, position.y, size.width, size.height);
		isActive = true;
	}
	
	//public GameObject(String imageLocation2, Point position2, Dimension size2) {
		// TODO Auto-generated constructor stub
	

	public abstract void move(int x, int y);
	public abstract void draw(Graphics g);
	
	public String getImageLocation() {
		return imageLocation;
	}
	public void setImageLocation(String imageLocation) {
		this.imageLocation = imageLocation;
	}
	public Point getPosition() {
		return position;
	}
	public void setPosition(Point position) {
		this.position = position;
	}
	public Dimension getSize() {
		return size;
	}
	public void setSize(Dimension size) {
		this.size = size;
	}
	public Rectangle2D getRectangle() {
		return rectangle;
	}
	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}
}
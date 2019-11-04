/*import javax.swing.JFrame;
public class SpaceInvaders extends JFrame {
	public SpaceInvaders() {
		add(new Board());
		setTitle("Board");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setVisible(true);
		setResizable(false);

	}

	public static void main(String[] args) {
		new SpaceInvaders();

 	}

}
*/
import java.awt.Color;
import java.awt.Container;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferStrategy;
import java.io.Serializable;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;

public class SpaceInvaders extends JFrame implements Serializable {
	private static final long serialVersionUID = 6037236323540109415L;
	public static final int WIDTH = 600;
	public static final int HEIGHT = 600;
	BufferStrategy strategy;
	Container c = getContentPane();
	static JTextArea names = new JTextArea(10, 50);
	static JList<String> userList;
	
	private static int direction = 1;
	
	private static final String TITLE = "Space Invaders";
	
	public SpaceInvaders() {
		super(TITLE);
		final JPanel startPanel = new JPanel();
		startPanel.setBackground(Color.BLACK);
		JButton start = new JButton("Start");
		JButton multiPlayer = new JButton("Multiplayer");
		JButton exit = new JButton("Exit");
		startPanel.add(start);
		startPanel.add(multiPlayer);
		startPanel.add(exit);
		this.add(startPanel);
		
		ImageIcon pic = new ImageIcon("Resources\\Enemy.jpg");
		Image img = pic.getImage();
		setVisible(true);
		setSize(WIDTH, HEIGHT);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setResizable(false);
		setIgnoreRepaint(true);
		createBufferStrategy(2);
		
		start.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				c.removeAll();
				JFrame frame = SpaceInvaders.this;
				c.add(GameCanvas.getGameCanvas(false));
				GameCanvas.getGameCanvas(false).grabFocus();
				frame.setContentPane(c);
				frame.repaint();
			}
		});
		
		multiPlayer.addActionListener(new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			c.removeAll();
			JFrame frame = SpaceInvaders.this;
			c.add(multiplayer());
			GameCanvas.getGameCanvas(true);
			multiplayer().grabFocus();
			frame.setContentPane(c);
			frame.repaint();
		}
		});
		
		exit.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		
		strategy = getBufferStrategy();
		strategy.show();
		
		setIconImage(img);
	}
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				new SpaceInvaders();
			}
		});
	}
	
	public static int getDirection() {
		return direction;
	}
	
	public static void setDirection(int direction) {
		SpaceInvaders.direction = direction;
	}

public JPanel multiplayer() {
	JPanel panel = new JPanel();
	names.setEditable(false);
	panel.setBackground(Color.BLACK);
	
	userList = new JList<String>();
    userList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    JScrollPane theList = new JScrollPane(userList);
	userList.setListData(GameCanvas.listVector);
    names.add(userList);
    names.add(theList);
	
	panel.add(names);
	
	this.add(panel);
	return panel;
}
}

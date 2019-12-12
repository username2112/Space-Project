package SpaceInvader;

import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;


public class Button {
	public boolean pressed = false;
	public JButton thisButton;
	public Button(String name, int x, int y, int w, int h) {
		thisButton = new JButton(name);
		thisButton.setBounds(x,y,w,h);
		thisButton.setFont(new Font("Bauhaus 93", Font.PLAIN, 40));   
		thisButton.setBackground(Color.GRAY);
		thisButton.setForeground(Color.BLACK);
		thisButton.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseClicked(MouseEvent arg0) { 
				
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
				// TODO Auto-generated method stub
				thisButton.setForeground(Color.DARK_GRAY);
				
				
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
				thisButton.setForeground(Color.BLACK);

			}

			@Override
			public void mousePressed(MouseEvent arg0) {
				// TODO Auto-generated method stub
				pressed = true;
				System.out.println("p");
			}

			@Override
			public void mouseReleased(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

		});
	}
	public boolean isPressed() {
		return pressed;
		
	}
}

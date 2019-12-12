package SpaceInvader;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import javax.swing.JPanel;

public class HighScoreMenu extends JPanel implements Runnable, Commons {
	public HighScoreMenu(boolean isvis) {
		setVisible(isvis);
		setBackground(Color.GRAY);
		setSize(BOARD_WIDTH, BOARD_HEIGHT);
		int num = 0;
		Scanner input = null;
		try {
			input = new Scanner(new File("High Scores.txt"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		while (input.hasNextLine() && num < 3) {
			int temp = input.nextInt();

			hss[num] = temp;
			num++;
		}
		input.close();

	}


	private int[] hss = new int[3];
	@Override
	
	public void paintComponent(Graphics g) {
		Font small = new Font("ZapfDingbats", Font.BOLD, 40);
		Font large = new Font("ZapfDingbats", Font.BOLD, 60);
		FontMetrics metr = this.getFontMetrics(small);
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, 558, 550);
		g.setColor(Color.GRAY);
		g.setFont(large);
		g.drawString("High Scores: ", 100, 170);
		g.setColor(Color.WHITE);
		g.setFont(small);
		
		for (int i = 0; i < hss.length; i++) {
			g.drawString("#" + (i + 1 + ": " + hss[i]), 200, 240 + (i * 40));
		}
	}

	@Override
	public void run() {
		// TODO
	}
	public void vis(boolean arg) {
		setVisible(arg);
	}

}

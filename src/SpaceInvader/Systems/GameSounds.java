package SpaceInvader.Systems;

import java.io.File;
import java.io.IOException;
//import java.util.Scanner;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class GameSounds {

	public static String background = "src\\sound\\TTT.wav";
	public static String shot = "src\\sound\\3543.wav";

	static Clip clip;

	static AudioInputStream audioInputStream;
	static String filePath;

	private static void playSound(String soundPath)
			throws UnsupportedAudioFileException, LineUnavailableException, IOException {
		// create AudioInputStream object
		audioInputStream = AudioSystem.getAudioInputStream(new File(soundPath).getAbsoluteFile());

		// create clip reference
		clip = AudioSystem.getClip();

		// open audioInputStream to the clip
		clip.open(audioInputStream);
		clip.start();
	}

	public static void background() {
		try {
			GameSounds.playSound(background);
			clip.loop(Clip.LOOP_CONTINUOUSLY);
		} catch (Exception ex) {
			System.out.println("Error with playing sound.");
			ex.printStackTrace();
		}
	}

	// TODO bullet
	public static void shot() {
		try {
			GameSounds.playSound(shot);
		} catch (Exception ex) {
			System.out.println("Error with playing sound.");
			ex.printStackTrace();
		}
	}
}

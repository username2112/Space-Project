package SpaceInvader.Systems;

import java.io.File;

public class ImagePaths {

	public static String imageFolderPath = "src\\images\\";

	private static String alienPath = "Hostiles\\alien.png";
	private static String asteroidPath = "Hostiles\\Asteroid.png";
	private static String bombPath = "Hostiles\\bomb.png"; // alien bomb
	private static String boss0Path = "Hostiles\\Boss0.png";
	private static String boss1Path = "Hostiles\\Boss1.png";
	private static String boss2Path = "Hostiles\\Boss2.png";
	private static String sBombPath = "Hostiles\\BossBullet.png";
	private static String playerPath = "Player\\player.png";
	private static String shotPath = "Player\\shot.png";
	private static String titlePath = "Misc\\title.png";
	private static String buttonPath = "Misc\\button.png";
	private static String explosionPath = "Misc\\explosion.png";
	private static String bombcPath = "Player\\bombc.png";
	private static String backgroundPath = "Misc\\starsbg.png";

	public static String getAlienPath() {
		return imageFolderPath + alienPath;
	}

	public static String getBombcPath() {
		return imageFolderPath + bombcPath;
	}

	public static String getAsteroidPath() {
		return imageFolderPath + asteroidPath;
	}

	public static String getExplosionPath() {
		return imageFolderPath + explosionPath;
	}

	public static String getBackgroundPath() {
		return imageFolderPath + backgroundPath;
	}

	public static String getBombPath() {
		return imageFolderPath + bombPath;
	}

	public static String getBoss1Path() {
		return imageFolderPath + boss1Path;
	}

	public static String getBoss0Path() {
		return imageFolderPath + boss0Path;
	}

	public static String getBoss2Path() {
		return imageFolderPath + boss2Path;
	}

	public static String getButtonPath() {
		return imageFolderPath + buttonPath;
	}

	public static String getImageFolderPath() {
		return imageFolderPath;
	}

	public static String getPlayerPath() {
		return imageFolderPath + playerPath;
	}

	public static String getSBombPath() {
		return imageFolderPath + sBombPath;
	}

	public static String getShotPath() {
		return imageFolderPath + shotPath;
	}

	public static String getTitlePath() {
		return imageFolderPath + titlePath;
	}

	public static void setImagePath(String newPath) {
		imageFolderPath = newPath;
	}

	private ImagePaths() {
	}
}

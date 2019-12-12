package SpaceInvader;

public class ImagePaths {

	public static String imageFolderPath = "src\\images\\";

	private static String alienPath = "alien.png";
	private static String asteroidPath = "Asteroid.png";
	private static String bombPath = "bomb.png";
	private static String boss0GlowPath = "Boss0Glow.png";
	private static String boss0Path = "Boss0.png";
	private static String boss1Path = "boss1.png";
	private static String boss2Path = "boss2.png";
	private static String bossPath = "Boss.png";
	private static String button0Path = "button0.png";
	private static String button1Path = "button1.png";
	private static String button2Path = "button2.png";
	private static String CthulhuPath = "Cthulhu.png";
	private static String playerPath = "player.png";
	private static String sBombPath = "SBomb.png";
	private static String shotPath = "shot.png";
	private static String titlePath = "title.png";

	public static String getAlienPath() {
		return imageFolderPath + alienPath;
	}

	public static String getAsteroidPath() {
		return imageFolderPath + asteroidPath;
	}

	public static String getBombPath() {
		return imageFolderPath + bombPath;
	}

	public static String getBoss0GlowPath() {
		return imageFolderPath + boss0GlowPath;
	}

	public static String getBoss0Path() {
		return imageFolderPath + boss0Path;
	}

	public static String getBoss1Path() {
		return imageFolderPath + boss1Path;
	}

	public static String getBoss2Path() {
		return imageFolderPath + boss2Path;
	}

	public static String getBossPath() {
		return imageFolderPath + bossPath;
	}

	public static String getButton0Path() {
		return imageFolderPath + button0Path;
	}

	public static String getButton1Path() {
		return imageFolderPath + button1Path;
	}

	public static String getButton2Path() {
		return imageFolderPath + button2Path;
	}

	public static String getCthulhuPath() {
		return imageFolderPath + CthulhuPath;
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

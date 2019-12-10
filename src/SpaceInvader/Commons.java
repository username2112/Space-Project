 
package SpaceInvader;
 
public interface Commons {
	
	/*
	 * player: 32 x 32
	 * player shot: 8 x 8
	 * boss: 128 x 128
	 * boss shot: 16 x 16
	 * alien: 32 x 32
	 * alien bomb: 16 x 16
	 * bossBomb/missile: 16 x 16
	 * 
	 * all audio must be in .wav format
	 * all images must be .png with no background
	 */
	
	
	//window dimensions
    public static final int BOARD_WIDTH = 658;
    public static final int BOARD_HEIGHT = 650;
    
    //safe zone
    public static final int GROUND = 500;
    
    //hit boxes
    	//bomb size (used in safe zone)
    	public static final int BOMB_HEIGHT = 5;
    	//alien dimensions
    	public static final int ALIEN_HEIGHT = 10;
    	public static final int ALIEN_WIDTH = 30;
    	//boss dimensions
    	public static final int BOSS_HEIGHT = 64;
    	public static final int BOSS_WIDTH = 120;
    	//player dimensions
        public static final int PLAYER_WIDTH = 25;
        public static final int PLAYER_HEIGHT = 25;
        
    //window edges (used for alien movement)
   	public static final int BORDER_RIGHT = 30;
   	public static final int BORDER_LEFT = 5;
    
   	//IDK i don't think its used	
    //public static final int GO_DOWN = 15;
    
    //integer limit, the number of aliens you need to kill to win
    public static final int NUMBER_OF_ALIENS_TO_DESTROY = 2147483647;
    
    //chance enemy will shoot
    public static final int CHANCE = 5;
    public static final int BCHANCE = 10;
    
    //game tick sleep delay
    public static final int DELAY = 15;

}
 
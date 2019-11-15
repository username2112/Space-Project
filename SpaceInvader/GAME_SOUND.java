package SpaceInvader;

import java.io.File;
import java.io.IOException;
//import java.util.Scanner;

import javax.sound.sampled.AudioInputStream; 
import javax.sound.sampled.AudioSystem; 
import javax.sound.sampled.Clip; 
import javax.sound.sampled.LineUnavailableException; 
import javax.sound.sampled.UnsupportedAudioFileException; 

public class GAME_SOUND {
	
    // to store current position 
    Long currentFrame; 
    static Clip clip; 
      
    // current status of clip 
    String status; 
      
    AudioInputStream audioInputStream; 
    static String filePath; 
	
    public GAME_SOUND() 
            throws UnsupportedAudioFileException, 
            LineUnavailableException, IOException  
        { 
            // create AudioInputStream object 
            audioInputStream = AudioSystem.getAudioInputStream(new File(filePath).getAbsoluteFile()); 

            // create clip reference 
            clip = AudioSystem.getClip(); 
              
            // open audioInputStream to the clip 
            clip.open(audioInputStream); 
              
            //clip.loop(Clip.LOOP_CONTINUOUSLY); 
        } 
    
    //TOOD background
	public static void Background() {
		 try
	        { 
	            filePath = "src\\sound\\TTT.wav"; 
	            GAME_SOUND audioPlayer = new GAME_SOUND(); 
	            clip.start();
	            //audioPlayer.play(); 
	            //while(true) {
	            clip.loop(Clip.LOOP_CONTINUOUSLY); 
	            //}
	        }  
	          
	        catch (Exception ex)  
	        { 
	            System.out.println("Error with playing sound."); 
	            ex.printStackTrace(); 
	          
	        } 

	}
    //TOOD bullet
	public static void shot() {
		 try
	        { 
	            filePath = "src/sound/3543.wav"; 
	            GAME_SOUND audioPlayer = new GAME_SOUND(); 
	            clip.start();
	        }  
	          
	        catch (Exception ex)  
	        { 
	            System.out.println("Error with playing sound."); 
	            ex.printStackTrace(); 
	          
	        } 

	}
}

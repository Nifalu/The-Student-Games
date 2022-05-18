package gameLogic;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;

/**
 * MusicPlayer plays the given music from folder resources
 */
public class MusicPlayer {
    /**
     * Plays the given music
     *
     * @param music Music to be played
     */
    public void play(String music) {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(music).getAbsoluteFile());
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
        } catch(Exception ex) {
            System.out.println("Error with playing sound.");
            ex.printStackTrace();
        }
    }
}

package gameLogic;

import javafx.scene.media.AudioClip;

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
            AudioClip clip = new AudioClip(getClass().getResource(music).toExternalForm());
            clip.play();
        } catch(Exception ex) {
            System.out.println("Error with playing sound.");
            ex.printStackTrace();
        }
    }
}

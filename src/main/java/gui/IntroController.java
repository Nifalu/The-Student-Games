package gui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;


/**
 * the controller to the intro scene which will be displayed at the start of the game
 * the intro scene shows the intro video
 */
public class IntroController implements Initializable {

    /**
     * switches to the start screen when pressing the button
     */
    public void switchToStart() {
        Main.displayStart();
    }


    /**
     * a node which provides a view of the media being played by the media player
     */
    @FXML
    private MediaView mediaView;

    /**
     * saves a file of choice
     */
    private File file;

    /**
     * saves the media to display
     */
    private Media media;

    /**
     * the MediaPlayer used to play the media
     */
    private MediaPlayer mediaPlayer;

    /**
     * method is called when the sceen first opens
     * the method will play the intro video
     *
     * @param arg0 URL
     * The location used to resolve relative paths for the root object, or
     * {@code null} if the location is not known.
     *
     * @param arg1 ResourceBundle
     * The resources used to localize the root object, or {@code null} if
     * the root object was not localized.
     */
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {

        Main.setIntroController(this);
        loadMedia();
    }


    /**
     * plays the media (here: the intro video)
     */
    public void playMedia() {
        mediaPlayer.play();
    }

    /**
     * loads media (intro)
     */
    public void loadMedia() {
        media = new Media(getClass().getResource("/Intro4.1.mp4").toExternalForm());

        mediaPlayer = new MediaPlayer(media);

        mediaView.setMediaPlayer(mediaPlayer);
    }

}

package gui;

import java.io.File;

import java.net.URL;

import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.fxml.Initializable;

import javafx.scene.control.Button;

import javafx.scene.media.Media;

import javafx.scene.media.MediaPlayer;

import javafx.scene.media.MediaView;

import javafx.util.Duration;



public class IntroController implements Initializable {
    public void switchToStart() {
        Main.displayStart();
    }


    @FXML
    private MediaView mediaView;

    @FXML
    private Button playButton, pauseButton, resetButton;

    private File file;

    private Media media;

    private MediaPlayer mediaPlayer;

    @Override

    public void initialize(URL arg0, ResourceBundle arg1) {

        file = new File("src/main/resources/Intro4.1.mp4");

        media = new Media(file.toURI().toString());

        mediaPlayer = new MediaPlayer(media);

        mediaView.setMediaPlayer(mediaPlayer);

        playMedia();

    }

    public void playMedia() {
        mediaPlayer.play();
    }

}

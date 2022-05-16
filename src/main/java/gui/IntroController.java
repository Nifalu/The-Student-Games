package gui;

import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.shape.Circle;

import javax.print.attribute.standard.Media;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXMLLoader;
import java.io.File;

import java.net.URL;

import java.util.ResourceBundle;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

import javafx.fxml.FXML;




public class IntroController implements Initializable{


    @FXML
    private MediaView mediaView;

    @FXML
    private File file;

    @FXML
    private Media media;

    @FXML
    private MediaPlayer mediaPlayer;


    public void initialize(URL location, ResourceBundle resources) {

    }
    public void switchToStart(ActionEvent actionEvent) {
        Main.displayStart();
    }
}

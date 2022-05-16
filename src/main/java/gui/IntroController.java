package gui;

import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.shape.Circle;

import java.net.URL;
import java.util.ResourceBundle;

public class IntroController {
    public void switchToStart(ActionEvent actionEvent) {
        Main.displayStart();
    }
}

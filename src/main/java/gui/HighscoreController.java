package gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HighscoreController {

    /**
     * the following methods are used to switch between scenes
     * they're only temporary
     */
    public void switchToMenu() {
        Main.displayMenu();
    }

    public void switchToGame() {
        Main.displayGame();
    }
}

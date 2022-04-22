package gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HighscoreController {
    private Stage menuStage;
    private Scene menuScene;
    private Parent menuRoot;
    private Stage gameStage;
    private Scene gameScene;
    private Parent gameRoot;

    /**
     * the following methods are used to switch between scenes
     * they're only temporary
     */
    public void switchToMenu(ActionEvent event) throws Exception {
        MenuController.hasJoinedChat = true;
        menuRoot = FXMLLoader.load(getClass().getClassLoader().getResource("fxml_menu.fxml"));
        menuStage = (Stage)((Node)event.getSource()).getScene().getWindow();
        menuScene = new Scene(menuRoot);
        menuStage.setScene(menuScene);
        menuStage.show();
    }

    public void switchToGame(ActionEvent event) throws IOException {
        GameController.hasJoinedChat = true;
        gameRoot = FXMLLoader.load(getClass().getClassLoader().getResource("fxml_game.fxml"));
        gameStage = (Stage)((Node)event.getSource()).getScene().getWindow();
        gameScene = new Scene(gameRoot);
        gameStage.setScene(gameScene);
        gameStage.show();
    }
}

package gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MenuController {
    private Stage gameStage;
    private Scene gameScene;
    private Parent gameRoot;
    private Stage highscoreStage;
    private Scene highscoreScene;
    private Parent highscoreRoot;

    /**
     * the following methods are used to switch between scenes
     * they're only temporary
     */
    public void switchToGame(ActionEvent event) throws IOException {
        GameController.hasJoinedChat = true;
        gameRoot = FXMLLoader.load(getClass().getClassLoader().getResource("fxml_game.fxml"));
        gameStage = (Stage)((Node)event.getSource()).getScene().getWindow();
        gameScene = new Scene(gameRoot);
        gameStage.setScene(gameScene);
        gameStage.show();
    }

    public void switchToHighscore(ActionEvent event) throws Exception {
        highscoreRoot = FXMLLoader.load(getClass().getClassLoader().getResource("fxml_highscore.fxml"));
        highscoreStage = (Stage)((Node)event.getSource()).getScene().getWindow();
        highscoreScene = new Scene(highscoreRoot);
        highscoreStage.setScene(highscoreScene);
        highscoreStage.show();
    }
}

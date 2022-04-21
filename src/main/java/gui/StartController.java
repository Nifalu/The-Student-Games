package gui;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import utility.io.CommandsToServer;
import utility.io.ReceiveFromProtocol;
import utility.io.SendToServer;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * this is the controller for fxml_start.fxml
 */

public class StartController implements Initializable {
    private final SendToServer sendToServer = new SendToServer();
    public static ReceiveFromProtocol receiveFromProtocol = new ReceiveFromProtocol();
    String msg;

    @FXML
    private Label showText;

    @FXML
    private TextField textInput;


    private Stage menuStage;
    private Scene menuScene;
    private Parent menuRoot;
    private Stage gameStage;
    private Scene gameScene;
    private Parent gameRoot;
    private Stage highscoreStage;
    private Scene highscoreScene;
    private Parent highscoreRoot;




    /**
     * This method reads the text from textInput and sends it to the server
     * @param actionEvent actionEvent
     */
    public void sendMsg(ActionEvent actionEvent) {
        String msg = textInput.getText();
        sendToServer.send(CommandsToServer.NAME, msg);
        textInput.clear();
    }

    /**
     * changes the displayed text on the stage
     * @param msg message
     */
    public void printMsg(String msg) {
        showText.setText(msg);
    }


    /**
     * This method is called, when the class is created
     * It is used to wait on incoming messages
     * This method waits for a change in the field msg, if it's changed it will call the printMsg method, which
     * changes the text on screen
     * @param location location
     * @param resources resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // A new Thread is made which waits
        Thread waitForMsgChange = new Thread(() -> {
            while(true) {
                msg = receiveFromProtocol.receive(); // blocks until a message is received
                Platform.runLater(() -> printMsg(msg)); // a javafx "thread" that calls the printMsg method
            }
        });
        waitForMsgChange.setName("GuiStartWaitForMsgChange"); // set name of thread
        waitForMsgChange.start(); // start thread
    }


    /**
     * the following methods are used to switch between scenes
     * they're only temporary
     */
    public void switchToMenu(ActionEvent event) throws Exception {
        GameController.hasJoinedChat = true;
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

    public void switchToHighscore(ActionEvent event) throws Exception {
        highscoreRoot = FXMLLoader.load(getClass().getClassLoader().getResource("fxml_highscore.fxml"));
        highscoreStage = (Stage)((Node)event.getSource()).getScene().getWindow();
        highscoreScene = new Scene(highscoreRoot);
        highscoreStage.setScene(highscoreScene);
        highscoreStage.show();
    }
}

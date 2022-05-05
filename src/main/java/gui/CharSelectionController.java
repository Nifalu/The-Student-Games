package gui;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;
import utility.io.CommandsToClient;
import utility.io.CommandsToServer;
import utility.io.SendToServer;

import java.net.URL;
import java.util.ResourceBundle;

public class CharSelectionController implements Initializable {

    /**
     * SendToServer object to communicate with the server
     */
    SendToServer sendToServer = new SendToServer();


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Main.setCharSelectionController(this);
    }

    @FXML
    void selectCharOne(MouseEvent event) {
        sendToServer.send(CommandsToServer.CHANGECHARACTER, "1");
        switchToGame();
    }

    @FXML
    void selectCharTwo(MouseEvent event) {
        sendToServer.send(CommandsToServer.CHANGECHARACTER, "2");
        switchToGame();
    }

    @FXML
    void selectCharThree(MouseEvent event) {
        sendToServer.send(CommandsToServer.CHANGECHARACTER, "3");
        switchToGame();
    }

    @FXML
    void selectCharFour(MouseEvent event) {
        sendToServer.send(CommandsToServer.CHANGECHARACTER, "4");
        switchToGame();
    }

    @FXML
    void selectCharFive(MouseEvent event) {
        sendToServer.send(CommandsToServer.CHANGECHARACTER, "5");
        switchToGame();
    }

    @FXML
    void selectCharSix(MouseEvent event) {
        sendToServer.send(CommandsToServer.CHANGECHARACTER, "6");
        switchToGame();
    }

    public void switchToGame () {
        Main.displayGame();
    }
}
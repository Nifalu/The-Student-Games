package gui;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import utility.io.CommandsToClient;
import utility.io.CommandsToServer;
import utility.io.SendToServer;

import java.net.URL;
import java.util.ResourceBundle;

public class CharSelectionController implements Initializable {

    @FXML
    private Circle charOneSelectionCircle;

    @FXML
    private Circle charTwoSelectionCircle;

    @FXML
    private Circle charThreeSelectionCircle;

    @FXML
    private Circle charFourSelectionCircle;

    @FXML
    private Circle charFiveSelectionCircle;

    @FXML
    private Circle charSixselectionCircle;

    /**
     * SendToServer object to communicate with the server
     */
    SendToServer sendToServer = new SendToServer();


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        Main.setCharSelectionController(this);

        charOneSelectionCircle.setStroke(Color.BLACK);
        charTwoSelectionCircle.setStroke(Color.BLACK);
        charThreeSelectionCircle.setStroke(Color.BLACK);
        charFourSelectionCircle.setStroke(Color.BLACK);

        Image char1 = new Image("char1.png", false);
        Image char2 = new Image("char2.png", false);
        Image char3 = new Image("char3.png", false);
        Image char4 = new Image("char4.png", false);
        charOneSelectionCircle.setFill(new ImagePattern(char1));
        charTwoSelectionCircle.setFill(new ImagePattern(char2));
        charThreeSelectionCircle.setFill(new ImagePattern(char3));
        charFourSelectionCircle.setFill(new ImagePattern(char4));
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
        Main.displayMenu();
    }

    public void enableCharOnScreen(String msg) {
        if (msg.equals("1")) {
            charOneSelectionCircle.setDisable(false);
            charOneSelectionCircle.setOpacity(1);
        } else if (msg.equals("2")) {
            charTwoSelectionCircle.setDisable(false);
            charTwoSelectionCircle.setOpacity(1);
        } else if(msg.equals("3")) {
            charThreeSelectionCircle.setDisable(false);
            charThreeSelectionCircle.setOpacity(1);
        } else if (msg.equals("4")){
            charFourSelectionCircle.setDisable(false);
            charFourSelectionCircle.setOpacity(1);
        }
    }
    public void disableCharOnScreen(String msg) {
        if (msg.equals("1")) {
            charOneSelectionCircle.setDisable(true);
            charOneSelectionCircle.setOpacity(0.1);
        } else if (msg.equals("2")) {
            charTwoSelectionCircle.setDisable(true);
            charTwoSelectionCircle.setOpacity(0.1);
        } else if(msg.equals("3")) {
            charThreeSelectionCircle.setDisable(true);
            charThreeSelectionCircle.setOpacity(0.1);
        } else if (msg.equals("4")){
            charFourSelectionCircle.setDisable(true);
            charFourSelectionCircle.setOpacity(0.1);
        }
    }
}
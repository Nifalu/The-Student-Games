package gui;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
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
    private Circle charSixSelectionCircle;

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
        charFiveSelectionCircle.setStroke(Color.BLACK);
        charSixSelectionCircle.setStroke(Color.BLACK);


        Image char1 = new Image("char1.png", false);
        Image char2 = new Image("char2.png", false);
        Image char3 = new Image("char3.png", false);
        Image char4 = new Image("char4.png", false);
        Image char5 = new Image("char5.png", false);
        Image char6 = new Image("char6.png", false);
        charOneSelectionCircle.setFill(new ImagePattern(char1));
        charTwoSelectionCircle.setFill(new ImagePattern(char2));
        charThreeSelectionCircle.setFill(new ImagePattern(char3));
        charFourSelectionCircle.setFill(new ImagePattern(char4));
        charFiveSelectionCircle.setFill(new ImagePattern(char5));
        charSixSelectionCircle.setFill(new ImagePattern(char6));
    }

    @FXML
    void selectCharOne(MouseEvent event) {
        sendToServer.send(CommandsToServer.CHANGECHARACTER, "1");
        close();
    }

    @FXML
    void selectCharTwo(MouseEvent event) {
        sendToServer.send(CommandsToServer.CHANGECHARACTER, "2");
        // switchToGame();
        close();
    }

    @FXML
    void selectCharThree(MouseEvent event) {
        sendToServer.send(CommandsToServer.CHANGECHARACTER, "3");
        //switchToGame();
        close();
    }

    @FXML
    void selectCharFour(MouseEvent event) {
        sendToServer.send(CommandsToServer.CHANGECHARACTER, "4");
        //switchToGame();
        close();
    }

    @FXML
    void selectCharFive(MouseEvent event) {
        sendToServer.send(CommandsToServer.CHANGECHARACTER, "5");
        //switchToGame();
        close();
    }

    @FXML
    void selectCharSix(MouseEvent event) {
        sendToServer.send(CommandsToServer.CHANGECHARACTER, "6");
        //switchToGame();
        close();
    }

    public void switchToGame () {
        Main.displayMenu();
    }

    public void enableCharOnScreen(String msg) {
        switch (msg) {
            case "1":
                charOneSelectionCircle.setDisable(false);
                charOneSelectionCircle.setOpacity(1);
                break;
            case "2":
                charTwoSelectionCircle.setDisable(false);
                charTwoSelectionCircle.setOpacity(1);
                break;
            case "3":
                charThreeSelectionCircle.setDisable(false);
                charThreeSelectionCircle.setOpacity(1);
                break;
            case "4":
                charFourSelectionCircle.setDisable(false);
                charFourSelectionCircle.setOpacity(1);
                break;
            case "5":
                charFiveSelectionCircle.setDisable(false);
                charFiveSelectionCircle.setOpacity(1);
                break;
            case "6":
                charSixSelectionCircle.setDisable(false);
                charSixSelectionCircle.setOpacity(1);
                break;
        }
    }
    public void disableCharOnScreen(String msg) {
        switch (msg) {
            case "1":
                charOneSelectionCircle.setDisable(true);
                charOneSelectionCircle.setOpacity(0.1);
                break;
            case "2":
                charTwoSelectionCircle.setDisable(true);
                charTwoSelectionCircle.setOpacity(0.1);
                break;
            case "3":
                charThreeSelectionCircle.setDisable(true);
                charThreeSelectionCircle.setOpacity(0.1);
                break;
            case "4":
                charFourSelectionCircle.setDisable(true);
                charFourSelectionCircle.setOpacity(0.1);
                break;
            case "5":
                charFiveSelectionCircle.setDisable(true);
                charFiveSelectionCircle.setOpacity(0.1);
                break;
            case "6":
                charSixSelectionCircle.setDisable(true);
                charSixSelectionCircle.setOpacity(0.1);
                break;
        }
    }

    private void close() {
        Stage stage = (Stage) charTwoSelectionCircle.getScene().getWindow();
        stage.close();
    }
}
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

/**
 * this is the controller for the character selection popup in the menu
 * the popup is triggered when joining a lobby or pressing the "change character" button
 */

public class CharSelectionController implements Initializable {

    /**
     * the circle to selecht character 1
     */
    @FXML
    private Circle charOneSelectionCircle;

    /**
     * the circle to selecht character 2
     */
    @FXML
    private Circle charTwoSelectionCircle;

    /**
     * the circle to selecht character 3
     */
    @FXML
    private Circle charThreeSelectionCircle;

    /**
     * the circle to selecht character 4
     */
    @FXML
    private Circle charFourSelectionCircle;

    /**
     * the circle to selecht character 5
     */
    @FXML
    private Circle charFiveSelectionCircle;

    /**
     * the circle to selecht character 6
     */
    @FXML
    private Circle charSixSelectionCircle;

    /**
     * SendToServer object to communicate with the server
     */
    SendToServer sendToServer = new SendToServer();

    /**
     * is called when the pop up first shows up
     *
     * here the images are set to the character selection circles
     *
     * @param location URL
     * The location used to resolve relative paths for the root object, or
     * {@code null} if the location is not known.
     *
     * @param resources ResourceBundle
     * The resources used to localize the root object, or {@code null} if
     * the root object was not localized.
     */
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


    /**
     * sets the users character to 1
     * @param event MouseEvent
     */
    @FXML
    void selectCharOne(MouseEvent event) {
        sendToServer.send(CommandsToServer.CHANGECHARACTER, "1");
        close();
    }

    /**
     * sets the users character to 2
     * @param event MouseEvent
     */
    @FXML
    void selectCharTwo(MouseEvent event) {
        sendToServer.send(CommandsToServer.CHANGECHARACTER, "2");
        // switchToGame();
        close();
    }

    /**
     * sets the users character to 3
     * @param event MouseEvent
     */
    @FXML
    void selectCharThree(MouseEvent event) {
        sendToServer.send(CommandsToServer.CHANGECHARACTER, "3");
        //switchToGame();
        close();
    }

    /**
     * sets the users character to 4
     * @param event MouseEvent
     */
    @FXML
    void selectCharFour(MouseEvent event) {
        sendToServer.send(CommandsToServer.CHANGECHARACTER, "4");
        //switchToGame();
        close();
    }

    /**
     * sets the users character to 5
     * @param event MouseEvent
     */
    @FXML
    void selectCharFive(MouseEvent event) {
        sendToServer.send(CommandsToServer.CHANGECHARACTER, "5");
        //switchToGame();
        close();
    }

    /**
     * sets the users character to 6
     * @param event MouseEvent
     */
    @FXML
    void selectCharSix(MouseEvent event) {
        sendToServer.send(CommandsToServer.CHANGECHARACTER, "6");
        //switchToGame();
        close();
    }

    /**
     * will be called if a player changes off a character (for example if they join a different lobby)
     * their old character will be set to full opacity and enabled again
     *
     * @param msg String containing the character number to be enabled again
     */
    public void enableCharOnScreen(String msg) {
        switch (msg) {
            case "0":
                // do nothing
                break;
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

    /**
     * will be called if a player in the lobby chose a character
     * their chosen character will turn partially transparent and be disabled for everyone
     * @param msg String contianing the Character number
     */
    public void disableCharOnScreen(String msg) {
        switch (msg) {
            case "0":
                // do nothing
                break;
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

    /**
     * closes the popup
     */
    private void close() {
        Stage stage = (Stage) charTwoSelectionCircle.getScene().getWindow();
        stage.close();
    }

    /**
     * changes the image of the character selection circle nr. 1 to the hover image of the character
     * @param mouseEvent MouseEvent: moving into the circle
     */
    public void changeCharOne(MouseEvent mouseEvent) {
        Image img = new Image("char1hover.png", false);
        charOneSelectionCircle.setFill(new ImagePattern(img));
    }

    /**
     * changes the image of the character selection circle nr. 1 back to the regular image when the client
     * stops hovering
     * @param mouseEvent MouseEvent: exiting the circle
     */
    public void changeCharOneBack(MouseEvent mouseEvent) {
        Image img = new Image("char1.png", false);
        charOneSelectionCircle.setFill(new ImagePattern(img));
    }

    /**
     * changes the image of the character selection circle nr. 2 to the hover image of the character
     * @param mouseEvent MouseEvent: moving into the circle
     */
    public void changeCharTwo(MouseEvent mouseEvent) {
        Image img = new Image("char2hover.png", false);
        charTwoSelectionCircle.setFill(new ImagePattern(img));
    }

    /**
     * changes the image of the character selection circle nr. 2 back to the regular image when the client
     * stops hovering
     * @param mouseEvent MouseEvent: exiting the circle
     */
    public void changeCharTwoBack(MouseEvent mouseEvent) {
        Image img = new Image("char2.png", false);
        charTwoSelectionCircle.setFill(new ImagePattern(img));
    }

    /**
     * changes the image of the character selection circle nr. 3 to the hover image of the character
     * @param mouseEvent MouseEvent: moving into the circle
     */
    public void changeCharThree(MouseEvent mouseEvent) {
        Image img = new Image("char3hover.png", false);
        charThreeSelectionCircle.setFill(new ImagePattern(img));
    }

    /**
     * changes the image of the character selection circle nr. 3 back to the regular image when the client
     * stops hovering
     * @param mouseEvent MouseEvent: exiting the circle
     */
    public void changeCharThreeBack(MouseEvent mouseEvent) {
        Image img = new Image("char3.png", false);
        charThreeSelectionCircle.setFill(new ImagePattern(img));
    }

    /**
     * changes the image of the character selection circle nr. 4 to the hover image of the character
     * @param mouseEvent MouseEvent: moving into the circle
     */
    public void changeCharFour(MouseEvent mouseEvent) {
        Image img = new Image("char4hover.png", false);
        charFourSelectionCircle.setFill(new ImagePattern(img));
    }

    /**
     * changes the image of the character selection circle nr. 4 back to the regular image when the client
     * stops hovering
     * @param mouseEvent MouseEvent: exiting the circle
     */
    public void changeCharFourBack(MouseEvent mouseEvent) {
        Image img = new Image("char4.png", false);
        charFourSelectionCircle.setFill(new ImagePattern(img));
    }

    /**
     * changes the image of the character selection circle nr. 5 to the hover image of the character
     * @param mouseEvent MouseEvent: moving into the circle
     */
    public void changeCharFive(MouseEvent mouseEvent) {
        Image img = new Image("char5hover.png", false);
        charFiveSelectionCircle.setFill(new ImagePattern(img));
    }

    /**
     * changes the image of the character selection circle nr. 5 back to the regular image when the client
     * stops hovering
     * @param mouseEvent MouseEvent: exiting the circle
     */
    public void changeCharFiveBack(MouseEvent mouseEvent) {
        Image img = new Image("char5.png", false);
        charFiveSelectionCircle.setFill(new ImagePattern(img));
    }

    /**
     * changes the image of the character selection circle nr. 6 to the hover image of the character
     * @param mouseEvent MouseEvent: moving into the circle
     */
    public void changeCharSix(MouseEvent mouseEvent) {
        Image img = new Image("char6hover.png", false);
        charSixSelectionCircle.setFill(new ImagePattern(img));
    }

    /**
     * changes the image of the character selection circle nr. 6 back to the regular image when the client
     * stops hovering
     * @param mouseEvent MouseEvent: exiting the circle
     */
    public void changeCharSixBack(MouseEvent mouseEvent) {
        Image img = new Image("char6.png", false);
        charSixSelectionCircle.setFill(new ImagePattern(img));
    }
}
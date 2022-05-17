package gui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * the controller for the quitconfirm scene which is shown when the quit button is pressed in the GUI
 */
public class QuitConfirmController {

    /**
     * the image representing the yes "button"
     */
    @FXML
    private ImageView yesButton;

    /**
     * closes the game (disconnects the client, closes the GUI) when pressing the yes "button"
     * @param mouseEvent
     */
    public void yesQuit(MouseEvent mouseEvent) {
        Main.exit();
    }

    /**
     * closes the popup window without closing the GUI or disconnecting the client
     * called by pressing the no "button"
     * @param mouseEvent
     */
    public void noQuit(MouseEvent mouseEvent) {
        Stage stage = (Stage) yesButton.getScene().getWindow();
        stage.close();
    }
}

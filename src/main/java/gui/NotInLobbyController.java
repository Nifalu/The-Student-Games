package gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

/**
 * the controller for the notInLobby scene
 */
public class NotInLobbyController {

    /**
     * a button to close the window
     */
    @FXML
    private Button button;

    /**
     * closes the window
     * @param actionEvent ActionEvent
     */
    public void closeWindow(ActionEvent actionEvent) {
        Stage stage = (Stage) button.getScene().getWindow();
        stage.close();
    }
}

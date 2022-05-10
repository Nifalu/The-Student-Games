package gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class NotInLobbyController {

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

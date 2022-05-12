package gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import utility.io.CommandsToServer;
import utility.io.SendToServer;

public class NameSelectionController {

    @FXML
    public TextField changeNameTextField;

    @FXML
    public Button button;


    SendToServer sendToServer = new SendToServer();

    public void changeName() {
        String newName = changeNameTextField.getText();
        sendToServer.send(CommandsToServer.NICK, newName);
        Stage stage = (Stage) button.getScene().getWindow();
        stage.close();
    }
}

package gui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import utility.io.CommandsToServer;
import utility.io.SendToServer;

import java.net.URL;
import java.util.ResourceBundle;

public class NameSelectionController implements Initializable {

    @FXML
    public ImageView okButton;

    @FXML
    private TextField changeNameTextField;

    private final SendToServer sendToServer = new SendToServer();



    public void changeName() {
        String newName = changeNameTextField.getText();
        sendToServer.send(CommandsToServer.NICK, newName);
        Main.getMenuController().refreshFriends();
        Stage stage = (Stage) changeNameTextField.getScene().getWindow();
        stage.close();
    }


    private void okListener() {
        EventHandler<ActionEvent> tmp = changeNameTextField.getOnAction();
        okButton.setDisable(true);
        okButton.setOpacity(0.5);
        changeNameTextField.onActionProperty().set(null);
        changeNameTextField.textProperty().addListener((obs, oldv, newv) -> {
            if (newv.equals("")) {
                okButton.setDisable(true);
                okButton.opacityProperty().set(0.5);
                changeNameTextField.onActionProperty().set(null);
            } else {
                okButton.setDisable(false);
                okButton.opacityProperty().set(1);
                changeNameTextField.onActionProperty().set(tmp);
            }
        });
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        okListener();
    }
}

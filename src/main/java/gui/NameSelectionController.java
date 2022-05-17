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

/**
 * this class is used as a controller for the name selection popup in the menu scene
 */
public class NameSelectionController implements Initializable {

    /**
     * a button used to send a new username
     */
    @FXML
    public ImageView okButton;

    /**
     * a textfield used to enter a new username
     */
    @FXML
    private TextField changeNameTextField;

    /**
     * a SendToServer object to communicate with the server
     */
    private final SendToServer sendToServer = new SendToServer();


    /**
     * this method is used to change the users name by reading his input on the "changeNameTextField" when the
     * users presses the ok button
     */
    public void changeName() {
        String newName = changeNameTextField.getText();
        sendToServer.send(CommandsToServer.NICK, newName);
        Main.getMenuController().refreshFriends();
        Stage stage = (Stage) changeNameTextField.getScene().getWindow();
        stage.close();
    }

    /**
     * adds a listener to the ok-button
     * is used to ensure the user can only press "ok" when their new username is valid
     */
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

    /**
     * this will be called when the scene starts
     * calls the "okListener" method
     *
     * @param location
     * The location used to resolve relative paths for the root object, or
     * {@code null} if the location is not known.
     *
     * @param resources
     * The resources used to localize the root object, or {@code null} if
     * the root object was not localized.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        okListener();
    }
}

package gui;

import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * the controller for the menu confirmation popup
 */
public class MenuConfirmController {

    /**
     * the image used as the yes "button"
     */
    @FXML
    private ImageView yesButton;

    /**
     * by pressing the no "button"" the popup closes and the scene won't change
     * @param event
     */
    @FXML
    void noMenu(MouseEvent event) {
        Stage stage = (Stage) yesButton.getScene().getWindow();
        stage.close();
    }

    /**
     * by pressing the yes "button" the popup closes and the scene changes to the menu
     * @param event
     */
    @FXML
    void yesMenu(MouseEvent event) {
        Main.displayMenu();
        Stage stage = (Stage) yesButton.getScene().getWindow();
        stage.close();
    }

}

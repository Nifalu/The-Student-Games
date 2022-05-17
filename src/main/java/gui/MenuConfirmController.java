package gui;

import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class MenuConfirmController {

    @FXML
    private ImageView yesButton;

    @FXML
    void noMenu(MouseEvent event) {
        Stage stage = (Stage) yesButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    void yesMenu(MouseEvent event) {
        Main.displayMenu();
        Stage stage = (Stage) yesButton.getScene().getWindow();
        stage.close();
    }

}

package gui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class QuitConfirmController {
    @FXML
    private ImageView yesButton;

    public void yesQuit(MouseEvent mouseEvent) {
        Main.exit();
    }

    public void noQuit(MouseEvent mouseEvent) {
        Stage stage = (Stage) yesButton.getScene().getWindow();
        stage.close();
    }
}

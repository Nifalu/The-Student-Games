package gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


import static javafx.application.Application.launch;

public class MainSave extends Application {

    /**
     * this class can technically be deleted
     * it's a copy of a previous version of the Main class which I was too afraid to delete
     * I'll keep it for now, it's not in use though
     */
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("fxml_chat.fxml"));

        Scene scene = new Scene(root, 600, 600);

        stage.setTitle("FXML Welcome");
        stage.setScene(scene);
        stage.show();
    }
}

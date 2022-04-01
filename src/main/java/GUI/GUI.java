package GUI;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * to launch this GUI class run the corresponding Main class
 *
 * ATM this class only exists to get familiar with javafx
 */

public class GUI extends Application {
    Button chnopf;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Seich");
        chnopf = new Button();
        chnopf.setText("en Chnopf");

        // creates Layout for button
        StackPane layout = new StackPane();
        layout.getChildren().add(chnopf);

        Scene chnopfScene = new Scene(layout, 500, 500); // sets up a scene
        primaryStage.setScene(chnopfScene); // tells which scene to show

        primaryStage.show(); // makes stage visible
    }
}

package GUI_Tests;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 *
 * to launch this class run the corresponding Main class
 *
 * another class that will be deleted later on :)
 * here: switching between scenes
 */
public class Scenes extends Application {
    Stage window; // needs a stage
    Scene sceneOne, sceneTwo;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        window = primaryStage;

        Label labelOne = new Label("mer send ide erste Szene, wow");
        Button chnopfOne = new Button("wotsch id Szene zwoi?? O_O");
        chnopfOne.setOnAction(e -> window.setScene(sceneTwo)); // switches to a different scene

        Button chnopfTwo = new Button("chasch au weder zrog wend wotsch");
        chnopfTwo.setOnAction(e -> window.setScene(sceneOne));

        /**
         * two different layouts are shown
         */
        // 1: VBox (stacks children on top of each other
        VBox layoutOne = new VBox(20); // VBox stacks all children on top of eachother
        layoutOne.getChildren().addAll(labelOne, chnopfOne);
        sceneOne = new Scene(layoutOne, 200, 200);

        // 2: StackPane
        StackPane layoutTwo = new StackPane();
        layoutTwo.getChildren().add(chnopfTwo);
        sceneTwo = new Scene(layoutTwo, 600, 300);


        // sets what scene will be shown at the beginning
        window.setScene(sceneOne);
        window.setTitle("weder es guets Programm zum Sache usprobiere");

        window.show(); // NED VERGESSE
    }
}

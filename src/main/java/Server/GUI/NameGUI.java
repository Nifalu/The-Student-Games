package Server.GUI;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * running this class may not work
 * to launch the GUI run the corresponding Main class instead
 */
public class NameGUI extends Application {
    Stage window;

    Scene proposeNameScene;
    Label proposeNameLabel;

    Scene askNameScene;
    Label askNameLabel;

    Scene finalNameScene;
    Label finalNameLabel;

    Scene greetingScene;
    Label greetingLabel;

    Button yes;
    Button no;
    Button ok;

    // names
    String username = "player1";
    String chosenName;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        window = primaryStage;
        String finalName;
        yes = new Button("yes");
        no = new Button("no");
        ok = new Button("ok");

        // creates scene 1: proposing name to user
        proposeNameLabel = new Label("Hey there, would you like to be named " + username + "?");
        // creates layout 1: not final layout, it's quite ugly
        VBox proposeNameLayout = new VBox(20);
        proposeNameLayout.getChildren().addAll(proposeNameLabel, yes, no);
        proposeNameScene = new Scene(proposeNameLayout, 500, 500);


        // creates scene 2: asks username if user said No before
        askNameLabel = new Label("Please enter your desired name below.");
        TextField nameInput = new TextField();
        // creates layout 2
        VBox askNameLayout = new VBox(20);
        askNameLayout.getChildren().addAll(askNameLabel, nameInput, ok);
        askNameScene = new Scene(askNameLayout, 500, 500);


        // creates scene 3: shows final username
        finalNameLabel = new Label("You're now called " + chosenName);
        // creates Layout 3
        VBox finalNameLayout = new VBox(20);
        finalNameLayout.getChildren().add(finalNameLabel);
        finalNameScene = new Scene(finalNameLayout, 500, 500);

        // sets scene at the beginning
        window.setScene((proposeNameScene));
        window.setTitle("Welcome!");
        window.show();

        yes.setOnAction(e -> {
            this.chosenName = username;
            window.setScene(finalNameScene);
        }); // needs to be changed
        no.setOnAction(e -> window.setScene(askNameScene)); // goes to scene where user can type in own name
        ok.setOnAction(e -> {
            setNewName(nameInput.getText()); // sets the new name
            window.setScene(finalNameScene);
        });

        }

    /**
     * methods
     */
    private void setNewName(String name) {
        this.chosenName = name;
    }

}


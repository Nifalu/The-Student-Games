package GUI_Tests;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import javafx.event.ActionEvent;

/**
 *
 * to launch the GUI classes run the corresponding Main class
 *
 * ATM this class only exists to get familiar with javafx (buttons)
 * it has a lot of comments in it, can be seen as somewhat of a reference on how to make buttons
 * no idea why that would be needed though
 * I should have probably done this in a private project, will delete later :)
 *
 */

public class Buttons extends Application implements EventHandler<ActionEvent> {
    Button chnopf;
    Button chnopfZwoi;
    Button chnopfThree;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("ergend es Fenster zum Seich denn usprobiere");

        // buttons are created here
        chnopf = new Button();
        chnopf.setText("de Original-Chnopf"); // labels can be set this way, or directly in the constructor (see below)
        chnopfZwoi = new Button("en andere Chnopf");
        chnopfThree = new Button("random Chnopf");

        /**
         * events are handled here
         * there are three different ways on how to do that shown here
         */
        // 1: kind of long, but good, if you want to code the specific actions in another class
        chnopf.setOnAction(this); // 'this' tells chnopf in which class to search for his event

        // 2: shorter, but gets a bit long if you have a lot of things to declare
        chnopfZwoi.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("ned so cool wie de erst Chnopf aber au en guete Chnopf");
            }
        });
        // 3: shortest version using lambdas
        chnopfThree.setOnAction(e -> {
            System.out.println("gratuliere, du hesch en Chnopf dröckt");
            System.out.println("de Chnopf seit sogar zwoi sache");
        }
        );


        // buttons need a layout
        // here it is created
        StackPane layout = new StackPane();

        // layout.getChildren().add(chnopf); // this would add one button manually
        layout.getChildren().addAll(chnopf, chnopfZwoi, chnopfThree); // idk how to fix that they're on top of eachother

        Scene chnopfScene = new Scene(layout, 500, 500); // sets up a scene and it's size
        primaryStage.setScene(chnopfScene); // tells which scene to show



        primaryStage.show(); // makes stage visible, is always needed
    }

    /**
     * this method gets called when the user clicks the buttons
     * @param event
     */
    @Override
    public void handle(ActionEvent event) {
        if(event.getSource() == chnopf) { // identifies where the action comes from
            System.out.println("hehe");
        }

    }
}

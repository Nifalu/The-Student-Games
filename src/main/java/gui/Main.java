package gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


import static javafx.application.Application.launch;

public class Main extends Application {

  /**
   * this class contains the start method to launch the GUI
   * the actual launch happens in the Launcher class though
   * the first scene that will be set is the start scene (fxml_start)
   * @param stage
   * @throws Exception
   */

  @Override
  public void start(Stage stage) throws Exception {
    Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("fxml_start.fxml"));

    Scene scene = new Scene(root, 600, 400);

    stage.setTitle("Welcome to the Student Games");
    stage.setScene(scene);
    stage.show();
  }
}

package gui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


import java.awt.*;

import java.util.Objects;


/**
 * this class contains the start method to launch the GUI
 * it will be called in the Launcher class
 */
public class Main extends Application {

  private static Stage stage;

  private static Scene start;
  private static Scene menu;
  private static Scene game;
  private static Scene highscore;

  private static double screenW;
  private static double screenH;




  /**
   * starts the GUI with the scene fxml_start
   * @param stage Stage
   * @throws Exception Exception
   */
  @Override
  public void start(Stage stage) throws Exception {
    stage.setTitle("Welcome to the Student Games");
    stage.setOnCloseRequest(e -> {
      /*
      SendToServer sendToServer = new SendToServer(q -> {
        CommandsToServer.QUIT, "";
      } );

       */
      Platform.exit();
      System.exit(0);
    });
    Main.stage = stage;

    Parent startfxml = FXMLLoader.load(Objects.requireNonNull(Main.class.getClassLoader().getResource("fxml_start.fxml")));
    start = new Scene(startfxml);

    Parent menufxml = FXMLLoader.load(Objects.requireNonNull(Main.class.getClassLoader().getResource("fxml_menu.fxml")));
    menu = new Scene(menufxml);

    Parent gamefxml = FXMLLoader.load(Objects.requireNonNull(Main.class.getClassLoader().getResource("fxml_game.fxml")));
    game = new Scene(gamefxml);

    Parent highscorefxml = FXMLLoader.load(Objects.requireNonNull(Main.class.getClassLoader().getResource("fxml_highscore.fxml")));
    highscore = new Scene(highscorefxml);

    GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
    screenW = gd.getDisplayMode().getWidth() * 0.5;
    screenH = gd.getDisplayMode().getHeight() * 0.5;
    displayStart();
  }

  public static void displayMenu() {
    MenuController.hasJoinedChat = true;
    setup(menu, screenW, screenH, true);
  }

  public static void displayStart() {
    setup(start, 600, 400, false);
  }

  public static void displayGame() {
    GameController.hasJoinedChat = true;
    setup(game, screenW, screenH, true);

  }

  public static void displayHighscore() {
    setup(highscore, screenW, screenH, true);
  }


  private static void setup(Scene scene, double minWidth, double minHeight, boolean resizable) {
    stage.setScene(scene);
    stage.setMinWidth(minWidth);
    stage.setMinHeight(minHeight);
    stage.setResizable(resizable);
    stage.centerOnScreen();
    stage.show();
  }
}

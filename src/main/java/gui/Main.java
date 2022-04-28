package gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import utility.io.CommandsToServer;
import utility.io.SendToServer;

import java.util.Objects;


/**
 * this class contains the start method to launch the GUI
 * it will be called in the Launcher class
 */
public class Main extends Application {

  private static Stage stage;

  private static Scene start;
  private static Pane startRoot;
  private static Scene menu;
  private static Pane menuRoot;
  private static Scene game;
  private static Pane gameRoot;
  private static Scene highscore;
  private static Pane hsRoot;




  /**
   * starts the GUI with the scene fxml_start
   * @param stage Stage
   * @throws Exception Exception
   */
  @Override
  public void start(Stage stage) throws Exception {
    stage.setTitle("Welcome to the Student Games");
    stage.setOnCloseRequest(e -> {
      SendToServer sendToServer = new SendToServer();
      sendToServer.send(CommandsToServer.QUIT,"");
    });
    Main.stage = stage;


    FXMLLoader startloader = new FXMLLoader(Objects.requireNonNull(Main.class.getClassLoader().getResource("fxml_start.fxml")));
    startRoot = startloader.load();
    start = new Scene(startRoot);
    start.getStylesheets().add("TheStudentGameLook.css");

    FXMLLoader menuloader = new FXMLLoader(Objects.requireNonNull(Main.class.getClassLoader().getResource("fxml_menu.fxml")));
    menuRoot = menuloader.load();
    menu = new Scene(menuRoot);
    menu.getStylesheets().add("TheStudentGameLook.css");

    FXMLLoader gameloader = new FXMLLoader(Objects.requireNonNull(Main.class.getClassLoader().getResource("fxml_game.fxml")));
    gameRoot = gameloader.load();
    game = new Scene(gameRoot);
    game.getStylesheets().add("TheStudentGameLook.css");

    FXMLLoader hsloader = new FXMLLoader(Objects.requireNonNull(Main.class.getClassLoader().getResource("fxml_highscore.fxml")));
    hsRoot = hsloader.load();
    highscore = new Scene(hsRoot);
    highscore.getStylesheets().add("TheStudentGameLook.css");

    displayStart();
  }

  public static void displayMenu() {
    MenuController.hasJoinedChat = true;
    setup(menu, menuRoot);
  }

  public static void displayStart() {
    setup(start, startRoot);
  }

  public static void displayGame() {
    GameController.hasJoinedChat = true;
    setup(game,gameRoot);
  }

  public static void displayHighscore() {
    setup(highscore, hsRoot);

  }


  private static void setup(Scene scene, Pane pane) {
    stage.setScene(scene);
    stage.sizeToScene();
    stage.setMinWidth(stage.getWidth());
    stage.setMinHeight(stage.getHeight());
    stage.setResizable(true);
    stage.centerOnScreen();
    stage.show();
    stage.setMinWidth(stage.getWidth());
    stage.setMinHeight(stage.getHeight());
    letterbox(scene,pane);
  }


  private static void letterbox(final Scene scene, final Pane contentPane) {
    final double initWidth  = scene.getWidth();
    final double initHeight = scene.getHeight();
    final double ratio      = initWidth / initHeight;

    SceneSizeChangeListener sizeListener = new SceneSizeChangeListener(scene, ratio, initHeight, initWidth, contentPane);
    scene.widthProperty().addListener(sizeListener);
    scene.heightProperty().addListener(sizeListener);
  }

}

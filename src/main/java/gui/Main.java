package gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utility.io.CommandsToServer;
import utility.io.SendToServer;

import java.io.IOException;
import java.util.Objects;


/**
 * this class contains the start method to launch the GUI
 * it will be called in the Launcher class
 */
public class Main extends Application {

  // Stage or "Window" where everything takes place
  private static Stage stage;

  // Different Scenes that can be shown on the Stage and their corresponding root Panes.
  private static Scene start;
  private static Pane startRoot;
  private static Scene menu;
  private static Pane menuRoot;
  private static Pane gameRoot;
  private static Scene game;
  private static Scene highscore;
  private static Pane highscoreRoot;

  // Logger
  private static final Logger logger = LogManager.getLogger();

  // Sets the .css file to be used for the scenes.
  private static final String CSS = "TheStudentGameLook.css";


  /**
   * preloads all necessary scenes and displays the start scene on the stage.
   *
   * @param stage Stage
   */
  @Override
  public void start(Stage stage) {
    stage.setTitle("Welcome to the Student Games");
    stage.setOnCloseRequest(e -> exit());
    Main.stage = stage;

    startRoot = getRoot("fxml_start.fxml");
    start = createScene(startRoot);

    menuRoot = getRoot("fxml_menu.fxml");
    menu = createScene(menuRoot);

    highscoreRoot = getRoot("fxml_highscore.fxml");
    highscore = createScene(highscoreRoot);

    displayStart(); // Display the first Scene.
    stage.centerOnScreen();
  }

  /**
   * Display the Menu scene on stage.
   */
  public static void displayMenu() {
    MenuController.hasJoinedChat = true;
    showScene(menu, menuRoot);
  }

  /**
   * Display the Start scene on stage.
   */
  public static void displayStart() {
    showScene(start, startRoot);
  }

  /**
   * Creates a new GameScene and Displays it on the Screen.
   * Note: This will overwrite any visual changes to any previous Game scenes!
   * Use this method when a new game is created.
   */
  public static void displayNewGame() {
    gameRoot = getRoot("fxml_game.fxml");
    game = createScene(gameRoot);
    showScene(game, gameRoot);
    GameController.hasJoinedChat = true;
  }

  /**
   * Displays the Game scene on stage.
   * Note: This Method will not overwrite any visual changes to previously shown Game scenes!
   * Use this method to switch scenes while a game is ongoing.
   * If this method is called and no Game scene has yet been created, this method will create a new game scene.
   */
  public static void displayGame() {
    if (game != null && gameRoot != null) {
      showScene(game, gameRoot);
    } else {
      displayNewGame();
      logger.warn("Tried to display 'Game' Scene but it was 'null' -> created new scene instead");
    }
  }

  /**
   * Display the Highscore scene on stage.
   */
  public static void displayHighscore() {
    showScene(highscore, highscoreRoot);
  }

  /**
   * Creates a new scene from the given root pane and adds css
   *
   * @param root Root pane to be used to create the scene
   * @return Scene object of the newly created scene.
   */
  private static Scene createScene(Pane root) {
    Scene scene = new Scene(root);
    scene.getStylesheets().add(CSS);
    return scene;
  }

  /**
   * Shows the given scene on the stage.
   *
   * @param scene Scene object to be shown.
   * @param pane  root pane of that scene.
   */
  private static void showScene(Scene scene, Pane pane) {
    stage.setScene(scene);
    stage.sizeToScene();
    stage.setMinWidth(stage.getWidth());
    stage.setMinHeight(stage.getHeight());
    stage.setResizable(true);
    stage.show();
    stage.setMinWidth(stage.getWidth());
    stage.setMinHeight(stage.getHeight());
    letterbox(scene, pane);
  }

  /**
   * Returns the root pane of the given fxml file.
   * If the fxml file is not found or could not be loaded the program will close and the error is logged.
   *
   * @param fxml String with the name of the fxml file
   * @return the root pane of the given fxml file
   */
  private static Pane getRoot(String fxml) {
    try {
      return new FXMLLoader(Objects.requireNonNull(Main.class.getClassLoader().getResource(fxml))).load();
    } catch (IOException e) {
      logger.error("Failed to load: " + fxml, e);
      exit();
    }
    return null;
  }


  /**
   * Controls the boundaries of the content in a scene. Ensures that nothing moves outside the window when resizing
   * while also maintaining the aspect ratio of the pane.
   *
   * @param scene       scene to be controlled.
   * @param contentPane Pane to stay inside the window while keeping its aspect ratio.
   */
  private static void letterbox(final Scene scene, final Pane contentPane) {
    final double initWidth = scene.getWidth();
    final double initHeight = scene.getHeight();
    final double ratio = initWidth / initHeight;

    SceneSizeChangeListener sizeListener = new SceneSizeChangeListener(scene, ratio, initHeight, initWidth, contentPane);
    scene.widthProperty().addListener(sizeListener);
    scene.heightProperty().addListener(sizeListener);
  }

  /**
   * Sends a quit command to the server. This will close the Program!
   */
  private static void exit() {
    SendToServer sendToServer = new SendToServer();
    sendToServer.send(CommandsToServer.QUIT, "");
  }

}

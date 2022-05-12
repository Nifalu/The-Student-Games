package gui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import starter.Starter;
import utility.io.CommandsToServer;
import utility.io.SendToServer;

import java.util.Objects;


/**
 * this class contains the start method to launch the GUI
 * it will be called in the Launcher class
 */
public class Main extends Application {

  /**
   * Stage or "Window" where everything takes place
   */
  private static Stage stage;


  // Different Scenes that can be shown on the Stage and their corresponding root Panes and controllers.
  /**
   * Scene for the start
   */
  private static Scene start;

  /**
   * root corresponding to the start scene
   */
  private static Pane startRoot;

  /**
   * the startcontroller
   */
  private static StartController startController;

  /**
   * Scene for the menu
   */
  private static Scene menu;

  /**
   * corresponding root to the menu scene
   */
  private static Pane menuRoot;

  /**
   * the menucontroller
   */
  private static MenuController menuController;

  /**
   * the corresponding root to the game scene
   */
  private static Pane gameRoot;

  /**
   * Scene for the game
   */
  private static Scene game;

  /**
   * the gamecontroller
   */
  private static GameController gameController;

  /**
   * Scene for the highscore
   */
  private static Scene highscore;

  /**
   * corresponding root for the highscore scene
   */
  private static Pane highscoreRoot;

  /**
   * the highscorecontroller
   */
  private static HighscoreController highscoreController;

  /**
   * the char selection controller
   */
  private static CharSelectionController charSelectionController;

  /**
   * corresponding root for the char selection scene
   */
  private static Pane charSelectionRoot;

  /**
   * the char selection scene
   */
  public static Scene charSelection;

  private static Pane notInLobbyRoot;
  public static Scene notInLobby;
  private static NotInLobbyController notInLobbyController;

  private static Pane nameSelectionRoot;
  public static Scene nameSelection;
  private static NameSelectionController nameSelectionController;

  /**
   * the logger
   */
  private static final Logger logger = LogManager.getLogger(Main.class);

  /**
   * sets the .css file to be used for the scenes
   */
  private static final String CSS = "TheStudentGameLook.css";

  static SendToServer sendToServer = new SendToServer();


  /**
   * preloads all necessary scenes and displays the start scene on the stage.
   *
   * @param stage Stage
   */
  @Override
  public void start(Stage stage) {
    try {
      stage.setTitle("Welcome to the Student Games");
      stage.setOnCloseRequest(e -> exit());
      Main.stage = stage;

      startRoot = getLoader("fxml_start.fxml").load();
      // startController = getLoader("fxml_start.fxml").getController();
      start = createScene(startRoot);

      menuRoot = getLoader("fxml_menu.fxml").load();
      // menuController = getLoader("fxml_menu.fxml").getController();
      menu = createScene(menuRoot);

      highscoreRoot = getLoader("fxml_highscore.fxml").load();
      // highscoreController = getLoader("fxml_highscore.fxml").getController();
      highscore = createScene(highscoreRoot);

      gameRoot = getLoader("fxml_game.fxml").load();
      //gameController = getLoader("fxml_game.fxml").getController();
      game = createScene(gameRoot);

      charSelectionRoot = getLoader("fxml_charSelection.fxml").load();
      charSelection = createScene(charSelectionRoot);

      notInLobbyRoot = getLoader("fxml_notInLobby.fxml").load();
      notInLobby = createScene(notInLobbyRoot);

      nameSelectionRoot = getLoader("fxml_nameSelection.fxml").load();
      nameSelection = createScene(nameSelectionRoot);


      displayStart(); // Display the first Scene.
      stage.centerOnScreen();

      Starter.connect(); // Tells the Starter that the gui is ready and the client can connect to the server

    } catch (Exception e) {
      logger.error("Failed to load scenes", e);
      exit();
    }
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
   * Displays the Game scene on stage.
   */
  public static void displayGame() {
    showScene(game, gameRoot);
    gameController.resetGame();
  }

  /**
   * Displays the charselection scene on stage
   */
  public static void displayCharSelectionPopUp() { showPopUp(charSelection); }

  /**
   * Display the Highscore scene on stage.
   */
  public static void displayHighscore() {
    showScene(highscore, highscoreRoot);
  }

  public static void displayNotInLobbyPopUp() { showPopUp(notInLobby); }

  public static void displayNameSelectionPopUp() {showPopUp(nameSelection); }

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
    //letterbox(scene, pane);
  }

  /**
   * creates a popup of the given scene
   *
   * @param scene the scene you want displayed as a popup
   */
  private static void showPopUp(Scene scene) {
    Stage popupWindow = new Stage();
    popupWindow.initModality(Modality.APPLICATION_MODAL);
    popupWindow.setScene(scene);
    popupWindow.showAndWait();
  }

  /**
   * Returns the root pane of the given fxml file.
   * If the fxml file is not found or could not be loaded the program will close and the error is logged.
   *
   * @param fxml String with the name of the fxml file
   * @return the root pane of the given fxml file
   */
  private static FXMLLoader getLoader(String fxml) throws NullPointerException {
    return new FXMLLoader(Objects.requireNonNull(Main.class.getClassLoader().getResource(fxml)));
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
  public static void exit() {
    try {
      SendToServer sendToServer = new SendToServer();
      sendToServer.send(CommandsToServer.QUIT, "");
    } catch (NullPointerException e) {
      logger.info("closed before connection was made");
      Platform.exit();
    }
  }


  // --------------------------- GETTERS AND SETTERS -------------------------------------- //

  /**
   * returns the clients StartController
   *
   * @return StartController
   */
  public static StartController getStartController() {
    return startController;
  }

  /**
   * returns the clients CharSelectionController
   *
   * @return CharSelectionController
   */
  public static CharSelectionController getCharSelectionController() { return charSelectionController; }

  /**
   * returns the clients MenuController
   *
   * @return MenuController
   */
  public static MenuController getMenuController() {
    return menuController;
  }

  /**
   * returns the clients GameController
   *
   * @return GameController
   */
  public static GameController getGameController() {
    return gameController;
  }

  /**
   * returns the clients HighscoreController
   *
   * @return HighscoreController
   */
  public static HighscoreController getHighscoreController() {
    return highscoreController;
  }

  public static NotInLobbyController getNotInLobbyController() { return notInLobbyController; }

  public static NameSelectionController getNameSelectionController() { return nameSelectionController; }

  /**
   * setts the clients GameController
   *
   * @param gameController GameController
   */
  public static void setGameController(GameController gameController) {
    Main.gameController = gameController;
  }

  /**
   * sets the clients StartController
   *
   * @param startController StartController
   */
  public static void setStartController(StartController startController) {
    Main.startController = startController;
  }

  /**
   * sets the clients MenuController
   *
   * @param menuController MenuController
   */
  public static void setMenuController(MenuController menuController) {
    Main.menuController = menuController;
  }

  /**
   * sets the clients HighscoreController
   *
   * @param highscoreController HighscoreController
   */
  public static void setHighscoreController(HighscoreController highscoreController) {
    Main.highscoreController = highscoreController;
  }

  /**
   * sets the clients CharSelectionController
   *
   * @param charSelectionController CharSelectionController
   */
  public static void setCharSelectionController(CharSelectionController charSelectionController) {
    Main.charSelectionController = charSelectionController;
  }

  public static void setNotInLobbyController(NotInLobbyController notInLobbyController) {
    Main.notInLobbyController = notInLobbyController;
  }

  public static void setNameSelectionController(NameSelectionController nameSelectionController) {
    Main.nameSelectionController = nameSelectionController;
  }
}

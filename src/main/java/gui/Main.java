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
   * Scene for the Login
   */
  private static Scene login;

  /**
   * root corresponding to the login scene
   */
  private static Pane loginRoot;

  /**
   * the logincontroller
   */
  private static LoginController loginController;
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
   * corresponding root to the help scene
   */
  private static Pane helpRoot;

  /**
   * the helpcontroller
   */
  private static HelpController helpController;

  /**
   * the help scene
   */
  private static Scene help;

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
   * the introcontroller
   */
  private static IntroController introController;

  /**
   * the intro scene
   */
  private static Scene intro;

  /**
   * the corresponding root to the intro scene
   */
  private static Pane introRoot;

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
   * the char selection scene
   */
  public static Scene charSelection;

  /**
   * the notInLobby scene
   */
  private static Scene notInLobby;

  /**
   * the notInLobbyController
   */
  private static NotInLobbyController notInLobbyController;

  /**
   * the name selection scene
   */
  private static Scene nameSelection;

  /**
   * the nameselectioncontroller
   */
  private static NameSelectionController nameSelectionController;

  /**
   * the quitConfirmation scene
   */
  private static Scene quitConfirmation;

  /**
   * the quitConfirmationController
   */
  private static QuitConfirmController quitConfirmController;

  /**
   * the corresponding quitConfirmation root
   */
  private static Pane quitConfirmationRoot;

  /**
   * the menuconfirmation scene
   */
  private static Scene menuConfirmation;

  /**
   * the corresponding menuconfirmation root
   */
  private static Pane menuConfirmationRoot;

  /**
   * the menuConfirmController
   */
  private static MenuConfirmController menuConfirmController;

  /**
   * the logger
   */
  private static final Logger logger = LogManager.getLogger(Main.class);

  /**
   * sets the .css file to be used for the scenes
   */
  private static final String CSS = "TheStudentGameLook.css";

  /**
   * the SendToServer object used to communicate with the server
   */
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

      loginRoot = getLoader("fxml_login.fxml").load();
      login = createScene(loginRoot);

      startRoot = getLoader("fxml_start.fxml").load();
      start = createScene(startRoot);

      menuRoot = getLoader("fxml_menu.fxml").load();
      menu = createScene(menuRoot);

      highscoreRoot = getLoader("fxml_highscore.fxml").load();
      highscore = createScene(highscoreRoot);

      gameRoot = getLoader("fxml_game.fxml").load();
      game = createScene(gameRoot);

      Pane charSelectionRoot = getLoader("fxml_charSelection.fxml").load();
      charSelection = createScene(charSelectionRoot);

      Pane notInLobbyRoot = getLoader("fxml_notInLobby.fxml").load();
      notInLobby = createScene(notInLobbyRoot);

      Pane nameSelectionRoot = getLoader("fxml_nameSelection.fxml").load();
      nameSelection = createScene(nameSelectionRoot);

      helpRoot = getLoader("fxml_help.fxml").load();
      help = createScene(helpRoot);

      introRoot = getLoader("fxml_intro.fxml").load();
      intro = createScene(introRoot);

      quitConfirmationRoot = getLoader("fxml_quitConfirm.fxml").load();
      quitConfirmation = createScene(quitConfirmationRoot);

      menuConfirmationRoot = getLoader("fxml_menuConfirm.fxml").load();
      menuConfirmation = createScene(menuConfirmationRoot);


      if (Starter.isArgumentStart) {
        displayIntro();
      } else {
        displayLogin();
      }
      stage.centerOnScreen();

    } catch (Exception e) {
      logger.error("Failed to load scenes", e);
      exit();
    }
  }

  /**
   * displays the login scene on stage
   */
  public static void displayLogin() {
    showScene(login, loginRoot);
  }

  /**
   * Display the Menu scene on stage.
   */
  public static void displayMenu() {
    getMenuController().refreshLobbies();
    getMenuController().refreshFriends();
    showScene(menu, menuRoot);
  }

  /**
   * Display the Start scene on stage.
   */
  public static void displayStart() {
    showScene(start, startRoot);
    Starter.connect();
  }

  /**
   * Displays the Game scene on stage.
   */
  public static void displayGame() {
    showScene(game, gameRoot);
    gameController.resetGame();
    sendToServer.send(CommandsToServer.SETALLCHARTOKENS, "");
  }

  /**
   * Displays the charselection scene on stage
   */
  public static void displayCharSelectionPopUp() { showPopUp(charSelection); }

  /**
   * displays the intro scene on stage
   */
  public static void displayIntro() {
    showScene(intro, introRoot);
    getIntroController().playMedia();
  }

  /**
   * Display the Highscore scene on stage.
   */
  public static void displayHighscore() {
    showPopUp(highscore);
    getHighscoreController().refreshWinners();
  }

  /**
   * displays the notInLobby popup
   */
  public static void displayNotInLobbyPopUp() { showPopUp(notInLobby); }

  /**
   * displays the name selection popup
   */
  public static void displayNameSelectionPopUp() { showPopUp(nameSelection); }

  /**
   * displays the help pop up
   */
  public static void displayHelpPopUp() { showPopUp(help); }

  /**
   * displays the quitConfirmation popup
   */
  public static void displayQuitConfirmationPopUp() { showPopUp(quitConfirmation); }

  /**
   * displays the menuConfirmation popup
   */
  public static void displayMenuConfirmationPopUp() { showPopUp(menuConfirmation); }

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
      sendToServer.send(CommandsToServer.PRINTLOUNGINGLIST, "");
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
   * returns the clients IntroController
   * @return IntroController
   */
  public static IntroController getIntroController() { return introController; }

  /**
   * returns the clients MenuController
   *
   * @return MenuController
   */
  public static MenuController getMenuController() {
    return menuController;
  }

  /**
   * returns the clients HelpController
   * @return HelpController
   */
  public static HelpController getHelpController() { return helpController; }

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

  /**
   * returns the clients QuitConfirmationController
   *
   * @return QuitConfirmController
   */
  public static QuitConfirmController getQuitConfirmController() { return quitConfirmController; }

  /**
   * returns the clients MenuConfirmController
   *
   * @return MenuConfirmController
   */
  public static MenuConfirmController getMenuConfirmController() { return menuConfirmController; }

  /**
   * setts the clients GameController
   *
   * @param gameController GameController
   */
  public static void setGameController(GameController gameController) {
    Main.gameController = gameController;
  }

  /**
   * sets the clients IntroController
   * @param introController IntroController
   */
  public static void setIntroController(IntroController introController) { Main.introController = introController; }

  /**
   * sets the clients HelpController
   * @param helpController HelpController
   */
  public static void setHelpController(HelpController helpController) { Main.helpController = helpController; }

  /**
   * sets the clients StartController
   *
   * @param startController StartController
   */
  public static void setStartController(StartController startController) {
    Main.startController = startController;
  }

  /**
   * sets the clients LoginController
   * @param loginController LoginController
   */
  public static void setLoginController(LoginController loginController) {
    Main.loginController = loginController;
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
   * sets the clients QuitConfirmController
   * @param quitConfirmController QuitConfirmController
   */
  public static void setQuitConfirmController(QuitConfirmController quitConfirmController) { Main.quitConfirmController = quitConfirmController; }

  /**
   * sets the clients MenuConfirmController
   * @param menuConfirmController MenuConfirmController
   */
  public static void setMenuConfirmController(MenuConfirmController menuConfirmController) { Main.menuConfirmController = menuConfirmController; }

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

}

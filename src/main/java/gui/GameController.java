package gui;

import javafx.animation.Animation;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.util.Duration;
import utility.io.CommandsToServer;
import utility.io.SendToServer;

import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;


/**
 * This is the controller for game.fxml
 */

public class GameController implements Initializable {

  /**
   * SendToServer object to communicate with the server
   */
  private final SendToServer sendToServer = new SendToServer();

  /**
   * the image used as a send "button"
   */
  @FXML
  private ImageView sendbutton;

  /**
   * the image used as the quiz c "button"
   */
  @FXML
  private ImageView quizC;

  /**
   * the image used as the quiz b "button"
   */
  @FXML
  private ImageView quizB;

  /**
   * the image used as the quiz a "button"
   */
  @FXML
  private ImageView quizA;

  /**
   * the image used as the quiz d "button"
   */
  @FXML
  private ImageView quizD;

  /**
   * the image used as the quit "button"
   */
  @FXML
  private ImageView quitbutton;

  /**
   * the image used as the help "button"
   */
  @FXML
  private ImageView helpbutton;

  /**
   * the image used as the start "button"
   */
  @FXML
  private ImageView startbutton;

  /**
   * the image used as the highscore "button"
   */
  @FXML
  private ImageView highscorebutton;

  /**
   * the image used as the menu "button"
   */
  @FXML
  private ImageView menubutton;

  /**
   * the image used as the "button" to change to global chat
   */
  @FXML
  private ImageView globalbuttonimage;

  /**
   * the image used as the "button" to change to lobby chat
   */
  @FXML
  private ImageView lobbybuttonimage;

  /**
   * the rectangle used as the regular dice (6 sides)
   */
  @FXML
  private ImageView regularDice;

  /**
   * marks whether a player wants to write in the global chat or not
   */
  private boolean writeInGlobalChat = false;

  /**
   * the opacity of the selected chat button (lobby or global)
   */
  private final double selectedOpacity = 1;

  /**
   * the opacity of the chat button (lobal or globby) which is not selected
   */
  private final double unselectedOpacity = 0.5;

  /**
   * boolean property which checks if the player has to answer a quiz question
   */
  private final SimpleBooleanProperty isMyQuiz = new SimpleBooleanProperty(false);

  /**
   * boolean property which checks if it's the players turn
   */
  private final SimpleBooleanProperty isMyTurn = new SimpleBooleanProperty(false);

  /**
   * boolean that is true when the game has started and false when its finished
   */
  private boolean hasStarted = false;


  /**
   * all fields of the board in a HashMap
   */
  private static final HashMap<Integer, Integer[]> fields = new HashMap<>();

  /**
   * the label used to mark the users playername on screen
   */
  @FXML
  private Label namelabel;

  /**
   * a TextField to enter chat messages
   */
  @FXML
  private TextField chatTextField;

  /**
   * a TextArea which displays sent chat messages
   */
  @FXML
  private TextArea chat;

  /**
   * Polygon which represents the first dicedice
   */
  @FXML
  private ImageView fourDice1;

  /**
   * Polygon which represents the second dicedice
   */
  @FXML
  private ImageView fourDice2;

  /**
   * Polygon which represents the third dicedice
   */
  @FXML
  private ImageView fourDice3;

  /**
   * TextArea used to display what happens in the game
   */
  @FXML
  private TextArea gameTracker;

  /**
   * a blue circle which represents a player character
   */
  @FXML
  private Circle tokenOne;

  /**
   * a red circle which represents a player character
   */
  @FXML
  private Circle tokenTwo;

  /**
   * a yellow circle which represents a player character
   */
  @FXML
  private Circle tokenThree;

  /**
   * a green circle which represents a player character
   */
  @FXML
  private Circle tokenFour;

  /**
   * the image used as the ready "button"
   */
  @FXML
  private ImageView readyButton;

  /**
   * GridPane which is used as the board of the game
   */
  @FXML
  private GridPane board;

  /**
   * the TranslateTransition which is used to animate the movements of token one
   */
  TranslateTransition transitionTokenOne = new TranslateTransition();

  /**
   * the TranslateTransition which is used to animate the movements of token two
   */
  TranslateTransition transitionTokenTwo = new TranslateTransition();

  /**
   * the TranslateTransition which is used to animate the movements of token three
   */
  TranslateTransition transitionTokenThree = new TranslateTransition();

  /**
   * the TranslateTransition which is used to animate the movements of token four
   */
  TranslateTransition transitionTokenFour = new TranslateTransition();

  /**
   * the TranslateTransition which is used to animage the ready button/image
   */
  TranslateTransition transitionReadyButton = new TranslateTransition();

  /**
   * This method runs, when the class is created
   * The scene resets, the "board" is created and the images are added to the playing characters
   *
   * @param location  URL
   * @param resources ResourceBundle
   */
  @Override
  public void initialize(URL location, ResourceBundle resources) {

    resetGame();
    createPlayingField();
    Main.setGameController(this);

    transitionReadyButton.setNode(readyButton);
    transitionReadyButton.setDuration(Duration.millis(600));
    transitionReadyButton.setToY(3);
    transitionReadyButton.setToX(3);
    transitionReadyButton.setToX(0.1);

    transitionReadyButton.setCycleCount(Animation.INDEFINITE);
    transitionReadyButton.play();

    transitionTokenOne.setNode(tokenOne);
    transitionTokenOne.setDuration(Duration.seconds(1));

    transitionTokenTwo.setNode(tokenTwo);
    transitionTokenTwo.setDuration(Duration.seconds(1));

    transitionTokenThree.setNode(tokenThree);
    transitionTokenThree.setDuration(Duration.seconds(1));

    transitionTokenFour.setNode(tokenFour);
    transitionTokenFour.setDuration(Duration.seconds(1));

    MyTurnListener();
    MyQuizListener();

  }

  // -------------------------------------- CHAT ------------------------------------------- //

  /**
   * this method is used to print game updates to the game tracker in the GUI
   *
   * @param gameMove String
   */
  public void printGameUpdate(String gameMove) {
    if (gameMove != null) {
      String[] splitted = gameMove.split("??");
      Platform.runLater(() -> {
        for (String s : splitted) {
          gameTracker.appendText(s);
          gameTracker.appendText("\n");
        }
      });
      if (gameMove.contains("graduated")) {
        Platform.runLater(() -> {
          menubutton.setOpacity(selectedOpacity);
          menubutton.setDisable(false);
          hasStarted = false;
        });
      }
      if (!hasStarted && gameMove.contains("roll the Dice")) {
        menubutton.setOpacity(unselectedOpacity);
        readyButton.setOpacity(unselectedOpacity);
        startbutton.setOpacity(unselectedOpacity);
        startbutton.setDisable(true);
        hasStarted = true;
      }
    }
  }

  /**
   * method reads input from the Textfield and checks, which command to send to the server
   * if there's no command at the start of the message, it will be sent as a chat (which is the main use for this GUI)
   */
  @FXML
  void sendChatMessage() {
    String msg = (chatTextField.getText());

    if (msg.startsWith("/nick")) {
      String[] split = msg.split(" ", 2);
      if (split[1].length() > 0) {
        String newName = split[1];
        sendToServer.send(CommandsToServer.NICK, newName);
      }
    } else if (msg.startsWith("/winnerwinnerchickendinner")) {
      String[] input = msg.split(" ", 2);
      if (input[1].length() > 0) {
        sendToServer.send(CommandsToServer.WWCD, input[1]);
      }
    } else if (msg.startsWith("skandal")) {
      String[] input = msg.split(" ", 2);
      if (input[1].length() > 0 && Integer.parseInt(input[1]) <= 6 && Integer.parseInt(input[1]) >= 1) {
        sendToServer.send(CommandsToServer.SKANDAL, input[1]);
      }
    } else if (msg.startsWith("/whisper")) {
      String[] split = msg.split(" ", 3);
      if (split.length > 1) {
        if (split[1].length() > 0 && split[2].length() > 0) {
          String whisperMsg = split[1] + "-" + split[2];
          sendToServer.send(CommandsToServer.WHISPER, whisperMsg);
        }
      } else {
        chat.appendText("You cannot whisper nothing!");
      }
    } else if (writeInGlobalChat) {
      sendToServer.send(CommandsToServer.CHAT, msg);
    } else {
      sendToServer.send(CommandsToServer.LOBBYCHAT, msg);
    }
    chatTextField.clear();
  }


  /**
   * method is used to print messages to the chat
   *
   * @param msg message which will be printed in the chat
   */
  @FXML
  public void printChatMessage(String msg) {
    if (msg != null) {
      Platform.runLater(() -> chat.appendText(msg + System.lineSeparator()));
    }
  }


  /**
   * This method is called when one of the togglebuttons is pressed
   * The method sets the variable writeInGlobalChat, which is used to determine if
   * a message is sent in the global chat or the lobby chat
   *
   * @param actionEvent actionEvent
   */
  @FXML
  private void switchChat(MouseEvent actionEvent) {
    if (actionEvent.getSource() == globalbuttonimage) {
      writeInGlobalChat = true;
      globalbuttonimage.setOpacity(selectedOpacity);
      globalbuttonimage.setDisable(true);
      lobbybuttonimage.setOpacity(unselectedOpacity);
      lobbybuttonimage.setDisable(false);
    } else {
      writeInGlobalChat = false;
      globalbuttonimage.setOpacity(unselectedOpacity);
      globalbuttonimage.setDisable(false);
      lobbybuttonimage.setOpacity(selectedOpacity);
      lobbybuttonimage.setDisable(true);
    }
  }


  // -------------------------------------- DICES ------------------------------------------ //

  /**
   * throws a dicedice when clicking on one of the four-dices
   */
  public void throwFourDice() {
    sendToServer.send(CommandsToServer.DICEDICE, "");
    isMyTurn.set(false);
  }


  /**
   * checks how many dicedice the player has left and adjust how the GUI shows them
   * used up dicedice have a lower opacity and are disabled
   *
   * @param diceLeft String
   */
  public void checkFourDiceLeft(String diceLeft) {
    Platform.runLater(() -> {
      // disables already used dicedices
      switch (diceLeft) {
        case "3":
          fourDice1.setDisable(false);
          fourDice1.setOpacity(1);
          fourDice2.setDisable(false);
          fourDice2.setOpacity(1);
          fourDice3.setDisable(false);
          fourDice3.setOpacity(1);
          break;
        case "2":
          fourDice1.setDisable(false);
          fourDice1.setOpacity(1);
          fourDice2.setDisable(false);
          fourDice2.setOpacity(1);
          fourDice3.setDisable(true);
          fourDice3.setOpacity(0.2);
          break;
        case "1":
          fourDice1.setDisable(false);
          fourDice1.setOpacity(1);
          fourDice2.setDisable(true);
          fourDice2.setOpacity(0.2);
          fourDice3.setDisable(true);
          fourDice3.setOpacity(0.2);
          break;
        default:
          fourDice1.setDisable(true);
          fourDice1.setOpacity(0.2);
          fourDice2.setDisable(true);
          fourDice2.setOpacity(0.2);
          fourDice3.setDisable(true);
          fourDice3.setOpacity(0.2);
          break;

      }
    });
  }

  /**
   * method throws a regular dice
   */
  public void throwRegularDice() {
    sendToServer.send(CommandsToServer.ROLLDICE, null);
    isMyTurn.set(false);
  }

  // ---------------------------------- PLAYER MOVEMENT ------------------------------------ //

  /**
   * This method determines which player needs to be moved on the GUI and on which field they now belong
   *
   * @param moveToField String which contains the color of the player and the field they move to
   * @return returns the correct player character to move around (which are circles)
   */
  public Circle chooseCorrectPlayerToMove(String moveToField) {
    String[] playerAndNewField = moveToField.split("--"); // splits the String into the nr of the new field and the player color
    String playerTokenNrToMove = playerAndNewField[0];

    switch (playerTokenNrToMove) {
      case "1":
        return tokenOne;
      case "2":
        return tokenTwo;
      case "3":
        return tokenThree;
      default:
        return tokenFour;
    }
  }

  /**
   * chooses the correct TranslateTransition so the correct player moves
   *
   * @param moveToField String containing the player token number and the new field to move to
   * @return TranslateTransition of the player who should move
   */
  private TranslateTransition chooseCorrectPathTransition(String moveToField) {
    String[] playerAndNewField = moveToField.split("--"); // splits the String into the nr of the new field and the player color
    String playerTokenNrToMove = playerAndNewField[0];

    switch (playerTokenNrToMove) {
      case "1":
        return transitionTokenOne;
      case "2":
        return transitionTokenTwo;
      case "3":
        return transitionTokenThree;
      default:
        return transitionTokenFour;
    }
  }


  /**
   * This method is used to get the x- and y-coordinate of a field on the board
   * To find a field you need to know the row and column number
   *
   * @param board GridPane: represents the board over which the player characters move
   * @param col   int: column of the field you want to search
   * @param row   int: row of the field you want to search
   * @return double[] containing the x- and y-coordinate of the field in
   */
  private double[] getPosFromGridPane(GridPane board, int col, int row) {
    double xPos = 0.0;
    double yPos = 0.0;
    ObservableList<Node> children = board.getChildren();
    for (Node node : children) {
      if (GridPane.getColumnIndex(node) != null && GridPane.getRowIndex(node) != null) {
        if (GridPane.getColumnIndex(node) == col && GridPane.getRowIndex(node) == row) {
          xPos = node.getBoundsInParent().getCenterX();
          yPos = node.getBoundsInParent().getCenterY();

          break;
        }
      }
    }

    return new double[]{xPos, yPos};

  }

  /**
   * Moves a character to a different field
   * This is done in three steps:
   * 1. find the correct player to move
   * 2. get the coordinates of the new field
   * 3. moves the player to the new coordinates
   *
   * @param moveToField String: contains the color of the character to move and on which field he belongs
   */
  public void movePlayer(String moveToField) {
    Circle playerToMove = chooseCorrectPlayerToMove(moveToField);
    TranslateTransition transition = chooseCorrectPathTransition(moveToField);

    // get the coordinates of the new field
    String[] playerAndNewField = moveToField.split("--");
    int newFieldToMoveTo = Integer.parseInt(playerAndNewField[1]);
    Integer[] columnAndRow = fields.get(newFieldToMoveTo);
    double[] newPos = getPosFromGridPane(board, columnAndRow[0], columnAndRow[1]);


    // moves the player to the new coordinates
    Platform.runLater(() -> {
      double currentX = playerToMove.getTranslateX();
      double currentY = playerToMove.getTranslateY();
      double newX = newPos[0] - 25;
      double newY = newPos[1] - 35;

      Line lineToMoveAlong = new Line(currentX, currentY, newX, newY);

      transition.setToX(newX);
      transition.setToY(newY);

      // transition.setCycleCount(1);
      transition.play();
      playerToMove.setTranslateX(newPos[0] - 25);
      playerToMove.setTranslateY(newPos[1] - 30);
    });
  }

  // ---------------------------------- QUIZ ------------------------------------ //

  /**
   * answers a quiz question with A when pressing the button
   */
  @FXML
  private void answerQuizA() {
    sendToServer.send(CommandsToServer.QUIZ, "A");
    isMyQuiz.set(false);
  }

  /**
   * answers a quiz question with B when pressing the button
   */
  @FXML
  private void answerQuizB() {
    sendToServer.send(CommandsToServer.QUIZ, "B");
    isMyQuiz.set(false);
  }

  /**
   * answers a quiz question with C when pressing the button
   */
  @FXML
  private void answerQuizC() {
    sendToServer.send(CommandsToServer.QUIZ, "C");
    isMyQuiz.set(false);
  }

  /**
   * answers a quiz question with D when pressing the button
   */
  @FXML
  private void answerQuizD() {
    sendToServer.send(CommandsToServer.QUIZ, "D");
    isMyQuiz.set(false);
  }

  // --------------------------------- TOKENS ----------------------------------- //

  /**
   * adds the correct picture to the player tokens
   *
   * @param tokenNr int of the token number
   * @param charNr  int of the char number
   */
  public void setCharToken(int tokenNr, int charNr) {
    if (tokenNr == 0) {
      return;
    }

    Image img = new Image(findCorrectImag(charNr), false);
    if (tokenNr == 1) {
      tokenOne.setFill(new ImagePattern(img));
      tokenOne.setOpacity(1);
      tokenOne.setId("char" + (charNr));
    } else if (tokenNr == 2) {
      tokenTwo.setFill(new ImagePattern(img));
      tokenTwo.setOpacity(1);
      tokenTwo.setId("char" + (charNr));
    } else if (tokenNr == 3) {
      tokenThree.setFill(new ImagePattern(img));
      tokenThree.setOpacity(1);
      tokenThree.setId("char" + (charNr));
    } else if (tokenNr == 4) {
      tokenFour.setFill(new ImagePattern(img));
      tokenFour.setOpacity(1);
      tokenFour.setId("char" + (charNr));
    }

  }

  /**
   * marks the player in the gui whose turn it is
   *
   * @param tokenNr String containing the number of the token which should be marked
   */
  public void markPlayer(String tokenNr) {

    tokenOne.setStroke(Color.BLACK);
    tokenOne.setStrokeWidth(1.0);

    tokenTwo.setStroke(Color.BLACK);
    tokenTwo.setStrokeWidth(1.0);

    tokenThree.setStroke(Color.BLACK);
    tokenThree.setStrokeWidth(1.0);

    tokenFour.setStroke(Color.BLACK);
    tokenFour.setStrokeWidth(1.0);
    switch (tokenNr) {
      case ("1"):
        tokenOne.setStroke(Color.DARKORANGE);
        tokenOne.setStrokeWidth(3.0);
        break;
      case ("2"):
        tokenTwo.setStroke(Color.DARKORANGE);
        tokenTwo.setStrokeWidth(3.0);
        break;
      case ("3"):
        tokenThree.setStroke(Color.DARKORANGE);
        tokenThree.setStrokeWidth(3.0);
        break;
      case ("4"):
        tokenFour.setStroke(Color.DARKORANGE);
        tokenFour.setStrokeWidth(3.0);
        break;
      default:
        break;
    }
  }


// --------------------------------- CHAR IMAGES ----------------------------------- //

  /**
   * finds the correct image when given a character number
   *
   * @param charNr the character number you want the image of
   * @return returns the path of the wanted image as a String
   */
  public String findCorrectImag(int charNr) {
    switch (charNr) {
      case 0:
        return "char0.png";
      case 1:
        return "char1.png";
      case 2:
        return "char2.png";
      case 3:
        return "char3.png";
      case 4:
        return "char4.png";
      case 5:
        return "char5.png";
      case 6:
        return "char6.png";
      default:
        return "";
    }
  }

  /**
   * shows the correct hover image of the token when hovering (entering the circle which represents the token)
   *
   * @param token Circle which represents the player token which is being hovered on
   * @param id    String containing the id of the circle being hovered on
   */
  void showHoverImage(Circle token, String id) {
    switch (id) {
      case "char1":
        token.setFill(new ImagePattern(new Image("char1hover.png")));

        break;

      case "char2":
        token.setFill(new ImagePattern(new Image("char2hover.png")));
        break;

      case "char3":
        token.setFill(new ImagePattern(new Image("char3hover.png")));
        break;

      case "char4":
        token.setFill(new ImagePattern(new Image("char4hover.png")));
        break;

      case "char5":
        token.setFill(new ImagePattern(new Image("char5hover.png")));
        break;

      case "char6":
        token.setFill(new ImagePattern(new Image("char6hover.png")));
        break;
    }
  }

  /**
   * shows the correct regular image of the token when stopping hovering (exiting the circle which represents the token)
   *
   * @param token Circle which represents the player token which is not being hovered on anymore
   * @param id    String containing the id of the circle which is not hovered on anymore
   */
  void showRegularImage(Circle token, String id) {
    switch (id) {
      case "char1":
        token.setFill(new ImagePattern(new Image("char1.png")));
        break;

      case "char2":
        token.setFill(new ImagePattern(new Image("char2.png")));
        break;

      case "char3":
        token.setFill(new ImagePattern(new Image("char3.png")));
        break;

      case "char4":
        token.setFill(new ImagePattern(new Image("char4.png")));
        break;

      case "char5":
        token.setFill(new ImagePattern(new Image("char5.png")));
        break;

      case "char6":
        token.setFill(new ImagePattern(new Image("char6.png")));
        break;
    }
  }

  // --------------------------------- CHAR HOVERS ----------------------------------- //

  /**
   * changes the character images when hovering over token one
   */
  @FXML
  private void tokenOneHover() {
    showHoverImage(tokenOne, tokenOne.getId());
  }

  /**
   * changes the character image back when stopping hovering over token one
   */
  @FXML
  private void tokenOneHoverEnd() {
    showRegularImage(tokenOne, tokenOne.getId());
  }

  /**
   * changes the character images when hovering over token two
   */
  @FXML
  private void tokenTwoHover() {
    showHoverImage(tokenTwo, tokenTwo.getId());
  }

  /**
   * changes the character image back when stopping hovering over token two
   */
  @FXML
  private void tokenTwoHoverEnd() {
    showRegularImage(tokenTwo, tokenTwo.getId());
  }

  /**
   * changes the character images when hovering over token three
   */
  @FXML
  private void tokenThreeHover() {
    showHoverImage(tokenThree, tokenThree.getId());
  }

  /**
   * changes the character image back when stopping hovering over token three
   */
  @FXML
  private void tokenThreeHoverEnd() {
    showRegularImage(tokenThree, tokenThree.getId());
  }

  /**
   * changes the character images when hovering over token four
   */
  @FXML
  private void tokenFourHover() {
    showHoverImage(tokenFour, tokenFour.getId());
  }

  /**
   * changes the character image back when stopping hovering over token four
   */
  @FXML
  private void tokenFourHoverEnd() {
    showRegularImage(tokenFour, tokenFour.getId());
  }

  // --------------------------------- SCENE SWITCHERS----------------------------------- //

  /**
   * opens the help popup which contains the manual
   */
  @FXML
  private void openHelp() {
    Main.displayHelpPopUp();
  }

  /**
   * used to switch to the Menu scene
   */
  @FXML
  private void switchToMenu() {
    Main.displayMenuConfirmationPopUp();
  }

  /**
   * used to switch to the Highscore scene
   */
  @FXML
  private void switchToHighscore() {
    Main.displayHighscore();
  }

  /**
   * opens the quit confirmation popup
   */
  @FXML
  private void quitGame() {
    Main.displayQuitConfirmationPopUp();
  }

  // --------------------------------- GAME ----------------------------------- //

  /**
   * creates the Hashmap which contains the row and column of each field on the board
   */
  private void createPlayingField() {
    Platform.runLater(() -> {
      int counter = 0;
      for (int y = 8; y >= 0; y--) {
        if (y % 2 == 0) {
          for (int x = 0; x <= 9; x++) {
            fields.put(counter, new Integer[]{x, y});
            counter += 1;
          }
        } else {
          for (int x = 9; x >= 0; x--) {
            fields.put(counter, new Integer[]{x, y});
            counter += 1;
          }
        }
      }
    });
  }

  /**
   * sets the player as ready when pressing the ready button
   * pressing it again will make the player unready
   */
  @FXML
  private void setPlayerAsReady() {
    sendToServer.send(CommandsToServer.READY, "");
    readyButton.setOpacity(unselectedOpacity);
    readyButton.setDisable(true);
    startbutton.setOpacity(selectedOpacity);
    startbutton.setDisable(false);
    transitionReadyButton.stop();
  }

  /**
   * starts the game by pressing on the start button
   */
  @FXML
  private void startTheGame() {
    alternateResetGame();
    sendToServer.send(CommandsToServer.START, null);
    Platform.runLater(() -> {
      readyButton.setOpacity(unselectedOpacity);
      readyButton.setDisable(true);
    });
  }

  /**
   * This method is used to reset the game at the start of it
   * the player characters are moved to the starting position and all dicedice are given back to the player
   */
  public void resetGame() {
    Platform.runLater(() -> {
      tokenOne.setTranslateY(524);
      tokenTwo.setTranslateY(524);
      tokenThree.setTranslateY(524);
      tokenFour.setTranslateY(524);

      tokenOne.setTranslateX(890);
      tokenTwo.setTranslateX(970);
      tokenThree.setTranslateX(810);
      tokenFour.setTranslateX(730);

      // resets the dicedice
      fourDice1.setDisable(false);
      fourDice2.setDisable(false);
      fourDice3.setDisable(false);
      fourDice1.setOpacity(1);
      fourDice2.setOpacity(1);
      fourDice3.setOpacity(1);

      readyButton.setOpacity(selectedOpacity);
      readyButton.setDisable(false);
      menubutton.setOpacity(selectedOpacity);
      startbutton.setOpacity(unselectedOpacity);
      startbutton.setDisable(true);
    });
  }

  /**
   * This method is used to reset the game at the start of it
   * the player characters are moved to the starting position and all dicedice are given back to the player
   */
  public void alternateResetGame() {
    Platform.runLater(() -> {
      tokenOne.setTranslateY(524);
      tokenTwo.setTranslateY(524);
      tokenThree.setTranslateY(524);
      tokenFour.setTranslateY(524);

      tokenOne.setTranslateX(890);
      tokenTwo.setTranslateX(970);
      tokenThree.setTranslateX(810);
      tokenFour.setTranslateX(730);

      // resets the dicedice
      fourDice1.setDisable(false);
      fourDice2.setDisable(false);
      fourDice3.setDisable(false);
      fourDice1.setOpacity(1);
      fourDice2.setOpacity(1);
      fourDice3.setOpacity(1);

      readyButton.setOpacity(selectedOpacity);
      readyButton.setDisable(false);
      menubutton.setOpacity(selectedOpacity);
    });
  }


  // --------------------------------- UTILITY ----------------------------------- //

  /**
   * sets the name label which displays the users own username
   *
   * @param name String containing the users name
   */
  public void setNamelabel(String name) {
    Platform.runLater(() -> namelabel.setText(name));
  }

  /**
   * sets the listener variable containing whether it's the players turn or not
   *
   * @param b boolean containing whether it's the users turn
   */
  public void setMyTurn(boolean b) {
    isMyTurn.set(b);
  }

  /**
   * sets the listener variable containing whether the player has to answer a quiz question or not
   *
   * @param b boolean containing whether the player has to answer a quiz question or not
   */
  public void setMyQuiz(boolean b) {
    isMyQuiz.set(b);
  }

  /**
   * waits until it's the players turn and then activates and highlights the dice
   */
  private void MyTurnListener() {
    isMyTurn.addListener((obs, oldv, newv) -> {
      if (newv) {
        regularDice.setDisable(false);
        regularDice.setScaleX(1);
        regularDice.setScaleY(1);
        regularDice.setMouseTransparent(false);
        fourDice1.setDisable(false);
        fourDice1.setScaleX(1);
        fourDice1.setScaleY(1);
        fourDice1.setMouseTransparent(false);
        fourDice2.setDisable(false);
        fourDice2.setScaleX(1);
        fourDice2.setScaleY(1);
        fourDice2.setMouseTransparent(false);
        fourDice3.setDisable(false);
        fourDice3.setScaleX(1);
        fourDice3.setScaleY(1);
        fourDice3.setMouseTransparent(false);
      } else {
        regularDice.setDisable(true);
        regularDice.setScaleX(0.7);
        regularDice.setScaleY(0.7);
        regularDice.setMouseTransparent(true);
        fourDice1.setDisable(true);
        fourDice1.setScaleX(0.7);
        fourDice1.setScaleY(0.7);
        fourDice1.setMouseTransparent(true);
        fourDice2.setDisable(true);
        fourDice2.setScaleX(0.7);
        fourDice2.setScaleY(0.7);
        fourDice2.setMouseTransparent(true);
        fourDice3.setDisable(true);
        fourDice3.setScaleX(0.7);
        fourDice3.setScaleY(0.7);
        fourDice3.setMouseTransparent(true);
      }
    });
  }

  /**
   * waits until the player has to answer a quiz question and enables the quiz answer buttons
   */
  private void MyQuizListener() {
    isMyQuiz.addListener((obs, oldv, newv) -> {
      if (newv) {
        quizA.setDisable(false);
        quizA.setOpacity(selectedOpacity);
        quizB.setDisable(false);
        quizB.setOpacity(selectedOpacity);
        quizC.setDisable(false);
        quizC.setOpacity(selectedOpacity);
        quizD.setDisable(false);
        quizD.setOpacity(selectedOpacity);
      } else {
        quizA.setDisable(true);
        quizA.setOpacity(unselectedOpacity);
        quizB.setDisable(true);
        quizB.setOpacity(unselectedOpacity);
        quizC.setDisable(true);
        quizC.setOpacity(unselectedOpacity);
        quizD.setDisable(true);
        quizD.setOpacity(unselectedOpacity);
      }
    });
  }

}


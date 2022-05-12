package gui;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
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
   * marks whether a player wants to write in the global chat or not
   */
  boolean writeInGlobalChat = false;

  /**
   * marks whether a player is ready or not
   */
  boolean isReady = false;

  /**
   * marks whether the game has started or not
   */
  public static boolean gameHasStarted = false;

  /**
   * all fields of the board in a HashMap
   */
  public static HashMap<Integer, Integer[]> fields = new HashMap<>();

  public int blueCharNr = 1;
  public int redCharNr = 1;
  public int yellowCharNr = 1;
  public int greenCharNr = 1;


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
   * a toggle button used to switch between global and lobby chat
   */
  @FXML
  private ToggleButton globalToggleButton;

  /**
   * Polygon which represents the first dicedice
   */
  @FXML
  private Polygon fourDice1;

  /**
   * Polygon which represents the second dicedice
   */
  @FXML
  private Polygon fourDice2;

  /**
   * Polygon which represents the third dicedice
   */
  @FXML
  private Polygon fourDice3;

  /**
   * button used for setting the player as ready or unready
   */
  @FXML
  private Button readyButton;

  /**
   * button used to start the game
   */
  @FXML
  private Button startButton;

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
   * GridPane which is used as the board of the game
   */
  @FXML
  private GridPane board;


  /**
   * method reads input from the Textfield and checks, which command to send to the server
   * if there's no command at the start of the message, it will be sent as a chat (which is the main use for this GUI)
   */
  @FXML
  void sendChatMessage() {
    String msg = (chatTextField.getText());

    if (msg.startsWith("/nick")) {
      String[] split = msg.split(" ", 2);
      String newName = split[1];
      sendToServer.send(CommandsToServer.NICK, newName);
    } else if (msg.startsWith("/winnerwinnerchickendinner")) {
      String[] input = msg.split(" ", 2);
      sendToServer.send(CommandsToServer.WWCD, input[1]);
    } else if (msg.startsWith("/whisper")) {
      String[] split = msg.split(" ", 3);
      if (split.length > 1) {
        String whisperMsg = split[1] + "-" + split[2];
        sendToServer.send(CommandsToServer.WHISPER, whisperMsg);
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
    chat.appendText(msg);
    chat.appendText("\n");
  }

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

    /*
    playerBlue.setStroke(Color.BLACK);
    Image blue = new Image("charBlueHead.png", false);
    playerBlue.setFill(new ImagePattern(blue));

    playerRed.setStroke(Color.BLACK);
    Image red = new Image("charRedHead.png", false);
    playerRed.setFill(new ImagePattern(red));

    playerGreen.setStroke(Color.BLACK);
    Image green = new Image("charGreenHead.png", false);
    playerGreen.setFill(new ImagePattern(green));

    playerYellow.setStroke(Color.BLACK);
    Image yellow = new Image("charYellowHead.png", false);
    playerYellow.setFill(new ImagePattern(yellow));


    // sets the player figures to the start-field
        /*System.out.println("Y: parent center: " + testToolBar.getTranslateY());
        System.out.println("Y: starterPane parents max " + starterPaneBlue.getBoundsInParent().getMaxY());
        System.out.println("Y: starterPane translate " + starterPaneBlue.getTranslateY());
        playerBlue.setTranslateX(starterPaneBlue.getBoundsInParent().getCenterX());
        playerBlue.setTranslateY(starterPaneBlue.getBoundsInParent().getCenterY());
        System.out.println("normal translate: " + playerBlue.getTranslateY());
        System.out.println("normal getCenter: " + playerBlue.getCenterY());
        System.out.println("parent getCenter: " + playerBlue.getBoundsInParent().getCenterY());
        playerBlue.setTranslateY(testToolBar.getTranslateY());*/

    //playerRed.setTranslateX(starterPaneRed.getTranslateX());
    //playerRed.setTranslateY(starterPaneRed.getTranslateY());


    /*
    // THREAD 1 : receive chat messages
    // A new Thread is made that waits for incoming messages
    // The thread will also wait for the game to start and then "remove" the start and ready button
    Thread waitForChatThread = new Thread(() -> {
      /*while (!hasJoinedChat) {
        try {
          sleep(10);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
      // Main.receiveChat.setMessage("You have joined the chat.");



      while (true) {
        msg = receiveFromProtocol.receive(); // blocks until a message is received
        Platform.runLater(() -> printChatMessage(msg)); // a javafx "thread" that calls the print method
      }
    });


    // THREAD 2 : waits for game moves to update the game tracker
    Thread waitForGameUpdatesThread = new Thread(() -> {
      while (true) {
        gameMove = receiveFromProtocolGameUpdate.receive(); // blocks until a message is received
        if (!gameMove.equals(gameMoveTmp)) {
          Platform.runLater(() -> printGameUpdate(gameMove)); // a javafx "thread" that calls the print method
        }
        gameMoveTmp = gameMove;
      }
    });


    // THREAD 3 : waits for characters to move
    Thread waitForCharacterMovement = new Thread(() -> {
      while (true) {
        moveToField = (receiveNewPlayerPosition.receive());
        Platform.runLater(() -> movePlayer(moveToField)); // a javafx "thread" that calls the print method
      }
    });

    // start threads
    waitForChatThread.setName("GuiWaitForChatThread"); // set name of thread
    waitForChatThread.start(); // start thread
    waitForGameUpdatesThread.setName("GuiWaitForGameUpdates");
    waitForGameUpdatesThread.start();
    waitForCharacterMovement.setName("GuiWaitForCharacterMovements");
    waitForCharacterMovement.start();

     */
  }

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
   * This method is used to reset the game at the start of it
   * the player characters are moved to the starting position and all dicedice are given back to the player
   */
  public void resetGame() {
    Platform.runLater(() -> {
      tokenOne.setTranslateY(594);
      tokenOne.setTranslateX(160);
      tokenTwo.setTranslateY(594);
      tokenTwo.setTranslateX(240);
      tokenThree.setTranslateY(594);
      tokenThree.setTranslateX(80);
      tokenFour.setTranslateY(594);

      // resets the dicedice
      fourDice1.setDisable(false);
      fourDice2.setDisable(false);
      fourDice3.setDisable(false);
      fourDice1.setOpacity(1);
      fourDice2.setOpacity(1);
      fourDice3.setOpacity(1);
    });
  }


  /**
   * this method is used to print game updates to the game tracker in the GUI
   *
   * @param gameMove String
   */
  public void printGameUpdate(String gameMove) {
    String[] splitted = gameMove.split("§");
    Platform.runLater(() -> {
      for (String s : splitted) {
        gameTracker.appendText(s);
        gameTracker.appendText("\n");
      }
    });
  }

  /**
   * Method which allows users to quit when pressing the quit button
   */
  @FXML
  public void quitGame() {
    Main.exit();
  }


  /**
   * This method is called when one of the togglebuttons is pressed
   * The method sets the variable writeInGlobalChat, which is used to determine if
   * a message is sent in the global chat or the lobby chat
   *
   * @param actionEvent actionEvent
   */
  public void switchChat(ActionEvent actionEvent) {
    writeInGlobalChat = actionEvent.getSource() == globalToggleButton;
  }

  /**
   * throws a dicedice when clicking on one of the four-dices
   */
  public void throwFourDice() {
    sendToServer.send(CommandsToServer.DICEDICE, "");
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
    }

    /**
     * starts the game by pressing on the start button
     */
    public void startTheGame () {
      resetGame();
      sendToServer.send(CommandsToServer.START, null);
      gameHasStarted = true;
    }

    /**
     * sets the player as ready when pressing the ready button
     * pressing it again will make the player unready
     */
    public void setPlayerAsReady () {
      if (!isReady) {
        isReady = true;
        sendToServer.send(CommandsToServer.READY, null);
        Platform.runLater(() -> readyButton.setText("UNREADY?"));
      } else {
        sendToServer.send(CommandsToServer.UNREADY, null);
        Platform.runLater(() -> readyButton.setText("READY?"));
        isReady = false;
      }
    }

  /**
   * adds the correct picture to the player tokens
   */
  public void setCharToken(int tokenNr, int charNr) {
    charNr = charNr + 1;
    System.out.println("----------------------------");
    System.out.println("tokenNr " + tokenNr);
    System.out.println("charNr " + charNr);
    System.out.println("----------------------------");


    Image img = new Image(findCorrectImag(charNr), false);
    if (tokenNr == 1){
      tokenOne.setFill(new ImagePattern(img));
      tokenOne.setOpacity(1);
    } else if (tokenNr ==2) {
      tokenTwo.setFill(new ImagePattern(img));
      tokenTwo.setOpacity(1);
    } else if (tokenNr == 3) {
      tokenThree.setFill(new ImagePattern(img));
      tokenThree.setOpacity(1);
    } else if (tokenNr == 4) {
      tokenFour.setFill(new ImagePattern(img));
      tokenFour.setOpacity(1);
    }
  }

  public String findCorrectImag(int charNr) {
    if (charNr == 1) {
      return "char1.png";
    } else if (charNr == 2) {
      return "char2.png";
    } else if (charNr == 3) {
      return "char3.png";
    } else if (charNr == 4) {
      return "char4.png";
    } else if (charNr == 5) {
      return "char5.png";
    } else if (charNr == 6) {
      return "char6.png";
    }
    return "";
  }

  /**
     * method is used to "remove" the start and ready button once the game has started
     * (it doesn't remove the buttons but disables them and sets the opacity to 0)
     */
    public void disableStartAndReadyButton () {
      Platform.runLater(() -> {
        startButton.setDisable(true);
        startButton.setOpacity(0);
        readyButton.setDisable(true);
        readyButton.setOpacity(0);
      });
    }

    /**
     * used to switch to the Menu scene
     */
    public void switchToMenu () {
      Main.displayMenu();
    }

    /**
     * used to switch to the Highscore scene
     */
    public void switchToHighscore() {
      Main.displayHighscore();
    }

    /**
     * answers a quiz question with A when pressing the button
     */
    public void answerQuizA () {
      sendToServer.send(CommandsToServer.QUIZ, "A");
    }

    /**
     * answers a quiz question with B when pressing the button
     */
    public void answerQuizB () {
      sendToServer.send(CommandsToServer.QUIZ, "B");
    }

    /**
     * answers a quiz question with C when pressing the button
     */
    public void answerQuizC () {
      sendToServer.send(CommandsToServer.QUIZ, "C");
    }

    /**
     * answers a quiz question with D when pressing the button
     */
    public void answerQuizD () {
      sendToServer.send(CommandsToServer.QUIZ, "D");
    }

    /**
     * This method determines which player needs to be moved on the GUI and on which field they now belong
     *
     * @param moveToField String which contains the color of the player and the field they move to
     * @return returns the correct player character to move around (which are circles)
     */
    public Circle chooseCorrectPlayerToMove (String moveToField){
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
     * This method is used to get the x- and y-coordinate of a field on the board
     * To find a field you need to know the row and column number
     *
     * @param board GridPane: represents the board over which the player characters move
     * @param col   int: column of the field you want to search
     * @param row   int: row of the field you want to search
     * @return double[] containing the x- and y-coordinate of the field in
     */
    private double[] getPosFromGridPane (GridPane board,int col, int row){
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
    public void movePlayer (String moveToField){

      Circle playerToMove = chooseCorrectPlayerToMove(moveToField);

      // get the coordinates of the new field
      String[] playerAndNewField = moveToField.split("--");
      int newFieldToMoveTo = Integer.parseInt(playerAndNewField[1]);
      Integer[] columnAndRow = fields.get(newFieldToMoveTo);
      double[] newPos = getPosFromGridPane(board, columnAndRow[0], columnAndRow[1]);


      // moves the player to the new coordinates
      Platform.runLater(() -> {
        playerToMove.setTranslateX(newPos[0] - 25);
        playerToMove.setTranslateY(newPos[1] - 30);
      });
    }
  }


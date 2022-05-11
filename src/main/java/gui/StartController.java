package gui;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import utility.io.CommandsToServer;
import utility.io.ReceiveFromProtocol;
import utility.io.SendToServer;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * this is the controller for fxml_start.fxml
 */
public class StartController implements Initializable {

  /**
   * SendToServer object used to communicate with the server
   */
  private final SendToServer sendToServer = new SendToServer();

  /**
   * ReceiveFromProtocol object used to communicate with the protocol
   */
  public static ReceiveFromProtocol receiveFromProtocol = new ReceiveFromProtocol();

  /**
   * a String which can save a message
   */
  String msg;

  /**
   * a label which is used to ask for the clients username
   */
  @FXML
  private Label showText;

  /**
   * a TextField used to enter answers to question
   */
  @FXML
  private TextField textInput;

  @FXML
  private Button sendButton;

  @FXML
  private Button noButton;

  @FXML
  private Button yesButton;


  /**
   * This method reads the text from textInput and sends it to the server
   */
  public void sendMsg() {
    String msg = textInput.getText();
    sendToServer.send(CommandsToServer.NAME, msg);
    textInput.clear();
    switchToMenu();
  }

  /**
   * changes the displayed text on the stage
   *
   * @param msg message
   */
  @FXML
  public void printMsg(String msg) {
    Platform.runLater(() -> showText.setText(msg));
    // showText.setText(msg);
  }


  /**
   * This method is called, when the class is created
   * It is used to wait on incoming messages
   * This method waits for a change in the field msg, if it's changed it will call the printMsg method, which
   * changes the text on screen
   *
   * @param location  location
   * @param resources resources
   */
  @Override
  public void initialize(URL location, ResourceBundle resources) {

    Main.setStartController(this);

    /*
    // A new Thread is made which waits
    Thread waitForMsgChange = new Thread(() -> {
      while (true) {
        msg = receiveFromProtocol.receive(); // blocks until a message is received
        Platform.runLater(() -> printMsg(msg)); // a javafx "thread" that calls the printMsg method
      }
    });
    waitForMsgChange.setName("GuiStartWaitForMsgChange"); // set name of thread
    waitForMsgChange.start(); // start thread

     */
  }


  /**
   * switches to the Menu scene when clicking the button
   */
  public void switchToMenu() {
    Main.displayMenu();
  }

  /**
   * switches to the Game scene when clicking the button
   */
  public void switchToGame() {
    Main.displayGame();
  }

  /**
   * switches to the Highscore scene when clicking the button
   */
  public void switchToHighscore() {
    Main.displayHighscore();
  }

    public void nameNo(ActionEvent actionEvent) {
      sendToServer.send(CommandsToServer.NAME, "no");
      yesButton.setOpacity(0);
      yesButton.setDisable(true);
      noButton.setOpacity(0);
      noButton.setDisable(true);
      textInput.setOpacity(1);
      sendButton.setOpacity(1);
      sendButton.setDisable(false);
    }

  public void nameYes(ActionEvent actionEvent) {
    sendToServer.send(CommandsToServer.NAME, "yes");
    switchToMenu();
  }
}

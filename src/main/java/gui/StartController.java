package gui;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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

  private final SendToServer sendToServer = new SendToServer();
  public static ReceiveFromProtocol receiveFromProtocol = new ReceiveFromProtocol();
  String msg;

  @FXML
  private Label showText;

  @FXML
  private TextField textInput;


  /**
   * This method reads the text from textInput and sends it to the server
   */
  public void sendMsg() {
    String msg = textInput.getText();
    sendToServer.send(CommandsToServer.NAME, msg);
    textInput.clear();
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
   * the following methods are used to switch between scenes
   * they're only temporary
   */
  public void switchToMenu() {
    Main.displayMenu();
  }

  public void switchToGame() {
    Main.displayGame();
  }

  public void switchToHighscore() {
    Main.displayHighscore();
  }
}

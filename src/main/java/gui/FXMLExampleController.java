package gui;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import utility.IO.*;


import java.net.URL;
import java.util.ResourceBundle;

public class FXMLExampleController implements Initializable {
  private final SendToServer sendToServer = new SendToServer();
  private static String msg;
  public static ReceiveFromProtocol receiveFromProtocol = new ReceiveFromProtocol();

  @FXML
  private TextField chatTextField;

  @FXML
  private TextArea chat;


    /**
     * method reads input from the Textfield and checks, which command to send to the server
     * if there's no command at the start of the message, it will be sent as a chat (which is the main use for this GUI)
     * @param event
     */

  @FXML
  void sendChatMessage(ActionEvent event) {
    String msg = (chatTextField.getText());

    if (msg.startsWith("/nick")) {
        System.out.println("momentan msg: " + msg );
        String[] split = msg.split(" ", 2);
        String newName = split[1];
        System.out.println("neui dengs: " + newName );
        sendToServer.send(CommandsToServer.NICK, newName);
    } else if (msg.startsWith("/whisper")) {
        String[] split = msg.split(" ", 3);
        if (split.length > 1) {
            String whisperMsg = split[1] + "-" + split[2];
            sendToServer.send(CommandsToServer.WHISPER, whisperMsg);
        } else {
            chat.appendText("You cannot whisper nothing!");
        }
    } else {
        sendToServer.send(CommandsToServer.CHAT, msg);
    }
    chatTextField.clear();
  }

  @FXML
  public void printChatMessage(String msg) {
    chat.appendText(msg);
    chat.appendText("\n");
  }

  // This Method runs when this class is created
  @Override
  public void initialize(URL location, ResourceBundle resources) {
    // A new Thread is made that waits for incoming messages
    Thread waitForChatThread = new Thread(() -> {
      while(true) {
        msg = receiveFromProtocol.receive(); // blocks until a message is received
        Platform.runLater(() -> printChatMessage(msg)); // a javafx "thread" that calls the print method
      }
    });
    waitForChatThread.setName("GuiWaitForChatThread"); // set name of thread
    waitForChatThread.start(); // start thread
  }

    public void quitGame(ActionEvent actionEvent) {
        sendToServer.send(CommandsToServer.QUIT, msg);
        chat.appendText("You left the game.");
    }
}


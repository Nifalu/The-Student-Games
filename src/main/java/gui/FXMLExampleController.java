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


  @FXML
  void sendChatMessage(ActionEvent event) {
    String msg = (chatTextField.getText());
    sendToServer.send(CommandsToServer.CHAT, msg);
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
}


package gui;

import Server.ClientHandler;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import utility.IO.*;


import javax.swing.*;
import java.net.URL;
import java.util.ResourceBundle;

public class FXMLExampleController implements Initializable {
  SendToServer sendToServer = new SendToServer();
  public static String chatmsg;
  public static ReceiveFromProtocol receiveFromProtocol;

  @FXML
  private TextField chatTextField;

  @FXML
  private TextArea chat;


  @FXML
  void sendChatMessage(ActionEvent event) {
    String msg = (chatTextField.getText());
    sendToServer.send(CommandsToServer.CHAT, msg);
    chatTextField.clear();
    // chat.appendText(msg);
  }


  @FXML
  public void printChatMessage(String msg) {
    System.out.println("in printChatMessage");
    chat.appendText(msg);
    chat.appendText("\n");
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {

    Platform.runLater(new Runnable() {

      @Override
      public void run() {


          // Ich wart bis de static String nüm null isch. --> Funktioniert aber halt nur 1x
          while(chatmsg == null) {
          }
          printChatMessage(chatmsg);


          /*
          // Eigentlich sgliche wie obe nur im loop, --> Fenster blockiert D:
          while(true) {
            if (chatmsg != null) {
              printChatMessage(chatmsg);
              chatmsg = null;
            }
          }

           */

        /*
        // git en nullpointer??
        String s;
        s = receiveFromProtocol.receive();
        printChatMessage(s);
         */
      }
    });

    System.out.println("finished initialize");

  }
}


package gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import utility.IO.CommandsToServer;


import javax.swing.*;

public class FXMLExampleController {

    @FXML
    private TextField chatTextField;

    @FXML
    private TextArea chat;

    @FXML
    void sendChatMessage(ActionEvent event) {
      String msg = (chatTextField.getText());
        //utility.IO.SendToServer.send(CommandsToServer.CHAT, msg);

      chat.appendText("Username: " + msg);
      chat.appendText("\n");
    }
  }


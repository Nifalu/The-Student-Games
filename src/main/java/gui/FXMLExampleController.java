package gui;

import Server.ClientHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import utility.IO.*;


import javax.swing.*;

public class FXMLExampleController {
    SendToServer sendToServer = new SendToServer();

    @FXML
    private TextField chatTextField;

    @FXML
    private TextArea chat;

    @FXML
    void sendChatMessage(ActionEvent event) {
      String msg = (chatTextField.getText());
      sendToServer.send(CommandsToServer.CHAT, msg);
      //chat.appendText(clientHandler.user.getUsername() + msg);
      //chat.appendText("\n");
    }


}


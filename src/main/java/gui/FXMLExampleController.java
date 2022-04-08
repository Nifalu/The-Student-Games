package gui;

import Server.ClientHandler;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import utility.IO.*;


import javax.swing.*;

public class FXMLExampleController {
    SendToServer sendToServer = new SendToServer();
    String msg = null;

    public static StringProperty stringProperty = new SimpleStringProperty("");




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
    void printChatMessage() {
        chat.appendText(msg);
        chat.appendText("\n");
    }


}


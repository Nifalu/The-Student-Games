package gui;

import Client.ClientManager;
import Server.Chat;
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


    String msg = null;

    @FXML
    private static TextField chatTextField;

    @FXML
    private static TextArea chat;

    @FXML
    void sendChatMessage(ActionEvent event) {
      String msg = (chatTextField.getText());
      sendToServer.send(CommandsToServer.CHAT, msg);
    }


    @FXML
    public static void printChatMessage(String msg) {
        chat.appendText(msg);
        chat.appendText("\n");
    }

    public void startChat(ActionEvent actionEvent) {

    }
}


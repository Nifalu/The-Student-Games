package gui;

import Server.ClientHandler;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.util.Duration;
import utility.IO.*;


import javax.swing.*;
import java.net.URL;
import java.util.ResourceBundle;

public class FXMLExampleController implements Initializable {

    SendToServer sendToServer = new SendToServer();
    public static String lastChatMsg = "Welcome to the chat!"; // saves the last chat message that came in
    static String tmp = "empty"; // will be compared with lastChatMsg -> if they're not equal, a new chat is sent

    @FXML
    private TextField chatTextField;

    @FXML
    public TextArea chat;


    @FXML
    void sendChatMessage(ActionEvent event) {
      String msg = (chatTextField.getText());
      sendToServer.send(CommandsToServer.CHAT, msg);
      chatTextField.clear();
    }

    /**
     * once the chat GUI is started, every 0.01 seconds the lastChatMsg is compared with the tmp String
     * if the lastChatMsg has been changed and differs from the tmp String, the message will be printed out in the chat
     * and the tmp String will be set to the new lastChatMsg
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(10), e -> {
            if(!tmp.equals(lastChatMsg)) {
                chat.appendText(lastChatMsg);
                chat.appendText("\n");
                tmp = lastChatMsg;
            }

        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }
}


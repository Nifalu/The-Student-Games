package gui;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.stage.Stage;
import utility.io.*;


import java.net.URL;
import java.util.ResourceBundle;

/**
 * This is the controller for game.fxml
 */

public class GameController implements Initializable {
    private final SendToServer sendToServer = new SendToServer();
    private static String msg;
    public static ReceiveFromProtocol receiveFromProtocol = new ReceiveFromProtocol();
    public static boolean hasJoinedChat = false;
    boolean writeInGlobalChat = false;

    @FXML
    private TextField chatTextField;

    @FXML
    private TextArea chat;

    @FXML
    private Button quitButton;

    @FXML
    private ToggleButton globalToggleButton;

    @FXML
    private ToggleButton lobbyToggleButton;


    /**
     * method reads input from the Textfield and checks, which command to send to the server
     * if there's no command at the start of the message, it will be sent as a chat (which is the main use for this GUI)
     * @param event event
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
        } else if (writeInGlobalChat) {
            sendToServer.send(CommandsToServer.CHAT, msg);
        } else if (!writeInGlobalChat) {
            sendToServer.send(CommandsToServer.LOBBYCHAT, msg);
        } else {
            chat.appendText("Please select global or lobby chat.");
            chat.appendText("\n");
        }
        chatTextField.clear();
    }

    /**
     * method is used to print messages to the chat
     * @param msg message which will be printed in the chat
     */
    @FXML
    public void printChatMessage(String msg) {
        chat.appendText(msg);
        chat.appendText("\n");
    }

    /**
     * This method runs, when the class is created
     * It first waits until the user has joined the chat and will then wait for incoming chat messages
     * Incoming messages will then be printed to the chat
     * @param location resource
     * @param resources resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // A new Thread is made that waits for incoming messages
        Thread waitForChatThread = new Thread(() -> {
            while(!hasJoinedChat) {
                try {
                    wait(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            receiveFromProtocol.setMessage("You have joined the chat.");
            while(true) {
                msg = receiveFromProtocol.receive(); // blocks until a message is received
                Platform.runLater(() -> printChatMessage(msg)); // a javafx "thread" that calls the print method
            }
        });
        waitForChatThread.setName("GuiWaitForChatThread"); // set name of thread
        waitForChatThread.start(); // start thread
    }

    /**
     * Method which allows users to quit when pressing the quit button
     * @param actionEvent actionEvent
     */
    @FXML
    public void quitGame(ActionEvent actionEvent) {
        sendToServer.send(CommandsToServer.CHAT, "left the chat"); // may need to change
        Stage stage = (Stage) quitButton.getScene().getWindow();
        stage.close();
        sendToServer.send(CommandsToServer.QUIT, msg);

    }


    /**
     * This method is called when one of the togglebuttons is pressed
     * The method sets the variable writeInGlobalChat, which is used to determine if
     * a message is sent in the global chat or the lobba chat
     * @param actionEvent actionEvent
     */
    public void switchChat(ActionEvent actionEvent) {
        writeInGlobalChat = actionEvent.getSource() == globalToggleButton;
    }
}


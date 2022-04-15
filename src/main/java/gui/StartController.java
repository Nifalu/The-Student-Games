package gui;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import utility.IO.CommandsToServer;
import utility.IO.ReceiveFromProtocol;
import utility.IO.SendToServer;

import java.net.URL;
import java.util.ResourceBundle;

public class StartController implements Initializable {
    private final SendToServer sendToServer = new SendToServer();
    public static ReceiveFromProtocol receiveFromProtocol = new ReceiveFromProtocol();
    String msg;

    @FXML
    private Label showText;

    @FXML
    private TextField textInput;


    /**
     * used to switch to the chat scene
     */
    private Stage stage;
    private Scene scene;
    private Parent root;

    public void switchToChat(ActionEvent event) throws Exception {
        ChatController.hasJoinedChat = true;
        root = FXMLLoader.load(getClass().getClassLoader().getResource("fxml_chat.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }


    /**
     * This method reads the text from textInput and sends it to the Server
     * @param actionEvent
     */
    public void sendMsg(ActionEvent actionEvent) {
        String msg = textInput.getText();
        sendToServer.send(CommandsToServer.NAME, msg);
        textInput.clear();
    }

    public void printMsg(String msg) {
        showText.setText(msg);
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // A new Thread is made which waits
        Thread waitForMsgChange = new Thread(() -> {
            while(true) {
                msg = receiveFromProtocol.receive(); // blocks until a message is received
                Platform.runLater(() -> printMsg(msg)); // a javafx "thread" that calls the print method
            }
        });
        waitForMsgChange.setName("GuiStartWaitForMsgChange"); // set name of thread
        waitForMsgChange.start(); // start thread
    }
}

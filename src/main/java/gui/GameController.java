package gui;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Polygon;
import javafx.stage.Stage;
import utility.io.*;


import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static java.lang.Thread.sleep;

/**
 * This is the controller for game.fxml
 */

public class GameController implements Initializable {
    private final SendToServer sendToServer = new SendToServer();
    private static String msg; // used to update the chat
    public static ReceiveFromProtocol receiveFromProtocol = new ReceiveFromProtocol();
    public static ReceiveFromProtocol receiveFromProtocolGameUpdate = new ReceiveFromProtocol();
    public static boolean hasJoinedChat = false;
    boolean writeInGlobalChat = false;
    boolean isReady = false;
    public static boolean gameHasStarted = false;

    public static String gameMove = "hello!"; // used to update the Game Tracker
    private static String lastGameMove = "hello!"; // used to compare to gameMove

    private Stage menuStage;
    private Scene menuScene;
    private Parent menuRoot;
    private Stage highscoreStage;
    private Scene highscoreScene;
    private Parent highscoreRoot;

    @FXML
    private TextField chatTextField;

    @FXML
    private TextArea chat;

    @FXML
    private Button quitButton;

    @FXML
    private ToggleButton globalToggleButton;

    @FXML
    private Polygon fourDice1;

    @FXML
    private Polygon fourDice2;

    @FXML
    private Polygon fourDice3;

    @FXML
    private Button readyButton;

    @FXML
    private Button startButton;

    @FXML
    private TextArea gameTracker;


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

        // THREAD 1 : receive chat messages
        // A new Thread is made that waits for incoming messages
        // The thread will also wait for the game to start and then "remove" the start and ready button
        Thread waitForChatThread = new Thread(() -> {
            while(!hasJoinedChat) {
                try {
                    sleep(2000);
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


        // THREAD 2 :
        Thread waitForGameUpdatesThread = new Thread(() -> {
            while(true) {
                gameMove = receiveFromProtocolGameUpdate.receive(); // blocks until a message is received
                Platform.runLater(() -> printGameUpdate(gameMove)); // a javafx "thread" that calls the print method
            }
        });


        // start threads
        waitForChatThread.setName("GuiWaitForChatThread"); // set name of thread
        waitForChatThread.start(); // start thread
        waitForGameUpdatesThread.setName("GuiWaitForGameUpdates");
        waitForGameUpdatesThread.start();
    }

    /**
     * this method is used to print game updates to the game tracker in the GUI
     * @param gameMove String
     */
    private void printGameUpdate(String gameMove) {
        gameTracker.appendText(gameMove);
        gameTracker.appendText(".\n");
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

    /**
     * throws a dicedice when pressing on one of the four-dices
     * once a dice has been clicked, it will be disabled and turn transparent
     * @param mouseEvent MouseEvent
     */
    public void throwFourDice(MouseEvent mouseEvent) {
        sendToServer.send(CommandsToServer.DICEDICE, null);

        // checks which fourDices are already disabled and disables one accordingly
        if (!fourDice3.isDisabled()) {
            fourDice3.setDisable(true);
            fourDice3.setOpacity(0.2);
        } else if (fourDice3.isDisabled() && !fourDice2.isDisabled()) {
            fourDice2.setDisable(true);
            fourDice2.setOpacity(0.2);
        } else if (fourDice3.isDisabled() && fourDice2.isDisabled() && !fourDice1.isDisabled()) {
            fourDice1.setDisable(true);
            fourDice1.setOpacity(0.2);
        }
    }

    /**
     * method throws a regular dice
     * @param mouseEvent MouseEvent
     */
    public void throwRegularDice(MouseEvent mouseEvent) {
        sendToServer.send(CommandsToServer.ROLLDICE, null);
    }

    /**
     * starts the game by pressing on the start button
     * @param actionEvent ActionEvent
     */
    public void startTheGame(ActionEvent actionEvent) {
        sendToServer.send(CommandsToServer.START, null);
        gameHasStarted = true;
        disableStartAndReadyButton();
    }

    /**
     * sets the player as ready when pressing the ready button
     * pressing it again will make the player unready
     * @param actionEvent ActionEvent
     */
    public void setPlayerAsReady(ActionEvent actionEvent) {
        if (!isReady) {
            isReady = true;
            sendToServer.send(CommandsToServer.READY, null);
            readyButton.setText("UNREADY?");
        } else {
            sendToServer.send(CommandsToServer.UNREADY, null);
            readyButton.setText("READY?");
            isReady = false;
        }
    }

    /**
     * method is used to "remove" the start and ready button once the game has started
     * (it doesn't remove the buttons but disables them and sets the opacity to 0)
     */
    public void disableStartAndReadyButton() {
        System.out.println("S ESCH IM DISABLE Züüg DENNE");
        startButton.setDisable(true);
        startButton.setOpacity(0);
        readyButton.setDisable(true);
        readyButton.setOpacity(0);
    }

    /**
     * the following methods are used to switch between scenes
     * they're only temporary
     */
    public void switchToMenu() {
        Main.displayMenu();
    }

    public void switchToHighscore() {
        Main.displayHighscore();
    }

    public void answerQuizA(ActionEvent actionEvent) {
        sendToServer.send(CommandsToServer.QUIZ, "A");
    }

    public void answerQuizB(ActionEvent actionEvent) {
        sendToServer.send(CommandsToServer.QUIZ, "B");
    }

    public void answerQuizC(ActionEvent actionEvent) {
        sendToServer.send(CommandsToServer.QUIZ, "C");
    }

    public void answerQuizD(ActionEvent actionEvent) {
        sendToServer.send(CommandsToServer.QUIZ, "D");
    }
}


package gui;

import gameLogic.GameList;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import utility.io.CommandsToServer;
import utility.io.ReceiveFromProtocol;
import utility.io.SendToServer;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static java.lang.Thread.sleep;

public class MenuController implements Initializable {

  private final SendToServer sendToServer = new SendToServer();
  private static String msg;
  public static ReceiveFromProtocol receiveFromProtocol = new ReceiveFromProtocol();
  public static boolean hasJoinedChat = false;
  public ToggleGroup switchGlobalLobby;
  boolean writeInGlobalChat = false;
  public static ReceiveFromProtocol lobbyReceiver = new ReceiveFromProtocol();
  public static ReceiveFromProtocol friendsReceiver = new ReceiveFromProtocol();

  public static String lobbyList;

  public static String friendList;


  @FXML
  private TextField chatTextField;

  @FXML
  private TextArea chat;

  @FXML
  private Button quitButton;

  @FXML
  private ToggleButton globalToggleButton;

  @FXML
  private ListView<String> lobbyListView;

  @FXML
  private TextField createLobbyTextField;

  @FXML
  private Label selectedLobbyLabel;

  @FXML
  private ListView<String> friendListView;


  /**
   * method reads input from the Textfield and checks, which command to send to the server
   * if there's no command at the start of the message, it will be sent as a chat (which is the main use for this GUI)
   */

  @FXML
  void sendChatMessage() {

    String msg = (chatTextField.getText());
    if (msg.startsWith("/nick")) {
      System.out.println("momentan msg: " + msg);
      String[] split = msg.split(" ", 2);
      String newName = split[1];
      System.out.println("neui dengs: " + newName);
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
    } else {
      sendToServer.send(CommandsToServer.LOBBYCHAT, msg);
    }
    chatTextField.clear();
  }

  /**
   * method is used to print messages to the chat
   *
   * @param msg message which will be printed in the chat
   */
  @FXML
  public void printChatMessage(String msg) {
    chat.appendText(msg);
    chat.appendText("\n");
  }

  @FXML
  public void printLobbies(String[] lobbies) {
    lobbyListView.getItems().clear();
    lobbyListView.getItems().addAll(lobbies);
  }

  @FXML
  public void printFriends(String[] friends) {
    friendListView.getItems().clear();
    friendListView.getItems().addAll(friends);
  }


  /**
   * This method runs, when the class is created
   * It first waits until the user has joined the chat and will then wait for incoming chat messages
   * Incoming messages will then be printed to the chat
   *
   * @param location  resource
   * @param resources resources
   */
  @Override
  public void initialize(URL location, ResourceBundle resources) {
    selectedLobbyLabel.setText("Please create or choose a lobby.");

    Thread lobbyListThread = new Thread(() -> {
      while (true) {

        lobbyList = lobbyReceiver.receive();
        lobbyList = removeNewline(lobbyList);
        String[] splittedLobbies = splittedString(lobbyList);
        Platform.runLater(() -> printLobbies(splittedLobbies));
      }
    });

    Thread friendThread = new Thread(() -> {
      while (true) {

        friendList = friendsReceiver.receive();
        friendList = removeNewline(friendList);
        String[] splittedFriends = splittedLobbies(friendList);
        Platform.runLater(() -> printFriends(splittedFriends));
      }
    });


    // A new Thread is made that waits for incoming messages
    Thread waitForChatThread = new Thread(() -> {

      while (!hasJoinedChat) {
        try {
          sleep(10);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }


      receiveFromProtocol.setMessage("You have joined the chat.");
      while (true) {
        msg = receiveFromProtocol.receive(); // blocks until a message is received
        Platform.runLater(() -> printChatMessage(msg)); // a javafx "thread" that calls the print method
      }
    });

    // starts threads
    friendThread.setName("friendThread");
    friendThread.start();
    lobbyListThread.setName("GuiWaitForLobbyList"); // set name of thread
    lobbyListThread.start();
    waitForChatThread.setName("GuiWaitForChatThread"); // set name of thread
    waitForChatThread.start(); // start thread
  }

  /**
   * Method which allows users to quit when pressing the quit button
   */
  @FXML
  public void quitGame() {
    sendToServer.send(CommandsToServer.CHAT, "left the chat"); // may need to change
    Stage stage = (Stage) quitButton.getScene().getWindow();
    stage.close();
    sendToServer.send(CommandsToServer.QUIT, msg);
  }


  /**
   * This method is called when one of the togglebuttons is pressed
   * The method sets the variable writeInGlobalChat, which is used to determine if
   * a message is sent in the global chat or the lobba chat
   *
   * @param actionEvent actionEvent
   */
  public void switchChat(ActionEvent actionEvent) {
    writeInGlobalChat = actionEvent.getSource() == globalToggleButton;
  }


  /**
   * the following methods are used to switch between scenes
   * they're only temporary
   */

  public void switchToGame() {
    Main.displayNewGame();
  }

  public void switchToHighscore() {
    Main.displayHighscore();
  }


  public void refreshLobbies(ActionEvent ActionEvent) {
    sendToServer.send(CommandsToServer.PRINTLOBBIES, "");
  }

  public String[] splittedString(String s) {
    return s.split("§");
  }

  public String[] splittedLobbies(String s) {
    return s.split("%");
  }

  public void refreshFriends() {
    sendToServer.send(CommandsToServer.PRINTLOUNGINGLIST, "");
  }

  public void printTest() {
    System.out.println(lobbyList);
  }

  private static String removeNewline(String str) {
    return str.replace("\n", "").replace("\r", "");
  }

  public String listViewSelectedLobby() {
    return lobbyListView.getSelectionModel().getSelectedItem();
  }

  public void joinSelectedLobby() {
    String selectedLobby = listViewSelectedLobby();
    if (selectedLobby == null) {
      selectedLobbyLabel.setText("Please select a lobby.");
    } else {
      String lobbyNumber = selectedLobby.substring(0, 1);
      try {
        Integer.parseInt(lobbyNumber);
        sendToServer.send(CommandsToServer.CHANGELOBBY, lobbyNumber);
        selectedLobbyLabel.setText("You are now member of Lobby: " + lobbyNumber);
      } catch (Exception e) {
        selectedLobbyLabel.setText("Lobby needs to be open.");
      }
    }
  }

  public void createLobby(ActionEvent actionEvent) {
    refreshLobbies(actionEvent);
    String lobbyName = createLobbyTextField.getText();
    sendToServer.send(CommandsToServer.CREATELOBBY, lobbyName);
    sendToServer.send(CommandsToServer.PRINTLOBBIES, "");
    refreshLobbies(actionEvent);
    createLobbyTextField.clear();
  }
}

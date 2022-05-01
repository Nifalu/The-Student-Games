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
import gui.GameController;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static java.lang.Thread.sleep;

public class MenuController implements Initializable {

  private final SendToServer sendToServer = new SendToServer();
  private static String msg;
  public static boolean hasJoinedChat = false;
  public ToggleGroup switchGlobalLobby;
  boolean writeInGlobalChat = false;
  // public static ReceiveFromProtocol receiveFromProtocol = new ReceiveFromProtocol();
  // public static ReceiveFromProtocol lobbyReceiver = new ReceiveFromProtocol();
  // public static ReceiveFromProtocol friendsReceiver = new ReceiveFromProtocol();

  public static String lobbyList; // String containing all the lobbies

  public static String friendList; // String containing all the Lobbies and their users


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
    Platform.runLater(() -> {
      chat.appendText(msg);
      chat.appendText("\n");
    });
  }

  /**
   * print all Lobbies into the GUI (ListView)
   * @param lobbies String containing all the lobbies
   */
  @FXML
  public void printLobbies(String lobbies) {
    String[] splittedLobbies = splittedString(lobbies);
    Platform.runLater(() -> {
      lobbyListView.getItems().clear();
      lobbyListView.getItems().addAll(splittedLobbies);
    });
  }

  /**
   * prints a String into the GUI (ListView) containing all Lobbie and users in the lobby.
   * @param friends all the users in the lobby + the lobby
   */
  @FXML
  public void printFriends(String friends) {
    String[] splittedFriends = splittedLobbies(friends);
    Platform.runLater(() -> {
      friendListView.getItems().clear();
      friendListView.getItems().addAll(splittedFriends);
    });
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

    Main.setMenuController(this);


    selectedLobbyLabel.setText("Please create or choose a lobby.");

    /*
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

      /*while (!hasJoinedChat) {
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

     */
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
   * switches to the Game scene when pressing the button
   */
  public void switchToGame() {
    Main.displayGame();
  }

  /**
   * switches to the Highscore scene when pressing the button
   */
  public void switchToHighscore() {
    Main.displayHighscore();
  }


  /**
   * asks to print out the lobbies when clicking on the button
   * @param ActionEvent ActionEvent
   */
  public void refreshLobbies(ActionEvent ActionEvent) {
    sendToServer.send(CommandsToServer.PRINTLOBBIES, "");
  }

  /**
   * returns the string split at §
   * @param s String
   * @return String[] containing the splitted string
   */
  public String[] splittedString(String s) {
    return s.split("§");
  }

  /**
   * returns the lobbies split at %
   * @param s String containing all lobbies
   * @return String[] containing all lobbies
   */
  public String[] splittedLobbies(String s) {
    return s.split("%");
  }

  /**
   * prints out the lounging list
   */
  public void refreshFriends() {
    sendToServer.send(CommandsToServer.PRINTLOUNGINGLIST, "");
  }

  /**
   * test method
   */
  public void printTest() {
    System.out.println(lobbyList);
  }

  /**
   * removes the new line attached to a string
   * @param str String
   * @return String without the new line attached
   */
  private static String removeNewline(String str) {
    return str.replace("\n", "").replace("\r", "");
  }

  /**
   * shows the selected lobby
   * @return String
   */
  public String listViewSelectedLobby() {
    return lobbyListView.getSelectionModel().getSelectedItem();
  }

  /**
   * joins the lobby selected in the listview
   */
  public void joinSelectedLobby() {
    String selectedLobby = listViewSelectedLobby();
    if (selectedLobby == null) {
      Platform.runLater(() -> selectedLobbyLabel.setText("Please select a lobby."));
    } else {
      String lobbyNumber = selectedLobby.substring(0, 1);
      try {
        Integer.parseInt(lobbyNumber);
        sendToServer.send(CommandsToServer.CHANGELOBBY, lobbyNumber);
        Platform.runLater(() -> selectedLobbyLabel.setText("You are now member of Lobby: " + lobbyNumber));
      } catch (Exception e) {
        Platform.runLater(() -> selectedLobbyLabel.setText("Lobby needs to be open."));
      }
    }
  }

  /**
   * creates a new lobby
   * @param actionEvent ActionEvent
   */
  public void createLobby(ActionEvent actionEvent) {
    refreshLobbies(actionEvent);
    String lobbyName = createLobbyTextField.getText();
    sendToServer.send(CommandsToServer.CREATELOBBY, lobbyName);
    sendToServer.send(CommandsToServer.PRINTLOBBIES, "");
    refreshLobbies(actionEvent);
    Platform.runLater(() -> createLobbyTextField.clear());
  }
}

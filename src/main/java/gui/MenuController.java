package gui;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utility.io.CommandsToServer;
import utility.io.SendToServer;

import java.net.URL;
import java.util.ResourceBundle;

public class MenuController implements Initializable {

  /**
   * SendToServer object used to communicate with the server
   */
  private final SendToServer sendToServer = new SendToServer();

  /**
   * String which saves a message
   */
  private static String msg;

  private final Logger logger = LogManager.getLogger(MenuController.class);


  private String selectedLobby = "";

  private final double selectedOpacity = 1;
  private final double unselectedOpacity = 0.5;

  /**
   * notes whether the user has joined the chat or not
   */
  public static boolean hasJoinedChat = false;

  /**
   * ToggleGroup used for the ToggleButton in the chat
   */
  public ToggleGroup switchGlobalLobby;
  public ImageView toGameButton;
  public ImageView joinLobbyButton;
  public ImageView createLobbyButton;
  public ImageView sendButton;
  public ImageView globalchatbuttonimage;
  public ImageView lobbychatbuttonimage;
  public ToggleButton globalToggleButton;
  public ToggleButton lobbyToggleButton;

  /**
   * notes whether the user wants to write in global chat
   */
  boolean writeInGlobalChat = false;
  // public static ReceiveFromProtocol receiveFromProtocol = new ReceiveFromProtocol();
  // public static ReceiveFromProtocol lobbyReceiver = new ReceiveFromProtocol();
  // public static ReceiveFromProtocol friendsReceiver = new ReceiveFromProtocol();

  /**
   * String containing all the lobbies
   */
  public static String lobbyList;

  /**
   * String containing all the lobbies and their users
   */
  public static String friendList;

  /**
   * a TextField used to write chat messages
   */
  @FXML
  private TextField chatTextField;

  /**
   * a TextArea used to display sent chat messages
   */
  @FXML
  private TextArea chat;

  /**
   * a quit button
   */
  @FXML
  private Button quitButton;

  /**
   * a ListView showing lobbies
   */
  @FXML
  private ListView<String> lobbyListView;

  /**
   * a TextField used to enter names for new lobbies
   */
  @FXML
  private TextField createLobbyTextField;

  /**
   * a label which displays the selected lobby
   */
  @FXML
  private Label selectedLobbyLabel;

  /**
   * a ListView showing players
   */
  @FXML
  private ListView<String> friendListView;

  public boolean clientIsInLobby = false;


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
    if (msg != null) {
      Platform.runLater(() -> chat.appendText(msg + System.lineSeparator()));
      //chat.appendText("\n");
    }
  }


  /**
   * print all Lobbies into the GUI (ListView)
   *
   * @param lobby String containing all the lobbies
   */
  @FXML
  public synchronized void printLobbies(String lobby) {
    String lobbynum = lobby.substring(0, 1);
    System.out.println("print: " + lobbynum);
    int counter = 0;
    for (String it : lobbyListView.getItems()) {
      // if lobbynumber does match
      if (it.contains(lobbynum)) {
        System.out.println("set: " + lobbynum);
        int finalCounter = counter;
        Platform.runLater(() -> lobbyListView.getItems().set(finalCounter, lobby));
        refreshFriends();
        return;
      }
      counter++;
    }
    // if lobbynumber is not in the list:
    // checks if the lobby to be added is the standardlobby
    System.out.println("not found: " + lobbynum);
    if (lobby.contains("0.")) {
      // adds the standardlobby to the top
      Platform.runLater(() -> lobbyListView.getItems().add(0, lobby));
    } else {
      // puts the lobby right under the standardlobby
      Platform.runLater(() -> lobbyListView.getItems().add(1, lobby));
    }
    refreshFriends();
    /*
    String[] splittedLobbies = splittedString(lobbies);
    Platform.runLater(() -> {
      lobbyListView.getItems().clear();
      lobbyListView.getItems().addAll(splittedLobbies);
    });

     */
  }

  /**
   * creates a new lobby
   */
  public void createLobby() {
    String lobbyName = createLobbyTextField.getText();
    sendToServer.send(CommandsToServer.CREATELOBBY, lobbyName);
    //sendToServer.send(CommandsToServer.PRINTLOBBIES, "");
    refreshFriends();
    Platform.runLater(() -> createLobbyTextField.clear());
  }

  /**
   * asks to print out the lobbies when clicking on the button
   */
  public void refreshLobbies() {
    sendToServer.send(CommandsToServer.PRINTLOBBIES, "");
  }

  /**
   * prints a String into the GUI (ListView) containing all Lobbie and users in the lobby.
   *
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
    lobbySelectionListener();
    createLobbyListener();
    sendListener();

  }

  /**
   * Method which allows users to quit when pressing the quit button
   */
  @FXML
  public void quitGame() {
    Main.exit();
  }


  /**
   * This method is called when one of the togglebuttons is pressed
   * The method sets the variable writeInGlobalChat, which is used to determine if
   * a message is sent in the global chat or the lobba chat
   *
   * @param actionEvent actionEvent
   */
  public void switchChat(MouseEvent actionEvent) {
    if (actionEvent.getSource() == globalchatbuttonimage) {
      writeInGlobalChat = true;
      globalchatbuttonimage.setOpacity(selectedOpacity);
      globalchatbuttonimage.setDisable(true);
      lobbychatbuttonimage.setOpacity(unselectedOpacity);
      lobbychatbuttonimage.setDisable(false);

    } else {
      writeInGlobalChat = false;
      globalchatbuttonimage.setOpacity(unselectedOpacity);
      globalchatbuttonimage.setDisable(false);
      lobbychatbuttonimage.setOpacity(selectedOpacity);
      lobbychatbuttonimage.setDisable(true);
    }
  }


  /**
   * switches to the Game scene when pressing the button
   */
  public void switchToGame() {
    //sendToServer.send(CommandsToServer.SETALLCHARTOKENS, "");
    Main.displayGame();

  }

  /**
   * switches to the Highscore scene when pressing the button
   */
  public void switchToHighscore() {
    Main.displayHighscore();
  }

  /**
   * switches to the Char Selection scene when pressing the button
   */
  public void switchToCharSelection() throws InterruptedException {
    Thread.sleep(500);
    // Main.displayCharSelection();
    sendToServer.send(CommandsToServer.CHECKALLCHARS, "");
    if (clientIsInLobby) {
      Main.displayCharSelectionPopUp();
    } else {
      Main.displayNotInLobbyPopUp();
    }
  }

  public void switchToNameSelection() {
    Main.displayNameSelectionPopUp();
  }

  /**
   * returns the string split at §
   *
   * @param s String
   * @return String[] containing the splitted string
   */
  public String[] splittedString(String s) {
    return s.split("§");
  }

  /**
   * returns the lobbies split at %
   *
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
   *
   * @param str String
   * @return String without the new line attached
   */
  private static String removeNewline(String str) {
    return str.replace("\n", "").replace("\r", "");
  }

  /**
   * shows the selected lobby
   *
   * @return String
   */
  public String listViewSelectedLobby() {
    return lobbyListView.getSelectionModel().getSelectedItem();
  }

  /**
   * joins the lobby selected in the listview
   */
  public void joinSelectedLobby() {
    joinLobbyButton.setDisable(true);
    joinLobbyButton.setOpacity(unselectedOpacity);
    String newSelectedLobby;
    if (!(newSelectedLobby = listViewSelectedLobby()).equals(selectedLobby)) {
      selectedLobby = newSelectedLobby;
      String lobbyNumber = selectedLobby.substring(0, 1);
      try {
        // HERE WE COULD ADD AN IF BLOCK THAT ONLY SENDS CHARSELECT COMMANDS IF LOBBY IS OPEN
        sendToServer.send(CommandsToServer.ENABLECURRENTCHARGUI, "");
        sendToServer.send(CommandsToServer.CHANGECHARACTER, "0");

        Integer.parseInt(lobbyNumber);
        sendToServer.send(CommandsToServer.CHANGELOBBY, lobbyNumber);
        Platform.runLater(() -> selectedLobbyLabel.setText("You are now member of Lobby: " + lobbyNumber));
        clientIsInLobby = true;

        if (selectedLobby.contains("open")) {
          // sendToServer.send(CommandsToServer.CHECKIFCHARSTAKEN, "");
          switchToCharSelection();
        }
        refreshFriends();
      } catch (Exception e) {
        Platform.runLater(() -> selectedLobbyLabel.setText("Lobby needs to be open."));
      }
    }
  }


  private void lobbySelectionListener() {
    lobbyListView.getSelectionModel().selectedItemProperty().addListener((obs, oldv, newv) -> {
      if (newv.contains("open") || newv.contains("going")) {
        if (!newv.equals(selectedLobby)) {
          joinLobbyButton.setDisable(false);
          joinLobbyButton.setOpacity(selectedOpacity);
        } else {
          joinLobbyButton.setDisable(true);
          joinLobbyButton.setOpacity(unselectedOpacity);
        }
        toGameButton.setDisable(false);
        toGameButton.setOpacity(selectedOpacity);
      } else {
        joinLobbyButton.setDisable(true);
        joinLobbyButton.setOpacity(unselectedOpacity);
        toGameButton.setDisable(true);
        toGameButton.setOpacity(unselectedOpacity);
      }
    });
  }

  private void createLobbyListener() {
    EventHandler<ActionEvent> tmp = createLobbyTextField.getOnAction();
    createLobbyTextField.onActionProperty().set(null);
    createLobbyTextField.textProperty().addListener((obs, oldv, newv) -> {
      if (newv.equals("")) {
        createLobbyTextField.onActionProperty().set(null);
        createLobbyButton.setDisable(true);
        createLobbyButton.opacityProperty().set(unselectedOpacity);
        createLobbyTextField.setStyle("-fx-text-fill: black");
      }
      if (newv.matches("[a-zA-Z]+")) {
        createLobbyTextField.onActionProperty().set(tmp);
        createLobbyButton.setDisable(false);
        createLobbyButton.opacityProperty().set(selectedOpacity);
        createLobbyTextField.setStyle("-fx-text-fill: green");
      } else {
        createLobbyTextField.onActionProperty().set(null);
        createLobbyButton.setDisable(true);
        createLobbyButton.opacityProperty().set(unselectedOpacity);
        createLobbyTextField.setStyle("-fx-text-fill: red");
      }
    });
  }

  private void sendListener() {
    EventHandler<ActionEvent> tmp = chatTextField.getOnAction();
    chatTextField.onActionProperty().set(null);
    chatTextField.textProperty().addListener((obs, oldv, newv) -> {
      if (newv.equals("")) {
        sendButton.setDisable(true);
        sendButton.opacityProperty().set(unselectedOpacity);
        chatTextField.onActionProperty().set(null);
      } else {
        sendButton.setDisable(false);
        sendButton.opacityProperty().set(selectedOpacity);
        chatTextField.onActionProperty().set(tmp);
      }
    });
  }


}

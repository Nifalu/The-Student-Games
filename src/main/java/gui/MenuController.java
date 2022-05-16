package gui;

import javafx.application.Platform;
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

  // ---------------------------------- UTILITY ---------------------------------------- //
  /**
   * Set opacity for selected Nodes
   */
  private final double selectedOpacity = 1;

  /**
   * Set opacity for unselected Nodes
   */
  private final double unselectedOpacity = 0.5;

  /**
   * Send a message to the server
   */
  private final SendToServer sendToServer = new SendToServer();

  /**
   * MenuController Logger
   */
  private final Logger logger = LogManager.getLogger(MenuController.class);

  /**
   * saves the current lobby the user is in.
   */
  private String selectedLobby = "";

  /**
   * notes whether the user wants to write in global chat
   */
  private boolean writeInGlobalChat = false;

  /**
   * determines whether the client has already joined a lobby
   */
  private boolean clientIsInLobby = false;

  // ------------------------------------ FXML ---------------------------------------- //

  /**
   * Acts as button that switches the scene to GameScene
   */
  @FXML
  private ImageView toGameButton;
  /**
   * Acts as button that joins the selected lobby
   */
  @FXML
  private ImageView joinLobbyButton;
  /**
   * Acts as button that creates a Lobby
   */
  @FXML
  private ImageView createLobbyButton;
  /**
   * Acts as button that sends a message to the chat
   */
  @FXML
  private ImageView sendButton;
  /**
   * Acts as button that switches to global chat
   */
  @FXML
  private ImageView globalchatbuttonimage;
  /**
   * Acts as button that switches to lobby chat
   */
  @FXML
  private ImageView lobbychatbuttonimage;
  /**
   * a TextField used to write chat messages
   */
  @FXML
  private TextField chatTextField;

  /**
   * a TextField used to enter names for new lobbies
   */
  @FXML
  private TextField createLobbyTextField;

  /**
   * a TextArea used to display sent chat messages
   */
  @FXML
  private TextArea chat;

  /**
   * a label which displays the selected lobby
   */
  @FXML
  private Label selectedLobbyLabel;

  /**
   * a ListView showing lobbies
   */
  @FXML
  private ListView<String> lobbyListView;

  /**
   * a ListView showing players
   */
  @FXML
  private ListView<String> friendListView;


  /**
   * Initializes this class. This method is called when the class is loaded for the first time.
   * It contains basic methods that need to be run from the beginning and can not be called from outside
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

  // ---------------------------------- CHAT ---------------------------------------- //

  /**
   * method reads input from the Textfield and checks, which command to send to the server
   * if there's no command at the start of the message, it will be sent as a chat (which is the main use for this GUI)
   */
  @FXML
  private void sendChatMessage() {

    String msg = (chatTextField.getText());
    if (msg.startsWith("/nick")) {
      String[] split = msg.split(" ", 2);
      String newName = split[1];
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
    }
  }

  /**
   * This method is called when one of the togglebuttons is pressed
   * The method sets the variable writeInGlobalChat, which is used to determine if
   * a message is sent in the global chat or the lobba chat
   *
   * @param actionEvent actionEvent
   */
  @FXML
  private void switchChat(MouseEvent actionEvent) {
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


  // ---------------------------------- LOBBIES ---------------------------------------- //

  /**
   * print all Lobbies into the GUI (ListView)
   *
   * @param lobby String containing all the lobbies
   */
  @FXML
  public synchronized void printLobbies(String lobby) {
    String lobbynum = lobby.substring(0, 1); // retrieves the lobby number from the String

    // Loops through the list and looks if the lobby is already listed
    int counter = 0;
    for (String it : lobbyListView.getItems()) {
      // if lobbynumber does match
      if (it.contains(lobbynum)) {
        int finalCounter = counter;
        Platform.runLater(() -> lobbyListView.getItems().set(finalCounter, lobby));
        refreshFriends();
        return;
      }
      counter++;
    }

    // This part is only reached when the lobby was not found above !

    if (lobby.contains("0.")) { // adds the standardlobby to the top
      Platform.runLater(() -> lobbyListView.getItems().add(0, lobby));
    } else { // puts the lobby right under the standardlobby
      Platform.runLater(() -> lobbyListView.getItems().add(1, lobby));
    }

    // because a new lobby was added, update the friends-list as well.
    refreshFriends();
  }

  /**
   * creates a new lobby
   */
  @FXML
  private void createLobby() {
    String lobbyName = createLobbyTextField.getText();
    sendToServer.send(CommandsToServer.CREATELOBBY, lobbyName);
    //sendToServer.send(CommandsToServer.PRINTLOBBIES, "");
    refreshFriends();
    Platform.runLater(() -> createLobbyTextField.clear());
  }

  /**
   * asks to print out the lobbies when clicking on the button
   */
  @FXML
  public void refreshLobbies() {
    sendToServer.send(CommandsToServer.PRINTLOBBIES, "");
  }


  /**
   * joins the lobby selected in the listview
   */
  @FXML
  private void joinSelectedLobby() {
    // Disable the join button
    joinLobbyButton.setDisable(true);
    joinLobbyButton.setOpacity(unselectedOpacity);

    // check if the selected lobby is not the lobby the user is already in
    // This should always be the case because the button is disabled after joining. So you
    // should not be able to join twice.
    String newSelectedLobby;
    if (!(newSelectedLobby = getlistViewSelectedLobby()).equals(selectedLobby)) {


      selectedLobby = newSelectedLobby;
      String lobbyNumber = selectedLobby.substring(0, 1);
      try {

        Integer.parseInt(lobbyNumber);
        sendToServer.send(CommandsToServer.CHANGELOBBY, lobbyNumber);

        // Label is hidden
        //Platform.runLater(() -> selectedLobbyLabel.setText("You are now member of Lobby: " + lobbyNumber));

        clientIsInLobby = true;

        if (selectedLobby.contains("open")) {
          switchToCharSelection();
        }
        refreshFriends();
      } catch (Exception e) {
        // Label is hidden
        // Platform.runLater(() -> selectedLobbyLabel.setText("Lobby needs to be open."));
      }
    }
  }


  // ---------------------------------- FRIEND-LIST ---------------------------------------- //


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
   * prints out the lounging list
   */
  public void refreshFriends() {
    sendToServer.send(CommandsToServer.PRINTLOUNGINGLIST, "");
  }


  // ---------------------------------- SCENE-SWITCHERS ---------------------------------------- //

  /**
   * switches to the Game scene when pressing the button
   */
  @FXML
  private void switchToGame() {
    //sendToServer.send(CommandsToServer.SETALLCHARTOKENS, "");
    Main.displayGame();

  }

  /**
   * switches to the Highscore scene when pressing the button
   */
  @FXML
  private void switchToHighscore() {
    Main.displayHighscore();
  }

  /**
   * switches to the Char Selection scene when pressing the button
   */
  @FXML
  private void switchToCharSelection() throws InterruptedException {
    Thread.sleep(500);
    // Main.displayCharSelection();
    // sendToServer.send(CommandsToServer.CHECKALLCHARS, "");
    if (clientIsInLobby) {
      Main.displayCharSelectionPopUp();
    } else {
      Main.displayNotInLobbyPopUp();
    }
  }

  @FXML
  private void switchToNameSelection() {
    Main.displayNameSelectionPopUp();
  }

  /**
   * Method which allows users to quit when pressing the quit button
   */
  @FXML
  private void quitGame() {
    Main.exit();
  }

  // ---------------------------------- LISTENERS ---------------------------------------- //

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


  // ---------------------------------- HELPERS ---------------------------------------- //

  /**
   * shows the selected lobby
   *
   * @return String
   */
  private String getlistViewSelectedLobby() {
    return lobbyListView.getSelectionModel().getSelectedItem();
  }

  /**
   * returns the lobbies split at %
   *
   * @param s String containing all lobbies
   * @return String[] containing all lobbies
   */
  private String[] splittedLobbies(String s) {
    return s.split("%");
  }

}

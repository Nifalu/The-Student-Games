package gui;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.fxml.Initializable;
import utility.io.CommandsToServer;
import utility.io.ReceiveFromProtocol;
import utility.io.SendToServer;

import java.net.URL;
import java.util.ResourceBundle;

public class HighscoreController implements Initializable {

  /**
   * ReceiveFromProcotol object to communicate with the protocol
   */
  public static ReceiveFromProtocol winnerReceiver = new ReceiveFromProtocol();

  /**
   * a String containing all winners
   */
  public static String winnerList;

  /**
   * SendToServer object to communicate with the server
   */
  private final SendToServer sendToServer = new SendToServer(); // sends to server

  /**
   * ListView to show the winners of the game
   */
  @FXML
  private ListView<String> winnerListView;


  /**
   * prints out the lobbies winners
   *
   * @param winners String
   */
  @FXML
  public void printLobbies(String winners) {
    String[] splittedWinners = splittedString(winners);
    Platform.runLater(() -> {
      winnerListView.getItems().clear();
      winnerListView.getItems().addAll(splittedWinners);
    });
  }

  /**
   * method is called when the scene is opened
   * atm it's only used to set the HigscoreController
   */
  @Override
  public void initialize(URL location, ResourceBundle resources) {

    Main.setHighscoreController(this);

    /*
    Thread winnerListThread = new Thread(() -> {
      while (true) {

        winnerList = winnerReceiver.receive();
        winnerList = removeNewline(winnerList);
        String[] splittedWinners = splittedString(winnerList);
        Platform.runLater(() -> printLobbies(splittedWinners));
      }
    });
    winnerListThread.setName("winnerListThread");
    winnerListThread.start();


     */
  }

  /**
   * switches to the Menu scene when pressing the button
   */
  public void switchToMenu() {
    Main.displayMenu();
  }

  /**
   * switches to the Game scene when pressing the button
   */
  public void switchToGame() {
    Main.displayGame();
  }

  /**
   * removes the new line attached to a string
   *
   * @param str String
   * @return String without a new line attached
   */
  private static String removeNewline(String str) {
    return str.replace("\n", "").replace("\r", "");
  }

  /**
   * splits the string at §
   *
   * @param s String
   * @return String[] containing the splitted string
   */
  public String[] splittedString(String s) {
    return s.split("§");
  }

  /**
   * refreshes the winners of the lobbies by calling PRINTLOBBIES
   */
  public void refreshWinners() {
    sendToServer.send(CommandsToServer.PRINTLOBBIES, "");
  }
}

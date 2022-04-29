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

  public static ReceiveFromProtocol winnerReceiver = new ReceiveFromProtocol();

  public static String winnerList;

  private final SendToServer sendToServer = new SendToServer();


  @FXML
  private ListView<String> winnerListView;


  @FXML
  public void printLobbies(String[] winners) {
    winnerListView.getItems().clear();
    winnerListView.getItems().addAll(winners);
  }

  /**
   * the following methods are used to switch between scenes
   * they're only temporary
   */

  @Override
  public void initialize(URL location, ResourceBundle resources) {
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

  }

  public void switchToMenu() {
    Main.displayMenu();
  }

  public void switchToGame() {
    Main.displayNewGame();
  }

  private static String removeNewline(String str) {
    return str.replace("\n", "").replace("\r", "");
  }

  public String[] splittedString(String s) {
    return s.split("§");
  }

  public void refreshWinners() {
    sendToServer.send(CommandsToServer.PRINTLOBBIES, "");
  }
}

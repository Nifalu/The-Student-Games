package gui;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.fxml.Initializable;
import javafx.stage.Stage;
import utility.io.CommandsToServer;
import utility.io.SendToServer;

import javafx.scene.media.Media;
import java.net.URL;
import java.util.ResourceBundle;

public class HighscoreController implements Initializable {

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
  public void printWinners(String winners) {
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
  }

  /**
   * switches to the Menu scene when pressing the button
   */
  @FXML
  private void exit() {
    Stage stage = (Stage) winnerListView.getScene().getWindow();
    stage.close();
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
    sendToServer.send(CommandsToServer.PRINTHIGHSCORE, "");
  }
}

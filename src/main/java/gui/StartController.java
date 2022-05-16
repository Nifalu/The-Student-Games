package gui;

import gameLogic.MusicPlayer;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import utility.io.SendToServer;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * this is the controller for fxml_start.fxml
 */
public class StartController implements Initializable {


  public ImageView noImage;
  public ImageView yesImage;

  /**
   * a label which is used to ask for the clients username
   */
  @FXML
  private Label showText;


  /**
   * changes the displayed text on the stage
   *
   * @param msg message
   */
  @FXML
  public void printMsg(String msg) {
    if (msg != null) {
      Platform.runLater(() -> showText.setText(msg));
    }
  }


  /**
   * This method is called, when the class is created
   *
   * @param location  location
   * @param resources resources
   */
  @Override
  public void initialize(URL location, ResourceBundle resources) {

    Main.setStartController(this);
  }


  /**
   * switches to the Menu scene when clicking the button
   */
  public void switchToMenu() {
    Main.displayMenu();
  }


    public void nameNo() {
      Main.displayNameSelectionPopUp();
    }

  public void nameYes() {
    music("audio/whistle.mp3");
    switchToMenu();
  }

  public void music(String music) {
    MusicPlayer mp = new MusicPlayer();
    mp.startMusic(music);
  }
}

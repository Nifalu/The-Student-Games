package gui;

import gameLogic.MusicPlayer;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * this is the controller for fxml_start.fxml
 */
public class StartController implements Initializable {

  /**
   * the image used as a no "button"
   */
  public ImageView noImage;

  /**
   * the image used as a yes "button"
   */
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

  /**
   * shows the name selectino popup when the client isn't happy with their proposed name (they press the no image/button)
   */
  public void nameNo() {
      Main.displayNameSelectionPopUp();
    }

  /**
   * switches the client over to the menu scene, when their happy with their username
   */
  public void nameYes() {
    music("src/main/resources/whistle.wav");
    switchToMenu();
  }

  /**
   * used to add music/sounds to the scene
   * @param music String containing the address of the chosen sound file
   */
  public void music(String music) {
    MusicPlayer mp = new MusicPlayer();
    mp.play(music);
  }
}

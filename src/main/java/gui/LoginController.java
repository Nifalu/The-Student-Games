package gui;

import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.util.converter.IntegerStringConverter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import server.ServerManager;
import server.User;
import starter.Starter;

import java.io.*;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ResourceBundle;

/**
 * the controller for the login scene
 */
public class LoginController implements Initializable {

  /**
   * a list of recently visited addresses
   */
  public ListView<String> recentlist;

  /**
   * the connect button
   */
  public Button connectbutton;

  /**
   * a textfield which is used to enter the port number
   */
  public TextField portfield;

  /**
   * a textfield which is used to enter addresses
   */
  public TextField addressfield;

  /**
   * the delete button
   */
  public Button deletebutton;

  /**
   * a label showing a port warning
   */
  public Label portwarning;

  /**
   * the add button
   */
  public Button addbutton;

  /**
   * a red border for invalid inputs
   */
  private final Border invalidBorder = new Border(
      new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, new CornerRadii(3), new BorderWidths(2)));

  /**
   * an orange border for inputs
   */
  private final Border warnBorder = new Border(
      new BorderStroke(Color.ORANGE, BorderStrokeStyle.SOLID, new CornerRadii(3), new BorderWidths(2)));

  /**
   * a green border for completely vaild inputs
   */
  private final Border validBorder = new Border(
      new BorderStroke(Color.GREEN, BorderStrokeStyle.SOLID, new CornerRadii(3), new BorderWidths(2)));

  /**
   * saves whether the address is ready or not
   */
  private boolean addressIsReady = false;

  /**
   * checks whether the port is ready or not
   */
  private boolean portIsReady = false;

  /**
   * a BooleanProperty which checks if all's ready
   */
  private final BooleanProperty allReady = new SimpleBooleanProperty(false);

  /**
   * the Logger for this class
   */
  private final Logger logger = LogManager.getLogger(LoginController.class);

  /**
   * connect the client to the server
   */
  public void connectToServer() {
    String address = addressfield.getText();
    String port = portfield.getText();
    addToList();
    Starter.address = address;
    Starter.port = Integer.parseInt(port);
    Main.displayIntro();
  }

  /**
   * adds a new address to the recentList
   */
  public void addToList() {
    String addressAndPort = addressfield.getText() + ":" + portfield.getText();
    if (!recentlist.getItems().contains(addressAndPort)) {
      Platform.runLater(() -> recentlist.getItems().add(addressAndPort));
      addLogin(addressAndPort);
    }
  }

  /**
   * removes an address from the recentList
   */
  public void removeFromList() {
    String selected = recentlist.getSelectionModel().getSelectedItem();
    Platform.runLater(() -> recentlist.getItems().remove(selected));
    removeLogin(selected);
  }

  /**
   * this method is called when the scene starts
   * it's mainly used to start all the listeners
   * @param location URL
   * The location used to resolve relative paths for the root object, or
   * {@code null} if the location is not known.
   *
   * @param resources ResourceBundle
   * The resources used to localize the root object, or {@code null} if
   * the root object was not localized.
   */
  @Override
  public void initialize(URL location, ResourceBundle resources) {
    Main.setLoginController(this);
    loadLogins();
    portfieldvalidator();
    addressfieldListener();
    allReadyListener();
    recentlistListener();
  }

  /**
   * this method is used to validate the port
   */
  public void portfieldvalidator() {
    portfield.setTextFormatter(new TextFormatter<>(new IntegerStringConverter()));
    portfield.textProperty().addListener((obs, oldv, newv) -> {
      try {
        portfield.getTextFormatter().getValueConverter().fromString(newv);
        int port = Integer.parseInt(portfield.getText());
        if (port < 0 || port > 65535) {
          portfield.setBorder(invalidBorder);
          portIsReady = false;
          portwarning.setOpacity(0);
        } else if (port < 1024) {
          portfield.setBorder(warnBorder);
          portIsReady = true;
          portwarning.setOpacity(1);
        } else {
          portfield.setBorder(validBorder);
          portIsReady = true;
          portwarning.setOpacity(0);
        }
        allReady.set(portIsReady && addressIsReady);
      } catch (NumberFormatException e) {
        if (portfield.getText().equals("")) {
          portfield.setBorder(null);
        } else {
          portfield.setBorder(invalidBorder);
        }
        portIsReady = false;
        allReady.set(false);
        portwarning.setOpacity(0);
      }
    });
  }

  /**
   * the listener which checks if the input is valid so far
   */
  public void addressfieldListener() {
    addressfield.textProperty().addListener((obs, oldv, newv) -> {
        addressIsReady = !newv.equals("");
        allReady.set(portIsReady && addressIsReady);
        if (newv.equals("")) {
          addressfield.setBorder(null);
        } else {
          addressfield.setBorder(validBorder);
        }
    });
  }

  /**
   * the listener which checks if all's ready yet
   */
  public void allReadyListener() {
    allReady.addListener((obs, oldv, newv) -> {
      if (newv) {
        connectbutton.disableProperty().set(false);
        connectbutton.setOpacity(1);
        addbutton.disableProperty().set(false);
        addbutton.setOpacity(1);
      } else {
        connectbutton.disableProperty().set(true);
        connectbutton.setOpacity(0.5);
        addbutton.disableProperty().set(true);
        addbutton.setOpacity(0.5);
      }
    });
  }

  /**
   * the listener which waits for changes in the recentList
   */
  public void recentlistListener() {
    recentlist.getSelectionModel().selectedItemProperty().addListener((obs,oldv,newv) -> {
      String address;
      String port;
      if (newv != null) {
        deletebutton.setOpacity(1);
        deletebutton.disableProperty().set(false);
        String[] text = recentlist.getSelectionModel().getSelectedItem().split(":");
        address = text[0];
        port = text[1];
      } else {
        deletebutton.setOpacity(0.5);
        deletebutton.disableProperty().set(true);
        address = "";
        port = "";
      }
      Platform.runLater(() -> {
        addressfield.setText(address);
        portfield.setText(port);
      });
    });
  }

  /**
   * this method is used to save logins in a .txt file
   * @param msg String
   */
  private void saveLogins(String msg) {
    try {
      BufferedWriter bw = new BufferedWriter(new FileWriter("gamefiles/utility/logins.txt", false));
      bw.write(msg);
      bw.flush();
      bw.close();
    } catch (IOException e) {
      logger.warn("Users were not saved!");
    }
  }

  /**
   * this method is used to add a new login
   * @param msg String
   */
  private void addLogin(String msg) {
    try {
      BufferedWriter bw = new BufferedWriter(new FileWriter("gamefiles/utility/logins.txt", true));
      bw.write(msg+"!");
      bw.flush();
      bw.close();
    } catch (IOException e) {
      logger.warn("Logins were not saved!");
    }
  }

  /**
   * removes a login
   * @param toDelete String
   */
  private void removeLogin(String toDelete) {
    try {
      BufferedReader br = new BufferedReader(new FileReader("gamefiles/utility/logins.txt"));
      String savedlogins = br.readLine();
      if (savedlogins != null) {
        savedlogins = savedlogins.replace(toDelete + "!", "");
        saveLogins(savedlogins);
      }
      br.close();
    } catch (IOException e) {
      logger.warn("Login was not removed");
    }
  }

  /**
   * loads the logins
   */
  private void loadLogins() {
    try {
      BufferedReader br = new BufferedReader(new FileReader("gamefiles/utility/logins.txt"));
      String s;
      if ((s = br.readLine()) != null) {
        for (String i : s.split("!")) {
          Platform.runLater(() -> recentlist.getItems().add(i));
        }
      }
      br.close();
    } catch (IOException e) {
      logger.warn("Logins could not be loaded");
    }

  }

}

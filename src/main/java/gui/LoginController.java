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

public class LoginController implements Initializable {


  public ListView<String> recentlist;
  public Button connectbutton;
  public TextField portfield;
  public TextField addressfield;
  public Button deletebutton;
  public Label portwarning;
  public Button addbutton;

  private final Border invalidBorder = new Border(
      new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, new CornerRadii(3), new BorderWidths(2)));
  private final Border warnBorder = new Border(
      new BorderStroke(Color.ORANGE, BorderStrokeStyle.SOLID, new CornerRadii(3), new BorderWidths(2)));
  private final Border validBorder = new Border(
      new BorderStroke(Color.GREEN, BorderStrokeStyle.SOLID, new CornerRadii(3), new BorderWidths(2)));


  private boolean addressIsReady = false;
  private boolean portIsReady = false;
  private final BooleanProperty allReady = new SimpleBooleanProperty(false);
  private final Logger logger = LogManager.getLogger(LoginController.class);

  public void connectToServer() {
    String address = addressfield.getText();
    String port = portfield.getText();
    addToList();
    Starter.address = address;
    Starter.port = Integer.parseInt(port);
    Main.displayStart();
  }

  public void addToList() {
    String addressAndPort = addressfield.getText() + ":" + portfield.getText();
    if (!recentlist.getItems().contains(addressAndPort)) {
      Platform.runLater(() -> recentlist.getItems().add(addressAndPort));
      addLogin(addressAndPort);
    }
  }

  public void removeFromList() {
    String selected = recentlist.getSelectionModel().getSelectedItem();
    Platform.runLater(() -> recentlist.getItems().remove(selected));
    removeLogin(selected);
  }


  @Override
  public void initialize(URL location, ResourceBundle resources) {
    Main.setLoginController(this);
    loadLogins();
    portfieldvalidator();
    addressfieldListener();
    allReadyListener();
    recentlistListener();
  }

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

  private void removeLogin(String toDelete) {
    try {
      BufferedReader br = new BufferedReader(new FileReader("gamefiles/utility/logins.txt"));
      String savedlogins = br.readLine();
      if (savedlogins != null) {
        savedlogins = savedlogins.replace(toDelete + "!", "");
        saveLogins(savedlogins);
      }
    } catch (IOException e) {
      logger.warn("Login was not removed");
    }
  }

  private void loadLogins() {
    try {
      BufferedReader br = new BufferedReader(new FileReader("gamefiles/utility/logins.txt"));
      String s;
      if ((s = br.readLine()) != null) {
        for (String i : s.split("!")) {
          Platform.runLater(() -> recentlist.getItems().add(i));
        }
      }
    } catch (IOException e) {
      logger.warn("Logins could not be loaded");
    }

  }

}

package starter;

import client.ClientManager;
import server.GameServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utility.Exceptions;
import utility.io.Uuid;

import java.io.File;

/**
 * Takes commandline parameters and starts the program
 */
public class Starter {

  /**
   * the logger
   */
  private static final Logger logger = LogManager.getLogger(Starter.class);

  /**
   * saves arguments in a string array
   */
  private static String[] args;

  /**
   * the clients username
   */
  private static String username = System.getProperty("user.name");

  /**
   * saves whether reconnects are possible
   */
  private static boolean reconnect = true;

  /**
   * the server address
   */
  public static String address;

  /**
   * the server port
   */
  public static int port;

  /**
   * saves if the server/client has started
   */
  public static boolean isArgumentStart;

  /**
   * Starts either the server or the client with the given arguments
   *
   * @param args arguments
   */
  public static void main(String[] args) {
    Starter.args = args;

    File folder = new File("gamefiles/utility/");
    folder.mkdirs();


    // Else it checks if there are enough parameters to run the program
    if (args.length < 2) {
      isArgumentStart = false;
      autostart();
    } else {
      isArgumentStart = true;
      argumentstart();
    }
  }

  /**
   * starts the server/client
   */
  private static void argumentstart() {
    switch (args[0].toLowerCase()) {

      case "server":
        if (isPortAllowed(args[1])) {
          // server.Main.start(Integer.parseInt(args[1]));
          logger.info("starting server with port: " + args[1]);
          GameServer.runGameServer(Integer.parseInt(args[1]));
        }
        break;

      case "client":
        // Starts the GUI
        String[] addressAndPort = args[1].split(":", 2);
        if (addressAndPort.length == 2 && isPortAllowed(addressAndPort[1])) {
          address = addressAndPort[0];
          port = Integer.parseInt(addressAndPort[1]);

          // Interprets the commandline arguments
          switch (args.length) {
            case 4:
              if (args[3].equals("-1")) {
                reconnect = false;
              }
              break;

            case 3:
              if (args[2].equals("-1")) {
                reconnect = false;
              } else {
                username = args[2];
                if (!checkUsername()) {
                  return;
                }
              }
              break;
          }
        }
        startGui();
    }
  }

  /**
   * If no arguments are given, the game will start and connect to the predefined Server.
   * Run the program with "server" as the only argument to automatically run the server on the right port for autostart to work
   */
  private static void autostart() {
    if (args.length == 1 && args[0].equals("-1")) {
      reconnect = false;
    }
    System.out.println("autostart client");
    startGui();
  }

  /**
   * starts the GUI
   */
  private static void startGui() {
    Thread guiStarter = new Thread(() -> gui.Launcher.main(new String[0]));
    guiStarter.start();
    guiStarter.setName("guiStarterThread");
  }

  /**
   * connects the client
   */
  public static void connect() {
    String uuid = Uuid.getUUID();
    if (reconnect) {
      ClientManager.runClientManager(address, port, username + "!" + uuid);
    } else {
      ClientManager.runClientManager(address, port, username + "!" + Uuid.generateType1UUID());
    }
  }


  /**
   * checks if the given port (string) is a valid port number (int) and returns true/false
   *
   * @param value port String
   * @return boolean
   */
  private static boolean isPortAllowed(String value) {
    try {
      int portValue = Integer.parseInt(value);
      if (portValue < 65536 && portValue > 0) {
        return true;
      } else {
        Exceptions.StarterIllegalPortNumber(portValue);
        logger.error("value for portNumber is no valid port number. ( " + portValue + ")");
        return false;
      }
    } catch (NumberFormatException e) {
      Exceptions.StarterCannotResolvePortNumber(value);
      logger.error("value for portNumber is no valid number. ( " + value + ")");
      return false;
    }
  }

  /**
   * checks whether the username is too long or not and replaces symbols which aren't allowed
   * @return boolean
   */
  private static boolean checkUsername() {
    if (username.length() > 20) {
      System.out.println("username is too long");
      return false;
    }
    username = username.replace("!", "");
    username = username.replace(",", "");
    username = username.replace("§", "");
    username = username.replace("@", "");
    username = username.replace(";", "");
    username = username.replace("-", "");
    username = username.replace(":", "");
    username = username.replace("?", "");
    username = username.replace(" ", "");
    return true;
  }
}

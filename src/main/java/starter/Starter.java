package starter;

import client.ClientManager;
import server.GameServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utility.Exceptions;

/**
 * Takes commandline parameters and starts the programm.
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
   * Starts either the server or the client with the given arguments
   *
   * @param args arguments
   */
  public static void main(String[] args) {
    Starter.args = args;

    if (args.length < 2) {
      Exceptions.StarterArgumentError();
      return;
    }

    switch (args[0].toLowerCase()) {

      case "server":
        logger.info("------------------------------------------------------------------");
        logger.info("------------------------Starting server---------------------------");
        logger.info("------------------------------------------------------------------");
        if (isPortAllowed(args[1])) {
          // server.Main.start(Integer.parseInt(args[1]));
          logger.info("starting server with port: " + args[1]);
          GameServer.runGameServer(Integer.parseInt(args[1]));


        }
        break;

      case "client":
        logger.info("------------------------------------------------------------------");
        logger.info("------------------------Starting client---------------------------");
        logger.info("------------------------------------------------------------------");

        // Starts the GUI
        Thread guiStarter = new Thread(() -> gui.Launcher.main(new String[0]));
        guiStarter.start();
        guiStarter.setName("guiStarterThread");


    }
  }

  /**
   * connect a new user to the server
   */
  public static void connect() {
    String[] address = args[1].split(":", 2);
    if (address.length > 1) {
      if (isPortAllowed(address[1])) {
        if (args.length > 2) {
          // Start the client with the given username
          logger.info("connecting client to: " + address[0] + ":" + Integer.parseInt(address[1]) + "with username: " + args[2]);
          ClientManager.runClientManager(address[0], Integer.parseInt(address[1]), args[2]);
        } else {
          // Start the client with the users Home Directory Name as username
          logger.info("connecting client to: " + address[0] + ":" + Integer.parseInt(address[1]) + "with username: user.name");
          ClientManager.runClientManager(address[0], Integer.parseInt(address[1]), System.getProperty("user.name"));
        }
      }
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
      if (portValue < 65535 && portValue > 2000) {
        return true;
      } else {
        Exceptions.StarterIllegalPortNumber(portValue);
        logger.error("value for portNumber is no valid port number. ( " + portValue + ")");
        return false;
      }
    } catch (NumberFormatException e) {
      Exceptions.StarterCannotResolvePortNumber(e, value);
      logger.error("value for portNumber is no valid number. ( " + value + ")");
      return false;
    }
  }
}

package Starter;

import Client.ClientManager;
import Server.GameServer;
import utility.Exceptions;

public class Starter {


  public static void main(String[] args) {

    if (args.length < 2) {
      Exceptions.StarterArgumentError();
      return;
    }

    switch (args[0].toLowerCase()) {

      case "server":
        if (isPortAllowed(args[1])) {
          // Server.Main.start(Integer.parseInt(args[1]));
          GameServer.runGameServer(Integer.parseInt(args[1]));
        }
        break;

      case "client":
        String[] address = args[1].split(":", 2);
        if (address.length > 1) {
          if (isPortAllowed(address[1])) {
            if (args.length > 2) {
              // Start the Client with the given username
              ClientManager.runClientManager(address[0], Integer.parseInt(address[1]), args[2]);
            } else {
              // Start the Client with the users Home Directory Name as username
              ClientManager.runClientManager(address[0], Integer.parseInt(address[1]), System.getProperty("user.name"));
            }
            // Starts the GUI
            gui.Launcher.main(new String[0]);
          }
        }
        break;
    }
  }

  private static boolean isPortAllowed(String value) {
    try {
      int portValue = Integer.parseInt(value);
      if (portValue < 65535 && portValue > 2000) {
        return true;
      } else {
        Exceptions.StarterIllegalPortNumber(portValue);
        return false;
      }
    } catch (NumberFormatException e) {
      Exceptions.StarterCannotResolvePortNumber(e, value);
      return false;
    }
  }
}

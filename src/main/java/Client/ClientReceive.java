package Client;

import gui.FXMLExample;
import gui.FXMLExampleController;
import javafx.fxml.FXMLLoader;
import utility.IO.CommandsToClient;


/**
 * ClientReceive processes and validates incoming Messages.
 */
public class ClientReceive {


  /**
   * Takes a String and splits it at the first "--". The first part is the command and is validated
   * with the command enum before it's processed in a switch-case.
   *
   * @param line String
   */
  public synchronized static void receive(String line) {
    // splits the line:
    String msg;
    String[] input;
    input = line.split("--", 2);

    // prevents a null-pointer when only a command was received
    if (input.length > 0) {
      msg = input[1];
    } else {
      msg = line;
    }

    // validates the Command
    CommandsToClient cmd = CommandsToClient.valueOf(input[0]);

    // processes the Command
    switch (cmd) {

      case PRINT: // prints the msg
        System.out.println(msg);
        System.out.println("in print case");

        // ich set e static String chatmsg. --> funktioniert
        FXMLExampleController.chatmsg = msg;

        // ich ruef e static method uf --> funktioniert ned
        // FXMLExampleController.receiveFromProtocol.setMessage((msg));

        System.out.println("finished print case");
        break;

      case PING: // Sends a Ping to the Connection Monitor of the Client
        ClientManager.connectionToServerMonitor.newPing(msg);
        break;

      case INITIALPING: // Sends a first initial Ping to the Connection Monitor
        ClientManager.connectionToServerMonitor.start(msg);
        break;

    }
  }
}



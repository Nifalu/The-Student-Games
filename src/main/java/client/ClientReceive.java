package client;

import gui.HighscoreController;
import gui.StartController;
import gui.GameController;
import utility.io.CommandsToClient;
import gui.MenuController;


/**
 * ClientReceive processes and validates incoming Messages.
 */
public class ClientReceive implements Runnable{

  String line;

  /**
   * receives an incoming message
   */
  ClientReceive(String line) {
    this.line = line;
  }
  /**
   * Takes a String and splits it at the first "--". The first part is the command and is validated
   * with the command enum before it's processed in a switch-case.
   */
  @Override
  public void run() {
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
        outPrint(msg);
        break;

      case PING: // Sends a Ping to the Connection Monitor of the Client
        ClientManager.connectionToServerMonitor.newPing(msg);
        break;

      case INITIALPING: // Sends a first initial Ping to the Connection Monitor
        try {
          ClientManager.connectionToServerMonitor.start(msg);
        } catch (Exception e) {
          System.out.println("Our Server is overloaded right now. Please try again.");
        }
        break;

      case CHAT: // Sends a Chat into the console and also to the chat-gui
        System.out.println(msg);
        GameController.receiveFromProtocol.setMessage(msg); // the chat field in the game receives the message
        MenuController.receiveFromProtocol.setMessage(msg);
        break;

      case LOBBYCHAT: // Sends a Chat into the console and also to the chat-gui
        System.out.println(msg);
        GameController.receiveFromProtocol.setMessage(msg); // the chat field in the game receives the message
        break;

      case PRINTGUISTART:
        System.out.println(msg);
        StartController.receiveFromProtocol.setMessage(msg);
        break;

      case PRINTGUIGAMETRACKER:
        System.out.println(msg);
        GameController.receiveFromProtocolGameUpdate.setMessage(msg);
        outPrint(msg);
        break;

      case GUIMOVECHARACTER:
        GameController.receiveNewPlayerPosition.setMessage(msg);
        break;

      case PRINTLOBBIESGUI:
        MenuController.lobbyReceiver.setMessage(msg);
        break;

      case PRINTWINNERSGUI:
        HighscoreController.winnerReceiver.setMessage(msg);
        break;

      case PRINTFRIENDSGUI:
        MenuController.friendsReceiver.setMessage(msg);
        break;
      }
    }
  public void outPrint (String msg) {
    String[] msgs = msg.split("§");
    for (String s : msgs) {
      System.out.println(s);
    }
  }
}



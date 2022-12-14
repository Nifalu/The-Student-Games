package client;

import gameLogic.MusicPlayer;
import gui.Main;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utility.io.CommandsToClient;


/**
 * ClientReceive processes and validates incoming Messages.
 */
public class ClientReceive implements Runnable {

  /**
   * a line of input
   */
  String line;

  /**
   * receives an incoming message
   */
  ClientReceive(String line) {
    this.line = line;
  }

  /**
   * the logger
   */
  Logger logger = LogManager.getLogger(ClientReceive.class);

  /**
   * Takes a String and splits it at the first "--". The first part is the command and is validated
   * with the command enum before it's processed in a switch-case.
   */
  @Override
  public void run() {
    process();
  }

  /**
   * handles the incoming commands
   * descriptions of each command can be found in the corresponding enum
   */
  private synchronized void process() {
    // splits the line:
    String msg;
    String[] input;
    input = line.split("--", 2);

    // prevents a null-pointer when only a command was received
    if (input.length > 1) {
      msg = input[1];
    } else {
      logger.warn("received command with no message");
      return;
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

        Main.getGameController().printChatMessage(msg);
        Main.getMenuController().printChatMessage(msg);

        break;

      case PRINTGUISTART:
        System.out.println(msg);
        //StartController.receiveFromProtocol.setMessage(msg);
        Main.getStartController().printMsg(msg);
        break;

      case PRINTGUIGAMETRACKER:
        //GameController.receiveFromProtocolGameUpdate.setMessage(msg);
        Main.getGameController().printGameUpdate(msg);
        outPrint(msg);
        break;

      case GUIMOVECHARACTER:
        //GameController.receiveNewPlayerPosition.setMessage(msg);
        Main.getGameController().movePlayer(msg);
        break;

      case PRINTLOBBIESGUI:
        //MenuController.lobbyReceiver.setMessage("split " + msg);
        Main.getMenuController().printLobbies(msg);
        break;

      case PRINTWINNERSGUI:
        //HighscoreController.winnerReceiver.setMessage(msg);
        Main.getHighscoreController().printWinners(msg);
        break;

      case PRINTFRIENDSGUI:
        //MenuController.friendsReceiver.setMessage(msg);
        Main.getMenuController().printFriends(msg);
        break;

      case DICEDICELEFT:
        Main.getGameController().checkFourDiceLeft(msg);
        break;

      case DISABLECHARGUI:
        // System.out.println("s s??tt de char " + msg + " disabled werde");
        Main.getCharSelectionController().disableCharOnScreen(msg);
        break;

      case ENABLECHARGUI:
        Main.getCharSelectionController().enableCharOnScreen(msg);
        break;

      case SETCHARTOKEN:
        // splits the message
        // part one: the new character image
        // part two: which game token
        String[] charAndToken = msg.split("--");
        int charNr = Integer.parseInt(charAndToken[0]);
        int tokenNr = Integer.parseInt(charAndToken[1]);
        Main.getGameController().setCharToken(tokenNr, charNr);
        break;

      case MUSIC:
        MusicPlayer mp = new MusicPlayer();
        mp.play(msg);
        break;

      case MARKPLAYER:
        Main.getGameController().markPlayer(msg);
        break;

      case NAME:
        Main.getMenuController().setNamelabel(msg);
        Main.getGameController().setNamelabel(msg);
        break;

      case YOURTURN:
        Main.getGameController().setMyTurn(true);
        break;

      case YOURQUIZ:
        Main.getGameController().setMyQuiz(true);
    }
  }

  /**
   * splits the String at ??
   *
   * @param msg String
   */
  public void outPrint(String msg) {
    String[] msgs = msg.split("??");
    for (String s : msgs) {
      System.out.println(s);
    }
  }
}



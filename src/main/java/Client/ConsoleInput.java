package Client;

import utility.IO.CommandsToServer;
import utility.IO.SendToServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * Reads input from Console and forwards it to ClientReceive for processing.
 */
class ConsoleInput implements Runnable {

  private volatile boolean stop = false;
  private final SendToServer sendToServer = new SendToServer();

  /**
   * Buffered reader reads in all the console inputs while boolean stop equals false.
   */
  @Override
  public synchronized void run() {
    try {

      // InputStream to read user-input is created:
      BufferedReader consoleIn = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));
      String line;

      while (!stop) {
        wait(1); // for more efficiency

        // continuously reading User-Input from Console and forwarding it to the send method
        if (consoleIn.ready()) {
          line = consoleIn.readLine();
          send(line);
        }
      }
      System.out.println("ConsoleInput Thread closed");
    } catch (IOException | InterruptedException e) {
      e.printStackTrace();
    }
  }

  /**
   * transform the given message to the protocol format
   * @param line the line of code that is going to be sent
   */
  public void send(String line) {
    String[] input;
    String msg;
    input = line.split(" ", 2); // defines where the input is split

    if (input.length > 1) { // prevents a null pointer when only a command is sent
      msg = input[1];
    } else {
      msg = "-1";
    }

    switch (input[0]) {

      case "/quit": // disconnects the client
        sendToServer.send(CommandsToServer.QUIT, msg);
        break;

      case "/name": // sends the message to the name class
        if (msg.equals("-1")) {
          System.out.println("You need to add a message");
          break;
        }
        sendToServer.send(CommandsToServer.NAME, msg);
        break;

      case "/nick": // changes the username
        if (msg.equals("-1")) {
          System.out.println("You need to add a name");
          break;
        }
        sendToServer.send(CommandsToServer.NICK, msg);
        break;

      case "/whisper": // sends a private chat to someone
        String[] split = msg.split(" ", 2);
        if (split.length > 1) {
          msg = split[0] + "-" + split[1];
        } else {
            System.out.println("You cannot whisper nothing!");
            break;
          }
        sendToServer.send(CommandsToServer.WHISPER, msg);
        break;

      case "/chat": // sends a chat to everyone
        if (msg.equals("-1")) {
          System.out.println("You cannot chat nothing!");
          break;
        }
        sendToServer.send(CommandsToServer.CHAT, msg);
        break;

      case "/lobbychat": // sends a chat to lobby
        if (msg.equals("-1")) {
          System.out.println("You cannot chat nothing!");
          break;
        }
        sendToServer.send(CommandsToServer.LOBBYCHAT, msg);
        break;

      case "/lobby":
        msg = msg.replaceAll("\\s", "");
        if (msg.equals("-1") || msg.equals("")) {
          System.out.println("We need a message!");
          break;
        }
        sendToServer.send(CommandsToServer.LOBBY, msg);
        break;

      case "/createLobby":
        msg = msg.replaceAll("\\s", "");
        if (msg.equals("-1") || msg.equals("")) {
          System.out.println("We need a message!");
          break;
        }
        sendToServer.send(CommandsToServer.CREATELOBBY, msg);
        break;

      case "/printUserList":
        sendToServer.send(CommandsToServer.PRINTUSERLIST, msg);
        break;

      case "/printLobbies":
        sendToServer.send(CommandsToServer.PRINTLOBBIES, msg);
        break;

      case "/printLoungingList":
        sendToServer.send(CommandsToServer.PRINTLOUNGINGLIST, msg);
        break;

      case "/printOpenLobbies":
        sendToServer.send(CommandsToServer.PRINTOPENLOBBIES, msg);
        break;

      case "/printFinishedLobbies":
        sendToServer.send(CommandsToServer.PRINTFINISHEDLOBBIES, msg);
        break;

      case "/printOnGoingLobbies":
        sendToServer.send(CommandsToServer.PRINTONGOINGLOBBIES, msg);
        break;

      case "/printHighscore":
        sendToServer.send(CommandsToServer.PRINTHIGHSCORE, msg);
        break;

      case "/changeLobby":
        sendToServer.send(CommandsToServer.CHANGELOBBY, msg);
        break;

      case "/printPlayersInLobby":
        sendToServer.send(CommandsToServer.PRINTPLAYERSINLOBBY, "");
        break;

      case "/ready":
        sendToServer.send(CommandsToServer.READY, "");
        break;

      case "/unready":
        sendToServer.send(CommandsToServer.UNREADY, "");
        break;

      case "/start":
        sendToServer.send(CommandsToServer.START, "");
        break;

      case "/rolldice":
        sendToServer.send(CommandsToServer.ROLLDICE, "");
        break;

      case "/dicedice":
        sendToServer.send(CommandsToServer.DICEDICE, "");
        break;

      case "/quiz":
        sendToServer.send(CommandsToServer.QUIZ, msg);
        break;

      case "/winnerwinnerchickendinner":
        sendToServer.send(CommandsToServer.WWCD, msg);
        break;

      default: // when no command is detected, Echo the message.
        sendToServer.send(CommandsToServer.ECHO, line);

    }
  }

  /**
   * Requests to stop the thread.
   */
  public void requestStop() {
    stop = true;
  }
}

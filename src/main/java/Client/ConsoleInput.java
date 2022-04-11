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

  // transforms the given message to the protocol format
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

      case "/lobby":
        if (msg.equals("-1")) {
          System.out.println("We need a message!");
          break;
        }
        sendToServer.send(CommandsToServer.LOBBY, msg);
        break;

      case "/ctlb":
        if (msg.equals("-1")) {
          System.out.println("We need a message!");
          break;
        }
        sendToServer.send(CommandsToServer.CREATELOBBY, msg);
        break;

      case "/ready":
        sendToServer.send(CommandsToServer.READY, "");
        break;

      case "/start":
        sendToServer.send(CommandsToServer.START, "");
        break;

      default: // when no command is detected, Echo the message.
        sendToServer.send(CommandsToServer.ECHO, line);

    }

  }

  // requests to stop the thread.
  public void requestStop() {
    stop = true;
  }
}

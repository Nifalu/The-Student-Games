package server;

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * creates a thread for the console input
 */
public class ServerConsoleIn implements Runnable {

  /**
   * checks if the thread has stopped
   */
  private boolean stop = false;

  /**
   * starts the ConsoleIn
   */
  @Override
  public synchronized void run() {
    try {
      // InputStream to read user-input is created:
      BufferedReader consoleIn = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));
      String line;

      while (!stop) {
        wait(100); // for more efficiency
        // continuously reading User-Input from Console
        if (consoleIn.ready()) {
          line = consoleIn.readLine();
          if (line.equalsIgnoreCase("stop")) {
            consoleIn.close();
            GameServer.closeGameServer();
            break;
          }
        }
      }
    } catch (IOException | InterruptedException e) {
      e.printStackTrace();
    }
  }

  /**
   * stops the thread
   */
  public void stop() {
    stop = true;
  }
}

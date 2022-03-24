package Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ConsoleInput implements Runnable{

  ClientProtokoll clientProtocol;

  public ConsoleInput(ClientProtokoll clientProtocol) {
    this.clientProtocol = clientProtocol;
  }

  @Override
  public void run() {
    try {

      // InputStream to read user-input is created:
      BufferedReader consoleIn = new BufferedReader(new InputStreamReader(System.in));
      String line;

      while (true) {
        Thread.sleep(1); // for more efficiency

        // Reading User-Input from Console
        line = consoleIn.readLine();

        // Sends Console Input to the Protocol for further processing
        clientProtocol.sendToServer(line);

        // break statement to leave the loop and disconnect
        if (line.equalsIgnoreCase("/quit")) {
          break;
        }

      }
    } catch (IOException | InterruptedException e) {
      e.printStackTrace();
    }
  }
}

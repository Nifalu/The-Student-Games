package Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class ConsoleInput implements Runnable{

  ClientProtokoll clientProtocol;
  private volatile boolean stop = false;

  public ConsoleInput(ClientProtokoll clientProtocol) {
    this.clientProtocol = clientProtocol;
  }

  @Override
  public void run() {
    try {

      // InputStream to read user-input is created:
      BufferedReader consoleIn = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));
      String line;

      while (!stop) {
        Thread.sleep(0,200000); // for more efficiency

        // Reading User-Input from Console and sending it to the protocol
        if (consoleIn.ready()) {
          line = consoleIn.readLine();
          clientProtocol.sendToServer(line);
        }
      }
      System.out.println("ConsoleInput Thread closed");
    } catch (IOException | InterruptedException e) {
      e.printStackTrace();
    }
  }

  // requests to stop the thread.
  public void requestStop() {
    stop = true;
  }
}

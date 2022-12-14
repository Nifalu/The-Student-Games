package server;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * Receives incoming messages from the client and sends them to ServerReceive for processing
 */
public class ClientHandlerIn implements Runnable {

  /**
   * the input stream
   */
  private final BufferedReader in;

  /**
   * the corresponding clienthandler
   */
  private final ClientHandler clientHandler;

  /**
   * notes whether stopped
   */
  private volatile boolean stop = false;

  /**
   * creates a new ClientHandlerIn
   *
   * @param clientHandler ClientHandler
   * @param in            BufferedReader
   */
  ClientHandlerIn(ClientHandler clientHandler, BufferedReader in) {
    this.clientHandler = clientHandler;
    this.in = in;
  }

  /**
   * Runs this Thread and starts waiting for incoming messages to give over to the ServerReceive
   */
  @Override
  public void run() {
    // continuously waits for incoming messages and sends them to ServerReceive for processing
    String msg;
    while (!stop) {
      msg = receive();
      ServerReceive protocol = new ServerReceive(clientHandler, msg);
      Thread ServerReceiveThread = new Thread(protocol);
      ServerReceiveThread.start();
      ServerReceiveThread.setName("ServerReceiveThread");
    }
    System.out.println("clientHandlerIn finished");
  }

  /**
   * Waits until a message is received from the client, returns that message
   *
   * @return String received Message
   */
  public synchronized String receive() {
    String line = "";
    try {
      // receiving loop
      while (!stop) {
        if (in.ready()) {
          line = in.readLine();
          break;
        }
        wait(0, 200000);
      }
      return line;

      // if connection fails
    } catch (IOException e) {

      if (clientHandler.user != null) {
        // error message when user is known
        System.out.println("cannot receive from " + clientHandler.user.getUsername());
      } else {
        // error message if user is unknown
        System.out.println("cannot receive from user");
      }
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    return null;
  }


  /**
   * stops possible infinite loops to finish the Thread
   */
  public void requestStop() {
    stop = true;
  }
}

package Client;


import java.io.*;
import java.net.Socket;


/**
 * This is the GameClient. Right now it only reads the keyboard input from the console and sends it to the Server.
 */
public class GameClient {

  String serverAddress;
  int serverPort;

  public GameClient(String serverAddress, int serverPort) {
    this.serverAddress = serverAddress;
    this.serverPort = serverPort;

  }

  public void startClient() {
    try {
      // Connection to the server is made and in/out streams are created:
      Socket socket = new Socket(serverAddress, serverPort);
      InputStream in = socket.getInputStream();
      OutputStream out = socket.getOutputStream();

      // Thread to handle incoming data is started:
      InThread th = new InThread(socket, in, out);
      Thread clientIn = new Thread(th);
      clientIn.start();

      // InputStream to read user-input is created:
      BufferedReader consoleIn = new BufferedReader(new InputStreamReader(System.in));
      String line;

      while (true) {
        // Reading User-Input from Console
        line = consoleIn.readLine();

        // break statement to leave the loop and disconnect
        if (line.equalsIgnoreCase("QUIT")) {
          break;
        }

        // Sending Client Input to Server

        out.write((line + ';').getBytes());
      }

      // disconnects the Streams and closes the socket
      disconnect(socket, in, out);

    } catch (IOException e) {
      e.printStackTrace();
    }
  }


  /**
   * Does Everything that needs to be done when the client disconnects.
   */
  public void disconnect(Socket socket, InputStream in, OutputStream out) {
    System.out.println("terminating...");
    try {
      if (in != null) {
        in.close();
      }
      if (out != null) {
        out.close();
      }
      if (socket != null) {
        socket.close();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }


}

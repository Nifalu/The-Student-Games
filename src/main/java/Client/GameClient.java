package Client;


import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;


/**
 * This is the GameClient. Right now it only reads the keyboard input from the console and sends it to the Server.
 */
public class GameClient {

  // connection
  String serverAddress;
  int serverPort;
  Socket socket;

  //streams
  BufferedReader in;
  BufferedWriter out;

  // threads
  InThread inThread;
  Thread clientIn;
  ConsoleInput consoleInput;
  Thread conin;
  ConnectionToServerMonitor  connectionToServerMonitor;
  Thread pingpong;

  // other
  ClientProtokoll clientProtocol;


  public GameClient(String serverAddress, int serverPort) {
    this.serverAddress = serverAddress;
    this.serverPort = serverPort;
    this.clientProtocol = new ClientProtokoll(this);

    // Connection to the server is made and in/out streams are created:
    try {
      this.socket = new Socket(serverAddress, serverPort);
      this.in = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
      this.out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8));

      // Thread to handle incoming data:
      inThread = new InThread(socket, in, clientProtocol);
      clientIn = new Thread(inThread);
      clientIn.start();


      // Thread to read console Input:
      consoleInput = new ConsoleInput(clientProtocol);
      conin = new Thread(consoleInput);
      conin.start();

      // Ping-Pong Thread:
      connectionToServerMonitor = new ConnectionToServerMonitor(this, clientProtocol);
      pingpong = new Thread(connectionToServerMonitor);
      pingpong.start();

    } catch (IOException e) {
      disconnect();
    }

  }



  /**
   * Does Everything that needs to be done when the client disconnects.
   */
  public void disconnect() {
    System.out.println("terminating...");
    try {
      Thread.sleep(10);
      if (pingpong.isAlive()) {
        connectionToServerMonitor.requestStop();
      }
      if (conin.isAlive()) {
        consoleInput.requestStop();
      }
      if (clientIn.isAlive()) {
        inThread.requestStop();
      }
      if (in != null) {
        in.close();
      }
      if (out != null) {
        out.close();
      }
      if (socket != null) {
        socket.close();
      }
    } catch (IOException | InterruptedException e) {
      e.printStackTrace();
    }
  }


}

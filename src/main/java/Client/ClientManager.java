package Client;


import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;


/**
 * The ClientManager connects to the Server and starts all the needed Tasks (Threads, Methods etc) to run the Client.
 */
public class ClientManager {

  // connection
  private static Socket socket;

  //streams
  private static BufferedReader in;
  private static BufferedWriter out;

  // threads
  private static InThread inThread;
  private static Thread clientIn;
  private static ConsoleInput consoleInput;
  private static Thread conin;
  protected static ConnectionToServerMonitor connectionToServerMonitor;
  private static Thread pingpong;


  public static void connect(String serverAddress, int serverPort) {

    // Connection to the server is made and in/out streams are created:
    try {
      socket = new Socket(serverAddress, serverPort);
      in = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
      out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8));
      out.write(System.getProperty("user.name"));
      out.newLine();
      out.flush();

      // Thread to handle incoming data:
      inThread = new InThread(in);
      clientIn = new Thread(inThread);
      clientIn.setName("clientIn");
      clientIn.start();


      // Thread to read console Input:
      consoleInput = new ConsoleInput();
      conin = new Thread(consoleInput);
      conin.setName("ConsoleIn");
      conin.start();

      // Ping-Pong Thread:
      connectionToServerMonitor = new ConnectionToServerMonitor();
      pingpong = new Thread(connectionToServerMonitor);
      pingpong.setName("Client-pingpong");
      pingpong.start();

    } catch (IOException e) {
      disconnect();
    }

  }


  /**
   * disconnect() makes sure that everything is closed when the client disconnects from the Server.
   * It closes all the Threads, Streams and the Socket.
   */
  public static void disconnect() {
    System.out.println("terminating...");
    try {
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
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  // Getters And Setters:
  public static BufferedWriter getOut() {
    return out;
  }

}

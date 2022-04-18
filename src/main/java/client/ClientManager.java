package client;

import javafx.application.Platform;
import utility.Exceptions;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;


/**
 * The ClientManager connects to the server and starts all the needed Tasks (Threads, Methods etc) to run the client.
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

  public static void runClientManager(String serverAddress, int serverPort, String username) {

    // Connection to the server is made and in/out streams are created:
    try {
      socket = new Socket(serverAddress, serverPort);
    } catch (IOException e) {
      Exceptions.invalidServerAddress(serverAddress,serverPort);
      return;
    }
    try {
      in = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
      out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8));
      out.write(username);
      out.newLine();
      out.flush();

      // Thread to handle incoming data:
      inThread = new InThread(in);
      clientIn = new Thread(inThread);
      clientIn.setName("inThread");
      clientIn.start();


      // Thread to read console Input:
      consoleInput = new ConsoleInput();
      conin = new Thread(consoleInput);
      conin.setName("ConsoleInput");
      conin.start();

      // Ping-Pong Thread:
      connectionToServerMonitor = new ConnectionToServerMonitor();
      pingpong = new Thread(connectionToServerMonitor);
      pingpong.setName("connectionToServerMonitor");
      pingpong.start();

    } catch (IOException e) {
      Exceptions.invalidServerAddress(serverAddress,serverPort);
      disconnect();
    }

  }

  /**
   * disconnect() makes sure that everything is closed when the client disconnects from the server.
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
      System.out.println("done");
    }

    Platform.exit();
    System.exit(0);
  }

  /**
   * @return the output stream
   */
  public static BufferedWriter getOut() {
    return out;
  }

}

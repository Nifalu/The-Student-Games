package Client;


import Game.Game;

import java.io.*;
import java.net.*;


/**
 * This is the GameClient. Right now it only reads the keyboard input from the console and sends it to the Server.
 */
public class GameClient extends Thread{

  String serverAddress;
  int serverPort;

  private InetAddress ipAddress;
  private DatagramSocket socket;
  private Game game;


  public GameClient(String serverAddress, int serverPort) {
    this.serverAddress = serverAddress;
    this.serverPort = serverPort;
  }

  public GameClient(Game game, String ipAddress) {
    this.game = game;
    try {
      this.socket = new DatagramSocket();
      this.ipAddress = InetAddress.getByName(ipAddress);
    } catch (SocketException e) {
      e.printStackTrace();
    } catch (UnknownHostException e) {
      e.printStackTrace();
    }
  }

  public void startClient() {
    try {
      // Connection to the server is made and in/out streams are created:
      Socket socket = new Socket(serverAddress, serverPort);
      DataInputStream in = new DataInputStream(socket.getInputStream());
      DataOutputStream out = new DataOutputStream(socket.getOutputStream());

      // Thread to handle incoming data is started:
      InThread th = new InThread(in, out);
      Thread clientIn = new Thread(th);
      clientIn.start();

      // InputStream to read user-input is created:
      BufferedReader consoleIn = new BufferedReader(new InputStreamReader(System.in));
      String line;

      do {
        // Reading User-Input from Consol
        line = consoleIn.readLine();

        // Sending Client Input to Server
        out.writeUTF(line);

        // Quit
      } while (!line.equalsIgnoreCase("QUIT"));

      disconnect(socket, in, out);

    } catch (IOException e) {
      e.printStackTrace();
    }
  }


  public void disconnect(Socket socket, DataInputStream in, DataOutputStream out) {
    System.out.println("terminating...");
    try {
      Thread.sleep(1000);
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

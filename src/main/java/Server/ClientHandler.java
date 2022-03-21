package Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.*;
import java.rmi.server.UID;
import java.util.ArrayList;


/**
 * Each Client is connected to a separate thread of this class.
 * The ClientHandler receives input from the Client, processes it and sends an answer back.
 * To be able to process the input correctly, the ClientHandler knows the game and the user he's connected to.
 */
public class ClientHandler implements Runnable {


  Game.Game game; // ClientHandler gets Access to the Game
  Socket socket; // ClientHandler is connected with the Client
  Game.User user; // ClientHandler knows which User the Client belongs to
  DataInputStream in; // Receive data from client
  DataOutputStream out; // Send data to client

  private DatagramSocket dataSocket;
  ServerSocket serverSocket;
  private Thread run, receive, send, tick;
  private boolean running = false;



  public ClientHandler(Socket socket, Game.Game game) throws IOException {
    this.socket = socket;
    this.game = game;
    try {
      this.in = new DataInputStream(socket.getInputStream());
      this.out = new DataOutputStream(socket.getOutputStream());
      this.dataSocket = new DatagramSocket();
      run = new Thread(this, "Server");
      run.start();
    } catch (IOException e) {
      disconnectClient(socket, in, out);
    }
    //broadcastMessage("SERVER: " + user.getUsername() + " has entered the arena."); // msg for clients that are already online
  }

  @Override
  public void run() {

    // Identifies the new Client
    user = game.connect(socket.getInetAddress(), this, socket.getInetAddress().getHostName(), socket.getPort());
    welcomeUser();
    running = true;
    ping();
    tick();
    receive();

    /* changed with read()

    try {
      String s; // message sent by client
      String[] input;

      // do chont vlt pinpong here??
      while (!(s = in.readUTF()).equals("QUIT")) {

        // Reads the incoming String and splits it into two parts
        // ( "instruction" and "value" ) which are saved in an array.
        input = s.split("-", 2); // Splits the String (limit - 1) times at the first "-"

        // Some possible Instructions. --> should be sent to a separate protocol class for more clarity.
        if (input[0].equals("CHANGENAME")) { // (use: CHANGENAME-YourNewName )
          System.out.println(user.getUsername() + "is now called: " + input[1]);
          user.setUsername(input[1]);
          out.writeUTF("Your new nickname is: " + user.getUsername());

          //} else if (input[0].equals("PING")) { // (use: PING )
          //  System.out.println(user.getUsername() + ": " + input[0]);
          //  out.writeUTF("PONG");

        } else {
          // message gets sent to all clients
          // needs to be replaced with method which doesn't send a message to the author for chatting
          broadcastMessage(s);
        }

      }
      out.writeUTF("QUIT");
      disconnectClient(socket, in, out);

    } catch (IOException e) {
      e.printStackTrace();
    }
     */
  }
  public void tick() {
    tick = new Thread("Tick") {
      public void run() {
        while (running) {

        }
      }
    };
    tick.start();
  }

  private void ping() {
    while (true) {

    }
  }

  private void receive() {
    receive = new Thread("Receive") {
      public void run() {
        while (running) {
          //System.out.println(clients.size());
          byte[] data = new byte[1024];
          DatagramPacket packet = new DatagramPacket(data, data.length);
          try {
            dataSocket.receive(packet);
          } catch (IOException e) {
            e.printStackTrace();
          }
          read(packet);
        }
      }
    };
    receive.start();
  }

  private void send(final byte[] data, final InetAddress address, final int port) {
    send = new Thread("Send") {
      public void run() {
        DatagramPacket packet = new DatagramPacket(data, data.length, address, port);
        try {
          dataSocket.send(packet);
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    };
    send.start();
  }

  private void send(String message, InetAddress address, int port) {
    String msg = "/b/" + message + "/e/";
    send(msg.getBytes(), address, port);
  }

  private void read(DatagramPacket packet) {
    String message = new String(packet.getData());
    String[] input;

    // Reads the incoming String and splits it into two parts
    // ( "instruction" and "value" ) which are saved in an array.
    input = message.split("-", 2); // Splits the String (limit - 1) times at the first "-"
    try {
      if (input[0].equals("PING")) {
        System.out.println(user.getUsername() + ": " + input[0]);
        send("PONG", dataSocket.getInetAddress(), dataSocket.getPort());
      } else if (input[0].equals("CHANGENAME")) { // (use: CHANGENAME-YourNewName )
        System.out.println(user.getUsername() + "is now called: " + input[1]);
        user.setUsername(input[1]);
        send("Your new nickname is: " + user.getUsername(), dataSocket.getInetAddress(), dataSocket.getPort());
      } else if (input[0].equals("QUIT")) {
        System.out.println("Got disconnect request: " + message.substring(3, message.length()));
        disconnectClient(socket, in, out);
      } else {
        broadcastMessage(message);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }


  private void welcomeUser() {
    try {
      if (user.isFirstTime()) {
        System.out.println(user.getUsername() + " from district " + user.getDistrict() + " has connected");
        out.writeUTF("Your name was drawn at the reaping. Welcome to the Student Games, " + user.getUsername() + " from district " + user.getDistrict() + "!");
      } else {
        System.out.println(user.getUsername() + " from district " + user.getDistrict() + " has reconnected");
        out.writeUTF("Welcome back " + user.getUsername() + " from district " + user.getDistrict() + "!");
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }


  /**
   * Disconnects this Client from the Server, sets the status of the user to "disconnected".
   * Closes the socket and all streams.
   *
   * @param socket Socket
   * @param in     DataInputStream
   * @param out    DataOutputStream
   */
  public void disconnectClient(Socket socket, DataInputStream in, DataOutputStream out) {
    System.out.println(user.getUsername() + " from district " + user.getDistrict() + " has left");
    user.setIsConnected(false);
    game.getActiveClientList().remove(this);
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

  /**
   * method sends a message to all clients
   */

  public void broadcastMessage(String msg) throws IOException {
    // goes through all clients
      ArrayList<ClientHandler> activeClientList = game.getActiveClientList();
      for(ClientHandler clientHandler : activeClientList){
          clientHandler.out.writeUTF(msg);
      }
  }

  /**
   * broadcast method for chat
   * method sends a message to all clients, but not the one who sent it
   */
  public void broadcastChatMessage(String msg) throws IOException {
      // TO DO
  }

}
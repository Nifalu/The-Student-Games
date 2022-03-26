package Server;

import java.io.*;
import java.net.Socket;
import java.net.InetAddress;
import java.net.UnknownHostException;


/**
 * Each Client is connected to a separate thread of this class.
 * The ClientHandler receives input from the Client, processes it and sends an answer back.
 * To be able to process the input correctly, the ClientHandler knows the game and the user he's connected to.
 */
public class ClientHandler implements Runnable {
  
  Game.Game game; // ClientHandler gets Access to the Game
  Socket socket; // ClientHandler is connected with the Client
  public Game.User user; // ClientHandler knows which User the Client belongs to
  InputStream in; // Receive data from client
  OutputStream out; // Send data to client


  public ClientHandler(Socket socket, Game.Game game) throws IOException {
    this.socket = socket;
    this.game = game;
    try {
      this.in = socket.getInputStream();
      this.out = socket.getOutputStream();
    } catch (IOException e) {
      disconnectClient(socket, in, out);
    }
  }


  @Override
  public void run() {

    // Identifies the new Client
    try {
      Name.askUsername(game, this);
      /* ----------- receive incoming msg and check for connection loss -----------------------------

      A StringBuilder (sb) appends every received byte (c) to a String until a break-character is found.
      Then the break-character is removed and the String is given to the Server Protocol.
      The result (return) of the Server Protocol is then sent back to the Client.
      After that the String is cleared, the counter is set to 0 and the StringBuilder starts a new String.

      If String equals "QUIT", the ServerProtocol is given the message but nothing is sent back.
      The loop breaks and the Client is disconnected
      */

      int c;
      int i = 0;
      StringBuilder sb = new StringBuilder();

      while ((c = in.read()) != -1) {
        sb.append((char) c);
        String s = sb.toString();
        s = s.replace("ä","ae");
        s = s.replace("ö","oe");
        s = s.replace("ü","ue");
        s = s.replace("é","e");
        s = s.replace("è","e");
        s = s.replace("à","a");
        if (s.charAt(i) == ';') {
          sb.delete(i, i + 1);
          if (s.equalsIgnoreCase("QUIT")) {
            ServerProtocol.get(game, user, s);
            break;
          }
          send(ServerProtocol.get(game, user, s));
          sb.delete(0, i + 1);
          i = 0;
        } else {
          i++;
        }
        Thread.sleep(1); // for more efficiency
      }

      disconnectClient(socket, in, out);

    } catch (IOException | InterruptedException e) {
      e.printStackTrace();
    }
  }

  /**
   * "Send a String to the Client."
   * Use this instead of "out.write" to avoid errors.
   * It converts the given String to bytes before sending.
   */
  public void send(String msg) {
    try {
      out.write((msg + ';').getBytes());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  
  /**
   * "Receive a String from the Client"
   * A StringBuilder appends every incoming byte to a String until a certain break-character is found.
   * Then removes the break-character and returns the String.
   */
  public String receive() {
    int c;
    int i = 0;
    StringBuilder sb = new StringBuilder();

    try {
      while (true) {
        Thread.sleep(1); // for more efficiency
        c = in.read();
        sb.append((char) c);

        if (sb.toString().charAt(i) == ';') {
          sb.delete(i, i + 1);
          break;

        } else {
          i++;
        }
      }
    } catch (IOException | InterruptedException e) {
      e.printStackTrace();
    }
    return sb.toString();
  }


  /**
   * "Does everything that needs to be done when a Client disconnects."
   * Closes the In/Out-Streams, the socket and removes the Client from the ActiveClientList.
   */
  public void disconnectClient(Socket socket, InputStream in, OutputStream out) {
    System.out.println(user.getUsername() + " from district " + user.getDistrict() + " has left");
    game.getActiveClientList().remove(this);
    game.getUserlist().remove(user.getId(),user);
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

  public void welcomeUser() {
    System.out.println(user.getUsername() + " from district " + user.getDistrict() + " has connected");
    send("Your name was drawn at the reaping. Welcome to the Student Games, " + user.getUsername() + " from district " + user.getDistrict() + "!");
  }

}

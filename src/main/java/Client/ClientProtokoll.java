package Client;

import java.io.IOException;


/**
 * Anything that needs to go to the Server is first send through the CLient-Protocol.
 * It processes the message and formats it to be readable by the Server.
 */
public class ClientProtokoll {

  GameClient gameClient;

  public ClientProtokoll(GameClient gameClient) {
    this.gameClient = gameClient;
  }

  /**
   * Messages to be sent to the Server
   * @param msg String to be sent
   */
  public void sendToServer(String msg) {

    String[] input;
    input = msg.split(" ", 2);


      switch (input[0].toLowerCase()) {

        case "/quit":
          send("QUIT");
          gameClient.disconnect();
          break;

        case "/chat":
          send("CHAT-" + input[1]);
          break;

        case "/nick":
          send("CHANGENAME-" + input[1]);
          break;

        case "/whisper":
          String[] splitUserAndMsg = input[1].split(" ");
          send("WHISPER-" + splitUserAndMsg[0] + "-" + splitUserAndMsg[1]);
          break;



        default:
          send(msg); // Echo
          break;
      }
    }


  public void receive(String msg){

    // Right now incoming msg are just printed to console:
    String[] input;
    input = msg.split("-", 2);

    switch(input[0]) {

      case "PING":
        send("PONG-" + input[1]);
        break;

      case "PONG":
        gameClient.connectionToServerMonitor.setLastReceivedPong(Long.parseLong(input[1]));
        break;

      default:
        System.out.println(msg);
        break;
    }
  }


  public void send(String msg) {
    try {
      gameClient.out.write(msg);
      gameClient.out.newLine();
      gameClient.out.flush();
    } catch (IOException e) {
      System.out.println("cannot reach Server");
    }
  }
}



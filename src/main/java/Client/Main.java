package Client;


/**
 * This Class contains the main Method to start the GameClient.
 * It only exists to easily find the main method.
 */
public class Main {

  /**
   * Connect to the Game
   *
   * @param args Server Address, Port Number
   */
  public static void main(String[] args) {

    GameClient client = new GameClient(args[0], Integer.parseInt(args[1]));
    client.startClient();
  }
}

package Client;


/**
 * This Class contains the main Method to start the GameClient.
 * It only exists to easily find the main method.
 */
public class Main {

  /**
   * Connect to the Game
   */
  public static void start(String serverAddress, String port) {

    GameClient client = new GameClient(serverAddress, Integer.parseInt(port));
  }
}

package Server;


/**
 * Class with a main() Method to start the Server.
 * This is only to easily find the main method.
 */
public class Main {


  /**
   * Starts the Server.
   */
  public static void start(String s) {
    GameServer justAGame = new GameServer(Integer.parseInt(s)); // specific Port Number
    Thread gameServerThread = new Thread(justAGame);
    gameServerThread.start();
  }

  public static void start() {
    GameServer justAGame = new GameServer(); // random Port Number
    Thread gameServerThread = new Thread(justAGame);
    gameServerThread.start();
  }
}

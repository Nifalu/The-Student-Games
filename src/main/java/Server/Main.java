package Server;


import java.util.Random;

/**
 * Class with a main() Method to start the Server.
 */
public class Main {


  /**
   * Starts the Server.
   * Port Number is optional
   *
   * @param args Port (optional)
   */
  public static void main(String[] args) {

    if (args.length != 0) {
      GameServer justAGame = new GameServer(Integer.parseInt(args[0])); // specific Port Number
      Thread gameServerThread = new Thread(justAGame);
      gameServerThread.start();
    } else {
      GameServer justAGame = new GameServer(); // random Port Number
      Thread gameServerThread = new Thread(justAGame);
      gameServerThread.start();
    }


  }
}

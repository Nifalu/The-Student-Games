package Client;


/**
 * This Class contains the main Method to start the ClientManager.
 * It only exists to easily find the main method.
 */
public class Main {

  /**
   * Connect to the Server
   * @param port int
   * @param serverAddress String
   */
  public static void start(String serverAddress, String port) {

    ClientManager client = new ClientManager(serverAddress, Integer.parseInt(port));
  }
}

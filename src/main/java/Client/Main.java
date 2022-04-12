package Client;


import gui.Launcher;

/**
 * This Class contains the main Method to start the ClientManager.
 * It only exists to easily find the main method.
 */
public class Main {

  /**
   * Connect to the Server
   */
  public static void start(String serverAddress, String port) {

    ClientManager.connect(serverAddress, Integer.parseInt(port));
    Launcher.main(new String[0]);

  }
}

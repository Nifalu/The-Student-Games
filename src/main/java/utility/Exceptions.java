package utility;

import java.io.IOException;

/**
 * Contains custom exception messages
 */
public class Exceptions {

  /**
   * When the given server address is not valid
   * @param address serveraddress
   * @param port port
   */
  public static void invalidServerAddress(String address, int port) {
    System.out.println("-----------------------------");
    System.out.println("Could not connect to :" + address + ":" + port);
    System.out.println("Check spelling and Network settings");
    System.out.println("-----------------------------");

  }

  /**
   * When the programm was started incorrectly
   */
  public static void StarterArgumentError() {
    System.out.println("-------------------------------");
    System.out.println("Wrong arguments! ");
    System.out.println("start the client with: ... client <hostadress>:<port> [<username>] ");
    System.out.println("start the server with: ... server <port> ");
    System.out.println("-------------------------------");
  }

  /**
   * When given the port number is not allowed
   * @param port int port
   */
  public static void StarterIllegalPortNumber(int port) {
    System.out.println("-------------------------------");
    System.out.println("Illegal port number! ");
    System.out.println(port + " is not allowed as port. Choose one between 2000 - 65535 ");
    System.out.println("-------------------------------");
  }

  /**
   * When the given port number is not a number.
   * @param e NumberFormatException
   * @param port int port
   */
  public static void StarterCannotResolvePortNumber(NumberFormatException e, String port) {
    System.out.println("-------------------------------");
    System.out.println("Cannot resolve port number! ");
    System.out.println(port + " is not allowed as port. Choose an integer between 2000 - 65535 ");
    System.out.println("-------------------------------");
    // e.printStackTrace();
  }


  /**
   * When there was an error starting the Server
   * @param e IOException
   * @param port int port
   */
  public static void failedToCreateServer(IOException e, int port) {
    System.out.println("-------------------------------");
    System.out.println("Failed to create Server ");
    System.out.println("Your port might be taken (Port: " + port + " ) . Also check your Network connection and settings");
    System.out.println("-------------------------------");
  }

  /**
   * When there was an error connecting to the Server
   */
  public static void failedToConnectClientHandler() {
    System.out.println("-------------------------------");
    System.out.println("An Error occured when trying to connect to a new Client");
    System.out.println("-------------------------------");
  }
}

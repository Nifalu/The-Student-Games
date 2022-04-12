package utility;

import java.io.IOException;

public class Exceptions {

  public static void invalidServerAddress(String address, int port) {
    System.out.println("-----------------------------");
    System.out.println("Could not connect to :" + address + ":" + port);
    System.out.println("Check spelling and Network settings");
    System.out.println("-----------------------------");

  }

  public static void StarterArgumentError() {
    System.out.println("-------------------------------");
    System.out.println("Wrong arguments! ");
    System.out.println("start the client with: ... client <hostadress>:<port> [<username>] ");
    System.out.println("start the server with: ... server <port> ");
    System.out.println("-------------------------------");
  }

  public static void StarterIllegalPortNumber(int port) {
    System.out.println("-------------------------------");
    System.out.println("Illegal port number! ");
    System.out.println(port + " is not allowed as port. Choose one between 2000 - 65535 ");
    System.out.println("-------------------------------");
  }

  public static void StarterCannotResolvePortNumber(NumberFormatException e, String port) {
    System.out.println("-------------------------------");
    System.out.println("Cannot resolve port number! ");
    System.out.println(port + " is not allowed as port. Choose an integer between 2000 - 65535 ");
    System.out.println("-------------------------------");
    // e.printStackTrace();
  }


  public static void failedToCreateServer(IOException e, int port) {
    System.out.println("-------------------------------");
    System.out.println("Failed to create Server ");
    System.out.println("Your port might be taken (Port: " + port + " ) . Also check your Network connection and settings");
    System.out.println("-------------------------------");
    // e.printStackTrace();
  }
}

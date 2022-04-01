package Starter;

import ch.unibas.dmi.dbis.cs108.example.gui.javafx.GUI;

public class Starter {


  public static void main(String[] args) {
    if (args.length == 0) {
      System.out.println("not enough arguments");
    } else if (args[0].equals("Server")) {
      if (args.length == 2) {
        Server.Main.start(args[1]);
      } else {
        Server.Main.start();
      }
    } else if (args[0].equals("Client")) {
      String[] address = args[1].split(":", 2);
      if (address.length == 2) {
        Client.Main.start(address[0], address[1]);
        Server.GUI.Main.main(args);
      } else {
        System.out.println("not enough arguments");
      }
    }
  }
}

package Starter;

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
      Client.Main.start(args[1], args[2]);
    }
  }


}

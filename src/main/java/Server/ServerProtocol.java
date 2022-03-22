package Server;

public class ServerProtocol {

  /**
   * Takes a user as identifier and a String[] as instruction.
   * Then processes the instruction and its value and returns
   * an answer as String to send back to the User/Client.
   * @param game game
   * @param user user
   * @param s string
   * @return string
   */
  public static String get(Game.Game game, Game.User user, String s) {

    // How to:
    // case "Befehl":
    // process the command
    // return a command (String) to answer the client

    // input[0] = command, input[1] = msg
    String[] input;
    input = s.split("-", 2); // Splits the String (limit - 1) times at the first "-"

    switch(input[0]) {

      case "PING":
        return "PONG";

      case "CHAT":
        game.broadcastMessage(user,user.getUsername() + ": " + input[1]);
        return user.getUsername() + ": " + input[1];


      case "CHANGENAME":

        // was soll passieren wenn jemand seinen Namen aendern moechte?

        return "new name: " + user.getUsername();
    }

    // Was soll passieren wenn der Befehl nicht bekannt ist? (aktuell Echo)
    return s;
  }

}

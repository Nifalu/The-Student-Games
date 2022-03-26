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
  public static String get(Game game, User user, String s) {

    // How to:
    // case "Befehl":
    // process the command
    // return a command (String) to answer the client (-1 to not send anything back)

    // input[0] = command, input[1] = msg
    String[] input;
    input = s.split("-", 2); // Splits the String (limit - 1) times at the first "-"

    switch(input[0]) {

      case "QUIT":
        user.getClienthandler().disconnectClient(); // disconnects Client
        user.getClienthandler().requestStop(); // stops the thread
        //maybe broadcast to everyone that user X has quit?
        return "-1";

      case "PING":
        return "PONG";

      case "CHAT":
        game.broadcastMessage(user,user.getUsername() + ": " + input[1]);
        return user.getUsername() + ": " + input[1];


      case "WHISPER":
        String[] splitUserAndMsg = input[1].split(":", 2);
        game.whisper(splitUserAndMsg[0], user.getUsername(), splitUserAndMsg[1]);
        return user.getUsername() + " to " + splitUserAndMsg[0] + ": " +  splitUserAndMsg[1];


      case "CHANGENAME":

        // was soll passieren wenn jemand seinen Namen aendern moechte?
        if (Name.nameAlreadyExists(game, input[1])) {
          return "SORRY! This tribute already exists. Please try another name.";
        } else {
          user.setUsername(input[1]);
          return "SUCCESS! You're now called : " + user.getUsername();
        }
    }

    // Was soll passieren wenn der Befehl nicht bekannt ist? (aktuell Echo)
    return s;
  }

}

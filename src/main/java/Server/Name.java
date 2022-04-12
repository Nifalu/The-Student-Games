package Server;

import utility.IO.CommandsToClient;
import utility.IO.SendToClient;
import utility.IO.ReceiveFromProtocol;

/**
 * This class handles all methods related to setting and changing usernames as well as checking
 * if a certain name is available and if not propose an alternative.
 */
public class Name {
  private final ClientHandler clientHandler;
  private final SendToClient sendToClient = new SendToClient();
  protected final ReceiveFromProtocol receiveFromClient = new ReceiveFromProtocol();

  /**
   * This method first calls the proposeUsernameBasedOnSystemName Method and asks the Client if he wants the name
   * proposed to be his username. If the user doesn't agree, he is then asked to type in his own username. A new user is created
   * and connected to the serverManager. If the username already exists in the serverManager he will get a new username assigned.
   * @param clientHandler ClientHandler
   **/
  public Name(ClientHandler clientHandler) {
    this.clientHandler = clientHandler;
  }


  /**
   * Asks the Client if he's happy with the name he got or if he wants to change it.
   * Then waits for an answer yes or no. If no it asks for a new name, then checks if that name is available and
   * if not proposes a new one.
   */
  public void askUsername() {
    sendToClient.send(clientHandler, CommandsToClient.PRINT, "Hey there, would you like to be named " + clientHandler.user.getUsername() + "?");
    String answer = receiveFromClient.receive();
    if (!answer.equalsIgnoreCase("YES")) { // if they are not happy with the proposed name
      sendToClient.send(clientHandler, CommandsToClient.PRINT, ("Please enter your desired name below."));
      String desiredName = receiveFromClient.receive();
      if (!desiredName.equals(clientHandler.user.getUsername())) {
        changeNameTo("", desiredName);
      }
    }
    welcomeUser();
  }

  /**
   * Changes the Username to the preferred Name if available. If not it suggests the User a new Name.
   * There cannot be two Players with the same Name (case independent).
   *
   * @param currentName   String
   * @param preferredName String
   */
  public void changeNameTo(String currentName, String preferredName) {
    if (currentName.equals(preferredName)) {
      sendToClient.send(clientHandler, CommandsToClient.PRINT, ("This is already your name"));
    } else if (nameAlreadyExists(preferredName)) { // Wenn preferredName bereits exisitert:
      String newName;
      newName = proposeUsernameIfTaken(preferredName);
      sendToClient.send(clientHandler, CommandsToClient.PRINT, ("Sorry! This tribute already exists. Try this one: " + newName));
    } else { // wenn preferredName frei ist:
      sendToClient.serverBroadcast(CommandsToClient.PRINT, (clientHandler.user.getUsername() + " is now called: " + preferredName));
      clientHandler.user.setUsername(preferredName);
    }
  }

  /**
   * In case a username is already taken this method proposes a new username.
   * @param preferredName String of the preferred Name
   * @return String proposedName
   */
  public String proposeUsernameIfTaken(String preferredName) {
    int i = 1;
    String updatedName = preferredName;
    while (nameAlreadyExists(updatedName)) {
      updatedName = preferredName + i;
      i++;
    }
    return updatedName;
  }

  /**
   * checks if the username already exists and returns true or false.
   */
  private boolean nameAlreadyExists(String desiredName) {
    String tmp_name;
    int length = ServerManager.getUserlist().size();
    for (int i = 0; i < length; i++) {
      tmp_name = ServerManager.getUserlist().get(i).getUsername();
      if (tmp_name.equalsIgnoreCase(desiredName) && !tmp_name.equals(clientHandler.user.getUsername())) {
        return true;
      }
    }
    return false;
  }

  /**
   * Sends welcome Message in the Chat
   */
  private void welcomeUser() {
    // Serverside
    System.out.println(clientHandler.user.getUsername() + " from district " + clientHandler.user.getDistrict() + " has connected");

    // Clientside
    sendToClient.send(clientHandler, CommandsToClient.PRINT, "Your name was drawn at the reaping.");
    sendToClient.serverBroadcast(CommandsToClient.PRINT, ("Welcome to the Student Games, " + clientHandler.user.getUsername() + " from district " + clientHandler.user.getDistrict() + "!"));
  }

}

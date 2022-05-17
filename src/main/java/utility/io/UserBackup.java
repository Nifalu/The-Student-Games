package utility.io;

import gameLogic.GameList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import server.ServerManager;
import server.User;

import java.io.*;
import java.util.HashMap;

/**
 * this class is used to create and manage user backups
 */
public class UserBackup {

  /**
   * creates the logger for this class
   */
  static Logger logger = LogManager.getLogger(UserBackup.class);

  /**
   * saves the users in a .txt file
   */
  public static void saveUsers() {
    try {
      BufferedWriter bw = new BufferedWriter(new FileWriter("gamefiles/utility/users.txt", false));
      bw.write(getAllUsers());
      bw.flush();
      bw.close();
    } catch (IOException e) {
      logger.warn("Users were not saved!");
    }
  }

  /**
   * loads the users from a .txt file
   */
  public static void loadUsers() {
    try {
      BufferedReader br = new BufferedReader(new FileReader("gamefiles/utility/users.txt"));
      String line = br.readLine();
      if (line == null) {
        return;
      }
      String[] logins = line.split(",");
      String[] split;
      User user;
      for (String s : logins) {
        split = s.split("!");
        if (split.length == 2) {
          user = new User(null, split[0], split[1], false);
          ServerManager.getUserlist().put(ServerManager.getUserlist().size(), user);
          user.setLobby(GameList.getLobbyList().get(0));
        }
      }
    } catch (
        IOException e) {
      logger.warn("Users could not be loaded");
    }

  }

  /**
   * returns all users currently stored in the userlist as a single string.
   * @return String
   */
  private static String getAllUsers() {
    StringBuilder sb = new StringBuilder();

    for (HashMap.Entry<Integer, User> entry : ServerManager.getUserlist().entrySet()) {
      sb.append(entry.getValue().getUsername());
      sb.append("!");
      sb.append(entry.getValue().getUuid());
      sb.append(",");
    }

    return sb.toString();
  }

}

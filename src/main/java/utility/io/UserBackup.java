package utility.io;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import server.ServerManager;
import server.User;

import java.io.*;
import java.util.HashMap;

public class UserBackup {

  static Logger logger = LogManager.getLogger(UserBackup.class);

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
        }
      }
    } catch (
        IOException e) {
      logger.warn("Users could not be loaded");
    }

  }

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

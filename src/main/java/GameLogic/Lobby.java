package GameLogic;
import Server.User;

import java.util.HashMap;

public class Lobby {

    String name;
    Server.User user;

    HashMap<Integer, Server.User> usersInLobby = new HashMap<>();

    public Lobby(String name) {
        this.name = name;
    }

    public HashMap<Integer, User> getUsersInLobby() {
        return usersInLobby;
    }

    public String getLobbyName() {
        return name;
    }

    public void addUserToLobby(Server.User user) {
        int size = usersInLobby.size();
        usersInLobby.put(size, user);
    }
}

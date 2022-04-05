package GameLogic;

import Server.User;

import java.util.HashMap;


public class GameList {


    private static final HashMap<Integer, Lobby> lobbyList = new HashMap<>();

    public synchronized static HashMap<Integer, Lobby> getLobbyList() {
        return lobbyList;
    }

    public synchronized static void connectLobby(String name) {
        Lobby lobby = new Lobby(name);
        lobbyList.put(lobbyList.size(), lobby);
    }

    public synchronized static String printLobbyList() {
        String s = "";
        for (int i = 0; i < lobbyList.size(); i++) {
            s = s + "Number of lobby:" + i + lobbyList.get(i).getLobbyName();
        }
        return s;
    }

}

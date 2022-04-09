package GameLogic;

import Server.User;
import utility.IO.CommandsToClient;

import java.util.HashMap;

/**
 * GameList class the contains all the list which are relevant in the game.
 */
public class GameList {

    /**
     * HashMap with all the Lobbies, can be accessed via the lobby number.
     */
    private static final HashMap<Integer, Lobby> lobbyList = new HashMap<>();

    public synchronized static HashMap<Integer, Lobby> getLobbyList() {
        return lobbyList;
    }

    /**
     *
     * @return return a String of all the open lobbies
     */
    public synchronized static String printLobbies(HashMap<Integer, Lobby> lobbyList) {
        String s = "";
        for (int i = 0; i < lobbyList.size(); i++) {
            s = s + " " + lobbyList.get(i).getLobbyName();
        }
        return s;
    }

    /**
     *
     * @return all the lobbies with the status open, which is saved in the lobby object as an int.
     * The status open is saved as 1.
     */
    public synchronized static HashMap<Integer, Lobby> getOpenLobbies() {
        HashMap<Integer, Lobby> openLobbies = new HashMap<>();
        int activeLobbyCounter = 0;
        for (int i = 0; i < lobbyList.size(); i++) {
            if (lobbyList.get(i).getLobbyStatus() == 1) {
                openLobbies.put(activeLobbyCounter, lobbyList.get(i));
                activeLobbyCounter++;
            }
        }
        return openLobbies;
    }

    /**
     *
     * @return all the lobbies with the status finished, which is saved in the lobby object as an int.
     * The status finished is saved as -1.
     */
    public synchronized static HashMap<Integer, Lobby> getInFinishedLobbies() {
        HashMap<Integer, Lobby> finishedLobbies = new HashMap<>();
        int InActiveLobbyCounter = 0;
        for (int i = 0; i < lobbyList.size(); i++) {
            if (lobbyList.get(i).getLobbyStatus() == -1) {
                finishedLobbies.put(InActiveLobbyCounter, lobbyList.get(i));
                InActiveLobbyCounter++;
            }
        }
        return finishedLobbies;
    }

    /**
     *
     * @return all the lobbies with the status on going, which is saved in the lobby object as an int.
     * The status on going is saved as 0.
     */
    public synchronized static HashMap<Integer, Lobby> getOnGoingLobbies() {
        HashMap <Integer, Lobby> OnGoingLobbies = new HashMap<>();
        int OnGoingLobbyCounter = 0;
        for (int i = 0; i < lobbyList.size(); i++) {
            if (lobbyList.get(i).getLobbyStatus() == 0) {
                OnGoingLobbies.put(OnGoingLobbyCounter, lobbyList.get(i));
                OnGoingLobbyCounter++;
            }
        }
        return OnGoingLobbies;
    }

}

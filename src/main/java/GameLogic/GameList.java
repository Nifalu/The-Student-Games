package GameLogic;

import Server.ClientHandler;
import Server.ServerManager;
import Server.User;

import java.util.ArrayList;
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

    //muss im Lobby Objekt sein
    // wird gelöscht
    private static final HashMap<Integer, User> usersInLobby = new HashMap<>();
    public synchronized static HashMap<Integer, User> getUsersInLobby() { return usersInLobby; }

    // es gibt keine UserInLobby HashMap mehr, weil jedes User Objekt eine Lobby Variable hat mit zugehöriger
    // LobbyNumber
    //public synchronized static HashMap<Server.User, Integer> getUserInLobby() { return userInLobby;}
    // ist nachher eine Methode
    //private static final HashMap<Server.User, Integer> userInLobby = new HashMap<>();


    //macht keinen Sinn aber jetzt sind alle Lists in der GameList Klasse
    public synchronized static ArrayList<ClientHandler> getActiveClientList() {
        return ServerManager.getActiveClientList();
    }

    public synchronized static HashMap<Integer, User> getUserlist() {
        return ServerManager.getUserlist();
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


    public synchronized static HashMap<Integer, User> getUsersInLobby(Lobby lobby) {
        int size = ServerManager.getUserlist().size();
        int counter = 0;
        HashMap<Integer, User> usersInLobby = new HashMap<>();
        for (int i = 0; i < size; i++) {
            User user = ServerManager.getUserlist().get(i);
            if (user.getLobby().equals(lobby)) {
                usersInLobby.put(counter, user);
            }
        }
        return usersInLobby;
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
    public synchronized static HashMap<Integer, Lobby> getFinishedLobbies() {
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
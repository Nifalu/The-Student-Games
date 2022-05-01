package gameLogic;

import server.ClientHandler;
import server.ServerManager;
import server.User;

import java.util.HashMap;
import java.util.ArrayList;

/**
 * GameList class the contains all the list which are relevant in the game.
 */
public class GameList {

    /**
     * HashMap with all the Lobbies, can be accessed via the lobby number.
     */
    private static final HashMap<Integer, Lobby> lobbyList = new HashMap<>();

    /**
     * returns the lobbylist
     * @return LobbyList
     */
    public synchronized static HashMap<Integer, Lobby> getLobbyList() {
        return lobbyList;
    }

    /**
     * returns the userlist
     * @return userlist
     */
    public synchronized static HashMap<Integer, User> getUserlist() {
        return ServerManager.getUserlist();
    }

    /**
     * returns a String of all users
     * @return String
     */
    public synchronized static String printUserList() {
        String s = "";
        for (int i = 0; i < getUserlist().size(); i++) {
            s = s + " " + getUserlist().get(i).getUsername();
        }
        return s;
    }

    /**
     * prints out users in lobby
     * @param lobby Lobby
     * @return String
     */
    public synchronized static String printUserInLobby (Lobby lobby) {
        ArrayList<ClientHandler> userList = ServerManager.getActiveClientList();
        String s = "";
        for (int i = 0; i < userList.size(); i++) {
            if (userList.get(i).user.getLobby().equals(lobby)) {
                s = s + "      -" + userList.get(i).user.getUsername() + "%";
            }
        }
        return s;
    }

    /**
     * prints out all users in a lobby
     * @return String
     */
    public synchronized static String printLoungingList() {
        String print = "";
        for (int i = 0; i < getLobbyList().size(); i++) {
            print = print + getLobbyList().get(i).getLobbyName() + "%" + printUserInLobby(getLobbyList().get(i));
        }
        return print;
    }

    /**
     * prints out all open lobbies
     * @param lobbyList Hashmap with all lobbies
     * @return a String of all open lobbies
     */
    public synchronized static String printLobbies(HashMap<Integer, Lobby> lobbyList) {
        String s = "";
        int counter = 0;
        for (int i = 0; i < lobbyList.size(); i++) {
            int lobbyStatus = lobbyList.get(i).getLobbyStatus();
            String lobbyStatusString = "";
            switch (lobbyStatus) {
                case 1:
                    lobbyStatusString  = "open";
                    break;
                case 0:
                    lobbyStatusString = "on going";
                    break;
                case -1:
                    lobbyStatusString = "finished";
                    break;
                case 69:
                    lobbyStatusString = "standard Lobby";
                    break;
            }
            if (lobbyStatusString.equals("open")) {
                s = s + counter + ". " + lobbyList.get(i).getLobbyName() + " " + "[" + lobbyStatusString + "]  " + "§";
                counter++;
            } else {
                s = s + lobbyList.get(i).getLobbyName() + " " + "[" + lobbyStatusString + "]  " + "§";
            }
        }
        return s;
    }


    /*public synchronized static HashMap<Integer, User> getUsersInLobby(Lobby lobby) {
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
    }*/

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
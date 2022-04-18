package gameLogic;

import server.ServerManager;
import server.User;

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
    //Lobby lobby = new Lobby("standard");


    //macht keinen Sinn aber jetzt sind alle Lists in der GameList Klasse
    //public synchronized static ArrayList<ClientHandler> getActiveClientList() {
    //    return ServerManager.getActiveClientList();
    //}

    public synchronized static HashMap<Integer, User> getUserlist() {
        return ServerManager.getUserlist();
    }

    /**
     *
     * @return return a String of all open lobbies
     */
    public synchronized static String printUserList() {
        String s = "";
        for (int i = 0; i < getUserlist().size(); i++) {
            s = s + " " + getUserlist().get(i).getUsername();
        }
        return s;
    }

    public synchronized static String printUserInLobby (Lobby lobby) {
        String usersInLobbyString = "";
        int counter = 0;
        HashMap<Integer, User> usersInLobby = new HashMap<>();
        for (int i = 0; i < getUserlist().size(); i++) {
            if (getUserlist().get(i).getLobby().equals(lobby)) {
                usersInLobby.put(counter, getUserlist().get(i));
                counter++;
            }
        }
        for (int i = 0; i < usersInLobby.size(); i++) {
            usersInLobbyString = usersInLobbyString + usersInLobby.get(i).getUsername() + ", ";
        }
        return usersInLobbyString;
    }

    /**
     *
     * @return return a String of all users in the lobby
     */
    public synchronized static String printLoungingList() {
        String print = "";
        for (int i = 0; i < getLobbyList().size(); i++) {
            String s = getLobbyList().get(i).getLobbyName();
            HashMap<Integer, User> list = getLobbyList().get(i).getUsersInLobby();
            for (int j = 0; j < list.size(); j++) {
                s = s + " " + list.get(j).getUsername();
            }
            print = print + " " + s;
        }
        return print;
    }

    /**
     *
     * @param lobbyList Hashmap with all lobbies
     * @return a String of all open lobbies
     */
    public synchronized static String printLobbies(HashMap<Integer, Lobby> lobbyList) {
        String s = "";
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
            s = s + i + ". " + lobbyList.get(i).getLobbyName() + " " + "[" + lobbyStatusString + "]  ";
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
package GameLogic;

import Client.ClientReceive;
import Server.ClientHandler;
import Server.ServerManager;
import Server.ServerReceive;
import utility.IO.CommandsToClient;
import utility.IO.ReceiveFromProtocol;
import utility.IO.SendToClient;
import java.util.Random;

import java.util.HashMap;

/**
 * this class handles everything related to creating andS joining a lobby.
 */
public class CreateLobbyHelper {

    private final SendToClient sendToClient = new SendToClient();
    public final ReceiveFromProtocol receiveFromClient = new ReceiveFromProtocol();
    private final ClientHandler clienthandler;


    Server.User user;

    //add createLobby
    public CreateLobbyHelper(Server.ClientHandler clienthandler) {
        this.clienthandler = clienthandler;
        this.user = clienthandler.user;
    }

    /**
     * This method first prints a list of all the open lobbies, then proceeds to ask if the user wants to create a new lobby.
     * After that the user is asked what lobby they want to join (via the lobby number (begins with zero)).
     *
     * @param clienthandler sends and receives the information through the clienthandler.
     */
    public void askWhatLobbyToJoin(Server.ClientHandler clienthandler) {
        if (GameList.getOpenLobbies().size() == 0) {
            sendToClient.send(clienthandler, CommandsToClient.PRINT, "There are no open Lobbies yet.");
        } else {
            sendToClient.send(clienthandler, CommandsToClient.PRINT, "Hello there, here are the open lobbies:");
            //HashMap<Integer, Lobby> openLobbies = GameList.getOpenLobbies();
            String openLobbyList = GameList.printLobbies(GameList.getOpenLobbies());
            sendToClient.send(clienthandler, CommandsToClient.PRINT, openLobbyList);
        }
        sendToClient.send(clienthandler, CommandsToClient.PRINT, "Would you like to create your own lobby?");
        String answer1 = receiveFromClient.receive();
        if (answer1.equalsIgnoreCase("YES")) {
            sendToClient.send(clienthandler, CommandsToClient.PRINT, "Enter Name of the lobby below:");
            String answer2 = receiveFromClient.receive();
            connectLobby(answer2); //puts Lobby into the lobbyList
        }

        sendToClient.send(clienthandler, CommandsToClient.PRINT, "what's the number of the lobby which you " +
                "would like to choose? ");
        String answer = receiveFromClient.receive();
        if (checkIfLobbyExists(answer)) {
            int number = Integer.parseInt(answer);

            //Lobby lobby = GameList.getOpenLobbies().get(number);


            //System.out.println(number);
            //System.out.println(GameList.getOpenLobbies().get(number).getLobbyName());
            //System.out.println(GameList.printLobbies(GameList.getOpenLobbies()));
            //System.out.println(lobby.getLobbyName() + "nnn"); // all the print outs work

            //user.lobby = lobby; //das geht jetzt
            user.setLobby(GameList.getOpenLobbies().get(number)); // wird momentan null pointer Exception


            //GameList.getOpenLobbies().get(number).addUserToLobby(user);
            //GameList.getUserInLobby().put(user, number);
            sendToClient.send(clienthandler, CommandsToClient.PRINT, "You have been added to lobby "
                    + user.getLobby().getLobbyName() + "."); //wirft eine Exception
        } else {
            sendToClient.send(clienthandler, CommandsToClient.PRINT, "Oooops sorry there this Lobby doesn't exist. " +
                    "Please try again.");
            String answer3 = receiveFromClient.receive();
            //int number2 = Integer.parseInt(answer3);
            if (checkIfLobbyExists(answer3)) {
                int number3 = Integer.parseInt(answer3);
                //Lobby lobby = GameList.getOpenLobbies().get(number3);
                user.setLobby(GameList.getOpenLobbies().get(number3));
                //Lobby lobby1 = GameList.getLobbyList().get(answer3);
                //user.setLobby(lobby1);

                //GameList.getOpenLobbies().get(answer3).addUserToLobby(user);
                //GameList.getUserInLobby().put(user, number2);
                //GameList.getLobbyList().get(number2).addUserToLobby(user);
                sendToClient.send(clienthandler, CommandsToClient.PRINT, "You have been added to lobby "
                        + user.getLobby().getLobbyName() + ".");
            } else {
                Random random = new Random();
                int randomLobby = random.nextInt(GameList.getOpenLobbies().size());
                //Lobby lobby2 = GameList.getLobbyList().get(randomLobby);
                user.setLobby(GameList.getLobbyList().get(randomLobby));
                //GameList.getOpenLobbies().get(randomLobby).addUserToLobby(user);
                //GameList.getUserInLobby().put(user, randomLobby);
                //GameList.getLobbyList().get(randomLobby).addUserToLobby(user);
                sendToClient.send(clienthandler, CommandsToClient.PRINT, " Sorry, this lobby doesn't exist, " +
                        "we have added you randomly to another lobby.");
                sendToClient.send(clienthandler, CommandsToClient.PRINT, " You have been added to lobby "
                        + user.getLobby().getLobbyName() + ".");
            }
        }
        //user.setLobby(GameList.getOpenLobbies().get(0));
        //user.changeLobby(GameList.getOpenLobbies().get(0));
        //askIfReadyToPlay(clienthandler, Integer.parseInt(answer));
        //String s = "";
        /**for (int i = 0; i < ServerManager.getUserlist().size(); i++) {
            String user = ServerManager.getUserlist().get(i).getUsername();
            String lobby = ServerManager.getUserlist().get(i).getLobby().getLobbyName();
            System.out.println(user + lobby);
        }**/

        /**for (int i = 0; i < GameList.getOpenLobbies().size(); i ++) {
            String s = "";
            s = "lobby: " + GameList.getOpenLobbies().get(i).getLobbyName();
            for ( int j = 0; j < GameList.getUsersInLobby(GameList.getOpenLobbies().get(i)).size(); j++) {
                Lobby lobby = GameList.getOpenLobbies().get(i);
                s = s +  GameList.getUsersInLobby(lobby).get(j).getUsername();
            }
            System.out.println(s);
        }**/
    }

    public void readyToPlay(Server.ClientHandler clienthandler) {
        user.setReadyToPlay(true);
        user.getLobby().waitingToPlay(clienthandler);
        sendToClient.send(clienthandler, CommandsToClient.PRINT, "You are now waiting…");
    }


    /**
     * checks if a lobby exists.
     *
     * @param number The number of the lobby
     * @return boolean true (Lobby already exists) or false (lobby doesn't exist yet)
     */
    public boolean checkIfLobbyExists(String number) {
        int number1 = Integer.parseInt(number);
        HashMap<Integer, Lobby> lobbyList = GameList.getLobbyList();
        return number1 <= lobbyList.size();
    }


    /**
     * connects puts a newly created lobby into the lobbyList.
     *
     * @param name name of the lobby
     */
    public synchronized void connectLobby(String name) {
        Lobby lobby = new Lobby(name);
        GameList.getLobbyList().put(GameList.getLobbyList().size(), lobby);
        sendToClient.send(clienthandler, CommandsToClient.PRINT, "You have created Lobby " + lobby.getLobbyName());
    }

}

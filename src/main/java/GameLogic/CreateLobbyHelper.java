package GameLogic;

import Client.ClientReceive;
import Server.ClientHandler;
import utility.IO.CommandsToClient;
import utility.IO.ReceiveFromProtocol;
import utility.IO.SendToClient;

import java.util.HashMap;

public class CreateLobbyHelper {

    private final Server.ClientHandler clientHandler;
    private final SendToClient sendToClient = new SendToClient();
    public final ReceiveFromProtocol receiveFromClient = new ReceiveFromProtocol();


    Server.User user;



    public CreateLobbyHelper(Server.ClientHandler clienthandler) {
        this.clientHandler = clienthandler;
    }

    public void askWhatLobbyToJoin(Server.ClientHandler clienthandler) {
        sendToClient.send(clienthandler, CommandsToClient.PRINT,"Hello there, here are the open lobbies:");
        String lobbyList = GameList.printLobbyList();
        sendToClient.send(clienthandler, CommandsToClient.PRINT, lobbyList);

        sendToClient.send(clienthandler, CommandsToClient.PRINT, "Would you like to create your own lobby?");
        String answer1 = receiveFromClient.receive();
        if (answer1.equalsIgnoreCase("YES")) {

            sendToClient.send(clienthandler, CommandsToClient.PRINT, "Enter Name of the lobby below:");
            String answer2 = receiveFromClient.receive();
            GameList.connectLobby(answer2);
        }
        sendToClient.send(clienthandler, CommandsToClient.PRINT, "what's the number of the lobby which you would like to choose? ");
        String answer = receiveFromClient.receive();
        if (checkIfLobbyExists(answer)) {
            int number = Integer.parseInt(answer);
            GameList.getLobbyList().get(number).addUserToLobby(user);
            sendToClient.send(clienthandler, CommandsToClient.PRINT, "You have been added to lobby" + number + ".");
            System.out.print("lobbyList size: " + GameList.getLobbyList().size());
        } else {
            sendToClient.send(clienthandler, CommandsToClient.PRINT, "Oooops sorry there this Lobby doesn't exist.");
        }



    }


    public boolean checkIfLobbyExists(String number) {
        int number1 = Integer.parseInt(number);
        HashMap<Integer, Lobby> lobbyList = GameList.getLobbyList();
        if (number1 <= lobbyList.size()) {
            return true;
        }
        return false;
    }
}

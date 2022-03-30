package Server;


import java.net.InetAddress;
import java.net.UnknownHostException;

// test comment

/**
 * this class handles all methods related to setting and changing usernames
 */

public class Name {


    /**
     * This method first calls the proposeUsernameBasedOnSystemName Method and asks the Client if he wants the name
     proposed to be his username. If the user doesn't agree, he is then asked to type in his own username. A new user is created
     and connected to the game. If the username already exists in the game he will get a new username assigned.
     **/

    public void askUsername(Game game, ClientHandler clientHandler) {
        try {
            proposeUsernameBasedOnSystemName(game, clientHandler);
        } catch (UnknownHostException e) {
            clientHandler.send("Your Hostname could not be detected.");
            e.printStackTrace();
            clientHandler.send("Please enter a name below.");
            String answer = clientHandler.receive();
            clientHandler.user = game.connect(clientHandler.socket.getInetAddress(), clientHandler, answer);
        }
        if (nameAlreadyExists(game, clientHandler.user.getUsername())) {
            proposeUsernameIfTaken(game, clientHandler);
        } else {
            clientHandler.welcomeUser();
        }
    }

    /**
     * Proposes a username based on the system name and proceeds to ask if the user actually wants that name. If not
     * the user can type in a different one.
     */
    public void proposeUsernameBasedOnSystemName(Game game, ClientHandler clientHandler) throws UnknownHostException {
        String systemName = InetAddress.getLocalHost().getHostName();
        String[] proposedName;
        String selectedName;
        proposedName = systemName.split("-", 2);

        if (proposedName[0].equalsIgnoreCase("DESKTOP")) {
            selectedName = proposedName[0];
        } else {
            selectedName = proposedName[0].substring(0, proposedName[0].length() - 1);
        }
        clientHandler.user = game.connect(clientHandler.socket.getInetAddress(), clientHandler, selectedName);
        if (nameAlreadyExists(game, clientHandler.user.getUsername())) {
            int i = 1;
            String name = clientHandler.user.getUsername();
            while (nameAlreadyExists(game, name)) {
                name = name + "." + i;
                clientHandler.user.setUsername(name);
                i++;
            }
        }
        clientHandler.send("Hey there, would you like to be named " + clientHandler.user.getUsername() + "?");
        String answer = clientHandler.receive();
        if (!answer.equalsIgnoreCase("YES")) {
            clientHandler.send("Please enter your desired name below.");
            String desiredName = clientHandler.receive();
            clientHandler.user.setUsername(desiredName);
        }
    }

    /**
     * In case a username is already taken this method proposes a new username for the client.
     * @param game
     * @param clientHandler
     */
    public void proposeUsernameIfTaken(Game game, ClientHandler clientHandler) {
        String newName = clientHandler.user.getUsername() + clientHandler.user.getDistrict();
        clientHandler.user.setUsername(newName);
        if (nameAlreadyExists(game, clientHandler.user.getUsername())) {
            int i = 1;
            while (nameAlreadyExists(game, clientHandler.user.getUsername())) {
                clientHandler.user.setUsername(clientHandler.user.getUsername() + ".i");
            }
        }
        clientHandler.send("Oooops that one was already taken, but here's a new one: " + newName);
        clientHandler.user.setUsername(newName);
        clientHandler.welcomeUser();
    }

    /**
     * checks if the username already exists and returns true or false.
     */
    public boolean nameAlreadyExists(Game game, String name) {
        boolean alreadyExists = false;
        int length = game.getUserlist().size() - 1;
        int counter = 0;
        for (int i = 0; i < length; i++) {
            String username = game.getUserlist().get(i).getUsername();
            if (username.equalsIgnoreCase(name)) {
                counter++;
            }
        }
        if (counter > 0) {
            alreadyExists = true;
        }
        return alreadyExists;
    }


}

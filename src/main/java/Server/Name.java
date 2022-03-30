package Server;


import java.net.InetAddress;
import java.net.UnknownHostException;

// test comment

/**
 * this class handles all methods related to setting and changing usernames
 */

public class Name {
    private String nameMessage = null;
    ClientHandler clientHandler;
    Game game;

    /**
     * This method first calls the proposeUsernameBasedOnSystemName Method and asks the Client if he wants the name
     proposed to be his username. If the user doesn't agree, he is then asked to type in his own username. A new user is created
     and connected to the game. If the username already exists in the game he will get a new username assigned.
     **/

    public Name(ClientHandler clientHandler) {
        this.clientHandler = clientHandler;
        this.game = clientHandler.game;
    }

    // receives messages
    // the ServerProtocol will use the setMessage method
    public String receive() {
        String tmp_msg;
        while(nameMessage == null) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        tmp_msg = nameMessage;
        nameMessage = null;
        return tmp_msg;
    }



    public void setMessage(String msg) {
        System.out.println("in Name Class: Msg wurde gesettet zu: " + msg);
        this.nameMessage = msg;
    }




    public void askUsername() {

        clientHandler.send("Hey there, would you like to be named " + clientHandler.user.getUsername() + "?");
        String answer = receive();
        if (!answer.equalsIgnoreCase("YES")) { // if they are not happy with the proposed name
            clientHandler.send("Please enter your desired name below.");
            String desiredName = receive();
            if (!desiredName.equals(clientHandler.user.getUsername())) {
                changeNameTo("", desiredName);
            }
        }
        welcomeUser();
    }

    public void changeNameTo(String currentName, String preferredName) {
        if (currentName.equals(preferredName)) {
            clientHandler.send("This is already your name");
        } else if (nameAlreadyExists(preferredName)) { // Wenn preferredName bereits exisitert:
            String newName;
            newName = proposeUsernameIfTaken(preferredName);
            clientHandler.send("SORRY! This tribute already exists. Here is a new one: " + newName);
            clientHandler.user.setUsername(newName);
        } else { // wenn preferredName frei ist:
            clientHandler.user.setUsername(preferredName);
            clientHandler.send("Success! You're now called : " + preferredName);
        }
    }

    /**
     * In case a username is already taken this method proposes a new username for the client.
     */
    public String proposeUsernameIfTaken(String preferredName) {
        int i = 1;
        while (nameAlreadyExists(preferredName)) {
            preferredName = preferredName + "_" + i;
            System.out.println("-");
        }
        return preferredName;
    }

    /**
     * checks if the username already exists and returns true or false.
     */
    private boolean nameAlreadyExists(String desiredName) {
        String tmp_name;
        int length = game.getUserlist().size();
        for (int i = 0; i < length; i++) {
            System.out.println("in");
            tmp_name = game.getUserlist().get(i).getUsername();
            if (tmp_name.equalsIgnoreCase(desiredName)) {
                System.out.println("true");
                return true;
            }
        }
        return false;
    }

    private void welcomeUser() {
        System.out.println(clientHandler.user.getUsername() + " from district " + clientHandler.user.getDistrict() + " has connected");
        clientHandler.send("Your name was drawn at the reaping. Welcome to the Student Games, " + clientHandler.user.getUsername() + " from district " + clientHandler.user.getDistrict() + "!");
    }

}

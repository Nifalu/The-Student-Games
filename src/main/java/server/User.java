package server;

import java.net.InetAddress;
import java.util.Random;


/**
 * The User Object holds various information that is connected to this individual User.
 * Since this User Object is known by the ServerManager, it can also store serverManager related information
 * like avatars, positions, scores etc...
 */
public class User {

    private final ClientHandler clienthandler;
    private final int id;
    private final InetAddress ip;
    private String username;
    private boolean firstTime = true;
    private final int district;
    private boolean isReady;
    private gameLogic.Lobby lobby;
    private int playingField;
    private boolean rolledDice;
    private int specialDiceLeft = 3;
    private boolean isPlaying;
    private boolean isNotActivelyRollingTheDice;
    private int userListNumber;

    public User(ClientHandler clientHandler, InetAddress ip, String username, int id) {
        this.clienthandler = clientHandler;
        this.id = id;
        this.ip = ip;
        this.username = username;
        this.district = assignDistrict();
    }


    // ----- GETTERS -----
    public synchronized String getUsername() {
        return username;
    }

    public synchronized int getDistrict() {
        return district;
    }

    public synchronized int getId() {
        return id;
    }

    public synchronized InetAddress getIp() {
        return ip;
    }

    public synchronized boolean isFirstTime() {
        return firstTime;
    }

    public synchronized ClientHandler getClienthandler() {
        return clienthandler;
    }

    public synchronized gameLogic.Lobby getLobby() {
        return lobby;
    }

    public synchronized boolean getIsReady() {
        return isReady;
    }

    public synchronized int getPlayingField() {
        return playingField;
    }

    public synchronized boolean getRolledDice() {
        return rolledDice;
    }

    public int getSpecialDiceLeft() {
        return specialDiceLeft;
    }

    public boolean getIsPlaying() { return isPlaying; }

    public boolean getIsNotActivelyRollingTheDice() { return isNotActivelyRollingTheDice; }

    public int getUserListNumber() { return userListNumber; }

    // ----- SETTERS -----
    public synchronized void setUsername(String username) {
        this.username = username;
    }

    public void usedSpecialDice() {
        specialDiceLeft--;
    }

    public void resetSpecialDice() { specialDiceLeft = 3; }

    public synchronized void setFirstTime(boolean firstTime) {
        this.firstTime = firstTime;
    }

    public synchronized void setLobby(gameLogic.Lobby newLobby) {
        lobby = newLobby;
    }

    public void setReadyToPlay(boolean isReady) {
        this.isReady = isReady;
    }

    public synchronized void setPlayingField(int newPlayingField) {
        playingField = newPlayingField;
    }

    public void setRolledDice(boolean rolledDice) {
        this.rolledDice = rolledDice;
    }

    public void setIsPlaying(boolean playing) { isPlaying = playing; }

    public void setNotActivelyRollingTheDice() { isNotActivelyRollingTheDice = true; }

    public void setIsActivelyRollingTheDice() { isNotActivelyRollingTheDice = false; }

    public void setUserListNumber(int userListNumber) { this.userListNumber = userListNumber; }



    //-------------OTHER METHODS------------------------------

    /**
     * @return randomInt or 1
     */
    public synchronized static int assignDistrict() {
        Random random = new Random();
        return random.nextInt(12) + 1;
    }
}

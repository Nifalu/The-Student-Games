package GameLogic;


import java.util.HashMap;

public class Game implements Runnable{

    final int lobbyNumber;
    final int minToStart = 2;
    public static HashMap<Integer, Server.User> playersStilPlaying = new HashMap<>();

    public Game(int lobbyNumber) {
        this.lobbyNumber = lobbyNumber;
    }
    public void run() {
        while (GameList.getLobbyList().get(this.lobbyNumber).usersInLobby.size() !=
                GameList.getLobbyList().get(this.lobbyNumber).usersReady.size() ||
                GameList.getLobbyList().get(this.lobbyNumber).usersInLobby.size() < minToStart) {
            try {
                wait(1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        startGame();
    }

    public void startGame() {
        int numPlayers = GameList.getLobbyList().get(lobbyNumber).getUsersInLobby().size();
        int playersEndedGame = 0;
        Calendar calendar = new Calendar(2021, 9, 21);
        calendar.getCurrentDate();
        for (Server.User users : GameList.getLobbyList().get(lobbyNumber).getUsersInLobby().values()) {
            PlayingFields.putPlayersToStart(users);
            for (int i = 0; i < numPlayers; i++) {
                playersStilPlaying.put(i, users);
            }
            while (numPlayers > playersEndedGame + 1) {
                for (int i = 0; i < numPlayers; i++) {
                    PlayingFields.changePosition(playersStilPlaying.get(i), Dice.Dice());

                    if (PlayingFields.getPosition(playersStilPlaying.get(i)) > 90) {
                        playersEndedGame++;
                        playersStilPlaying.remove(i);
                    }
                }
                calendar.newRound6();
            }
        }
    }
}

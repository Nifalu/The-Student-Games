package GameLogic;


import java.util.HashMap;
import java.util.HashSet;

public class Game{

    final int lobbyNumber;
    final int minToStart = 2;
    public static HashMap<Integer, Server.User> playersStillPlaying = new HashMap<>();

    public Game(int lobbyNumber) {
        this.lobbyNumber = lobbyNumber;
    }
    /*
    public void run() {

        while (GameList.getLobbyList().get(this.lobbyNumber).usersInLobby.size() !=
                GameList.getLobbyList().get(this.lobbyNumber).usersReady.size() ||
                GameList.getLobbyList().get(this.lobbyNumber).usersInLobby.size() < minToStart) {
            try {
                Thread.sleep(500);
                System.out.println("not ready to play");
            } catch (Exception e) {
                System.out.println("end");
                e.printStackTrace();
            }
        }
        startGame();
    }
    */
    public void startGame() {
        int numPlayers = GameList.getLobbyList().get(lobbyNumber).getUsersReady().size();
        int playersEndedGame = 0;
        Calendar calendar = new Calendar(2021, 9, 21);
        calendar.getCurrentDate();
        for (int u = 0; u < numPlayers; u++) {
            PlayingFields.putPlayersToStart(GameList.getLobbyList().get(lobbyNumber).getUsersReady().get(u));
            playersStillPlaying.put(u, GameList.getLobbyList().get(lobbyNumber).getUsersReady().get(u));
        }
        while (numPlayers - playersEndedGame != 1) {
            for (int i = 0; i < numPlayers - playersEndedGame; i++) {
                if (PlayingFields.getPosition(playersStillPlaying.get(i)) > 90) {
                    playersEndedGame += 1;
                    playersStillPlaying.remove(i);
                } else {
                    PlayingFields.changePosition(playersStillPlaying.get(i), Dice.Dice());
                }
            }
            calendar.newRound6();
            System.out.println(calendar.getCurrentDate());
        }
        System.out.println("Game ended");

    }
}

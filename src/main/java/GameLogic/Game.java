package GameLogic;


import utility.IO.CommandsToClient;
import utility.IO.SendToClient;

import java.util.HashMap;

public class Game{

    public static HighScore HighScore = new HighScore();
    private final SendToClient sendToClient = new SendToClient();

    final int lobbyNumber;
    final int minToStart = 2;
    public static HashMap<Integer, Server.User> playersPlaying = new HashMap<>();

    public Game(int lobbyNumber) {
        this.lobbyNumber = lobbyNumber;
    }

    public void startGame() {
        int numPlayers = GameList.getLobbyList().get(lobbyNumber).getUsersReady().size();
        if (GameList.getLobbyList().get(lobbyNumber).getLobbyStatus() == 1){
            if (numPlayers >= minToStart) {
                GameList.getLobbyList().get(lobbyNumber).setLobbyStatusToOnGoing();
                int playersEndedGame = 0;
                Calendar calendar = new Calendar(2021, 9, 21);
                calendar.getCurrentDate();
                for (int u = 0; u < numPlayers; u++) {
                    PlayingFields.putPlayersToStart(GameList.getLobbyList().get(lobbyNumber).getUsersReady().get(u));
                    playersPlaying.put(u, GameList.getLobbyList().get(lobbyNumber).getUsersReady().get(u));
                }
                while (numPlayers - playersEndedGame != 1) {
                    int k = 0;
                    for (int i = 0; i < numPlayers - playersEndedGame; i++) {
                        if (i == 0) {
                            System.out.println(calendar.getCurrentDate());
                        }
                        if (playersPlaying.get(i) == null) {
                            k += 1;
                        }
                        if (PlayingFields.getPosition(playersPlaying.get(i + k)) <= 90) {
                            PlayingFields.changePosition(playersPlaying.get(i + k), Dice.Dice());
                            sendToClient.send(playersPlaying.get(i + k).getClienthandler(), CommandsToClient.PRINT, "Still at the Uni " + calendar.getCurrentDate());
                            if (PlayingFields.getPosition(playersPlaying.get(i + k)) > 90) {
                                playersEndedGame += 1;
                                sendToClient.send(playersPlaying.get(i + k).getClienthandler(), CommandsToClient.PRINT,
                                        "Ended in " + calendar.getCurrentDate() + " Ready for Master?");
                                HighScore.add("" + playersPlaying.get(i + k).getUsername(),
                                        Integer.parseInt(calendar.year + "" + String.format("%02d", calendar.month) + "" + String.format("%02d", calendar.day)));
                                playersPlaying.remove(i + k);
                            }
                        }
                    }

                    calendar.newRound6();
                    System.out.println("");

                }
                GameList.getLobbyList().get(lobbyNumber).setLobbyStatusToFinished();
                //sendToClient.send(playersPlaying.get(i).getClienthandler(), CommandsToClient.PRINT, "Game ended. Sorry you looser.");
                HighScore.getTop10();
            } else {
                sendToClient.send(GameList.getLobbyList().get(lobbyNumber).getUsersReady().get(0).getClienthandler(), CommandsToClient.PRINT, "Not enough players ready");
            }
        } else {
            //TODO
            sendToClient.send(playersPlaying.get(0).getClienthandler(), CommandsToClient.PRINT, "Game is already ongoing or has ended");
        }
    }
}

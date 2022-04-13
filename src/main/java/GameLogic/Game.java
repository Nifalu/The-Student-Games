package GameLogic;


import Server.ClientHandler;
import Server.User;
import utility.IO.CommandsToClient;
import utility.IO.ReceiveFromProtocol;
import utility.IO.SendToClient;

import java.awt.desktop.SystemSleepEvent;
import java.util.HashMap;
import java.util.Map;

public class Game{

    public static HighScore HighScore = new HighScore();
    private static final SendToClient sendToClient = new SendToClient();
    //private static ReceiveFromProtocol receiveFromClient = new ReceiveFromProtocol();

    final Lobby lobby;
    final int minToStart = 1;
    public HashMap<Integer, Server.User> playersPlaying;

    public Game(Lobby lobby, HashMap playersPlaying) {
        this.lobby = lobby;
        this.playersPlaying = playersPlaying;
    }

    //Starts the game
    public void start() {
        int numPlayers = lobby.getUsersReady().size();
        if (lobby.getLobbyStatus() == 1){

            //If enough players are ready to play the calendar will be set to 21.09.2021 and
            //every player will be put to the starting point of the playing field.
            if (numPlayers >= minToStart) {
                lobby.setLobbyStatusToOnGoing();
                int playersEndedGame = 0;
                Calendar calendar = new Calendar(2021, 9, 21);
                calendar.getCurrentDate();
                for (int u = 0; u < numPlayers; u++) {
                    lobby.getUsersReady().get(u).setPlayingField(0);
                    playersPlaying.put(u, lobby.getUsersReady().get(u));
                    System.out.println(lobby.getUsersReady().get(u).getUsername());
                }

                //The game will end when the second last player has ended the game
                while (numPlayers - playersEndedGame != 1) {
                    for (int i = 0; i < numPlayers; i++) {
                        if (i == 0) {

                            //Sends at the beginning of each round the current date.
                            lobbyBroadcastToPlayer(calendar.getCurrentDate());
                        }
                        if (playersPlaying.get(i) != null) {
                            if (playersPlaying.get(i).getPlayingField() <= 90) {
                                lobbyBroadcastToPlayer(playersPlaying.get(i).getUsername() + " has to roll the Dice");
                                //TODO Players have to Dice (delete try/catch)
                                try {
                                    Thread.sleep(1000);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                //sends the current users turn and the diced number to PlayingFields
                                changePosition(playersPlaying.get(i), sendAllDice(playersPlaying.get(i).getClienthandler()));

                                //checks if a player has ended the game and adds him to the high score
                                if (playersPlaying.get(i).getPlayingField() > 90) {
                                    playersEndedGame += 1;
                                    sendToClient.send(playersPlaying.get(i).getClienthandler(), CommandsToClient.PRINT,
                                            "Graduated in " + calendar.getCurrentDate() + " Ready for Master?");
                                    HighScore.add("" + playersPlaying.get(i).getUsername(),
                                            Integer.parseInt(calendar.year + "" + String.format("%02d", calendar.month) + "" + String.format("%02d", calendar.day)));
                                    //playersPlaying.remove(i);
                                }
                            }
                        }
                    }
                    lobbyBroadcastToPlayer("");
                    calendar.newRound6();
                }
                //At the end of the game the lobby will be set to finished
                lobby.setLobbyStatusToFinished();
                lobbyBroadcastToPlayer(HighScore.getTop10());
                lobbyBroadcastToPlayer("Congratulations! Most of you have successfully graduated.");
                closeGame();
            } else {
                lobbyBroadcastToPlayer("Life is boring without friends");
            }
        } else {
            //TODO
            sendToClient.send(lobby.usersReady.get(0).getClienthandler(), CommandsToClient.PRINT, "Game is already ongoing or has ended");
        }
    }

    //Will send a message to all players of the game and tells them who's turn it is to roll the dice.
    public int sendAllDice(ClientHandler user) {
        int dice = Dice.dice();
        lobbyBroadcastToPlayer(user.user.getUsername() + " rolled " + dice);
        return dice;
    }

    public static void closeGame() {

    }

    public void changePosition(User user, int move) {
        int currentPosition = user.getPlayingField();
        int newPosition = currentPosition + move;

        //checks if the new position is already occupied and switches places if necessary.
        for (int i = 0; i < playersPlaying.size(); i++) {
            if (playersPlaying.get(i).getPlayingField() == newPosition) {
                playersPlaying.get(i).setPlayingField(currentPosition);
                lobbyBroadcastToPlayer(user.getUsername() + " pushed back " + playersPlaying.get(i).getUsername() + " to " + currentPosition);
            }
        }

        //puts the player to the new position
        user.setPlayingField(newPosition);
        if (newPosition <= 90) {
            lobbyBroadcastToPlayer( user.getUsername() + " moved from: " + currentPosition + " to " + newPosition);
        } else {
            lobbyBroadcastToPlayer( user.getUsername() + " moved from: " + currentPosition + " to Bachelorfeier");
        }
        checkField(user, newPosition);
    }

    public void checkField(User user, int field) {
        // 2 + 56 ladder up
        String msg = null;
        if (field == 2) {
            lobbyBroadcastToPlayer(user.getUsername() + ": Leiter hoch");
            changePosition(user, 15 - field);
        } else if (field == 56) {
            lobbyBroadcastToPlayer(user.getUsername() + ": Leiter hoch");
            changePosition(user, 59 - field);
        }
        // 21 - 89 ladder down
        else if (field == 21) {
            lobbyBroadcastToPlayer(user.getUsername() + ": Leiter runter");
            changePosition(user, 14 - field);
        } else if (field == 27) {
            lobbyBroadcastToPlayer(user.getUsername() + ": Leiter runter");
            changePosition(user, 10 - field);
        } else if (field == 53) {
            lobbyBroadcastToPlayer(user.getUsername() + ": Leiter runter");
            changePosition(user, 36 - field);
        } else if (field == 58) {
            lobbyBroadcastToPlayer(user.getUsername() + ": Leiter runter");
            changePosition(user, 40 - field);
        } else if (field == 81) {
            lobbyBroadcastToPlayer(user.getUsername() + ": Leiter runter");
            changePosition(user, 78 - field);
        } else if (field == 89) {
            lobbyBroadcastToPlayer(user.getUsername() + ": Leiter runter");
            changePosition(user, 68 - field);
        }
        //Cards
        else if (field == 18 || field == 46 || field == 74) {
            String card = Cards.getCards();
            String arr[] = card.split(" ", 2);
            int positionToChange = Integer.parseInt(arr[0]);
            String textKarten = arr[1];
            lobbyBroadcastToPlayer(user.getUsername() + " zieht eine Ereigniskarte: " + textKarten);
            changePosition(user, positionToChange);
        }
        // Quiz
        else if (field == 23 || field == 50) {
            String quizQuestion = Quiz.quiz();
            String quiz[] = quizQuestion.split("§");
            lobbyBroadcastToPlayer(quiz[0]);
            changePosition(user, Integer.parseInt(quiz[2]));
        }
        // This is the end
        else if (field > 90) {
            lobbyBroadcastToPlayer(user.getUsername() + " has successfully graduated from university");
        }
    }

    public void lobbyBroadcastToPlayer(String msg) {
        sendToClient.lobbyBroadcastDice(lobby.usersReady,
                CommandsToClient.PRINT, msg);
    }
}

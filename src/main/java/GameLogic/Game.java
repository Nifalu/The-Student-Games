package GameLogic;


import Server.ClientHandler;
import Server.User;
import utility.IO.CommandsToClient;
import utility.IO.ReceiveFromProtocol;
import utility.IO.SendToClient;

import java.awt.desktop.SystemSleepEvent;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Game implements Runnable{

    public static HighScore HighScore = new HighScore();
    private static final SendToClient sendToClient = new SendToClient();
    //private static ReceiveFromProtocol receiveFromClient = new ReceiveFromProtocol();

    final Lobby lobby;
    final int minToStart = 2;
    public HashMap<Integer, Server.User> playersPlaying;
    int numPlayers;
    int maxTimeToAnswerQuiz = 15000;
    int maxTimeToRollDice = 3000;
    int playersEndedGame = 0;
    boolean rolledDice;
    boolean rolledSpecialDice;
    public User userToRollDice;
    boolean quizAnsweredCorrect;
    boolean quizAnsweredWrong;
    boolean quizOngoing;
    public User userToAnswerQuiz;
    public String correctAnswer;
    HighScore highScoreGame;

    public Game(Lobby lobby, HashMap playersPlaying) {
        this.lobby = lobby;
        this.playersPlaying = playersPlaying;
    }

    //Starts the game
    public void run() {
        highScoreGame = new HighScore();
        numPlayers = lobby.getUsersReady().size();
        if (lobby.getLobbyStatus() == 1){

            //If enough players are ready to play the calendar will be set to 21.09.2021 and
            //every player will be put to the starting point of the playing field.
            if (numPlayers >= minToStart) {
                lobby.setLobbyStatusToOnGoing();
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
                        if (playersPlaying.get(i).getClienthandler().getHasStopped()){
                            if (playersPlaying.get(i).getPlayingField() <= 90 &&
                                    playersPlaying.get(i).getPlayingField() != -69) {
                                lobbyBroadcastToPlayer(playersPlaying.get(i).getUsername() + " has to roll the Dice");
                                /**
                                try {
                                    Thread.sleep(500);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                 */
                                //sends the current users turn and the diced number to PlayingFields
                                changePosition(playersPlaying.get(i), sendAllDice(playersPlaying.get(i).getClienthandler().user));

                                //checks if a player has ended the game and adds him to the high score
                                if (playersPlaying.get(i).getPlayingField() > 90) {
                                    playersEndedGame += 1;
                                    sendToClient.send(playersPlaying.get(i).getClienthandler(), CommandsToClient.PRINT,
                                            "Graduated in " + calendar.getCurrentDate() + " Ready for Master?");
                                    HighScore.add("" + playersPlaying.get(i).getUsername(),
                                            Integer.parseInt(calendar.year + "" + String.format("%02d", calendar.month) + "" + String.format("%02d", calendar.day)));
                                    highScoreGame.add("" + playersPlaying.get(i).getUsername(),
                                            Integer.parseInt(calendar.year + "" + String.format("%02d", calendar.month) + "" + String.format("%02d", calendar.day)));
                                }
                            }
                        } else {
                            if (playersPlaying.get(i).getPlayingField() != -69) {
                                lostConnection(playersPlaying.get(i));
                            }
                        }
                    }
                    lobbyBroadcastToPlayer("");
                    calendar.newRound6();
                }
                //At the end of the game the lobby will be set to finished
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
    public int sendAllDice(Server.User user) {
        setUserToRollDice(user);
        user.setRolledDice(false);
        int dice = Dice.dice();
        for (int i = 0; i < maxTimeToRollDice; i++) {
            if (rolledDice) {
                dice = Dice.dice();
                break;
            } else if (rolledSpecialDice) {
                dice = Dice.specialDice();
            }
            else {
                try {
                    Thread.sleep(1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        user.setRolledDice(true);
        rolledDice = false;
        rolledSpecialDice = false;
        lobbyBroadcastToPlayer(user.getUsername() + " rolled " + dice);
        return dice;
    }

    public void setRolledDice(String user, int number) {
        if (!userToRollDice.getRolledDice()) {
            if (userToRollDice.getUsername().equals(user)) {
                if (number == 4) {
                    if (userToRollDice.getSpecialDiceLeft() > 0) {
                        rolledSpecialDice = true;
                        userToRollDice.usedSpecialDice();
                        lobbyBroadcastToPlayer(userToRollDice.getUsername() + " has " + userToRollDice.getSpecialDiceLeft() + " special dices left");
                    }
                } else {
                    rolledDice = true;
                }
            }
        }
    }

    public void setUserToRollDice(Server.User user) {
        userToRollDice = user;
    }

    public void setAnswer(String answer) {
        correctAnswer = answer;
    }
    public void setUserToAnswerQuiz(Server.User user) {
        userToAnswerQuiz = user;
    }
    public void quizAnswer(String user, String receivedAnswer) {
        if (quizOngoing) {
            if (userToAnswerQuiz.getUsername().equals(user)) {
                if (correctAnswer.equals(receivedAnswer)) {
                    quizAnsweredCorrect = true;
                } else {
                    quizAnsweredWrong = true;
                }
            }
        }
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
            quizOngoing = true;
            String quizQuestion = Quiz.quiz();
            String quiz[] = quizQuestion.split("§");
            setUserToAnswerQuiz(user);
            setAnswer(quiz[1]);
            lobbyBroadcastToPlayer("Quizfrage an " + user.getUsername() + ". " + quiz[0]);
            for (int i = 0; i < maxTimeToAnswerQuiz; i++) {
                if (quizAnsweredCorrect) {
                    changePosition(userToAnswerQuiz, Integer.parseInt(quiz[2]));
                    quizAnsweredCorrect = false;
                    lobbyBroadcastToPlayer(user.getUsername() + "'s answer: " + quiz[1] +" is correct.");
                    break;
                } else if (quizAnsweredWrong || i == 14999){
                    quizAnsweredWrong = false;
                    lobbyBroadcastToPlayer(user.getUsername() + "'s answer is wrong.");
                    if (user.isFirstTime()) {
                        changePosition(userToAnswerQuiz, Integer.parseInt(quiz[2]) * -1);
                        user.setFirstTime(false);
                    } else {
                        gameOver(user);
                    }
                    break;
                } else {
                    try {
                        Thread.sleep(1);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            quizOngoing = false;
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

    public void gameOver (Server.User user) {
        lobbyBroadcastToPlayer(user.getUsername() + " has been exmatriculated.");
        user.setPlayingField(-69);
        user.resetSpecialDice();
        playersEndedGame++;
    }

    public void lostConnection (Server.User user) {
        lobbyBroadcastToPlayer(user.getUsername() + " lost connection and left the game.");
        user.setPlayingField(-69);
        user.resetSpecialDice();
        playersEndedGame++;
    }

    public void closeGame() {
        lobbyBroadcastToPlayer("Best students of this game: " + highScoreGame.getTop10());
        lobbyBroadcastToPlayer("All time leaders: " + HighScore.getTop10());
        lobbyBroadcastToPlayer("Congratulations! Most of you have successfully graduated.");
        for (int i = 0; i < numPlayers; i++) {
            User user = lobby.getUsersReady().get(i);
            lobby.removeUserFromLobby(user);
            user.setLobby(GameList.getLobbyList().get(0));
            user.setFirstTime(true);
            user.resetSpecialDice();
        }
        lobby.setLobbyStatusToFinished();
    }
}

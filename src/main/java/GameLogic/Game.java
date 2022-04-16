package GameLogic;


import Server.User;
import utility.IO.CommandsToClient;
import utility.IO.SendToClient;
import java.util.HashMap;

public class Game implements Runnable{

    public static HighScore highScore = new HighScore();
    private static final SendToClient sendToClient = new SendToClient();

    final Lobby lobby;
    public HashMap<Integer, Server.User> playersPlaying;
    int numPlayers;
    int maxTimeToAnswerQuiz = 30000;
    int maxTimeToRollDice = 20000;
    int maxTimeWhenInactive = 5000;
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

    //Calendar will be set to 21.09.2021 and every player will be put to the starting point of the playing field.
        Calendar calendar = new Calendar(2021, 9, 21);
        calendar.getCurrentDate();
        for (int u = 0; u < numPlayers; u++) {
            lobby.getUsersReady().get(u).setPlayingField(0);
            playersPlaying.put(u, lobby.getUsersReady().get(u));
            lobby.getUsersReady().get(u).setIsPlaying(true);
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

                        //sends the current users turn and the diced number to PlayingFields
                        changePosition(playersPlaying.get(i), sendAllDice(playersPlaying.get(i).getClienthandler().user));

                        //checks if a player has ended the game and adds him to the high score
                        if (playersPlaying.get(i).getPlayingField() > 90) {
                            playersEndedGame += 1;
                            sendToClient.send(playersPlaying.get(i).getClienthandler(), CommandsToClient.PRINT,
                                    "Graduated in " + calendar.getCurrentDate() + " Ready for Masters?");
                            highScore.add("" + playersPlaying.get(i).getUsername(),
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
    }

    //Will send a message to all players of the game and tells them who's turn it is to roll the dice.
    public int sendAllDice(Server.User user) {
        setUserToRollDice(user);
        user.setRolledDice(false);
        int dice = Dice.dice();
        int time = maxTimeToRollDice;
        if (user.getIsNotActivelyRollingTheDice()) {
            time = maxTimeWhenInactive;
        }
        for (int i = 0; i < time; i++) {
            if (rolledDice) {
                dice = Dice.dice();
                break;
            } else if (rolledSpecialDice) {
                dice = Dice.specialDice();
            } else if (i == time - 1) {
                user.setNotActivelyRollingTheDice();
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

    public void cheat(String user, int number) {
        if (!userToRollDice.getRolledDice()) {
            if (userToRollDice.getUsername().equals(user)) {
                lobbyBroadcastToPlayer(userToRollDice.getUsername() + " has rich parents.");
                changePosition(userToRollDice, number - userToRollDice.getPlayingField());
                rolledDice = true;
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
            lobbyBroadcastToPlayer(user.getUsername() + ": ladder up");
            changePosition(user, 15 - field);
        } else if (field == 56) {
            lobbyBroadcastToPlayer(user.getUsername() + ": ladder up");
            changePosition(user, 59 - field);
        }
        // 21 - 89 ladder down
        else if (field == 21) {
            lobbyBroadcastToPlayer(user.getUsername() + ": ladder down");
            changePosition(user, 14 - field);
        } else if (field == 27) {
            lobbyBroadcastToPlayer(user.getUsername() + ": ladder down");
            changePosition(user, 10 - field);
        } else if (field == 53) {
            lobbyBroadcastToPlayer(user.getUsername() + ": ladder down");
            changePosition(user, 36 - field);
        } else if (field == 58) {
            lobbyBroadcastToPlayer(user.getUsername() + ": ladder down");
            changePosition(user, 40 - field);
        } else if (field == 81) {
            lobbyBroadcastToPlayer(user.getUsername() + ": ladder down");
            changePosition(user, 78 - field);
        } else if (field == 89) {
            lobbyBroadcastToPlayer(user.getUsername() + ": ladder down");
            changePosition(user, 68 - field);
        }
        //Cards
        else if (field == 18 || field == 46 || field == 74) {
            String card = Cards.getCards();
            String arr[] = card.split(" ", 2);
            int positionToChange = Integer.parseInt(arr[0]);
            String textCard = arr[1];
            lobbyBroadcastToPlayer(user.getUsername() + " draws an action card: " + textCard);
            changePosition(user, positionToChange);
        }
        // Quiz
        else if (field == 23 || field == 50) {
            quizOngoing = true;
            String quizQuestion = Quiz.quiz();
            String quiz[] = quizQuestion.split("§");
            setUserToAnswerQuiz(user);
            setAnswer(quiz[1]);
            lobbyBroadcastToPlayer("Quiz for " + user.getUsername() + ". " + quiz[0]);
            for (int i = 0; i < maxTimeToAnswerQuiz; i++) {
                if (quizAnsweredCorrect) {
                    lobbyBroadcastToPlayer(user.getUsername() + "'s answer: " + quiz[1] +" is correct.");
                    changePosition(userToAnswerQuiz, Integer.parseInt(quiz[2]));
                    quizAnsweredCorrect = false;
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
        resetPlayer(user);
        playersEndedGame++;
    }

    public void lostConnection (Server.User user) {
        lobbyBroadcastToPlayer(user.getUsername() + " lost connection and left the game.");
        user.setPlayingField(-69);
        resetPlayer(user);
        user.setIsPlaying(false);
        playersEndedGame++;
    }

    public void closeGame() {
        lobbyBroadcastToPlayer("Best students of this game: " + highScoreGame.getTop10());
        lobbyBroadcastToPlayer("All time leaders: " + highScore.getTop10());
        lobbyBroadcastToPlayer("Congratulations! Most of you have successfully graduated.");
        for (int i = 0; i < numPlayers; i++) {
            User user = lobby.getUsersReady().get(i);
            lobby.removeUserFromLobby(user);
            user.setLobby(GameList.getLobbyList().get(0));
            resetPlayer(user);
            user.setIsPlaying(false);
        }
        lobby.setLobbyStatusToFinished();
    }
    
    public void resetPlayer (Server.User user) {
        user.setFirstTime(true);
        user.resetSpecialDice();
        user.setReadyToPlay(false);
    }
}

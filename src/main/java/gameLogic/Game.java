package gameLogic;


import server.ServerManager;
import server.User;
import utility.io.CommandsToClient;
import utility.io.SendToClient;

import java.util.HashMap;

/**
 * This class handles everything related with the game
 */
public class Game implements Runnable {

  /**
   * sendToClient object to communicate with the client
   */
  private static final SendToClient sendToClient = new SendToClient();

  /**
   * the games lobby
   */
  final Lobby lobby;

  /**
   * the games highscore
   */
  final HighScore highScore;

  /**
   * the player in the game
   */
  public HashMap<Integer, server.User> playersPlaying;

  /**
   * the number of players
   */
  int numPlayers;

  /**
   * how much time a player has to answer a quiz question
   */
  int maxTimeToAnswerQuiz = 60000;

  /**
   * how much time a player has to roll a dice
   */
  int maxTimeToRollDice = 15000;

  /**
   * how much time a player has to do something when they're inactive
   */
  int maxTimeWhenInactive = 5000;

  /**
   * how many player have finished
   */
  int playersEndedGame = 0;

  /**
   * saves the dsq
   */
  int dsq = 0;

  /**
   * notes whether the regular dice has been rolled or not
   */
  boolean rolledDice;

  /**
   * notes whether the special dice has been rolled or not
   */
  boolean rolledSpecialDice;

  /**
   * marks the user whose turn it is to roll the dice
   */
  public User userToRollDice;

  /**
   * notes whether a quiz question has been answered correctly
   */
  boolean quizAnsweredCorrect;

  /**
   * marks whether a quiz question has been answered wrong
   */
  boolean quizAnsweredWrong;

  /**
   * marks whether a quiz is ongoing or not
   */
  boolean quizOngoing;

  /**
   * marks whether a player has cheated or not
   */
  boolean cheated;

  /**
   * marks the user to answer a quiz question
   */
  public User userToAnswerQuiz;

  /**
   * notes the correct answer to a quiz question
   */
  public String correctAnswer;

  /**
   * the highscore of the game
   */
  HighScore highScoreGame;

  /**
   * creates a new game
   *
   * @param lobby          Lobby
   * @param playersPlaying HashMap
   * @param highScore      HighScore
   */
  public Game(Lobby lobby, HashMap<Integer, User> playersPlaying, HighScore highScore) {
    this.lobby = lobby;
    this.playersPlaying = playersPlaying;
    this.highScore = highScore;
  }

  /**
   * Starts the game as soon as it has been initiated by its lobby
   */
  public void run() {
    highScoreGame = new HighScore();
    numPlayers = lobby.getUsersReady().size();


    // Calendar will be set to 21.09.2021 and every player will be put to the starting point of the playing field.
    Calendar calendar = new Calendar(2021, 9, 21);
    calendar.getCurrentDate();
    for (int u = 0; u < numPlayers; u++) {
      lobby.getUsersReady().get(u).setPlayingField(0);
      playersPlaying.put(u, lobby.getUsersReady().get(u));
      lobby.getUsersReady().get(u).setIsPlaying(true);
    }

    // The game will run until the second last player has ended the game
    while (numPlayers - (playersEndedGame + dsq) > 1) {
      for (int i = 0; i < numPlayers; i++) {
        if (i == 0) {
          //Sends at the beginning of each round the current date.
          lobbyBroadcastToPlayer(calendar.getCurrentDate());
        }
        if (ServerManager.getActiveClientList().contains(playersPlaying.get(i).getClienthandler())) {
          if (playersPlaying.get(i).getPlayingField() <= 88 &&
              !playersPlaying.get(i).getGameOver()) {
            lobbyBroadcastToPlayer(playersPlaying.get(i).getUsername() + " has to roll the Dice");

            //sends the current users turn and the diced number to PlayingFields
            changePosition(playersPlaying.get(i), sendAllDice(playersPlaying.get(i).getClienthandler().user));

            // moves the player characters in the GUI
            for (int j = 0; j < numPlayers; j++) {
              if (playersPlaying.get(j).getPlayingField() >= 0 && playersPlaying.get(j).getPlayingField() <= 89) {
                sendToClient.lobbyBroadcast(lobby.getUsersInLobby(), CommandsToClient.GUIMOVECHARACTER, playersPlaying.get(j).characterColor + "--" + playersPlaying.get(j).getPlayingField());
              }
            }

            cheated = false;

            //checks if a player has ended the game and adds him to the high score
            if (playersPlaying.get(i).getPlayingField() > 88) {
              playersEndedGame += 1;
              lobbyBroadcastToPlayer(playersPlaying.get(i).getUsername() + " graduated " + place() + " in " + calendar.getCurrentDate());
              //sendToClient.send(playersPlaying.get(i).getClienthandler(), CommandsToClient.PRINT,
              //        "Graduated " + place() + " in " + calendar.getCurrentDate() + " Ready for Masters?");
              highScore.add("" + playersPlaying.get(i).getUsername(),
                  Integer.parseInt(calendar.year + "" + String.format("%02d", calendar.month) + "" + String.format("%02d", calendar.day)), "global");
              highScoreGame.add("" + playersPlaying.get(i).getUsername(),
                  Integer.parseInt(calendar.year + "" + String.format("%02d", calendar.month) + "" + String.format("%02d", calendar.day)), "game");
            }
          }
          // moves the player characters in the GUI
          for (int j = 0; j < numPlayers; j++) {
            if (playersPlaying.get(j).getPlayingField() >= 0 && playersPlaying.get(j).getPlayingField() <= 89) {
              sendToClient.lobbyBroadcast(lobby.getUsersInLobby(), CommandsToClient.GUIMOVECHARACTER, playersPlaying.get(j).characterColor + "--" + playersPlaying.get(j).getPlayingField());
            }
          }
        } else {
          if (playersPlaying.get(i).getPlayingField() != 0) {
            lostConnection(playersPlaying.get(i));
          }
        }
      }
      lobbyBroadcastToPlayer("");
      calendar.newRound6();
    }
    //At the end of the game the lobby will be set to be finished
    closeGame();
  }

  /**
   * Will send a message to all players of the game and tells them who's turn it is to roll the dice.
   *
   * @param user To roll the dice
   * @return The diced number
   */
  public int sendAllDice(server.User user) {
    setUserToRollDice(user);
    user.setRolledDice(false);
    int dice = Dice.dice();
    int time = maxTimeToRollDice;
    if (user.getIsNotActivelyRollingTheDice()) {
      time = maxTimeWhenInactive;
    }
    for (int i = 0; i < time; i++) {
      if (rolledDice) {
        user.setIsActivelyRollingTheDice();
        dice = Dice.dice();
        break;
      } else if (rolledSpecialDice) {
        user.setIsActivelyRollingTheDice();
        dice = Dice.specialDice();
        lobbyBroadcastToPlayer(userToRollDice.getUsername() + " rolled a special dice and has " + userToRollDice.getSpecialDiceLeft() + " dices left");
        sendToClient.send(userToRollDice.getClienthandler(), CommandsToClient.DICEDICELEFT, Integer.toString(userToRollDice.getSpecialDiceLeft()));
        break;
      } else if (i == time - 1) {
        user.setNotActivelyRollingTheDice();
      } else {
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
    if (!cheated) {
      lobbyBroadcastToPlayer(user.getUsername() + " rolled " + dice);
    } else {
      dice = 0;
    }
    return dice;
  }

  /**
   * Receives the command from lobby with the user who sends the command and the number of sides of the cube.
   * Also checks if the user who sent the command is the player on the move.
   *
   * @param user   That sent the command
   * @param number Number of sides of the cube (4 or 6)
   */
  public void setRolledDice(String user, int number) {
    if (!userToRollDice.getRolledDice()) {
      if (userToRollDice.getUsername().equals(user)) {
        if (number == 4) {
          if (userToRollDice.getSpecialDiceLeft() > 0) {
            rolledSpecialDice = true;
            userToRollDice.usedSpecialDice();
          } else {
            sendToClient.send(userToRollDice.getClienthandler(), CommandsToClient.PRINT, "You have no special dices left.");
          }
        } else {
          rolledDice = true;
        }
      }
    }
  }

  /**
   * Receives the command from lobby when the correct cheat code has been entered.
   *
   * @param user   User sending the command
   * @param number Target field
   */
  public void cheat(String user, int number) {
    if (!userToRollDice.getRolledDice()) {
      if (userToRollDice.getUsername().equals(user)) {
        cheated = true;
        lobbyBroadcastToPlayer(userToRollDice.getUsername() + " has rich parents and moved to: " + number);
        changePosition(userToRollDice, number - userToRollDice.getPlayingField());
        rolledDice = true;
      }
    }
  }

  /**
   * Receives the user that has to roll the dice
   *
   * @param user User who has to roll the dice
   */
  public void setUserToRollDice(server.User user) {
    userToRollDice = user;
  }

  /**
   * Receives the correct answer from the server and saves is, so it can later check if the answer from the user is correct.
   *
   * @param answer Correct answer
   */
  public void setAnswer(String answer) {
    correctAnswer = answer;
  }

  /**
   * Receives the user that has to answer the quiz
   *
   * @param user User to answer quiz
   */
  public void setUserToAnswerQuiz(server.User user) {
    userToAnswerQuiz = user;
  }

  /**
   * Checks if the answer comes from the right person and if it is correct
   *
   * @param user           User sending the answer
   * @param receivedAnswer Received answer
   */
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

  /**
   * Changes the position on the field.
   *
   * @param user User who is playing right now
   * @param move Number of position to be changed
   */
  public void changePosition(User user, int move) {
    int currentPosition = user.getPlayingField();
    int newPosition = currentPosition + move;

    //checks if the new position is already occupied and switches places if necessary.
    if (newPosition <= 88 && newPosition != 0) {
      for (int i = 0; i < playersPlaying.size(); i++) {
        if (playersPlaying.get(i).getPlayingField() == newPosition && playersPlaying.get(i).getPlayingField() != user.getPlayingField()) {
          playersPlaying.get(i).setPlayingField(currentPosition);
          lobbyBroadcastToPlayer(user.getUsername() + " pushed back " + playersPlaying.get(i).getUsername() + " to " + currentPosition);
        }
      }
    }

    //puts the player to the new position
    user.setPlayingField(newPosition);
    if (!cheated) {
      if (newPosition <= 88) {
        lobbyBroadcastToPlayer(user.getUsername() + " moved from: " + currentPosition + " to " + newPosition);
        checkField(user, newPosition);
      } else {
        user.setPlayingField(89);
        lobbyBroadcastToPlayer(user.getUsername() + " moved from: " + currentPosition + " to Bachelorfeier");
        user.setIsPlaying(false);
      }
      //checkField(user, newPosition);
    } else {
      if (newPosition > 88) {
        user.setPlayingField(89);
        checkField(user, newPosition);
        user.setIsPlaying(false);
      }
    }
  }

  /**
   * Checks if the new position of the player to play is a special field
   *
   * @param user  User who is playing right now
   * @param field Users new position
   */
  public void checkField(User user, int field) {
    String msg = null;
    if (field == 1) { // 1 + 55 ladder up
      lobbyBroadcastToPlayer(user.getUsername() + ": ladder up");
      changePosition(user, 15 - field);
    } else if (field == 55) {
      lobbyBroadcastToPlayer(user.getUsername() + ": ladder up");
      changePosition(user, 59 - field);
    } else if (field == 20) { // 20 - 87 ladder down
      lobbyBroadcastToPlayer(user.getUsername() + ": ladder down");
      changePosition(user, 14 - field);
    } else if (field == 26) {
      lobbyBroadcastToPlayer(user.getUsername() + ": ladder down");
      changePosition(user, 10 - field);
    } else if (field == 52) {
      lobbyBroadcastToPlayer(user.getUsername() + ": ladder down");
      changePosition(user, 36 - field);
    } else if (field == 57) {
      lobbyBroadcastToPlayer(user.getUsername() + ": ladder down");
      changePosition(user, 40 - field);
    } else if (field == 80) {
      lobbyBroadcastToPlayer(user.getUsername() + ": ladder down");
      changePosition(user, 78 - field);
    } else if (field == 87) {
      lobbyBroadcastToPlayer(user.getUsername() + ": ladder down");
      changePosition(user, 68 - field);
    } else if (field == 17 || field == 31 || field == 45 || field == 73) { // Action Cards
      String card = Cards.getCards();
      String[] arr = card.split(" ", 2);
      int positionToChange = Integer.parseInt(arr[0]);
      String textCard = arr[1];
      lobbyBroadcastToPlayer("§" + user.getUsername() + " draws an action card:" + "§" + textCard + "§");
      changePosition(user, positionToChange);
    } else if (field == 7 || field == 22 || field == 49 || field == 65) { // Quiz
      quizOngoing = true;
      String quizQuestion = Quiz.quiz();
      String[] quiz = quizQuestion.split("Ç");
      setUserToAnswerQuiz(user);
      setAnswer(quiz[1]);
      lobbyBroadcastToPlayer("§" + "Exam question for " + user.getUsername() + "." + "§" + quiz[0]);
      for (int i = 0; i < maxTimeToAnswerQuiz; i++) {
        if (quizAnsweredCorrect) {
          lobbyBroadcastToPlayer(user.getUsername() + "'s answer: " + quiz[1] + " is correct.");
          changePosition(userToAnswerQuiz, Integer.parseInt(quiz[2]));
          quizAnsweredCorrect = false;
          break;
        } else if (quizAnsweredWrong || i == maxTimeToAnswerQuiz - 1) {
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
    } else if (field > 88) { // This is the end of the game for a player
      lobbyBroadcastToPlayer(user.getUsername() + " has successfully graduated from university");
    }
  }

  /**
   * Sends a message to all players in a game
   *
   * @param msg Message to be sent.
   */
  public void lobbyBroadcastToPlayer(String msg) {
    sendToClient.lobbyBroadcast(lobby.usersReady,
        CommandsToClient.PRINTGUIGAMETRACKER, msg);
    try {
      Thread.sleep(50);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  /**
   * If a player has answered two question wrong then the person will exmatriculated.
   *
   * @param user Player who has to stop playing the game
   */
  public void gameOver(server.User user) {
    lobbyBroadcastToPlayer(user.getUsername() + " has been exmatriculated.");
    user.setPlayingField(0);
    user.setGameOver();
    resetPlayer(user);
    dsq++;
  }

  /**
   * Takes a player out of the game when she or he loses connection and sends a message to all.
   *
   * @param user Player who lost connection with the server
   */
  public void lostConnection(server.User user) {
    lobbyBroadcastToPlayer(user.getUsername() + " lost connection and left the game.");
    user.setPlayingField(0);
    user.setGameOver();
    resetPlayer(user);
    user.setIsPlaying(false);
    dsq++;
  }

  /**
   * Closes the game and sets the lobby's status to finished
   */
  public void closeGame() {
    //if (highScore.getTop10().length() > 0) {
      lobbyBroadcastToPlayer("All time leaders:§" + highScore.getTop10());
      sendToClient.serverBroadcast(CommandsToClient.PRINTWINNERSGUI, highScore.getTop10());
    //}
    if (highScoreGame.getTop10().length() > 0) {
      lobbyBroadcastToPlayer("Best students of this game:§" + highScoreGame.getTop10());
      sendToClient.serverBroadcast(CommandsToClient.PRINTWINNERSGUI, highScore.getTop10());
      if (playersEndedGame == numPlayers) {
        lobbyBroadcastToPlayer("Congratulations! All of you have successfully graduated.");
      } else {
        lobbyBroadcastToPlayer("Congratulations! Most of you have successfully graduated.");
      }
    } else {
      lobbyBroadcastToPlayer("None of you has graduated.");
      sendToClient.serverBroadcast(CommandsToClient.PRINTWINNERSGUI, highScore.getTop10());
    }
    for (int i = 0; i < numPlayers; i++) {
      User user = lobby.getUsersReady().get(i);
      lobby.removeUserFromLobby(user);
      user.setLobby(GameList.getLobbyList().get(0));
      resetPlayer(user);
      user.setIsPlaying(false);
    }
    lobby.setLobbyStatusToFinished();
  }

  /**
   * Resets a player (e.g. number of dices with only 4 sides and number of wrong answered questions).
   *
   * @param user Player to be reset.
   */
  public void resetPlayer(server.User user) {
    user.setFirstTime(true);
    user.resetSpecialDice();
    user.setReadyToPlay(false);
  }

  /**
   * Returns the correct placement of the user of the game after finishing it.
   *
   * @return String with the correct placement.
   */
  public String place() {
    String s = "1st";
    if (playersEndedGame == 2) {
      s = "2nd";
    } else if (playersEndedGame == 3) {
      s = "3rd";
    } else if (playersEndedGame > 3) {
      s = playersEndedGame + "th";
    }
    return s;
  }
}

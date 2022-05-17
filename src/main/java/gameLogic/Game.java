package gameLogic;


import server.GameServer;
import server.ServerManager;
import server.User;
import utility.io.CommandsToClient;
import utility.io.SendToClient;

import java.util.HashMap;
import java.util.Random;

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

  public HashMap<Integer, server.User> playersPlayingAndSpectators;

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
    music("audio/gameStart.mp3");

    // Calendar will be set to 21.09.2021 and every player will be put to the starting point of the playing field.
    Calendar calendar = new Calendar(2021, 9, 20);
    calendar.getCurrentDate();
    for (int u = 0; u < numPlayers; u++) {
      lobby.getUsersReady().get(u).setPlayingField(0);
      playersPlaying.put(u, lobby.getUsersReady().get(u));
      lobby.getUsersReady().get(u).setIsPlaying(true);
    }

    // The game will run until the second last player has ended the game
    while (numPlayers - (playersEndedGame + dsq) > 1 && GameServer.isonline) {
      for (int i = 0; i < numPlayers; i++) {
        if (i == 0) {
          //Sends at the beginning of each round the current date.
          lobbyBroadcastToPlayer(calendar.getCurrentDate());
        }

        if (ServerManager.getActiveClientList().contains(playersPlaying.get(i).getClienthandler())) {
          if (playersPlaying.get(i).getPlayingField() <= 88 &&
              !playersPlaying.get(i).getGameOver()) {
            if (playersPlaying.get(i).getLobbyBeforeDisconnection().equals(this.lobby.getLobbyName())) {
              playersPlaying.get(i).setLobbyBeforeDisconnection("lobbyNameBeforeDisconnection");
              dsq--;
            }
            sendToClient.lobbyBroadcast(lobby.getUsersInLobby(), CommandsToClient.MARKPLAYER, Integer.toString(playersPlaying.get(i).gameTokenNr));
            sendToClient.send(playersPlaying.get(i).getClienthandler(), CommandsToClient.YOURTURN, "");

            // mis-used for a quickfix. if msg does not contain "roll the Dice", the buttons in the menu scene will fail.
            // the buttons check if "roll the dice" has been sent to detect that the game has started. (I know this is bad)
            lobbyBroadcastToPlayer(playersPlaying.get(i).getUsername() + " has to roll the Dice");

            //sends the current users turn and the diced number to PlayingFields
            changePosition(playersPlaying.get(i), sendAllDice(playersPlaying.get(i).getClienthandler().user));

            // moves the player characters in the GUI
            positionUpdate();

            cheated = false;

            //checks if a player has ended the game and adds him to the high score
            if (playersPlaying.get(i).getPlayingField() > 88) {
              playersEndedGame += 1;

              // mis-used for a quickfix. if msg does not contain "graduated", the buttons in the menu scene will fail.
              // the buttons check if "graduated" has been sent to detect that someone has finished (I know this is bad)
              lobbyBroadcastToPlayer(playersPlaying.get(i).getUsername() + " graduated " + place() + " in " + calendar.getCurrentDate());

              //MusicPlayer jiggleJiggle = new MusicPlayer();
              //jiggleJiggle.dateiAnspielen("audio/jiggleJiggle.mp3");
              highScore.add("" + playersPlaying.get(i).getUsername(),
                  Integer.parseInt(calendar.year + "" + String.format("%02d", calendar.month) + "" + String.format("%02d", calendar.day)), "global");
              highScoreGame.add("" + playersPlaying.get(i).getUsername(),
                  Integer.parseInt(calendar.year + "" + String.format("%02d", calendar.month) + "" + String.format("%02d", calendar.day)), "game");
            }
          }
          pause(500);
          // moves the player characters in the GUI
          positionUpdate();
        } else {
          if (playersPlaying.get(i).getPlayingField() != 0 && !playersPlaying.get(i).getGameOver()) {
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
    String[] diceMusic = {"audio/dice1.mp3", "audio/dice2.mp3", "audio/dice3.mp3", "audio/dice4.mp3"};
    Random random = new Random();
    for (int i = 0; i < time; i++) {
      if (!GameServer.isonline) {
        break; // stops this loop when the server closes
      } else if (rolledDice) {
        user.setIsActivelyRollingTheDice();
        dice = Dice.dice();
        music(diceMusic[random.nextInt(4)]);
        break;
      } else if (rolledSpecialDice) {
        user.setIsActivelyRollingTheDice();
        dice = Dice.specialDice();
        lobbyBroadcastToPlayer(user.getUsername() + " rolled a special dice and has " + user.getSpecialDiceLeft() + " dices left");
        sendToClient.send(user.getClienthandler(), CommandsToClient.DICEDICELEFT, Integer.toString(user.getSpecialDiceLeft()));
        music("audio/specialDice.mp3");
        break;
      } else if (i == time - 1) {
        user.setNotActivelyRollingTheDice();
        music(diceMusic[random.nextInt(4)]);
      } else if (time - i == 5000) {
        lobbyBroadcastToPlayer(user.getUsername() + " has 5 seconds left roll the dice");
        music("audio/wetClick.mp3");
      }
      pause(1);
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
        if (!userToRollDice.isPunished()) {
          if (number > 88) {
            userToRollDice.setPunished(true);
            cheated = true;
            lobbyBroadcastToPlayer(userToRollDice.getUsername() + " wanted to cheat to the finish.");
            changePosition(userToRollDice, 2 - userToRollDice.getPlayingField());
            rolledDice = true;
          } else {
            cheated = true;
            lobbyBroadcastToPlayer(userToRollDice.getUsername() + " has rich parents and moved to: " + number);
            changePosition(userToRollDice, number - userToRollDice.getPlayingField());
            rolledDice = true;
          }
        } else {
          lobbyBroadcastToPlayer(userToRollDice.getUsername() + " wanted to cheat again.");
        }
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
          music("audio/laughter.mp3");
          lobbyBroadcastToPlayer(user.getUsername() + " pushed back " + playersPlaying.get(i).getUsername() + " to " + currentPosition);
        }
      }
    }

    //puts the player to the new position
    if (newPosition > 88) { newPosition = 89; }
    user.setPlayingField(newPosition);
    positionUpdate();
    if (!cheated) {
      if (newPosition <= 88) {
        lobbyBroadcastToPlayer(user.getUsername() + " moved from: " + currentPosition + " to " + newPosition);
        checkField(user, newPosition);
      } else {
        lobbyBroadcastToPlayer(user.getUsername() + " moved from: " + currentPosition + " to Bachelorfeier");
        user.setIsPlaying(false);
      }
    } else {
      if (newPosition > 88) {
        checkField(user, newPosition);
        user.setIsPlaying(false);
      }
    }
    positionUpdate();
  }

  /**
   * Checks if the new position of the player to play is a special field
   *
   * @param user  User who is playing right now
   * @param field Users new position
   */
  public void checkField(User user, int field) {
    if (field == 3 || field == 55) {
      pause(1500);
      music("audio/correct.mp3");
      lobbyBroadcastToPlayer(user.getUsername() + ": ladder up");
      if (field == 3) { // 1 + 55 ladder up
        changePosition(user, 16 - field);
      } else {
        changePosition(user, 64 - field);
      }
    } else if (field == 21 || field == 28 || field == 52 || field == 57 || field == 80 || field == 87) {
      pause(1500);
      music("audio/wrong.mp3");
      lobbyBroadcastToPlayer(user.getUsername() + ": ladder down");
      if (field == 21) { // 20 - 87 ladder down
        changePosition(user, 18 - field);
      } else if (field == 28) {
        changePosition(user, 11 - field);
      } else if (field == 52) {
        changePosition(user, 47 - field);
      } else if (field == 57) {
        changePosition(user, 42 - field);
      } else if (field == 80) {
        changePosition(user, 60 - field);
      } else {
        changePosition(user, 72 - field);
      }
    } else if (field == 17 || field == 31 || field == 45 || field == 73) { // Action Cards
      pause(1500);
      String card = Cards.getCards();
      String[] arr = card.split(" ", 2);
      int positionToChange = Integer.parseInt(arr[0]);
      if (positionToChange > 0) {
        music("audio/correct.mp3");
      } else {
        music("audio/wrong.mp3");
      }
      String textCard = arr[1];
      lobbyBroadcastToPlayer("§" + user.getUsername() + " draws an action card:" + "§" + textCard + "§");
      pause(1500);
      changePosition(user, positionToChange);
    } else if (field == 7 || field == 13 || field == 22 || field == 38 || field == 49 || field == 65 || field == 83) { // Quiz
      quizOngoing = true;
      String quizQuestion = Quiz.quiz();
      String[] quiz = quizQuestion.split("Ç");
      setUserToAnswerQuiz(user);
      sendToClient.send(user.getClienthandler(), CommandsToClient.YOURQUIZ,"");
      setAnswer(quiz[1]);
      lobbyBroadcastToPlayer("§" + "Exam question for " + user.getUsername() + "." + "§" + quiz[0]);
      for (int i = 0; i < maxTimeToAnswerQuiz; i++) {
        if (quizAnsweredCorrect) {
          lobbyBroadcastToPlayer(user.getUsername() + "'s answer: " + quiz[1] + " is correct.");
          music("audio/correct.mp3");
          changePosition(userToAnswerQuiz, Integer.parseInt(quiz[2]));
          quizAnsweredCorrect = false;
          break;
        } else if (quizAnsweredWrong || i == maxTimeToAnswerQuiz - 1) {
          quizAnsweredWrong = false;
          lobbyBroadcastToPlayer(user.getUsername() + "'s answer is wrong.");
          if (user.isFirstTime()) {
            user.setFirstTime(false);
            music("audio/wrong.mp3");
            changePosition(userToAnswerQuiz, Integer.parseInt(quiz[2]) * -1);
          } else {
            gameOver(user);
            sendToClient.send(userToAnswerQuiz.getClienthandler(), CommandsToClient.MUSIC, "audio/nooh.mp3");
          }
          break;
        } else {
          if (maxTimeToAnswerQuiz - i == 5000) {
            lobbyBroadcastToPlayer(userToAnswerQuiz.getUsername() +" has 5 seconds left to answer");
            music("audio/wetClick.mp3");
          }
          pause(1);
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
    sendToClient.lobbyBroadcast(lobby.getUsersInLobby(),
        CommandsToClient.PRINTGUIGAMETRACKER, msg);
    try {
      Thread.sleep(50);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  /**
   * Thread.sleep so the GUI can update all the position and all the people are able to see every new position
   *
   * @param time Time in ms till next move.
   */
  public void pause (int time) {
    try {
      Thread.sleep(time);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  /**
   * Sends the updated position to the GUI
   */
  public void positionUpdate() {
    // moves the player characters in the GUI
    for (int j = 0; j < numPlayers; j++) {
      if (playersPlaying.get(j).getPlayingField() >= 0 && playersPlaying.get(j).getPlayingField() <= 89) {
        sendToClient.lobbyBroadcast(lobby.getUsersInLobby(), CommandsToClient.GUIMOVECHARACTER,
                playersPlaying.get(j).gameTokenNr + "--" + playersPlaying.get(j).getPlayingField());
      }
    }
  }

  /**
   * Receives the music to be played
   *
   * @param music Music to be played
   */
  public void music (String music) {
    sendToClient.lobbyBroadcast(lobby.getUsersInLobby(), CommandsToClient.MUSIC, music);
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
    //user.setPlayingField(0);
    user.setLobbyBeforeDisconnection(this.lobby.getLobbyName());
    user.setGameOver();
    resetPlayer(user);
    user.setIsPlaying(false);
    dsq++;
  }

  /**
   * Closes the game and sets the lobby's status to finished
   */
  public void closeGame() {
    music("audio/click.mp3");
    lobbyBroadcastToPlayer("All time leaders:§" + highScore.getTop10("global"));
    sendToClient.serverBroadcast(CommandsToClient.PRINTWINNERSGUI, highScore.getTop10("global"));
    if (highScoreGame.getTop10("game").length() > 0) {
      lobbyBroadcastToPlayer("Best students of this game:§" + highScoreGame.getTop10("game"));
      sendToClient.serverBroadcast(CommandsToClient.PRINTWINNERSGUI, highScoreGame.getTop10("game"));
      if (playersEndedGame == numPlayers) {
        lobbyBroadcastToPlayer("Congratulations! All of you have successfully graduated.");
      } else {
        lobbyBroadcastToPlayer("Congratulations! Most of you have successfully graduated.");
      }
    } else {
      // mis-used for a quickfix. if msg does not contain "graduated", the buttons in the menu scene will fail.
      // the buttons check if "graduated" has been sent to detect that nobody has finished (I know this is bad)
      lobbyBroadcastToPlayer("None of you has graduated.");

      sendToClient.serverBroadcast(CommandsToClient.PRINTWINNERSGUI, highScore.getTop10("global"));
    }
    for (int i = 0; i < numPlayers; i++) {
      User user = lobby.getUsersReady().get(i);
      lobby.removeUserFromLobby(user);
      user.setLobby(GameList.getLobbyList().get(0));
      resetPlayer(user);
      user.setIsPlaying(false);
      user.setNotGameOver();
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
    user.setPunished(false);
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

  /*public void getPlayersPlayingAndSpectators() {
    playersPlayingAndSpectators = lobby.getUsersInLobby();
  }

   */
}

package server;

import gameLogic.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(OrderAnnotation.class)
public class LiveTest {

  @Test
  @Order(1)
  public void connectClientHandlers() {
    addClientHandlers(6);
    assertAll("correctly connected",
        () -> assertEquals(6, getUsers().size(), "UserList"),
        () -> assertEquals(6, getClients().size(), "ActiveClientList")
    );
  }

  @Test
  @Order(2)
  public void createLobbies() {
    UserCreatesLobby(0, "yo");
    UserCreatesLobby(0, "ya");
    UserCreatesLobby(1, "ye");
    UserCreatesLobby(2, "yi");
    UserCreatesLobby(3, "yu");
    UserCreatesLobby(4, "yeet");
    assertEquals(6, getLobbies().size());
  }

  @Test
  @Order(3)
  public void joinLobbies() {
    UserJoinsLobby(0, 1);
    UserJoinsLobby(1, 1);
    UserJoinsLobby(2, 1);
    UserJoinsLobby(3, 1);
    UserJoinsLobby(4, 2);
    UserJoinsLobby(5, 2);
    assertAll("usersInLobby",
        () -> assertEquals(4, getLobby(1).getUsersInLobby().size()),
        () -> assertEquals(2, getLobby(2).getUsersInLobby().size()),
        () -> assertEquals(0, getLobby(3).getUsersInLobby().size()),
        () -> assertEquals(0, getLobby(4).getUsersInLobby().size())
    );
  }


  @Test
  @Order(4)
  public void getReady() {
    getLobby(1).readyToPlay(getClient(0));
    getLobby(1).readyToPlay(getClient(1));
    getLobby(1).readyToPlay(getClient(2));
    getLobby(1).readyToPlay(getClient(3));
    getLobby(2).readyToPlay(getClient(4));
    getLobby(2).readyToPlay(getClient(5));
    assertAll("UsersAreReady",
        () -> assertEquals(4, getLobby(1).getUsersReady().size()),
        () -> assertEquals(2, getLobby(2).getUsersReady().size())
    );
  }


  @Test
  @Order(5)
  public void startGame() {
    getLobby(1).startThread();
    getLobby(1).receiveFromProtocol.setMessage("start");
  }


  private void addClientHandlers(int amount) {
    ClientHandler client;
    for (int i = 0; i < amount; i++) {
      client = new ClientHandler(String.valueOf(i));
    }
  }

  private void UserCreatesLobby(int usernum, String lobbynum) {
    getClient(usernum).lobbyhelper.connectLobby(lobbynum);
  }

  private void UserJoinsLobby(int usernum, int lobbynum) {
    getClient(usernum).lobbyhelper.changeLobby(String.valueOf(lobbynum));
  }

  private User getUser(int num) {
    return GameList.getUserlist().get(num);
  }

  private ClientHandler getClient(int num) {
    return ServerManager.getActiveClientList().get(num);
  }

  private ArrayList<ClientHandler> getClients() {
    return ServerManager.getActiveClientList();
  }

  private Lobby getLobby(int num) {
    return GameList.getLobbyList().get(num);
  }

  private HashMap<Integer, Lobby> getLobbies() {
    return GameList.getLobbyList();
  }

  private HashMap<Integer, User> getUsers() {
    return GameList.getUserlist();
  }

  private String getUsername(int num) {
    return GameList.getUserlist().get(num).getUsername();
  }

}

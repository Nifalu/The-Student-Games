package server;

import gameLogic.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(OrderAnnotation.class)
public class NameTest {

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
  public void uppercaseName() {
    getClient(0).nameClass.changeNameTo(getUsername(0), "ABCD");
    assertEquals("ABCD",getUsername(0),"uppercase allowed");
  }

  @Test
  @Order(3)
  public void lowercaseName() {
    getClient(0).nameClass.changeNameTo(getUsername(0), "abcd");
    assertEquals("abcd",getUsername(0),"lowercase allowed");
  }

  @Test
  @Order(4)
  public void numberName() {
    getClient(0).nameClass.changeNameTo(getUsername(0), "1234");
    assertEquals("1234",getUsername(0),"numbers allowed");
  }

  @Test
  @Order(5)
  public void nameAlreadyExists() {
    getClient(0).nameClass.changeNameTo(getUsername(0), "firstname");
    getClient(1).nameClass.changeNameTo(getUsername(1), "secondname");
    getClient(1).nameClass.changeNameTo(getUsername(1), "firstname");
    assertEquals("firstname",getUsername(0),"keeps first name");
    assertEquals("firstname1",getUsername(1),"gets new first name");
  }

  @Test
  @Order(6)
  public void specialChars() {
    getClient(0).nameClass.changeNameTo(getUsername(0), "%£•@🥰");
    assertEquals("%£•@🥰",getUsername(0),"special chars allowed");
  }

  @Test
  @Order(7)
  public void emptyName() {
    getClient(0).nameClass.changeNameTo(getUsername(0), "");
    assertEquals("",getUsername(0),"empty name allowed");
  }

  @Test
  @Order(8)
  public void verylongname() {
    getClient(0).nameClass.changeNameTo(getUsername(0), String.valueOf(Double.MAX_VALUE));
    assertEquals(String.valueOf(Double.MAX_VALUE),getUsername(0),"very long name allowed");
  }










  private void addClientHandlers(int amount) {
    ClientHandler client;
    for (int i = 0; i < amount; i++) {
      client = new ClientHandler("Player_" + String.valueOf(i));
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

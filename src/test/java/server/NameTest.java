package server;

import gameLogic.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import utility.io.CommandsToClient;
import utility.io.ReceiveFromProtocol;
import utility.io.SendToClient;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(OrderAnnotation.class)
public class NameTest {

  /**
   * tests if the ClientHandler can connect by checking the userlist
   */
  @Test
  @Order(1)
  public void connectClientHandlers() {
    addClientHandlers(6);
    assertAll("correctly connected",
        () -> assertEquals(6, getUsers().size(), "UserList"),
        () -> assertEquals(6, getClients().size(), "ActiveClientList")
    );
  }

  /**
   * tests usernames: checks if uppercase works
   */
  @Test
  @Order(2)
  public void uppercaseName() {
    getClient(0).nameClass.changeNameTo(getUsername(0), "ABCD");
    assertEquals("ABCD",getUsername(0),"uppercase allowed");
  }

  /**
   * tests usernames: checks if lowercase works
   */
  @Test
  @Order(3)
  public void lowercaseName() {
    getClient(0).nameClass.changeNameTo(getUsername(0), "abcd");
    assertEquals("abcd",getUsername(0),"lowercase allowed");
  }

  /**
   * tests usernames: checks if numbers work
   */
  @Test
  @Order(4)
  public void numberName() {
    getClient(0).nameClass.changeNameTo(getUsername(0), "1234");
    assertEquals("1234",getUsername(0),"numbers allowed");
  }

  /**
   * tests usernames: checks if username is already taken, if yes it will check if everything's handled correctly
   */
  @Test
  @Order(5)
  public void nameAlreadyExists() {
    getClient(0).nameClass.changeNameTo(getUsername(0), "firstname");
    getClient(1).nameClass.changeNameTo(getUsername(1), "secondname");
    getClient(1).nameClass.changeNameTo(getUsername(1), "firstname");
    assertEquals("firstname",getUsername(0),"keeps first name");
    assertEquals("firstname1",getUsername(1),"gets new first name");

    getClient(1).nameClass.changeNameTo(getUsername(1), "firstname1");
    assertEquals("firstname1",getUsername(1),"keeps own name");
  }

  /**
   * tests usernames: checks if names with special characters work
   */
  @Test
  @Order(6)
  public void NameWithspecialChars() {
    getClient(0).nameClass.changeNameTo(getUsername(0), "%£•@🥰");
    assertEquals("%£•@🥰",getUsername(0),"special chars allowed");
  }

  /**
   * tests usernames: checks if an empty username works
   */
  @Test
  @Order(7)
  public void emptyName() {
    getClient(0).nameClass.changeNameTo(getUsername(0), "");
    assertEquals("",getUsername(0),"empty name allowed");
  }

  /**
   * tests usernames: checks if names with spaces work
   */
  @Test
  @Order(8)
  public void NameWithSpaces() {
    getClient(0).nameClass.changeNameTo(getUsername(0), "Hello World");
    assertEquals("Hello_World",getUsername(0));
  }

  /**
   * tests usernames: checks if long names work
   */
  @Test
  @Order(9)
  public void verylongname() {
    getClient(0).nameClass.changeNameTo(getUsername(0), String.valueOf(Double.MAX_VALUE));
    assertEquals(String.valueOf(Double.MAX_VALUE),getUsername(0),"very long name allowed");
  }

  /**
   * checks if the message is sent correctly
   */
  @Test
  @Order(10)
  public void sendMsg() {
    SendToClient send = new SendToClient();
    send.send(getClient(0), CommandsToClient.CHAT,"TestMessage");
  }

  /**
   * checks if an empty message is sent correctly
   */
  @Test
  @Order(11)
  public void sendEmptyMsg() {
    SendToClient send = new SendToClient();
    send.send(getClient(0), CommandsToClient.CHAT,"");
  }

  /**
   * checks if a message with spaces is sent correctly
   */
  @Test
  @Order(12)
  public void sendMsgWithSpaces() {
    SendToClient send = new SendToClient();
    send.send(getClient(0), CommandsToClient.CHAT,"Hello World");
  }

  /**
   * checks if special characters are sent correctly
   */
  @Test
  @Order(13)
  public void SendSpecialChars() {
    SendToClient send = new SendToClient();
    send.send(getClient(0), CommandsToClient.CHAT,"%£•@🥰");
  }

  /**
   * checks if the server can correctly receive uppercase messages
   */
  @Test
  @Order(14)
  public void ReceiveUpperCase() {
    ReceiveFromProtocol receiver = new ReceiveFromProtocol();
    receiver.setMessage("ABCD");
    assertEquals("ABCD", receiver.receive());
  }

  /**
   * checks if the server can correctly receive an empty message
   */
  @Test
  @Order(15)
  public void ReceiveNothing() {
    ReceiveFromProtocol receiver = new ReceiveFromProtocol();
    receiver.setMessage("");
    assertEquals("", receiver.receive());
  }

  /**
   * checks if the server can correctly receive special characters
   */
  @Test
  @Order(16)
  public void ReceiveSpecialChars() {
    ReceiveFromProtocol receiver = new ReceiveFromProtocol();
    receiver.setMessage("%£•@🥰");
    assertEquals("%£•@🥰", receiver.receive());
  }

  /**
   * checks if the server can correctly receive numbers
   */
  @Test
  @Order(17)
  public void ReceiveNumbers() {
    ReceiveFromProtocol receiver = new ReceiveFromProtocol();
    receiver.setMessage("1234");
    assertEquals("1234", receiver.receive());
  }

  /**
   * checks if the server can correctly receive large numbers
   */
  @Test
  @Order(18)
  public void ReceiveLargeNumbers() {
    ReceiveFromProtocol receiver = new ReceiveFromProtocol();
    receiver.setMessage(String.valueOf(Double.MAX_VALUE));
    assertEquals(String.valueOf(Double.MAX_VALUE), receiver.receive());
  }

  /**
   * checks if a lobby can be created correctly when containing uppercase letters
   */
  @Test
  @Order(19)
  public void createUppercaseLobby() {
    UserCreatesLobby(0, "TESTLobby");
    assertEquals("TESTLobby", getLobby(0).getLobbyName());
  }

  /**
   * checks if a lobby can be created correctly when containing lowercase letters
   */
  @Test
  @Order(20)
  public void createLowercaseLobby() {
    UserCreatesLobby(0, "testlobby");
    assertEquals("testlobby", getLobby(1).getLobbyName());
  }

  /**
   * checks if a lobby can be created correctly with an empty name
   */
  @Test
  @Order(21)
  public void createEmptyLobby() {
    UserCreatesLobby(0, "");
    assertEquals("", getLobby(2).getLobbyName());
  }

  /**
   * checks if a lobby can be created correctly when the name contains spaces
   */
  @Test
  @Order(22)
  public void createLobbyWithSpaces() {
    UserCreatesLobby(0, "Hello World");
    assertEquals("Hello_World", getLobby(3).getLobbyName());
  }

  /**
   * checks if lobbies are stored correctly
   */
  @Test
  @Order(23)
  public void LobbiesAreStoredCorrectly() {
    assertEquals(4, getLobbies().size());
  }

  /**
   * checks if clients are stored correctly
   */
  @Test
  @Order(24)
  public void ClientsAreStoredCorrectly() {
    assertEquals(6, getClients().size());
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

package gameLogic;

import java.io.*;

/**
 * The high score is calculated in form of the time taken to reach the bachelor's degree.
 */
public class HighScore {

  /**
   * head node
   */
  Node first = null;

  /**
   * Adds the players name together with his graduations date in a sorted list.
   *
   * @param name  Player to be added to the list
   * @param score The date of graduation
   */
  void add(String name, int score, String game) {
    Node current = first;
    if (game.equals("global") && current == null) {
      loadHighscore();
    }
    current = first;
    Node n = new Node(name, score);
    if (current == null || first.score > n.score) {
      n.next = first;
      first = n;
    } else {
      while (current.next != null && current.next.score <= n.score) {
        current = current.next;
      }
      n.next = current.next;
      current.next = n;
    }
    if (game.equals("global")) { saveHighscore(); }
  }

  /**
   * @return The best 10 players of the game sorted by date of graduation with their dates
   */
  public String getTop10(String game) {
    if (first == null && game.equals("global")) { loadHighscore(); }
    Node n = first;
    int count = 0;
    String top10 = "";
    while (n != null && count < 10) {
      String date = String.valueOf(n.score);
      int year = Integer.parseInt(date.substring(0, 4));
      int month = Integer.parseInt(date.substring(4, 6));
      int day = Integer.parseInt(date.substring(6, 8));
      top10 += (count + 1 + ". Platz: " + n.name + " Abschluss: " + day + "." + month + "." + year + "§");
      n = n.next;
      count++;
    }
    return top10;
  }

  /**
   * loads in the highscore file
   */
  public void loadHighscore() {
      BufferedReader br;
      String line;
      Node current = first;
      try {
        br = new BufferedReader(new FileReader("gamefiles/utility/highscore.txt"));
        line = br.readLine();
        br.close();
      } catch (IOException e) {
        //line = "";
        return;
      }
      if (line != null) {
        String[] highscore = line.split(",");
        for (int i = highscore.length - 1; i != -1; i--) {
          String[] nameScore = highscore[i].split("!");
          Node n = new Node(nameScore[0], Integer.parseInt(nameScore[1]));
          if (current == null) {
            n.next = first;
            first = n;
          } else {
            while (current.next != null && current.next.score <= n.score) {
              current = current.next;
            }
            n.next = current.next;
            current.next = n;
          }
        }
        //lblHighscore.setText(line);
      }
  }

  /**
   * saves the highscore to an external .txt file
   */
  public void saveHighscore(){
    BufferedWriter bw;
    String save = "";
    Node current = first;
    while (current != null) {
      save += current.name + "!" + current.score + ",";
      current = current.next;
    }
    try {
      bw = new BufferedWriter(new FileWriter("gamefiles/utility/highscore.txt", false));
      bw.write(save);
      bw.flush();
      bw.close();
    } catch (IOException e) {
      System.out.println("Error in saveHighscore");
    }
  }
}

package GameLogic;

/**
 * The high score is calculated in form of the time taken to reach the bachelor's degree.
 */

class Node {
    Node node;
    Node next;
    int score;
    String name;

    public Node(String name, int score) {
        this.name = name;
        this.score = score;
        this.next = null;
    }
}

public class HighScore {

    Node first = null;

    //adds the players name together with his graduations date in a sorted list.
    void add(String name, int score) {
        Node current = first;
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
    }

    //returns the best 10 players of the game sorted by date of graduation.
    String getTop10() {
        Node n = first;
        int count = 0;
        String top10 = "";
        while (n != null && count < 10) {
            String date = String.valueOf(n.score);
            int year = Integer.parseInt(date.substring(0, 4));
            int month = Integer.parseInt(date.substring(4, 6));
            int day = Integer.parseInt(date.substring(6, 8));
            top10 += (count + 1 + ". Platz: " + n.name + " Abschluss: " + day + "." + month + "." + year + " ");
            n = n.next;
            count++;
        }
        return top10;
    }
}

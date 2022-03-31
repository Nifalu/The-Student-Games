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

    void add(String name, int score) {
        Node current = first;
        Node n = new Node(name, score);
        if (current == null || first.score < n.score) {
            n.next = first;
            first = n;
        } else {
            while (current.next != null && current.next.score > n.score) {
                current = current.next;
            }
            n.next = current.next;
            current.next = n;
        }
    }

    void printList() {
        Node n = first;
        int count = 0;
        while (n != null && count < 10) {
            System.out.println(count + 1 + ". Platz: " + n.name + " " + n.score);
            n = n.next;
            count++;
        }
    }
}


//LinkedList Top 10?
    /*
public class HighScore {
    public String name;
    public int score;
    private int[] top10 = new int[10];

    Map<Integer, String> Leaderboard = new HashMap<>();

    public void highScore(String name, int score) {
        this.name = name;
        this.score = score;
        Leaderboard.put(score, name);
        add(score);
    }

    public void add(int score) {
        if (top10[9] < score) {
            for (int i = 8; i >= 0; i--) {
                if (top10[i] < score) {
                    top10[i+1] = top10[i];
                } else {
                    top10[i] = score;
                }
            }
        }
    }

    public void print() {
        for (int i = 0; i < 10; i++) {
            System.out.println(top10[i] + Leaderboard.get(top10[i]));
        }
    }
}
*/


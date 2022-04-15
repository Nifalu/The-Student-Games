package GameLogic;

class Node {

    Node next;
    int score;
    String name;

    public Node(String name, int score) {
        this.name = name;
        this.score = score;
        this.next = null;
    }
}

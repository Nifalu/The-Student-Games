package gameLogic;

/**
 * Node for the high score
 */
class Node {

    /**
     * the next node
     */
    Node next;

    /**
     * the score
     */
    int score;

    /**
     * the name
     */
    String name;

    /**
     * Creates a node with the user's name and its score.
     * @param name Name of the user
     * @param score Date of graduation
     */
    public Node(String name, int score) {
        this.name = name;
        this.score = score;
        this.next = null;
    }
}

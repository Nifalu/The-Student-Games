package gameLogic;

import java.util.Random;

/**
 * The person whose turn it is can roll the dice. The result is then added to the current position.
 */
public class Dice {

    /**
     * 6 sided dice
     * @return random number between 1-6
     */
    public static int dice() {
        Random random = new Random();
        return random.nextInt(6) + 1;
    }

    /**
     * 4 sided dice
     * @return random number between 1-4
     */
    public static int specialDice() {
        Random random = new Random();
        return random.nextInt(4) + 1;
    }
}


package GameLogic;

import java.util.Random;

/**
 * The person whose turn it is can roll the dice. The result is then added to the current position.
 */
public class Dice {

    public static int dice() {
        Random random = new Random();
        int randomInt = random.nextInt(6) + 1;
        return randomInt;
    }

    public static int specialDice() {
        Random random = new Random();
        int randomInt = random.nextInt(4) + 1;
        return randomInt;
    }
}

package GameLogic;

import java.util.Random;

public class Quiz {
    final static int n = 100;

    public static int quiz() {
        String[][] questionList = new String[n][2];
        questionList[0][0] = "Was ist der Erwartungswert eines Wurfels mit sechs Flächen?" +
                "\nA: 2.5\nB: 3\nC: 4\nD: 4.5";
        questionList[0][1] = "C";
        questionList[0][2] = "4";
//        questionList[1][0] = ;
//        questionList[1][1] = ;
//        questionList[2][0] = ;
//        questionList[2][1] = ;
//        questionList[3][0] = ;
//        questionList[3][1] = ;
//        questionList[3][0] = ;
//        questionList[4][1] = ;
//        questionList[5][0] = ;
//        questionList[5][1] = ;

        Random random = new Random();
        int randomInt = random.nextInt(n);
        /*
        if (answer.equals(questionList[randomInt][1])) {
            System.out.println("Correct!");
            return Integer.parseInt(questionList[randomInt][2]);
        } else {
            System.out.println("Incorrect, the answer was " + questionList[randomInt][1]);
        }
         */
        return 0;
    }
}

package GameLogic;

import java.util.Random;

public class Quiz {
    final static int n = 10;

    public static String quiz() {
        String[][] questionList = new String[n][3];
        questionList[0][0] = "Was ist der Erwartungswert eines Würfels mit sechs Flächen? A: 2.5 B: 3.5 C: 4 D: 4.5";
        questionList[0][1] = "B";
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
        String quiz = questionList[0][0] + "§" + questionList[0][1] + "§" + questionList[0][2];

        return quiz;
    }
}

package GameLogic;

import java.util.Random;

public class Quiz {
    final static int n = 3;

    public static String quiz() {
        String[][] questionList = new String[n][3];
        questionList[0][0] = "Was ist der Erwartungswert eines Würfels mit sechs Flächen? " +
                "A: 2.5 B: 3.5 C: 4 D: 4.5";
        questionList[0][1] = "B";
        questionList[0][2] = "7";
        questionList[1][0] = "Wann wurde die Universität Basel gegründet? " +
                "A: 1420 B: 1450 C: 1460 D: 1470";
        questionList[1][1] = "C";
        questionList[1][2] = "5";
        questionList[2][0] = "Welches hat die schlechtere \"Worst Case Luafzeit\"?: " +
                "A: Quicksort B: MergeSort";
        questionList[2][1] = "A";
        questionList[2][2] = "3";
//        questionList[3][0] = ;
//        questionList[3][1] = ;
//        questionList[3][2] = ;
//        questionList[4][0] = ;


        Random random = new Random();
        int randomInt = random.nextInt(n);
        String quiz = questionList[randomInt][0] + "§" + questionList[randomInt][1] + "§" + questionList[randomInt][2];

        return quiz;
    }
}

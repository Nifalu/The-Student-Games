package gameLogic;

import java.util.Random;

/**
 * Quiz questions with its answer and the number of fields to be moved forward or backward.
 */
public class Quiz {
    final static int n = 4;

    /**
     * List of all quiz questions.
     * @return A string with the question, answer and number of fields to be moved.
     */
    public static String quiz() {
        String[][] questionList = new String[n][3];
        questionList[0][0] = "Was ist der Erwartungswert eines Würfels mit sechs Flächen? " +
                "A: 3, B: 3.5, C: 4, D: 4.5";
        questionList[0][1] = "B";
        questionList[0][2] = "7";
        questionList[1][0] = "Wann wurde die Universität Basel gegründet? " +
                "A: 1420, B: 1450, C: 1460, D: 1470";
        questionList[1][1] = "C";
        questionList[1][2] = "5";
        questionList[2][0] = "Welches hat die schlechtere \"Worst Case Luafzeit\"?: " +
                "A: Quicksort B: MergeSort";
        questionList[2][1] = "A";
        questionList[2][2] = "3";
        questionList[3][0] = "Mit welchem Programm wurde das MockUp designed?" +
                "A: Excel B: Photoshop C: Paint D: GIMP";
        questionList[3][1] = "A";
        questionList[3][2] = "4";
//        questionList[4][0] = ;


        Random random = new Random();
        int randomInt = random.nextInt(n);

        return questionList[randomInt][0] + "§" + questionList[randomInt][1] + "§" + questionList[randomInt][2];
    }
}

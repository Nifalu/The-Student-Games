package gameLogic;

import java.util.Random;

/**
 * Quiz questions with its answer and the number of fields to be moved forward or backward.
 */
public class Quiz {

  /**
   * amount of answers to a quiz question
   */
  final static int n = 11;

  /**
   * List of all quiz questions.
   *
   * @return A string with the question, answer and number of fields to be moved.
   */
  public static String quiz() {
    String[][] questionList = new String[n][3];
    questionList[0][0] = "Was ist der Erwartungswert eines Würfels mit sechs Flächen?§A: 3, B: 3.5, C: 4, D: 4.5";
    questionList[0][1] = "B";
    questionList[0][2] = "7";
    questionList[1][0] = "Wann wurde die Universität Basel gegründet?§A: 1420, B: 1450, C: 1460, D: 1470";
    questionList[1][1] = "C";
    questionList[1][2] = "5";
    questionList[2][0] = "Welches hat die schlechtere \"Worst Case Laufzeit\"?:§A: Quicksort, B: MergeSort, C: FastSort, D: CombineSort";
    questionList[2][1] = "A";
    questionList[2][2] = "5";
    questionList[3][0] = "Mit welchem Programm wurde das MockUp designed?§A: Excel, B: Photoshop, C: Paint, D: GIMP";
    questionList[3][1] = "A";
    questionList[3][2] = "5";
    questionList[4][0] = "Wie heisst der Professor in Statistik?§A: Černi, B: Cerný, C: Černy, D: Černý";
    questionList[4][1] = "D";
    questionList[4][2] = "5";
    questionList[5][0] = "Wie heisst die Rektorin der Uni Basel?§A: Schenker-Wicki, B: Gäbler, C: Loprieno, D: Frey";
    questionList[5][1] = "A";
    questionList[5][2] = "5";
    questionList[6][0] = "Wie hoch sind die jährlichen Studiengebühren?§A: 800, B: 850, C: 1600, D: 1700";
    questionList[6][1] = "D";
    questionList[6][2] = "5";
    questionList[7][0] = "Aus welchem Kanton kommen die meisten Studierenden? (HS21)§A: Aargau, B: BL, C: BS, D: Neuenburg";
    questionList[7][1] = "B";
    questionList[7][2] = "5";
    questionList[8][0] = "Wie viele Studierende und Doktorierende" + "§" + "hat es an der Theologischen Fakultät? (HS21)§A: 98, B: 102, C: 108, D: 110";
    questionList[8][1] = "B";
    questionList[8][2] = "5";
    questionList[9][0] = "Was kann man an der Uni Basel nicht studieren?§A: Architecktur, B: Hispanistik, C: Kulturanthropologie, D: Soziologie";
    questionList[9][1] = "A";
    questionList[9][2] = "5";
    questionList[10][0] = "Wann beginnen die Vorlesungen im HS22?§A: 18.09.2022, B: 19.09.2022, C: 20.09.2022, D: 21.09.2022";
    questionList[10][1] = "B";
    questionList[10][2] = "5";

    Random random = new Random();
    int randomInt = random.nextInt(n);

    return questionList[randomInt][0] + "Ç" + questionList[randomInt][1] + "Ç" + questionList[randomInt][2];
  }
}

package GUI;

import javafx.application.Application;

/**
 *  launching the GUI class directly may not work
 *  this class is used to launch it instead
 */
public class Main {

    public static void main(String[] args) {

            Application.launch(GUI.class, args);
    }


}

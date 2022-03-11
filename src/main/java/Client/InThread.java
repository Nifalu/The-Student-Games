package Client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;


/**
 * This Thread receives and processes inputs from the server
 */
public class InThread implements Runnable {

  DataInputStream in;
  DataOutputStream out;

  public InThread(DataInputStream in, DataOutputStream out) {
    this.in = in;
    this.out = out;
  }

  @Override
  public void run() {

    String s;
    // String[] input;
    try {
      while (!(s = in.readUTF()).equals("QUIT")) {

        // Reads the incoming String and splits it into two parts
        // ( "instruction" and "value" ) which are saved in an array.

        // input = s.split("-",2); // Splits the String (limit - 1) times at the first "-"

        System.out.println(s);
      }

    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
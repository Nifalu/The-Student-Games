package utility.IO;

/**
 * Instances of this class wait until a message is set from outside this class. Then returns it.
 * This class will BLOCK until a message is received! Consider placing it in a Thread if it should
 * wait for incoming messages in the background
 */
public class ReceiveFromProtocol {
  String receivedMessage;


  /**
   * Waits until a message is Received then returns it as String.
   * @return String
   */
  public synchronized String receive() {
    String tmp_msg;
    while (receivedMessage == null) {
      try {
        wait(0, 100000); // waits 0.1 milliseconds
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
    tmp_msg = receivedMessage;
    receivedMessage = null;
    return tmp_msg;
  }

  /**
   * Use this method in the protocol (SendToServer class) to forward a message from the client to this instance.
   * @param msg String
   */
  public synchronized void setMessage(String msg) {
    this.receivedMessage = msg;
  }

}

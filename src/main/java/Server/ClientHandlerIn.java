package Server;

import java.io.BufferedReader;
import java.io.IOException;

public class ClientHandlerIn implements Runnable {
    BufferedReader in;
    ClientHandler clientHandler;
    private volatile boolean stop = false;

    ClientHandlerIn(ClientHandler clientHandler, BufferedReader in) {
        this.clientHandler = clientHandler;
        this.in = in;
    }

    @Override
    public void run() {
        // processes traffic with serverProtocol
        String msg;
        while (!stop) {
            msg = receive();
            if (msg == null) {
                break;
            }
            clientHandler.send(ServerProtocol.get(clientHandler.game, clientHandler.user, msg));
        }
    }

    /**
     * "Receive a String from the Client"
     * A StringBuilder appends every incoming byte to a String until a certain break-character is found.
     * Then removes the break-character and returns the String.
     */
    public String receive() {
        String line = "";
        try {
            while(!stop) {
                if (in.ready()) {
                    line = in.readLine();
                    break;
                }
                Thread.sleep(0,200000);
            }
            return line;
            // if connection fails
        } catch (IOException e) {

            if (clientHandler.user != null) {
                // error message when user is known
                System.out.println("cannot receive from " + clientHandler.user.getUsername());
            } else {
                // error message if user is unknown
                System.out.println("cannot receive from user");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }
}

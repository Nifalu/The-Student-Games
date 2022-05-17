package utility.io;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Random;
import java.util.UUID;

/**
 * This class stores and loads uuid's from and in a file to allow clients to reconnect to a server and be remembered
 * by the server.
 */
public class Uuid {

  /**
   * uuid logger
   */
  static Logger logger = LogManager.getLogger(Uuid.class);


  /**
   * Writes the given uuid in the uuid file. This uuid will be used to connect to the server when starting the client.
   * If it is overwritten by a different uuid, the old one will be lost forever!
   * @param uuid uuid to be saved in the file
   */
  private static void rememberConnection(String uuid) {
    try {
      BufferedWriter bw = new BufferedWriter(new FileWriter("gamefiles/utility/uuid.txt", false));
      bw.write(uuid);
      bw.flush();
      bw.close();

    } catch (IOException e) {
      logger.warn("Could not save uuid. no reconnect possible!");
    }
  }

  /**
   * Tries to read the UUID from the uuid file. If successful, it returns the uuid. If not (if for example the file
   * is empty), a new uuid will be generated, saved in the file and returned.
   * @return uuid as String
   */
  public static String getUUID() {

    try {
      BufferedReader br = new BufferedReader(new FileReader("gamefiles/utility/uuid.txt", StandardCharsets.UTF_8));
      String line;
      if ((line = br.readLine()) != null) {
        return line;
      }

    } catch (IOException e) {
      // if the connections.txt does not exist or throws an error when reading
      logger.warn("Could not get uuid -> creating new uuid file");
    }

    // create new ID for this connection and store it.
    String uuid = generateType1UUID().toString();
    rememberConnection(uuid);
    return uuid;
  }

  /**
   * generates a type1 uuid
   * @return uuid
   */
  public static UUID generateType1UUID() {
    return new UUID(get64MostSignificantBitsForVersion1(), get64LeastSignificantBitsForVersion1());
  }

  /**
   * gets the 64 least significant bits for the uuid version 1
   * @return the 64 least significant bits as long
   */
  private static long get64LeastSignificantBitsForVersion1() {
    Random random = new Random();
    long random63BitLong = random.nextLong() & 0x3FFFFFFFFFFFFFFFL;
    long variant3BitFlag = 0x8000000000000000L;
    return random63BitLong + variant3BitFlag;
  }

  /**
   * gets the 64 most significant bits for the uuid version 1
   * @return the 64 most significant bits as long
   */
  private static long get64MostSignificantBitsForVersion1() {
    LocalDateTime start = LocalDateTime.of(1582, 10, 15, 0, 0, 0);
    Duration duration = Duration.between(start, LocalDateTime.now());
    long seconds = duration.getSeconds();
    long nanos = duration.getNano();
    long timeForUuidIn100Nanos = seconds * 10000000 + nanos * 100;
    long least12SignificantBitOfTime = (timeForUuidIn100Nanos & 0x000000000000FFFFL) >> 4;
    long version = 1 << 12;
    return
        (timeForUuidIn100Nanos & 0xFFFFFFFFFFFF0000L) + version + least12SignificantBitOfTime;
  }



}

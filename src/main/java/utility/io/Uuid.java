package utility.io;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Random;
import java.util.UUID;

public class Uuid {

  static Logger logger = LogManager.getLogger(Uuid.class);


  private static void rememberConnection(String uuid) {
    try {
      BufferedWriter bw = new BufferedWriter(new FileWriter("Properties/uuid.txt", true));
      bw.write(uuid);
      bw.flush();
      bw.close();
    } catch (IOException e) {
      logger.warn("Could not save uuid. no reconnect possible!");
    }
  }

  public static String getUUID() {

    try {
      BufferedReader br = new BufferedReader(new FileReader("Properties/uuid.txt", StandardCharsets.UTF_8));
      String line;
      if ((line = br.readLine()) != null) {
        return line;
      }

    } catch (IOException e) {
      // if the connections.txt does not exist or throws an error when reading
      logger.warn("Could not read from connections.txt -> creating new connections.txt file");
    }

    // create new ID for this connection and store it.
    String uuid = generateType1UUID().toString();
    rememberConnection(uuid);
    return uuid;
  }

  public static UUID generateType1UUID() {
    return new UUID(get64MostSignificantBitsForVersion1(), get64LeastSignificantBitsForVersion1());
  }

  private static long get64LeastSignificantBitsForVersion1() {
    Random random = new Random();
    long random63BitLong = random.nextLong() & 0x3FFFFFFFFFFFFFFFL;
    long variant3BitFlag = 0x8000000000000000L;
    return random63BitLong + variant3BitFlag;
  }

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

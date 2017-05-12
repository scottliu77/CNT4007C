public class ProjectVariables {
   public static boolean VERBOSE = false;
   public static int PASS = 0;
   public static int CORRUPT = 1;
   public static int DROP = 2;
   public static int ACK0 = PASS;
   public static int ACK1 = CORRUPT;
   public static int ACK2 = DROP;

   public static String getAckString(int ack) {
      switch (ack) {
            case 0:  return "PASS";
            case 1:  return "CORRUPT";
            case 2:  return "DROP";
            default:
                     return "DROP";
        }
   }

   public static String createAck(String message, int sequenceNumber) {
      if (message.equals("-1")) {
         return message;
      }

      String ack = "";
      ack += sequenceNumber + " ";
      ack += getCheckSum(message) + " ";
      ack += message;
      return ack;
   }

   public static String createPacket(String message, int sequenceNumber, int id) {
      if (message.equals("-1")) {
         return message;
      }

      String packet = "";
      packet += sequenceNumber + " ";
      packet += id + " ";
      packet += getCheckSum(message) + " ";
      packet += message;
      return packet;
   }

   public static int getCheckSum(String packet) {
      int checkSum = 0;
      for (int i = 0; i < packet.length(); i++) {
         char letter = packet.charAt(i);
         checkSum += letter;
      }
      return checkSum;
   }

   public static String[] splitString(String line, String splitOn) {
      if (splitOn.equals(".")) {
         splitOn = "\\.";
      }
      return line.split(splitOn);
   }

   // public static String stringify(String[] input) {
   //    String output = "";
   //    for (String line : input) {
   //       output += line;
   //    }
   //    return output;
   // }
}

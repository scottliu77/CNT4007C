import java.net.InetAddress;
import java.net.UnknownHostException;
import java.lang.NumberFormatException;
import java.io.*;

public class sender {
   public static void main(String[] args) {
      System.out.println("Starting Sender");
      try {
         SenderHelper sender = new SenderHelper(
         InetAddress.getByName(args[0]),
         Integer.parseInt(args[1]),
         args[2]);
         sender.start();
      } catch (UnknownHostException e) {
         System.out.println("Please use a valid host address");
         System.out.println(e.getMessage());
      } catch (NumberFormatException e) {
         System.out.println("Please use a valid port number");
         e.printStackTrace();
      } catch(Exception e) {
         e.printStackTrace();
      }
   }
}

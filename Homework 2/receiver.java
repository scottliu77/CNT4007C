import java.net.InetAddress;
import java.net.UnknownHostException;
import java.lang.NumberFormatException;
import java.io.*;

public class receiver {
   public static void main(String[] args) {
      System.out.println("Starting Receiver");
      try {
         ReceiverHelper receiver = new ReceiverHelper(
         InetAddress.getByName(args[0]),
         Integer.parseInt(args[1]));
         receiver.start();
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

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.lang.NumberFormatException;
import java.io.*;

public class network {
   public static void main(String[] args) {
      System.out.println("Starting Network");
      try {
         NetworkHelper network = new NetworkHelper(
         Integer.parseInt(args[0]));
         network.start();
      } catch (NumberFormatException e) {
         System.out.println("Please use a valid port number");
         e.printStackTrace();
      } catch(Exception e) {
         e.printStackTrace();
      }
   }
}

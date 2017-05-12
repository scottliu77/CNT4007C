import java.net.InetAddress;
import java.net.UnknownHostException;
import java.lang.NumberFormatException;

public class client {
   public static void main(String[] args) {
      try {
         ClientHelper client = new ClientHelper(
         InetAddress.getByName(args[0]),
         Integer.parseInt(args[1]));
         client.start();
      } catch (UnknownHostException e) {
         System.out.println("Please use a valid host address");
         System.out.println(e.getMessage());
      } catch (NumberFormatException e) {
         System.out.println("Please use a valid port number");
         e.printStackTrace();
      }
   }
}

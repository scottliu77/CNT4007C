import java.lang.NumberFormatException;

public class server {
   public static void main(String[] args) {
      try {
         ServerHelper server = new ServerHelper(Integer.parseInt(args[0]));
         server.start();
      } catch (NumberFormatException e) {
         System.out.println("Please use a valid port number");
         e.printStackTrace();
      }
   }
}

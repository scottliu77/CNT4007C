//I hate that the filename is required to be lowercase...

public class linkstate {
   public static void main(String[] args) {
      String inputFile = args[0];
      LinkstateHelper helper = new LinkstateHelper(inputFile);
      helper.start();
   }
}

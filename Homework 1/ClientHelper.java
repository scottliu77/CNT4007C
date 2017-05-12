import java.net.*;
import java.io.*;

public class ClientHelper {
   private final String genericError = "There was an error: ";
   private Socket serverSocket;
   private InetAddress serverAddress;
   private int portNumber;
   private SocketAddress serverSocketAddress;
   private Boolean continuous;
   private Boolean connected;

   //extremelyVerbose used for debugging
   private Boolean extremelyVerbose;

   //projectVerbose prints what the project prompt calls for
   private Boolean projectVerbose;

   //Starts everything in motion
   private void runClient() {
      initialize();
      if (serverSocket.isConnected() && !connected) {
         if (projectVerbose) { System.out.println("Hello!"); }
         connected = true;
      }
      String userInput = waitForUserInput();
      if (extremelyVerbose) { System.out.println("Get: " + userInput + ". "); }
      sendToServer(userInput);
      String response = getResponseDescription(waitForServerResponse());
      if (projectVerbose) { System.out.println("Response: " + response + "."); }
      if (response.equals("Exit")) {
         continuous = false;
      }
      end();
   }

   private String getResponseDescription(String response) {
      String description = "";
      switch (response) {
         case "-1":
            description = "Incorrect operation command";
            break;
         case "-2":
            description = "Number of inputs is less than two";
            break;
         case "-3":
            description = "Number of inputs is more than four";
            break;
         case "-4":
            description = "One or more of the inputs contain(s) non­number(s)";
            break;
         case "-5":
            description = "Exit";
            continuous = false;
            break;
         default:
            description = response;
            break;
      }
      return description;
   }

   private String waitForServerResponse() {
      if (extremelyVerbose) { System.out.println("Waiting for server response."); }
      String response = "";
      try {
         BufferedReader inFromServer = new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));
         response = inFromServer.readLine();
      } catch (IOException e) {
         System.out.println(genericError + e.getMessage());
      }
      return response;
   }

   private String waitForUserInput() {
      if (extremelyVerbose) { System.out.println("Waiting for user input."); }
      if (projectVerbose) { System.out.println("Please enter your prompt."); }
      String response = "";
      try {
         BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
         response = input.readLine();
      } catch (IOException e) {
         System.out.println(genericError + e.getMessage());
      }
      return response;
   }

   private void sendToServer(String userInput) {
      if (extremelyVerbose) { System.out.println("Sending message to server."); }
      try {
         DataOutputStream outputStream = new DataOutputStream(serverSocket.getOutputStream());
         outputStream.writeBytes(userInput + "\n");
      } catch (IOException e) {
         System.out.println(genericError + e.getMessage());
      }
   }

   //Creates socket
   private void initialize() {
      try {
         if (extremelyVerbose) { System.out.println("Opening Socket."); }
         serverSocket = new Socket(serverAddress, portNumber);
         serverSocketAddress = serverSocket.getRemoteSocketAddress();
         if (extremelyVerbose) { System.out.println("Get: Connection from: " + serverSocketAddress + "."); }
      } catch (IOException e) {
         System.out.println(genericError + e.getMessage());
      }
   }

   //Ends program
   private void end() {
      try {
         if (extremelyVerbose) { System.out.println("Closing Socket."); }
         serverSocket.close();
         if (extremelyVerbose) { System.out.println("Socket closed"); }
      } catch (IOException e) {
         System.out.println(genericError + e.getMessage());
      }
   }

   //Constructor
   public ClientHelper(InetAddress serverURL, int portNumber) {
      extremelyVerbose = false;
      projectVerbose = true;
      this.portNumber = portNumber;
      serverSocket = null;
      serverAddress = serverURL;
      continuous = true;
      connected = false;
   }

   //I wanted it to be client.start
   //Deal with it (•_•) / ( •_•)>⌐■-■ / (⌐■_■)
   public void start() {
      while(continuous) {
         runClient();
      }
   }
}


// try {
//    if (extremelyVerbose) { System.out.println("Getting localHost address."); }
//    localHost = InetAddress.getLocalHost();
//    if (extremelyVerbose) { System.out.println("localHost address: " + localHost); }
// } catch (UnknownHostException e) {
//    System.out.println(genericError + e.getMessage());
// }

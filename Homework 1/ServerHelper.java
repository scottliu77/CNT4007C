import java.net.*;
import java.io.*;

public class ServerHelper {
   private final String genericError = "There was an error: ";
   private ServerSocket serverSocket;
   private Socket clientConnectionSocket;
   private int portNumber;
   private Boolean continuous;
   private Boolean online;
   private Boolean connected;

   //extremelyVerbose used for debugging
   private Boolean extremelyVerbose;
   //projectVerbose prints what the project prompt calls for
   private Boolean projectVerbose;

   //Starts everything in motion
   private void runServer() {
      initialize();
      acceptConnection();
      String query = waitForClient();
      if (projectVerbose) { System.out.print("Get: " + query.toLowerCase() + ". "); }
      String response = performCommand(query);
      if (projectVerbose) { System.out.println("Response: " + response + "."); }
      respondToClient(response);
   }

   //Respond to client with correct query
   private void respondToClient(String response) {
      try {
         DataOutputStream outputStream = new DataOutputStream(clientConnectionSocket.getOutputStream());
         outputStream.writeBytes(response + "\n");
      } catch (IOException e) {
         System.out.println(genericError + e.getMessage());
      }
   }

   //Perform command client has sent
   private String performCommand(String query) {
      String response = query + " SERVER STUFF";
      String[] inputs = query.split(" ");
      int answer = -1000;
      //TODO: lowercase
      inputs[0] = inputs[0].toLowerCase();
      switch (inputs[0]) {
         case "add":
            answer = add(inputs);
            break;
         case "subtract":
            answer = subtract(inputs);
            break;
         case "multiply":
            answer = multiply(inputs);
            break;
         case "bye":
            answer = -5;
            connected = false;
            break;
         case "terminate":
            answer = -5;
            continuous = false;
            break;
         default:
            answer = -1;
            break;
      }
      response = convertToString(answer);
      return response;
   }

   private String convertToString(int answer) {
      String response = "";
      try {
         response =  Integer.toString(answer);
      } catch (NumberFormatException e) {
         System.out.println(genericError + "could not convert answer to string");
      }
      return response;
   }

   private int convertToInt(String input) {
      int response = 0;
      try {
         response = Integer.parseInt(input);
      } catch (NumberFormatException e) {
         System.out.println(genericError + "could not convert answer to string");
      }
      return response;
   }

   private int correctInputs(String[] inputs) {
      int answer = 0;
      if (inputs.length < 3) {
         answer = -2;
         return answer;
      } else if (inputs.length > 5) {
         answer = -3;
         return answer;
      }
      return answer;
   }

   private int add(String[] inputs) {
      int answer = correctInputs(inputs);
      if (answer != 0) { return answer; }

      try {
         //If here, answer = 0
         if (extremelyVerbose) { System.out.println("Adding."); }
         for (int i = 1; i < inputs.length; i++) {
            answer += Integer.parseInt(inputs[i]);
         }
      } catch (NumberFormatException e) {
         return -4;
      }

      return answer;
   }

   private int subtract(String[] inputs) {
      int answer = correctInputs(inputs);
      if (answer != 0) { return answer; }

      try {
         //If here, answer = 0
         if (extremelyVerbose) { System.out.println("Subtracting."); }
         answer = convertToInt(inputs[1]);
         for (int i = 2; i < inputs.length; i++) {
            answer -= Integer.parseInt(inputs[i]);
         }
      } catch (NumberFormatException e) {
         return -4;
      }

      return answer;
   }

   private int multiply(String[] inputs) {
      int answer = correctInputs(inputs);
      if (answer != 0) { return answer; }

      try {
         //If here, answer = 0
         if (extremelyVerbose) { System.out.println("Multiplying."); }
         answer = convertToInt(inputs[1]);
         for (int i = 2; i < inputs.length; i++) {
            answer *= Integer.parseInt(inputs[i]);
         }
      } catch (NumberFormatException e) {
         return -4;
      }

      return answer;
   }

   //Wait for client to send message
   private String waitForClient() {
      String response = "";
      try {
         BufferedReader inFromClient = new BufferedReader(new InputStreamReader(clientConnectionSocket.getInputStream()));
         response = inFromClient.readLine();
      } catch (IOException e) {
         System.out.println(genericError + e.getMessage());
      }
      return response;
   }

   //Accept connection from client socket
   private void acceptConnection() {
      if (projectVerbose && !online) {
         System.out.println("Server Online");
         online = true;
      }
      try {
         clientConnectionSocket = serverSocket.accept();
         SocketAddress socketAddress = clientConnectionSocket.getRemoteSocketAddress();
         if (projectVerbose && !connected) {
            System.out.println("Get connection from: " + socketAddress + ".");
            connected = true;
         }
      } catch (IOException e) {
         System.out.println(genericError + e.getMessage());
      }
   }

   //Creates Socket
   private void initialize() {
      try {
         if (extremelyVerbose) { System.out.println("Opening Socket."); }
         serverSocket = new ServerSocket(portNumber);
         if (extremelyVerbose) { System.out.println("Socket Opened."); }
      } catch (IOException e) {
         if (extremelyVerbose) { System.out.println(genericError + e.getMessage()); }
      }
   }

   //Closes socket
   private void end() {
      try {
         if (extremelyVerbose) { System.out.println("Closing Sockets."); }
         serverSocket.close();
         clientConnectionSocket.close();
         if (extremelyVerbose) { System.out.println("Sockets closed"); }
      } catch (IOException e) {
         System.out.println(genericError + e.getMessage());
      }
   }

   //Constructor
   public ServerHelper(int portNumber) {
      extremelyVerbose = false;
      projectVerbose = true;
      this.portNumber = portNumber;
      serverSocket = null;
      continuous = true;
      online = false;
      connected = false;
   }

   //I wanted it to be server.start
   //Deal with it (•_•) / ( •_•)>⌐■-■ / (⌐■_■)
   public void start() {
      while(continuous) {
         runServer();
      }
   }
}

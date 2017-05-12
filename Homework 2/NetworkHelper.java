import java.net.*;
import java.io.*;
import java.lang.NumberFormatException;
import java.util.*;

public class NetworkHelper {
   int portNumber;
   ServerSocket socket;
   Socket receiverSocket;
   Socket senderSocket;
   BufferedReader inFromSender;
   BufferedReader inFromReceiver;
   DataOutputStream outToSender;
   DataOutputStream outToReceiver;
   boolean keepRunning;
   Random rand;

   private void run() throws IOException {
      waitForReceiver();
      waitForSender();
      relay();
      end();
   }

   private void relay() throws IOException {
      String packet = "";
      boolean sendPacket = true;
      while(keepRunning) {
         if (packet == null) {
            continue;
         }
         if (sendPacket) {
            packet = receivePacket(inFromSender);
            if (packet.contains("DROP")) {
               sendPacket(packet, outToSender);
               //String temp = ProjectVariables.splitString(packet, " ");
               System.out.println("ACK2 DROP");
               continue;
            }
            sendPacket(packet, outToReceiver);
         } else {
            packet = receivePacket(inFromReceiver);
            if (packet.contains("-1")) {
               keepRunning = false;
            }
            sendPacket(packet, outToSender);
         }
         sendPacket = !sendPacket;
      }
   }

   private void relayResponse() throws IOException {
      String packet = "";
      packet = receivePacket(inFromReceiver);
      sendPacket(packet, outToSender);
   }

   private void relayMessage() throws IOException {
      String packet = "";
      while (!packet.equals("-1")) {
         packet = receivePacket(inFromSender);
         sendPacket(packet, outToReceiver);

         //If packet = -1 don't print packet
         if (packet.equals("-1")) { break; }
         if (ProjectVariables.VERBOSE) { System.out.println(packet); }
      }
   }

   //# id check Packet
   //# check ack
   private String receivePacket(BufferedReader reader) throws IOException {
      String response = reader.readLine();
      String[] parsedResponse = ProjectVariables.splitString(response, " ");
      String output = "";
      if (response.contains("-1")) {
         return response;
      }
      if (parsedResponse.length == 4) {
         //System.out.println("TESTING " + response);
         response = randomPacket(response);
         //System.out.println("TESTING " + randomPacket(response));
         output += "Packet" + parsedResponse[0];
         output += " " + parsedResponse[1];
         String temp = checkPacket(response);
         String[] split = ProjectVariables.splitString(temp, " ");
         temp = split[2];
         output += " " + temp;
         System.out.println(output);
      } else if (parsedResponse.length == 3) {
         output += "ACK" + parsedResponse[0];
         output += " " + parsedResponse[2];
         System.out.println(output);
      }
      return response;
   }

   private String checkPacket(String packet) throws IOException {
      String[] split = packet.split(" ");
      String ack = ProjectVariables.getAckString(ProjectVariables.CORRUPT);
      if (split.length == 4) {
         int sum = ProjectVariables.getCheckSum(split[3]);
         if (sum == Integer.parseInt(split[2])) {
            ack = ProjectVariables.getAckString(
            ProjectVariables.PASS);
         } else {
            ack = ProjectVariables.getAckString(
            ProjectVariables.CORRUPT);
         }
      }
      ack = ProjectVariables.createAck(ack, Integer.parseInt(split[0]));
      if (packet.contains("DROP")) {
         ack = split[0] + " ";
         ack += ProjectVariables.getCheckSum("DROP");
         ack += " DROP";
      }
      if (packet.contains("-1")) {
         ack += " -1";
      }
      return ack;
   }

   private String randomPacket(String packet) throws IOException {
      double number = randomNumber();
      if (number < 0.5) {
         return packet;
      } else if (number >= 0.5 && number < 0.75) {
         String[] split = ProjectVariables.splitString(packet, " ");
         packet = "";
         int check = Integer.parseInt(split[2]);
         check++;
         split[2] = Integer.toString(check);
         String temp = "";
         for (int i = 0; i < split.length - 1; i++) {
            packet += split[i] + " ";
            temp = split[i+1];
         }
         packet += temp;
         return packet;
      } else {
         String[] split = ProjectVariables.splitString(packet, " ");
         packet = "";
         packet += split[0] + " ";
         packet += ProjectVariables.getCheckSum("DROP");
         packet += " DROP";
      }
      return packet;
   }

   private void sendPacket(String packet, DataOutputStream stream) throws IOException {
      stream.writeBytes(packet + '\n');
      if (ProjectVariables.VERBOSE) {
         System.out.println("Sending packet: " + packet); }
   }

   private void waitForReceiver() throws IOException {
      receiverSocket = socket.accept();
      inFromReceiver = new BufferedReader(new InputStreamReader(receiverSocket.getInputStream()));
      outToReceiver = new DataOutputStream(receiverSocket.getOutputStream());

      if (ProjectVariables.VERBOSE) {
         System.out.println("Connected to Receiver."); }
   }

   private void waitForSender() throws IOException {
      senderSocket = socket.accept();
      inFromSender = new BufferedReader(new InputStreamReader(senderSocket.getInputStream()));
      outToSender = new DataOutputStream(senderSocket.getOutputStream());

      if (ProjectVariables.VERBOSE == true) {
         System.out.println("Connected to Sender."); }
   }

   public void start() throws IOException {
      run();
   }

   public void end() throws IOException {
      inFromSender.close();
      inFromReceiver.close();
      outToSender.close();
      outToReceiver.close();
   }

   public NetworkHelper(int portNumber) throws IOException {
      this.portNumber = portNumber;
      this.socket = new ServerSocket(this.portNumber);
      this.keepRunning = true;
      this.rand = new Random();
   }

   private double randomNumber() {
      return rand.nextDouble();
   }
}

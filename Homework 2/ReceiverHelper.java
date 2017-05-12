import java.net.*;
import java.io.*;
import java.lang.NumberFormatException;

public class ReceiverHelper {
   InetAddress url;
   int portNumber;
   Socket socket;
   BufferedReader inFromNetwork;
   DataOutputStream outToNetwork;
   boolean energizer;
   int packetsReceived;

   private void run() throws IOException {
      receive();
   }

   private void receive() throws IOException {
      while (energizer) {
         String packet = receivePacket(inFromNetwork);
         if (packet == null) {

         } else {
            if (packet.contains("-1")) {
               energizer = false;
            }
            String ack = checkPacket(packet);
            sendPacket(ack, outToNetwork);
         }
      }
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
      if (packet.contains("-1")) {
         ack += " -1";
      }
      return ack;
   }

   private String receivePacket(BufferedReader reader) throws IOException {
      String response = reader.readLine();
      packetsReceived++;
      String[] parsedResponse = ProjectVariables.splitString(response, " ");
      String output = "";
      output += "Waiting ";
      output += parsedResponse[0] + " ";
      output += packetsReceived + " ";
      output += "(" + response + ")" + " ";
      //output += checkPacket(response);
      String ack = checkPacket(response);
      String[] split = ProjectVariables.splitString(ack, " ");
      ack = split[2];
      output += ack;
      System.out.println(output);
      return response;
   }

   private void sendAck(String ack, DataOutputStream stream) throws IOException {
      sendPacket(ack, stream);
   }

   private void sendPacket(String packet, DataOutputStream stream) throws IOException {
      stream.writeBytes(packet + '\n');
      if (ProjectVariables.VERBOSE) { System.out.println("Sending packet: " + packet); }
   }

   private String receiveFile() throws IOException {
      String message = receiveMessage();
      return message;
   }

   private String receiveMessage() throws IOException {
      String message = "";
      String packet = "";
      while (!packet.equals("-1")) {
         packet = receivePacket();
         if (packet.equals("-1")) {
            break;
         } else {
            message += packet + " ";
         }
      }
      return message;
   }

   private String receivePacket() throws IOException {
      String response = inFromNetwork.readLine();
      System.out.println(response);
      //checkPacket(response);
      String[] split = response.split(" ");
      if (split.length == 4) {
         return split[3];
      }
      return response;
   }

   public void start() throws IOException {
      run();
   }

   public ReceiverHelper(InetAddress url, int portNumber) throws IOException {
      this.energizer = true;
      this.url = url;
      this.portNumber = portNumber;
      this.socket = new Socket(url, portNumber);
      this.inFromNetwork = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      this.outToNetwork = new DataOutputStream(socket.getOutputStream());
      this.packetsReceived = 0;
   }
}

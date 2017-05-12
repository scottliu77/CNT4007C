import java.net.*;
import java.io.*;
import java.lang.NumberFormatException;

public class SenderHelper {
   InetAddress url;
   int portNumber;
   String fileName;
   Socket socket;
   BufferedReader reader;
   BufferedReader inFromNetwork;
   DataOutputStream outToNetwork;
   int sequenceNumber;
   int id;
   int packetsSent;
   boolean resending;

   private void run() throws IOException {
      String file = readFile(reader);
      if (ProjectVariables.VERBOSE) {
         System.out.println("File: " + file);
      }
      String[] messages = ProjectVariables.splitString(file, "\\.");
      for (String message : messages) {
         message += ".";
         sendMessage(message);
      }
      //String response = receiveAcks(inFromNetwork);
      //String[] acks = ProjectVariables.splitString(file, " ");
      //System.out.println(response);
   }

   private String receiveAcks(BufferedReader reader) throws IOException {
      String acks = "";
      String line = "";
      boolean keepRunning = true;
      while (keepRunning) {
         line = reader.readLine();
         if (line == null) {
            line = "-1";
            keepRunning = false;
            continue;
         } else {
            acks += line + "\n";
         }
      }
      return acks;
   }

   private String receiveAck(BufferedReader reader) throws IOException {
      String ack = "";
      ack = receivePacket(reader);
      if (ack == null) {
         return null;
      }
      return ack;
   }

   private String readFile(BufferedReader reader) throws IOException {
      String file = "";
      String line = "";
      boolean keepRunning = true;
      while (keepRunning) {
         line = reader.readLine();;
         if (line == null) {
            line = "-1";
            keepRunning = false;
         }
         file += line;
      }
      return file;
   }

   private void sendMessage(String message) throws IOException {
      if (ProjectVariables.VERBOSE) {
         System.out.println("\nSending message: " + message);
      }
      String[] packets = ProjectVariables.splitString(message, " ");
      for (int i = 0; i < packets.length; i++) {
         if (packets[i].equals("")) {
            continue;
         }
         boolean success = false;
         String ack = "";
         while (!success) {
            sendPacket(packets[i], outToNetwork);
            ack = receiveAck(inFromNetwork);
            success = checkAck(ack);
            printUpdate(ack, success);
            if (!success) {
               resending = true;
               getSequenceNumber();
               id--;
            }
         }
      }
   }

   private void printUpdate(String packet, boolean yes) {
      String[] parsedResponse = ProjectVariables.splitString(packet, " ");
      String output = "";
      output += "Waiting ACK";
      if (yes) {
         output += "0 ";
      } else {
         if (packet.contains("DROP")) {
            output += "2 ";
         } else {
            output += "1 ";            
         }
      }
      output += packetsSent + " ";
      output += parsedResponse[2] + " ";
      if (!yes) {
         output += "resend packet" + parsedResponse[0];
      } else {
         output += "send packet" + sequenceNumber;
      }
      System.out.println(output);
   }

   private boolean checkAck(String ack) {
      String pass = ProjectVariables.getAckString(ProjectVariables.PASS);
      if (ack.contains(pass)) {
         return true;
      }
      return false;
   }

   private String receiveResponse() throws IOException {
      return receivePacket(inFromNetwork);
   }

   private String receivePacket(BufferedReader reader) throws IOException {
      String response = reader.readLine();
      return response;
   }

   private void sendFile() throws IOException {
      if (ProjectVariables.VERBOSE) { System.out.println("Sending file"); }
      sendMessage();
   }

   private void sendMessage() throws IOException {
      String line = "";
      boolean keepRunning = true;
      while (keepRunning) {
         line = readLine();
         if (line == null) {
            line = "-1";
            keepRunning = false;
         }
         String[] packets = splitLine(line);
         sendPackets(packets);
      }
   }

   private String readLine() throws IOException {
      String line = "";
      try {
         line = reader.readLine();
      } catch (FileNotFoundException e) {
         System.out.println(e.getMessage());
      }
      return line;
   }

   private String[] splitLine(String line) throws IOException {
      return line.split(" ");
   }

   private void sendPackets(String[] packets) throws IOException {
      for (String packet : packets) {
         sendPacket(packet, outToNetwork);
      }
   }

   private void sendPacket(String packet, DataOutputStream stream) throws IOException {
      packet = ProjectVariables.createPacket(packet, getSequenceNumber(), getId());
      packetsSent++;
      stream.writeBytes(packet + '\n');
      if (ProjectVariables.VERBOSE) { System.out.println("Sending packet: " + packet); }
   }

   private int getSequenceNumber() {
      int number = sequenceNumber;
      sequenceNumber = (sequenceNumber + 1) % 2;
      return number;
   }

   private int getId() {
      return id++;
   }

   private void openSocket() {
      if (ProjectVariables.VERBOSE) {
         System.out.println("Opening Socket.");
      }
      if (socket.isConnected() && ProjectVariables.VERBOSE == true) {
         System.out.println("Socket connected.");
      }
   }

   public void start() throws IOException {
      run();
   }

   public SenderHelper(InetAddress url, int portNumber, String fileName) throws IOException {
      this.url = url;
      this.portNumber = portNumber;
      this.fileName = fileName;
      this.socket = new Socket(this.url, this.portNumber);
      this.outToNetwork = new DataOutputStream(socket.getOutputStream());
      this.inFromNetwork = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      this.id = 0;
      this.sequenceNumber = 0;
      this.resending = false;
      try {
         this.reader = new BufferedReader(new FileReader(fileName));
      } catch (FileNotFoundException e) {
         System.out.println(e.getMessage());
      }
   }
}

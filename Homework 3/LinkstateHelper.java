import java.io.*;

public class LinkstateHelper {
   String filename;
   String file;
   String[] fileArray;
   int[][] nodeCosts;
   int INFINITY;
   boolean[] visitedArray;
   String DELIMIT;
   int[] finalMinimumCosts;

   //Method called to start helper logic
   public void start() {
      file = readFileAsString(filename);
      fileArray = convertFileToStringArray(file);
      nodeCosts = convertStringArrayToIntArray(fileArray);
      visitedArray = new boolean[fileArray.length - 1];

      //Prints preliminary information if VERBOSE is true
      printBegin();

      GlobalMethods.printDashedLines();
      GlobalMethods.print(GlobalMethods.header(fileArray.length));
      GlobalMethods.printDashedLines();

      String output = dijkstra(nodeCosts, visitedArray);
      //GlobalMethods.print(output);

      printEnd();
   }

   //Print information about the file
   private void printBegin() {
      GlobalMethods.statusMessage("Reading: " + filename);
      GlobalMethods.statusMessage("");

      GlobalMethods.statusMessage("Printing file...");
      GlobalMethods.statusMessage(file);
      GlobalMethods.statusMessage("");

      GlobalMethods.statusMessage("Printing fileArray...");
      String temp = GlobalMethods.printCostArray(nodeCosts);
      GlobalMethods.statusMessage(temp);
      GlobalMethods.statusMessage("");

      GlobalMethods.statusMessage("Printing visitedArray...");
      temp = GlobalMethods.printVisitedArray(visitedArray);
      GlobalMethods.statusMessage(temp);
      GlobalMethods.statusMessage("");
      GlobalMethods.statusMessage("BEGIN PROJECT OUTPUT");
      GlobalMethods.statusMessage("////////////");
      GlobalMethods.statusMessage("");
   }

   private void printEnd() {
      GlobalMethods.statusMessage("");
      GlobalMethods.statusMessage("////////////");
      GlobalMethods.statusMessage("END PROJECT OUTPUT");
      GlobalMethods.statusMessage("");
      GlobalMethods.statusMessage("Printing visitedArray...");
      String temp = GlobalMethods.printVisitedArray(visitedArray);
      GlobalMethods.statusMessage(temp);
      GlobalMethods.statusMessage("");

      GlobalMethods.statusMessage("Printing minimumCosts...");
      temp = GlobalMethods.printMinimumCostArray(finalMinimumCosts);
      GlobalMethods.statusMessage(temp);
      GlobalMethods.statusMessage("");
   }

   private String dijkstra(int[][] costs, boolean[] oldVisited) {
      String output = "";
      int numNodes = costs.length;
      int step = 0;

      boolean[] visited;
      int[][] originalCosts;
      int[] minimumCosts;
      int currentNode;
      int previousNode;
      boolean[] neighbors;
      int[] previousNodes;

      visited = oldVisited;
      originalCosts = costs;
      currentNode = 0;
      previousNode = 0;
      neighbors = GlobalMethods.findNeighbors(costs, currentNode);
      minimumCosts = GlobalMethods.initializeMinimumCostArray(numNodes);
      previousNodes = new int[numNodes];

      for (int i = 0; i < numNodes; i++) {
         minimumCosts[i] = costs[0][i];
      }

      while (!GlobalMethods.allNodesVisited(visited)) {
         //GlobalMethods.statusMessage("Step: " + Integer.toString(step++));

         //Go to the shortest cost neighbor
         previousNode = currentNode;
         currentNode = GlobalMethods.findClosestNeighbor(minimumCosts, visited);

         output = "";
         output += step++;
         output += DELIMIT;

         //Set currentNode to visited
         visited[currentNode] = true;

         //Find neighbors
         //neighbors = GlobalMethods.findNeighbors(originalCosts, currentNode);

         //Set minimumCosts of reachable, not visited neighbors
            //minimumCost = minimum(minimumCost, minimumCost(previousNode) + originalCost)
         for (int i = 0; i < numNodes; i++) {
            if (!visited[i]) {
               int change = minimum(minimumCosts[i], minimumCosts[currentNode] + originalCosts[currentNode][i]);
               if (change != minimumCosts[i]) {
                  minimumCosts[i] = change;
                  previousNodes[i] = currentNode;
               }
            }
         }

         output += GlobalMethods.visitedNodes(visited);
         output += DELIMIT;
         output += GlobalMethods.printMinsAndVisits(previousNodes, minimumCosts);
         //GlobalMethods.statusMessage(GlobalMethods.printMinimumCostArray(minimumCosts));

         GlobalMethods.print(output);
         GlobalMethods.printDashedLines();
      }
      //GlobalMethods.printDashedLines();

      finalMinimumCosts = minimumCosts;

      return output;
   }

   //Mathematical minimum
   private int minimum(int x, int y) {
      if (x < y) {
         if (x < 0) {
            return y;
         }
         return x;
      } else {
         if (y < 0) {
            return x;
         }
         return y;
      }
   }

   //Converts array index into node
   private int node(int node) {
      return ++node;
   }

   //Convert string array to int array for easy access to costs
   private int[][] convertStringArrayToIntArray(String[] array) {
      int length = array.length - 1;
      int[][] costs = GlobalMethods.initializeCostArray(length);
      String nodeString = "";
      String[] nodeSplit = null;
      try {
         for (int node = 0; node < length; node++) {
            nodeString = array[node];
            nodeString = nodeString.replace(".", "");
            nodeSplit = nodeString.split(",");
            if (nodeString.contains("EOF")) {
               continue;
            }
            for (int cost = 0; cost < length; cost++) {
               int value = INFINITY;
               if (!nodeSplit[cost].contains("N")) {
                  value = Integer.parseInt(nodeSplit[cost]);
               }
               costs[node][cost] = value;
            }
         }
      } catch (NumberFormatException e) {
         GlobalMethods.errorMessage(e.getMessage());
      }
      return costs;
   }

   //Returns the whole file as a string
   private String readFileAsString(String name) {
      BufferedReader reader = createFileReader(name);
      String line = "";
      String file = "";

      while (line != null) {
         line = readLine(reader);
         if (line == null) {
            continue;
         } else if (line.equals("EOF.")) {
            file += line;
         } else {
            file += line + "\n";
         }
      }

      return file;
   }

   //Reads a line from the given buffer
   private String readLine(BufferedReader reader) {
      String line = "";
      try {
         line = reader.readLine();
      } catch (FileNotFoundException e) {
         GlobalMethods.errorMessage(e.getMessage());
      } catch (IOException e) {
         GlobalMethods.errorMessage(e.getMessage());
      }
      return line;
   }

   //Creates a buffered reader
   private BufferedReader createFileReader(String name) {
      BufferedReader reader = null;
      try {
         reader = new BufferedReader(new FileReader(name));
      } catch (FileNotFoundException e) {
         GlobalMethods.errorMessage(e.getMessage());
      }
      return reader;
   }

   //Converts a single string with lines delimited by \n
   //Into a string array
   private String[] convertFileToStringArray(String file) {
      String[] array = null;
      array = file.split("\n");
      return array;
   }

   public LinkstateHelper(String filename) {
      this.filename = filename;
      this.INFINITY = Integer.MAX_VALUE;
      this.DELIMIT = " | ";
   }

   //Method for testing methods
   private void testMethods() {
      GlobalMethods.statusMessage(filename);
      GlobalMethods.printDashedLines();
      file = readFileAsString(filename);
      GlobalMethods.statusMessage(file);
      GlobalMethods.printDashedLines();
      String[] array = convertFileToStringArray(file);
      for (String string : array) {
         GlobalMethods.statusMessage(string);
      }
   }
}

//Java file for static methods that are useful for the project

public class GlobalMethods {
   public static boolean VERBOSE = true;
   private static int DASHES = 70;
   private static String SPACES = " | ";
   private static int INFINITY = Integer.MAX_VALUE;

   //Used to print a status messaage for debugging
   public static void statusMessage(String message) {
      if (VERBOSE) {
         System.out.println(message);
      }
   }

   //Simple print statement
   public static void print(String message) {
      System.out.println(message);
   }

   //Used to print an error message
   public static void errorMessage(String message) {
      System.out.println("Error: " + message);
   }

   //Prints out DASHES dashes to delimite terminal output
   public static void printDashedLines() {
      for (int i = 0; i < DASHES; i++) {
         System.out.print("-");
      }
      System.out.println();
   }

   //Creates header for output
   public static String header(int nodes) {
      String output = "";
      output += "Step";
      output += SPACES;
      output += "N'";
      for (int i = 2; i < nodes; i++) {
         output += SPACES;
         output += "D(" + i + "),p(" + i + ")";
      }
      return output;
   }

   //Creates cost array with infinity for all values
   public static int[][] initializeCostArray(int nodes) {
      int[][] costArray = new int[nodes][nodes];
      for (int node = 0; node < nodes; node++) {
         for (int cost = 0; cost < nodes; cost++) {
            costArray[node][cost] = INFINITY;
         }
      }
      //System.out.println(printCostArray(costArray));
      return costArray;
   }

   public static String printCostArray(int[][] array) {
      String output = "";
      for (int node = 0; node < array.length; node++) {
         for (int cost = 0; cost < array[node].length; cost++) {
            if (array[node][cost] == INFINITY) {
               //System.out.print("N");
               output += "N";
            } else {
               //System.out.print(array[node][cost]);
               output += array[node][cost];
            }
            //System.out.print(" ");
            output += " ";
         }
         //System.out.println();
         if (node != array.length - 1) {
            output += "\n";
         }
      }
      return output;
   }

   public static String printVisitedArray(boolean[] array) {
      String output = "";
      for (int i = 0; i < array.length; i++) {
         if (array[i]) {
            output += "YES";
         } else {
            output += "NO";
         }
         if (i != array.length - 1) {
            output += " ";
         }
      }
      return output;
   }

   public static int[] initializeMinimumCostArray(int nodes) {
      int[] output = new int[nodes];
      for (int i = 0; i < nodes; i++) {
         output[i] = INFINITY;
      }
      return output;
   }

   public static String printMinimumCostArray(int[] array) {
      String output = "";
      for (int i = 0; i < array.length; i++) {
         if (array[i] == INFINITY) {
            //System.out.print("N");
            output += "N";
         } else {
            output += array[i];
         }
         if (i != array.length - 1) {
            output += " ";
         }
      }
      return output;
   }

   public static boolean[] findNeighbors(int[][] costs, int node) {
      boolean[] neighbors = new boolean[costs.length];
      int cost;
      for (int i = 0; i < costs.length; i++) {
         cost = costs[node][i];
         if (cost != INFINITY && cost != 0) {
            neighbors[i] = true;
         } else {
            neighbors[i] = false;
         }
      }
      return neighbors;
   }

   public static int findClosestNeighbor(int[] minimumCosts, boolean[] visited) {
      int minimum = INFINITY;
      int output = INFINITY;
      for (int i = 0; i < minimumCosts.length; i++) {
         if (!visited[i]) {
            if (minimumCosts[i] < minimum) {
               minimum = minimumCosts[i];
               output = i;
            }
         }
      }
      //System.out.println("Neighbor: " + (output+1));
      return output;
   }

   public static boolean allNodesVisited(boolean[] visited) {
      for (boolean node : visited) {
         if (!node) {
            return false;
         }
      }
      return true;
   }

   public static String visitedNodes(boolean[] visited) {
      String nodes = "";
      for (int i = 0; i < visited.length; i++) {
         if (visited[i]) {
            nodes += (i+1) + ",";
         }
      }
      nodes = nodes.substring(0, nodes.length()-1);
      return nodes;
   }

   public static String printMinsAndVisits(int[] previous, int[] minimumCosts) {
      String output = "";
      for (int i = 1; i < previous.length; i++) {
         if (minimumCosts[i] == INFINITY) {
            output += "N" + SPACES;
            //return output;
         } else {
            output += minimumCosts[i];
            output += "," + (previous[i] + 1) + SPACES;
         }
         //output += "," + (previous[i] + 1) + SPACES;
      }
      return output;
   }
}

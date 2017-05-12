// private String dijkstra(int[][] costs, boolean[] visited) {
//    String output = "";
//    int step = 0;
//    int currentNode = 0;
//    int minimumIndex = INFINITY;
//    visited[0] = true;
//    int countNodes = costs.length;
//    int tempCost = INFINITY;
//
//    minimumCosts = GlobalMethods.initializeMinimumCostArray(countNodes);
//    for (int i = 0; i < countNodes; i++) {
//       minimumCosts[i] = costs[0][i];
//    }
//
//    while (true) {
//       minimumIndex = minimum(costs[currentNode], visited);
//       if (minimumIndex == INFINITY) {
//          break;
//       }
//       visited[minimumIndex] = true;
//       for (int i = 0; i < countNodes; i++) {
//          tempCost = costs[currentNode][i];
//          minimumCosts[i] = minimum(minimumCosts[i], costs[currentNode][i] + tempCost);
//       }
//       output += dijkstraOutput(step, currentNode, visited, minimumCosts);
//    }
//    return output;
// }
//
// private String dijkstraOutput(int step, int node, boolean[] visited, int[] costs) {
//    String output = "";
//    output += step;
//    output += DELIMIT;
//    output += node(node);
//    output += "\n";
//    return output;
// }
//
// //Finds minimum cost index
// private int minimum(int[] array, boolean[] visited) {
//    int minimum = INFINITY;
//    int minimumIndex = INFINITY;
//    for (int i = 0; i < array.length; i++) {
//       if (array[i] < minimum && array[i] != 0) {
//          if (!visited[i]) {
//             //System.out.println("Array: " + array[i]);
//             minimumIndex = i;
//             minimum = array[i];
//          }
//       }
//    }
//    //System.out.println("MinimumIndex: " + minimumIndex);
//    return minimumIndex;
// }

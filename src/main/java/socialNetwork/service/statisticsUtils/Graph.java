package socialNetwork.service.statisticsUtils;

import java.util.ArrayList;

//https://www.geeksforgeeks.org/connected-components-in-an-undirected-graph/
public class Graph {
    // A user define class to represent a graph.
    // A graph is an array of adjacency lists.
    // Size of array will be V (number of vertices
    // in graph)
    int V;
    ArrayList<ArrayList<Integer>> adjListArray;

    // constructor
    public Graph(int V)
    {
        this.V = V;
        // define the size of array as
        // number of vertices
        adjListArray = new ArrayList<>();

        // Create a new list for each vertex
        // such that adjacent nodes can be stored

        for (int i = 0; i < V; i++) {
            adjListArray.add(i, new ArrayList<>());
        }
    }

    // Adds an edge to an undirected graph
    public void addEdge(int src, int dest)
    {
        // Add an edge from src to dest.
        adjListArray.get(src).add(dest);

        // Since graph is undirected, add an edge from dest
        // to src also
        adjListArray.get(dest).add(src);
    }

    void DFSUtil(int v, boolean[] visited)
    {
        // Mark the current node as visited and print it
        visited[v] = true;
        // System.out.print(v + " ");
        // Recur for all the vertices
        // adjacent to this vertex
        for (int x : adjListArray.get(v)) {
            if (!visited[x])
                DFSUtil(x, visited);
        }
    }

    int DFSUtilCount(int v, boolean[] visited, int numberOfEdge, ArrayList<Integer> connectedNumbers)
    {
        // Mark the current node as visited and print it
        visited[v] = true;
        connectedNumbers.add(v);
        // System.out.print(v + " ");
        // Recur for all the vertices
        // adjacent to this vertex
        for (int x : adjListArray.get(v)) {
            if (!visited[x])
                numberOfEdge = DFSUtilCount(x, visited, numberOfEdge + 1, connectedNumbers);
        }
        return numberOfEdge;
    }

    public int connectedComponents(boolean[] validId)
    {
        int numberOfConnectedComponents = 0;
        // Mark all the vertices as not visited
        boolean[] visited = new boolean[V];
        for (int v = 0; v < V; ++v) {
            if (!visited[v] && validId[v]) {
                numberOfConnectedComponents++;
                // print all reachable vertices
                // from v
                DFSUtil(v, visited);
                // System.out.println();
            }
        }
        // System.out.println();
        return numberOfConnectedComponents;
    }

    public ArrayList<Integer> maxConnectedComponents(boolean[] validId)
    {
        ArrayList<Integer> maxConnectedComponentsList = new ArrayList<Integer>();
        int maxNumberOfEdge = -1;
        boolean[] visited = new boolean[V];
        for (int v = 0; v < V; ++v) {
            if (!visited[v] && validId[v]) {
                int currentNumberOfEdge = 0;
                ArrayList<Integer> connectedComponents = new ArrayList<Integer>();
                currentNumberOfEdge = DFSUtilCount(v, visited, currentNumberOfEdge, connectedComponents);

                if(currentNumberOfEdge > maxNumberOfEdge){
                    maxNumberOfEdge = currentNumberOfEdge;
                    maxConnectedComponentsList = connectedComponents;
                }
            }
        }

        return maxConnectedComponentsList;
    }
}

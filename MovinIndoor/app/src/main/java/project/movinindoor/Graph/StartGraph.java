package project.movinindoor.Graph;

import android.util.Log;

/**
 * Created by Wietse on 24-11-2014.
 */
public class StartGraph {


    public static void runGraphs() {
        Graph g = new Graph();

        g.addVertex("77");
        g.addVertex("87");
        g.addVertex("4");
        g.addVertex("6");
        g.addVertex("2");
        g.addEdge("77", "2", 7.0);
        g.addEdge("2", "87", 3.0);
        g.addEdge("77", "87", 11.0);
        g.addEdge("6", "2", 3.0);


        g.getShortestPath("77", "87");
        g.printPath("87");
        g.printPath("6");
    }
}

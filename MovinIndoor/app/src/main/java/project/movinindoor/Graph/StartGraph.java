package project.movinindoor.Graph;

import android.util.Log;

/**
 * Created by Wietse on 24-11-2014.
 */
public class StartGraph {


    public static void runGraphs() {
        Graph g = new Graph();
        NodeReader r = new NodeReader();


        try {
            Log.i("groep3", r.jsonList.toString());
            for (Node n : r.jsonList.values()) {
                g.addVertex(n.nodeId);
            }

            for (Node n : r.jsonList.values()) {
                for (ToNode t : n.toNode) {
                    g.addEdge(n.nodeId, t.toNodeID, t.cost);
                }
            }


//        g.addVertex("77");
//        g.addVertex("87");
//        g.addVertex("4");
//        g.addVertex("6");
//        g.addVertex("2");
//        g.addEdge("77", "2", 7.0);
//        g.addEdge("2", "87", 3.0);
//        g.addEdge("77", "87", 11.0);
//        g.addEdge("6", "2", 3.0);


//        g.getShortestPath("77", "87");
//        g.printPath("87");
//        g.printPath("6");
        }catch(Exception e){
//            Log.i("FAILED", e.toString());
            Log.i("groep1", e.getMessage());
        }
    }
}

package project.movinindoor.Graph;

import android.util.Log;

import java.util.List;

/**
 * Created by Wietse on 24-11-2014.
 */
public class StartGraph {


    public static void runGraphs() {
        Graph g = new Graph();
        NodeReader r = new NodeReader();

        try {

            for (Node n : r.jsonList.values()) {
                g.addVertex(n.nodeId, n.location.get(0), n.location.get(1));
            }

            for (Node n : r.jsonList.values()) {
                for (ToNode t : n.toNode) {
                    g.addEdge(n.nodeId, t.toNodeID, t.cost);
                    Log.i("groep3", n.nodeId + " -> " + t.toNodeID + ": " + t.cost);
                }
            }

        g.printPath("68");
        List l = g.getPath("68");

            for(Object v : l){
                Log.i("coord", String.valueOf(((Vertex) v).lat1));
                Log.i("coord", String.valueOf(((Vertex) v).long1));
            }

//        g.printPath("6");
        }catch(Exception e){
//            Log.i("FAILED", e.toString());
            e.printStackTrace();
        }
    }
}

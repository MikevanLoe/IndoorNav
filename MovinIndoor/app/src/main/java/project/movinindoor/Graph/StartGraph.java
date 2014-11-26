package project.movinindoor.Graph;

import android.graphics.Color;
import android.util.Log;

import project.movinindoor.MapsActivity;

/**
 * Created by Wietse on 24-11-2014.
 */
public class StartGraph {


    public static void runGraphs() {
        Graph g = new Graph();
        NodeReader r = new NodeReader();

        try {
            double lat1 = 0.0;
            double long1 = 0.0;
            double lat2 = 0.0;
            double long2 = 0.0;

            for (Node n : r.jsonList.values()) {
                lat1 = (double) n.location.get(0);
                long1 = (double) n.location.get(1);
                g.addVertex(n.nodeId);
                MapsActivity.addMarker(lat1, long1, n.nodeId);
            }




            for (Node n : r.jsonList.values()) {
                lat1 = (double) n.location.get(0);
                long1 = (double) n.location.get(1);
                for (ToNode t : n.toNode) {
                    lat2 = (double) r.jsonList.get(t.toNodeID).location.get(0);
                    long2 = (double) r.jsonList.get(t.toNodeID).location.get(1);

                    MapsActivity.addPolyline(lat1, long1, lat2, long2, Color.BLACK);
                    g.addEdge(n.nodeId, t.toNodeID, t.cost);
                    Log.i("groep3", n.nodeId + " -> " + t.toNodeID + ": " + t.cost);
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


        g.getShortestPath("18");
        g.printPath("70");
//        g.printPath("6");
        }catch(Exception e){
//            Log.i("FAILED", e.toString());
            e.printStackTrace();
        }
    }
}

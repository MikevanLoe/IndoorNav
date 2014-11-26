package project.movinindoor.Graph;

import android.graphics.Color;
import android.util.Log;

import java.util.List;
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
                g.addVertex(n.nodeId, n.location.get(0), n.location.get(1));
                lat1 = (double) n.location.get(0);
                long1 = (double) n.location.get(1);
                g.addVertex(n.nodeId, lat1, long1);
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

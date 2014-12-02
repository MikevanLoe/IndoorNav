package project.movinindoor.Graph;

import android.graphics.Color;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import project.movinindoor.MapDrawer;

/**
 * Created by Wietse on 24-11-2014.
 */
public class StartGraph {

    public static Graph g;
    public static void runGraphs() {
        g = new Graph();
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
                //MapDrawer.addMarker(lat1, long1, n.nodeId);
            }

            for (Node n : r.jsonList.values()) {
                lat1 = (double) n.location.get(0);
                long1 = (double) n.location.get(1);
                for (ToNode t : n.toNode) {
                    lat2 = (double) r.jsonList.get(t.toNodeID).location.get(0);
                    long2 = (double) r.jsonList.get(t.toNodeID).location.get(1);

                    MapDrawer.addPolyline(lat1, long1, lat2, long2);
                    g.addEdge(n.nodeId, t.toNodeID, t.cost);
                    //Log.i("groep3", n.nodeId + " -> " + t.toNodeID + ": " + t.cost);
                }
            }

            findNearestNode(r);


             /*
            g.dijkstra("70");
            g.printPath("68");
            List l = g.getPath("68");
            g.drawPath("36");
            g.drawPath("70", "36");
            */

        } catch (Exception e) {
            Log.i("FAILED", e.toString());
        }
    }

    public static void findNearestNode(NodeReader r) {
        double startLat1 = 52.499506171693106;
        double startLong1 = 6.080507151782513;

        double shortLat = 0.0;
        double shortLng = 0.0;

        for (Node n : r.jsonList.values()) {

            double lat1 = n.location.get(0);
            double long1 = n.location.get(1);

            if(shortLng == 0.0 && shortLng == 0.0) {
                shortLat = lat1;
                shortLng = long1;
            }

            double som1 = measureMeters(startLat1, startLong1, lat1, long1);
            double som2 = measureMeters(startLat1, startLong1, shortLat,  shortLng);

            if(som1 <= som2) {
                //Log.i("TETTTTTT-1", som1 + "<=" + som2);
                shortLat = lat1;
                shortLng = long1;
            }
        }
        MapDrawer.addPolyline(shortLat, shortLng, startLat1, startLong1, Color.BLUE);
    }

    public static double measureMeters(double lat1,double lon1,double lat2,double lon2){
        double R = 6378.137; // Radius of earth in KM
        double dLat = (lat2 - lat1) * Math.PI / 180;
        double dLon = (lon2 - lon1) * Math.PI / 180;
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(lat1 * Math.PI / 180) * Math.cos(lat2 * Math.PI / 180) *
                        Math.sin(dLon/2) * Math.sin(dLon/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double d = R * c;
        return d * 1000; // meters
    }
}

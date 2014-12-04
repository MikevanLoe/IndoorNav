package project.movinindoor.Graph;

import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import project.movinindoor.LoadRooms;
import project.movinindoor.MapDrawer;
import project.movinindoor.MapsActivity;
import project.movinindoor.R;
import project.movinindoor.Room;
import project.movinindoor.Rooms;

/**
 * Created by Wietse on 24-11-2014.
 */
public class SetupGraph  {

    private Graph g;
    private NodeReader r = new NodeReader();
    private Rooms rooms;

    public SetupGraph() {

        new Thread(new Runnable() {
            public void run() {
                g = new Graph();
                createNodes();
                createEdges();
            }
        }).start();


        new Thread(new Runnable() {
            public void run() {
                rooms = new Rooms();
                int size = rooms.getRooms().size();
            }
        }).start();


    }

    public Graph getGraph() { return g; }

    public NodeReader getNodes() { return r; }

    public Rooms getRooms() { return rooms; }

    public void createNodes() {
        double lat1, long1;

        for (Node n : r.jsonList.values()) {
            g.addVertex(n.nodeId, n.location.get(0), n.location.get(1));
            lat1 = n.location.get(0);
            long1 = n.location.get(1);
            g.addVertex(n.nodeId, lat1, long1);

            final double t1 = lat1;
            final double t2 = long1;
            final String t3 = n.nodeId;

            /*
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    MapDrawer.addMarker(t1, t2, t3);
                }
            });
            */
            //MapDrawer.addMarker(lat1, long1, n.nodeId);
        }
    }

    public void createEdges() {
        double lat1, long1, lat2, long2;

        for (Node n : r.jsonList.values()) {
            lat1 =  n.location.get(0);
            long1 = n.location.get(1);
            for (ToNode t : n.toNode) {
                lat2 =  r.jsonList.get(t.toNodeID).location.get(0);
                long2 =  r.jsonList.get(t.toNodeID).location.get(1);
                g.addEdge(n.nodeId, t.toNodeID, t.cost);

                final double t1 = lat1;
                final double t2 = long1;
                final double t3 = lat2;
                final double t4 = long2;

                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        MapDrawer.addPolyline(t1, t2, t3, t4);
                    }
                });


                //Log.i("groep3", n.nodeId + " -> " + t.toNodeID + ": " + t.cost);
            }
        }
    }

    public Node findNearestNode(LatLng latLng) {
        double startLat1 = latLng.latitude;
        double startLong1 = latLng.longitude;

        double shortLat = 0.0;
        double shortLng = 0.0;
        Node tempNode = null;

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
                shortLat = lat1;
                shortLng = long1;
                tempNode = n;
            }
        }
        MapDrawer.addPolyline(shortLat, shortLng, startLat1, startLong1, Color.BLUE);
        return tempNode;
    }

    //From Room -> To Room
    public boolean navigateRoute(String startPosition, String endPosition) {
        Room startRoom = rooms.getRoom(startPosition);
        if(startRoom == null) {
            Toast.makeText(MapsActivity.getContext().getApplicationContext(), "To" + startPosition + " not found (Format B0.14)", Toast.LENGTH_SHORT).show();
            return false;
        }
        Room endRoom = rooms.getRoom(endPosition);
        if(endRoom == null) {
            Toast.makeText(MapsActivity.getContext().getApplicationContext(), "From " + endPosition + " not found (Format B0.14)", Toast.LENGTH_SHORT).show();
            return false;
        }
        Node startNode = FindClosestNodeInsideRoom(startRoom);
        if(startNode == null) { startNode = findNearestNode(startRoom.getLatLngBoundsCenter()); }

        Node endNode = FindClosestNodeInsideRoom(endRoom);
        if(endNode == null) { endNode = findNearestNode(endRoom.getLatLngBoundsCenter()); }

        if(startNode == null || endNode == null) {
            Toast.makeText(MapsActivity.getContext().getApplicationContext(), "Navigation not found", Toast.LENGTH_SHORT).show();
            return false;
        }
        LatLng startPositionLatLng = new LatLng(startNode.location.get(0), startNode.location.get(1));
        LatLng endPositionLatLng = new LatLng(endNode.location.get(0), endNode.location.get(1));

        double cost = g.drawPath(startNode.nodeId.toString(), endNode.nodeId.toString());
        if(cost != 0.0) {
            String walkingSpeed = g.calculateWalkingSpeed(cost);
            MapsActivity.textSpeed.setText("Estimate duration: " + walkingSpeed);
            MapsActivity.textSpeedCost.setText(String.valueOf(Math.round(cost)) + "m");

            MapsActivity.textFrom.setText(startPosition);
            MapsActivity.textTo.setText(endPosition);

            MapDrawer.addMarker(startPositionLatLng, startPosition);
            MapDrawer.addMarker(endPositionLatLng, endPosition);
            return true;
        }
        return false;
    }

    public Node FindClosestNodeInsideRoom(Room room) {

        for (Node node : r.jsonList.values()) {
            LatLng latLng1 =  new LatLng(node.location.get(0), node.location.get(1));
            if(rooms.nodeInsideRoom(room, latLng1)) {
                return node;
            }
        }



        return r.jsonList.get(0);
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

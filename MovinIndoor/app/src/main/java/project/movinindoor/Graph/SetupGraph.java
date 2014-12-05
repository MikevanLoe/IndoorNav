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
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
                final Node nNew = n;

                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        MapDrawer.addPolyline(t1, t2, t3, t4, Color.BLACK, Integer.parseInt(nNew.floor));
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
        double extraCost = 0.0;
        LatLng startPositionLatLng;
        LatLng endPositionLatLng;
        Node endNode, startNode;
        Room startRoom, endRoom;

        //if not a custom location
        if(MapsActivity.customStartPos == null) {
            startRoom = rooms.getRoom(startPosition);

            if (startRoom == null) {
                Toast.makeText(MapsActivity.getContext().getApplicationContext(), "From" + startPosition + " not found", Toast.LENGTH_SHORT).show();
                return false;
            }

            startNode = FindClosestNodeInsideRoom(startRoom);
            if(startNode == null) {
                startNode = findNearestNode(startRoom.getLatLngBoundsCenter());
                extraCost = measureMeters(startRoom.getLatLngBoundsCenter().latitude, startRoom.getLatLngBoundsCenter().longitude, startNode.location.get(0), startNode.location.get(1));
            }

            startPositionLatLng = new LatLng(startNode.location.get(0), startNode.location.get(1));
        } else {
            startNode = findNearestNode(MapsActivity.customStartPos);
            extraCost = measureMeters(MapsActivity.customStartPos.latitude, MapsActivity.customStartPos.longitude, startNode.location.get(0), startNode.location.get(1));
            startPositionLatLng = MapsActivity.customStartPos;
        }

        //if not a custom location
        if(MapsActivity.customEndPos == null) {
            String re1="([a-z])";	// Any Single Word Character (Not Whitespace) 1
            String re2="(\\d+)";	// Integer Number 1
            String re3="(.)";	// Any Single Character 1
            String re4="(\\d)";	// Any Single Digit 1
            String re5="(\\d)";	// Any Single Digit 2

            Pattern p = Pattern.compile(re1+re2+re3+re4+re5,Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
            Matcher m = p.matcher(endPosition);
            if (m.matches()) {
                endRoom = rooms.getRoom(endPosition);
            } else {
                List<Room> roomsWithName = rooms.getAllRoomsWithName(endPosition);
                if(roomsWithName.size() != 1) {
                    double tempCost = 0.0;
                    Room tempRoom = null;
                    for (Room room : roomsWithName) {
                        Node node = FindClosestNodeInsideRoom(room);
                        //find shortest route to position
                        if(node != null) {
                            double cost = g.drawPath(startNode.nodeId.toString(), node.nodeId.toString());
                            if(tempCost == 0.0 && tempRoom == null) {
                                tempCost = cost;
                                tempRoom = room;
                            }

                            if (cost <= tempCost) {
                                tempCost = cost;
                                tempRoom = room;
                            }
                        }

                    }
                    MapDrawer.removePolylines();
                    endRoom = tempRoom;
                } else {
                    endRoom = rooms.getRoom(endPosition);
                }
            }

            if (endRoom == null) {
                Toast.makeText(MapsActivity.getContext().getApplicationContext(), "To " + endPosition + " not found", Toast.LENGTH_SHORT).show();
                return false;
            }

            endNode = FindClosestNodeInsideRoom(endRoom);
            if (endNode == null) {
                endNode = findNearestNode(endRoom.getLatLngBoundsCenter());
                extraCost = measureMeters(endRoom.getLatLngBoundsCenter().latitude, endRoom.getLatLngBoundsCenter().longitude, endNode.location.get(0), endNode.location.get(1));
            }

            endPositionLatLng = new LatLng(endNode.location.get(0), endNode.location.get(1));
        } else {
            endNode = findNearestNode(MapsActivity.customEndPos);
            extraCost = measureMeters(MapsActivity.customEndPos.latitude, MapsActivity.customEndPos.longitude, endNode.location.get(0), endNode.location.get(1));
            endPositionLatLng = MapsActivity.customEndPos;
        }

            if(startNode == null || endNode == null) {
                Toast.makeText(MapsActivity.getContext().getApplicationContext(), "Navigation not found", Toast.LENGTH_SHORT).show();
                return false;
            }




        double cost = g.drawPath(startNode.nodeId.toString(), endNode.nodeId.toString());
        if(cost != 0.0) {
            cost += extraCost;
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
        return null;
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

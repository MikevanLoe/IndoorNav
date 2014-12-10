package project.movinindoor.Graph;

import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import project.movinindoor.*;
import project.movinindoor.Graph.Graph.Graph;
import project.movinindoor.Readers.NodeReader;
import project.movinindoor.Readers.RepairReader;
import project.movinindoor.Rooms.Room;
import project.movinindoor.Rooms.Rooms;

/**
 * Created by Wietse on 24-11-2014.
 */
public class GraphHandler {

    private Graph graph;
    private NodeReader nodeReader = new NodeReader();
    private RepairReader repairReader;
    private Rooms rooms;


    public GraphHandler() {

        new Thread(new Runnable() {
            public void run() {
                graph = new Graph();
                createNodes();
                createEdges();
            }
        }).start();


        new Thread(new Runnable() {
            public void run() {
                rooms = new Rooms();
                int size = rooms.getRooms().size();
                repairReader = new RepairReader();
            }
        }).start();


    }

    public Graph getGraph() { return graph;
    }
    public RepairReader getRepairReader() { return repairReader; }

    public NodeReader getNodes() { return nodeReader; }

    public Rooms getRooms() { return rooms; }

    public void createNodes() {
        for (Node n : nodeReader.jsonList.values()) {
            graph.addVertex(n.nodeId, n.location.get(0), n.location.get(1));
        }
    }

    public void createEdges() {
        double lat1, long1, lat2, long2;

        for (Node n : nodeReader.jsonList.values()) {
            lat1 =  n.location.get(0);
            long1 = n.location.get(1);
            for (ToNode t : n.toNode) {
                lat2 =  nodeReader.jsonList.get(t.toNodeID).location.get(0);
                long2 =  nodeReader.jsonList.get(t.toNodeID).location.get(1);
                graph.addEdge(n.nodeId, t.toNodeID, t.cost);

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

        for (Node n : nodeReader.jsonList.values()) {

            double lat1 = n.location.get(0);
            double long1 = n.location.get(1);

            if(shortLng == 0.0 && shortLng == 0.0) {
                shortLat = lat1;
                shortLng = long1;
            }

            double som1 = CalcMath.measureMeters(startLat1, startLong1, lat1, long1);
            double som2 = CalcMath.measureMeters(startLat1, startLong1, shortLat, shortLng);

            if(som1 <= som2) {
                shortLat = lat1;
                shortLng = long1;
                tempNode = n;
            }
        }
        MapDrawer.addPolyline(shortLat, shortLng, startLat1, startLong1, Color.BLUE);
        return tempNode;
    }

    public Node FindClosestNodeInsideRoom(Room room) {

        for (Node node : nodeReader.jsonList.values()) {
            LatLng latLng1 =  new LatLng(node.location.get(0), node.location.get(1));
            if(rooms.nodeInsideRoom(room, latLng1)) {
                return node;
            }
        }
        return null;
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
                extraCost = CalcMath.measureMeters(startRoom.getLatLngBoundsCenter().latitude, startRoom.getLatLngBoundsCenter().longitude, startNode.location.get(0), startNode.location.get(1));
            }

            startPositionLatLng = new LatLng(startNode.location.get(0), startNode.location.get(1));
        } else {
            startNode = findNearestNode(MapsActivity.customStartPos);
            extraCost = CalcMath.measureMeters(MapsActivity.customStartPos.latitude, MapsActivity.customStartPos.longitude, startNode.location.get(0), startNode.location.get(1));
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
                            double cost = graph.drawPath(startNode.nodeId.toString(), node.nodeId.toString());
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
                extraCost = CalcMath.measureMeters(endRoom.getLatLngBoundsCenter().latitude, endRoom.getLatLngBoundsCenter().longitude, endNode.location.get(0), endNode.location.get(1));
            }

            endPositionLatLng = new LatLng(endNode.location.get(0), endNode.location.get(1));
        } else {
            endNode = findNearestNode(MapsActivity.customEndPos);
            extraCost = CalcMath.measureMeters(MapsActivity.customEndPos.latitude, MapsActivity.customEndPos.longitude, endNode.location.get(0), endNode.location.get(1));
            endPositionLatLng = MapsActivity.customEndPos;
        }

            if(startNode == null || endNode == null) {
                Toast.makeText(MapsActivity.getContext().getApplicationContext(), "Navigation not found", Toast.LENGTH_SHORT).show();
                return false;
            }




        double cost = graph.drawPath(startNode.nodeId.toString(), endNode.nodeId.toString());
        if(cost != 0.0) {
            cost += extraCost;
            String walkingSpeed = graph.calculateWalkingSpeed(cost);
            MapsActivity.textSpeed.setText("ETA: " + walkingSpeed);
            MapsActivity.textSpeedCost.setText("(" + String.valueOf(Math.round(cost)) + "m)");

            MapsActivity.textFrom.setText(startPosition);
            MapsActivity.textTo.setText(endPosition);

            MapDrawer.addMarker(startPositionLatLng, startPosition);
            MapDrawer.addMarker(endPositionLatLng, endPosition);
            return true;
        }
        return false;
    }




}

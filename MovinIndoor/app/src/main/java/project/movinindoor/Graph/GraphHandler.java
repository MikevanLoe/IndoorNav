package project.movinindoor.Graph;

import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;

import project.movinindoor.*;
import project.movinindoor.Graph.Graph.Graph;
import project.movinindoor.Models.Elevator;
import project.movinindoor.Models.Elevators;
import project.movinindoor.Models.Stairs;
import project.movinindoor.Readers.NodeReader;
import project.movinindoor.Readers.RepairReader;
import project.movinindoor.Models.Rooms;

/**
 * Created by Wietse on 24-11-2014.
 */
public class GraphHandler {

    public Graph graph;
    private NodeReader nodeReader = new NodeReader();
    private RepairReader repairReader;
    public Rooms rooms;
    public Elevators elevators;
    public Stairs stairs;


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


    public Elevators getElevators() {
        return elevators;
    }

    public Stairs getStairs() {
        return stairs;
    }

    public Graph getGraph() { return graph;  }
    public RepairReader getRepairReader() { return repairReader; }

    public void setRepairReader(RepairReader repairReader) { this.repairReader = repairReader; }

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

    /*
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
    */






}

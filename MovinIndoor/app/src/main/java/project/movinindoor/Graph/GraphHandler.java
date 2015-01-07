package project.movinindoor.Graph;

import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import project.movinindoor.Graph.Graph.Graph;
import project.movinindoor.Graph.Graph.Vertex;
import project.movinindoor.MapDrawer;
import project.movinindoor.Models.Elevators;
import project.movinindoor.Models.Rooms;
import project.movinindoor.Models.Stairs;
import project.movinindoor.Readers.NodeReader;
import project.movinindoor.Readers.RepairReader;

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
                createVertices();
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

    public Graph getGraph() {
        return graph;
    }

    public RepairReader getRepairReader() {
        return repairReader;
    }

    public void setRepairReader(RepairReader repairReader) {
        this.repairReader = repairReader;
    }

    public NodeReader getNodes() {
        return nodeReader;
    }

    public Rooms getRooms() {
        return rooms;
    }

    public void createVertices() {
        Vertex.Vertextype type = null;
        for (Node n : nodeReader.jsonList.values()) {
            if (n.getType() != null) {
                switch (n.getType()) {
                    case "Hall":
                        type = Vertex.Vertextype.Hall;
                        break;
                    case "Room":
                        type = Vertex.Vertextype.Room;
                        break;
                    case "Elevator":
                        type = Vertex.Vertextype.Elevator;
                        break;
                    case "Stairs":
                        type = Vertex.Vertextype.Hall;
                        break;
                    default:
                        break;
                }

                final LatLng t1 = n.getLatLng();
                final String t3 = n.getType();
               

                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                         MapDrawer.addMarker(t1, t3);
                    }
                });

                graph.addVertex(n.getNodeId(), n.getLatLng().latitude, n.getLatLng().longitude, type, n.getFloor());
            }
        }
    }
    public void createEdges() {
        double lat1, long1, lat2, long2;

        for (Node n : nodeReader.jsonList.values()) {
            lat1 = n.getLatLng().latitude;
            long1 = n.getLatLng().longitude;
            for (ToNode t : n.getToNode()) {
                lat2 = nodeReader.jsonList.get(t.getToNodeID()).getLatLng().latitude;
                long2 = nodeReader.jsonList.get(t.getToNodeID()).getLatLng().longitude;
                graph.addEdge(n.getNodeId(), t.getToNodeID(), t.getCost(), t.getActions());

                final double t1 = lat1;
                final double t2 = long1;
                final double t3 = lat2;
                final double t4 = long2;
                final Node nNew = n;

                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                       // MapDrawer.addPolyline(t1, t2, t3, t4, Color.BLACK, Integer.parseInt(nNew.floor));
                    }
                });


                //Log.i("groep3", n.nodeId + " -> " + t.toNodeID + ": " + t.cost);
            }
        }
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                MapDrawer.hidePolylines();
                MapDrawer.showPolylinesFloor(0);
            }
        });

    }
}

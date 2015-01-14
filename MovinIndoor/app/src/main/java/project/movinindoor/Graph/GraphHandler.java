package project.movinindoor.Graph;

import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import project.movinindoor.Graph.Graph.Graph;
import project.movinindoor.Graph.Graph.Vertex;
import project.movinindoor.MapDrawer;
import project.movinindoor.Models.Rooms;
import project.movinindoor.Readers.NodeReader;
import project.movinindoor.Readers.RepairReader;

/**
 * Created by Wietse on 24-11-2014.
 */
public class GraphHandler {

    private Graph graph;
    private NodeReader nodeReader = new NodeReader();
    private RepairReader repairReader;
    private Rooms rooms;

    /**
     * constructor that creates 2 threads.
     * one to create all vertices and edges
     * other one to create the rooms and the repairlist
     */
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

    /**
     *
     * @return Graph
     */
    public Graph getGraph() {
        return graph;
    }

    /**
     *
     * @return returns the repairreader so we can get the list of repairments
     */
    public RepairReader getRepairReader() {
        return repairReader;
    }

    /**
     * setter for repairReader
     * @param repairReader
     */
    public void setRepairReader(RepairReader repairReader) {
        this.repairReader = repairReader;
    }

    /**
     *
     * @return nodeReader
     */
    public NodeReader getNodes() {
        return nodeReader;
    }

    /**
     *
     * @return rooms
     */
    public Rooms getRooms() {
        return rooms;
    }

    /**
     * creates all the Vertices from nodes.
     *
     */
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
                        type = Vertex.Vertextype.Stairs;
                        break;
                    default:
                        break;
                }

                final LatLng t1 = n.getLatLng();
                final String t3 = n.getType();
                final String t2 = n.getNodeId();
                final int t4 = n.getFloor();


                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
//                         MapDrawer.addMarker(t1, t3 + ": " + t2, t4);
                    }
                });

                graph.addVertex(n.getNodeId(), n.getLatLng().latitude, n.getLatLng().longitude, type, n.getFloor());
            }
        }
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                MapDrawer.hideMarkers();
                MapDrawer.showMarkersFloor(0);
            }
        });
    }

    /**
     * adds edges to the vertices and calculates the distances of those edges.
     */
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
//                       MapDrawer.addPolyline(t1, t2, t3, t4, Color.BLACK, nNew.getFloor());
                    }
                });
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

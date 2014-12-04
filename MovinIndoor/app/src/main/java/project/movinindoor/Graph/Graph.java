package project.movinindoor.Graph;

import android.graphics.Color;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import project.movinindoor.MapDrawer;
import project.movinindoor.MapsActivity;

/**
 * Created by Wietse on 24-11-2014.
 */
public class Graph {

    public static final double INFINITY = Double.MAX_VALUE;
    private Map<String, Vertex> vertexMap = new HashMap<String, Vertex>();
    private int Cost;


    //function to add edges, sourcename is the source of the edge and the destname will be the destination of the edge.
    //the edge will be added to the vertex.adj list of the sourcename vertex.
    public void addEdge(String sourcename, String destname, double cost) {
        Vertex v = vertexMap.get(sourcename);
        Vertex v2 = vertexMap.get(destname);
        v.adj.add(new Edge(v2, cost));
    }

    //function to add vertex to the graph. a vertex has a name which will be the way to later get your vertex back, and a position; latitude and longitude.
    public void addVertex(String name, double lat1, double long1) {
        Vertex v = new Vertex(name, lat1, long1);
        vertexMap.put(name, v);
    }

    //function to reset all vertices of their 'prev' 'dist' and 'scratch' value (dist gets set to INFINITY)
    private void clearAll() {
        for (Vertex v : vertexMap.values()) {
            v.reset();
        }
    }

    //function that you can run after running dijkstra, to get a list of the shortest path to that destination
    private void printPath(String destname) {
        Vertex dest = vertexMap.get(destname);
    }

    private String printPath(Vertex dest) {
        if (dest.prev != null) {
            return printPath(dest.prev) + " -> " + dest.name;
        }
        return dest.name;
    }

    private LinkedList getPath(String destname) {
        LinkedList a = new LinkedList();
        a = getPath(vertexMap.get(destname), a);
        return a;
    }

    private LinkedList getPath(Vertex v, LinkedList l) {
        l.add(0, v);
        if (v.prev != null) {
            l = getPath(v.prev, l);
        }
        return l;
    }

    public double getCost(String destName) {
        Vertex v = vertexMap.get(destName);
        return v.dist;
    }


    //function that verifies if the strings are in the hashmap, and runs the private drawPath function.
    //returns the cost of the path.
    private void drawPath(Vertex v) {
        MapDrawer.addPolyline(v.lat1, v.long1, v.prev.lat1, v.prev.long1, Color.BLUE);
        if (v.prev.prev != null) {
            drawPath(v.prev);
        } else {
            MapsActivity.getMap().moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(v.lat1, v.long1), 20));
        }
    }

    //function that verifies if the strings are in the hashmap, and runs the private drawPath function.
    //returns the cost of the path.
    public double drawPath(String startName, String destName) {
        if (!startName.equals(destName)) {
            dijkstra(startName);
            Vertex v = vertexMap.get(destName);
            if (v != null) {
                if (v.prev != null) {
                    drawPath(v);
                    return v.dist;
                }else{
                    Toast.makeText(MapsActivity.getContext().getApplicationContext(), "couldn't find a path to the destination", Toast.LENGTH_LONG).show();
                    return 0.0;
                }
            } else {
                Toast.makeText(MapsActivity.getContext().getApplicationContext(), "end vertex was not found", Toast.LENGTH_LONG).show();
                return 0.0;
            }
        } else {
            Toast.makeText(MapsActivity.getContext().getApplicationContext(), "Start and end are equal", Toast.LENGTH_SHORT).show();
            return 0.0;
        }
    }


    public void dijkstra(String startName) {
        PriorityQueue<Path> pq = new PriorityQueue<Path>();

        Vertex start = vertexMap.get(startName);
        if (start != null) {
            clearAll();
            pq.add(new Path(start, 0));
            start.dist = 0;

            int nodesSeen = 0;
            while (!pq.isEmpty() && nodesSeen < vertexMap.size()) {
                Path vrec = pq.remove();
                Vertex v = vrec.getDest();
                if (v.scratch) // already processed v
                {
                    continue;
                }
                v.scratch = true;
                nodesSeen++;

                for (Edge e : v.adj) {
                    Vertex w = e.dest;
                    double cvw = e.cost;

                    if (w.dist > v.dist + cvw) {
                        w.dist = v.dist + cvw;
                        w.prev = v;
                        pq.add(new Path(w, w.dist));
                    }
                }
            }
        } else {
            Toast.makeText(MapsActivity.getContext().getApplicationContext(), "start vertex was not found", Toast.LENGTH_LONG).show();
        }
    }


    public static String calculateWalkingSpeed(double cost) {
        int walkingSpeed = 5000; //Walking speed in meters per hour
        int minuteInSec = 3600;
        float walkingspeedPerSecond = ((float) walkingSpeed) / minuteInSec;
        double time;
        time = (cost / walkingspeedPerSecond);
        int minute = (int) time / 60;
        int second = (int) time % 60;
        return String.format("%dm%02ds", minute, second);
    }

}
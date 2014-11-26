package project.movinindoor.Graph;

import android.graphics.Color;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import project.movinindoor.MapsActivity;

/**
 * Created by Wietse on 24-11-2014.
 */
public class Graph {

    public static final double INFINITY = Double.MAX_VALUE;
    private Map<String, Vertex> vertexMap = new HashMap<String, Vertex>();
    private static int Cost;

    public void addEdge(String sourcename, String destname, double cost) {
        Vertex v = vertexMap.get(sourcename);
        Vertex v2 = vertexMap.get(destname);
        v.adj.add(new Edge(v2, cost));
    }

    public void addVertex(String name, double lat1, double long1) {
        Vertex v = new Vertex(name, lat1, long1);
        vertexMap.put(name, v);
    }

    private void clearAll() {
        for (Vertex v : vertexMap.values()) {
            v.reset();
        }
    }

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

    public double getCost(String destName){
        Vertex v =  vertexMap.get(destName);
        return v.dist;
    }

    private void drawPath(Vertex v){
        MapsActivity.addPolyline(v.lat1, v.long1, v.prev.lat1, v.prev.long1, Color.BLUE);
        if(v.prev.prev != null){
            drawPath(v.prev);
        }
    }

    public double drawPath(String startName, String destName){
        dijkstra(startName);
        drawPath(vertexMap.get(destName));
        return vertexMap.get(destName).dist;
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
            Log.i("FAILED", "start vertex was not found");
        }
    }



    public static String calculateWalkingSpeed() {
        int walkingSpeed = 4000; //Walking speed in meters per hour
        int minuteInSec = 3600;
        float walkingspeedPerSecond = ((float)walkingSpeed)/ minuteInSec;
        double time;
        time = (Cost / walkingspeedPerSecond);
        int minute =  (int) time /60;
        int second = (int) time % 60;
        return String.format("%d:%02d", minute, second);
    }

}
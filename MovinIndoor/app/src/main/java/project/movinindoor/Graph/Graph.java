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
    private int Cost;

    //function to add edges, sourcename is the source of the edge and the destname will be the destination of the edge.
    //the edge will be added to the vertex.adj list.
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
    public LinkedList getPath(String destname) {
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

    //function to get the cost of a dest
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

    //function that verifies if the strings are in the hashmap, and runs the private drawPath function.
    //returns the cost of the path.
    public double drawPath(String startName, String destName){
        dijkstra(startName);
        Vertex v = vertexMap.get(destName);
        if(v != null) {
            drawPath(v);
            return v.dist;
        }else{
            Log.i("PathError", "end vertex was not found");
        }
        return 0.0;
    }

    //runs dijkstra from the parameter startname.
    //
    public void dijkstra(String startName) {
        //creating a priorityqueue path so we can always continue creating new paths from the shortest unchecked path
        PriorityQueue<Path> pq = new PriorityQueue<Path>();

        //resetting all the variables that are made by a previous pathing algorithm
        //also add the given start vertex to the queue
        Vertex start = vertexMap.get(startName);
        if (start != null) {
            clearAll();
            pq.add(new Path(start, 0));
            start.dist = 0;
            int nodesSeen = 0;

            //for every vertex on the queue we calculate the distances to connected vertices.
            //if this is shorter than the already set distance we replace the cost.
            //for every connected edge to the vertex we place them on the queue.
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
            Log.i("PathError", "start vertex was not found");
        }
    }



    public static String calculateWalkingSpeed(double cost) {
        int walkingSpeed = 5000; //Walking speed in meters per hour
        int minuteInSec = 3600;
        float walkingspeedPerSecond = ((float)walkingSpeed)/ minuteInSec;
        double time;
        time = (cost / walkingspeedPerSecond);
        int minute =  (int) time /60;
        int second = (int) time % 60;
        return String.format("%dm%02ds", minute, second);
    }

}
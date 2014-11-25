package project.movinindoor.Graph;

import android.util.Log;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.PriorityQueue;

/**
 * Created by Wietse on 24-11-2014.
 */
public class Graph {

    public static final double INFINITY = Double.MAX_VALUE;
    private Map<String, Vertex> vertexMap = new HashMap<String, Vertex> ();

    public void addEdge(String sourcename, String destname, double cost) {
        Vertex v = vertexMap.get(sourcename);
        Vertex v2 = vertexMap.get(destname);
        v.adj.add(new Edge(v2, cost));
    }

    public void addVertex(String name) {
        Vertex v = new Vertex(name);
        vertexMap.put(name, v);
    }

    public String printPath(Vertex dest) {
//        if (dest.prev != null) {
//            printPath(dest.prev);
//            System.out.print(" -> ");
//        }
//        System.out.print(dest.name);

        if(dest.prev != null){
            return printPath(dest.prev) +  " -> " + dest.name;
        }
        return dest.name;
    }

    public void printPath(String destname) {
        Vertex dest = vertexMap.get(destname);
        Log.i("PATH", printPath(dest));
    }

    private void clearAll() {
        for(Vertex v : vertexMap.values()){
            v.reset();
        }
    }

    public void printEdges() {

    }

    public void printVertexes() {
        for (Vertex v : vertexMap.values()) {
            System.out.print(v.name + " ");
        }
        System.out.println("");
    }

    //    public void dijkstra(String startName) {
//        PriorityQueue<Path> pq = new PriorityQueue<Path>();
//
//        Vertex start = vertexMap.get(startName);
//        if (start != null) {
//            clearAll();
//            pq.add(new Path(start, 0));
//            start.dist = 0;
//
//            int nodesSeen = 0;
//            while (!pq.isEmpty() && nodesSeen < vertexMap.size()) {
//                Path vrec = pq.remove();
//                Vertex v = vrec.getDest();
//                if (v.scratch) // already processed v
//                {
//                    continue;
//                }
//                v.scratch = true;
//                nodesSeen++;
//
//                for (Edge e : v.adj) {
//                    Vertex w = e.dest;
//                    double cvw = e.cost;
//
//                    if (w.dist > v.dist + cvw) {
//                        w.dist = v.dist + cvw;
//                        w.prev = v;
//                        pq.add(new Path(w, w.dist));
//                    }
//                }
//            }
//        } else {
//            System.out.println("start vertex was not found");
//        }
//    }
    public void getShortestPath(String startName) {
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
            System.out.println("startvertex or endvertex was not found");
        }
    }
}
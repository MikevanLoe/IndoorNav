package project.movinindoor.Graph;

import java.util.LinkedList;
import java.util.List;

import project.movinindoor.Reparation.Reparation;

/**
 * Created by Wietse on 24-11-2014.
 */
public class Vertex {

    protected String name;
    protected List<Edge> adj;
    protected double dist;
    protected Vertex prev;
    protected boolean scratch;
    protected double lat1;
    protected double long1;

    public Vertex(String name, double lat1, double long1) {
        this.name = name;
        this.lat1 = lat1;
        this.long1 = long1;
        adj = new LinkedList<Edge>() ;
        reset();
    }

    public void reset(){
        dist = Graph.INFINITY;
        prev = null;
        scratch = false;
    }

}

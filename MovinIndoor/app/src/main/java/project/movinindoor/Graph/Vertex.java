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
    protected Reparation rep;

    public Vertex(String name) {
        this.name = name;
        adj = new LinkedList<Edge>() ;
        reset();
    }

    public Vertex(String name, Reparation rep) {
        this.name = name;
        this.rep = rep;
        adj = new LinkedList<Edge>() ;
        reset();
    }

    public void reset(){
        dist = Graph.INFINITY;
        prev = null;
//        pos = null;
        scratch = false;
    }


}

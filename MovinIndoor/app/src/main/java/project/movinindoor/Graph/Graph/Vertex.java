package project.movinindoor.Graph.Graph;

import java.util.LinkedList;
import java.util.List;

import project.movinindoor.Models.Elevator;
import project.movinindoor.Models.Stairs;

/**
 * Created by Wietse on 24-11-2014.
 */
public class Vertex {

    public String name;
    protected List<Edge> adj;
    protected double dist;
    public Vertex prev;
    protected boolean scratch;
    protected double lat1;
    protected double long1;
    public enum Vertextype {Elevator, Stairs, Room, Hall};
    protected Vertextype type;
    protected int Floor;

    public Vertex(String name, double lat1, double long1) {
        this.name = name;
        this.lat1 = lat1;
        this.long1 = long1;
        adj = new LinkedList<Edge>() ;
        reset();
    }

    public Vertex(String name, double lat1, double long1, Vertextype type, int floor){
        this.name = name;
        this.lat1 = lat1;
        this.long1 = long1;
        this.type = type;
        this.Floor = floor;
        adj = new LinkedList<Edge>();
        reset();
    }

    public void reset(){
        dist = Graph.INFINITY;
        prev = null;
        scratch = false;
    }

}

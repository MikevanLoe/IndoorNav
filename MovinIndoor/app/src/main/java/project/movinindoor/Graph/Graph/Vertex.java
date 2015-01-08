package project.movinindoor.Graph.Graph;

import com.google.android.gms.maps.model.LatLng;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Wietse on 24-11-2014.
 */
public class Vertex {

    //basic data for the class
    public String NodeId;
    protected LatLng latLng;
    protected int floor;
    public List<Edge> adj;
    protected Vertextype type;
    public enum Vertextype {Elevator, Stairs, Room, Hall};


    //data used to determine shortest path
    protected double dist;
    public Vertex prev;
    protected boolean scratch;

    public Vertex(String name, LatLng latLng) {
        this.NodeId = name;
        this.latLng = latLng;
        adj = new LinkedList<Edge>() ;
        reset();
    }

    public Vertex(String name, LatLng latLng, Vertextype type, int floor){
        this.NodeId = name;
        this.latLng = latLng;
        this.type = type;
        this.floor = floor;
        adj = new LinkedList<Edge>();
        reset();
    }

    public void reset(){
        dist = Graph.INFINITY;
        prev = null;
        scratch = false;
    }

    public String getName() {
        return NodeId;
    }

    public List<Edge> getAdj() {
        return adj;
    }

    public Vertex getPrev() {
        return prev;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public int getFloor() {
        return floor;
    }
}

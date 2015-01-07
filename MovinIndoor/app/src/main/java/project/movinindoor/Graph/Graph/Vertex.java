package project.movinindoor.Graph.Graph;

import com.google.android.gms.maps.model.LatLng;

import java.util.LinkedList;
import java.util.List;

import project.movinindoor.Models.Elevator;
import project.movinindoor.Models.Stairs;

/**
 * Created by Wietse on 24-11-2014.
 */
public class Vertex {

    public String name;
    public List<Edge> adj;
    protected double dist;
    public Vertex prev;
    protected boolean scratch;
    protected LatLng latLng;
    public enum Vertextype {Elevator, Stairs, Room, Hall};
    protected Vertextype type;
    protected int floor;

    public Vertex(String name, LatLng latLng) {
        this.name = name;
        this.latLng = latLng;
        adj = new LinkedList<Edge>() ;
        reset();
    }

    public Vertex(String name, LatLng latLng, Vertextype type, int floor){
        this.name = name;
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
        return name;
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

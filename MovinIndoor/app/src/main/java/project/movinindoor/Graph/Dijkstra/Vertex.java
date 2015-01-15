package project.movinindoor.Graph.Dijkstra;

import com.google.android.gms.maps.model.LatLng;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Wietse on 24-11-2014.
 */

/**
 * vertex is a class that has points on the map
 * A Vertex has a location (latLng) links to other Vertices (adj) and a type (Elevator, Stairs, Room or Hall)
 */
public class Vertex {

    public String VertexId;
    protected LatLng latLng;
    protected int floor;
    public List<Edge> adj;
    protected Vertextype type;
    public enum Vertextype {Elevator, Stairs, Room, Hall};


    /**
     * data used to determine shortest path
     */
    protected double dist;
    public Vertex prev;
    protected boolean scratch;

    /**
     *
     * @param VertexId number of the vertex
     * @param latLng latitude and longitude of the vertex
     * @param type type of the vertex that can be Elevator, Room, Stairs or Hall
     * @param floor the floor of the vertex
     */
    public Vertex(String VertexId, LatLng latLng, Vertextype type, int floor){
        this.VertexId = VertexId;
        this.latLng = latLng;
        this.type = type;
        this.floor = floor;
        adj = new LinkedList<Edge>();
        reset();
    }

    /**
     * function to reset all the data used by Dijkstra
     */
    public void reset(){
        dist = Graph.INFINITY;
        prev = null;
        scratch = false;
    }

    /**
     * getters and setters
     */
    public String getVertexId() {
        return VertexId;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public int getFloor() {
        return floor;
    }
}

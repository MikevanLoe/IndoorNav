package project.movinindoor.Graph.Graph;

import java.util.ArrayList;

import project.movinindoor.Graph.edgeActions;

/**
 * Created by Wietse on 24-11-2014.
 * class that describes a link from vertex to vertex
 */
public class Edge {
    protected Vertex dest;
    protected double cost;
    protected ArrayList<edgeActions> actions;

    /**
     *
     * @param v the vertex it links to
     * @param cost the cost of the link
     * @param actions the actions
     */
    public Edge(Vertex v, double cost, ArrayList<edgeActions> actions) {
        dest = v;
        this.cost = cost;
        this.actions = actions;
    }

    /**
     *
     * @return Arraylist of edgeactions
     */
    public ArrayList<edgeActions> getActions() {
        return actions;
    }
}

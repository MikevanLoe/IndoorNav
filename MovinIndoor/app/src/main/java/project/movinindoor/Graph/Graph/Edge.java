package project.movinindoor.Graph.Graph;

import java.util.ArrayList;

import project.movinindoor.Graph.edgeActions;

/**
 * Created by Wietse on 24-11-2014.
 */
public class Edge {
    protected Vertex dest;
    protected double cost;
    protected ArrayList<edgeActions> actions;

    public Edge(Vertex d, double c, ArrayList<edgeActions> actions) {
        dest = d;
        cost = c;
        this.actions = actions;
    }
}

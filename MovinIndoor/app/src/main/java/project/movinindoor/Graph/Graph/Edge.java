package project.movinindoor.Graph.Graph;

/**
 * Created by Wietse on 24-11-2014.
 */
public class Edge {
    protected Vertex dest;
    protected double cost;

    public Edge(Vertex d, double c) {
        dest = d;
        cost = c;
    }
}

package project.movinindoor.Graph;

import java.util.ArrayList;

/**
* Created by Davey on 25-11-2014.
* describes links from nodes to nodes
*/
public class ToNode {

    private String toNodeID;
    private double cost;
    private ArrayList<edgeActions> actions;

    /**
     *
     * @param toNodeID the node this class links to
     * @param cost the cost in meters
     * @param actions class that describes all the actions this specific class has
     */
    public ToNode(String toNodeID, double cost, ArrayList actions) {
        this.cost = cost;
        this.toNodeID = toNodeID;
        this.actions = actions;
    }

    /**
     *
     * simple getters and setters
     */
    public String getToNodeID() {
        return toNodeID;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public ArrayList<edgeActions> getActions() {
        return actions;
    }
}

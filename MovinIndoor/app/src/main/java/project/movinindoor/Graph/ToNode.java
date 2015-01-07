package project.movinindoor.Graph;

import java.util.ArrayList;

/**
* Created by Davey on 25-11-2014.
        */
public class ToNode {

    private String toNodeID;
    private double cost;
    private ArrayList<edgeActions> actions;

    public ToNode(String toNodeID, double cost, ArrayList actions) {
        this.cost = cost;
        this.toNodeID = toNodeID;
        this.actions = actions;
    }

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

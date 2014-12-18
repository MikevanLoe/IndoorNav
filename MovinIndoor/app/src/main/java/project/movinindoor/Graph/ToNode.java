package project.movinindoor.Graph;

import java.util.ArrayList;

/**
* Created by Davey on 25-11-2014.
        */
public class ToNode {

    public String toNodeID;
    public double cost;
    public ArrayList<edgeActions> actions;

    public ToNode(String toNodeID, double cost, ArrayList actions) {
        this.cost = cost;
        this.toNodeID = toNodeID;
        this.actions = actions;
    }
}

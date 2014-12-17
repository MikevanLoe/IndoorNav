package project.movinindoor.Graph;

import java.util.List;

/**
 * Created by Davey on 25-11-2014.
 */
public class Node {

    public String nodeId;
    public List<Double> location;
    public String floor;
    public List<ToNode> toNode;
    public String type;

    public Node(String nodeId, List<Double> location, String floor, List<ToNode> toNode, String type) {
        this.nodeId = nodeId;
        this.location = location;
        this.floor = floor;
        this.toNode = toNode;
        this.type = type;
    }


}

package project.movinindoor.Graph;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

/**
 * Created by Davey on 25-11-2014.
 */
public class Node {

    private String nodeId;
    private LatLng latLng;
    private int floor;
    private List<ToNode> toNode;
    private String type;

    /**
     *
     * @param nodeId the id the node has
     * @param latLng the location of the node
     * @param floor the floor of the node
     * @param toNode describes which connections this node has
     * @param type the type of the node. can be Hall, Elevator, Room or Stairs
     */
    public Node(String nodeId, LatLng latLng, int floor, List<ToNode> toNode, String type) {
        this.nodeId = nodeId;
        this.latLng = latLng;
        this.floor = floor;
        this.toNode = toNode;
        this.type = type;
    }


    /**
     *
     * simple getters for all the variables
     */
    public String getNodeId() {
        return nodeId;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public int getFloor() {
        return floor;
    }

    public List<ToNode> getToNode() {
        return toNode;
    }

    public String getType() {
        return type;
    }
}

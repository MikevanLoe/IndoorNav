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

    public Node(String nodeId, LatLng latLng, int floor, List<ToNode> toNode, String type) {
        this.nodeId = nodeId;
        this.latLng = latLng;
        this.floor = floor;
        this.toNode = toNode;
        this.type = type;
    }

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

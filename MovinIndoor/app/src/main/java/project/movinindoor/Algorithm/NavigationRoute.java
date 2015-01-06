package project.movinindoor.Algorithm;

import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import project.movinindoor.Graph.Graph.Edge;
import project.movinindoor.Graph.Graph.Graph;
import project.movinindoor.Graph.Graph.Vertex;
import project.movinindoor.Graph.Node;
import project.movinindoor.Graph.ToNode;
import project.movinindoor.Graph.edgeActions;
import project.movinindoor.MapDrawer;
import project.movinindoor.MapsActivity;
import project.movinindoor.R;

/**
 * Created by Davey on 18-12-2014.
 */
public class NavigationRoute {

    private int num = 0;
    private int rotation = 0;
    private LinkedList<RouteStep> linkedList;

    public NavigationRoute() {
        if(tempMarker != null) tempMarker.remove();
        if(linkedList != null) {
            linkedList.remove();
            linkedList.clear();
        }
        num = 0;
        HashMap<String, Node> nodes = MapsActivity.setupGraph.getNodes().jsonList;
        linkedList = new LinkedList<>();
        for (int v = Graph.walkingPath.size() -1 ; v >= 0; v--) {
            Vertex v2 = Graph.walkingPath.get(v);
            /*
            for (Edge e : v2.adj) {
                if(v2.name) {

                }
                e.actions
            }
            */
            Node n = nodes.get(Graph.walkingPath.get(v).name);
            for (ToNode tn : n.toNode) {
                for (edgeActions e : tn.actions) {
                    if(Graph.walkingPath.get(v).prev.prev != null) {
                        if (e.toNodeID == Integer.valueOf(nodes.get(Graph.walkingPath.get(v).prev.prev.name).nodeId)) {
                            String text = "";
                            String action = "";
                            Node nq = nodes.get(tn.toNodeID);
                            switch (e.action) {
                                case "GoStraight":
                                    text = "Ga rechtdoor";
                                    action = "GoStraight";
                                    break;
                                case "GoRight":
                                    text = "Ga naar links";
                                    action = "GoLeft";
                                    break;
                                case "GoLeft":
                                    text = "Ga naar rechts";
                                    action = "GoRight";
                                    break;
                                case "GoSlightlyRight":
                                    text = "Ga schuin naar links";
                                    action = "GoSlightlyLeft";
                                    break;
                                case "GoSlightlyLeft":
                                    text = "Ga schuin naar rechts";
                                    action = "GoSlightlyRight";
                                    break;
                            }
                            RouteStep routeStep = new RouteStep(action, text, new LatLng(nq.location.get(0), nq.location.get(1)), Integer.valueOf(nq.floor));
                            linkedList.add(routeStep);
                        }
                    }
                }
            }
        }


        RouteStep routeStep = new RouteStep("GoLeft", "U bent gearriveerd", linkedList.getLast().getLatLng(), linkedList.getLast().getFloor());
        linkedList.add(routeStep);

        switch (linkedList.getFirst().getAction()) {
            case "GoRight":
                rotation = rotation - 90;
                break;
            case "GoLeft":
                rotation = rotation - 180;
                break;
            case "GoSlightlyRight":
                rotation = rotation + 35;
                break;
            case "GoSlightlyLeft":
                rotation = rotation - 135;
                break;
        }
    }

    public LinkedList<RouteStep> getLinkedList() {
        return linkedList;
    }

    public void setLinkedList(LinkedList<RouteStep> linkedList) {
        this.linkedList = linkedList;
    }

    Marker tempMarker = null;
    public String getNextCard() {
        if(tempMarker != null) tempMarker.remove();
        String s = linkedList.get(num).getAction() + ", " + linkedList.get(num).getText();
        if (num < linkedList.size()) {

        switch (linkedList.get(num).getAction()) {
            case "GoRight":
                rotation = rotation + 90;
                break;
            case "GoLeft":
                rotation = rotation - 90;
                break;
            case "GoSlightlyRight":
                rotation = rotation + 45;
                break;
            case "GoSlightlyLeft":
                rotation = rotation - 45;
                break;
        }
            tempMarker = MapsActivity.getMap().addMarker(new MarkerOptions().position(linkedList.get(num).getLatLng()).title(linkedList.get(num).getText()));
            if(MapDrawer.getFloor() != linkedList.get(num).getFloor()) {
                MapDrawer.setFloor(linkedList.get(num).getFloor());
                MapDrawer.hidePolylines();
                MapDrawer.showPolylinesFloor(linkedList.get(num).getFloor());
                MapDrawer.showPolylinesFloorNav(linkedList.get(num).getFloor());
                MapsActivity.btnCurrentFloor.setText(String.valueOf(linkedList.get(num).getFloor()));
            }

            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(linkedList.get(num).getLatLng())      // Sets the center of the map to Mountain View
                    .zoom(20)                   // Sets the zoom
                    .bearing(rotation % 360)               // Sets the orientation of the camera to east
                    .tilt(45)                   // Sets the tilt of the camera to 30 degrees
                    .build();                   // Creates a CameraPosition from the builder
            MapsActivity.getMap().animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            num++;
        } else {
            linkedList.clear();
            linkedList.remove();
        }

        return s;
    }

    public int getNum() {
        return num;
    }
}

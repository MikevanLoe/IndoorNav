package project.movinindoor.Algorithm;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;
import java.util.LinkedList;

import project.movinindoor.Graph.Graph.Graph;
import project.movinindoor.Graph.Graph.Vertex;
import project.movinindoor.Graph.Node;
import project.movinindoor.Graph.ToNode;
import project.movinindoor.Graph.edgeActions;
import project.movinindoor.MapDrawer;
import project.movinindoor.MapsActivity;

/**
 * Created by Davey on 18-12-2014.
 */
public class NavigationRoute {

    private int num = 0;
    private int rotation = 0;
    private LinkedList<RouteStep> linkedList;

    public NavigationRoute() {
        if (tempMarker != null) tempMarker.remove();

        num = 0;
        HashMap<String, Node> nodes = MapsActivity.getSetupGraph().getNodes().jsonList;
        linkedList = new LinkedList<>();

        for (int v = Graph.getWalkingPath().size() - 1; v >= 0; v--) {
            Vertex v2 = Graph.getWalkingPath().get(v);
            /*
            for (Edge e1 : v2.adj) {
                for (edgeActions e : e1.actions) {
                    if (v2.NodeId) {
                        String text = "";
                        String action = "";
                        Node nq = nodes.get(e1.toNodeID);
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
            RouteStep routeStep = new RouteStep("GoLeft", "U bent gearriveerd", linkedList.getLast().getLatLng(), linkedList.getLast().getFloor());
            linkedList.add(routeStep);
*/
            Node n = nodes.get(Graph.getWalkingPath().get(v).getNodeId());
            for (ToNode tn : n.getToNode()) {
               // Log.i("V", "=>");
                for (edgeActions e : tn.getActions()) {
                   // Log.i("V", "====>");
                    if (Graph.getWalkingPath().get(v).prev.prev != null) {
                       // Log.i("V", "=======>");
                        if (e.getToNodeID() == Integer.valueOf(nodes.get(Graph.getWalkingPath().get(v).prev.prev.NodeId).getNodeId())) {
                            String text = "";
                            String action = "";
                            Node nq = nodes.get(tn.getToNodeID());
                            switch (e.getAction()) {
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
                            RouteStep routeStep = new RouteStep(action, text, nq.getLatLng(), Integer.valueOf(nq.getFloor()));
                            linkedList.add(routeStep);

                        }
                    }
                }
            }
        }



        if(linkedList.getLast() != null) {
            RouteStep routeStep = new RouteStep("GoStraight", "U bent gearriveerd", linkedList.getLast().getLatLng(), linkedList.getLast().getFloor());
            linkedList.add(routeStep);
        }

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

    Marker tempMarker = null;

    public String getNextCard() {
        if (tempMarker != null) tempMarker.remove();
        String s = linkedList.get(num).getAction() + ", " + linkedList.get(num).getText();
        if (num == linkedList.size()) num += -1;
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
            if (MapDrawer.getFloor() != linkedList.get(num).getFloor()) {
                MapDrawer.setFloor(linkedList.get(num).getFloor());
                MapDrawer.hideAllMarkersAndPolylines();
                MapDrawer.showMarkersAndPolylinesFloor(linkedList.get(num).getFloor());
                MapsActivity.getBtnCurrentFloor().setText(String.valueOf(linkedList.get(num).getFloor()));
            }

            if(num != linkedList.size() - 1) {
                double startLat = Math.toRadians(linkedList.get(num).getLatLng().latitude);
                double startLong = Math.toRadians(linkedList.get(num).getLatLng().longitude);
                double endLat = Math.toRadians(linkedList.get(num + 1).getLatLng().latitude);
                double endLong = Math.toRadians(linkedList.get(num + 1).getLatLng().longitude);

                double dLong = endLong - startLong;

                double dPhi = Math.log(Math.tan(endLat/2.0+Math.PI/4.0)/Math.tan(startLat/2.0+Math.PI/4.0));
                if (Math.abs(dLong) > Math.PI) {
                    if (dLong > 0.0) {
                        dLong = -(2.0 * Math.PI - dLong);
                    } else {
                        dLong = (2.0 * Math.PI + dLong);
                    }
                }

                double bearing = (Math.toDegrees(Math.atan2(dLong, dPhi)) + 360.0) % 360.0;

                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(linkedList.get(num).getLatLng())      // Sets the center of the map to Mountain View
                        .zoom(20)                   // Sets the zoom
                        .bearing((float) bearing)               // Sets the orientation of the camera to east
                        .tilt(50)                   // Sets the tilt of the camera to 30 degrees
                        .build();                   // Creates a CameraPosition from the builder
                MapsActivity.getMap().animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }


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

    public void reset() {
        num = 0;
        linkedList.clear();
        if(tempMarker != null) tempMarker.remove();
    }
}

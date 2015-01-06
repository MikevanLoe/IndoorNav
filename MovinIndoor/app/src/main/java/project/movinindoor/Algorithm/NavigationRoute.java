package project.movinindoor.Algorithm;

import android.location.LocationManager;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
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
        HashMap<String, Node> nodes = MapsActivity.setupGraph.getNodes().jsonList;
        linkedList = new LinkedList<>();

        for (int v = Graph.walkingPath.size() - 1; v >= 0; v--) {
            Vertex v2 = Graph.walkingPath.get(v);
            /*
            for (Edge e1 : v2.adj) {
                for (edgeActions e : e1.actions) {
                    if (v2.name) {
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
            Node n = nodes.get(Graph.walkingPath.get(v).name);
            for (ToNode tn : n.toNode) {
               // Log.i("V", "=>");
                for (edgeActions e : tn.actions) {
                   // Log.i("V", "====>");
                    if (Graph.walkingPath.get(v).prev.prev != null) {
                       // Log.i("V", "=======>");
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
                MapDrawer.hidePolylines();
                MapDrawer.showPolylinesFloor(linkedList.get(num).getFloor());
                MapDrawer.showPolylinesFloorNav(linkedList.get(num).getFloor());
                MapsActivity.btnCurrentFloor.setText(String.valueOf(linkedList.get(num).getFloor()));
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

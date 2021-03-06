package project.movinindoor.Algorithm;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;
import java.util.LinkedList;

import project.movinindoor.Graph.Dijkstra.Graph;
import project.movinindoor.Graph.Dijkstra.Vertex;
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
    private LinkedList<RouteStep> LinkedList;

    /**
     * this class is to show the navigation cards
     * constructor for NavigationRoute
     *
     */
    public NavigationRoute() {
        if (tempMarker != null) tempMarker.remove();

        num = 0;
        HashMap<String, Node> nodes = MapsActivity.getSetupGraph().getNodes().jsonList;
        LinkedList = new LinkedList<>();

        for (int v = Graph.getWalkingPath().size() - 1; v >= 0; v--) {
            Vertex v2 = Graph.getWalkingPath().get(v);
            Node n = nodes.get(Graph.getWalkingPath().get(v).getVertexId());
            for (ToNode tn : n.getToNode()) {

                for (edgeActions e : tn.getActions()) {

                    if (Graph.getWalkingPath().get(v).prev.prev != null) {
                        if (e.getToNodeID() == Integer.valueOf(nodes.get(Graph.getWalkingPath().get(v).prev.prev.VertexId).getNodeId())) {
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
                            LinkedList.add(routeStep);

                        }
                    }
                }
            }
        }



        if(LinkedList.getLast() != null) {
            RouteStep routeStep = new RouteStep("GoStraight", "U bent gearriveerd", LinkedList.getLast().getLatLng(), LinkedList.getLast().getFloor());
            LinkedList.add(routeStep);
        }

        switch (LinkedList.getFirst().getAction()) {
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
        return LinkedList;
    }

    Marker tempMarker = null;

    /**
     *
     * @return returns the string of the next card
     */
    public String getNextCard() {
        if (tempMarker != null) tempMarker.remove();
        String s = LinkedList.get(num).getAction() + ", " + LinkedList.get(num).getText();
        if (num < LinkedList.size()) {

            switch (LinkedList.get(num).getAction()) {
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
            tempMarker = MapsActivity.getMap().addMarker(new MarkerOptions().position(LinkedList.get(num).getLatLng()).title(LinkedList.get(num).getText()));
            if (MapDrawer.getFloor() != LinkedList.get(num).getFloor()) {
                MapDrawer.setFloor(LinkedList.get(num).getFloor());
                MapDrawer.hideAllMarkersAndPolylines();
                MapDrawer.showMarkersAndPolylinesFloor(LinkedList.get(num).getFloor());
                MapsActivity.getBtnCurrentFloor().setText(String.valueOf(LinkedList.get(num).getFloor()));
            }

            if(num != LinkedList.size() - 1) {
                double startLat = Math.toRadians(LinkedList.get(num).getLatLng().latitude);
                double startLong = Math.toRadians(LinkedList.get(num).getLatLng().longitude);
                double endLat = Math.toRadians(LinkedList.get(num + 1).getLatLng().latitude);
                double endLong = Math.toRadians(LinkedList.get(num + 1).getLatLng().longitude);

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
                        .target(LinkedList.get(num).getLatLng())      // Sets the center of the map to Mountain View
                        .zoom(20)                   // Sets the zoom
                        .bearing((float) bearing)               // Sets the orientation of the camera to east
                        .tilt(50)                   // Sets the tilt of the camera to 30 degrees
                        .build();                   // Creates a CameraPosition from the builder
                MapsActivity.getMap().animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }


            num++;

        } else {
            LinkedList.clear();
            LinkedList.remove();
        }

        return s;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public void reset() {
        num = 0;
        LinkedList.clear();
        if(tempMarker != null) tempMarker.remove();
    }
}

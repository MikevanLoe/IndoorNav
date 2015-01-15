package project.movinindoor.Readers;


import android.graphics.Color;
import android.util.JsonReader;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import project.movinindoor.CalcMath;
import project.movinindoor.Graph.Node;
import project.movinindoor.Graph.ToNode;
import project.movinindoor.Graph.edgeActions;
import project.movinindoor.MapDrawer;
import project.movinindoor.MapsActivity;
import project.movinindoor.Models.Room;

/**
 * Created by Davey on 25-11-2014.
 */
public class NodeReader {

    public HashMap<String, Node> jsonList;

    /**
     * constructor for nodereader
     * this reads the nodes and creates a jsonlist from the wrs server of movinsoftware.nl
     */
    public NodeReader() {
        InputStream inputStream = null;

        try {
            URL url = new URL("http://wrs.movinsoftware.nl/?service=wrs&version=1.0.0&request=GetNavigationGrid&mapid=00W");
            //inputStream = url.openStream();
            inputStream = MapsActivity.getContext().getAssets().open("WindesheimNavMesh.json");
            HashMap<String, Node> read = readJsonStream(inputStream);
            jsonList = calculate(read);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * calculate all the distances for every tonode
     *
     * @param read a hashmap of nodes without distances in the tonode
     * @return a hashmap of nodes with meters in the tonode
     */
    public HashMap<String, Node> calculate(HashMap<String, Node> read) {
        double lat1 = 0.0;
        double long1 = 0.0;
        double lat2 = 0.0;
        double long2 = 0.0;

        for (Node n : read.values()) {

            lat1 = n.getLatLng().latitude;
            long1 = n.getLatLng().longitude;


            for (ToNode tN : n.getToNode()) {
                lat2 = read.get(tN.getToNodeID()).getLatLng().latitude;
                long2 = read.get(tN.getToNodeID()).getLatLng().longitude;
                double cost = CalcMath.measureMeters(lat1, long1, lat2, long2);
                tN.setCost(cost);
            }
        }
        return read;
    }

    /**
     * @param in the inputstream
     * @return a hashmap of nodes
     * @throws IOException
     */
    public HashMap<String, Node> readJsonStream(InputStream in) throws IOException {
        JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
        return readMessagesArray(reader);
    }

    /**
     * @param reader jsonreader
     * @return hashmap filled with nodes
     * @throws IOException
     */
    public HashMap<String, Node> readMessagesArray(JsonReader reader) throws IOException {
        HashMap<String, Node> nodes = new HashMap<String, Node>();

        reader.beginArray();
        while (reader.hasNext()) {
            Node n = readNodes(reader);
            nodes.put(n.getNodeId(), n);
        }
        reader.endArray();
        return nodes;
    }

    /**
     * @param reader jsonreader
     * @return returns the node the jsonreader encounters next
     * @throws IOException
     */
    public Node readNodes(JsonReader reader) throws IOException {
        String nodeID = "";
        LatLng latLng = null;
        int floor = 0;
        List<ToNode> nodeLinks = null;
        String type = "";

        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();

            switch (name) {
                case "nodeID":
                    nodeID = reader.nextString();
                    break;
                case "position":
                    latLng = readDoublesArray(reader);
                    break;
                case "floor":
                    floor = Integer.valueOf(reader.nextString());
                    break;
                case "nodeLinks":
                    List<ToNode> r = readNodeLinks(reader);
                    nodeLinks = r;
                    break;
                case "featureBaseType":
                    if (reader.peek().toString().equals("STRING")) {
                        type = reader.nextString();
                    } else {
                        reader.nextNull();
                    }
                    break;
                default:
                    reader.skipValue();
                    break;
            }
        }
        reader.endObject();
        return new Node(nodeID, latLng, floor, nodeLinks, type);
    }

    /**
     * @param reader jsonreader
     * @return returns the latlng the jsonreader encounters next
     * @throws IOException
     */
    public LatLng readDoublesArray(JsonReader reader) throws IOException {
        List<Double> doubles = new ArrayList();

        reader.beginArray();
        while (reader.hasNext()) {
            doubles.add(reader.nextDouble());
        }
        reader.endArray();
        return new LatLng(doubles.get(0), doubles.get(1));
    }

    /**
     * @param reader jsonreader
     * @return returns a list of tonodes
     * @throws IOException
     */
    public List<ToNode> readNodeLinks(JsonReader reader) throws IOException {
        List<ToNode> list = new ArrayList<ToNode>();
        String toNodeID = null;
        ArrayList<edgeActions> actions = null;
        String name;

        reader.beginArray();
        while (reader.hasNext()) {
            reader.beginObject();
            while (reader.hasNext()) {
                name = reader.nextName();
                if (name.equals("toNodeID")) {
                    toNodeID = reader.nextString();
                } else if (name.equals("edgeActions")) {
                    actions = readActions(reader);
                } else {
                    reader.skipValue();
                }
            }
            reader.endObject();
            list.add(new ToNode(toNodeID, 0, actions));
        }
        reader.endArray();
        return list;
    }

    /**
     * @param reader jsonreader
     * @return returns an arraylist of edgeactions the reader encounters next
     * @throws IOException
     */
    public ArrayList<edgeActions> readActions(JsonReader reader) throws IOException {
        String action = "";
        String toNodeID = "";
        String name;
        ArrayList<edgeActions> actions = new ArrayList<edgeActions>();
        reader.beginArray();
        while (reader.hasNext()) {
            reader.beginObject();
            while (reader.hasNext()) {
                name = reader.nextName();
                if (name.equals("action")) {
                    action = reader.nextString();
                } else if (name.equals("toNodeID")) {
                    toNodeID = reader.nextString();
                } else if (name.equals("turns")) {
                    reader.skipValue();
                } else {
                    reader.skipValue();
                }
            }
            reader.endObject();
            actions.add(new edgeActions(action, Integer.parseInt(toNodeID), 0, 0, 0, 0));
        }
        reader.endArray();
        return actions;
    }

    /**
     * function to find the nearest node near the given latitude and longitude on a specified floor
     *
     * @param latLng the location
     * @param floor  the floor
     * @return the nearest node to the given parameters
     */
    public Node findNearestNode(LatLng latLng, int floor) {
        double startLat1 = latLng.latitude;
        double startLong1 = latLng.longitude;

        double shortLat = 0.0;
        double shortLng = 0.0;
        Node tempNode = null;

        for (Node n : jsonList.values()) {
            if (n.getFloor() == floor) {
                double lat1 = n.getLatLng().latitude;
                double long1 = n.getLatLng().longitude;

                if (shortLng == 0.0 && shortLng == 0.0) {
                    shortLat = lat1;
                    shortLng = long1;
                }

                double som1 = CalcMath.measureMeters(startLat1, startLong1, lat1, long1);
                double som2 = CalcMath.measureMeters(startLat1, startLong1, shortLat, shortLng);

                if (som1 <= som2) {
                    shortLat = lat1;
                    shortLng = long1;
                    tempNode = n;
                }
            }
        }
        MapDrawer.addPolyline(shortLat, shortLng, startLat1, startLong1, Color.BLUE);
        return tempNode;
    }

    /**
     * function that finds the closest node inside aroom
     *
     * @param room the room you want to find a node in
     * @return the node in the room
     * returns null if there weas not a node found
     */
    public Node FindClosestNodeInsideRoom(Room room) {

        String s = room.getLocation();
        String l = s.split("\\.").toString();
        String floor = l.substring(1);
        for (Node node : jsonList.values()) {
            LatLng latLng1 = new LatLng(node.getLatLng().latitude, node.getLatLng().longitude);
            if (MapsActivity.getSetupGraph().getRooms().nodeInsideRoom(room, latLng1) && floor.equals(node.getFloor())) {
                return node;
            }
        }
        return null;
    }
}

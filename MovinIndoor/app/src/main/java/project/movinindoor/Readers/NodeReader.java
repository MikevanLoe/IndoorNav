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
import project.movinindoor.MapDrawer;
import project.movinindoor.MapsActivity;
import project.movinindoor.Models.Elevator;
import project.movinindoor.Models.Room;
import project.movinindoor.Models.Stair;

/**
 * Created by Davey on 25-11-2014.
 */
public class NodeReader {

    public HashMap<String, Node> jsonList;

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

    public HashMap<String, Node> calculate(HashMap<String, Node> read) {
        double lat1 = 0.0;
        double long1 = 0.0;
        double lat2 = 0.0;
        double long2 = 0.0;

        for (Node n : read.values()) {

            lat1 = n.location.get(0);
            long1 = n.location.get(1);


            for (ToNode tN : n.toNode) {
                lat2 = read.get(tN.toNodeID).location.get(0);
                long2 = read.get(tN.toNodeID).location.get(1);
                double cost = CalcMath.measureMeters(lat1, long1, lat2, long2);
                tN.cost = cost;
            }
        }
        return read;
    }

    public HashMap<String, Node> readJsonStream(InputStream in) throws IOException {
        JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
        return readMessagesArray(reader);
    }


    public HashMap<String, Node> readMessagesArray(JsonReader reader) throws IOException {
        HashMap<String, Node> nodes = new HashMap<String, Node>();

        reader.beginArray();
        while (reader.hasNext()) {
            Node n = readNodes(reader);
            nodes.put(n.nodeId, n);
        }
        reader.endArray();
        return nodes;
    }


    public Node readNodes(JsonReader reader) throws IOException {
        String nodeID = "";
        List<Double> position = null;
        String floor = null;
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
                    position = readDoublesArray(reader);
                    break;
                case "floor":
                    floor = reader.nextString();
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
        return new Node(nodeID, position, floor, nodeLinks, type);
    }


    public List<Double> readDoublesArray(JsonReader reader) throws IOException {
        List<Double> doubles = new ArrayList();

        reader.beginArray();
        while (reader.hasNext()) {
            doubles.add(reader.nextDouble());
        }
        reader.endArray();
        return doubles;
    }

    public List<ToNode> readNodeLinks(JsonReader reader) throws IOException {
        List<ToNode> list = new ArrayList<ToNode>();
        String toNodeID = null;

        reader.beginArray();
        while (reader.hasNext()) {
            reader.beginObject();
            while (reader.hasNext()) {
                String name = reader.nextName();
                if (name.equals("toNodeID")) toNodeID = reader.nextString();
                else reader.skipValue();
            }
            reader.endObject();
            list.add(new ToNode(toNodeID, 0));
        }
        reader.endArray();
        return list;
    }

    public Node findNearestNode(LatLng latLng) {
        double startLat1 = latLng.latitude;
        double startLong1 = latLng.longitude;

        double shortLat = 0.0;
        double shortLng = 0.0;
        Node tempNode = null;

        for (Node n : jsonList.values()) {

            double lat1 = n.location.get(0);
            double long1 = n.location.get(1);

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
        MapDrawer.addPolyline(shortLat, shortLng, startLat1, startLong1, Color.BLUE);
        return tempNode;
    }

    public Node FindClosestNodeInsideRoom(Room room) {

        for (Node node : jsonList.values()) {
            LatLng latLng1 = new LatLng(node.location.get(0), node.location.get(1));
            if (MapsActivity.setupGraph.getRooms().nodeInsideRoom(room, latLng1)) {
                return node;
            }
        }
        return null;
    }

    public Node FindClosestNodeInsideElevator(Elevator elevator) {

        for (Node node : jsonList.values()) {
            LatLng latLng1 = new LatLng(node.location.get(0), node.location.get(1));
            if (MapsActivity.setupGraph.getElevators().nodeInsideElevator(elevator, latLng1)) {
                return node;
            }
        }
        return null;
    }

    public Node FindClosestNodeInsideStair(Stair stair) {
        for (Node node : jsonList.values()) {
            LatLng latLng1 = new LatLng(node.location.get(0), node.location.get(1));
            if (MapsActivity.setupGraph.getStairs().nodeInsideStair(stair, latLng1)) {
                return node;
            }
        }
        return null;
    }
}

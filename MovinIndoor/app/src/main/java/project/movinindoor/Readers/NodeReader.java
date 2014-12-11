package project.movinindoor.Readers;


import android.graphics.Color;
import android.util.JsonReader;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import project.movinindoor.CalcMath;
import project.movinindoor.Graph.Node;
import project.movinindoor.Graph.ToNode;
import project.movinindoor.MapDrawer;
import project.movinindoor.MapsActivity;
import project.movinindoor.Rooms.Room;

/**
 * Created by Davey on 25-11-2014.
 */
public class NodeReader {

    public HashMap<String, Node> jsonList;

    public NodeReader() {
        InputStream inputStream = null;
        try {
            inputStream = MapsActivity.getContext().getAssets().open("WindesheimNavMesh.json");
            HashMap<String, Node> read = readJsonStream(inputStream);
            jsonList = calculate(read);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public HashMap<String, Node> calculate(HashMap<String, Node> read ) {
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
        String nodeID = null;
        List<Double> position = null;
        String floor = null;
        List<ToNode> nodeLinks = null;

        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("nodeID")) {
                nodeID = reader.nextString();
            } else if (name.equals("position")) {
                position = readDoublesArray(reader);
            } else if (name.equals("floor")) {
                floor =  reader.nextString();
            } else if (name.equals("nodeLinks")) {
                List<ToNode> r = readNodeLinks(reader);
                nodeLinks = r;
            } else {
                reader.skipValue();
            }

        }
        reader.endObject();
        return new Node(nodeID,position,floor,nodeLinks);
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
            while(reader.hasNext()) {
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

            if(shortLng == 0.0 && shortLng == 0.0) {
                shortLat = lat1;
                shortLng = long1;
            }

            double som1 = CalcMath.measureMeters(startLat1, startLong1, lat1, long1);
            double som2 = CalcMath.measureMeters(startLat1, startLong1, shortLat, shortLng);

            if(som1 <= som2) {
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
            LatLng latLng1 =  new LatLng(node.location.get(0), node.location.get(1));
            if(MapsActivity.setupGraph.getRooms().nodeInsideRoom(room, latLng1)) {
                return node;
            }
        }
        return null;
    }
}

package project.movinindoor.Graph;


import android.util.JsonReader;
import android.util.Log;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Davey on 25-11-2014.
 */
public class NodeReader {

    public NodeReader() {
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream("WTCNavMesh.json");
            HashMap<String, Node> read = readJsonStream(inputStream);
            HashMap<String, Node> t = calculate(read);


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public HashMap<String, Node> addVertexes() {
        
    }

    public HashMap<String, Node> calculate(HashMap<String, Node> read ) {
        double lat1 = 0.0;
        double long1 = 0.0;
        double lat2 = 0.0;
        double long2 = 0.0;

        for (Node n : read.values()) {
            lat1 = (double) n.location.get(0);
            long1 = (double) n.location.get(1);

            for (ToNode tN : n.toNode) {
                lat2 = (double) read.get(tN.toNodeID).location.get(0);
                long2 = (double) read.get(tN.toNodeID).location.get(1);

                double cost = measureMeters(lat1, long1, lat2, long2);
                tN.cost = cost;
            }
        }
        return read;
    }

    public double measureMeters(double lat1,double lon1,double lat2,double lon2){
        double R = 6378.137; // Radius of earth in KM
        double dLat = (lat2 - lat1) * Math.PI / 180;
        double dLon = (lon2 - lon1) * Math.PI / 180;
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(lat1 * Math.PI / 180) * Math.cos(lat2 * Math.PI / 180) *
                        Math.sin(dLon/2) * Math.sin(dLon/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double d = R * c;
        return d * 1000; // meters
    }

    public HashMap<String, Node> readJsonStream(InputStream in) throws IOException {
        JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
        try {
            return readMessagesArray(reader);


        } finally {
            reader.close();
        }
    }




    public HashMap<String, Node> readMessagesArray(JsonReader reader) throws IOException {
        HashMap<String, Node> nodes = new HashMap<String, Node>();

        reader.beginArray();
        while (reader.hasNext()) {
            Log.i("JSON", reader.toString());
            nodes.put(readNodes(reader).nodeId, readNodes(reader));
        }
        reader.endArray();
        return nodes;
    }


    public Node readNodes(JsonReader reader) throws IOException {

        String nodeID = null;
        List position = null;
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
                nodeLinks.add(readNodeLinks(reader));
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        return new Node(nodeID,position,floor,nodeLinks);
    }



    public List readDoublesArray(JsonReader reader) throws IOException {
        List doubles = new ArrayList();

        reader.beginArray();
        while (reader.hasNext()) {
            doubles.add(reader.nextDouble());
        }
        reader.endArray();
        return doubles;
    }



    public ToNode readNodeLinks(JsonReader reader) throws IOException {
        String toNodeID = null;


        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("toNodeID")) {
                toNodeID  = reader.nextString();
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        return new ToNode(toNodeID, 0);
    }
}

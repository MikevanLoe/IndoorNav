package project.movinindoor.Graph;


import android.util.JsonReader;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import project.movinindoor.MapsActivity;

/**
 * Created by Davey on 25-11-2014.
 */
public class NodeReader {

    HashMap<String, Node> jsonList;

    public NodeReader() {
        InputStream inputStream = null;
        try {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpGet get = new HttpGet("http://wrs.movinsoftware.nl/?service=wrs&version=1.0.0&request=GetNavigationGrid&mapid=00W");

                HttpResponse httpResponse = httpClient.execute(get);
                String json = EntityUtils.toString(httpResponse.getEntity());
               Log.i("JSON_URL0", json);



            //inputStream = IOUtils.toInputStream(json, "UTF-8");


            HashMap<String, Node> read = readJsonStream(json);



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

    public HashMap<String, Node> readJsonStream(String in) throws IOException {
        JsonReader reader = new JsonReader(new StringReader(in));

        //try {


        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("nodes")) {
                return readMessagesArray(reader);
            }
        }
        reader.beginObject();
        return readMessagesArray(reader);


        //} finally {
         //   reader.close();
       // }
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
                if (name.equals("toNodeID")) {
                    toNodeID = reader.nextString();
                } else {
                    reader.skipValue();
                }
            }
            reader.endObject();
            list.add(new ToNode(toNodeID, 0));
        }
        reader.endArray();
        return list;
    }
}

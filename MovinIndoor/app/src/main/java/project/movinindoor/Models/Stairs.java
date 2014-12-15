package project.movinindoor.Models;

import android.util.JsonReader;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import project.movinindoor.MapDrawer;
import project.movinindoor.MapsActivity;

/**
 * Created by Davey on 12-12-2014.
 */
public class Stairs {
    private Map<String,Stair> Stairs = new HashMap<String,Stair>();

    public Stairs() {
        InputStream inputStream = null;
        try {
            URL url = new URL("http://wfs.movinsoftware.nl/?service=wfs&version=1.0.0&request=getfeature&mapid=00W&typenames=Stairs");
            inputStream = url.openStream();
            inputStream = MapsActivity.getContext().getAssets().open("WindesheimStairs.json");
            Stairs = readJsonStream(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Map<String, Stair> getStairs() {
        return Stairs;
    }

    public HashMap<String, Stair> readJsonStream(InputStream in) {
        try {
            JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
            return readStairsArray(reader);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new HashMap<String, Stair>();
    }


    public HashMap<String, Stair> readStairsArray(JsonReader reader)  {
        HashMap<String, Stair> Stair = new HashMap<String, Stair>();

        try {
            reader.beginObject();
            while (reader.hasNext()) {
                String name = reader.nextName();
                if (name.equals("features")) {
                    reader.beginArray();
                    int count = 1;
                    while (reader.hasNext()) {
                        Stair r = readStair(reader);
                        if(Stair.get(r.getLocation()) == null) {
                            Stair.put(r.getLocation(), r);
                        } else {
                            Stair.put(r.getLocation() + "" + String.valueOf(count), r);
                            count++;
                        }
                    }
                    reader.endArray();
                } else {
                    reader.skipValue();
                }
            }
            reader.endObject();


        } catch (IOException e) {
            e.printStackTrace();
        }
        return Stair;
    }


    public Stair readStair(JsonReader reader) {
        String location = "";
        ArrayList<ArrayList<Double>> latLngBounds = new ArrayList<ArrayList<Double>>();

        try {
            reader.beginObject();
            while (reader.hasNext()) {
                String name = reader.nextName();
                if (name.equals("geometry")) {
                    reader.beginObject();
                    while (reader.hasNext()) {
                        String name2 = reader.nextName();
                        if (name2.equals("coordinates")) {
                            latLngBounds = getLatLngBounds(reader);

                        } else {
                            reader.skipValue();
                        }
                    }
                    reader.endObject();

                    String name2 = reader.nextName();
                    if (name2.equals("properties")) {
                        reader.beginObject();
                        while (reader.hasNext()) {
                            String name3 = reader.nextName();
                            if (name3.equals("name")) {
                                location = reader.nextString();
                            } else {
                                reader.skipValue();
                            }
                        }
                        reader.endObject();
                    } else {
                        reader.skipValue();
                    }

                } else {
                    reader.skipValue();
                }
            }
            reader.endObject();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new Stair(location, latLngBounds);
    }

    private ArrayList<ArrayList<Double>> getLatLngBounds(JsonReader reader) {
        int polySides = 1;
        ArrayList<Double> polyX = new ArrayList<Double>();
        ArrayList<Double>  polyY = new ArrayList<Double>();
        ArrayList<ArrayList<Double>> returnList = new ArrayList<ArrayList<Double>>();

        try {

            reader.beginArray();
            while (reader.hasNext()) {
                int prev = 0;
                reader.beginArray();
                while (reader.hasNext()) {

                    if(prev == 0) {
                        polyX.add(reader.nextDouble());
                        prev++;
                    } else {
                        polyY.add(reader.nextDouble());
                        prev--;
                    }

                }
                reader.endArray();
                polySides++;
            }
            reader.endArray();

        } catch (IOException e) {
            e.printStackTrace();
        }
        returnList.add(polyX);
        returnList.add(polyY);
        return returnList;
        //return new LatLngBounds(new LatLng(locationRBLat, locationRBLng), new LatLng(locationLTLat, locationLTLng));
    }

    public Stair getStair(String location) {
        return Stairs.get(location);
    }

    public List<Stair> getAllStairsWithName(String location) {
        List<Stair> StairsWithName = new ArrayList<Stair>();
        for (Stair Stair : Stairs.values()) {
            if(Stair.getLocation().startsWith(location)) {
                StairsWithName.add(Stair);
            }
        }
        return StairsWithName;
    }

    public Stair nodeInsideStair(LatLng latLng) {
        Stair returnStair = null;
        for (Stair Stair : Stairs.values()) {
            try {
                if (!Stair.getLocation().equals("") || !Stair.getLocation().equals("toilet")) {
                    String split = Stair.getLocation().substring(1, 2);

                    if (MapDrawer.getFloor() == Integer.valueOf(split)) {
                        if (nodeInsideStair(Stair, latLng)) {
                            return Stair;
                        }
                    }


                }
            } catch (Exception e) {
                //Not a proper integer
            }
        }
        return returnStair;
    }

    public Stair nodeInsideStair(LatLng latLng, int floor) {
        Stair returnStair = null;
        for (Stair Stair : Stairs.values()) {
            try {
                if (!Stair.getLocation().equals("") || !Stair.getLocation().equals("toilet")) {
                    String[] split = Stair.getLocation().split("\\.");

                    if (floor == Integer.valueOf(split[0].substring(1))) {
                        if (nodeInsideStair(Stair, latLng)) {
                            return Stair;
                        }
                    }


                }
            } catch (Exception e) {
                //Not a proper integer
            }
        }
        return returnStair;
    }

    public boolean nodeInsideStair(Stair Stair, LatLng latLng) {
        ArrayList<Double> vertx = Stair.getLatLngBounds().get(1);
        ArrayList<Double> verty = Stair.getLatLngBounds().get(0);
        double x = latLng.latitude;
        double y = latLng.longitude;
        int nvert = vertx.size();

        int i, j;
        Boolean c = false;
        for (i = 0, j = nvert - 1; i < nvert; j = i++)
        {
            if (((verty.get(i) > y) != (verty.get(j) > y)) && (x < (vertx.get(j) - vertx.get(i)) * (y - verty.get(i)) / (verty.get(j) - verty.get(i)) + vertx.get(i))) {
                c = !c;
            }

        }
        return c;
    }
}

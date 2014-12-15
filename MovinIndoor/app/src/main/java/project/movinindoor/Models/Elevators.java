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
public class Elevators {
    private Map<String,Elevator> Elevators = new HashMap<String,Elevator>();

    public Elevators() {
        InputStream inputStream = null;
        try {
            URL url = new URL("http://wfs.movinsoftware.nl/?service=wfs&version=1.0.0&request=getfeature&mapid=00W&typenames=Elevators");
            inputStream = url.openStream();
            inputStream = MapsActivity.getContext().getAssets().open("WindesheimElevators.json");
            Elevators = readJsonStream(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Map<String, Elevator> getElevators() {
        return Elevators;
    }

    public HashMap<String, Elevator> readJsonStream(InputStream in) {
        try {
            JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
            return readElevatorsArray(reader);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return new HashMap<String, Elevator>();
    }


    public HashMap<String, Elevator> readElevatorsArray(JsonReader reader)  {
        HashMap<String, Elevator> Elevator = new HashMap<String, Elevator>();

        try {
            reader.beginObject();
            while (reader.hasNext()) {
                String name = reader.nextName();
                if (name.equals("features")) {
                    reader.beginArray();
                    int count = 1;
                    while (reader.hasNext()) {
                        Elevator r = readElevator(reader);
                        if(Elevator.get(r.getLocation()) == null) {
                            Elevator.put(r.getLocation(), r);
                        } else {
                            Elevator.put(r.getLocation() + "" + String.valueOf(count), r);
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
        return Elevator;
    }


    public Elevator readElevator(JsonReader reader) {
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
        return new Elevator(location, latLngBounds);
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

    public Elevator getElevator(String location) {
        return Elevators.get(location);
    }

    public List<Elevator> getAllElevatorsWithName(String location) {
        List<Elevator> ElevatorsWithName = new ArrayList<Elevator>();
        for (Elevator Elevator : Elevators.values()) {
            if(Elevator.getLocation().startsWith(location)) {
                ElevatorsWithName.add(Elevator);
            }
        }
        return ElevatorsWithName;
    }

    public Elevator nodeInsideElevator(LatLng latLng) {
        Elevator returnElevator = null;
        for (Elevator Elevator : Elevators.values()) {
            try {
                if (!Elevator.getLocation().equals("") || !Elevator.getLocation().equals("toilet")) {
                    String split = Elevator.getLocation().substring(1, 2);

                    if (MapDrawer.getFloor() == Integer.valueOf(split)) {
                        if (nodeInsideElevator(Elevator, latLng)) {
                            return Elevator;
                        }
                    }


                }
            } catch (Exception e) {
                //Not a proper integer
            }
        }
        return returnElevator;
    }

    public Elevator nodeInsideElevator(LatLng latLng, int floor) {
        Elevator returnElevator = null;
        for (Elevator Elevator : Elevators.values()) {
            try {
                if (!Elevator.getLocation().equals("") || !Elevator.getLocation().equals("toilet")) {
                    String[] split = Elevator.getLocation().split("\\.");

                    if (floor == Integer.valueOf(split[0].substring(1))) {
                        if (nodeInsideElevator(Elevator, latLng)) {
                            return Elevator;
                        }
                    }


                }
            } catch (Exception e) {
                //Not a proper integer
            }
        }
        return returnElevator;
    }

    public boolean nodeInsideElevator(Elevator Elevator, LatLng latLng) {
        ArrayList<Double> vertx = Elevator.getLatLngBounds().get(1);
        ArrayList<Double> verty = Elevator.getLatLngBounds().get(0);
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

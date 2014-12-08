package project.movinindoor;

import android.util.JsonReader;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import project.movinindoor.Graph.Graph;


/**
 * Created by Davey on 3-12-2014.
 */
public class Rooms {

    private Map<String,Room> rooms = new HashMap<String,Room>();

    public Rooms() {
        InputStream inputStream = null;
        try {
            inputStream = MapsActivity.getContext().getAssets().open("WindesheimRooms.json");
            rooms = readJsonStream(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Map<String, Room> getRooms() {
        return rooms;
    }

    public HashMap<String, Room> readJsonStream(InputStream in) {
        try {
            JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
            return readRoomsArray(reader);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new HashMap<String, Room>();
    }


    public HashMap<String, Room> readRoomsArray(JsonReader reader)  {
        HashMap<String, Room> room = new HashMap<String, Room>();

            try {
                reader.beginObject();
                while (reader.hasNext()) {
                    String name = reader.nextName();
                    if (name.equals("features")) {
                        reader.beginArray();
                        int count = 1;
                        while (reader.hasNext()) {
                            Room r = readRoom(reader);
                            if(room.get(r.getLocation()) == null) {
                                room.put(r.getLocation(), r);
                            } else {
                                room.put(r.getLocation() + "" + String.valueOf(count), r);
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
        return room;
    }


    public Room readRoom(JsonReader reader) {
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
        return new Room(location, latLngBounds);
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

    public Room getRoom(String location) {
        return rooms.get(location);
    }

    public List<Room> getAllRoomsWithName(String location) {
        List<Room> roomsWithName = new ArrayList<Room>();
        for (Room room : rooms.values()) {
            if(room.getLocation().startsWith(location)) {
                roomsWithName.add(room);
            }
        }
        return roomsWithName;
    }

    public Room nodeInsideRoom(LatLng latLng) {
        Room returnRoom = null;
        for (Room room : rooms.values()) {
            try {
                if (!room.getLocation().equals("") || !room.getLocation().equals("toilet")) {
                    String split = room.getLocation().substring(1, 2);

                        if (MapDrawer.getFloor() == Integer.valueOf(split)) {
                            if (nodeInsideRoom(room, latLng)) {
                                return room;
                            }
                        }


                }
            } catch (Exception e) {
                //Not a proper integer
            }
        }
        return returnRoom;
    }

    public Room nodeInsideRoom(LatLng latLng, int floor) {
        Room returnRoom = null;
        for (Room room : rooms.values()) {
            try {
                if (!room.getLocation().equals("") || !room.getLocation().equals("toilet")) {
                    String[] split = room.getLocation().split(".");

                    if (floor == Integer.valueOf(split[0].substring(1))) {
                        if (nodeInsideRoom(room, latLng)) {
                            return room;
                        }
                    }


                }
            } catch (Exception e) {
                //Not a proper integer
            }
        }
        return returnRoom;
    }

    public boolean nodeInsideRoom(Room room, LatLng latLng) {
        ArrayList<Double> vertx = room.getLatLngBounds().get(1);
        ArrayList<Double> verty = room.getLatLngBounds().get(0);
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

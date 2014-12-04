package project.movinindoor;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Davey on 3-12-2014.
 */
public class Room {

    private String location;
    private ArrayList<ArrayList<Double>> latLngBounds;

    public Room(String location, ArrayList<ArrayList<Double>> latLngBounds) {
        this.location = location;
        this.latLngBounds = latLngBounds;
    }

    public String getLocation() {
        return location;
    }


    public ArrayList<ArrayList<Double>> getLatLngBounds() {
        return latLngBounds;
    }

    public LatLng getLatLngBoundsCenter() {
        return new LatLng(latLngBounds.get(1).get(0), latLngBounds.get(0).get(0));
    }

}

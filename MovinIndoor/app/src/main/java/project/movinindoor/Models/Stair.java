package project.movinindoor.Models;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Davey on 12-12-2014.
 */
public class Stair {
    private String location;
    private ArrayList<ArrayList<Double>> latLngBounds;

    public Stair(String location, ArrayList<ArrayList<Double>> latLngBounds) {
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

        List<LatLng> points = new ArrayList<LatLng>();
        for(int i = 0; i < latLngBounds.get(0).size(); i++) {
            points.add(new LatLng(latLngBounds.get(1).get(i), latLngBounds.get(0).get(i)));
        }

        double[] centroid = { 0.0, 0.0 };

        for (int i = 0; i < points.size(); i++) {
            centroid[0] += points.get(i).latitude;
            centroid[1] += points.get(i).longitude;
        }

        int totalPoints = points.size();
        centroid[0] = centroid[0] / totalPoints;
        centroid[1] = centroid[1] / totalPoints;

        return new LatLng(centroid[0], centroid[1]);
    }
}

package project.movinindoor.Algorithm;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Davey on 5-1-2015.
 */
public class RouteStep {
    private String action;
    private String text;
    private LatLng latLng;

    public RouteStep(String action, String text, LatLng latLng) {
        this.action = action;
        this.text = text;
        this.latLng = latLng;
    }

    public String getAction() {
        return action;
    }

    public String getText() {
        return text;
    }

    public LatLng getLatLng() {
        return latLng;
    }
}

package project.movinindoor;

import android.graphics.Color;
import android.util.Log;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.android.gms.maps.model.TileProvider;
import com.google.android.gms.maps.model.UrlTileProvider;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Davey on 1-12-2014.
 */
public class MapDrawer {

    static GoogleMap mMap = MapsActivity.getMap();
    public static String floorUrl = "http://wmts.movinsoftware.nl/?Service=WMTS&Request=GetTile&Version=1.0.0&Layer=AllTypes&TileMatrixSet=GoogleMapsCompatible&Format=image/png&Style=Default&TileMatrix=%d&TileCol=%d&TileRow=%d";
    private static int floorNumber = 0;
    public static List<TileOverlay> tileOverlays;
    private static TileOverlay tileOverlay;

    public MapDrawer() {
        //mMap.addMarker(new MarkerOptions().position(new LatLng(51.92108335157883, 4.4808608293533325)).title("Marker"));
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        mMap.getUiSettings().setZoomControlsEnabled(false);
        mMap.getUiSettings().setCompassEnabled(false);
        mMap.setIndoorEnabled(true);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(51.92108335157883, 4.4808608293533325), 17));

        mMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener(){

            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                float minZoom = 15.0f;
                if (cameraPosition.zoom < minZoom)
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(minZoom));
            }
        });

        tileOverlays = new ArrayList<TileOverlay>();

            tileOverlay = mMap.addTileOverlay(new TileOverlayOptions()
                    .tileProvider(MapDrawer.tileProvider));
    }

    public static void setFloor(int floor) {
        tileOverlay.clearTileCache();
        floorNumber = floor;
    }

    public static int getFloor() {
        return floorNumber;
    }

    static TileProvider tileProvider = new UrlTileProvider(256, 256) {
        @Override
        public URL getTileUrl(int x, int y, int zoom) {

            String s = String.format("http://wmts.movinsoftware.nl/?Service=WMTS&Request=GetTile&Version=1.0.0&Layer=AllTypes&TileMatrixSet=GoogleMapsCompatible&Format=image/png&Style=Default&TileMatrix=%d&TileCol=%d&TileRow=%d&Floor="+floorNumber,
                    zoom, x, y);
            if (!checkTileExists(x, y, zoom)) { return null; }

            try {
                //Log.i("MAP_LOG1", s);
                return new URL(s);
            } catch (MalformedURLException e) {
                throw new AssertionError(e);
            }
        }

        private boolean checkTileExists(int x, int y, int zoom) {
            int minZoom = 12;
            int maxZoom = 22;

            if ((zoom < minZoom || zoom > maxZoom)) return false;

            return true;
        }
    };

    public static List<Polyline> polylines = new ArrayList<Polyline>();
    public static List<Marker> markers = new ArrayList<Marker>();

    public static void addPolyline(double lat1, double long1, double lat2, double long2){
        // Instantiates a new Polyline object and adds points to define a rectangle
        PolylineOptions rectOptions = new PolylineOptions()
                .add(new LatLng(lat1, long1))
                .add(new LatLng(lat2, long2)).zIndex(100);

        Polyline polyline = mMap.addPolyline(rectOptions);
        polylines.add(polyline);
    }

    public static void addPolyline(double lat1, double long1, double lat2, double long2, int color){
        // Instantiates a new Polyline object and adds points to define a rectangle
        PolylineOptions rectOptions = new PolylineOptions()
                .add(new LatLng(lat1, long1))
                .add(new LatLng(lat2, long2)).zIndex(500).color(color);

        Polyline polyline =  mMap.addPolyline(rectOptions);
        polylines.add(polyline);
    }

    public static void addPolyline(double lat1, double long1, double lat2, double long2, int color, int zIndex){
        // Instantiates a new Polyline object and adds points to define a rectangle
        PolylineOptions rectOptions = new PolylineOptions()
                .add(new LatLng(lat1, long1))
                .add(new LatLng(lat2, long2)).zIndex(200 + zIndex).color(color);

        Polyline polyline =  mMap.addPolyline(rectOptions);
        polylines.add(polyline);

    }

    public static void addMarker(double lat1, double long1, String name) {
        markers.add(mMap.addMarker(new MarkerOptions().position(new LatLng(lat1, long1)).title(name)));
    }

    public static void addMarker(LatLng latLng, String name) {

        markers.add(mMap.addMarker(new MarkerOptions().position(latLng).title(name)));
    }

    public static void addMarker(double lat1, double long1) {
        markers.add(mMap.addMarker(new MarkerOptions().position(new LatLng(lat1, long1)).title("Marker")));
    }

    public static void removeMarkers() {
        for(Marker m : markers) { m.remove(); }
    }

    public static void hidePolylinesFloor(int floor) {
        for(Polyline p : polylines) {
            if(p.getColor() == Color.BLACK && p.getZIndex() == (floor + 200)) p.setVisible(false);
        }
    }

    public static void showPolylinesFloor(int floor) {
        for(Polyline p : polylines) {
            if(p.getColor() == Color.BLACK && p.getZIndex() == (floor + 200)) p.setVisible(true);
        }
    }

    public static void removePolylines() {
        for(Polyline p : polylines) {
            if(p.getColor() != Color.BLACK) p.remove();
        }
    }
}

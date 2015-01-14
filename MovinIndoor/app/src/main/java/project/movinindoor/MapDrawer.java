package project.movinindoor;

import android.graphics.Color;
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

    /**
     * constructor that creates a mapdrawer and sets the settings for the mMap
     *
     */
    public MapDrawer() {
        mMap.setMapType(GoogleMap.MAP_TYPE_NONE);
        mMap.getUiSettings().setZoomControlsEnabled(false);
        mMap.getUiSettings().setCompassEnabled(false);
        mMap.setIndoorEnabled(true);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(52.49985968094016, 6.0805946588516235), 18));

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

    /**
     * function that changes the floor to the given parameter
     * @param floor the floor you want mapdrawer to show
     */
    public static void setFloor(int floor) {
        tileOverlay.clearTileCache();
        floorNumber = floor;
        MapsActivity.getBtnCurrentFloor().setText(String.valueOf(floor));
        MapsActivity.getBtnFloorUp().setVisibility(View.VISIBLE);
        MapsActivity.getBtnFloorDown().setVisibility(View.VISIBLE);

        if (floor >= 10) {
            MapsActivity.getBtnFloorUp().setVisibility(View.INVISIBLE);
        }

        if (floor <= 0) {
            MapsActivity.getBtnFloorDown().setVisibility(View.INVISIBLE);
        }
    }

    public static int getFloor() {
        return floorNumber;
    }

    /**
     * function that gets the tiles from the wmts server of movinsoftware and draws them on mMap
     */
    static TileProvider tileProvider = new UrlTileProvider(256, 256) {
        @Override
        public URL getTileUrl(int x, int y, int zoom) {

            String s = String.format("http://wmts.movinsoftware.nl/?Service=WMTS&Request=GetTile&Version=1.0.0&Layer=AllTypes&TileMatrixSet=GoogleMapsCompatible&Format=image/png&Style=Default&TileMatrix=%d&TileCol=%d&TileRow=%d&Floor="+floorNumber,
                    zoom, x, y);
            if (!checkTileExists(x, y, zoom)) { return null; }

            try {
                return new URL(s);
            } catch (MalformedURLException e) {
                throw new AssertionError(e);
            }
        }

        /**
         * checks if the tile exists on the specified params
         * @param x the x value of the tile you want to check
         * @param y the y value of the tile you want to check
         * @param zoom the zoom level you are currently in
         * @return if the tile exists returns true
         */
        private boolean checkTileExists(int x, int y, int zoom) {
            int minZoom = 12;
            int maxZoom = 22;

            if ((zoom < minZoom || zoom > maxZoom)) return false;

            return true;
        }
    };

    public static List<Polyline> polylines = new ArrayList<Polyline>();
    public static List<Marker> markers = new ArrayList<Marker>();

    /**
     * function to draw a line on the specified parameters
     * @param lat1 latitude of the first point
     * @param long1 longitude of the first point
     * @param lat2 latitude of the second point
     * @param long2 longitude of the second point
     */
    public static void addPolyline(double lat1, double long1, double lat2, double long2){
        // Instantiates a new Polyline object and adds points to define a rectangle
        PolylineOptions rectOptions = new PolylineOptions()
                .add(new LatLng(lat1, long1))
                .add(new LatLng(lat2, long2)).zIndex(100);

        Polyline polyline = mMap.addPolyline(rectOptions);
        polylines.add(polyline);
    }

    /**
     * function to draw a line on the specified parameters
     * @param lat1 latitude of the first point
     * @param long1 longitude of the first point
     * @param lat2 latitude of the second point
     * @param long2 longitude of the second point
     * @param color the clor you want the line to be
     */
    public static void addPolyline(double lat1, double long1, double lat2, double long2, int color){
        // Instantiates a new Polyline object and adds points to define a rectangle
        PolylineOptions rectOptions = new PolylineOptions()
                .add(new LatLng(lat1, long1))
                .add(new LatLng(lat2, long2)).zIndex(500).color(color);

        Polyline polyline =  mMap.addPolyline(rectOptions);
        polylines.add(polyline);
    }

    /**
     *
     * function to draw a line on the specified parameters
     * @param lat1 latitude of the first point
     * @param long1 longitude of the first point
     * @param lat2 latitude of the second point
     * @param long2 longitude of the second point
     * @param color the clor you want the line to be
     * @param zIndex the importance of the line
     *               if some lines overlap the line with the
     *               highest zIndex will be drawn
     */
    public static void addPolyline(double lat1, double long1, double lat2, double long2, int color, int zIndex){
        // Instantiates a new Polyline object and adds points to define a rectangle
        PolylineOptions rectOptions = new PolylineOptions()
                .add(new LatLng(lat1, long1))
                .add(new LatLng(lat2, long2)).zIndex(200 + zIndex).color(color);

        Polyline polyline =  mMap.addPolyline(rectOptions);
        polylines.add(polyline);

    }

    /**
     *
     * @param lat1 latitude of the first point
     * @param long1 longitude of the first point
     * @param lat2 latitude of the second point
     * @param long2 longitude of the second point
     * @param color the clor you want the line to be
     * @param zIndex the importance of the line
     *               if some lines overlap the line with the
     *               highest zIndex will be drawn
     */
    public static void addPolylineNav(double lat1, double long1, double lat2, double long2, int color, int zIndex){
        // Instantiates a new Polyline object and adds points to define a rectangle
        PolylineOptions rectOptions = new PolylineOptions()
                .add(new LatLng(lat1, long1))
                .add(new LatLng(lat2, long2)).zIndex(205 + zIndex).color(color);

        Polyline polyline =  mMap.addPolyline(rectOptions);
        polylines.add(polyline);

    }

    /**
     * add a marker on the map
     * @param latLng the place of the marker
     * @param name the name of the marker that shows when you click on it
     */
    public static void addMarker(LatLng latLng, String name) {

        markers.add(mMap.addMarker(new MarkerOptions().position(latLng).title(name)));
    }

    /**
     * add a marker on the map
     * @param latLng the place of the marker
     * @param name the name of the marker that shows when you click it
     * @param floor the floor the marker needs to be. The marker will only show if you are on specified floor
     */
    public static void addMarker(LatLng latLng, String name, int floor) {

        markers.add(mMap.addMarker(new MarkerOptions().position(latLng).title(name).snippet(String.valueOf(floor))));
    }

    /**
     * hides all the markers on a specified floor
     * @param floor the floor you want all markers to hide
     */
    public static void hideMarkersFloor(int floor) {
        for(Marker m : markers) {
            if(m.getSnippet() != null) {
                if (m.getSnippet().equals(String.valueOf(floor))) m.setVisible(false);
            }
        }
    }

    /**
     * hides all markers on the map
     */
    public static void hideMarkers() {
        for(Marker m : markers) {
           m.setVisible(false);
        }
    }

    /**
     * show the markers on a specified floor
     * @param floor the floor you want the markers to be visible
     */
    public static void showMarkersFloor(int floor) {
        for(Marker m : markers) {
            if(m.getSnippet() != null) {
                if (m.getSnippet().equals(String.valueOf(floor))) m.setVisible(true);
            }
        }
    }

    /**
     * removes all the markers
     */

    public static void removeMarkers() {
        for(Marker m : markers) { m.remove(); }
    }

    /**
     * hide all drawn lines on a specified floor
     * @param floor the floor you want all the lines to be hidden
     */
    public static void hidePolylinesFloor(int floor) {
        for(Polyline p : polylines) {
            if(p.getZIndex() == (floor + 200)) p.setVisible(false);
        }
    }

    /**
     * hide all the lines that are drawn by the navigation route
     * @param floor
     */
    public static void hidePolylinesFloorNav(int floor) {
        for(Polyline p : polylines) {
            if(p.getZIndex() == (floor + 205)) p.setVisible(false);
        }
    }

    /**
     * hide all the lines
     */
    public static void hidePolylines() {
        for(Polyline p : polylines) {
            p.setVisible(false);
        }
    }

    /**
     * show lines on a specified floor
     * @param floor the floor you want all the lines to show
     */
    public static void showPolylinesFloor(int floor) {
        for(Polyline p : polylines) {
            if(p.getZIndex() == (floor + 200)) p.setVisible(true);
        }
    }

    /**
     * show all the lines that have been placed for navigation on a specified floor
     * @param floor
     */
    public static void showPolylinesFloorNav(int floor) {
        for(Polyline p : polylines) {
            if(p.getZIndex() == (floor + 205 )) p.setVisible(true);
        }
    }

    /**
     * remove all lines drawn
     */
    public static void removePolylines() {
        for(Polyline p : polylines) {
            if(p.getColor() != Color.BLACK) p.remove();
        }
    }

    /**
     * show all markers and lines of a specified floor
     * @param floor the floor you want the markers and lines to be shown
     */
    public static void showMarkersAndPolylinesFloor(int floor) {
        showMarkersFloor(floor);
        showPolylinesFloor(floor);
        showPolylinesFloorNav(floor);
    }

    /**
     * hide all markers  and lines of a specified floor
     * @param floor the floor you want the markers and lines to be hidden
     */
    public static void hideMarkersAndPolylinesFloor(int floor) {
        hideMarkersFloor(floor);
        hidePolylinesFloor(floor);
        hidePolylinesFloorNav(floor);
    }

    /**
     * hide all markers and lines
     */
    public static void hideAllMarkersAndPolylines() {
        hidePolylines();
        hideMarkers();
    }


}

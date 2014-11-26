package project.movinindoor;

import android.content.Context;
import android.content.res.Configuration;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.support.v7.app.ActionBarActivity;
import android.widget.Toast;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.android.gms.maps.model.TileProvider;
import com.google.android.gms.maps.model.UrlTileProvider;
import java.net.MalformedURLException;
import java.net.URL;
import project.movinindoor.Graph.StartGraph;

public class MapsActivity extends FragmentActivity implements AdapterView.OnItemClickListener {

    public static Context context;
    private static GoogleMap mMap; // Might be null if Google Play services APK is not available.
    //public static GoogleMap m1Map;
    public final String TAG = "MapsActivity";
    private LatLngBounds bounds = new LatLngBounds( new LatLng(52.496262, 6.072961), new LatLng(52.501134, 6.087896));

    TileProvider tileProvider = new UrlTileProvider(256, 256) {
        @Override
        public URL getTileUrl(int x, int y, int zoom) {
    /* Define the URL pattern for the tile images */
            String s = String.format("http://wmts.movinsoftware.nl/?Service=WMTS&Request=GetTile&Version=1.0.0&Layer=AllTypes&TileMatrixSet=GoogleMapsCompatible&Format=image/png&Style=GisConference&TileMatrix=%d&TileCol=%d&TileRow=%d",
                    zoom, x, y);
            //Log.d("value of y", Integer.toString(y));
            //Log.d("value of zoom", Integer.toString(zoom));
            if (!checkTileExists(x, y, zoom)) {
                return null;
            }

            try {
                System.out.println(s);
                return new URL(s);
            } catch (MalformedURLException e) {
                throw new AssertionError(e);
            }
        }

        /*
         * Check that the tile server supports the requested x, y and zoom.
         * Complete this stub according to the tile range you support.
         * If you support a limited range of tiles at different zoom levels, then you
         * need to define the supported x, y range at each zoom level.
         */
        private boolean checkTileExists(int x, int y, int zoom) {

            int minZoom = 12;
            int maxZoom = 22;

            if ((zoom < minZoom || zoom > maxZoom)) {
                return false;
            }

            return true;
        }
    };

    public static Context getContext() {
        return context;
    }
    
    public static GoogleMap getMap() {
        return mMap;
    }
    // Layout
    private DrawerLayout drawerLayout;
    private ListView listView;
    private ActionBarDrawerToggle drawerListener;
    private String[] pages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Layout
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        listView = (ListView) findViewById(R.id.drawer_list);

        pages = getResources().getStringArray(R.array.nav_drawer_items);

        listView = (ListView) findViewById(R.id.drawer_list);
        listView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, pages));
        listView.setOnItemClickListener(this);

        drawerListener = new ActionBarDrawerToggle(this, drawerLayout, R.string.drawer_open, R.string.drawer_close)
        {
            @Override
            public void onDrawerClosed(View drawerView){
                Toast.makeText(MapsActivity.this, "Drawer Closed ", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDrawerOpened(View drawerView){
                Toast.makeText(MapsActivity.this, "Drawer Opened ", Toast.LENGTH_SHORT).show();
            }
        };
        drawerLayout.setDrawerListener(drawerListener);
        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        // Test om defecten op te halen uit db
        new HttpJson().execute("http://movin.nvrstt.nl/defectsjson.php");

        setUpMapIfNeeded();
        context = getApplicationContext();


//        this.getApplicationContext().getAssets().open("WTCNavMesh.json");
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        mMap.getUiSettings().setCompassEnabled(false);
        mMap.setIndoorEnabled(true);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(51.92108335157883, 4.4808608293533325), 15));
        mMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener(){
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                float minZoom = 15.0f;
                LatLng position = cameraPosition.target;
            }
        });

        StartGraph.runGraphs();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);
        drawerListener.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (drawerListener.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle your other action bar items...
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState){
        super.onPostCreate(savedInstanceState);
        drawerListener.syncState();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(this, pages[position] + " was selected ", Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.maps_activity_actions, menu);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();

        StartGraph.runGraphs();
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link com.google.android.gms.maps.SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(android.os.Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {

            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        mMap.addMarker(new MarkerOptions().position(new LatLng(51.92108335157883, 4.4808608293533325)).title("Marker"));
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        mMap.getUiSettings().setZoomControlsEnabled(false);
        mMap.getUiSettings().setCompassEnabled(false);
        mMap.setIndoorEnabled(true);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(51.92108335157883, 4.4808608293533325), 15));

        mMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener(){

            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                float minZoom = 15.0f;
                if (cameraPosition.zoom < minZoom)
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(minZoom));
            }
        });

        TileProvider tileProvider = new UrlTileProvider(256, 256) {
            @Override
            public URL getTileUrl(int x, int y, int zoom) {

    /* Define the URL pattern for the tile images */
                String s = String.format("http://wmts.movinsoftware.nl/?Service=WMTS&Request=GetTile&Version=1.0.0&Layer=AllTypes&TileMatrixSet=GoogleMapsCompatible&Format=image/png&Style=GisConference&TileMatrix=%d&TileCol=%d&TileRow=%d",
                        zoom, x, y);
                if (!checkTileExists(x, y, zoom)) {
                    return null;
                }

                try {
                    System.out.println(s);
                    return new URL(s);
                } catch (MalformedURLException e) {
                    throw new AssertionError(e);
                }
            }

            /*
             * Check that the tile server supports the requested x, y and zoom.
             * Complete this stub according to the tile range you support.
             * If you support a limited range of tiles at different zoom levels, then you
             * need to define the supported x, y range at each zoom level.
             */
            private boolean checkTileExists(int x, int y, int zoom) {

                int minZoom = 12;
                int maxZoom = 22;

                if ((zoom < minZoom || zoom > maxZoom)) {
                    return false;
                }

                return true;
            }
        };

        TileOverlay tileOverlay = mMap.addTileOverlay(new TileOverlayOptions()
                .tileProvider(tileProvider));
    }

    public static void addPolyline(double lat1, double long1, double lat2, double long2){
        // Instantiates a new Polyline object and adds points to define a rectangle
        PolylineOptions rectOptions = new PolylineOptions()
                .add(new LatLng(lat1, long1))
                .add(new LatLng(lat2, long2)).zIndex(100);

        // Get back the mutable Polyline
        Polyline polyline = getMap().addPolyline(rectOptions);
    }

    public static void addPolyline(double lat1, double long1, double lat2, double long2, int color){
        // Instantiates a new Polyline object and adds points to define a rectangle
        PolylineOptions rectOptions = new PolylineOptions()
                .add(new LatLng(lat1, long1))
                .add(new LatLng(lat2, long2)).zIndex(100).color(color);

        // Get back the mutable Polyline
        Polyline polyline = getMap().addPolyline(rectOptions);
    }

    public static void addMarker(double lat1, double long1, String name) {
        getMap().addMarker(new MarkerOptions().position(new LatLng(lat1, long1)).title(name));
    }

    public static void addMarker(double lat1, double long1) {
        getMap().addMarker(new MarkerOptions().position(new LatLng(lat1, long1)).title("Marker"));
    }
}

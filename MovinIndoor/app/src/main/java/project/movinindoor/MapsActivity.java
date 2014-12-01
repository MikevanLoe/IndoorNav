package project.movinindoor;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
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
import org.json.JSONArray;
import java.net.MalformedURLException;
import java.net.URL;

import project.movinindoor.Graph.StartGraph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import project.movinindoor.Graph.StartGraph;
import project.movinindoor.Reparation.Reparation;

public class MapsActivity extends FragmentActivity implements Fragement_FromToDislay.OnFragmentInteractionListener, NavigationBar.OnFragmentInteractionListener {

    public static Context context;
    private static GoogleMap mMap; // Might be null if Google Play services APK is not available.
    public final String TAG = "MapsActivity";
    private LatLngBounds bounds = new LatLngBounds( new LatLng(52.496262, 6.072961), new LatLng(52.501134, 6.087896));

    public static Context getContext() {
        return context;
    }

    public static GoogleMap getMap() {
        return mMap;
    }

    // Layout
    private DrawerLayout drawerLayout;

    //ExpandableListView
    ExpandableListAdapterNew listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;

   // private ActionBarDrawerToggle drawerListener;
    private HttpJson httpjson;

    public EditText editStart;
    public EditText editEnd;
    public TextView textSpeed;
    public TextView textSpeedCost;
    public TextView textFrom;
    public TextView textTo;
    public FrameLayout oOverlay;
    public LinearLayout linearLayout2;
    public FragmentManager fm;
    public FragmentManager fm2;
    android.support.v4.app.Fragment fragment;
    android.support.v4.app.Fragment fragment2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // id: fragement2
        fm       = getSupportFragmentManager();
        fragment = fm.findFragmentById(R.id.fragment2);

        if (fragment == null) {
            FragmentTransaction ft2 = fm.beginTransaction();
            ft2.add(R.id.fragment2, new NavigationBar());
            ft2.commit();
        }
        //end id: fragement2

        //id: fragement3
        fm2       = getSupportFragmentManager();
        fragment2 = fm2.findFragmentById(R.id.fragment3);

        if (fragment2 == null) {
            FragmentTransaction ft2 = fm2.beginTransaction();
            ft2.add(R.id.fragment2, new Fragement_FromToDislay());
            ft2.commit();
        }
        //end id: fragement3

        fragment.getView().setVisibility(View.INVISIBLE);
        fragment2.getView().setVisibility(View.INVISIBLE);

        //onButtonClick
        editStart = (EditText) findViewById(R.id.editText);
        editEnd = (EditText) findViewById(R.id.editText2);
        textSpeed = (TextView) findViewById(R.id.textView);
        textSpeedCost = (TextView) findViewById(R.id.textView2);
        textFrom = (TextView) findViewById(R.id.fromText);
        textTo = (TextView) findViewById(R.id.toText);

        oOverlay = (FrameLayout) findViewById(R.id.Ooverlay);
        linearLayout2 = (LinearLayout) findViewById(R.id.linearLayout2);

        oOverlay.setVisibility(View.INVISIBLE);

        // Layout
        expListView = (ExpandableListView) findViewById(R.id.expandableListView);

        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();


        // Navigation drawer items
        try
        {
            JSONArray jitems = new HttpJson().execute("http://movin.nvrstt.nl/defectsjson.php").get();

            //Loop though my JSONArray
            for (int  j=0; j< 10; j++) {
                for (Integer i = 0; i < jitems.length(); i++) {
                    //Get My JSONObject and grab the String Value that I want.
                    String title = jitems.getJSONObject(i).getString("Title");
                    String building = jitems.getJSONObject(i).getString("Building");
                    String floor = jitems.getJSONObject(i).getString("Floor");
                    String priority = jitems.getJSONObject(i).getString("Priority");
                    String description = jitems.getJSONObject(i).getString("Description");
                    String status = jitems.getJSONObject(i).getString("Status");
                    String node = jitems.getJSONObject(i).getString("ID");

                    List<String> subList = new ArrayList<String>();
                    listDataHeader.add(j +"-" + i + ": " + title);
                    subList.add("Location:       " + building + "" + floor + "." + node);
                    subList.add("Priority:          " + Reparation.PriorityType.values()[Integer.valueOf(6 - 1)]);
                    subList.add("Status:           " + status);
                    subList.add("Description:  " + description);
                    listDataChild.put(j +"-" + i + ": " + title, subList);
                }
            }
            listAdapter = new ExpandableListAdapterNew(this, listDataHeader, listDataChild);
            expListView.setAdapter(listAdapter);

            // Listview Group click listener
            expListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

                @Override
                public boolean onGroupClick(ExpandableListView parent, View v,
                                            int groupPosition, long id) {
                    // Toast.makeText(getApplicationContext(),
                    // "Group Clicked " + listDataHeader.get(groupPosition),
                    // Toast.LENGTH_SHORT).show();
                    return false;
                }
            });

            // Listview Group expanded listener
            expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

                @Override
                public void onGroupExpand(int groupPosition) {
                    Toast.makeText(getApplicationContext(),
                            listDataHeader.get(groupPosition) + " Expanded",
                            Toast.LENGTH_SHORT).show();
                }
            });

            // Listview Group collasped listener
            expListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

                @Override
                public void onGroupCollapse(int groupPosition) {
                    Toast.makeText(getApplicationContext(),
                            listDataHeader.get(groupPosition) + " Collapsed",
                            Toast.LENGTH_SHORT).show();

                }
            });

            // Listview on child click listener
            expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

                @Override
                public boolean onChildClick(ExpandableListView parent, View v,
                                            int groupPosition, int childPosition, long id) {
                    // TODO Auto-generated method stub
                    Toast.makeText(
                            getApplicationContext(),
                            listDataHeader.get(groupPosition)
                                    + " : "
                                    + listDataChild.get(
                                    listDataHeader.get(groupPosition)).get(
                                    childPosition), Toast.LENGTH_SHORT)
                            .show();
                    return false;
                }
            });

        }
        catch(Exception e)
        {
            Log.e("items_error: ", e.toString());
        }

        getActionBar().hide();

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
        textSpeed.setText("");


    }

    public void menuOpen(View view) {
        drawerLayout.openDrawer(Gravity.LEFT);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);
        //drawerListener.onConfigurationChanged(newConfig);
    }

    public void btnCloseNavigate(View view) {
        removePolylines();
        linearLayout2.setVisibility(View.VISIBLE);
        Animation hideTop = AnimationUtils.loadAnimation(this, R.anim.abc_slide_out_top);
        Animation hideBottom = AnimationUtils.loadAnimation(this, R.anim.abc_slide_out_bottom);
        oOverlay.startAnimation(hideBottom);
        oOverlay.setVisibility(View.INVISIBLE);

        fragment2.getView().startAnimation(hideTop);
        fragment2.getView().setVisibility(View.INVISIBLE);


    }

    public void btnCloseNavBar(View view) {
        Animation hideTop = AnimationUtils.loadAnimation(this, R.anim.abc_slide_out_top);
        Animation showTop = AnimationUtils.loadAnimation(this, R.anim.abc_slide_in_top);

        linearLayout2.startAnimation(showTop);
        linearLayout2.setVisibility(View.VISIBLE);

        fragment.getView().startAnimation(hideTop);
        fragment.getView().setVisibility(View.INVISIBLE);

    }

    public void btnNavBar(View view) {
        Animation hideTop = AnimationUtils.loadAnimation(this, R.anim.abc_slide_out_top);
        Animation showTop = AnimationUtils.loadAnimation(this, R.anim.abc_slide_in_top);

        linearLayout2.startAnimation(hideTop);
        linearLayout2.setVisibility(View.INVISIBLE);

        fragment.getView().startAnimation(showTop);
        fragment.getView().setVisibility(View.VISIBLE);
    }

    public void btnNavigate(View view) {
        InputMethodManager inputManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);

        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);

        oOverlay.setVisibility(View.INVISIBLE);
        removePolylines();
        double cost = StartGraph.g.drawPath(editStart.getText().toString(), editEnd.getText().toString());
        String walkingSpeed = StartGraph.g.calculateWalkingSpeed(cost);
        textSpeed.setText("Estimate duration: " + walkingSpeed);
        textSpeedCost.setText(String.valueOf(Math.round(cost)) + "m");

        textFrom.setText(editStart.getText());
        textTo.setText(editEnd.getText());

        Animation hideTop = AnimationUtils.loadAnimation(this, R.anim.abc_slide_out_top);
        Animation showTop = AnimationUtils.loadAnimation(this, R.anim.abc_slide_in_top);
        Animation showBottom = AnimationUtils.loadAnimation(this, R.anim.abc_slide_in_bottom);

        oOverlay.startAnimation(showBottom);
        oOverlay.setVisibility(View.VISIBLE);


        fragment.getView().startAnimation(hideTop);
        fragment.getView().setVisibility(View.INVISIBLE);

        fragment2.getView().startAnimation(showTop);
        fragment2.getView().setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        /*
        if (drawerListener.onOptionsItemSelected(item)) {
            return true;
        }
        */
        // Handle your other action bar items...
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState){
        super.onPostCreate(savedInstanceState);
        //drawerListener.syncState();
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

        TileProvider tileProvider = new UrlTileProvider(256, 256) {
            @Override
            public URL getTileUrl(int x, int y, int zoom) {

    /* Define the URL pattern for the tile images */
                String s = String.format("http://wmts.movinsoftware.nl/?Service=WMTS&Request=GetTile&Version=1.0.0&Layer=AllTypes&TileMatrixSet=GoogleMapsCompatible&Format=image/png&Style=Default&TileMatrix=%d&TileCol=%d&TileRow=%d",
                        zoom, x, y);
                if (!checkTileExists(x, y, zoom)) {
                    return null;
                }

                try {
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


    public static List<Polyline> polylines = new ArrayList<Polyline>();

    public static void addPolyline(double lat1, double long1, double lat2, double long2){
        // Instantiates a new Polyline object and adds points to define a rectangle
        PolylineOptions rectOptions = new PolylineOptions()
                .add(new LatLng(lat1, long1))
                .add(new LatLng(lat2, long2)).zIndex(100);

        // Get back the mutable Polyline
        Polyline polyline = getMap().addPolyline(rectOptions);
        polylines.add(polyline);
    }

    public static void addPolyline(double lat1, double long1, double lat2, double long2, int color){
        // Instantiates a new Polyline object and adds points to define a rectangle
        PolylineOptions rectOptions = new PolylineOptions()
                .add(new LatLng(lat1, long1))
                .add(new LatLng(lat2, long2)).zIndex(101).color(color);

        // Get back the mutable Polyline
        Polyline polyline = getMap().addPolyline(rectOptions);
        polylines.add(polyline);
    }


    public static void addMarker(double lat1, double long1, String name) {

        getMap().addMarker(new MarkerOptions().position(new LatLng(lat1, long1)).title(name));
    }

    public static void addMarker(double lat1, double long1) {
        getMap().addMarker(new MarkerOptions().position(new LatLng(lat1, long1)).title("Marker"));
    }

    public static void removePolylines() {
        for(Polyline p : polylines)
        {
            if(p.getColor() != Color.BLACK) {
                p.remove();
            }
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}

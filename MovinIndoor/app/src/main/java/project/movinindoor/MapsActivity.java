package project.movinindoor;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Menu;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

import project.movinindoor.Graph.Graph;
import project.movinindoor.Graph.Node;
import project.movinindoor.Graph.SetupGraph;
import project.movinindoor.Reparation.Reparation;

public class MapsActivity extends FragmentActivity implements MarkerInfoFragment.OnFragmentInteractionListener, FloorDisplayFragment.OnFragmentInteractionListener, Fragment_FromToDisplay.OnFragmentInteractionListener, NavigationBar.OnFragmentInteractionListener {

    private static Context context;
    private static GoogleMap mMap; // Might be null if Google Play services APK is not available.
    public static Context getContext() { return context; }
    public static GoogleMap getMap() { return mMap; }
    public static SetupGraph setupGraph;

    //ExpandableListView
    private ExpandableListAdapterNew listAdapter;
    private ExpandableListView expListView;
    private List<String> listDataHeader;
    private HashMap<String, List<String>> listDataChild;

    private Marker longClickMarker = null;
    private Room inRoom;
    public static LatLng customStartPos = null;
    public static LatLng customEndPos = null;
    private JSONArray jitems;

    public static EditText editStart;
    private EditText editEnd;
    public static TextView textSpeed, textSpeedCost, textFrom, textTo;
    private GridLayout oOverlay;
    private Button btnCurrentFloor;
    private LinearLayout linearLayout2;
    private FragmentManager fm, fm2, fmFloorNavigator, fmMarkerDisplay;
    private android.support.v4.app.Fragment fragment, fragment2, fFloorNavigator2, fMarkerDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        setUpMapIfNeeded();
        context = getApplicationContext();

        setupGraph = new SetupGraph();

        try {
            jitems = new HttpJson().execute("http://movin.nvrstt.nl/defectsjson.php").get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        // id: fMarkerDisplay
        fmMarkerDisplay       = getSupportFragmentManager();
        fMarkerDisplay = fmMarkerDisplay.findFragmentById(R.id.fMarkerDisplay);

        if (fMarkerDisplay == null) {
            FragmentTransaction ft2 = fmMarkerDisplay.beginTransaction();
            ft2.add(R.id.fMarkerDisplay, new NavigationBar());
            ft2.commit();
        }
        //end id: fMarkerDisplay

        // id: fFloorNavigator
        fmFloorNavigator       = getSupportFragmentManager();
        fFloorNavigator2 = fmFloorNavigator.findFragmentById(R.id.fFloorNavigator);

        if (fFloorNavigator2 == null) {
            FragmentTransaction ft2 = fmFloorNavigator.beginTransaction();
            ft2.add(R.id.fFloorNavigator, new NavigationBar());
            ft2.commit();
        }
        //end id: fFloorNavigator

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
            ft2.add(R.id.fragment2, new Fragment_FromToDisplay());
            ft2.commit();
        }
        //end id: fragement3

        fragment.getView().setVisibility(View.INVISIBLE);
        fragment2.getView().setVisibility(View.INVISIBLE);
        fFloorNavigator2.getView().setVisibility(View.VISIBLE);
        fMarkerDisplay.getView().setVisibility(View.INVISIBLE);

        //onButtonClick
        editStart = (EditText) findViewById(R.id.editText);
        editEnd = (EditText) findViewById(R.id.editText2);
        textSpeed = (TextView) findViewById(R.id.textView);
        textSpeedCost = (TextView) findViewById(R.id.textView2);
        textFrom = (TextView) findViewById(R.id.fromText);
        textTo = (TextView) findViewById(R.id.toText);

        btnCurrentFloor = (Button) findViewById(R.id.currentFloor);

        oOverlay = (GridLayout) findViewById(R.id.Ooverlay);
        linearLayout2 = (LinearLayout) findViewById(R.id.linearLayout2);
        oOverlay.setVisibility(View.INVISIBLE);

        // Layout
        expListView = (ExpandableListView) findViewById(R.id.expandableListView);


        getActionBar().hide();



//        this.getApplicationContext().getAssets().open("WTCNavMesh.json");
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        mMap.getUiSettings().setCompassEnabled(false);
        mMap.setIndoorEnabled(false);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(52.49985968094016, 6.0805946588516235), 16));

        //Set a marker on long click
        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                TextView textView = (TextView) findViewById(R.id.txtMarkerLocation);
                textView.setText("");

                inRoom = setupGraph.getRooms().nodeInsideRoom(latLng);
                if (inRoom != null) textView.setText(inRoom.getLocation());
                else textView.setText("Custom Position");

                if (longClickMarker != null) longClickMarker.remove();
                TextView txtCord = (TextView) findViewById(R.id.txtCord);
                longClickMarker = mMap.addMarker(new MarkerOptions().position(latLng));
                txtCord.setText(latLng.latitude + "\n " + latLng.longitude);
                if (fMarkerDisplay.getView().getVisibility() == View.INVISIBLE) {
                    Animation showBottom = AnimationUtils.loadAnimation(MapsActivity.this, R.anim.abc_slide_in_bottom);
                    fMarkerDisplay.getView().startAnimation(showBottom);
                    fMarkerDisplay.getView().setVisibility(View.VISIBLE);
                }

            }
        });

        RadioGroup radioGroupMovingBy = (RadioGroup) findViewById(R.id.radioGroupMovingBy);
        final ImageView infoWalkingBy = (ImageView) findViewById(R.id.infoWalkingBy);
        radioGroupMovingBy.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radioCart:
                        Graph.movingByWalk=false;
                        infoWalkingBy.setImageDrawable(getResources().getDrawable(R.drawable.ic_local_grocery_store_black_24dp));
                        Toast.makeText(getContext(), "Cart selected", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        Graph.movingByWalk=true;
                        infoWalkingBy.setImageDrawable(getResources().getDrawable(R.drawable.ic_directions_walk_black_24dp));
                        Toast.makeText(getContext(), "Walking selected", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });

        mMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener(){
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                float minZoom = 15.0f;
                LatLng position = cameraPosition.target;
            }
        });

        textSpeed.setText("");
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);
    }

    public void btnFloorUp(View view) {
       int currentFloor = MapDrawer.getFloor();
        if(currentFloor < 10) {
            MapDrawer.setFloor(currentFloor + 1);
            btnCurrentFloor.setText(String.valueOf(currentFloor + 1));
            MapDrawer.hidePolylinesFloor(currentFloor);
            MapDrawer.showPolylinesFloor(currentFloor + 1);
        }
    }

    public void btnMarkerClose(View view) {
        longClickMarker.remove();
        MapDrawer.removeMarkers();
        MapDrawer.removePolylines();
        Animation hideBottom = AnimationUtils.loadAnimation(this, R.anim.abc_slide_out_bottom);
        fMarkerDisplay.getView().startAnimation(hideBottom);
        fMarkerDisplay.getView().setVisibility(View.INVISIBLE);
    }

    public void btnMarkerSelect(View view) {
        longClickMarker.remove();

        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.groupLocation);
        int selectedId = radioGroup.getCheckedRadioButtonId();
        RadioButton radioButton = (RadioButton) findViewById(selectedId);
        String text;

        if(radioButton.getHint().toString().equals("to")) {
            if(inRoom !=null) {
                customEndPos = null;
                editEnd.setText(inRoom.getLocation());
            }
            else {
                customEndPos = longClickMarker.getPosition();
                editEnd.setText("Custom End Position");
            }
            text = "End";
        } else if(radioButton.getHint().toString().equals("from")) {
            if(inRoom !=null) {
                customStartPos = null;
                editStart.setText(inRoom.getLocation());
            }
            else {
                customStartPos = longClickMarker.getPosition();
                editStart.setText("Custom Start Position");
            }
            text = "Start";
        } else {
            Toast.makeText(getContext(), "Failed. Try again", Toast.LENGTH_SHORT).show();
            return;
        }

        Animation hideBottom = AnimationUtils.loadAnimation(this, R.anim.abc_slide_out_bottom);
        fMarkerDisplay.getView().startAnimation(hideBottom);
        fMarkerDisplay.getView().setVisibility(View.INVISIBLE);
        Toast.makeText(getContext(), text + " location added", Toast.LENGTH_SHORT).show();
    }


    public void btnFloorDown(View view) {
        int currentFloor = MapDrawer.getFloor();
        if(currentFloor > 0) {
            MapDrawer.setFloor(currentFloor - 1);
            btnCurrentFloor.setText(String.valueOf(currentFloor - 1));
            MapDrawer.hidePolylinesFloor(currentFloor);
            MapDrawer.showPolylinesFloor(currentFloor - 1);
        }
    }

    public void btnCloseNavigate(View view) {


        MapDrawer.removePolylines();
        MapDrawer.removeMarkers();
        linearLayout2.setVisibility(View.VISIBLE);
        Animation hideTop = AnimationUtils.loadAnimation(this, R.anim.abc_slide_out_top);
        Animation hideBottom = AnimationUtils.loadAnimation(this, R.anim.abc_slide_out_bottom);
        Animation showRight = AnimationUtils.loadAnimation(this, R.anim.abc_fade_in);
        oOverlay.startAnimation(hideBottom);
        oOverlay.setVisibility(View.INVISIBLE);

        fragment2.getView().startAnimation(hideTop);
        fragment2.getView().setVisibility(View.INVISIBLE);

        fFloorNavigator2.getView().startAnimation(showRight);
        fFloorNavigator2.getView().setVisibility(View.VISIBLE);
    }

    public void btnCloseNavBar(View view) {
        Animation hideTop = AnimationUtils.loadAnimation(this, R.anim.abc_slide_out_top);
        Animation showTop = AnimationUtils.loadAnimation(this, R.anim.abc_slide_in_top);
        Animation showRight = AnimationUtils.loadAnimation(this, R.anim.abc_fade_in);
        linearLayout2.startAnimation(showTop);
        linearLayout2.setVisibility(View.VISIBLE);
        fragment.getView().startAnimation(hideTop);
        fragment.getView().setVisibility(View.INVISIBLE);

        fFloorNavigator2.getView().startAnimation(showRight);
        fFloorNavigator2.getView().setVisibility(View.VISIBLE);
    }

    public void btnNavBar(View view) {
        prepareListData();

        Animation hideTop = AnimationUtils.loadAnimation(this, R.anim.abc_slide_out_top);
        Animation showTop = AnimationUtils.loadAnimation(this, R.anim.abc_slide_in_top);
        Animation hideRight = AnimationUtils.loadAnimation(this, R.anim.abc_fade_out);
        linearLayout2.startAnimation(hideTop);
        linearLayout2.setVisibility(View.INVISIBLE);
        fragment.getView().startAnimation(showTop);
        fragment.getView().setVisibility(View.VISIBLE);

        fFloorNavigator2.getView().startAnimation(hideRight);
        fFloorNavigator2.getView().setVisibility(View.INVISIBLE);
    }

    public void btnNavigate(View view) {
        //Hide keyboard on navigate
        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

        //Get Start position
        String startPosition = editStart.getText().toString();
        String endPosition = editEnd.getText().toString();

        navigate(startPosition, endPosition);
    }


    public void btnNavigateRepair(View view) {
        final String startRoom;

        int pos = Integer.valueOf(view.getTag().toString());
        if(pos > 0) {
            startRoom = listAdapter.getChild(pos - 1, 0).toString().substring(16);
        } else {
            startRoom = MapsActivity.editStart.getText().toString();
        }

        final String EndRoom = listAdapter.getChild(pos, 0).toString().substring(16);

        navigate(startRoom, EndRoom);
    }


    public void showLocation(View view) {
        MapDrawer.removePolylines();
        MapDrawer.removeMarkers();
        String room;

        int pos = Integer.valueOf(view.getTag().toString());
        room = listAdapter.getChild(pos, 0).toString().substring(16);
        LatLng getRoom = setupGraph.getRooms().getRoom(room).getLatLngBoundsCenter();
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(getRoom, 20));

        MapDrawer.addMarker(getRoom.latitude, getRoom.longitude, "Location");

        Animation hideTop = AnimationUtils.loadAnimation(this, R.anim.abc_slide_out_top);
        Animation showTop = AnimationUtils.loadAnimation(this, R.anim.abc_slide_in_top);
        Animation showBottom = AnimationUtils.loadAnimation(this, R.anim.abc_slide_in_bottom);
        Animation showRight = AnimationUtils.loadAnimation(this, R.anim.abc_fade_in);

        oOverlay.startAnimation(showBottom);
        oOverlay.setVisibility(View.VISIBLE);

        fragment.getView().startAnimation(hideTop);
        fragment.getView().setVisibility(View.INVISIBLE);

        linearLayout2.startAnimation(showTop);
        linearLayout2.setVisibility(View.VISIBLE);

        fFloorNavigator2.getView().startAnimation(showRight);
        fFloorNavigator2.getView().setVisibility(View.VISIBLE);
    }


    public void navigate(String start, String end) {
        //Removes From -> To Fragement;
        oOverlay.setVisibility(View.INVISIBLE);
        //Removes existing Polylines
        MapDrawer.removePolylines();
        MapDrawer.removeMarkers();

        boolean sucess = setupGraph.navigateRoute(start, end);

        if(sucess) {
            Toast.makeText(getApplicationContext(), "Navigation started", Toast.LENGTH_SHORT).show();
            Animation hideTop = AnimationUtils.loadAnimation(this, R.anim.abc_slide_out_top);
            Animation showTop = AnimationUtils.loadAnimation(this, R.anim.abc_slide_in_top);
            Animation showBottom = AnimationUtils.loadAnimation(this, R.anim.abc_slide_in_bottom);
            Animation showRight = AnimationUtils.loadAnimation(this, R.anim.abc_fade_in);

            oOverlay.startAnimation(showBottom);
            oOverlay.setVisibility(View.VISIBLE);

            fragment.getView().startAnimation(hideTop);
            fragment.getView().setVisibility(View.INVISIBLE);

            fragment2.getView().startAnimation(showTop);
            fragment2.getView().setVisibility(View.VISIBLE);

            fFloorNavigator2.getView().startAnimation(showRight);
            fFloorNavigator2.getView().setVisibility(View.VISIBLE);
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle your other action bar items...
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState){
        super.onPostCreate(savedInstanceState);
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
        setUpGraphIfNeeded();
    }

    private void setUpGraphIfNeeded() {
        if (setupGraph == null) {
            setupGraph = new SetupGraph();
        }
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
        //sendPushNotification("He mooie titel", "Goede text man");
        MapDrawer mapDrawer = new MapDrawer();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        MapDrawer mapDrawer = new MapDrawer();
    }


    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();
        // Navigation drawer items

        try
        {


            //Loop though my JSONArray
            for (int  j=0; j< 10; j++) {
                for (Integer i = 0; i < jitems.length(); i++) {
                    //Get My JSONObject and grab the String Value that I want.
                    String title = jitems.getJSONObject(i).getString("shortdescription");
                    String building = jitems.getJSONObject(i).getString("building");
                    String floor = jitems.getJSONObject(i).getString("floor");
                    String priority = jitems.getJSONObject(i).getString("priority");
                    String description = jitems.getJSONObject(i).getString("description");
                    String clong = jitems.getJSONObject(i).getString("clong");
                    String clat = jitems.getJSONObject(i).getString("clat");
                    String comments = jitems.getJSONObject(i).getString("comments");
                    String status = jitems.getJSONObject(i).getString("status");
                    String node = jitems.getJSONObject(i).getString("defectid");

                    LatLng latLng = new LatLng(Double.valueOf(clat), Double.valueOf(clong));
                    Rooms nodeRooms = setupGraph.getRooms();
                    Room nodeRoom = nodeRooms.nodeInsideRoom(latLng);

                    String room = nodeRoom.getLocation();


                    List<String> subList = new ArrayList<String>();
                    listDataHeader.add(j +"" + i + " " + title);
                    if(room != null) subList.add("Location:       " + room);
                    else subList.add("Location:       " + building + "" + floor + "." + "16");
                    subList.add("Priority:          " + Reparation.PriorityType.values()[Integer.valueOf(Integer.valueOf(priority) - 1)]);
                    subList.add("Status:           " + status);
                    subList.add("Description:  " + description);
                    subList.add("Comment:  " + comments);
                    listDataChild.put(j +"" + i + " " + title, subList);


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
                    /*Toast.makeText(getApplicationContext(),
                            listDataHeader.get(groupPosition) + " Expanded",
                            Toast.LENGTH_SHORT).show();*/
                }
            });

            // Listview Group collasped listener
            expListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

                @Override
                public void onGroupCollapse(int groupPosition) {
                     /*Toast.makeText(getApplicationContext(),
                            listDataHeader.get(groupPosition) + " Collapsed",
                            Toast.LENGTH_SHORT).show();*/

                }
            });

            // Listview on child click listener
            expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

                @Override
                public boolean onChildClick(ExpandableListView parent, View v,
                                            int groupPosition, int childPosition, long id) {
                    // TODO Auto-generated method stub
                    /* Toast.makeText(
                            getApplicationContext(),
                            listDataHeader.get(groupPosition)
                                    + " : "
                                    + listDataChild.get(
                                    listDataHeader.get(groupPosition)).get(
                                    childPosition), Toast.LENGTH_SHORT)
                            .show();*/
                    return false;
                }
            });

        }
        catch(Exception e)
        {
            Log.e("items_error: ", e.toString());
        }
    }

    public void sendPushNotification(String title, String text) {
        context = getApplicationContext();

        NotificationManager notificationManager = (NotificationManager) getSystemService(context.NOTIFICATION_SERVICE);
        Notification notification = new Notification.Builder(context)
                .setContentTitle(title)
                .setContentText(text)
                .setSmallIcon(R.drawable.movin_push)
                .build();

        notificationManager.notify(0, notification);
    }
}


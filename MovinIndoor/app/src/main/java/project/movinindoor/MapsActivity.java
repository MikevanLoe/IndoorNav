package project.movinindoor;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.Menu;
import android.view.ViewGroup;
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

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

import project.movinindoor.Algorithm.Algorithm;
import project.movinindoor.Fragment.FloorDisplayFragment;
import project.movinindoor.Fragment.Fragment_FromToDisplay;
import project.movinindoor.Fragment.MarkerInfoFragment;
import project.movinindoor.Fragment.NavigationBar;
import project.movinindoor.Graph.Graph.Graph;
import project.movinindoor.Graph.GraphHandler;
import project.movinindoor.Readers.HttpJson;


import project.movinindoor.Models.Room;


public class MapsActivity extends FragmentActivity implements MarkerInfoFragment.OnFragmentInteractionListener, FloorDisplayFragment.OnFragmentInteractionListener, Fragment_FromToDisplay.OnFragmentInteractionListener, NavigationBar.OnFragmentInteractionListener {

    private static Context context;
    private static GoogleMap mMap; // Might be null if Google Play services APK is not available.
    public static Context getContext() { return context; }
    public static GoogleMap getMap() { return mMap; }
    public static final LatLngBounds BOUNDS = new LatLngBounds(new LatLng(52.497917, 6.076639), new LatLng(52.501379, 6.083449));
    public static GraphHandler setupGraph;

    //ExpandableListView
    private ExpandableListAdapterNew listAdapter;
    private ExpandableListView expListView;
    private List<String> listDataHeader;
    private HashMap<String, List<String>> listDataChild;

    private Marker longClickMarker = null;
    private Room inRoom;
    public static LatLng customStartPos = null;
    public static LatLng customEndPos = null;
    public static JSONArray jitems;

    public static EditText editStart;
    private EditText editEnd;
    public static TextView textSpeed, textSpeedCost, textFrom, textTo;
    public static GridLayout fNavigationInfoBottom;
    private Button btnCurrentFloor;
    private ImageButton btnFloorUp, btnFloorDown;

    public static LinearLayout fNavigationMenu;
    private FragmentManager fmRepairList, fmNavigationInfoTop, fmFloorNavigator, fmMarkerDisplay;
    public static android.support.v4.app.Fragment fRepairList, fNavigationInfoTop, fFloorNavigator2, fMarkerDisplay;
    private ImageView infoWalkingBy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        getActionBar().hide();

        try {
            jitems = new HttpJson().execute("http://movin.nvrstt.nl/defectsjson.php").get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        setUpMapIfNeeded();
        context = getApplicationContext();
        setupGraph = new GraphHandler();



        fmMarkerDisplay       = getSupportFragmentManager();
        fMarkerDisplay = fmMarkerDisplay.findFragmentById(R.id.fMarkerDisplay);

        fmFloorNavigator       = getSupportFragmentManager();
        fFloorNavigator2 = fmFloorNavigator.findFragmentById(R.id.fFloorNavigator);

        fmRepairList = getSupportFragmentManager();
        fRepairList = fmRepairList.findFragmentById(R.id.fragment2);

        fmNavigationInfoTop = getSupportFragmentManager();
        fNavigationInfoTop = fmNavigationInfoTop.findFragmentById(R.id.fragment3);

        fRepairList.getView().setVisibility(View.INVISIBLE);
        fNavigationInfoTop.getView().setVisibility(View.INVISIBLE);
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
        btnFloorUp = (ImageButton) findViewById(R.id.floorUp);
        btnFloorDown = (ImageButton) findViewById(R.id.floorDown);

        fNavigationInfoBottom = (GridLayout) findViewById(R.id.Ooverlay);
        fNavigationMenu = (LinearLayout) findViewById(R.id.linearLayout2);
        fNavigationInfoBottom.setVisibility(View.INVISIBLE);

        // Layout
        expListView = (ExpandableListView) findViewById(R.id.expandableListView);



        //Select Walking With Cart or By Foot
        RadioGroup radioGroupMovingBy = (RadioGroup) findViewById(R.id.radioGroupMovingBy);
        infoWalkingBy = (ImageView) findViewById(R.id.infoWalkingBy);
        radioGroupMovingBy.setOnCheckedChangeListener(onCheckedChangeListener);

        mMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                float minZoom = 16.0f;
                LatLng position = cameraPosition.target;

                if(cameraPosition.zoom < minZoom)
                {
                    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(position, minZoom);
                    mMap.moveCamera(cameraUpdate);
                }
                if(position.latitude < BOUNDS.southwest.latitude || position.longitude < BOUNDS.southwest.longitude || position.latitude > BOUNDS.northeast.latitude || position.longitude > BOUNDS.northeast.longitude)
                {
                    LatLng correctedPosition = getLatLngCorrection(position);
                    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(correctedPosition, cameraPosition.zoom);
                    mMap.moveCamera(cameraUpdate);
                }
            }
        });

        textSpeed.setText("");

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);
    }

    public RadioGroup.OnCheckedChangeListener onCheckedChangeListener = new RadioGroup.OnCheckedChangeListener() {
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
    };

    public GoogleMap.OnMapLongClickListener onMapLongClick = new GoogleMap.OnMapLongClickListener() {
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
    };

    //OnClick FloorNavigator Button Up
    public void btnFloorUp(View view) {
        btnFloorDown.setVisibility(View.VISIBLE);
       int currentFloor = MapDrawer.getFloor();
        if(currentFloor < 10) {
            MapDrawer.setFloor(currentFloor + 1);
            btnCurrentFloor.setText(String.valueOf(currentFloor + 1));

            MapDrawer.hidePolylinesFloor(currentFloor);
            MapDrawer.showPolylinesFloor(currentFloor + 1);
        }

        if (currentFloor >= 9) {
            btnFloorUp.setVisibility(View.INVISIBLE);
        }
    }


    //OnClick Close Button From Custom Marker
    public void btnMarkerClose(View view) {
        longClickMarker.remove();
        MapDrawer.removeMarkers();
        MapDrawer.removePolylines();
        Animator.visibilityMarkerInfo(Animator.Visibility.HIDE);
    }


    //OnClick Select Custom Location
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
            } else {
                customEndPos = longClickMarker.getPosition();
                editEnd.setText("Custom End Position");
            }
            text = "End";
        } else if(radioButton.getHint().toString().equals("from")) {
            if(inRoom !=null) {
                customStartPos = null;
                editStart.setText(inRoom.getLocation());
            } else {
                customStartPos = longClickMarker.getPosition();
                editStart.setText("Custom Start Position");
            }
            text = "Start";
        } else {
            Toast.makeText(getContext(), "Failed. Try again", Toast.LENGTH_SHORT).show();
            return;
        }

        Animator.visibilityMarkerInfo(Animator.Visibility.HIDE);
        Toast.makeText(getContext(), text + " location added", Toast.LENGTH_SHORT).show();
    }


    //OnClick FloorNavigator Button Down
    public void btnFloorDown(View view) {
        btnFloorUp.setVisibility(View.VISIBLE);
        int currentFloor = MapDrawer.getFloor();
        if(currentFloor > 0) {
            MapDrawer.setFloor(currentFloor - 1);
            btnCurrentFloor.setText(String.valueOf(currentFloor - 1));
            MapDrawer.hidePolylinesFloor(currentFloor);
            MapDrawer.showPolylinesFloor(currentFloor - 1);
        }

        if (currentFloor <= 1) {
            btnFloorDown.setVisibility(View.INVISIBLE);
        }
    }


    //OnClick Close Navagation
    public void btnCloseNavigate(View view) {
        MapDrawer.removePolylines();
        MapDrawer.removeMarkers();
        //animate
        Animator.visibilityNavigationInfoBottom(Animator.Visibility.HIDE);
        Animator.visibilityNavigationInfoTop(Animator.Visibility.HIDE);
        Animator.visibilityFloorNavagator(Animator.Visibility.SHOW);
        Animator.visibilityNavigationMenu(Animator.Visibility.SHOW);
    }

    public void btnCloseNavBar(View view) {
        //animate
        Animator.visibilityRepairList(Animator.Visibility.HIDE);
        Animator.visibilityFloorNavagator(Animator.Visibility.SHOW);
        Animator.visibilityNavigationMenu(Animator.Visibility.SHOW);
    }

    //Onclick NavagationMenu
    public void btnNavBar(View view) {
        prepareListData();
        //animate
        Animator.visibilityNavigationMenu(Animator.Visibility.HIDE);
        Animator.visibilityRepairList(Animator.Visibility.SHOW);
        Animator.visibilityFloorNavagator(Animator.Visibility.HIDE);
    }

    //OnClick Navigate Between Positions
    public void btnNavigate(View view) {
        //Hide keyboard on navigate
        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

        //Get Start position
        String startPosition = editStart.getText().toString();
        String endPosition = editEnd.getText().toString();

        Algorithm.navigate(startPosition, endPosition);
    }

    //OnClick Navigate to Reparation
    public void btnNavigateRepair(View view) {
        int pos = Integer.valueOf(view.getTag().toString());
        String startRoom = (pos > 0) ? listAdapter.getChild(pos - 1, 0).toString().substring(16) : MapsActivity.editStart.getText().toString();
        String EndRoom = listAdapter.getChild(pos, 0).toString().substring(16);

        Algorithm.navigate(startRoom, EndRoom);
    }

    //OnClick Activate/Close Reparation
    public void btnCheckRepair(View view){
        //sendPushNotification("Movin", "checked a repair");

        //int pos = Integer.valueOf(view.getTag().toString());
        //Object o = listAdapter.getGroup(pos);

        HttpURLConnection urlConnection;

        try {
            URL url = new URL("http://movin.nvrstt.nl/statusdefect.php?defectid=" + "" + "&status=geaccepteerd");
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.disconnect();
        } catch (MalformedURLException u){

        } catch (IOException e){

        }
        view.setEnabled(false);
    }

    //OnClick Location From Reparation
    public void showLocation(View view) {
        MapDrawer.removePolylines();
        MapDrawer.removeMarkers();

        int pos = Integer.valueOf(view.getTag().toString());
        String room = listAdapter.getChild(pos, 0).toString().substring(16);
        LatLng getRoom = setupGraph.getRooms().getRoom(room).getLatLngBoundsCenter();
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(getRoom, 20));

        MapDrawer.addMarker(getRoom.latitude, getRoom.longitude, "Location");
        //animate
        Animator.visibilityRepairList(Animator.Visibility.HIDE);
        Animator.visibilityNavigationInfoBottom(Animator.Visibility.SHOW);
        Animator.visibilityFloorNavagator(Animator.Visibility.SHOW);
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
            setupGraph = new GraphHandler();
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
        //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(52.49985968094016, 6.0805946588516235), 18));
        //Set a marker on long click
        mMap.setOnMapLongClickListener(onMapLongClick);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        MapDrawer mapDrawer = new MapDrawer();
    }

    private void prepareListData() {
        setupGraph.getRepairReader().bindToRepairList(jitems);
        listDataHeader = setupGraph.getRepairReader().listDataHeader;
        listDataChild = setupGraph.getRepairReader().listDataChild;
        // Navigation drawer items
        listAdapter = new ExpandableListAdapterNew(this, listDataHeader, listDataChild);
        expListView.setAdapter(listAdapter);

        // Listview on child click listener
        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                // TODO Auto-generated method stub
                if (childPosition == 4) {
                    TextView editText2 = (TextView) v.findViewById(R.id.lblListItem);
                    editText2.setText("Comment:");


                    ImageButton btn = (ImageButton) v.findViewById(R.id.btnListItem);
                    btn.setVisibility(View.VISIBLE);

                    final EditText editText = (EditText) v.findViewById(R.id.lblListItemEdit);
                    editText.setVisibility(View.VISIBLE);
                    parent.requestFocus();
                    parent.setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);
                    editText.requestFocusFromTouch();
                    final View view = v;

                    btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            EditText editText = (EditText) view.findViewById(R.id.lblListItemEdit);
                            editText.setVisibility(View.GONE);

                            TextView editText2 = (TextView) view.findViewById(R.id.lblListItem);
                            editText2.setText("Comment:  " + editText.getText());

                            ImageButton btn = (ImageButton) view.findViewById(R.id.btnListItem);
                            btn.setVisibility(View.GONE);
                        }
                    });
                }

                return false;
            }
        });


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

    private LatLng getLatLngCorrection(LatLng cameraPosition) {
        double latitude = cameraPosition.latitude;
        double longitude = cameraPosition.longitude;

        if(cameraPosition.latitude < BOUNDS.southwest.latitude) {
            latitude = BOUNDS.southwest.latitude;
        }
        if(cameraPosition.longitude < BOUNDS.southwest.longitude) {
            longitude = BOUNDS.southwest.longitude;
        }
        if(cameraPosition.latitude > BOUNDS.northeast.latitude) {
            latitude = BOUNDS.northeast.latitude;
        }
        if(cameraPosition.longitude > BOUNDS.northeast.longitude) {
            longitude = BOUNDS.northeast.longitude;
        }
        return new LatLng(latitude, longitude);
    }
}


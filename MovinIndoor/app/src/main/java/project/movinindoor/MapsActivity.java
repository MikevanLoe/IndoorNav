package project.movinindoor;

import android.content.Context;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
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

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import java.net.MalformedURLException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

import project.movinindoor.Algorithm.Algorithm;
import project.movinindoor.Algorithm.NavigationRoute;
import project.movinindoor.Fragment.DFragment;
import project.movinindoor.Fragment.FloorDisplayFragment;
import project.movinindoor.Fragment.Fragment_FromToDisplay;
import project.movinindoor.Fragment.MarkerInfoFragment;
import project.movinindoor.Fragment.NavigationBar;
import project.movinindoor.Fragment.ShowNavigationCardFragment;
import project.movinindoor.Graph.Graph.Graph;
import project.movinindoor.Graph.GraphHandler;
import project.movinindoor.Readers.HttpJson;


import project.movinindoor.Models.Room;
import project.movinindoor.Readers.RepairReader;


public class MapsActivity extends FragmentActivity implements ShowNavigationCardFragment.OnFragmentInteractionListener, MarkerInfoFragment.OnFragmentInteractionListener, FloorDisplayFragment.OnFragmentInteractionListener, Fragment_FromToDisplay.OnFragmentInteractionListener, NavigationBar.OnFragmentInteractionListener {

    GoogleCloudMessaging gcm;
    String regid;
    String PROJECT_NUMBER = "607567241847";

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
    FragmentManager fm = getSupportFragmentManager();
    private FragmentManager fmRepairList, fmNavigationInfoTop, fmFloorNavigator, fmMarkerDisplay, fmNavigationCard;
    public static android.support.v4.app.Fragment fRepairList, fNavigationInfoTop, fFloorNavigator2, fMarkerDisplay, fNavigationCard;
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

        getRegId();

        fmMarkerDisplay       = getSupportFragmentManager();
        fMarkerDisplay = fmMarkerDisplay.findFragmentById(R.id.fMarkerDisplay);

        fmNavigationCard       = getSupportFragmentManager();
        fNavigationCard = fmNavigationCard.findFragmentById(R.id.fNavigationCard);

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
        fNavigationCard.getView().setVisibility(View.INVISIBLE);

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
                    Graph.movingByFoot =false;
                    infoWalkingBy.setImageDrawable(getResources().getDrawable(R.drawable.ic_local_grocery_store_black_24dp));
                    Toast.makeText(getContext(), "Cart selected", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    Graph.movingByFoot =true;
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

            MapDrawer.hidePolylinesFloorNav(currentFloor);
            MapDrawer.showPolylinesFloorNav(currentFloor + 1);
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

            MapDrawer.hidePolylinesFloorNav(currentFloor);
            MapDrawer.showPolylinesFloorNav(currentFloor - 1);
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
        Animator.visibilityCardNavigator(Animator.Visibility.HIDE);
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
    try {
        prepareListData();
    } catch (NullPointerException e) {}
        //animate
        Animator.visibilityNavigationMenu(Animator.Visibility.HIDE);
        Animator.visibilityRepairList(Animator.Visibility.SHOW);
        Animator.visibilityFloorNavagator(Animator.Visibility.HIDE);
    }

    NavigationRoute navigationRoute = null;
    //OnClick Navigate Between Positions
    public void btnNavigate(View view) {
        //Hide keyboard on navigate
        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

        //Get Start position
        String startPosition = editStart.getText().toString();
        String endPosition = editEnd.getText().toString();

        Algorithm.navigate(startPosition, endPosition);
        navigationRoute = new NavigationRoute();
    }

    //OnClick Navigate to Reparation
    public void btnNavigateRepair(View view) {
        int pos = Integer.valueOf(view.getTag().toString());
        String startRoom = (pos > 0) ? listAdapter.getChild(pos - 1, 0).toString().substring(16) : MapsActivity.editStart.getText().toString();
        String EndRoom = listAdapter.getChild(pos, 0).toString().substring(16);


        Algorithm.navigate(startRoom, EndRoom);
        navigationRoute = new NavigationRoute();
    }

    //OnClick Activate/Close Reparation
    public void btnCheckRepair(View view){
        int pos = Integer.valueOf(view.getTag().toString());
        final String tag = listAdapter.getChild(pos, 5).toString();
        final String stat = listAdapter.getChild(pos, 2).toString().substring(18);

        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String cTag = tag.substring(4);
                //Log.i("MIKE", stat);
                try {
                    HttpClient httpclient = new DefaultHttpClient();
                    HttpGet httpget;
                    switch(stat) {
                        case "Geaccepteerd":
                            httpget = new HttpGet("http://movin.nvrstt.nl/statusdefect.php?defectid=" + cTag + "&status=Gerepareerd");
                            break;
                        default:
                            httpget = new HttpGet("http://movin.nvrstt.nl/statusdefect.php?defectid=" + cTag + "&status=Geaccepteerd");
                            break;
                    }
                    HttpResponse response = httpclient.execute(httpget);
                } catch (ClientProtocolException e) {
                    //Log.i("MIKE", "ClientProtocol");
                } catch (MalformedURLException u) {
                    //Log.i("MIKE", "URL chrash");
                } catch (IOException e) {
                    //Log.i("MIKE", "IOException");
                }

                return "";
            }
        }.execute(null, null, null);
        refreshList();
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



    public void showNextCardLocation(View view) {
        double count = 0.0;
        for(int s = navigationRoute.getNum() ; s < navigationRoute.getLinkedList().size() - 1 ; s++) {
            LatLng latLng = navigationRoute.getLinkedList().get(s).getLatLng();
            //int s1 = (s+1 == navigationRoute.getLinkedList().size()) ? s+1: s;
            LatLng latLng2 = navigationRoute.getLinkedList().get(s+1).getLatLng();
            Log.i("Routeee3/1", String.valueOf(latLng.latitude));
            Log.i("Routeee3/2", String.valueOf(latLng.longitude));
            Log.i("Routeee3/3", String.valueOf(latLng.latitude));
            Log.i("Routeee3/4", String.valueOf(latLng.longitude));
            count+= CalcMath.measureMeters(latLng.latitude, latLng.longitude, latLng2.latitude, latLng2.longitude);

            Log.i("Routeee3/5", String.valueOf(count));
            Log.i("Routeee3/6", String.valueOf(CalcMath.measureMeters(latLng.latitude, latLng.longitude, latLng2.latitude, latLng2.longitude)));
        }
       String cost = Graph.calculateWalkingSpeed(count);
        Log.i("Routeee1", String.valueOf(count));
        Log.i("Routeee2", cost);
        textSpeed.setText("ETA: " + cost);
        textSpeedCost.setText("(" + String.valueOf(Math.round(count)) + "m)");
       // Animator.visibilityCardNavigator(Animator.Visibility.HIDE);
        if(navigationRoute.getNum() < navigationRoute.getLinkedList().size()) {
            ImageView imageView = (ImageView) findViewById(R.id.imgCardIcon);
            String[] split = navigationRoute.getNextCard().split(",");
            switch (split[0]) {
                case "GoStraight":      imageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_direction_up_white_36dp));
                                break;
                case "GoRight":   imageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_direction_right_white_36dp));
                                break;
                case "GoLeft":    imageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_direction_left_white_36dp));
                                break;
                case "GoSlightlyRight":  imageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_direction_sright_white_36dp));
                                break;
                case "GoSlightlyLeft":   imageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_direction_sleft_white_36dp));
                                break;
            }

            if(navigationRoute.getNum() == navigationRoute.getLinkedList().size()) imageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_place_white_36dp));
            //Animator.visibilityCardNavigator(Animator.Visibility.SHOW);
            TextView textView = (TextView) findViewById(R.id.txtCardText);
            textView.setText(split[1]);
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

    public void refreshList() {
        try {
            jitems = new HttpJson().execute("http://movin.nvrstt.nl/defectsjson.php").get();
            setupGraph.setRepairReader(new RepairReader());
            prepareListData();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        MapDrawer mapDrawer = new MapDrawer();
    }

    private void prepareListData() {
        try {
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
                    String Rid = listAdapter.getChild(groupPosition, 5).toString().substring(4);

                    TextView textView = (TextView) v.findViewById(R.id.lblListItem);

                    DFragment alertdFragment = new DFragment();
                    alertdFragment.setEditText(textView.getText().toString().substring(10));
                    alertdFragment.setRepairId(Rid);
                    alertdFragment.show(fm, "Edit Comment");


                    // Show Alert DialogFragment

                    //textView.setText("Comment:  " + editText.getText());





                }

                return false;
            }
        });

        } catch (NullPointerException e) {}
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

    public void getRegId(){
            new AsyncTask<Void, Void, String>() {
                @Override
                protected String doInBackground(Void... params) {
                    String msg = "";
                    try {
                        if (gcm == null) {
                            gcm = GoogleCloudMessaging.getInstance(getApplicationContext());
                        }
                        try {
                        regid = gcm.register(PROJECT_NUMBER);

                        } catch (NullPointerException e) {}
                        msg = "Device registered, registration ID=" + regid;
                        HttpClient httpclient = new DefaultHttpClient();
                        HttpPost httppost = new HttpPost("http://movin.nvrstt.nl/registrateid.php");

                        try {
                            // Add your data
                            List<NameValuePair> nameValuePairs = new ArrayList<>();
                            nameValuePairs.add(new BasicNameValuePair("registrationid", regid));
                            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                            // Execute HTTP Post Request
                            HttpResponse response = httpclient.execute(httppost);

                        } catch (ClientProtocolException e) {
                            // TODO Auto-generated catch block
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                        }

                        // AsyncTask<String, String, String> registrationid = PostRequest.execute("http://movin.nvrstt.nl/registrateid.php", "registrationid", msg);
                        //Log.i("GCM", msg);

                    } catch (IOException ex) {
                        msg = "Error :" + ex.getMessage();

                    }

                    return msg;
                }


            }.execute(null, null, null);
    }

    @Override
    public void onBackPressed() {
        if(fm.getBackStackEntryCount() != 0){
            fm.popBackStack();
        }
        else if(fmRepairList.getBackStackEntryCount() != 0){
            fmRepairList.popBackStack();
        }
        else if(fmNavigationInfoTop.getBackStackEntryCount() != 0){
            fmNavigationInfoTop.popBackStack();
        }
        else if(fmFloorNavigator.getBackStackEntryCount() != 0) {
            fmFloorNavigator.popBackStack();
        }
        else if(fmMarkerDisplay.getBackStackEntryCount() != 0) {
            fmMarkerDisplay.popBackStack();
        }
        else if(fmNavigationCard.getBackStackEntryCount() != 0){
            fmNavigationCard.popBackStack();
        } else {
            super.onBackPressed();
        }
    }
}


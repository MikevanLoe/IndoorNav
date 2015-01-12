package project.movinindoor;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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

import java.io.IOException;
import java.net.MalformedURLException;
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
import project.movinindoor.Graph.Node;
import project.movinindoor.Models.Room;
import project.movinindoor.Readers.HttpJson;
import project.movinindoor.Readers.RepairReader;
import project.movinindoor.Reparation.Reparation;

public class MapsActivity extends FragmentActivity implements ShowNavigationCardFragment.OnFragmentInteractionListener, MarkerInfoFragment.OnFragmentInteractionListener, FloorDisplayFragment.OnFragmentInteractionListener, Fragment_FromToDisplay.OnFragmentInteractionListener, NavigationBar.OnFragmentInteractionListener {

    private static Context context;
    private static GoogleMap mMap; // Might be null if Google Play services APK is not available.

    private static int loggedIn;
    private static int userinfo;

    public static final LatLngBounds BOUNDS = new LatLngBounds(new LatLng(52.497917, 6.076639), new LatLng(52.501379, 6.083449));
    private static GraphHandler setupGraph;

    SharedPreferences prefs;

    //ExpandableListView
    private ExpandableListAdapterNew listAdapter;
    private ExpandableListView expListView;
    private List<String> listDataHeader;
    private HashMap<String, List<String>> listDataChild;

    private Marker longClickMarker = null;
    private Room inRoom;
    private static LatLng customStartPos = null, customEndPos = null;
    private static int customStartFloor = 0, customEndFloor = 0;
    private static JSONArray jitems;

    private static EditText editStart, editEnd;
    private static TextView textSpeed, textSpeedCost, textFrom, textTo;
    private static GridLayout fNavigationInfoBottom;
    private static Button btnCurrentFloor;
    private static ImageButton btnFloorUp, btnFloorDown;

    private static LinearLayout fNavigationMenu;
    FragmentManager fm = getSupportFragmentManager();
    private static android.support.v4.app.Fragment fRepairList, fNavigationInfoTop, fFloorNavigator2, fMarkerDisplay, fNavigationCard;
    private ImageView infoWalkingBy;

    /**
     *  Getters and Setters
     */
    public static Context getContext() {
        return context;
    }
    public static GoogleMap getMap() {
        return mMap;
    }
    public static ImageButton getBtnFloorUp() {
        return btnFloorUp;
    }
    public static ImageButton getBtnFloorDown() {
        return btnFloorDown;
    }
    public static GridLayout getfNavigationInfoBottom() {
        return fNavigationInfoBottom;
    }
    public static LinearLayout getfNavigationMenu() {
        return fNavigationMenu;
    }
    public static Fragment getfRepairList() {
        return fRepairList;
    }
    public static Fragment getfNavigationInfoTop() {
        return fNavigationInfoTop;
    }
    public static Fragment getfFloorNavigator2() {
        return fFloorNavigator2;
    }
    public static Fragment getfMarkerDisplay() {
        return fMarkerDisplay;
    }
    public static Fragment getfNavigationCard() {
        return fNavigationCard;
    }
    public static GraphHandler getSetupGraph() {
        return setupGraph;
    }
    public static void setSetupGraph(GraphHandler setupGraph) {
        MapsActivity.setupGraph = setupGraph;
    } //TODO Unused method removal
    public static JSONArray getJitems() {
        return jitems;
    }
    public static int getUserID() {
        return userinfo;
    }
    public static void setJitems(JSONArray jitems) {
        MapsActivity.jitems = jitems;
    }
    public static TextView getTextSpeed() {
        return textSpeed;
    }
    public static TextView getTextSpeedCost() {
        return textSpeedCost;
    }
    public static TextView getTextFrom() {
        return textFrom;
    }
    public static TextView getTextTo() {
        return textTo;
    }
    public static EditText getEditEnd() {
        return editEnd;
    }
    public static EditText getEditStart() {
        return editStart;
    }
    public static Button getBtnCurrentFloor() {
        return btnCurrentFloor;
    }
    public static LatLng getCustomStartPos() {
        return customStartPos;
    }
    public static LatLng getCustomEndPos() {
        return customEndPos;
    }
    public static int getCustomStartFloor() {
        return customStartFloor;
    }
    public static int getCustomEndFloor() {
        return customEndFloor;
    }

    /**
     * Initialize the aplication.
     *
     * @param savedInstanceState A bundle of information.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        getActionBar().hide();

        prefs = getSharedPreferences("Login", MODE_PRIVATE);

        this.loggedIn = prefs.getInt("LoggedIn", -1);
        this.userinfo = prefs.getInt("UserID", -1);

        try {
            jitems = new HttpJson().execute("http://movin.nvrstt.nl/defectsjson.php?userid="+MapsActivity.getUserID()).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        setUpMapIfNeeded();
        context = getApplicationContext();
        setupGraph = new GraphHandler();

        fMarkerDisplay = fm.findFragmentById(R.id.fMarkerDisplay);
        fNavigationCard = fm.findFragmentById(R.id.fNavigationCard);
        fFloorNavigator2 = fm.findFragmentById(R.id.fFloorNavigator);
        fRepairList = fm.findFragmentById(R.id.fragment2);
        fNavigationInfoTop = fm.findFragmentById(R.id.fragment3);

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

                if (cameraPosition.zoom < minZoom) {
                    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(position, minZoom);
                    mMap.moveCamera(cameraUpdate);
                }
                if (position.latitude < BOUNDS.southwest.latitude || position.longitude < BOUNDS.southwest.longitude || position.latitude > BOUNDS.northeast.latitude || position.longitude > BOUNDS.northeast.longitude) {
                    LatLng correctedPosition = getLatLngCorrection(position);
                    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(correctedPosition, cameraPosition.zoom);
                    mMap.moveCamera(cameraUpdate);
                }
            }
        });

        textSpeed.setText("");
    }

    /**
     * Changes the configuration when the user changes some settings.
     *
     * @param newConfig The new configuration of the application.
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    //TODO
    public RadioGroup.OnCheckedChangeListener onCheckedChangeListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId) {
                case R.id.radioCart:
                    Graph.setMovement(false);
                    infoWalkingBy.setImageDrawable(getResources().getDrawable(R.drawable.ic_local_grocery_store_black_24dp));
                    Toast.makeText(getContext(), "Cart selected", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    Graph.setMovement(true);
                    infoWalkingBy.setImageDrawable(getResources().getDrawable(R.drawable.ic_directions_walk_black_24dp));
                    Toast.makeText(getContext(), "Walking selected", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    //TODO
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

    /**
     * Goes up one floor and show it on the map when the user presses the button.
     *
     * @param view The button that is pressed.
     */
    public void btnFloorUp(View view) {
        btnFloorDown.setVisibility(View.VISIBLE);
        int currentFloor = MapDrawer.getFloor();
        if (currentFloor < 10) {
            MapDrawer.setFloor(currentFloor + 1);
            btnCurrentFloor.setText(String.valueOf(currentFloor + 1));
            MapDrawer.hideMarkersAndPolylinesFloor(currentFloor);
            MapDrawer.showMarkersAndPolylinesFloor(currentFloor + 1);
        }

        if (currentFloor >= 9) {
            btnFloorUp.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * Removes the custom marker when the user presses the button
     *
     * @param view The button that is pressed.
     */
    public void btnMarkerClose(View view) {
        longClickMarker.remove();
        MapDrawer.removeMarkers();
        MapDrawer.removePolylines();
        Animator.visibilityMarkerInfo(Animator.Visibility.HIDE);
    }

    /**
     * Selects the custom marker as either the start or end point
     *  when the user presses the button.
     *
     * @param view The button that is pressed.
     */
    public void btnMarkerSelect(View view) {
        longClickMarker.remove();

        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.groupLocation);
        int selectedId = radioGroup.getCheckedRadioButtonId();
        RadioButton radioButton = (RadioButton) findViewById(selectedId);
        String text;

        if (radioButton.getHint().toString().equals("to")) {
            if (inRoom != null) {
                customEndPos = null;
                editEnd.setText(inRoom.getLocation());
            } else {
                customEndPos = longClickMarker.getPosition();
                customEndFloor = MapDrawer.getFloor();
                editEnd.setText("Custom End Position");
            }
            text = "End";
        } else if (radioButton.getHint().toString().equals("from")) {
            if (inRoom != null) {
                customStartPos = null;
                editStart.setText(inRoom.getLocation());
            } else {
                customStartPos = longClickMarker.getPosition();
                customStartFloor = MapDrawer.getFloor();
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

    /**
     * Goes down one floor and show it on the map when the user presses the button.
     *
     * @param view The button that is pressed.
     */
    public void btnFloorDown(View view) {
        btnFloorUp.setVisibility(View.VISIBLE);
        int currentFloor = MapDrawer.getFloor();
        if (currentFloor > 0) {
            MapDrawer.setFloor(currentFloor - 1);
            btnCurrentFloor.setText(String.valueOf(currentFloor - 1));
            MapDrawer.hideMarkersAndPolylinesFloor(currentFloor);
            MapDrawer.showMarkersAndPolylinesFloor(currentFloor - 1);
        }

        if (currentFloor <= 1) {
            btnFloorDown.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * Closes the navigation overlay and returns to the map
     *  when the user presses the button.
     *
     * @param view The button that is pressed.
     */
    public void btnCloseNavigate(View view) {
        try {
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(navigationRoute.getLinkedList().getLast().getLatLng())
                    .zoom(20)
                    .bearing(0)
                    .tilt(0)
                    .build();
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            if(MapDrawer.getFloor() != navigationRoute.getLinkedList().getLast().getFloor()) MapDrawer.setFloor(navigationRoute.getLinkedList().getLast().getFloor());
        } catch (NullPointerException e) {};

        if(navigationRoute != null) navigationRoute.reset();
        navigationRoute = null;

        MapDrawer.removePolylines();
        MapDrawer.removeMarkers();
        //animate
        Animator.visibilityCardNavigator(Animator.Visibility.HIDE);
        Animator.visibilityNavigationInfoBottom(Animator.Visibility.HIDE);
        Animator.visibilityNavigationInfoTop(Animator.Visibility.HIDE);
        Animator.visibilityFloorNavagator(Animator.Visibility.SHOW);
        Animator.visibilityNavigationMenu(Animator.Visibility.SHOW);
        currentRepair = "";
    }

    /**
     * Close the navigation menu and returns to the map
     *  when the user presses the button.
     *
     * @param view The button that is pressed.
     */
    public void btnCloseNavBar(View view) {
        //animate
        Animator.visibilityRepairList(Animator.Visibility.HIDE);
        Animator.visibilityFloorNavagator(Animator.Visibility.SHOW);
        Animator.visibilityNavigationMenu(Animator.Visibility.SHOW);
    }

    /**
     * Opens the navigation menu
     *
     * @param view The button that is pressed.
     */
    public void btnNavBar(View view) {
        try {
            boolean stat = prepareListData();
            if (stat) {
                //animate
                Animator.visibilityNavigationMenu(Animator.Visibility.HIDE);
                Animator.visibilityRepairList(Animator.Visibility.SHOW);
                Animator.visibilityFloorNavagator(Animator.Visibility.HIDE);
            } else {
                Toast.makeText(getContext(), "Repairs not loaded yet", Toast.LENGTH_SHORT).show();
            }

        } catch (NullPointerException e) {
            Toast.makeText(getContext(), "Repairs not loaded", Toast.LENGTH_SHORT).show();
        }

    }

    //TODO
    NavigationRoute navigationRoute = null;

    /**
     * Opens the navigation overlay and plots a route between
     *  two points given by the user when the user presses it.
     *
     * @param view The button that is pressed.
     */
    public void btnNavigate(View view) {
        //Hide keyboard on navigate
        try {
            InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (NullPointerException e) {
        }

        //Get Start position
        String startPosition = editStart.getText().toString();
        String endPosition = editEnd.getText().toString();


        ImageView imageView = (ImageView) findViewById(R.id.imgCardIcon);
        TextView textView = (TextView) findViewById(R.id.txtCardText);
        imageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_navigation_white_36dp));
        textView.setText("Klik voor start");

        boolean succes = Algorithm.navigate(startPosition, endPosition);
        if(succes) navigationRoute = new NavigationRoute();
    }

    /**
     * Opens the navigation overlay and plots a route between
     *  two reparations when the user presses it.
     *
     * @param view The button that is pressed.
     */
    String currentRepair = "";
    public void btnNavigateRepair(View view) {
        int pos = Integer.valueOf(view.getTag().toString());
        String startRoom = (pos > 0) ? listAdapter.getChild(pos - 1, 0).toString().substring(16) : MapsActivity.editStart.getText().toString();
        String EndRoom = listAdapter.getChild(pos, 0).toString().substring(16);
        currentRepair = EndRoom;

        ImageView imageView = (ImageView) findViewById(R.id.imgCardIcon);
        TextView textView = (TextView) findViewById(R.id.txtCardText);
        imageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_navigation_white_36dp));
        textView.setText("Klik voor start");
        boolean succes = Algorithm.navigate(startRoom, EndRoom);
        if(succes) navigationRoute = new NavigationRoute();
    }

    /**
     * Updates the status of a reparation to accepted on first press, and
     *  to repaired on the second press by the user.
     *
     * @param view The button that is pressed.
     */
    public void btnCheckRepair(View view) {
        int pos = Integer.valueOf(view.getTag().toString());
        final String tag = listAdapter.getChild(pos, 5).toString();
        final String stat = listAdapter.getChild(pos, 2).toString().substring(18);


        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String cTag = tag.substring(4);
                try {
                    HttpClient httpclient = new DefaultHttpClient();
                    HttpGet httpget;
                    switch (stat) {
                        case "Geaccepteerd":
                            httpget = new HttpGet("http://movin.nvrstt.nl/statusdefect.php?defectid=" + cTag + "&status=Gerepareerd");
                            break;
                        default:
                            httpget = new HttpGet("http://movin.nvrstt.nl/statusdefect.php?defectid=" + cTag + "&status=Geaccepteerd");
                            break;
                    }
                    HttpResponse response = httpclient.execute(httpget);
                } catch (ClientProtocolException e) {
                } catch (MalformedURLException u) {
                } catch (IOException e) {
                }

                return "";
            }
        }.execute(null, null, null);
        refreshList();
    }

    /**
     * Shows the location of the reparation when the user presses the button.
     *
     * @param view The button that is pressed.
     */
    public void showLocation(View view) {
        int pos = Integer.valueOf(view.getTag().toString());
        String room = listAdapter.getChild(pos, 0).toString().substring(16);
        Room room1 = setupGraph.getRooms().getRoom(room);
        Node node = setupGraph.getNodes().FindClosestNodeInsideRoom(room1);
        int floor = (node == null) ? room1.getFloor(): node.getFloor();
        LatLng getRoom = room1.getLatLngBoundsCenter();
        if(MapDrawer.getFloor() != floor) {
            MapDrawer.hideMarkersAndPolylinesFloor(MapDrawer.getFloor());
            MapDrawer.showMarkersAndPolylinesFloor(floor);
            MapDrawer.setFloor(floor);
        }

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(getRoom, 20));


        MapDrawer.addMarker(new LatLng(getRoom.latitude, getRoom.longitude), room1.getLocation(), floor);
        //animate
        Animator.visibilityRepairList(Animator.Visibility.HIDE);
        Animator.visibilityNavigationInfoBottom(Animator.Visibility.SHOW);
        Animator.visibilityFloorNavagator(Animator.Visibility.SHOW);
    }

    /**
     * Updates the given directions when the user presses the button.
     *
     * @param view The button that is pressed.
     */
    public void showNextCardLocation(View view) {
        double count = 0.0;
        for (int s = navigationRoute.getNum(); s < navigationRoute.getLinkedList().size() - 1; s++) {
            LatLng latLng = navigationRoute.getLinkedList().get(s).getLatLng();
            //int s1 = (s+1 == navigationRoute.getLinkedList().size()) ? s+1: s;
            LatLng latLng2 = navigationRoute.getLinkedList().get(s + 1).getLatLng();
            //MapDrawer.addPolylineNav(latLng.latitude, latLng.longitude, latLng2.latitude, latLng2.longitude, Color.GREEN, MapDrawer.getFloor() - 1);
            count += CalcMath.measureMeters(latLng.latitude, latLng.longitude, latLng2.latitude, latLng2.longitude);

        }
        String cost = Graph.getMovement().calculateMovingSpeed(count);
        textSpeed.setText("ETA: " + cost);
        textSpeedCost.setText("(" + String.valueOf(Math.round(count)) + "m)");

        ImageView imageView = (ImageView) findViewById(R.id.imgCardIcon);
        TextView textView = (TextView) findViewById(R.id.txtCardText);

        if (navigationRoute.getNum() == navigationRoute.getLinkedList().size() + 1) navigationRoute.setNum(Integer.MAX_VALUE);

        if(navigationRoute.getNum() == Integer.MAX_VALUE) {
            int countRepair = 0;
            int currentIntRepair = 0;
            for(Reparation r : setupGraph.getRepairReader().getAl()) {
                String a = (r.Building.equals(Reparation.BuildingEnum.Custom)) ? r.Building + "" + r.Location : r.Building + "" + r.Floor + "." + r.Location;
                if(currentRepair.equals(a)) currentIntRepair = countRepair;
                countRepair++;
            }
            if((currentIntRepair + 1) < setupGraph.getRepairReader().getAl().size()) {
                Reparation nextRepair = setupGraph.getRepairReader().getAl().get(currentIntRepair+1);
                imageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_navigation_white_36dp));
                textView.setText("Klik voor start");

                MapDrawer.removePolylines();
                MapDrawer.removeMarkers();
                navigationRoute.reset();

                String a = (nextRepair.Building.equals(Reparation.BuildingEnum.Custom)) ? nextRepair.Building + "" + nextRepair.Location : nextRepair.Building + "" + nextRepair.Floor + "." + nextRepair.Location;
                boolean succes = Algorithm.navigate(currentRepair, a);
                currentRepair = a;

                if (succes) navigationRoute = new NavigationRoute();
            } else{
                textView.setText("Eind");
            }
        }

        // Animator.visibilityCardNavigator(Animator.Visibility.HIDE);
        if (navigationRoute.getNum() < navigationRoute.getLinkedList().size()) {

            String[] split = navigationRoute.getNextCard().split(",");
            switch (split[0]) {
                case "GoStraight":
                    imageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_direction_up_white_36dp));
                    break;
                case "GoRight":
                    imageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_direction_right_white_36dp));
                    break;
                case "GoLeft":
                    imageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_direction_left_white_36dp));
                    break;
                case "GoSlightlyRight":
                    imageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_direction_sright_white_36dp));
                    break;
                case "GoSlightlyLeft":
                    imageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_direction_sleft_white_36dp));
                    break;
            }

            if (navigationRoute.getNum() == navigationRoute.getLinkedList().size()) {
                imageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_place_white_36dp));
                //Animator.visibilityCardNavigator(Animator.Visibility.SHOW);

                if(currentRepair.equals("")) {
                    textView.setText(split[1]);
                } else {
                    textView.setText(split[1] + "\n >Volgende reparatie<");
                    navigationRoute.setNum(navigationRoute.getNum() + 1);
                }
            } else {
                textView.setText(split[1]);
            }



        }

    }

    /**
     * Tells the program when an item in the menu is selected.
     *
     * @param item The menu item that was selected.
     * @return boolean Return false to allow normal menu processing to
     *         proceed, true to consume it here.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle your other action bar items...
        return super.onOptionsItemSelected(item);
    }

    /**
     *
     * @param savedInstanceState
     */
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    /**
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.maps_activity_actions, menu);
        return true;
    }

    /**
     *
     */
    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
        setUpGraphIfNeeded();
    }

    /**
     *
     */
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
            mMap = ((SupportMapFragment) fm.findFragmentById(R.id.map))
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
        MapDrawer mapDrawer = new MapDrawer();
        //Set a marker on long click
        mMap.setOnMapLongClickListener(onMapLongClick);
    }

    /**
     *
     */
    public void refreshList() {
        try {
            jitems = new HttpJson().execute("http://movin.nvrstt.nl/defectsjson.php?userid="+MapsActivity.getUserID()).get();
            setupGraph.setRepairReader(new RepairReader());
            prepareListData();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }

    /**
     *
     * @param uri
     */
    @Override
    public void onFragmentInteraction(Uri uri) {
        MapDrawer mapDrawer = new MapDrawer();
    }

    /**
     *
     */
    private boolean prepareListData() {
        try {
            boolean stat = setupGraph.getRepairReader().bindToRepairList();
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
            return stat;
        } catch (NullPointerException e) {
        }
        return false;
    }

    /**
     *
     * @param cameraPosition
     * @return
     */
    private LatLng getLatLngCorrection(LatLng cameraPosition) {
        double latitude = cameraPosition.latitude;
        double longitude = cameraPosition.longitude;

        if (cameraPosition.latitude < BOUNDS.southwest.latitude) {
            latitude = BOUNDS.southwest.latitude;
        }
        if (cameraPosition.longitude < BOUNDS.southwest.longitude) {
            longitude = BOUNDS.southwest.longitude;
        }
        if (cameraPosition.latitude > BOUNDS.northeast.latitude) {
            latitude = BOUNDS.northeast.latitude;
        }
        if (cameraPosition.longitude > BOUNDS.northeast.longitude) {
            longitude = BOUNDS.northeast.longitude;
        }
        return new LatLng(latitude, longitude);
    }

    /**
     * Creates a registration id to allow push notification
     *  to be send to the users phone
     */

    /**
     * When the back button on the phone is pressed,
     *  set up the map and graph if it is needed.
     */
    @Override
    public void onBackPressed() {
        setUpMapIfNeeded();
        setUpGraphIfNeeded();
    }
}
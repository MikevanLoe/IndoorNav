package project.movinindoor.Readers;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import project.movinindoor.Algorithm.Algorithm;
import project.movinindoor.Rooms.Room;
import project.movinindoor.Rooms.Rooms;
import project.movinindoor.HighPrioritySplit;
import project.movinindoor.MapsActivity;
import project.movinindoor.Reparation.Buildings;
import project.movinindoor.Reparation.Reparation;

/**
 * Created by Davey on 8-12-2014.
 */
public class RepairReader {

    private  Buildings buildings;
    public RepairReader(){
        try
        {
            List<Reparation.BuildingEnum> buildingsArray = new ArrayList<Reparation.BuildingEnum>();
            for (Reparation.BuildingEnum dir : Reparation.BuildingEnum.values()) {
                buildingsArray.add(dir);
            }

            JSONArray jitems;

            //Create buildings
            buildings = new Buildings(buildingsArray);
            try {
                jitems = MapsActivity.jitems;
            } catch (Exception e) {
                jitems = new HttpJson().execute("http://movin.nvrstt.nl/defectsjson.php").get();
            }

            //Loop though my JSONArray
                for (Integer i = 0; i < jitems.length(); i++) {
                    String title = jitems.getJSONObject(i).getString("shortdescription");
                    String floor = jitems.getJSONObject(i).getString("floor");
                    String priority = jitems.getJSONObject(i).getString("priority");
                    String description = jitems.getJSONObject(i).getString("description");
                    String clong = jitems.getJSONObject(i).getString("clong");
                    String clat = jitems.getJSONObject(i).getString("clat");
                    String comments = jitems.getJSONObject(i).getString("comments");
                    String status = jitems.getJSONObject(i).getString("status");
                    String node = jitems.getJSONObject(i).getString("defectid");

                    LatLng latLng = new LatLng(Double.valueOf(clat), Double.valueOf(clong));
                    Rooms nodeRooms = MapsActivity.setupGraph.getRooms();
                    Room nodeRoom = nodeRooms.nodeInsideRoom(latLng, Integer.valueOf(floor));

                    String room = nodeRoom.getLocation();
                    String building = room.substring(0, 1);

                    String[] splitFloor = room.split("\\.");

                    int nodeId = Integer.valueOf(node);
                    Reparation.BuildingEnum buildingEnum;
                    int floor1 = Integer.valueOf(splitFloor[0].substring(1));
                    String location = splitFloor[1];
                    LatLng latLng1 = new LatLng(Double.valueOf(clat), Double.valueOf(clong));
                    Reparation.StatusEnum statusEnum;

                    try {
                        buildingEnum = Reparation.BuildingEnum.valueOf(building);

                    } catch (IllegalArgumentException ex) {
                        buildingEnum = Reparation.BuildingEnum.A;
                    }

                    try {
                        statusEnum = Reparation.StatusEnum.valueOf(status);

                    } catch (IllegalArgumentException ex) {
                        statusEnum = Reparation.StatusEnum.NEW;
                    }


                    Reparation reparation = new Reparation(nodeId, buildingEnum, floor1 ,location, latLng1, statusEnum, Reparation.PriorityType.values()[Integer.valueOf(Integer.valueOf(priority) - 1)], title, description, comments);
                    buildings.addRepair(reparation);


                }

            Algorithm algo = new Algorithm(buildings);
            HighPrioritySplit.HighTestMethod(buildings);
            HighPrioritySplit.LowTestMethod(buildings);

        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    //ExpandableListView
    //public ExpandableListAdapterNew listAdapter;
    //public ExpandableListView expListView;
    public List<String> listDataHeader;
    public HashMap<String, List<String>> listDataChild;

    public void bindToRepairList(JSONArray jitems) {
        //listAdapter = MapsActivity.listAdapter;
        //expListView = MapsActivity.expListView;
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        List<Reparation> t = new ArrayList<Reparation>();

        for(Reparation r : t) {
            List<String> subList = new ArrayList<String>();
            listDataHeader.add(r.ShortDescription);
            subList.add("Location:       " + r.Building + "" + r.Floor + "." + r.Location);
            subList.add("Priority:          " + r.Priority);
            subList.add("Status:           " + r.Status);
            subList.add("Description:  " + r.Description);
            subList.add("Comment:  " + r.Comment);
            listDataChild.put(r.ShortDescription, subList);
        }
        //listAdapter = new ExpandableListAdapterNew(MapsActivity.getContext(), listDataHeader, listDataChild);
        //expListView.setAdapter(listAdapter);
    }

    public void prepareListData(JSONArray jitems) {
        //listAdapter = MapsActivity.listAdapter;
        //expListView = MapsActivity.expListView;
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
                   // String building = jitems.getJSONObject(i).getString("building");
                    String floor = jitems.getJSONObject(i).getString("floor");
                    String priority = jitems.getJSONObject(i).getString("priority");
                    String description = jitems.getJSONObject(i).getString("description");
                    String clong = jitems.getJSONObject(i).getString("clong");
                    String clat = jitems.getJSONObject(i).getString("clat");
                    String comments = jitems.getJSONObject(i).getString("comments");
                    String status = jitems.getJSONObject(i).getString("status");
                    String node = jitems.getJSONObject(i).getString("defectid");

                    LatLng latLng = new LatLng(Double.valueOf(clat), Double.valueOf(clong));
                    Rooms nodeRooms = MapsActivity.setupGraph.getRooms();
                    Room nodeRoom = nodeRooms.nodeInsideRoom(latLng, Integer.valueOf(floor));

                    String room = nodeRoom.getLocation();


                    List<String> subList = new ArrayList<String>();
                    listDataHeader.add(j +"" + i + " " + title);
                    if(room != null) subList.add("Location:       " + room);
                    else subList.add("Location:       " + "C" + floor + "." + "16");
                    subList.add("Priority:          " + Reparation.PriorityType.values()[Integer.valueOf(Integer.valueOf(priority) - 1)]);
                    subList.add("Status:           " + status);
                    subList.add("Description:  " + description);
                    subList.add("Comment:  " + comments);
                    listDataChild.put(j +"" + i + " " + title, subList);


                }
            }
            //listAdapter = new ExpandableListAdapterNew(MapsActivity.getContext(), listDataHeader, listDataChild);
            //expListView.setAdapter(listAdapter);
        }
        catch(Exception e)
        {
            Log.e("items_error: ", e.toString());
        }
    }
}

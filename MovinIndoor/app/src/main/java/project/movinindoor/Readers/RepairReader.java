package project.movinindoor.Readers;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import project.movinindoor.HighPrioritySplit;
import project.movinindoor.MapsActivity;
import project.movinindoor.Models.Room;
import project.movinindoor.Models.Rooms;
import project.movinindoor.Reparation.Buildings;
import project.movinindoor.Reparation.Reparation;

/**
 * Created by Davey on 8-12-2014.
 */
public class RepairReader {

    private Buildings buildings;
    private ArrayList<Reparation> al;

    public RepairReader() {
        try {
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
                try {
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
                Reparation.StatusEnum statusEnum = Reparation.StatusEnum.NEW;


                try {
                    buildingEnum = Reparation.BuildingEnum.valueOf(building);
                } catch (IllegalArgumentException ex) {
                    buildingEnum = Reparation.BuildingEnum.A;
                }

                switch (status) {
                    case "Nieuw":
                        statusEnum = Reparation.StatusEnum.NEW;
                        break;
                    case "Geaccepteerd":
                        statusEnum = Reparation.StatusEnum.ACCEPTED;
                        break;
                    case "Toegekend":
                        statusEnum = Reparation.StatusEnum.ASSIGNED;
                        break;
                    case "Gerepareerd":
                        statusEnum = Reparation.StatusEnum.DONE;
                        break;
                    case "Afgemeld":
                        statusEnum = Reparation.StatusEnum.REPAIRED;
                        break;
                }

                Reparation reparation = new Reparation(nodeId, buildingEnum, floor1, location, latLng1, statusEnum, Reparation.PriorityType.values()[Integer.valueOf(Integer.valueOf(priority) - 1)], title, description, comments);
                buildings.addRepair(reparation);
                } catch (NullPointerException e) {}
            }

            Buildings high = HighPrioritySplit.highSplit(buildings);
            //Buildings low = HighPrioritySplit.lowSplit(buildings);


            al = high.getList();
            for (Reparation r : al) {
                Log.i("REPARATIONS", "Building: " + r.Building + " Floor: " + r.getFloor() + " Priority: " + r.Priority + " Description: " + r.Description);
            }


        } catch (Exception e) {
            Log.i("ERROR123", "something went wrong with this for-loop");
            e.printStackTrace();
        }
    }


    public List<String> listDataHeader;
    public HashMap<String, List<String>> listDataChild;

    public void bindToRepairList(JSONArray jitems) {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        //List<Reparation> t = new ArrayList<Reparation>();
        try {
            for (Reparation r : al) {
                String statusName = "Nieuw";
                String PrioName = "Normaal";

                switch (r.Status) {
                    case NEW:
                        statusName = "Nieuw";
                        break;
                    case ACCEPTED:
                        statusName = "Geaccepteerd";
                        break;
                    case ASSIGNED:
                        statusName = "Toegekend";
                        break;
                    case DONE:
                        statusName = "Gerepareerd";
                        break;
                    case REPAIRED:
                        statusName = "Afgemeld";
                        break;
                }

                switch (r.Priority) {
                    case URGENT:
                        PrioName = "Urgent";
                        break;
                    case IMPORTANT:
                        PrioName = "Erg belangrijk";
                        break;
                    case HIGH:
                        PrioName = "Belangrijk";
                        break;
                    case AVERAGE:
                        PrioName = "Normaal";
                        break;
                    case LOW:
                        PrioName = "Laag";
                        break;
                    case VERYLOW:
                        PrioName = "Erg laag";
                        break;
                }

                List<String> subList = new ArrayList<String>();
                listDataHeader.add(r.ShortDescription);
                subList.add("Location:       " + r.Building + "" + r.Floor + "." + r.Location);
                subList.add("Priority:          " + PrioName);
                subList.add("Status:           " + statusName);
                subList.add("Description:  " + r.Description);
                subList.add("Comment:  " + r.Comment);
                listDataChild.put(r.ShortDescription, subList);
            }
        } catch (NullPointerException e) {
        }
    }

    public void prepareListData(JSONArray jitems) {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();
        // Navigation drawer items

        try {


            //Loop though my JSONArray
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


                String statusName = "Nieuw";
                String PrioName = "Normaal";

                switch (status) {
                    case "NEW":
                        statusName = "Nieuw";
                        break;
                    case "ACCEPTED":
                        statusName = "Geaccepteerd";
                        break;
                    case "ASSIGNED":
                        statusName = "Toegekend";
                        break;
                    case "DONE":
                        statusName = "Gerepareerd";
                        break;
                    case "REPAIRED":
                        statusName = "Afgemeld";
                        break;
                }

                switch (priority) {
                    case "6":
                        PrioName = "Urgent";
                        break;
                    case "5":
                        PrioName = "Erg belangrijk";
                        break;
                    case "4":
                        PrioName = "Belangrijk";
                        break;
                    case "3":
                        PrioName = "Normaal";
                        break;
                    case "2":
                        PrioName = "Laag";
                        break;
                    case "1":
                        PrioName = "Erg laag";
                        break;
                }

                List<String> subList = new ArrayList<String>();
                listDataHeader.add(title);
                if (room != null) subList.add("Location:       " + room);
                else subList.add("Location:       " + "C" + floor + "." + "16");
                subList.add("Priority:          " + PrioName + "|" + Reparation.PriorityType.values()[Integer.valueOf(Integer.valueOf(priority) - 1)]);
                subList.add("Status:           " + status);
                subList.add("Description:  " + description);
                subList.add("Comment:  " + comments);
                listDataChild.put(title, subList);


            }
            //listAdapter = new ExpandableListAdapterNew(MapsActivity.getContext(), listDataHeader, listDataChild);
            //expListView.setAdapter(listAdapter);
        } catch (Exception e) {
            Log.e("items_error: ", e.toString());
        }
    }


}

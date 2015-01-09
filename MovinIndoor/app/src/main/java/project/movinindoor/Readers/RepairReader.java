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

    public List<String> listDataHeader;
    public HashMap<String, List<String>> listDataChild;
    private Buildings buildings;
    private ArrayList<Reparation> al;

    public ArrayList<Reparation> getAl() {
        return al;
    }

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
                jitems = MapsActivity.getJitems();
            } catch (Exception e) {
                jitems = new HttpJson().execute("http://movin.nvrstt.nl/defectsjson.php?userid="+MapsActivity.getUserID()).get();
            }
            int customRoomCount = 1;

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
                    Rooms nodeRooms = MapsActivity.getSetupGraph().getRooms();
                    Room nodeRoom = nodeRooms.nodeInsideRoom(latLng, Integer.valueOf(floor));


                    int floor1 = Integer.valueOf(floor);
                    String location = " location " + customRoomCount;
                    Reparation.BuildingEnum buildingEnum = Reparation.BuildingEnum.Custom;

                    if (nodeRoom == null) {
                        ArrayList<ArrayList<Double>> latLngBounds = new ArrayList<>();
                        ArrayList<Double> latitide = new ArrayList<>();
                        ArrayList<Double> longitude = new ArrayList<>();
                        latitide.add(Double.valueOf(clat));
                        longitude.add(Double.valueOf(clong));
                        latLngBounds.add(longitude);
                        latLngBounds.add(latitide);
                        Room customRoom = new Room("Custom location " + customRoomCount, latLngBounds, floor1);
                        nodeRooms.getRooms().put("Custom location " + customRoomCount, customRoom);
                        customRoomCount++;
                    } else {
                        String room = nodeRoom.getLocation();
                        String building = room.substring(0, 1);

                        String[] splitFloor = room.split("\\.");
                        floor1 = Integer.valueOf(splitFloor[0].substring(1));
                        location = splitFloor[1];
                        try {
                            buildingEnum = Reparation.BuildingEnum.valueOf(building);
                        } catch (IllegalArgumentException ex) {
                            buildingEnum = Reparation.BuildingEnum.A;
                        }
                    }

                    int nodeId = Integer.valueOf(node);
                    Reparation.StatusEnum statusEnum = Reparation.StatusEnum.NEW;
                    LatLng latLng1 = new LatLng(Double.valueOf(clat), Double.valueOf(clong));

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
                } catch (NullPointerException e) {
                }
            }

            Buildings high = HighPrioritySplit.highSplit(buildings);
            al = high.getList();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean bindToRepairList() {
        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<>();

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
                if (r.Building.equals(Reparation.BuildingEnum.Custom))
                    subList.add("Location:       " + r.Building + "" + r.Location);
                else subList.add("Location:       " + r.Building + "" + r.Floor + "." + r.Location);
                subList.add("Priority:          " + PrioName);
                subList.add("Status:           " + statusName);
                subList.add("Description:  " + r.Description);
                if(r.Comment.equals("null")) subList.add("Comment:  ");
                else subList.add("Comment:  " + r.Comment);
                subList.add("id: " + r.Id);
                listDataChild.put(r.ShortDescription, subList);
            }
            return true;
        } catch (NullPointerException e) {
            return false;
        }
    }
}

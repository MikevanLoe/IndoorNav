package project.movinindoor.Graph;

import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import project.movinindoor.Algorithm.Algorithm;
import project.movinindoor.ExpandableListAdapterNew;
import project.movinindoor.HttpJson;
import project.movinindoor.MapsActivity;
import project.movinindoor.Reparation.Buildings;
import project.movinindoor.Reparation.Reparation;
import project.movinindoor.Room;
import project.movinindoor.Rooms;

/**
 * Created by Davey on 8-12-2014.
 */
public class RepairReader {

    public RepairReader(){
        try
        {
            List<Reparation.BuildingEnum> buildingsArray = new ArrayList<Reparation.BuildingEnum>();
            for (Reparation.BuildingEnum dir : Reparation.BuildingEnum.values()) {
                buildingsArray.add(dir);
            }

            //Create buildings
            Buildings buildings = new Buildings(buildingsArray);
            JSONArray jitems = new HttpJson().execute("http://movin.nvrstt.nl/defectsjson.php").get();

            //Loop though my JSONArray
            for (int  j=0; j< 10; j++) {
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

                    String[] splitFloor = room.split(".");

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
            }

            Algorithm algo = new Algorithm(buildings);


        }
        catch(Exception e)
        {
            Log.e("items_error: ", e.toString());
        }
    }
}

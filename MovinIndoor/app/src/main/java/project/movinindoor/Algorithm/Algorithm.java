/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package project.movinindoor.Algorithm;

import java.util.List;

import project.movinindoor.MapsActivity;
import project.movinindoor.Reparation.Buildings;
import project.movinindoor.Rooms.Room;

/**
 *
 * @author Davey
 */

public class Algorithm {
    public Buildings buildings;


    public Algorithm(Buildings campusWin) {
        buildings = campusWin;
    }

    public void findNearestElevator(String startPos, String name) {
        List<Room> roomsWithName = MapsActivity.setupGraph.getRooms().getAllRoomsWithName(name);
        /*
        if(roomsWithName.size() != 1) {
            double tempCost = 0.0;
            Room tempRoom = null;
            for (Room room : roomsWithName) {
                Node node = MapsActivity.setupGraph.FindClosestNodeInsideRoom(room);
                //find shortest route to position
                if(node != null) {
                    double cost = g.drawPath(startNode.nodeId.toString(), node.nodeId.toString());
                    if(tempCost == 0.0 && tempRoom == null) {
                        tempCost = cost;
                        tempRoom = room;
                    }

                    if (cost <= tempCost) {
                        tempCost = cost;
                        tempRoom = room;
                    }
                }

            }
            MapDrawer.removePolylines();
            endRoom = tempRoom;
        } else {
            endRoom = rooms.getRoom(endPosition);
        }
        */
    }
    
}

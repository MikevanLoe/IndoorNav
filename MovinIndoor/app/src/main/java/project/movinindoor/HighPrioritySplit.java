package project.movinindoor;

import java.util.Collections;
import java.util.PriorityQueue;
import java.util.Queue;

import project.movinindoor.Reparation.Building;
import project.movinindoor.Reparation.Buildings;
import project.movinindoor.Reparation.Floor;

/**
 * Created by 5736z454 on 24-11-2014.
 */
public class HighPrioritySplit {

    public void split(Buildings input){

        Queue buildingQ = new PriorityQueue(15, Collections.reverseOrder());
        for(Building B : input.buildingList.values()) {
            buildingQ.offer(B);
        }

        Building A = (Building) buildingQ.poll();

        Queue floorQ = new PriorityQueue(200, Collections.reverseOrder());
        for(Floor B : A.floorList.values()){
            floorQ.offer(B);
        }
    }
}

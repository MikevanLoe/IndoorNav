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

    public void highSplit(Buildings input){

        Queue buildingQ = new PriorityQueue(15, Collections.reverseOrder());
        for(Building B : input.buildingList.values()) {
            buildingQ.offer(B);
        }

        Queue floorQ = new PriorityQueue(450, Collections.reverseOrder());
        boolean time = true;
        while(time) {
            Building B = (Building) buildingQ.poll();
            if(B != null)gi
                for (Floor F : B.floorList.values())
                    floorQ.offer(F);
            else
                time = false;
        }



    }
}

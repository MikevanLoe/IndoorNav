package project.movinindoor;

import java.util.Collections;
import java.util.PriorityQueue;
import java.util.Queue;

import project.movinindoor.Reparation.Building;
import project.movinindoor.Reparation.Buildings;
import project.movinindoor.Reparation.Floor;
import project.movinindoor.Reparation.Reparation;

/**
 * Created by 5736z454 on 24-11-2014.
 */
public class HighPrioritySplit {

    public void highSplit(Buildings input){

        Queue buildingQ = new PriorityQueue(15, Collections.reverseOrder());
        for(Building B : input.buildingList.values()) {
            buildingQ.offer(B);
        }

        Queue tempQ = buildingQ;
        boolean time = true;

        while(time) {
            Building temp = (Building) tempQ.poll();
            if(time) {
                temp.order = new PriorityQueue(temp.totalFloors, Collections.reverseOrder());
                for (Floor f : temp.floorList.values()) {
                    temp.order.add(f);
                }
            }
            else
                time = false;
        }

        tempQ = buildingQ;
        time = true;

        while (time) {
            Floor tempF = (Floor) tempQ.poll();
            if(tempF != null) {
                for (Reparation rep : tempF.repairList.values()) {
                    if (rep.Priority.value >= 5) {
                        tempF.order.add(rep);
                        tempF.repairList.remove(rep);
                        input.calculatePriorityFloor(rep.Building, rep.Floor);
                        tempQ.add(tempF);
                    }
                }
            }
            else
                time = false;
        }
        /*
        * kijk in de Queue of er prio 5/6 op de verdieping is                                       foreach(REP : floor)
        * sla prio 5/6 op in prioQueue                                                              if(REP.prio.value == 5 || 6) tmpFloor.add(REP) 5<=tmpFloor.prio<=6
        * herbereken verdieping prio na het verwijderen van de prio 5/6                             (voor) 0/null<=floor.prio<=6 (na) 0/null<=floor.prio<=4
        * plaats verdiepingen weer in prioQueue                                                     END
        */


    }
}

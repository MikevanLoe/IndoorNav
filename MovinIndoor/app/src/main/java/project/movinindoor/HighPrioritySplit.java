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
    /**
     * Splits the input based on the priority of repairs.
     * Takes the high repairs, and follows with the medium and low priority
     *  repairs.
     *
     * @param input the buildings object you want to split
     */
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
            tempF.highOrder = new PriorityQueue(100, Collections.reverseOrder());
            tempF.lowOrder = new PriorityQueue(100, Collections.reverseOrder());

            if(tempF != null) {
                for (Reparation rep : tempF.repairList.values()) {
                    if(rep.Priority.value >= 5) {
                        tempF.highOrder.add(rep);
                        tempF.repairList.remove(rep);
                        input.calculatePriorityFloor(rep.Building, rep.Floor);
                        if (rep.Priority.value > 0) {
                            tempQ.add(tempF);
                        }
                    }
                    else if (tempF.priority.value <=4 ){
                        tempF.lowOrder.add(rep);
                        tempF.repairList.remove(rep);
                        input.calculatePriorityFloor(rep.Building, rep.Floor);
                        if (rep.Priority.value > 0) {
                            tempQ.add(tempF);
                        }
                    }
                }
            }
            else {
                time = false;
            }
        }
    }

    /**
     * Splits the input based on the priority of repairs.
     * Takes the high and medium repairs, and follows with the low priority
     *  repairs.
     *
     * @param input the buildings object you want to split
     */
    public void lowSplit(Buildings input){
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
            tempF.highOrder = new PriorityQueue(100, Collections.reverseOrder());
            tempF.lowOrder = new PriorityQueue(100, Collections.reverseOrder());

            if(tempF != null) {
                for (Reparation rep : tempF.repairList.values()) {
                    if(rep.Priority.value >= 3) {
                        tempF.highOrder.add(rep);
                        tempF.repairList.remove(rep);
                        input.calculatePriorityFloor(rep.Building, rep.Floor);
                        if (rep.Priority.value > 0) {
                            tempQ.add(tempF);
                        }
                    }
                    else if (tempF.priority.value <=2 ){
                        tempF.lowOrder.add(rep);
                        tempF.repairList.remove(rep);
                        input.calculatePriorityFloor(rep.Building, rep.Floor);
                        if (rep.Priority.value > 0) {
                            tempQ.add(tempF);
                        }
                    }
                }
            }
            else {
                time = false;
            }
        }
    }
}
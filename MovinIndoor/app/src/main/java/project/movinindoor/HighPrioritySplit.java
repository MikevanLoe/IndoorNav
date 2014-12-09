package project.movinindoor;

import android.util.Log;

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
    private static final String LOG_TAG = "Tag" ;

    /**
     * Splits the input based on the priority of repairs.
     * Takes the high repairs, and follows with the medium and low priority
     *  repairs.
     *
     * @param input the buildings object you want to split
     */
    public static void highSplit(Buildings input){
        Queue buildingQ = new PriorityQueue(15, Collections.reverseOrder());
        for(Building B : input.buildingList.values()) {
            buildingQ.offer(B);
        }

        Queue tempQ = buildingQ;
        boolean time = true;

        // Loop through the buildings and add the floors to a PriorityQueue
        // but only if there are buildings left.
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

        // Loop through the floors and add the repairs to a PriorityQueue
        // but only if there are floors left.
        while (time) {
            Floor tempF = (Floor) tempQ.poll();
            tempF.highOrder = new PriorityQueue(100, Collections.reverseOrder());
            tempF.lowOrder = new PriorityQueue(100, Collections.reverseOrder());

            if(tempF != null) {
                for (Reparation rep : tempF.repairList.values()) {
                    // If the priority of the repair is equal or higher than 5
                    // add it to the high order queue and remove it from the list
                    if(rep.Priority.value >= 5) {
                        tempF.highOrder.add(rep);
                        tempF.repairList.remove(rep);
                        input.calculatePriorityFloor(rep.Building, rep.Floor);
                        if (rep.Priority.value > 0) {
                            tempQ.add(tempF);
                        }
                    }
                    // If the priority of the floor is equal or lower than 4
                    // add it to the low order queue and remove it from the list
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
    public static void lowSplit(Buildings input){
        Queue buildingQ = new PriorityQueue(15, Collections.reverseOrder());
        for(Building B : input.buildingList.values()) {
            buildingQ.offer(B);
        }

        Queue tempQ = buildingQ;
        boolean time = true;

        // Loop through the buildings and add the floors to a PriorityQueue
        // but only if there are buildings left.
        while(time) {
            Building temp = (Building) tempQ.poll();
            if(temp != null) {
                temp.order = new PriorityQueue(temp.totalFloors, Collections.reverseOrder());
                for (Floor f : temp.floorList.values()) {
                    temp.order.offer(f);
                }
            }
            else
                time = false;
        }

        tempQ = buildingQ;
        time = true;

        // Loop through the floors and add the repairs to a PriorityQueue
        // but only if there are floors left.
        while (time) {
            Floor tempF = (Floor) tempQ.poll();
            tempF.highOrder = new PriorityQueue(100, Collections.reverseOrder());
            tempF.lowOrder = new PriorityQueue(100, Collections.reverseOrder());

            // If there is a building left, loop through the repairlist
            if(tempF != null) {
                for (Reparation rep : tempF.repairList.values()) {
                    // If the priority of the repair is equal or higher than 3
                    // add it to the high order queue and remove it from the list
                    if(rep.Priority.value >= 3) {
                        tempF.highOrder.add(rep);
                        tempF.repairList.remove(rep);
                        input.calculatePriorityFloor(rep.Building, rep.Floor);
                        if (rep.Priority.value > 0) {
                            tempQ.add(tempF);
                        }
                    }
                    // If the priority of the floor is equal or lower than 2
                    // add it to the low order queue and remove it from the list
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

    /**
     *
     */
    public static void HighTestMethod (Buildings input){
        Log.i(HighPrioritySplit.LOG_TAG, "Test number 1");
        System.out.println("systeem test");
        highSplit(input);
        for (Building b: input.buildingList.values()){
            for(Floor f : b.floorList.values()){

                for(Object r : f.highOrder) {
                    Reparation s = (Reparation) r;
                    Log.i("", s.Description + " " + s.Location);
                }
                for(Object r : f.lowOrder){
                    Reparation s = (Reparation) r;
                    Log.i("", s.Description + " " + s.Location);
                }
            }
        }
    }

    public static void LowTestMethod(Buildings input){
        Log.i(HighPrioritySplit.LOG_TAG, "Test number 2");
        System.out.println("test");
        lowSplit(input);
        for (Building b: input.buildingList.values()){
            for(Floor f : b.floorList.values()){
                for(Object r : f.highOrder) {
                    Reparation s = (Reparation) r;
                    Log.i("A", s.Description + " " + s.Location);
                }
                for(Object r : f.lowOrder){
                    Reparation s = (Reparation) r;
                    Log.i("A", s.Description + " " + s.Location);
                }
            }
        }
    }
}
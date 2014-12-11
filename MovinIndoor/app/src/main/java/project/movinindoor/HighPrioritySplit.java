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
    private static final String LOG_TAG = "Mike" ;

    /**
     * Splits the input based on the priority of repairs.
     * Takes the high repairs, and follows with the medium and low priority
     *  repairs.
     *
     * @param input the buildings object you want to split
     */
    public static void highSplit(Buildings input){
        for(Reparation.BuildingEnum b : input.buildingList.keySet()) {
            input.calculatePriorityBuilding(b);
        }

        Queue buildingQ = new PriorityQueue(input.buildingList.size(), Collections.reverseOrder());
        for(Building B : input.buildingList.values()) {
            buildingQ.add(B);
        }

        input.order = buildingQ;

        // Loop through the buildings and add the floors to a PriorityQueue
        // but only if there are buildings left.
        for(Object b : input.order) {
            Building c = (Building) b;
            c.order = new PriorityQueue(c.totalFloors, Collections.reverseOrder());
            for (Floor f : c.floorList.values()) {
                c.order.add(f);
            }
        }

        // Loop through the floors and add the repairs to a PriorityQueue
        // but only if there are floors left.
        for(Object b : input.order){
            Building c = (Building) b;
            for(Floor f : c.floorList.values()) {
                f.highOrder = new PriorityQueue(100, Collections.reverseOrder());
                f.lowOrder = new PriorityQueue(100, Collections.reverseOrder());

                for (Reparation rep : f.repairList.values()) {
                    // If the priority of the repair is equal or higher than 5
                    // add it to the high order queue and remove it from the list
                    if (rep.Priority.value >= 5) {
                        f.highOrder.add(rep);
                    }
                    // If the priority of the floor is equal or lower than 4
                    // add it to the low order queue and remove it from the list
                    else if (f.priority.value <= 4) {
                        f.lowOrder.add(rep);
                    }
                }
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
        for(Reparation.BuildingEnum b : input.buildingList.keySet()) {
            input.calculatePriorityBuilding(b);
        }

        Queue buildingQ = new PriorityQueue(input.buildingList.size(), Collections.reverseOrder());
        for(Building B : input.buildingList.values()) {
            buildingQ.add(B);
        }

        input.order = buildingQ;

        // Loop through the buildings and add the floors to a PriorityQueue
        // but only if there are buildings left.
        for(Object b : input.order) {
            Building c = (Building) b;
            c.order = new PriorityQueue(c.totalFloors, Collections.reverseOrder());
            for (Floor f : c.floorList.values()) {
                c.order.add(f);
            }
        }

        // Loop through the floors and add the repairs to a PriorityQueue
        // but only if there are floors left.
        for(Object b : input.order){
            Building c = (Building) b;
            for(Floor f : c.floorList.values()) {
                f.highOrder = new PriorityQueue(100, Collections.reverseOrder());
                f.lowOrder = new PriorityQueue(100, Collections.reverseOrder());

                for (Reparation rep : f.repairList.values()) {
                    // If the priority of the repair is equal or higher than 5
                    // add it to the high order queue and remove it from the list
                    if (rep.Priority.value >= 3) {
                        f.highOrder.add(rep);
                    }
                    // If the priority of the floor is equal or lower than 4
                    // add it to the low order queue and remove it from the list
                    else if (f.priority.value <= 2) {
                        f.lowOrder.add(rep);
                    }
                }
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
                    Log.i(HighPrioritySplit.LOG_TAG, s.Description + " " + s.Location);
                }
                for(Object r : f.lowOrder){
                    Reparation s = (Reparation) r;
                    Log.i(HighPrioritySplit.LOG_TAG, s.Description + " " + s.Location);
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
                    Log.i(HighPrioritySplit.LOG_TAG, s.Description + " " + s.Location);
                }
                for(Object r : f.lowOrder){
                    Reparation s = (Reparation) r;
                    Log.i(HighPrioritySplit.LOG_TAG, s.Description + " " + s.Location);
                }
            }
        }
    }
}
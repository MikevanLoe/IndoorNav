/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package project.movinindoor.Reparation;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

/**
 *
 * @author Davey
 */
public class Building implements Comparable<Building>{
    
    public Map<Integer, Floor> floorList;
    public Reparation.PriorityType priority;
    public Queue<Floor> order;
    public int totalFloors;

    public Building() {
        totalFloors = 10;
        floorList = new HashMap<Integer, Floor>();

        for (int i = 0; i <= totalFloors; i++) {
            Floor floor = new Floor();
            floorList.put(i, floor);
        }
    }

    public Building(int totalFloors) {
        this.totalFloors = totalFloors;
        floorList = new HashMap<Integer, Floor>();

        for (int i = 0; i <= totalFloors; i++) {
            Floor floor = new Floor();
            floorList.put(i, floor);
        }

    }

    /**
     * Adds a floor to the building.
     *
     * @param name the NodeId of the floor being added.
     * @param obj the floor being added.
     */
    public void Add(int name, Floor obj) {
        floorList.put(name, obj);
    }

    /**
     * Removes a floor from the building.
     *
     * @param obj the object to remove.
     */
    public void Remove(Floor obj) {
        floorList.remove(obj);
    }

    /**
     * Compare two buildings with eachother.
     *
     * @param b the building you compare to.
     * @return an int to define the largest or smallest.
     */
    @Override
    public int compareTo(Building b) {
        if((b.priority == null)||(this.priority == null)) {
            return 0;
        }
        else if(this.priority.getValue() == b.priority.getValue()){
            return 0;
        }
        else if(this.priority.getValue() < b.priority.getValue()){
            return 1;
        }
        else if(this.priority.getValue() > b.priority.getValue()){
            return -1;
        }
        else{
            return 0;
        }
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package project.movinindoor.Reparation;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 *
 * @author Davey
 */
public class Building implements Comparable<Building>{
    
    public Map<Integer, Floor> floorList;
    public Reparation.PriorityType priority;
    public Queue order;
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
     *
     * @param name
     * @param obj
     */
    public void Add(int name, Floor obj) {
        floorList.put(name, obj);
    }

    /**
     *
     * @param obj
     */
    public void Remove(Floor obj) {
        floorList.remove(obj);
    }

    /**
     *
     * @param b
     * @return
     */
    @Override
    public int compareTo(Building b) {

        if(this.priority.getValue() < b.priority.getValue()){
            return -1;
        }else if(this.priority.getValue() > b.priority.getValue()){
            return 1;
        }else{
            return 0;
        }
    }
}

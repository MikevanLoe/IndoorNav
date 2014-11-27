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
public class Floor implements Comparable<Floor>{
    
    public Map<Integer, Reparation> repairList;
    public Reparation.PriorityType priority;
    public Queue order;
    
    public Floor() {
        repairList = new HashMap<Integer, Reparation>();
    }

    /**
     *
     * @param name
     * @param obj
     */
    public void Add(int name, Reparation obj) {
        repairList.put(name, obj);
    }

    /**
     *
     * @param obj
     */
    public void Remove(Reparation obj) {
        repairList.remove(obj);
    }

    /**
     *
     * @param b
     * @return
     */
    @Override
    public int compareTo(Floor b) {

        if(this.priority.getValue() < b.priority.getValue()){
            return -1;
        }else if(this.priority.getValue() > b.priority.getValue()){
            return 1;
        }else{
            return 0;
        }
    }
}

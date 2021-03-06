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
public class Floor implements Comparable<Floor>{
    
    public Map<String, Reparation> repairList;
    public Reparation.PriorityType priority;
    public Queue<Reparation> highOrder;
    public Queue<Reparation> lowOrder;
    
    public Floor() {
        repairList = new HashMap<String, Reparation>();
    }

    /**
     * Adds a floor to the building.
     *
     * @param name the name of the floor being added.
     * @param obj the floor being added.
     */
    public void Add(String name, Reparation obj) {
        repairList.put(name, obj);
    }

    /**
     * Removes a reparation from the floor.
     *
     * @param obj the object to remove.
     */
    public void Remove(Reparation obj) {
        repairList.remove(obj);
    }

    /**
     * Compare two floors with eachother.
     *
     * @param b the floor you compare to.
     * @return an int to define the largest or smallest.
     */
    @Override
    public int compareTo(Floor b) {
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

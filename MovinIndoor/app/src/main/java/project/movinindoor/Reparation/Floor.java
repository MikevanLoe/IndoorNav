/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package project.movinindoor.Reparation;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Davey
 */
public class Floor {
    
    Map<Integer, Reparation> repairList;
    public Reparation.PriorityType priority;
    
    public Floor() {
        repairList = new HashMap<Integer, Reparation>();
    }
    
    public void Add(int name, Reparation obj) {
        repairList.put(name, obj);
    }
    
    public void Remove(Reparation obj) {
        repairList.remove(obj);
    }
}

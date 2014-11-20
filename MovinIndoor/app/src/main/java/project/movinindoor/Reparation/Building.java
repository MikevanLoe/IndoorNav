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
public class Building {
    
    public Map<String, Floor> floorList;

    public Building() {
        floorList = new HashMap<String, Floor>();
        
        Floor floor0 = new Floor();       
        Floor floor1 = new Floor();
        Floor floor2 = new Floor();
        Floor floor3 = new Floor();
        Floor floor4 = new Floor();
        Floor floor5 = new Floor();
        
        floorList.put("0", floor0);        
        floorList.put("1", floor1);
        floorList.put("2", floor2);
        floorList.put("3", floor3);
        floorList.put("4", floor4);
        floorList.put("5", floor5);
        
        
    }
    
    public void Add(String name, Floor obj) {
        floorList.put(name, obj);
    }
    
    public void Remove(Floor obj) {
        floorList.remove(obj);
    }
}

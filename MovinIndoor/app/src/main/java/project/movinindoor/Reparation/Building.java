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
    
    Map<String, Floor> floorList;
    public Priority priority;

    public Building() {
        int totalFloors = 10;
        floorList = new HashMap<String, Floor>();

        for (int i = 0; i <= totalFloors; i++) {
            Floor floor = new Floor(); 
            floorList.put(Integer.toString(i), floor); 
        }  
    }
    
    public Building(int totalFloors) {
        floorList = new HashMap<String, Floor>();

        for (int i = 0; i <= totalFloors; i++) {
            Floor floor = new Floor(); 
            floorList.put(Integer.toString(i), floor); 
        }
         
    }
    
    public void Add(String name, Floor obj) {
        floorList.put(name, obj);
    }
    
    public void Remove(Floor obj) {
        floorList.remove(obj);
    }
}

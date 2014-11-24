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
    
    Map<Integer, Floor> floorList;
    public Reparation.PriorityType priority;

    public Building() {
        int totalFloors = 10;
        floorList = new HashMap<Integer, Floor>();

        for (int i = 0; i <= totalFloors; i++) {
            Floor floor = new Floor();
            floorList.put(i, floor);
        }
    }

    public Building(int totalFloors) {
        floorList = new HashMap<Integer, Floor>();

        for (int i = 0; i <= totalFloors; i++) {
            Floor floor = new Floor();
            floorList.put(i, floor);
        }

    }

    public void Add(int name, Floor obj) {
        floorList.put(name, obj);
    }
    
    public void Remove(Floor obj) {
        floorList.remove(obj);
    }
}

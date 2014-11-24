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
    
<<<<<<< HEAD
    Map<Integer, Floor> floorList;
    public Reparation.PriorityType priority;

    public Building() {
        int totalFloors = 10;
        floorList = new HashMap<Integer, Floor>();

        for (int i = 0; i <= totalFloors; i++) {
            Floor floor = new Floor(); 
            floorList.put(i, floor);
=======
    Map<String, Floor> floorList;
    public Priority priority;

    public Building() {
        int totalFloors = 10;
        floorList = new HashMap<String, Floor>();

        for (int i = 0; i <= totalFloors; i++) {
            Floor floor = new Floor(); 
            floorList.put(Integer.toString(i), floor); 
>>>>>>> a0a4c60641c3717e61d14641c85e327d4f76f426
        }  
    }
    
    public Building(int totalFloors) {
<<<<<<< HEAD
        floorList = new HashMap<Integer, Floor>();

        for (int i = 0; i <= totalFloors; i++) {
            Floor floor = new Floor(); 
            floorList.put(i, floor);
=======
        floorList = new HashMap<String, Floor>();

        for (int i = 0; i <= totalFloors; i++) {
            Floor floor = new Floor(); 
            floorList.put(Integer.toString(i), floor); 
>>>>>>> a0a4c60641c3717e61d14641c85e327d4f76f426
        }
         
    }
    
<<<<<<< HEAD
    public void Add(int name, Floor obj) {
=======
    public void Add(String name, Floor obj) {
>>>>>>> a0a4c60641c3717e61d14641c85e327d4f76f426
        floorList.put(name, obj);
    }
    
    public void Remove(Floor obj) {
        floorList.remove(obj);
    }
}

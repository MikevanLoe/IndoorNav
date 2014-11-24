/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package project.movinindoor.Reparation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Davey
 */
public class Buildings {
    
    Map<Reparation.BuildingEnum, Building> buildingList;
    
    public Buildings() {
        buildingList = new HashMap<Reparation.BuildingEnum, Building>();
    }
    
    public Buildings(List<Reparation.BuildingEnum> buildings) {
        buildingList = new HashMap<Reparation.BuildingEnum, Building>();
        for (Reparation.BuildingEnum buildingEnum : buildings) {
            Building building = new Building();
            buildingList.put(buildingEnum, building);
        }
    }
    
    public Buildings(List<Reparation.BuildingEnum> buildings, int totalFloors) {
        buildingList = new HashMap<Reparation.BuildingEnum, Building>();
        for (Reparation.BuildingEnum buildingEnum : buildings) {
            Building building = new Building(totalFloors);
            buildingList.put(buildingEnum, building);
        }
    }
    
    public void Add(Reparation.BuildingEnum name, Building obj) {
        buildingList.put(name, obj);
    }
    
    public void Remove(Building obj) {
        buildingList.remove(obj);
    }
 
    public void setRepairs(Reparation.BuildingEnum building, int floor, Reparation reparation) {
<<<<<<< HEAD
        Map<Integer, Floor> getBuilding = getRepairsFromBuilding(building);
=======
        Map<String, Floor> getBuilding = getRepairsFromBuilding(building); 
>>>>>>> a0a4c60641c3717e61d14641c85e327d4f76f426
        Floor floorR = getBuilding.get(Integer.toString(floor));
        floorR.Add(reparation.Location, reparation);
    }
    
<<<<<<< HEAD
    public Map<Integer, Floor> getRepairsFromBuilding(Reparation.BuildingEnum building) {
=======
    public Map<String, Floor> getRepairsFromBuilding(Reparation.BuildingEnum building) {
>>>>>>> a0a4c60641c3717e61d14641c85e327d4f76f426
        Building buildingR = buildingList.get(building);
        return buildingR.floorList;
    }
    
<<<<<<< HEAD
    public Map<Integer, Reparation> getRepairsFromFloor(Reparation.BuildingEnum building, int floor) {
        Map<Integer, Floor> getBuilding = getRepairsFromBuilding(building);
=======
    public Map<Integer, Reparation> getRepairsFromFloor(Reparation.BuildingEnum building, String floor) {
        Map<String, Floor> getBuilding = getRepairsFromBuilding(building); 
>>>>>>> a0a4c60641c3717e61d14641c85e327d4f76f426
        Floor floorR = getBuilding.get(floor);
        return floorR.repairList;
    }
    
    public Reparation getReparation(Reparation.BuildingEnum building, int floor, int location) {
<<<<<<< HEAD
        Map<Integer, Floor> getBuilding = getRepairsFromBuilding(building);
=======
        Map<String, Floor> getBuilding = getRepairsFromBuilding(building); 
>>>>>>> a0a4c60641c3717e61d14641c85e327d4f76f426
        Floor floorR = getBuilding.get(Integer.toString(floor));
        return floorR.repairList.get(location);
    }
    
    
<<<<<<< HEAD
    public Reparation getRepairsFromLocation(Reparation.BuildingEnum building, int floor, int location) {
=======
    public Reparation getRepairsFromLocation(Reparation.BuildingEnum building, String floor, int location) {
>>>>>>> a0a4c60641c3717e61d14641c85e327d4f76f426
        Map<Integer, Reparation> getFloor = getRepairsFromFloor(building, floor);
        Reparation reparationR = getFloor.get(location);
        return reparationR;
    }
    
<<<<<<< HEAD
    public void setPriorityBuilding(Reparation.BuildingEnum building, Reparation.PriorityType priority) {
=======
    public void setPriorityBuilding(Reparation.BuildingEnum building, Priority priority) {
>>>>>>> a0a4c60641c3717e61d14641c85e327d4f76f426
        Building buildingR = buildingList.get(building);
        buildingR.priority = priority;
    }
    
<<<<<<< HEAD
    public Reparation.PriorityType getPriorityBuilding(Reparation.BuildingEnum building) {
=======
    public Priority getPriorityBuilding(Reparation.BuildingEnum building) {
>>>>>>> a0a4c60641c3717e61d14641c85e327d4f76f426
        Building buildingR = buildingList.get(building);
        return buildingR.priority;
    }
    
<<<<<<< HEAD
    public void setPriorityFloor(Reparation.BuildingEnum building, int floor, Reparation.PriorityType priority) {
         Map<Integer, Floor> getBuilding = getRepairsFromBuilding(building);
=======
    public void setPriorityFloor(Reparation.BuildingEnum building, int floor, Priority priority) {
         Map<String, Floor> getBuilding = getRepairsFromBuilding(building); 
>>>>>>> a0a4c60641c3717e61d14641c85e327d4f76f426
         Floor floorR = getBuilding.get(floor);
         floorR.priority = priority;
    }
    
<<<<<<< HEAD
    public Reparation.PriorityType setPriorityFloor(Reparation.BuildingEnum building, int floor) {
         Map<Integer, Floor> getBuilding = getRepairsFromBuilding(building);
=======
    public Priority setPriorityFloor(Reparation.BuildingEnum building, int floor) {
         Map<String, Floor> getBuilding = getRepairsFromBuilding(building); 
>>>>>>> a0a4c60641c3717e61d14641c85e327d4f76f426
         Floor floorR = getBuilding.get(floor);
         return floorR.priority;
    }
}





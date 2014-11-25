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
 
    public void addRepair(Reparation.BuildingEnum building, int floor, Reparation reparation) {
        Map<Integer, Floor> getBuilding = getRepairsFromBuilding(building);
        Floor floorR = getBuilding.get(Integer.toString(floor));
        floorR.Add(reparation.Location, reparation);
    }

    public Map<Integer, Floor> getRepairsFromBuilding(Reparation.BuildingEnum building) {
        Building buildingR = buildingList.get(building);
        return buildingR.floorList;
    }

    public Map<Integer, Reparation> getRepairsFromFloor(Reparation.BuildingEnum building, int floor) {
        Map<Integer, Floor> getBuilding = getRepairsFromBuilding(building);
        Floor floorR = getBuilding.get(floor);
        return floorR.repairList;
    }

    /*
    duplicate from getRepairs
    public Reparation getReparation(Reparation.BuildingEnum building, int floor, int location) {
        Map<Integer, Floor> getBuilding = getRepairsFromBuilding(building);
        Floor floorR = getBuilding.get(Integer.toString(floor));
        return floorR.repairList.get(location);
    }
    */

    public Reparation getReparation(Reparation.BuildingEnum building, int floor, int location) {
        Map<Integer, Reparation> getFloor = getRepairsFromFloor(building, floor);
        Reparation reparationR = getFloor.get(location);
        return reparationR;
    }

    public void setPriorityBuilding(Reparation.BuildingEnum building, Reparation.PriorityType priority) {
        Building buildingR = buildingList.get(building);
        buildingR.priority = priority;
    }

    public Reparation.PriorityType getPriorityBuilding(Reparation.BuildingEnum building) {
        Building buildingR = buildingList.get(building);
        return buildingR.priority;
    }

    public void setPriorityFloor(Reparation.BuildingEnum building, int floor, Reparation.PriorityType priority) {
         Map<Integer, Floor> getBuilding = getRepairsFromBuilding(building);
         Floor floorR = getBuilding.get(floor);
         floorR.priority = priority;
    }

    public Reparation.PriorityType getPriorityFloor(Reparation.BuildingEnum building, int floor) {
         Map<Integer, Floor> getBuilding = getRepairsFromBuilding(building);
         Floor floorR = getBuilding.get(floor);
         return floorR.priority;
    }
}





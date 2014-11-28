/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package project.movinindoor.Reparation;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 *
 * @author Davey
 */
public class Buildings {
    public Queue order;
    public Map<Reparation.BuildingEnum, Building> buildingList;
    
    public Buildings() {
        buildingList = new HashMap<Reparation.BuildingEnum, Building>();
    }

    public Buildings(List<Reparation.BuildingEnum> buildings) {
        order = new PriorityQueue(buildings.size(), Collections.reverseOrder());
        buildingList = new HashMap<Reparation.BuildingEnum, Building>();
        for (Reparation.BuildingEnum buildingEnum : buildings) {
            Building building = new Building();
            buildingList.put(buildingEnum, building);
        }
    }

    public Buildings(List<Reparation.BuildingEnum> buildings, int totalFloors) {
        order = new PriorityQueue(buildings.size(), Collections.reverseOrder());
        buildingList = new HashMap<Reparation.BuildingEnum, Building>();
        for (Reparation.BuildingEnum buildingEnum : buildings) {
            Building building = new Building(totalFloors);
            buildingList.put(buildingEnum, building);
        }
    }

    /**
     *
     * @param name
     * @param obj
     */
    public void Add(Reparation.BuildingEnum name, Building obj) {
        buildingList.put(name, obj);
    }

    /**
     *
     * @param obj
     */
    public void Remove(Building obj) {
        buildingList.remove(obj);
    }

    /**
     *
     * @param building
     * @param floor
     * @param reparation
     */
    public void addRepair(Reparation.BuildingEnum building, int floor, Reparation reparation) {
        Map<Integer, Floor> getBuilding = getRepairsFromBuilding(building);
        Floor floorR = getBuilding.get(Integer.toString(floor));
        floorR.Add(reparation.Location, reparation);
    }

    /**
     *
     * @param building
     * @return
     */
    public Map<Integer, Floor> getRepairsFromBuilding(Reparation.BuildingEnum building) {
        Building buildingR = buildingList.get(building);
        return buildingR.floorList;
    }

    /**
     *
     * @param building
     * @param floor
     * @return
     */
    public Map<Integer, Reparation> getRepairsFromFloor(Reparation.BuildingEnum building, int floor) {
        Map<Integer, Floor> getBuilding = getRepairsFromBuilding(building);
        Floor floorR = getBuilding.get(floor);
        return floorR.repairList;
    }


    /*duplicate from getRepairs
    public Reparation getReparation(Reparation.BuildingEnum building, int floor, int location) {
        Map<Integer, Floor> getBuilding = getRepairsFromBuilding(building);
        Floor floorR = getBuilding.get(Integer.toString(floor));
        return floorR.repairList.get(location);
    }*/

    /**
     *
     * @param building
     * @param floor
     * @param location
     * @return
     */
    public Reparation getReparation(Reparation.BuildingEnum building, int floor, int location) {
        Map<Integer, Reparation> getFloor = getRepairsFromFloor(building, floor);
        Reparation reparationR = getFloor.get(location);
        return reparationR;
    }

    /**
     *
     * @param building
     * @param priority
     */
    public void setPriorityBuilding(Reparation.BuildingEnum building, Reparation.PriorityType priority) {
        Building buildingR = buildingList.get(building);
        buildingR.priority = priority;
    }

    /**
     *
     * @param building
     * @return
     */
    public Reparation.PriorityType getPriorityBuilding(Reparation.BuildingEnum building) {
        Building buildingR = buildingList.get(building);
        return buildingR.priority;
    }

    /**
     *
     * @param building
     * @param floor
     * @param priority
     */
    public void setPriorityFloor(Reparation.BuildingEnum building, int floor, Reparation.PriorityType priority) {
         Map<Integer, Floor> getBuilding = getRepairsFromBuilding(building);
         Floor floorR = getBuilding.get(floor);
         floorR.priority = priority;
    }

    /**
     *
     * @param building
     * @param floor
     * @return
     */
    public Reparation.PriorityType getPriorityFloor(Reparation.BuildingEnum building, int floor) {
         Map<Integer, Floor> getBuilding = getRepairsFromBuilding(building);
         Floor floorR = getBuilding.get(floor);
         return floorR.priority;
    }

    /**
     *
     * @param building
     * @param floor
     */
    public void calculatePriorityFloor(Reparation.BuildingEnum building, int floor){
        Map<Integer, Reparation> prioFloor = getRepairsFromFloor(building, floor);
        int prioSom = 0;
        int count = 0;

        for(Reparation rep : prioFloor.values()) {
            prioSom += rep.Priority.getValue();
            count++;
        }

        int prio = prioSom/count;

        switch(prio) {
            case 1:
                setPriorityFloor(building, floor, Reparation.PriorityType.VERYLOW);
                break;
            case 2:
                setPriorityFloor(building, floor, Reparation.PriorityType.LOW);
                break;
            case 3:
                setPriorityFloor(building, floor, Reparation.PriorityType.AVERAGE);
                break;
            case 4:
                setPriorityFloor(building, floor, Reparation.PriorityType.HIGH);
                break;
            case 5:
                setPriorityFloor(building, floor, Reparation.PriorityType.IMPORTANT);
                break;
            case 6:
                setPriorityFloor(building, floor, Reparation.PriorityType.URGENT);
                break;
            default:
                setPriorityFloor(building, floor, null);
                break;
        }
    }

    /**
     *
     * @param building
     */
    public void calculatePriorityBuilding(Reparation.BuildingEnum building) {
        Map<Integer, Floor> prioFloor = getRepairsFromBuilding(building);
        int prioSom = 0;
        int count = 0;

        for(Floor rep : prioFloor.values()) {
            prioSom += rep.priority.getValue();
            count++;
        }

        int prio;
        if(prioSom != 0 && count != 0) {
            prio = prioSom / count;
        }
        else {
            prio = 0;
        }

        switch(prio) {
            case 1:
                setPriorityBuilding(building, Reparation.PriorityType.VERYLOW);
                break;
            case 2:
                setPriorityBuilding(building, Reparation.PriorityType.LOW);
                break;
            case 3:
                setPriorityBuilding(building, Reparation.PriorityType.AVERAGE);
                break;
            case 4:
                setPriorityBuilding(building, Reparation.PriorityType.HIGH);
                break;
            case 5:
                setPriorityBuilding(building, Reparation.PriorityType.IMPORTANT);
                break;
            case 6:
                setPriorityBuilding(building, Reparation.PriorityType.URGENT);
                break;
            default:
                setPriorityBuilding(building, null);
                break;
        }
    }
}





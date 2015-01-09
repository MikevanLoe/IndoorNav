/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package project.movinindoor.Reparation;

import java.util.ArrayList;
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
    public Queue<Building> order;
    public ArrayList<Reparation> acceptedWork = new ArrayList<Reparation>();
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
     * Adds a building to the list.
     *
     * @param name the name of the building.
     * @param obj the building.
     */
    public void Add(Reparation.BuildingEnum name, Building obj) {
        buildingList.put(name, obj);
    }

    /**
     * Remove a building.
     *
     * @param obj the building you want to remove.
     */
    public void Remove(Building obj) {
        buildingList.remove(obj);
    }

    /**
     * Adds a repair to the corresponding building and floor.
     *
     * @param reparation The reparation you want to add.
     */
    public void addRepair(Reparation reparation) {
        Map<Integer, Floor> getBuilding = getRepairsFromBuilding(reparation.Building);
        Floor floorR = getBuilding.get(Integer.valueOf(reparation.Floor));
        floorR.Add(reparation.Location, reparation);
    }

    /**
     * Returns all the repairs in a building.
     *
     * @param building the building you want to know the repairs off.
     * @return a hashmap with all data.
     */
    public Map<Integer, Floor> getRepairsFromBuilding(Reparation.BuildingEnum building) {
        Building buildingR = buildingList.get(building);
        return buildingR.floorList;
    }

    /**
     * Returns all reparations on a floor.
     *
     * @param building The building the floor is in.
     * @param floor The floor you want to know about.
     * @return a hashmap with the repairs of the specified floor.
     */
    public Map<String, Reparation> getRepairsFromFloor(Reparation.BuildingEnum building, int floor) {
        Map<Integer, Floor> getBuilding = getRepairsFromBuilding(building);
        Floor floorR = getBuilding.get(floor);
        return floorR.repairList;
    }


    /* duplicate from getRepairs
    public Reparation getReparation(Reparation.BuildingEnum building, int floor, int location) {
        Map<Integer, Floor> getBuilding = getRepairsFromBuilding(building);
        Floor floorR = getBuilding.get(Integer.toString(floor));
        return floorR.repairList.get(location);
    }*/

    /**
     * Get a reparation from the specified location in the specified building.
     *
     * @param building The building the floor is in.
     * @param floor The floor you want the reparation from.
     * @param location The location of the reparation.
     * @return the reparation at the location you specified.
     */
    public Reparation getReparation(Reparation.BuildingEnum building, int floor, int location) {
        Map<String, Reparation> getFloor = getRepairsFromFloor(building, floor);
        Reparation reparationR = getFloor.get(location);
        return reparationR;
    }

    /**
     * Set the priority of a building.
     *
     * @param building The building you want to set the priority of.
     * @param priority The priority you want to give the building.s
     */
    public void setPriorityBuilding(Reparation.BuildingEnum building, Reparation.PriorityType priority) {
        Building buildingR = buildingList.get(building);
        buildingR.priority = priority;
    }

    /**
     * Return the priority of the specified building.
     *
     * @param building the building you want to know the priority of.
     * @return The priority of the building.
     */
    public Reparation.PriorityType getPriorityBuilding(Reparation.BuildingEnum building) {
        Building buildingR = buildingList.get(building);
        return buildingR.priority;
    }

    /**
     * Set the priority of a building.
     *
     * @param building The building where the floor is.
     * @param floor The floor you want to give a priority.
     * @param priority The priority you want to give the floor.
     */
    public void setPriorityFloor(Reparation.BuildingEnum building, int floor, Reparation.PriorityType priority) {
         Map<Integer, Floor> getBuilding = getRepairsFromBuilding(building);
         Floor floorR = getBuilding.get(floor);
         floorR.priority = priority;
    }

    /**
     * Return the priority of the specified floor.
     *
     * @param building The building where the floor is.
     * @param floor The floor you want to know the priority of.
     * @return The priority of the building.
     */
    public Reparation.PriorityType getPriorityFloor(Reparation.BuildingEnum building, int floor) {
         Map<Integer, Floor> getBuilding = getRepairsFromBuilding(building);
         Floor floorR = getBuilding.get(floor);
         return floorR.priority;
    }

    /**
     * Calculate and set the priority of a building.
     *
     * @param building The building where the floor is.
     * @param floor The floor you want to calculate the priority of.
     */
    public void calculatePriorityFloor(Reparation.BuildingEnum building, int floor){
        Map<String, Reparation> prioFloor = getRepairsFromFloor(building, floor);
        int prioSom = 0;
        int count = 0;

        for(Reparation rep : prioFloor.values()) {
            prioSom += rep.Priority.getValue();
            count++;
        }

        int prio;
        if(prioSom != 0 && count != 0) {
            prio = prioSom / count;
            setPriorityFloor(building, floor, Reparation.PriorityType.values()[prio - 1]);
        }
        else {
            prio = 0;
            setPriorityFloor(building, floor, Reparation.PriorityType.values()[prio]);
        }
    }

    /**
     * Calculate and set the priority of a building.
     *
     * @param building the building you want to calculate the priority of.
     */
    public void calculatePriorityBuilding(Reparation.BuildingEnum building) {
        Map<Integer, Floor> prioFloor = getRepairsFromBuilding(building);
        int prioSom = 0;
        int count = 0;

        for(int i = 0 ; i < prioFloor.size(); i++){
            this.calculatePriorityFloor(building, i);
        }

        for(Floor rep : prioFloor.values()) {
            prioSom += rep.priority.getValue();
            count++;
        }

        int prio;
        if(prioSom != 0 && count != 0) {
            prio = prioSom / count;
            setPriorityBuilding(building, Reparation.PriorityType.values()[prio - 1]);
        }
        else {
            prio = 0;
            setPriorityBuilding(building, Reparation.PriorityType.values()[prio]);
        }
    }

    public ArrayList getList(){
        ArrayList<Reparation> al = new ArrayList<Reparation>();
        for (Reparation r : acceptedWork){
            al.add(r);
        }
        for(Building b : order){
            for(Floor f : b.order){
                for(Reparation r : f.highOrder){
                    al.add(r);
                }
            }
        }
        for(Building b : order){
            for(Floor f : b.order){
                for(Reparation r : f.lowOrder){
                    al.add(r);
                }
            }
        }
        return al;
    }
}





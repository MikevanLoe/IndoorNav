/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package project.movinindoor.Algorithm;

import java.util.HashMap;
import java.util.Map;

import project.movinindoor.Reparation.Building;
import project.movinindoor.Reparation.Campus;
import project.movinindoor.Reparation.Floor;
import project.movinindoor.Reparation.Reparation;

/**
 *
 * @author Davey
 */

public class Algorithm {
    Campus buildings;
    
    public Algorithm(Campus campusWin) {
        buildings = campusWin;
    }
    
    public Map<String, Floor> getBuildingRepairs(Reparation.BuildingEnum building) {
        Building buildingR = buildings.buildingList.get(building);
        return buildingR.floorList;
    }
    
    public Map<Integer, Reparation> getFloorRepairs(Reparation.BuildingEnum building, String floor) {
        Map<String, Floor> getBuilding = getBuildingRepairs(building);
        Floor floorR = getBuilding.get(floor);
        return floorR.repairList;
    }
    
    public Reparation getLocationRepairs(Reparation.BuildingEnum building, String floor, int location) {
        Map<Integer, Reparation> getFloor = getFloorRepairs(building, floor);
        Reparation reparationR = getFloor.get(location);
        return reparationR;
    }
   /* 
    public Algorithm(ReparationList repairList) {
        System.out.println("Start Algorithm");
        Map<String, Building> lists = SortByBuilding(repairList);
        
        
        for (Building building : lists.values()) {
            for (Reparation repair : building.floorList.values()) {
                System.out.println(repair.Building + "" + repair.Floor +  "." + repair.Location);
            }
            
        }
        /*
        for (int i = 1; i <= lists.size(); i++) {
            ReparationList buildings = sortByFloor(lists.get(i));
            for (Reparation repair : buildings.reparationList.values()) {
               System.out.println(repair.Building + "" + repair.Floor +  "." + repair.Location);
            }
        }
        
    }
    
/*
    public Map<String, Building>  SortByBuilding(ReparationList repairList) {
        
        Map<String, Building> buildings = new HashMap<String, Building>();
        Building buildingT = new Building();     
        Building buildingD = new Building();

        
        for (Reparation repair : repairList.reparationList.values()) {
            
            if(repair.Building.equals(Reparation.BuildingEnum.T)) {
                buildingT.Add(repair.Id, repair);
            } else if(repair.Building.equals(Reparation.BuildingEnum.D)) {
                buildingD.Add(repair.Id, repair);
            }
        }
        
        
        buildings.put("T", buildingT);
        buildings.put("D", buildingD);
        System.out.println("Sorted By Building");
        return buildings;
    }
    
    /*
    public Map<String, Building>  SortByFloor(ReparationList repairList) {
        
        Map<String, Building> buildings = new HashMap<String, Building>();
        Floor buildingT = new Building();     
        Floor buildingD = new Building();

        
        for (Reparation repair : repairList.reparationList.values()) {
            
            if(repair.Building.equals(Reparation.BuildingEnum.T)) {
                buildingT.Add(repair.Id, repair);
            } else if(repair.Building.equals(Reparation.BuildingEnum.D)) {
                buildingD.Add(repair.Id, repair);
            }
        }
        
        
        buildings.put("T", buildingT);
        buildings.put("D", buildingD);
        System.out.println("Sorted By Building");
        return buildings;
    }
    */
    /*
     
    public ReparationList sortByFloor(ReparationList repairList) {
        ReparationList repairListNew = new ReparationList();
        List<Reparation> peopleByAge = new ArrayList<>();
        for (Reparation repair : repairList.reparationList.values()) {
            peopleByAge.add(repair);
        }
        Collections.sort(peopleByAge, new Comparator<Reparation>() {
            @Override
            public int compare(Reparation t, Reparation t1) {
                //Integer.valueOf((int) t.Location).compareTo(Integer.valueOf((int) t1.Location));
                return Integer.compare(t.Floor, t1.Floor);
            }
       } );
       
        return repairListNew;
    }
    */
    
}

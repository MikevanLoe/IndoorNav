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
public class Campus {
    
    public Map<Reparation.BuildingEnum, Building> buildingList;
    
    public Campus() {
        buildingList = new HashMap<Reparation.BuildingEnum, Building>();
    }
    
    public void Add(Reparation.BuildingEnum name, Building obj) {
        buildingList.put(name, obj);
    }
    
    public void Remove(Building obj) {
        buildingList.remove(obj);
    }
}





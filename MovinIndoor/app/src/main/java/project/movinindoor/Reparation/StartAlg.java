/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package project.movinindoor.Reparation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import project.movinindoor.Algorithm.Algorithm;

/**
 *
 * @author Davey
 */
public class StartAlg {

    
    public void StartAlg() {
        // List with all Buildings
        List<Reparation.BuildingEnum> buildingsArray = new ArrayList<Reparation.BuildingEnum>();
        for (Reparation.BuildingEnum dir : Reparation.BuildingEnum.values()) {
            buildingsArray.add(dir);
        }
        
        //Create buildings
        Buildings buildings = new Buildings(buildingsArray);
    
<<<<<<< HEAD
        /* Test Repairs */
        Reparation rep1 = new Reparation(Reparation.BuildingEnum.T, 3, 54, Reparation.PriorityType.URGENT, "bla");
        Reparation rep2 = new Reparation(Reparation.BuildingEnum.T, 5, 45, Reparation.PriorityType.AVERAGE, "bla");
        Reparation rep3 = new Reparation(Reparation.BuildingEnum.T, 2, 45, Reparation.PriorityType.LOW, "bla");
        Reparation rep4 = new Reparation(Reparation.BuildingEnum.T, 2, 13, Reparation.PriorityType.AVERAGE, "bla");
        Reparation rep5 = new Reparation(Reparation.BuildingEnum.D, 1, 16, Reparation.PriorityType.LOW, "bla");
        Reparation rep6 = new Reparation(Reparation.BuildingEnum.D, 1, 19, Reparation.PriorityType.VERYLOW, "bla");
        Reparation rep7 = new Reparation(Reparation.BuildingEnum.D, 2, 40, Reparation.PriorityType.AVERAGE, "bla");
        Reparation rep8 = new Reparation(Reparation.BuildingEnum.T, 2, 28, Reparation.PriorityType.LOW, "bla");
        Reparation rep9 = new Reparation(Reparation.BuildingEnum.D, 2, 45, Reparation.PriorityType.VERYLOW, "bla");
        Reparation rep10 = new Reparation(Reparation.BuildingEnum.D, 0, 45, Reparation.PriorityType.HIGH, "bla");
        Reparation rep11 = new Reparation(Reparation.BuildingEnum.T, 0, 32, Reparation.PriorityType.IMPORTENT, "bla");
=======
        //Test Repairs
        Reparation rep1 = new Reparation(Reparation.BuildingEnum.T, 3, 54, Priority.URGENT, "bla");
        Reparation rep2 = new Reparation(Reparation.BuildingEnum.T, 5, 45, Priority.AVERAGE, "bla");
        Reparation rep3 = new Reparation(Reparation.BuildingEnum.T, 2, 45, Priority.LOW, "bla");
        Reparation rep4 = new Reparation(Reparation.BuildingEnum.T, 2, 13, Priority.AVERAGE, "bla");
        Reparation rep5 = new Reparation(Reparation.BuildingEnum.D, 1, 16, Priority.LOW, "bla");
        Reparation rep6 = new Reparation(Reparation.BuildingEnum.D, 1, 19, Priority.VERYLOW, "bla");
        Reparation rep7 = new Reparation(Reparation.BuildingEnum.D, 2, 40, Priority.AVERAGE, "bla");
        Reparation rep8 = new Reparation(Reparation.BuildingEnum.T, 2, 28, Priority.LOW, "bla");
        Reparation rep9 = new Reparation(Reparation.BuildingEnum.D, 2, 45, Priority.VERYLOW, "bla");
        Reparation rep10 = new Reparation(Reparation.BuildingEnum.D, 0, 45, Priority.HIGH, "bla");
        Reparation rep11 = new Reparation(Reparation.BuildingEnum.T, 0, 32, Priority.IMPORTENT, "bla");
>>>>>>> a0a4c60641c3717e61d14641c85e327d4f76f426
        
         // Set Repairs to Building and Floor
         buildings.setRepairs(rep1.Building, rep1.Floor, rep1);
         buildings.setRepairs(rep2.Building, rep2.Floor, rep2);
         buildings.setRepairs(rep3.Building, rep3.Floor, rep3);
         buildings.setRepairs(rep4.Building, rep4.Floor, rep4);
         buildings.setRepairs(rep5.Building, rep5.Floor, rep5);
         buildings.setRepairs(rep6.Building, rep6.Floor, rep6);
         buildings.setRepairs(rep7.Building, rep7.Floor, rep7);
         buildings.setRepairs(rep8.Building, rep8.Floor, rep8);
         buildings.setRepairs(rep9.Building, rep9.Floor, rep9);
         buildings.setRepairs(rep10.Building, rep10.Floor, rep10);
         buildings.setRepairs(rep11.Building, rep11.Floor, rep11);
         
         
<<<<<<< HEAD
         buildings.setPriorityBuilding(Reparation.BuildingEnum.T, Reparation.PriorityType.VERYLOW);
         //System.out.println(buildings.getPriorityBuilding(Reparation.BuildingEnum.T));
         buildings.setPriorityBuilding(Reparation.BuildingEnum.T, Reparation.PriorityType.IMPORTENT);
=======
         buildings.setPriorityBuilding(Reparation.BuildingEnum.T, Priority.VERYLOW);
         //System.out.println(buildings.getPriorityBuilding(Reparation.BuildingEnum.T));
         buildings.setPriorityBuilding(Reparation.BuildingEnum.T, Priority.IMPORTENT);
>>>>>>> a0a4c60641c3717e61d14641c85e327d4f76f426
         //System.out.println(buildings.getPriorityBuilding(Reparation.BuildingEnum.T));
         
         //System.out.println(buildings.getRepairsFromLocation(Reparation.BuildingEnum.D, "0", 45));
         
         //start Algorithm
         Algorithm algo = new Algorithm(buildings);

<<<<<<< HEAD


         for (Entry<Reparation.BuildingEnum, Building> b : buildings.buildingList.entrySet()) {
        for (Entry<Integer, Floor> f : b.getValue().floorList.entrySet()) {
=======
    for (Entry<Reparation.BuildingEnum, Building> b : buildings.buildingList.entrySet()) {
        for (Entry<String, Floor> f : b.getValue().floorList.entrySet()) {
>>>>>>> a0a4c60641c3717e61d14641c85e327d4f76f426
            for (Entry<Integer, Reparation> l : f.getValue().repairList.entrySet()) {
                //System.out.println(b.getKey() + "" + f.getKey() + "." + l.getKey());
            }
        }
            
    }
        
      
   
        
    }
    
}

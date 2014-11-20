/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package project.movinindoor.Reparation;

import project.movinindoor.Algorithm.Algorithm;

/**
 *
 * @author Davey
 */
public class StartAlg {

    public StartAlg() {
        Campus campusWin = new Campus();
        Building buildingT = new Building();
        Building buildingD = new Building();
    
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
        
        buildingT.floorList.get("3").Add(rep1.Location, rep1);
        buildingT.floorList.get("5").Add(rep2.Location, rep2);
        buildingT.floorList.get("2").Add(rep3.Location, rep3);
        buildingT.floorList.get("2").Add(rep4.Location, rep4);
        buildingD.floorList.get("1").Add(rep5.Location, rep5);
        buildingD.floorList.get("1").Add(rep6.Location, rep6);
        buildingD.floorList.get("2").Add(rep7.Location, rep7);
        buildingT.floorList.get("2").Add(rep8.Location, rep8);
        buildingD.floorList.get("2").Add(rep9.Location, rep9);
        buildingD.floorList.get("0").Add(rep10.Location, rep10);
        buildingT.floorList.get("0").Add(rep11.Location, rep11);

        campusWin.Add(Reparation.BuildingEnum.T, buildingT);
        campusWin.Add(Reparation.BuildingEnum.D, buildingD);
        
         Algorithm algo = new Algorithm(campusWin);
         
        // System.out.println(algo.getLocationRepairs(Reparation.BuildingEnum.D, "0", 45));
         
    /*
    for (Entry<Reparation.BuildingEnum, Building> b : campusWin.buildingList.entrySet()) {
        for (Entry<String, Floor> f : b.getValue().floorList.entrySet()) {
            for (Entry<Integer, Reparation> l : f.getValue().repairList.entrySet()) {
                System.out.println(b.getKey() + "" + f.getKey() + "." + l.getKey());
            }
        }
            
    }
    */
        
      
   
        
    }
    
}

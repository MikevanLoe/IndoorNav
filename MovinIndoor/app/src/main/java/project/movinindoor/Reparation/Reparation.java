/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package project.movinindoor.Reparation;

/**
 *
 * @author Davey
 */
public class Reparation {
<<<<<<< HEAD

    public enum PriorityType {
        VERYLOW(1), LOW(2), AVERAGE(3), HIGH(4), IMPORTENT(5), URGENT(6);
        private final int value;

        private PriorityType(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

=======
    


    
    
>>>>>>> a0a4c60641c3717e61d14641c85e327d4f76f426
    public enum BuildingEnum {
    A, B, C, D, E, F, G, H, S, T, X, Z, P1, P2, P3
    }
    
<<<<<<< HEAD
=======

    
>>>>>>> a0a4c60641c3717e61d14641c85e327d4f76f426
    private static int CountID = 1;
    public int Id;
    public BuildingEnum Building;
    public int Floor;
    public int Location;
<<<<<<< HEAD
    public PriorityType Priority;
    public String Description;
    
    public Reparation(BuildingEnum building, int floor, int location, PriorityType priority, String desc) {
=======
    public Priority Priority;
    public String Description;
    
    public Reparation(BuildingEnum building, int floor, int location, Priority priority, String desc) {
>>>>>>> a0a4c60641c3717e61d14641c85e327d4f76f426
        this.Id = CountID++;
        this.Building = building;
        this.Floor = floor;
        this.Location = location;
        this.Priority = priority;
        this.Description = desc;
    }
    
    public int getFloor() {
        return this.Floor;
    }
}

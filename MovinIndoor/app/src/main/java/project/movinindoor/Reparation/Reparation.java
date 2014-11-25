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
public class  Reparation {

    public enum PriorityType {
        VERYLOW(1), LOW(2), AVERAGE(3), HIGH(4), IMPORTANT(5), URGENT(6);
        public final int value;

        private PriorityType(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    public enum BuildingEnum {
    A, B, C, D, E, F, G, H, S, T, X, Z, P1, P2, P3
    }

    private static int CountID = 1;
    public int Id;
    public BuildingEnum Building;
    public int Floor;
    public int Location;
    public PriorityType Priority;
    public String Description;

    public Reparation(BuildingEnum building, int floor, int location, PriorityType priority, String desc) {
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

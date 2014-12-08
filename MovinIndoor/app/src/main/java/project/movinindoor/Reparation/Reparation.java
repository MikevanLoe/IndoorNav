/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package project.movinindoor.Reparation;

import com.google.android.gms.maps.model.LatLng;

/**
 *
 * @author Davey
 */
public class  Reparation implements Comparable<Reparation>{

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

    public enum StatusEnum {
        NEW, ASSIGNED, ACCEPTED, REPAIRED, DONE
    }

    //private static int CountID = 1;
    public int Id;
    public String ShortDescription;
    public BuildingEnum Building;
    public LatLng LatLng;
    public int Floor;
    public String Location;
    public StatusEnum Status;
    public PriorityType Priority;
    public String Description;
    public String Comment;

    public Reparation(int id, BuildingEnum building, int floor, String location, LatLng latLng, StatusEnum status, PriorityType priority, String shortDescription, String description, String comment) {
        Id = id;
        ShortDescription = shortDescription;
        Building = building;
        LatLng = latLng;
        Floor = floor;
        Location = location;
        Status = status;
        Priority = priority;
        Description = description;
        Comment = comment;
    }

    /**
     * Gets the floor the reparation is on.
     *
     * @return the floor the reparation is on.
     */
    public int getFloor() {
        return this.Floor;
    }

    /**
     * Compare two reparations with eachother.
     *
     * @param b the reparations you compare to.
     * @return an int to define the largest or smallest.
     */
    @Override
    public int compareTo(Reparation b) {

        if(this.Priority.getValue() < b.Priority.getValue()){
            return -1;
        }else if(this.Priority.getValue() > b.Priority.getValue()){
            return 1;
        }else{
            return 0;
        }
    }
}

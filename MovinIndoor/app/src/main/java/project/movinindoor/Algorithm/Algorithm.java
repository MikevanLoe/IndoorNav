/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package project.movinindoor.Algorithm;

import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import project.movinindoor.Animator;
import project.movinindoor.CalcMath;
import project.movinindoor.Graph.GraphHandler;
import project.movinindoor.Graph.Node;
import project.movinindoor.MapDrawer;
import project.movinindoor.MapsActivity;
import project.movinindoor.Models.Room;
import project.movinindoor.Reparation.Buildings;

/**
 *
 * @author Davey
 */

public class Algorithm {
    public Buildings buildings;


    public Algorithm(Buildings campusWin) {
        buildings = campusWin;
    }

    private static GraphHandler graphHandler = MapsActivity.getSetupGraph();

    /**
     * function that manages all classes that are needed for running the navigation
     * @param start the starting point of the navigation
     * @param destination the destination of the navigation
     * @return returns true if the function worked without problems
     */
    public static boolean navigate(String start, String destination) {
        //Removes From -> To Fragement;
        MapsActivity.getfNavigationInfoBottom().setVisibility(View.INVISIBLE);
        //Removes existing Polylines
        MapDrawer.removePolylines();
        MapDrawer.hideMarkers();

        boolean sucess = navigateRoute(start, destination);

        if(sucess) {
            //animate
            if(Animator.isRepairListVisible()) {
                Animator.visibilityCardNavigator(Animator.Visibility.SHOW);
                Animator.visibilityRepairList(Animator.Visibility.HIDE);
                Animator.visibilityNavigationInfoTop(Animator.Visibility.SHOW);
                Animator.visibilityNavigationInfoBottom(Animator.Visibility.SHOW);
                Animator.visibilityFloorNavagator(Animator.Visibility.SHOW);
            } else {
                Animator.visibilityCardNavigator(Animator.Visibility.SHOW);
                Animator.visibilityNavigationInfoTop(Animator.Visibility.SHOW);
                Animator.visibilityNavigationInfoBottom(Animator.Visibility.SHOW);
                Animator.visibilityFloorNavagator(Animator.Visibility.SHOW);
            }
        } else{
            Animator.visibilityCardNavigator(Animator.Visibility.HIDE);
            Animator.visibilityRepairList(Animator.Visibility.SHOW);
            Animator.visibilityNavigationInfoTop(Animator.Visibility.HIDE);
            Animator.visibilityNavigationInfoBottom(Animator.Visibility.HIDE);
            Animator.visibilityFloorNavagator(Animator.Visibility.HIDE);
        }
        return sucess;
    }

    /**
     * supporting funciton of navigate
     * @param start
     * @param destination
     * @return returns true if function ran without problems
     */
    private static boolean navigateRoute(String start, String destination) {
        double extraCost = 0.0;
        LatLng startPositionLatLng, endPositionLatLng;
        Node endNode, startNode;
        Room startRoom = null, endRoom = null;

        //if not a custom location
        if(MapsActivity.getCustomStartPos() == null && !MapsActivity.getEditStart().getText().toString().contains("Custom") && !start.contains("Custom")) {
            startRoom = graphHandler.getRooms().getRoom(start);
            if (startRoom == null) startRoom = graphHandler.getRooms().getRoom(start.toUpperCase());

            if (startRoom == null) {
                Toast.makeText(MapsActivity.getContext().getApplicationContext(), "From " + start + " not found", Toast.LENGTH_SHORT).show();
                return false;
            }

            String s = startRoom.getLocation();
            String[] l = s.split("\\.");
            int floor = Integer.valueOf(l[0].substring(1));
            startNode = graphHandler.getNodes().FindClosestNodeInsideRoom(startRoom);
            if(startNode == null) {
                startNode = graphHandler.getNodes().findNearestNode(startRoom.getLatLngBoundsCenter(), floor);
                if(startNode!=null) extraCost = CalcMath.measureMeters(startRoom.getLatLngBoundsCenter().latitude, startRoom.getLatLngBoundsCenter().longitude, startNode.getLatLng().latitude, startNode.getLatLng().longitude);
                else return false;
            }

            startPositionLatLng = new LatLng(startNode.getLatLng().latitude, startNode.getLatLng().longitude);
        } else {
            if (!start.contains("Custom Start")) {
                startRoom = graphHandler.getRooms().getRoom(start);
                startNode = graphHandler.getNodes().findNearestNode(startRoom.getLatLngBoundsCenter(), startRoom.getFloor());
                extraCost = CalcMath.measureMeters(startRoom.getLatLngBoundsCenter().latitude, startRoom.getLatLngBoundsCenter().longitude, startNode.getLatLng().latitude, startNode.getLatLng().longitude);
                startPositionLatLng = startRoom.getLatLngBoundsCenter();
            } else {
                startNode = graphHandler.getNodes().findNearestNode(MapsActivity.getCustomStartPos(), MapsActivity.getCustomStartFloor());
                extraCost = CalcMath.measureMeters(MapsActivity.getCustomStartPos().latitude, MapsActivity.getCustomStartPos().longitude, startNode.getLatLng().latitude, startNode.getLatLng().longitude);
                startPositionLatLng = MapsActivity.getCustomStartPos();
            }

        }

        //if not a custom location
        if(MapsActivity.getCustomEndPos() == null  && !MapsActivity.getEditEnd().getText().toString().contains("Custom") && !destination.contains("Custom")) {
            String re1="([a-z])";	// Any Single Word Character (Not Whitespace) 1
            String re2="(\\d+)";	// Integer Number 1
            String re3="(.)";	// Any Single Character 1
            String re4="(\\d)";	// Any Single Digit 1
            String re5="(\\d)";	// Any Single Digit 2

            Pattern p = Pattern.compile(re1+re2+re3+re4+re5,Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
            Matcher m = p.matcher(destination);
            if (m.matches()) {
                endRoom = graphHandler.getRooms().getRoom(destination);
                if(endRoom == null) endRoom = graphHandler.getRooms().getRoom(destination.toUpperCase());
            } else {
                List<Room> roomsWithName = graphHandler.getRooms().getAllRoomsWithName(destination);
                if(roomsWithName.size() != 1) {
                    double tempCost = 0.0;
                    Room tempRoom = null;
                    for (Room room : roomsWithName) {
                        Node node = graphHandler.getNodes().FindClosestNodeInsideRoom(room);
                        //find shortest route to position
                        if(node != null) {
                            double cost = graphHandler.getGraph().drawPath(startNode.getNodeId(), node.getNodeId());
                            if(tempCost == 0.0 && tempRoom == null) {
                                tempCost = cost;
                                tempRoom = room;
                            }

                            if (cost <= tempCost) {
                                tempCost = cost;
                                tempRoom = room;
                            }
                        }

                    }
                    MapDrawer.removePolylines();
                    endRoom = tempRoom;
                } else {
                    endRoom = graphHandler.getRooms().getRoom(destination);
                }
            }

            if (endRoom == null) {
                Toast.makeText(MapsActivity.getContext().getApplicationContext(), "To " + destination + " not found", Toast.LENGTH_SHORT).show();
                return false;
            }

            String s = endRoom.getLocation();
            String[] l = s.split("\\.");
            int floor = Integer.valueOf(l[0].substring(1));
            endNode = graphHandler.getNodes().FindClosestNodeInsideRoom(endRoom);
            if (endNode == null) {
                endNode = graphHandler.getNodes().findNearestNode(endRoom.getLatLngBoundsCenter(), floor);
                if(startNode!=null) extraCost = CalcMath.measureMeters(endRoom.getLatLngBoundsCenter().latitude, endRoom.getLatLngBoundsCenter().longitude, endNode.getLatLng().latitude, endNode.getLatLng().longitude);
                else return false;
            }

            endPositionLatLng = new LatLng(endNode.getLatLng().latitude, endNode.getLatLng().longitude);
        } else {
            if (!destination.contains("Custom End")) {
                endRoom = graphHandler.getRooms().getRoom(destination);
                endNode = graphHandler.getNodes().findNearestNode(endRoom.getLatLngBoundsCenter(), endRoom.getFloor());
                extraCost = CalcMath.measureMeters(endRoom.getLatLngBoundsCenter().latitude, endRoom.getLatLngBoundsCenter().longitude, endNode.getLatLng().latitude, endNode.getLatLng().longitude);
                endPositionLatLng = endRoom.getLatLngBoundsCenter();
            } else {
                endNode = graphHandler.getNodes().findNearestNode(MapsActivity.getCustomEndPos(), MapsActivity.getCustomEndFloor());
                extraCost = CalcMath.measureMeters(MapsActivity.getCustomEndPos().latitude, MapsActivity.getCustomEndPos().longitude, endNode.getLatLng().latitude, endNode.getLatLng().longitude);
                endPositionLatLng = MapsActivity.getCustomEndPos();
            }

        }

        if(startNode == null || endNode == null) {
            Toast.makeText(MapsActivity.getContext().getApplicationContext(), "Navigation not found", Toast.LENGTH_SHORT).show();
            return false;
        }

        double cost = 0.0;
        if (startNode.getNodeId().equals(endNode.getNodeId())) Toast.makeText(MapsActivity.getContext().getApplicationContext(), "Start and end is same location", Toast.LENGTH_SHORT).show();
        else cost = graphHandler.getGraph().drawPath(startNode.getNodeId(), endNode.getNodeId());

        if(cost != 0.0) {
            cost += extraCost;
            String walkingSpeed = graphHandler.getGraph().getMovement().calculateMovingSpeed(cost);
            MapsActivity.getTextSpeed().setText("ETA: " + walkingSpeed);
            MapsActivity.getTextSpeedCost().setText("(" + String.valueOf(Math.round(cost)) + "m)");

            MapsActivity.getTextFrom().setText(start);
            MapsActivity.getTextTo().setText(destination);
            MapDrawer.setFloor(startNode.getFloor());
            MapsActivity.getBtnCurrentFloor().setText(String.valueOf(startNode.getFloor()));


            MapsActivity.getBtnFloorUp().setVisibility(View.VISIBLE);
            MapsActivity.getBtnFloorDown().setVisibility(View.VISIBLE);

            MapsActivity.getMap().moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(startNode.getLatLng().latitude, startNode.getLatLng().longitude), 20));

            MapDrawer.addMarker(startPositionLatLng, start, startNode.getFloor());
            MapDrawer.addMarker(endPositionLatLng, destination, endNode.getFloor());

            MapDrawer.hideAllMarkersAndPolylines();
            MapDrawer.showMarkersAndPolylinesFloor(MapDrawer.getFloor());
            return true;
        }
        return false;
    }

}

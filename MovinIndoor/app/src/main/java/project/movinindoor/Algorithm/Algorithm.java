/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package project.movinindoor.Algorithm;

import android.view.View;
import android.widget.ImageButton;
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
import project.movinindoor.Reparation.Buildings;
import project.movinindoor.Models.Room;

/**
 *
 * @author Davey
 */

public class Algorithm {
    public Buildings buildings;


    public Algorithm(Buildings campusWin) {
        buildings = campusWin;
    }

    private enum Travel {
        ELEVATOR, STAIR
    }

    /*
    public static void NavAlgo(Room startPos, Room endPos) {
        String startLocation= startPos.getLocation();
        String endLocation= endPos.getLocation();

        String[] splitStartLocation = splitLocation(startLocation);
        String[] splitEndLocation = splitLocation(endLocation);

        String startBuilding = splitStartLocation[0].substring(0, 1);
        String endBuilding = splitEndLocation[0].substring(0, 1);

        int startFloor = Integer.valueOf(splitStartLocation[0].substring(1));
        int endFloor = Integer.valueOf(splitEndLocation[0].substring(1));

        if(startBuilding.equals(endBuilding)) { //Same building
            if(splitStartLocation[0].equals(splitEndLocation[0])) {  //if Same Building And Floor
                navigate(startLocation, endLocation);
            } else { // if Same Building but Different Floor
                // find all Elevators / Stairs
                if(Graph.movingByFoot == true) { // If Traveling by Foot
                    if(Math.abs(startFloor - endFloor) >= 2) { //Floor Differenct is to big
                        // Find (primarly) Only Elevators
                        List<Node> nodeList = travelByElevatorOrStair(Travel.ELEVATOR, startPos, endPos);
                        drawRoute(nodeList);
                    } else {
                    // Find only Stairs
                        List<Node> nodeList = travelByElevatorOrStair(Travel.STAIR, startPos, endPos);
                        drawRoute(nodeList);
                    }
                } else { // If Traveling by Foot With Cart
                  //  Find Only Elevators
                    List<Node> nodeList = travelByElevatorOrStair(Travel.ELEVATOR, startPos, endPos);
                    drawRoute(nodeList);
                }
            }
        } else { // If Different Building
            if((buildingHaveBridge(startBuilding) && Math.abs(startFloor) >= 2 )|| (buildingHaveBridge(endBuilding) && Math.abs(startFloor)  >= 2 )) { //Calculate with Floor 2  And 0
                if(Graph.movingByFoot == true) { // If Traveling by Foot
                    if(Math.abs(startFloor - endFloor) >= 2) { //Floor Differenct is to big
                        // Find (primarly) Only Elevators
                        List<Node> nodeList = travelBetweenBuildingsByElevatorOrStairByBridge(Travel.ELEVATOR, startPos, endPos);
                        drawRoute(nodeList);
                    } else {
                        // Find only Stairs
                        List<Node> nodeList = travelBetweenBuildingsByElevatorOrStairByBridge(Travel.STAIR, startPos, endPos);
                        drawRoute(nodeList);
                    }
                } else { // If Traveling by Foot With Cart
                    //  Find Only Elevators
                    List<Node> nodeList = travelBetweenBuildingsByElevatorOrStairByBridge(Travel.ELEVATOR, startPos, endPos);
                    drawRoute(nodeList);
                }
            } else { // Calculate with Floor 0
                if(Graph.movingByFoot == true) { // If Traveling by Foot
                    if(Math.abs(startFloor - endFloor) >= 2) { //Floor Differenct is to big
                        // Find (primarly) Only Elevators
                        List<Node> nodeList = travelBetweenBuildingsByElevatorOrStair(Travel.ELEVATOR, startPos, endPos);
                        drawRoute(nodeList);
                    } else {
                        // Find only Stairs
                        List<Node> nodeList = travelBetweenBuildingsByElevatorOrStair(Travel.STAIR, startPos, endPos);
                        drawRoute(nodeList);
                    }
                } else { // If Traveling by Foot With Cart
                    //  Find Only Elevators
                    List<Node> nodeList = travelBetweenBuildingsByElevatorOrStair(Travel.ELEVATOR, startPos, endPos);
                    drawRoute(nodeList);
                }
            }
        }
    }
    */

    private static GraphHandler graphHandler = MapsActivity.getSetupGraph();

    //Called by methods
    public static void navigate(String start, String end) {
        //Removes From -> To Fragement;
        MapsActivity.getfNavigationInfoBottom().setVisibility(View.INVISIBLE);
        //Removes existing Polylines
        MapDrawer.removePolylines();
        MapDrawer.hideMarkers();

        boolean sucess = navigateRoute(start, end);

        if(sucess) {
            Toast.makeText(MapsActivity.getContext(), "Navigation started", Toast.LENGTH_SHORT).show();
            //animate
            Animator.visibilityCardNavigator(Animator.Visibility.SHOW);
            Animator.visibilityRepairList(Animator.Visibility.HIDE);
            Animator.visibilityNavigationInfoTop(Animator.Visibility.SHOW);
            Animator.visibilityNavigationInfoBottom(Animator.Visibility.SHOW);
            Animator.visibilityFloorNavagator(Animator.Visibility.SHOW);
        }
    }

    public static boolean navigateRoute(String startPosition, String endPosition) {
        double extraCost = 0.0;
        LatLng startPositionLatLng, endPositionLatLng;
        Node endNode, startNode;
        Room startRoom = null, endRoom = null;

        //if not a custom location
        if(MapsActivity.getCustomStartPos() == null && !MapsActivity.getEditStart().getText().toString().contains("Custom")) {
            startRoom = graphHandler.getRooms().getRoom(startPosition.toUpperCase());

            if (startRoom == null) {
                Toast.makeText(MapsActivity.getContext().getApplicationContext(), "From" + startPosition + " not found", Toast.LENGTH_SHORT).show();
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
            startNode = graphHandler.getNodes().findNearestNode(MapsActivity.getCustomStartPos(), MapsActivity.getCustomStartFloor());
            extraCost = CalcMath.measureMeters(MapsActivity.getCustomStartPos().latitude, MapsActivity.getCustomStartPos().longitude, startNode.getLatLng().latitude, startNode.getLatLng().longitude);
            startPositionLatLng = MapsActivity.getCustomStartPos();
        }

        //if not a custom location
        if(MapsActivity.getCustomEndPos() == null  && !MapsActivity.getEditEnd().getText().toString().contains("Custom")) {
            String re1="([a-z])";	// Any Single Word Character (Not Whitespace) 1
            String re2="(\\d+)";	// Integer Number 1
            String re3="(.)";	// Any Single Character 1
            String re4="(\\d)";	// Any Single Digit 1
            String re5="(\\d)";	// Any Single Digit 2

            Pattern p = Pattern.compile(re1+re2+re3+re4+re5,Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
            Matcher m = p.matcher(endPosition);
            if (m.matches()) {
                endRoom = graphHandler.getRooms().getRoom(endPosition.toUpperCase());
            } else {
                List<Room> roomsWithName = graphHandler.getRooms().getAllRoomsWithName(endPosition);
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
                    endRoom = graphHandler.getRooms().getRoom(endPosition);
                }
            }

            if (endRoom == null) {
                Toast.makeText(MapsActivity.getContext().getApplicationContext(), "To " + endPosition + " not found", Toast.LENGTH_SHORT).show();
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

            endNode = graphHandler.getNodes().findNearestNode(MapsActivity.getCustomEndPos(), MapsActivity.getCustomEndFloor());
            extraCost = CalcMath.measureMeters(MapsActivity.getCustomEndPos().latitude, MapsActivity.getCustomEndPos().longitude, endNode.getLatLng().latitude, endNode.getLatLng().longitude);
            endPositionLatLng = MapsActivity.getCustomEndPos();
        }

        if(startNode == null || endNode == null) {
            Toast.makeText(MapsActivity.getContext().getApplicationContext(), "Navigation not found", Toast.LENGTH_SHORT).show();
            return false;
        }

        double cost = graphHandler.getGraph().drawPath(startNode.getNodeId(), endNode.getNodeId());
        if(cost != 0.0) {
            cost += extraCost;
            String walkingSpeed = graphHandler.getGraph().calculateWalkingSpeed(cost);
            MapsActivity.getTextSpeed().setText("ETA: " + walkingSpeed);
            MapsActivity.getTextSpeedCost().setText("(" + String.valueOf(Math.round(cost)) + "m)");




            MapsActivity.getTextFrom().setText(startPosition);
            MapsActivity.getTextTo().setText(endPosition);
            MapDrawer.setFloor(startNode.getFloor());
            MapsActivity.getBtnCurrentFloor().setText(String.valueOf(startNode.getFloor()));
            MapDrawer.hideAllMarkersAndPolylines();
            MapDrawer.showMarkersAndPolylinesFloor(MapDrawer.getFloor());

            MapsActivity.getBtnFloorUp().setVisibility(View.VISIBLE);
            MapsActivity.getBtnFloorDown().setVisibility(View.VISIBLE);

            MapsActivity.getMap().moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(startNode.getLatLng().latitude, startNode.getLatLng().longitude), 20));

            MapDrawer.addMarker(startPositionLatLng, startPosition, startNode.getFloor());
            MapDrawer.addMarker(endPositionLatLng, endPosition, endNode.getFloor());
            return true;
        }
        return false;
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package project.movinindoor.Algorithm;

import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import project.movinindoor.Animator;
import project.movinindoor.CalcMath;
import project.movinindoor.Graph.Graph.Graph;
import project.movinindoor.Graph.GraphHandler;
import project.movinindoor.Graph.Node;
import project.movinindoor.Graph.ToNode;
import project.movinindoor.MapDrawer;
import project.movinindoor.MapsActivity;
import project.movinindoor.Models.Elevator;
import project.movinindoor.Models.Stair;
import project.movinindoor.Reparation.Buildings;
import project.movinindoor.Reparation.Reparation;
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
<<<<<<< HEAD
                navigate3(startLocation, endLocation);
=======
                navigate(startLocation, endLocation);
>>>>>>> 109ad34595e7840241a651db6f1fe188bd70b865
            } else { // if Same Building but Different Floor
                // find all Elevators / Stairs
                if(Graph.movingByWalk == true) { // If Traveling by Foot
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
                if(Graph.movingByWalk == true) { // If Traveling by Foot
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
                if(Graph.movingByWalk == true) { // If Traveling by Foot
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

    private static List<Node> travelBetweenBuildingsByElevatorOrStairByBridge(Travel travel, Room startPos, Room endPos) {
        String startLocation= startPos.getLocation();
        String endLocation= endPos.getLocation();

        String[] splitStartLocation = splitLocation(startLocation);
        String[] splitEndLocation = splitLocation(endLocation);

        int startFloor = Integer.valueOf(splitStartLocation[0].substring(1));
        int endFloor = Integer.valueOf(splitEndLocation[0].substring(1));

        List<Node> startElevatorTopN, startElevatorDownN, endElevatorTopN, endElevatorDownN;
        if(travel == Travel.STAIR) {
            startElevatorTopN = findAllStairsByNode();
            startElevatorDownN = findAllStairsByNodeFloor(startElevatorTopN, 0);

            endElevatorTopN = findAllStairsByNode();
            endElevatorDownN = findAllStairsByNodeFloor(endElevatorTopN, 0);
        } else {
            startElevatorTopN = findAllElevatorsByNode();
            startElevatorDownN = findAllElevatorsByNodeFloor(startElevatorTopN, 0);

            endElevatorTopN = findAllElevatorsByNode();
            endElevatorDownN = findAllElevatorsByNodeFloor(endElevatorTopN, 0);
        }

        Node shortestStartNode = null;
        Node shortestEndNode = null;
        Node shortestEndElevatorTop = null;
        Node shortestStartElevatorTop = null;
        Node shortestEndElevatorDown = null;
        Node shortestStartElevatorDown = null;
        double shortestCost = 0.0, costBuilding1= 0.0, costBuilding2 = 0.0, costBetweenBuildings = 0.0;
        for (int i = 0; i < endElevatorDownN.size(); i++) {
            Node startPositionNode = graphHandler.getNodes().FindClosestNodeInsideRoom(startPos);
            Node startElevatorTop = null;
            if(startFloor != 0) { // startPosition is not on floor 0
                startElevatorTop = startElevatorTopN.get(i);
                costBuilding1 = MapsActivity.setupGraph.graph.drawPath(startPositionNode.nodeId.toString(), startElevatorTop.nodeId.toString()); // Start -> TravelType1
            }

            Node startElevatorDown = startElevatorDownN.get(i);
            for(int j = 0; j < endElevatorDownN.size(); j++) {
                Node endElevatorDown = endElevatorDownN.get(j);
                Node endPositionNode = graphHandler.getNodes().FindClosestNodeInsideRoom(endPos);
                Node endElevatorTop = null;

                if(startFloor != 0 && endFloor != 0) costBetweenBuildings = MapsActivity.setupGraph.graph.drawPath(startPositionNode.nodeId.toString(), endPositionNode.nodeId.toString()); // Start -> End
                else if(startFloor != 0) costBetweenBuildings = MapsActivity.setupGraph.graph.drawPath(startPositionNode.nodeId.toString(), endElevatorDown.nodeId.toString()); // Start -> TraveType2
                else if(endFloor != 0) costBetweenBuildings = MapsActivity.setupGraph.graph.drawPath(startElevatorDown.nodeId.toString(), endPositionNode.nodeId.toString()); // TravelType1 -> End
                else costBetweenBuildings = MapsActivity.setupGraph.graph.drawPath(startElevatorDown.nodeId.toString(), endElevatorDown.nodeId.toString()); // TravelType1 -> TraveType2


                if(endFloor != 0) { // endPosition is not on floor 0
                    endElevatorTop = endElevatorTopN.get(j);
                    costBuilding2 = MapsActivity.setupGraph.graph.drawPath(endElevatorTop.nodeId.toString(), endPositionNode.nodeId.toString()); // TravelType2 -> End
                }

                double cost = costBuilding1 + costBetweenBuildings + costBuilding2;
                if (shortestCost == 0.0) shortestCost = cost;
                if (shortestCost >= cost) {
                    shortestStartNode = startPositionNode;
                    if(startFloor != 0) shortestStartElevatorTop = startElevatorTop;
                    if(startFloor != 0) shortestStartElevatorDown = startElevatorDown;
                    if(endFloor != 0) shortestEndElevatorDown = endElevatorDown;
                    if(endFloor != 0) shortestEndElevatorTop = endElevatorTop;
                    shortestEndNode = endPositionNode;
                    shortestCost = cost;
                }
            }
        }

        //4. return shortest route
        List<Node> returnNodeList = new ArrayList<>();
        returnNodeList.add(shortestStartNode);
        if(startFloor != 0) returnNodeList.add(shortestStartElevatorTop);
        if(startFloor != 0) returnNodeList.add(shortestStartElevatorDown);
        if(endFloor != 0) returnNodeList.add(shortestEndElevatorDown);
        if(endFloor != 0) returnNodeList.add(shortestEndElevatorTop);
        returnNodeList.add(shortestEndNode);
        return returnNodeList;
    }


    /*
       2. find all elevators
       3. Find Nodes from Elevators
       4. Find Elevator node on Floor 0
       5. calculate route foreach elevator to door
       6. calculate route foreach elevator to door

       6. find all elevators from
       7. Find Nodes from Elevators
       8. Find Elevator node on Floor 0
       10. calculate route foreach elevator to door
       11. calculate simple van from door to door
      11. return shortest route
    */
    private static List<Node> travelBetweenBuildingsByElevatorOrStair(Travel travel, Room startPos, Room endPos) {
        String startLocation= startPos.getLocation();
        String endLocation= endPos.getLocation();

        String[] splitStartLocation = splitLocation(startLocation);
        String[] splitEndLocation = splitLocation(endLocation);

        int startFloor = Integer.valueOf(splitStartLocation[0].substring(1));
        int endFloor = Integer.valueOf(splitEndLocation[0].substring(1));

        List<Node> startElevatorTopN, startElevatorDownN, endElevatorTopN, endElevatorDownN;
        if(travel == Travel.STAIR) {
            startElevatorTopN = findAllStairsByNode();
            startElevatorDownN = findAllStairsByNodeFloor(startElevatorTopN, 0);

            endElevatorTopN = findAllStairsByNode();
            endElevatorDownN = findAllStairsByNodeFloor(endElevatorTopN, 0);
        } else {
            startElevatorTopN = findAllElevatorsByNode();
            startElevatorDownN = findAllElevatorsByNodeFloor(startElevatorTopN, 0);

            endElevatorTopN = findAllElevatorsByNode();
            endElevatorDownN = findAllElevatorsByNodeFloor(endElevatorTopN, 0);
        }

        Node shortestStartNode = null;
        Node shortestEndNode = null;
        Node shortestEndElevatorTop = null;
        Node shortestStartElevatorTop = null;
        Node shortestEndElevatorDown = null;
        Node shortestStartElevatorDown = null;
        double shortestCost = 0.0, costBuilding1= 0.0, costBuilding2 = 0.0, costBetweenBuildings = 0.0;
        for (int i = 0; i < endElevatorDownN.size(); i++) {
            Node startPositionNode = graphHandler.getNodes().FindClosestNodeInsideRoom(startPos);
            Node startElevatorTop = null;
            if(startFloor != 0) { // startPosition is not on floor 0
                startElevatorTop = startElevatorTopN.get(i);
                costBuilding1 = MapsActivity.setupGraph.graph.drawPath(startPositionNode.nodeId.toString(), startElevatorTop.nodeId.toString()); // Start -> TravelType1
            }

            Node startElevatorDown = startElevatorDownN.get(i);
            for(int j = 0; j < endElevatorDownN.size(); j++) {
                Node endElevatorDown = endElevatorDownN.get(j);
                Node endPositionNode = graphHandler.getNodes().FindClosestNodeInsideRoom(endPos);
                Node endElevatorTop = null;

                if(startFloor != 0 && endFloor != 0) costBetweenBuildings = MapsActivity.setupGraph.graph.drawPath(startPositionNode.nodeId.toString(), endPositionNode.nodeId.toString()); // Start -> End
                else if(startFloor != 0) costBetweenBuildings = MapsActivity.setupGraph.graph.drawPath(startPositionNode.nodeId.toString(), endElevatorDown.nodeId.toString()); // Start -> TraveType2
                else if(endFloor != 0) costBetweenBuildings = MapsActivity.setupGraph.graph.drawPath(startElevatorDown.nodeId.toString(), endPositionNode.nodeId.toString()); // TravelType1 -> End
                else costBetweenBuildings = MapsActivity.setupGraph.graph.drawPath(startElevatorDown.nodeId.toString(), endElevatorDown.nodeId.toString()); // TravelType1 -> TraveType2


                if(endFloor != 0) { // endPosition is not on floor 0
                    endElevatorTop = endElevatorTopN.get(j);
                    costBuilding2 = MapsActivity.setupGraph.graph.drawPath(endElevatorTop.nodeId.toString(), endPositionNode.nodeId.toString()); // TravelType2 -> End
                }

                double cost = costBuilding1 + costBetweenBuildings + costBuilding2;
                if (shortestCost == 0.0) shortestCost = cost;
                if (shortestCost >= cost) {
                    shortestStartNode = startPositionNode;
                    if(startFloor != 0) shortestStartElevatorTop = startElevatorTop;
                    if(startFloor != 0) shortestStartElevatorDown = startElevatorDown;
                    if(endFloor != 0) shortestEndElevatorDown = endElevatorDown;
                    if(endFloor != 0) shortestEndElevatorTop = endElevatorTop;
                    shortestEndNode = endPositionNode;
                    shortestCost = cost;
                }
            }
        }

        //4. return shortest route
        List<Node> returnNodeList = new ArrayList<>();
        returnNodeList.add(shortestStartNode);
        if(startFloor != 0) returnNodeList.add(shortestStartElevatorTop);
        if(startFloor != 0) returnNodeList.add(shortestStartElevatorDown);
        if(endFloor != 0) returnNodeList.add(shortestEndElevatorDown);
        if(endFloor != 0) returnNodeList.add(shortestEndElevatorTop);
        returnNodeList.add(shortestEndNode);
        return returnNodeList;
    }


    private static List<Node> findAllElevatorsByNode() {
        List<Node> elevatorNode = new ArrayList<>();
        for(Node n : MapsActivity.setupGraph.getNodes().jsonList.values()) {
            if(n.type.equals("Elevator")) elevatorNode.add(n);
        }

        return elevatorNode;
    }

    private static List<Node> findAllElevatorsByNodeFloor(List<Node> elevatorNode, int floor) {
        List<Node> elevatorFloorNode = new ArrayList<>();
        for(Node node : elevatorNode) {
            for (ToNode toNode : node.toNode) {
                Node newNode = MapsActivity.setupGraph.getNodes().jsonList.get(toNode.toNodeID);
                if(newNode.floor.equals(String.valueOf(floor))) elevatorFloorNode.add(newNode);
            }
        }

        return elevatorFloorNode;
    }

    private static List<Node> findAllStairsByNode() {
        List<Node> elevatorNode = new ArrayList<>();
        for(Node n : MapsActivity.setupGraph.getNodes().jsonList.values()) {
            if(n.type.equals("Stairs")) elevatorNode.add(n);
        }
        return elevatorNode;
    }

    private static List<Node> findAllStairsByNodeFloor(List<Node> elevatorNode, int floor) {
        List<Node> elevatorFloorNode = new ArrayList<>();
        for(Node node : elevatorNode) {
            for (ToNode toNode : node.toNode) {
                Node newNode = MapsActivity.setupGraph.getNodes().jsonList.get(toNode.toNodeID);
                if(newNode.floor.equals(String.valueOf(floor))) elevatorFloorNode.add(newNode);
            }
        }
        return elevatorFloorNode;
    }


    private static void drawRoute(List<Node> nodeList) {
        double cost = 0.0;
        MapDrawer.removePolylines();

        for(int i = 0; i < nodeList.size(); i = i+2) {
            Node start = nodeList.get(i);
            Node end = nodeList.get(i+1);
            cost += MapsActivity.setupGraph.graph.drawPath(start.nodeId.toString(), end.nodeId.toString());
        }
        if(cost != 0.0) {
            String walkingSpeed = graphHandler.graph.calculateWalkingSpeed(cost);
            MapsActivity.textSpeed.setText("ETA: " + walkingSpeed);
            MapsActivity.textSpeedCost.setText("(" + String.valueOf(Math.round(cost)) + "m)");


            LatLng startPositionLatLng = new LatLng(nodeList.get(0).location.get(0), nodeList.get(0).location.get(1));
            LatLng endPositionLatLng = new LatLng(nodeList.get(nodeList.size() -1).location.get(0), nodeList.get(nodeList.size() -1).location.get(1));
            MapsActivity.textFrom.setText(nodeList.get(0).nodeId);
            MapsActivity.textTo.setText(nodeList.get(nodeList.size() -1).nodeId);

            MapDrawer.addMarker(startPositionLatLng, nodeList.get(0).nodeId);
            MapDrawer.addMarker(endPositionLatLng, nodeList.get(nodeList.size() -1).nodeId);


            MapDrawer.hidePolylines();

            MapDrawer.showPolylinesFloor(MapDrawer.getFloor());
            MapDrawer.showPolylinesFloorNav(MapDrawer.getFloor());
        }
    }

    private static final String TAG = "NavAlgo";
    /*
    1. find all elevators
    2. Find Nodes from Elevators
    2. Find Elevator node on new Floor
    3. calculate route foreach elevator
    4. return shortest route
    */
    private static List<Node> travelByElevatorOrStair(Travel travel, Room startPos, Room endPos) {
        String endLocation= endPos.getLocation();
        String[] splitEndLocation = splitLocation(endLocation);
        int endFloor = Integer.valueOf(splitEndLocation[0].substring(1));
        List<Node> startTravelTypeTopN, endTravelTypeDownN;
        // 1. find all elevators
        // 2. Find Nodes from Elevators
        if(travel == Travel.STAIR) {
            startTravelTypeTopN = findAllStairsByNode();
            endTravelTypeDownN = findAllStairsByNodeFloor(startTravelTypeTopN, endFloor);
        } else {
            startTravelTypeTopN = findAllElevatorsByNode();
            endTravelTypeDownN = findAllElevatorsByNodeFloor(startTravelTypeTopN, endFloor);
        }


        // 3. calculate route foreach elevator
        Node shortestStartNodeSF = null;
        Node shortestEndNodeSF = null;
        Node shortestStartNodeEF = null;
        Node shortestEndNodeEF = null;
        double shortestCost = 0.0;
        for (int i = 0; i < endTravelTypeDownN.size(); i++) {
            Node startPositionNode = graphHandler.getNodes().FindClosestNodeInsideRoom(startPos);
            Node startTravelTypeTop = startTravelTypeTopN.get(i);
            double costStartFloor = MapsActivity.setupGraph.graph.drawPath(startPositionNode.nodeId.toString(), startTravelTypeTop.nodeId.toString());

            Node endTravelTypeDown = endTravelTypeDownN.get(i);
            Node endPositionNode = graphHandler.getNodes().FindClosestNodeInsideRoom(endPos);
            double costEndFloor = MapsActivity.setupGraph.graph.drawPath(endTravelTypeDown.nodeId.toString(), endPositionNode.nodeId.toString());

            double cost = costStartFloor + costEndFloor;
            if(shortestCost == 0.0) shortestCost = cost;
            if(shortestCost >= cost) {
                shortestStartNodeSF = startPositionNode;
                shortestEndNodeSF = startTravelTypeTop;
                shortestStartNodeEF = endTravelTypeDown;
                shortestEndNodeEF = endPositionNode;
                shortestCost = cost;
            }
        }

        //4. return shortest route
        List<Node> returnNodeList = new ArrayList<>();
        returnNodeList.add(shortestStartNodeSF);
        returnNodeList.add(shortestEndNodeSF);
        returnNodeList.add(shortestStartNodeEF);
        returnNodeList.add(shortestEndNodeEF);
        return returnNodeList;
    }

    public static boolean buildingHaveBridge(String building) { // Floor 2
        List<Reparation.BuildingEnum> buildingWithBridges = new ArrayList<Reparation.BuildingEnum>();

        buildingWithBridges.add(Reparation.BuildingEnum.C);
        buildingWithBridges.add(Reparation.BuildingEnum.B);
        buildingWithBridges.add(Reparation.BuildingEnum.D);
        buildingWithBridges.add(Reparation.BuildingEnum.E);
        buildingWithBridges.add(Reparation.BuildingEnum.F);
        buildingWithBridges.add(Reparation.BuildingEnum.G);
        buildingWithBridges.add(Reparation.BuildingEnum.G);
        buildingWithBridges.add(Reparation.BuildingEnum.H);
        buildingWithBridges.add(Reparation.BuildingEnum.T);
        for(Reparation.BuildingEnum b : buildingWithBridges) {
            if(Reparation.BuildingEnum.valueOf(building).equals(b)) return true;
        }
        return false;
    }

    private static String[] splitLocation(String location) {
        return location.split("\\.");
    }

    private static GraphHandler graphHandler = MapsActivity.setupGraph;
    //From Room -> To Room

    //Called by methods
    public static void navigate3(String start, String end) {
        //Removes From -> To Fragement;
        MapsActivity.fNavigationInfoBottom.setVisibility(View.INVISIBLE);
        //Removes existing Polylines
        MapDrawer.removePolylines();
        MapDrawer.removeMarkers();

        boolean sucess = navigateRoute3(start, end);

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

    public static boolean navigateRoute3(String startPosition, String endPosition) {
        double extraCost = 0.0;
        LatLng startPositionLatLng;
        LatLng endPositionLatLng;
        Node endNode, startNode;
        Room startRoom = null, endRoom = null;

        //if not a custom location
        if(MapsActivity.customStartPos == null) {
            startRoom = graphHandler.rooms.getRoom(startPosition);

            if (startRoom == null) {
                Toast.makeText(MapsActivity.getContext().getApplicationContext(), "From" + startPosition + " not found", Toast.LENGTH_SHORT).show();
                return false;
            }

            startNode = graphHandler.getNodes().FindClosestNodeInsideRoom(startRoom);
            if(startNode == null) {
                startNode = graphHandler.getNodes().findNearestNode(startRoom.getLatLngBoundsCenter());
                extraCost = CalcMath.measureMeters(startRoom.getLatLngBoundsCenter().latitude, startRoom.getLatLngBoundsCenter().longitude, startNode.location.get(0), startNode.location.get(1));
            }

            startPositionLatLng = new LatLng(startNode.location.get(0), startNode.location.get(1));
        } else {
            startNode = graphHandler.getNodes().findNearestNode(MapsActivity.customStartPos);
            extraCost = CalcMath.measureMeters(MapsActivity.customStartPos.latitude, MapsActivity.customStartPos.longitude, startNode.location.get(0), startNode.location.get(1));
            startPositionLatLng = MapsActivity.customStartPos;
        }

        //if not a custom location
        if(MapsActivity.customEndPos == null) {
            String re1="([a-z])";	// Any Single Word Character (Not Whitespace) 1
            String re2="(\\d+)";	// Integer Number 1
            String re3="(.)";	// Any Single Character 1
            String re4="(\\d)";	// Any Single Digit 1
            String re5="(\\d)";	// Any Single Digit 2

            Pattern p = Pattern.compile(re1+re2+re3+re4+re5,Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
            Matcher m = p.matcher(endPosition);
            if (m.matches()) {
                endRoom = graphHandler.rooms.getRoom(endPosition);
            } else {
                List<Room> roomsWithName = graphHandler.rooms.getAllRoomsWithName(endPosition);
                if(roomsWithName.size() != 1) {
                    double tempCost = 0.0;
                    Room tempRoom = null;
                    for (Room room : roomsWithName) {
                        Node node = graphHandler.getNodes().FindClosestNodeInsideRoom(room);
                        //find shortest route to position
                        if(node != null) {
                            double cost = graphHandler.graph.drawPath(startNode.nodeId.toString(), node.nodeId.toString());
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
                    endRoom = graphHandler.rooms.getRoom(endPosition);
                }
            }

            if (endRoom == null) {
                Toast.makeText(MapsActivity.getContext().getApplicationContext(), "To " + endPosition + " not found", Toast.LENGTH_SHORT).show();
                return false;
            }

            endNode = graphHandler.getNodes().FindClosestNodeInsideRoom(endRoom);
            if (endNode == null) {
                endNode = graphHandler.getNodes().findNearestNode(endRoom.getLatLngBoundsCenter());
                extraCost = CalcMath.measureMeters(endRoom.getLatLngBoundsCenter().latitude, endRoom.getLatLngBoundsCenter().longitude, endNode.location.get(0), endNode.location.get(1));
            }

            endPositionLatLng = new LatLng(endNode.location.get(0), endNode.location.get(1));
        } else {
            endNode = graphHandler.getNodes().findNearestNode(MapsActivity.customEndPos);
            extraCost = CalcMath.measureMeters(MapsActivity.customEndPos.latitude, MapsActivity.customEndPos.longitude, endNode.location.get(0), endNode.location.get(1));
            endPositionLatLng = MapsActivity.customEndPos;
        }

        if(startNode == null || endNode == null) {
            Toast.makeText(MapsActivity.getContext().getApplicationContext(), "Navigation not found", Toast.LENGTH_SHORT).show();
            return false;
        }


        NavAlgo(startRoom, endRoom);
        //double cost = graphHandler.graph.drawPath(startNode.nodeId.toString(), endNode.nodeId.toString());
        /*
        if(cost != 0.0) {
            cost += extraCost;
            String walkingSpeed = graphHandler.graph.calculateWalkingSpeed(cost);
            MapsActivity.textSpeed.setText("ETA: " + walkingSpeed);
            MapsActivity.textSpeedCost.setText("(" + String.valueOf(Math.round(cost)) + "m)");

            MapsActivity.textFrom.setText(startPosition);
            MapsActivity.textTo.setText(endPosition);

            MapDrawer.addMarker(startPositionLatLng, startPosition);
            MapDrawer.addMarker(endPositionLatLng, endPosition);
            return true;
        }
        */
        return true;
    }

    //Called by methods
    public static void navigate(String start, String end) {
        //Removes From -> To Fragement;
        MapsActivity.fNavigationInfoBottom.setVisibility(View.INVISIBLE);
        //Removes existing Polylines
        MapDrawer.removePolylines();
        MapDrawer.removeMarkers();

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
        LatLng startPositionLatLng;
        LatLng endPositionLatLng;
        Node endNode, startNode;
        Room startRoom = null, endRoom = null;

        //if not a custom location
        if(MapsActivity.customStartPos == null) {
            startRoom = graphHandler.rooms.getRoom(startPosition);

            if (startRoom == null) {
                Toast.makeText(MapsActivity.getContext().getApplicationContext(), "From" + startPosition + " not found", Toast.LENGTH_SHORT).show();
                return false;
            }

            startNode = graphHandler.getNodes().FindClosestNodeInsideRoom(startRoom);
            if(startNode == null) {
                startNode = graphHandler.getNodes().findNearestNode(startRoom.getLatLngBoundsCenter());
                extraCost = CalcMath.measureMeters(startRoom.getLatLngBoundsCenter().latitude, startRoom.getLatLngBoundsCenter().longitude, startNode.location.get(0), startNode.location.get(1));
            }

            startPositionLatLng = new LatLng(startNode.location.get(0), startNode.location.get(1));
        } else {
            startNode = graphHandler.getNodes().findNearestNode(MapsActivity.customStartPos);
            extraCost = CalcMath.measureMeters(MapsActivity.customStartPos.latitude, MapsActivity.customStartPos.longitude, startNode.location.get(0), startNode.location.get(1));
            startPositionLatLng = MapsActivity.customStartPos;
        }

        //if not a custom location
        if(MapsActivity.customEndPos == null) {
            String re1="([a-z])";	// Any Single Word Character (Not Whitespace) 1
            String re2="(\\d+)";	// Integer Number 1
            String re3="(.)";	// Any Single Character 1
            String re4="(\\d)";	// Any Single Digit 1
            String re5="(\\d)";	// Any Single Digit 2

            Pattern p = Pattern.compile(re1+re2+re3+re4+re5,Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
            Matcher m = p.matcher(endPosition);
            if (m.matches()) {
                endRoom = graphHandler.rooms.getRoom(endPosition);
            } else {
                List<Room> roomsWithName = graphHandler.rooms.getAllRoomsWithName(endPosition);
                if(roomsWithName.size() != 1) {
                    double tempCost = 0.0;
                    Room tempRoom = null;
                    for (Room room : roomsWithName) {
                        Node node = graphHandler.getNodes().FindClosestNodeInsideRoom(room);
                        //find shortest route to position
                        if(node != null) {
                            double cost = graphHandler.graph.drawPath(startNode.nodeId.toString(), node.nodeId.toString());
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
                    endRoom = graphHandler.rooms.getRoom(endPosition);
                }
            }

            if (endRoom == null) {
                Toast.makeText(MapsActivity.getContext().getApplicationContext(), "To " + endPosition + " not found", Toast.LENGTH_SHORT).show();
                return false;
            }

            endNode = graphHandler.getNodes().FindClosestNodeInsideRoom(endRoom);
            if (endNode == null) {
                endNode = graphHandler.getNodes().findNearestNode(endRoom.getLatLngBoundsCenter());
                extraCost = CalcMath.measureMeters(endRoom.getLatLngBoundsCenter().latitude, endRoom.getLatLngBoundsCenter().longitude, endNode.location.get(0), endNode.location.get(1));
            }

            endPositionLatLng = new LatLng(endNode.location.get(0), endNode.location.get(1));
        } else {
            endNode = graphHandler.getNodes().findNearestNode(MapsActivity.customEndPos);
            extraCost = CalcMath.measureMeters(MapsActivity.customEndPos.latitude, MapsActivity.customEndPos.longitude, endNode.location.get(0), endNode.location.get(1));
            endPositionLatLng = MapsActivity.customEndPos;
        }

        if(startNode == null || endNode == null) {
            Toast.makeText(MapsActivity.getContext().getApplicationContext(), "Navigation not found", Toast.LENGTH_SHORT).show();
            return false;
        }

        double cost = graphHandler.graph.drawPath(startNode.nodeId.toString(), endNode.nodeId.toString());
        if(cost != 0.0) {
            cost += extraCost;
            String walkingSpeed = graphHandler.graph.calculateWalkingSpeed(cost);
            MapsActivity.textSpeed.setText("ETA: " + walkingSpeed);
            MapsActivity.textSpeedCost.setText("(" + String.valueOf(Math.round(cost)) + "m)");

            MapsActivity.textFrom.setText(startPosition);
            MapsActivity.textTo.setText(endPosition);

            MapDrawer.addMarker(startPositionLatLng, startPosition);
            MapDrawer.addMarker(endPositionLatLng, endPosition);
            return true;
        }
        return false;
    }

}

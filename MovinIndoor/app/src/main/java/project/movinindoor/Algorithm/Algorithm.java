/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package project.movinindoor.Algorithm;

import android.view.View;
import android.widget.Toast;

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
import project.movinindoor.Rooms.Room;

/**
 *
 * @author Davey
 */

public class Algorithm {
    public Buildings buildings;


    public Algorithm(Buildings campusWin) {
        buildings = campusWin;
    }

    public void findNearestElevator(String startPos, String name) {
        List<Room> roomsWithName = MapsActivity.setupGraph.getRooms().getAllRoomsWithName(name);
        /*
        if(roomsWithName.size() != 1) {
            double tempCost = 0.0;
            Room tempRoom = null;
            for (Room room : roomsWithName) {
                Node node = MapsActivity.setupGraph.FindClosestNodeInsideRoom(room);
                //find shortest route to position
                if(node != null) {
                    double cost = g.drawPath(startNode.nodeId.toString(), node.nodeId.toString());
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
            endRoom = rooms.getRoom(endPosition);
        }
        */
    }

    private static GraphHandler graphHandler = MapsActivity.setupGraph;
    //From Room -> To Room

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
        Room startRoom, endRoom;

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

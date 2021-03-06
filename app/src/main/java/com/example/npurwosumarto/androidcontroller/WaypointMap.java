package com.example.npurwosumarto.androidcontroller;

import java.util.ArrayList;

/**
 * Created by 301968 on 1/4/2018.
 */

public class WaypointMap {

    ArrayList<Waypoint> waypoints = new ArrayList<>();

    public WaypointMap(){
        createWaypoints();
    }

    public void createWaypoints(){
        Waypoint start = new Waypoint(0, 0, 0);
        waypoints.add(start);
        Waypoint a = new Waypoint(5, 0, 1);
        waypoints.add(a);
        Waypoint b = new Waypoint(5, 5, 2);
        waypoints.add(b);
        Waypoint c = new Waypoint(8, 5, 3);
        waypoints.add(c);
        Waypoint d = new Waypoint(8, 8, 4);
        waypoints.add(d);
        start.setConnection(1, 5);
        start.setConnection(2, 25);
        a.setConnection(0, 5);
        a.setConnection(2, 5);
        a.setConnection(4, 7);
        b.setConnection(0, 25);
        b.setConnection(1, 5);
        b.setConnection(3, 3);
        b.setConnection(4, 5);
        c.setConnection(2, 3);
        c.setConnection(4, 3);
        d.setConnection(1, 7);
        d.setConnection(2, 5);
        d.setConnection(3, 3);
    }

    public ArrayList<Waypoint> findPath(int start_id, int end_id){
        ArrayList<Waypoint> path = new ArrayList<>();
        //if node indicies are out of bounds, return the empty path
        if(start_id >= waypoints.size() || end_id >= waypoints.size()){
            return path;
        }
        ArrayList<Boolean> visited = new ArrayList<>();
        ArrayList<Integer> distances = new ArrayList<>();
        ArrayList<Integer> previousNode = new ArrayList<>();

        //populate unvisited and distance arraylists
        for(int i = 0; i < waypoints.size(); i++){
            visited.add(false);
            distances.add(-1);
            previousNode.add(-1);
        }
        Waypoint currentNode = waypoints.get(start_id);
        distances.set(start_id, 0);

        while(visited.get(end_id) == false) {
            //iterate through unvisited list and update distances to unvisited nodes connected to current node
            for (int i = 0; i < visited.size(); i++) {
                if (visited.get(i) == false && currentNode.getDistanceTo(i) != -1) {
                    if (distances.get(i) == -1 || distances.get(i) > distances.get(currentNode.getId()) + currentNode.getDistanceTo(i)) {
                        distances.set(i, distances.get(currentNode.getId()) + currentNode.getDistanceTo(i));
                        previousNode.set(i, currentNode.getId());
                    }
                }
            }
            //set current node as visited
            visited.set(currentNode.getId(), true);
            //get next unvisited node that has the smallest distance
            currentNode = null;
            for (int i = 0; i < visited.size(); i++){
                if(visited.get(i) == false && distances.get(i) != -1){
                    if(currentNode == null || distances.get(currentNode.getId()) > distances.get(i)){
                        currentNode = waypoints.get(i);
                    }
                }
            }
            //if unvisited node is still null, form the path
            if(currentNode == null){
                currentNode = waypoints.get(end_id);
                while(currentNode.getId() != start_id){
                    path.add(currentNode);
                    currentNode = waypoints.get(previousNode.get(currentNode.getId()));
                }
                return path;
            }
        }
        //create list
        currentNode = waypoints.get(end_id);
        while(currentNode.getId() != start_id){
            path.add(currentNode);
            currentNode = waypoints.get(previousNode.get(currentNode.getId()));
        }
        return path;
    }
}

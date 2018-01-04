package com.example.npurwosumarto.androidcontroller;

import java.util.HashMap;

/**
 * Created by 301968 on 1/4/2018.
 */

public class Waypoint {

    int xpos;
    int ypos;
    int node_id;

    HashMap<Integer, Integer> connections = new HashMap<>();

    public Waypoint(int newxpos, int newypos, int newid){
        xpos = newxpos;
        ypos = newypos;
        node_id = newid;
    }
    //set connections to other waypoints
    public void setConnection(int target_node, int distance){
        connections.put(target_node, distance);
    }

    public int getDistanceTo(int target_node){
        if(connections.containsKey(target_node)){
            return connections.get(target_node);
        }
        return -1;
    }

    public int getId(){
        return node_id;
    }
}

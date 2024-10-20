/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edd_project;

import com.graph.BFSListener;
import com.graph.Station;

/**
 *
 * @author PC
 */
class MyBFSListener implements BFSListener {

    @Override
    public void stationVisited(Station station) {
        System.out.println("Visiting station: " + station.getName());
    }
}

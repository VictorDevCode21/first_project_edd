/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.graph;

/**
 *
 * @author PC
 */
public interface NetworkTrainListener {
    void onStationAdded(Station station);
    void onConnectionAdded(Station station1, Station station2);
}

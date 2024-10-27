/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.graph;

/**
 *
 * @author PC
 */

/**
 *Define un listener que notifique cuando se haga una busqueda BFS
 */
public interface BFSListener {
    
    /**
     * Método llamado cuando una estación es visitada durante la búsqueda en anchura.
     *
     * @param station la estación que ha sido visitada
     */
    void stationVisited(Station station);//
}

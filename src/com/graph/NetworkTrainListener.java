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
 * Interface para recibir notificaciones de eventos de la red de metro.
 */
public interface NetworkTrainListener {
    
    /**
     * Método llamado para notificar que se ha agregado una nueva estación a la red de metro.
     * 
     * @param station La estación que se ha agregado.
     */
    
    void onStationAdded(Station station);
    
    
    /**
     * Método llamado para notificar que se ha agregado una nueva conexión entre estaciones en la red de metro.
     * 
     * @param station1 La primera estación de la conexión.
     * @param station2 La segunda estación de la conexión.
     */
    
    void onConnectionAdded(Station station1, Station station2);
}

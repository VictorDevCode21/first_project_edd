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
 * Interface para recibir notificaciones cuando las estaciones han sido cargadas.
 */
public interface StationLoadListener {
    
    /**
     * Método llamado para notificar que las estaciones han sido cargadas.
     * 
     * @param stations La lista de estaciones que han sido cargadas.
     */
    void onStationsLoaded(LinkedList<Station> stations);
}

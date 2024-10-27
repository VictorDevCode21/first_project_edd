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
 *Define un listener que notifica cuando se produce un cambio en las sucursales
 */
public interface BranchListener {

    
    /**
     * Método que se llamará al cambiar las sucursales
     */
    void onBranchChanged();  
}

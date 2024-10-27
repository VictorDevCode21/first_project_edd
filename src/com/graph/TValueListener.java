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
 * Interface para recibir notificaciones cuando el valor de T ha cambiado.
 */
public interface TValueListener {
    
    
    /**
     * Método llamado para notificar que el valor de T ha cambiado.
     * 
     * @param newT El nuevo valor de T.
     */
    void onTValueChanged(int newT);
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.graph;

import javax.swing.JPanel;

/**
 *
 * @author PC
 */


/**
 * Interface para recibir notificaciones de cambios de panel.
 */
public interface PanelChangeListener {
    
    /**
     * Método llamado para notificar que se ha cambiado el panel.
     * 
     * @param newPanel El nuevo panel que se ha establecido.
     */
    
    void onChangePanel(JPanel newPanel);
}

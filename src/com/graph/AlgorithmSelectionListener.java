package com.graph;

/**
 *
 * @author PC
 */

/**
 * Interface para recibir notificaciones sobre cambios en la selección del algoritmo.
 */
public interface AlgorithmSelectionListener {
    
    /**
     * Método llamado para notificar que se ha seleccionado un algoritmo.
     * 
     * @param useBFS true si se ha seleccionado el algoritmo BFS, false en caso contrario.
     */
    
    void onAlgorithmSelected(boolean useBFS); // Método que se llamará cuando se seleccione un algoritmo
}

package com.graph;

/**
 *
 * @author PC
 */

// Define un listener que notifique sobre cambios en la selección del algoritmo
public interface AlgorithmSelectionListener {
    void onAlgorithmSelected(boolean useBFS); // Método que se llamará cuando se seleccione un algoritmo
}

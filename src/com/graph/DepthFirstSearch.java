/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.graph;

/**
 *
 * @author PC
 */

  /**
   *Clase que implementa el algoritmo de búsqueda en profundidad (DFS) para recorrer estaciones.
   */ 
 
public class DepthFirstSearch {
     
    /** Grafo que representa la red de trenes. */
    private NetworkTrain graph;
    
    /** Lista enlazada para almacenar los nombres de las estaciones visitadas. */
    private LinkedList<String> visitedStations; 

    
    /**
     * Constructor para inicializar la búsqueda en profundidad.
     *
     * @param graph el grafo que representa la red de trenes
     */
    
    public DepthFirstSearch(NetworkTrain graph) {
        this.graph = graph;
        visitedStations = new LinkedList<>();
    }

    /**
     * Realiza la búsqueda en profundidad desde la estación de inicio.
     *
     * @param startStation el nombre de la estación de inicio
     * @return una lista enlazada con los nombres de las estaciones visitadas en el orden en que fueron recorridas
     */
    
    public LinkedList<String> dfs(String startStation) {
        // Simulamos el Stack usando la clase Stack que es una LinkedList 
        Stack<String> stack = new Stack<>();
        stack.push(startStation);

        while (!stack.isEmpty()) {
            String currentStationName = stack.pop();

            // Si la estación no ha sido visitada
            if (!visitedStations.contains(currentStationName)) {
                visitedStations.add(currentStationName); // Añadir el nombre de la estación visitada

                // Obtener la estación actual
                Station currentStation = graph.getStation(currentStationName);
                LinkedList<Connection> connections = currentStation.getConnections(); // Obtener conexiones

                // Agregar las estaciones no visitadas al stack
                for (int i = 0; i < connections.size(); i++) {
                    Connection conn = connections.get(i);
                    String neighborName = conn.getStation1().getName().equals(currentStationName)
                            ? conn.getStation2().getName()
                            : conn.getStation1().getName();

                    // Si la estación vecina no ha sido visitada, la agregamos al stack
                    if (!visitedStations.contains(neighborName)) {
                        stack.push(neighborName);
                    }
                }
            }
        }

        return visitedStations; // Retorna la lista de estaciones visitadas
    }
}

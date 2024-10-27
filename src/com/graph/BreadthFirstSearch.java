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
  *Clase que implementa el algoritmo de búsqueda en anchura (BFS) para recorrer estaciones.
  */
public class BreadthFirstSearch {
    
    private Station startStation; // Estación de inicio para el algoritmo
    private LinkedList<Station> visitedStations; // LinkedList para almacenar estaciones visitadas
    
    /**
     * Constructor para inicializar la búsqueda en anchura.
     *
     * @param startStation la estación de inicio para el algoritmo
     * @param visitedStations la lista de estaciones visitadas
     */

    public BreadthFirstSearch(Station startStation, LinkedList<Station> visitedStations) {
        this.startStation = startStation;
        this.visitedStations = visitedStations; // Inicializa la lista de estaciones visitadas
    }

    /**
     * Realiza la búsqueda en anchura desde la estación de inicio y notifica a los listeners.
     *
     * @param listener el listener que será notificado cuando una estación sea visitada
     */
    
    public void traverse(BFSListener listener) {
        Queue<Station> queue = new Queue<>();
        visitedStations.add(startStation); // Añadimos la estación de inicio a las visitadas
        queue.enqueue(startStation);


        while (!queue.isEmpty()) {
            Station currentStation = queue.dequeue();
            listener.stationVisited(currentStation);


            // Verificar las conexiones de la estación actual
            for (Connection connection : currentStation.getConnections()) {
                // Determinar la estación adyacente
                Station adjacentStation = (connection.getStation1().equals(currentStation))
                        ? connection.getStation2()
                        : connection.getStation1();

                // Verificar si ya fue visitada
                if (!isVisited(adjacentStation)) {
                    visitedStations.add(adjacentStation); // Añadimos al conjunto de visitadas
                    queue.enqueue(adjacentStation);

                } else {

                }
            }
        }
    }

    /**
     * Imprime las estaciones visitadas en el orden en que fueron recorridas.
     */
    
    public void printVisitedStations() {
        System.out.println("Estaciones visitadas en orden:");
        Node<Station> aux = visitedStations.getHead();
        while (aux != null) {
            Station station = aux.getData();
            System.out.println("Estación recorrida: " + station.getName());
            aux = aux.getNext();
        }
    }

    /**
     * Verifica si una estación ya ha sido visitada.
     *
     * @param station la estación a verificar
     * @return true si la estación ya fue visitada, false en caso contrario
     */
    
    private boolean isVisited(Station station) {
        Node<Station> visitedAux = visitedStations.getHead(); // Obtener la cabeza de la lista de visitadas
        while (visitedAux != null) {
            if (visitedAux.getData().equals(station)) {
                return true; // La estación ya fue visitada
            }
            visitedAux = visitedAux.getNext(); // Mover al siguiente nodo
        }
        return false; // La estación no ha sido visitada
    }

    /**
     * Obtiene la lista de estaciones visitadas.
     *
     * @return la lista de estaciones visitadas
     */
    
    public LinkedList<Station> getVisitedStations() {
        return this.visitedStations;
    }
}

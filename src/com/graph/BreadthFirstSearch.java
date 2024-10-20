/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.graph;

/**
 *
 * @author PC
 */
public class BreadthFirstSearch {

    private Station startStation; // Estación de inicio para el algoritmo
    private LinkedList<Station> visitedStations; // LinkedList para almacenar estaciones visitadas

    public BreadthFirstSearch(Station startStation, LinkedList<Station> visitedStations) {
        this.startStation = startStation;
        this.visitedStations = visitedStations; // Inicializa la lista de estaciones visitadas
    }

    public void traverse(BFSListener listener) {
        Queue<Station> queue = new Queue<>();
        visitedStations.add(startStation); // Añadimos la estación de inicio a las visitadas
        queue.enqueue(startStation);
        System.out.println("Iniciando BFS desde la estación: " + startStation.getName());

        while (!queue.isEmpty()) {
            Station currentStation = queue.dequeue();
            listener.stationVisited(currentStation);
            System.out.println("Visitando estación: " + currentStation.getName());

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
                    System.out.println("Encolando estación: " + adjacentStation.getName());
                } else {
                    System.out.println("Conexión ya visitada entre: " + currentStation.getName() + " y " + adjacentStation.getName());
                }
            }
        }
    }

    public void printVisitedStations() {
        System.out.println("Estaciones visitadas en orden:");
        Node<Station> aux = visitedStations.getHead();
        while (aux != null) {
            Station station = aux.getData();
            System.out.println("Estación recorrida: " + station.getName());
            aux = aux.getNext();
        }
    }

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

    public LinkedList<Station> getVisitedStations() {
        return this.visitedStations;
    }
}

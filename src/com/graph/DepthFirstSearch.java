/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.graph;

/**
 *
 * @author PC
 */
public class DepthFirstSearch {

    private NetworkTrain graph;
    private LinkedList visitedStations;

    public DepthFirstSearch(NetworkTrain graph) {
        this.graph = graph;
        visitedStations = new LinkedList();
    }

    public void dfs(String startStation) {
        //  Simulamos el Stack usando la clase Stack que es una LinkedList 
        Stack stack = new Stack();
        stack.push(startStation);

        while (!stack.isEmpty()) {
            Station currentStation = stack.pop();

            //  Si la estacion no ha sido visitada
            if (!visitedStations.contains(currentStation)) {
//                System.out.println("Visitando: " + currentStation);
                visitedStations.add(currentStation);

                // Obtener estaciones adyacentes (conexiones)
                Station station = graph.getStation(currentStation.getName());
                LinkedList connections = station.getConnections(); // Devuelve las conexiones

                // Agregamos las estaciones no visitadas al stack 
                for (int i = 0; i < connections.size(); i++) {
                    Connection conn = (Connection) connections.get(i);

                    // Determinamos cual es la estacion vecina
                    String neighbor = null;

                    if (conn.getStation1().getName().equals(currentStation)) {
                        neighbor = conn.getStation2().getName();
                    } else {
                        neighbor = conn.getStation1().getName();
                    }

                    // Si la estacion vecina no ha sido visitada la agregamos al stack
                    if (!visitedStations.contains(neighbor)) {
                        stack.push(neighbor);
                    }

                }

            }

        }

    }

}

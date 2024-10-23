/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.graph;

/**
 *
 * @author PC
 */
public class Station {

    private String name; // Es el atributo que identifica la lista de estaciones
    private LinkedList connections; // Lista de aristas/conexiones
    private boolean visited; // Atributo para saber si este vertice fue visitado
    private LinkedList<Station> neighbors = new LinkedList<>();

    /* Lista para guardar
    los vertices vecinos
     */
 /* Constructor de la clase estacion que pide como parametro el name y 
    crea una nueva lista de conexiones */
    public Station(String name) {
        this.name = name;
        this.connections = new LinkedList(); // Lista de conexiones a otras estaciones
    }

    // Agrega una conexion 
    public void addConnection(Connection connection) {
        if (!connectionsContains(connection)) {
            this.getConnections().add(connection);
        }
    }

    private boolean connectionsContains(Connection connection) {
        Node current = connections.getHead();
        while (current != null) {
            Connection existingConnection = (Connection) current.getData();
            if (existingConnection.isEqual(connection)) {
                return true;
            }
            current = current.getNext();
        }
        return false;
    }

    // Obtiene el nombre de la estacion
    public String getName() {
        return name;
    }

    // Obtiene una lista de conexiones
    public LinkedList<Connection> getConnections() {
        return connections;
    }

    // Método para obtener conexiones especiales (objetos JSON)
    public LinkedList getSpecialConnections() {
        LinkedList specialConnections = new LinkedList();

        Node current = getConnections().getHead();
        while (current != null) {
            Connection connection = (Connection) current.getData();

            // Comprobar si la conexión es un objeto de tipo especial
            if (isSpecialConnection(connection)) {
                specialConnections.add(connection); // Agregar a la lista de conexiones especiales
            }
            current = current.getNext();
        }

        return specialConnections; // Devolver la lista de conexiones especiales
    }

    // Método para determinar si la conexión es especial
    private boolean isSpecialConnection(Connection connection) {
        // Aquí puedes implementar la lógica para identificar si una conexión
        // es especial, basándote en las características que se definen en tu JSON
        // Por ejemplo, si la conexión se considera especial por su estación de destino.

        // Obtén el nombre de la estación de destino
        String station2Name = connection.getStation2().getName();

        // Por simplicidad, vamos a asumir que si el nombre de la estación de destino
        // contiene un guion, se considera una conexión especial.
        return station2Name.contains("-");
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @param connections the connections to set
     */
    public void setConnections(LinkedList connections) {
        this.connections = connections;
    }

    /**
     * @return the visited
     */
    public boolean isVisited() {
        return visited;
    }

    /**
     * @param visited the visited to set
     */
    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    /**
     * @return the neighbors
     */
    public LinkedList<Station> getNeighbors() {
        return neighbors;
    }

    /**
     * @param neighbors the neighbors to set
     */
    public void setNeighbors(LinkedList<Station> neighbors) {
        this.neighbors = neighbors;
    }

    public void printConnections(LinkedList<Station> stations) {
        for (Station station : stations) {
            System.out.println("Estación: " + station.getName());
            for (Connection connection : station.getConnections()) {
                System.out.println("  Conexión a: " + (connection.getStation1().equals(station)
                        ? connection.getStation2().getName()
                        : connection.getStation1().getName()));
            }
        }
    }

    @Override
    public String toString() {
        return this.name; // Devuelve el name de la Station 
    }

}

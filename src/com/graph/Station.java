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
 * Clase que representa una estación en la red de metro.
 */
public class Station {

    private String name; // Es el atributo que identifica la lista de estaciones
    private LinkedList connections; // Lista de aristas/conexiones
    private boolean visited; // Atributo para saber si este vertice fue visitado
    private LinkedList<Station> neighbors = new LinkedList<>();

    /**
     * Constructor de la clase estación que pide como parámetro el nombre y 
     * crea una nueva lista de conexiones.
     * 
     * @param name El nombre de la estación.
     */
    
    public Station(String name) {
        this.name = name;
        this.connections = new LinkedList<>(); // Lista de conexiones a otras estaciones
    }

    /**
     * Agrega una conexión a la estación.
     * 
     * @param connection La conexión a agregar.
     */
    
    public void addConnection(Connection connection) {
        if (!connectionsContains(connection)) {
            this.getConnections().add(connection);
        }
    }
    
    /**
     * Verifica si la conexión ya existe en la lista de conexiones.
     * 
     * @param connection La conexión a verificar.
     * @return true si la conexión ya existe, false en caso contrario.
     */

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
    
    
    /**
     * Obtiene el nombre de la estación.
     * 
     * @return El nombre de la estación.
     */

    public String getName() {
        return name;
    }

    /**
     * Obtiene el nombre de la estación.
     * 
     * @return El nombre de la estación.
     */
    
    public LinkedList<Connection> getConnections() {
        return connections;
    }
    
    
    /**
     * Obtiene una lista de conexiones especiales.
     * 
     * @return La lista de conexiones especiales.
     */

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
    
    /**
     * Determina si la conexión es especial.
     * 
     * @param connection La conexión a verificar.
     * @return true si la conexión es especial, false en caso contrario.
     */

    private boolean isSpecialConnection(Connection connection) {
        

        // Obtén el nombre de la estación de destino
        String station2Name = connection.getStation2().getName();

        
        return station2Name.contains("-");
    }

    /**
     * Establece el nombre de la estación.
     * 
     * @param name El nombre a establecer.
     */
    
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Establece la lista de conexiones de la estación.
     * 
     * @param connections La lista de conexiones a establecer.
     */
    
    public void setConnections(LinkedList connections) {
        this.connections = connections;
    }

    /**
     * Verifica si la estación ha sido visitada.
     * 
     * @return true si la estación ha sido visitada, false en caso contrario.
     */
    
    public boolean isVisited() {
        return visited;
    }

    /**
     * Establece el estado de visita de la estación.
     * 
     * @param visited El estado de visita a establecer.
     */
    
    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    /**
     * Obtiene la lista de estaciones vecinas.
     * 
     * @return Una lista de estaciones vecinas.
     */
    
    public LinkedList<Station> getNeighbors() {
        return neighbors;
    }

    /**
     * Establece la lista de estaciones vecinas.
     * 
     * @param neighbors La lista de estaciones vecinas a establecer.
     */
    
    public void setNeighbors(LinkedList<Station> neighbors) {
        this.neighbors = neighbors;
    }
    
    
    /**
     * Imprime las conexiones de una lista de estaciones.
     * 
     * @param stations La lista de estaciones cuyas conexiones se imprimirán.
     */

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
    
    
    /**
     * Devuelve el nombre de la estación.
     * 
     * @return El nombre de la estación.
     */
    
    @Override
    public String toString() {
        return this.name; // Devuelve el name de la Station 
    }

}

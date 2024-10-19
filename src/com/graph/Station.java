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

    String name; // Es el atributo que identifica la lista de estaciones
    LinkedList connections; // Lista de aristas/conexiones

    /* Constructor de la clase estacion que pide como parametro el name y 
    crea una nueva lista de conexiones */
    public Station(String name) {
        this.name = name;
        this.connections = new LinkedList(); // Lista de conexiones a otras estaciones
    }

    // Agrega una conexion 
    public void addConnection(Connection connection) {
        this.connections.add(connection);
    }

    // Obtiene el nombre de la estacion
    public String getName() {
        return name;
    }

    // Obtiene una lista de conexiones
    public LinkedList getConnections() {
        return connections;
    }

    // Método para obtener conexiones especiales (objetos JSON)
    public LinkedList getSpecialConnections() {
        LinkedList specialConnections = new LinkedList();

        Node current = connections.getHead();
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
}

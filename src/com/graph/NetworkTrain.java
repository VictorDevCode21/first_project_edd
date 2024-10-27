package com.graph;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;

/**
 *
 * @author PC
 */


/**
 *
 * Clase que representa una red del metro 
 */
public class NetworkTrain {

    private LinkedList<Station> stations;
    private Graph graph;
    private LinkedList<NetworkTrainListener> networkListeners;

    
    
    /**
     * Constructor de la clase NetworkTrain.
     * Inicializa las listas de estaciones y listeners, y crea un grafo para la red del metro.
     */
    
    public NetworkTrain() {
        this.stations = new LinkedList();
        this.graph = new SingleGraph("Metro de Caracas");
        this.networkListeners = new LinkedList<>(); // Inicializar la lista correctamente
    }
    
    
    /**
     * Agrega un listener a la lista de listeners de la red del metro.
     * 
     * @param listener El listener a agregar.
     */

    public void addListener(NetworkTrainListener listener) {
        networkListeners.add(listener);
    }
    
    /**
     * Elimina un listener de la lista de listeners de la red del metro.
     * 
     * @param networkListener El listener a eliminar.
     */

    public void removeListener(NetworkTrainListener networkListener) {
        networkListeners.remove(networkListener);
    }
    
    /**
     * Notifica a todos los listeners que se ha agregado una nueva estación.
     * 
     * @param station La estación que se ha agregado.
     */

    private void notifyStationAdded(Station station) {
        for (NetworkTrainListener networkListener : networkListeners) {
            networkListener.onStationAdded(station);
        }
    }
    
    
    /**
     * Notifica a todos los listeners que se ha agregado una nueva conexión entre estaciones.
     * 
     * @param station1 La primera estación de la conexión.
     * @param station2 La segunda estación de la conexión.
     */

    private void notifyConnectionAdded(Station station1, Station station2) {
        for (NetworkTrainListener networkListener : networkListeners) {
            networkListener.onConnectionAdded(station1, station2);
        }
    }
    
    
    /**
     * Agrega una nueva estación a la red de metro.
     * 
     * @param name El nombre de la estación a agregar.
     */

    public void addStation(String name) {
        // Verificar si la estación ya existe
        if (getStation(name) == null) {
            Station newStation = new Station(name); 
            getStations().add(newStation); 
            graph.addNode(name); // Agregar nodo al grafo solo si no existe
            notifyStationAdded(newStation); // Notifica a sus listener
        }
    }
    
    
    /**
     * Agrega una nueva estación a la red de metro y la asocia a una línea.
     * 
     * @param lineName El nombre de la línea a la que pertenece la estación.
     * @param stationName El nombre de la estación a agregar.
     */

    public void addStation(String lineName, String stationName) {
        // Verificar si la estación ya existe
        if (getStation(stationName) == null) {
            Station newStation = new Station(stationName);
            getStations().add(newStation);
            graph.addNode(stationName); // Agregar nodo al grafo solo si no existe
            notifyStationAdded(newStation); // Notificar a los listeners que se añadió la estación
        }
        
    }
    
    /**
     * Obtiene una estación por su nombre.
     * 
     * @param name El nombre de la estación a buscar.
     * @return La estación encontrada, o null si no se encuentra.
     */

    public Station getStation(String name) {
        Node aux = getStations().getHead();

        while (aux != null) {
            Station station = (Station) aux.getData();
            if (station.getName().equals(name)) {
                return station;
            }
            aux = aux.getNext();
        }
        return null;
    }
    
    /**
     * Agrega una conexión entre dos estaciones.
     * 
     * @param name1 El nombre de la primera estación.
     * @param name2 El nombre de la segunda estación.
     */

    public void addConnection(String name1, String name2) {
        Station station1 = getStation(name1);
        Station station2 = getStation(name2);

        if (station1 != null && station2 != null) {
            // Verificar si la conexión ya existe
            boolean connectionExists = false;

            for (Node<Connection> connNode = station1.getConnections().getHead(); connNode != null; connNode = connNode.getNext()) {
                Connection connection = connNode.getData();
                if ((connection.getStation1().equals(station1) && connection.getStation2().equals(station2))
                        || (connection.getStation1().equals(station2) && connection.getStation2().equals(station1))) {
                    connectionExists = true;
                    break;
                }
            }

            // Solo agregar la conexión si no existe
            if (!connectionExists) {
                Connection connection = new Connection(station1, station2); // Tipo regular por defecto
                station1.addConnection(connection);
                station2.addConnection(connection);
                graph.addEdge(station1.getName() + "-" + station2.getName(), station1.getName(), station2.getName());
//                System.out.println("Conexión agregada: " + station1.getName() + " <-> " + station2.getName());
                // Notificar que una conexión fue añadida
                notifyConnectionAdded(station1, station2);
            } else {
//                System.out.println("La conexión ya existe: " + station1.getName() + " <-> " + station2.getName());
            }
        }
    }
    
    /**
     * Obtiene los vecinos de una estación.
     * 
     * @param station La estación de la que se quieren obtener los vecinos.
     * @return Una lista con las estaciones vecinas.
     */

    public LinkedList<Station> getNeighbors(Station station) {
        LinkedList<Station> neighbors = new LinkedList<>(); // Lista para almacenar los vecinos

        // Itera sobre todas las estaciones
        for (Node<Station> stationNode = stations.getHead(); stationNode != null; stationNode = stationNode.getNext()) {
            Station currentStation = stationNode.getData();

            // Verifica si la estación actual tiene conexiones
            for (Node<Connection> connNode = currentStation.getConnections().getHead(); connNode != null; connNode = connNode.getNext()) {
                Connection connection = connNode.getData();

                // Comprueba si la conexión incluye la estación dada
                if (connection.getStation1().equals(station)) {
                    neighbors.add(connection.getStation2()); // Agrega la estación vecina
                } else if (connection.getStation2().equals(station)) {
                    neighbors.add(connection.getStation1()); // Agrega la estación vecina
                }
            }
        }

        return neighbors; // Retorna la lista de vecinos
    }
    
    /**
     * Obtiene una estación por su nombre.
     * 
     * @param name El nombre de la estación a buscar.
     * @return La estación encontrada, o null si no se encuentra.
     */

    public Station getStationByName(String name) {
        Node<Station> current = stations.getHead();
        while (current != null) {
            Station station = current.getData();
            if (station.getName().equals(name)) {
                return station;
            }
            current = current.getNext();
        }
        return null; // Retorna null si no se encuentra la estación
    }
    
    /**
     * Carga la red de trenes desde un archivo JSON.
     * 
     * @param json El objeto JSON que contiene la información de la red de trenes.
     */

    public void loadFromJson(JSONObject json) {
        String networkName = json.keys().next();
        JSONArray metroLines = json.getJSONArray(networkName);

        // Recorrer cada línea del metro
        for (int i = 0; i < metroLines.length(); i++) {
            JSONObject lineObject = metroLines.getJSONObject(i);

            for (String lineName : lineObject.keySet()) {
                JSONArray stationsArray = lineObject.getJSONArray(lineName);
                Station previousStation = null;

                for (int j = 0; j < stationsArray.length(); j++) {
                    Object station = stationsArray.get(j);
                    String stationName;

                    if (station instanceof String) {
                        stationName = (String) station;
                        addStation(stationName); // Agregar la estación

                        // Conectar la estación anterior si existe
                        if (previousStation != null) {
                            addConnection(previousStation.getName(), stationName); // Conexión regular
                        }
                        previousStation = getStation(stationName);
                    } else if (station instanceof JSONObject) {
                        JSONObject connection = (JSONObject) station;
                        String station1 = connection.keys().next();
                        String station2 = connection.getString(station1);

                        addStation(station1);
                        addStation(station2);

                        // Conectar la estación de transferencia con la anterior si existe
                        if (previousStation != null) {
                            addConnection(previousStation.getName(), station1); // Conexión con estación anterior
                        }

                        // Agregar conexión como regular entre las estaciones de transferencia
                        addConnection(station1, station2); // Sin distinción de tipo

                        // Establecer la última estación conocida
                        previousStation = getStation(station1); // Actualizar la estación anterior
                    }
                }
            }
        }

    }
    
    /**
     * Imprime el grafo de la red de metro, mostrando las estaciones y sus conexiones.
     */
    
    public void printGraph() {
        for (Station station : stations) {
            System.out.println("Estación: " + station.getName());
            // Obtener las conexiones de la estación actual
            for (Node<Connection> connNode = station.getConnections().getHead(); connNode != null; connNode = connNode.getNext()) {
                Connection conn = connNode.getData();
                if (conn.getStation1().equals(station)) {
                    System.out.println("  Conexión a: " + conn.getStation2().getName());
                } else if (conn.getStation2().equals(station)) {
                    System.out.println("  Conexión a: " + conn.getStation1().getName());
                }
            }
        }
    }
    
    /**
     * Imprime la red de metro, mostrando las estaciones y sus conexiones.
     */
    
    public void printNetwork() {
        Node aux = getStations().getHead();

        while (aux != null) {
            Station station = (Station) aux.getData();
            System.out.println("Station: " + station.getName());

            Node connAux = station.getConnections().getHead();

            while (connAux != null) {
                Connection connection = (Connection) connAux.getData();
                System.out.println("Connection to: " + connection.getStation2().getName());
                connAux = connAux.getNext();
            }
            aux = aux.getNext();
        }
    }
    
    /**
     * Imprime todas las conexiones en la red de metro.
     */

    public void printAllConnections() {
        System.out.println("Conexiones en la red:");
        for (Station station : this.stations) { // Asegúrate de tener una colección de estaciones
            for (Connection connection : station.getConnections()) {
                System.out.println("Conexión: " + connection.getStation1().getName() + " - " + connection.getStation2().getName());
            }
        }
    }
    
    /**
     * Obtiene la lista de estaciones.
     * 
     * @return La lista de estaciones.
     */
    
    public LinkedList<Station> getStations() {
        return stations;
    }
    
    
    /**
     * Imprime todas las estaciones en la red de metro.
     */

    public void printStations() {
        System.out.println("Estaciones en la red:");
        for (Station station : this.getStations()) {
            System.out.println(station.getName());
        }
    }
    
    /**
     * Establece la lista de estaciones.
     * 
     * @param stations La lista de estaciones a establecer.
     */

    public void setStations(LinkedList stations) {
        this.stations = stations;
    }

    /**
     * Verifica si una estación existe en la red de metro.
     * 
     * @param stationName El nombre de la estación a verificar.
     * @return true si la estación existe, false en caso contrario.
     */
    
    public boolean stationExists(String stationName) {
        // Debe comprobar si la estación existe en la lista de estaciones
        for (Station station : stations) { // Asumiendo que `stations` es tu lista de estaciones
            if (station.getName().equalsIgnoreCase(stationName)) {
                return true;
            }
        }
        return false;
    }

}

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
public class NetworkTrain {

    private LinkedList<Station> stations;
    private Graph graph;
    private LinkedList<NetworkTrainListener> networkListeners;

    public NetworkTrain() {
        this.stations = new LinkedList();
        this.graph = new SingleGraph("Metro de Caracas");
        this.networkListeners = new LinkedList<>(); // Inicializar la lista correctamente
    }

    public void addListener(NetworkTrainListener listener) {
        networkListeners.add(listener);
    }

    public void removeListener(NetworkTrainListener networkListener) {
        networkListeners.remove(networkListener);
    }

    private void notifyStationAdded(Station station) {
        for (NetworkTrainListener networkListener : networkListeners) {
            networkListener.onStationAdded(station);
        }
    }

    private void notifyConnectionAdded(Station station1, Station station2) {
        for (NetworkTrainListener networkListener : networkListeners) {
            networkListener.onConnectionAdded(station1, station2);
        }
    }

    public void addStation(String name) {
        // Verificar si la estación ya existe
        if (getStation(name) == null) {
            Station newStation = new Station(name);
            getStations().add(newStation);
            graph.addNode(name); // Agregar nodo al grafo solo si no existe
            notifyStationAdded(newStation); // Notifica a sus listener
        }
    }

    public void addStation(String lineName, String stationName) {
        // Verificar si la estación ya existe
        if (getStation(stationName) == null) {
            Station newStation = new Station(stationName);
            getStations().add(newStation);
            graph.addNode(stationName); // Agregar nodo al grafo solo si no existe
            notifyStationAdded(newStation); // Notificar a los listeners que se añadió la estación
        }
        // Aquí puedes agregar lógica para asociar la estación a la línea si es necesario
    }

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

    // Método para obtener los vecinos de una estación
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

    // Método para obtener una estación por su nombre
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

    //    
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
//        printGraph();
    }

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

    public void printAllConnections() {
        System.out.println("Conexiones en la red:");
        for (Station station : this.stations) { // Asegúrate de tener una colección de estaciones
            for (Connection connection : station.getConnections()) {
                System.out.println("Conexión: " + connection.getStation1().getName() + " - " + connection.getStation2().getName());
            }
        }
    }

    /**
     * @return the stations
     */
    public LinkedList<Station> getStations() {
        return stations;
    }

    public void printStations() {
        System.out.println("Estaciones en la red:");
        for (Station station : this.getStations()) {
            System.out.println(station.getName());
        }
    }

    /**
     * @param stations the stations to set
     */
    public void setStations(LinkedList stations) {
        this.stations = stations;
    }

    // Método para verificar si una estación existe
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

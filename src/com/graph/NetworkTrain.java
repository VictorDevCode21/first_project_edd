/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
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

    private LinkedList stations;
    private Graph graph;

    public NetworkTrain() {
        this.stations = new LinkedList();
        this.graph = new SingleGraph("Metro de Caracas");
    }

    public void addStation(String name) {
        // Verificar si la estación ya existe
        if (getStation(name) == null) {
            Station newStation = new Station(name);
            getStations().add(newStation);
            graph.addNode(name); // Agregar nodo al grafo solo si no existe
        }
    }

    public void addStation(String lineName, String stationName) {
        // Verificar si la estación ya existe
        if (getStation(stationName) == null) {
            Station newStation = new Station(stationName);
            getStations().add(newStation);
            graph.addNode(stationName); // Agregar nodo al grafo solo si no existe
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

            // Verificar en las conexiones de la primera estación
            for (Node<Connection> connNode = station1.getConnections().getHead(); connNode != null; connNode = connNode.getNext()) {
                Connection connection = connNode.getData();
                if ((connection.getStation1().equals(station1) && connection.getStation2().equals(station2))
                        || (connection.getStation1().equals(station2) && connection.getStation2().equals(station1))) {
                    connectionExists = true;
                    break;
                }
            }

            // Si la conexión no existe, agregarla
            if (!connectionExists) {
                Connection connection = new Connection(station1, station2);
                station1.addConnection(connection);
                station2.addConnection(connection);
            }
        }
    }

    public void addConnection(String name1, String name2, String type) {
        Station station1 = getStation(name1);
        Station station2 = getStation(name2);

        if (station1 != null && station2 != null) {
            // Asignar tipo de conexión si es relevante
            Connection connection = new Connection(station1, station2, type);
            station1.addConnection(connection);
            station2.addConnection(connection);
        }
    }

    //    
    public void loadFromJson(JSONObject json) {
        JSONArray metroLines = json.getJSONArray("Metro de Caracas");

        // Recorrer cada línea del metro
        for (int i = 0; i < metroLines.length(); i++) {
            JSONObject lineObject = metroLines.getJSONObject(i);

            // Obtener el nombre de la línea
            for (String lineName : lineObject.keySet()) {
                JSONArray stationsArray = lineObject.getJSONArray(lineName);
                Station previousStation = null; // Para conectar estaciones en línea

                // Recorrer cada estación en la línea
                for (int j = 0; j < stationsArray.length(); j++) {
                    Object station = stationsArray.get(j);
                    String stationName;

                    if (station instanceof String) {
                        // Agregar estación normal
                        stationName = (String) station;
                        addStation(stationName); // Agregar la estación

                        // Si hay una estación anterior, agregar conexión
                        if (previousStation != null) {
                            addConnection(previousStation.getName(), stationName);
                        }
                        // Actualizar la estación anterior
                        previousStation = getStation(stationName);
                    } else if (station instanceof JSONObject) {
                        // Manejar conexiones peatonales
                        JSONObject connection = (JSONObject) station;
                        String station1 = connection.keys().next(); // Nombre de la estación
                        String station2 = connection.getString(station1); // Estación conectada

                        // Agregar estaciones si no están ya en la lista
                        addStation(station1);
                        addStation(station2);
                        addConnection(station1, station2, "peatonal");
                    }
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

    /**
     * @return the stations
     */
    public LinkedList getStations() {
        return stations;
    }

    /**
     * @param stations the stations to set
     */
    public void setStations(LinkedList stations) {
        this.stations = stations;
    }

}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.interfaces;

import com.graph.NetworkTrain;
import com.graph.LinkedList;
import com.graph.Node;
import com.graph.Connection;
import com.graph.Station;
import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

/**
 *
 * @author carlos
 */
public class GUI extends JFrame {

    private NetworkTrain networkTrain;
    private Graph graphStreamGraph;
    private Set<String> addedStations; // Para evitar agregar una estación más de una vez

    public GUI() {
        setTitle("Supermarket Location Planner");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        addedStations = new HashSet<>(); // Inicializa el conjunto de estaciones agregadas
        initUI();
    }

    private void initUI() {
        // Paneles y componentes
        JButton loadButton = new JButton("Cargar Red de Transporte");
        loadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int result = fileChooser.showOpenDialog(null);
                if (result == JFileChooser.APPROVE_OPTION) {
                    String filePath = fileChooser.getSelectedFile().getAbsolutePath();
                    try {
                        // Leer el contenido del archivo JSON
                        String content = new String(Files.readAllBytes(Paths.get(filePath)));

                        // Convertir el contenido a JSONObject
                        JSONObject jsonObject = new JSONObject(new JSONTokener(content));

                        // Cargar los datos desde el objeto JSONObject
                        networkTrain = new NetworkTrain();
                        networkTrain.loadFromJson(jsonObject);

                        // Mostrar la red en GraphStream
                        showNetworkTrain(jsonObject);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(null, "Error al cargar el archivo JSON: " + ex.getMessage());
                    } catch (org.json.JSONException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(null, "Formato de archivo JSON incorrecto: " + ex.getMessage());
                    }
                }
            }
        });

        // Agregar componentes al frame
        add(loadButton, BorderLayout.NORTH);
        // Otros componentes y configuraciones
    }

    // Mostrar la red con las conexiones
    private void showNetworkTrain(JSONObject jsonObject) {
        System.setProperty("org.graphstream.ui", "swing");
        graphStreamGraph = new SingleGraph("Metro Network");

        try {
            JSONArray metroLines = jsonObject.getJSONArray("Metro de Caracas");

            // Paso 1: Agregar todas las estaciones (nodos) y conexiones
            for (int i = 0; i < metroLines.length(); i++) {
                JSONObject lineObject = metroLines.getJSONObject(i);
                String lineName = lineObject.keys().next();
                JSONArray stations = lineObject.getJSONArray(lineName);

                // Recorrer las estaciones y conectarlas
                for (int j = 0; j < stations.length(); j++) {
                    Object currentStationObj = stations.get(j);
                    String currentStation;
                    String nextStation = null;

                    if (currentStationObj instanceof JSONObject) {
                        // Estación de transferencia con dos conexiones
                        JSONObject connectionObj = (JSONObject) currentStationObj;
                        currentStation = connectionObj.keys().next();
                        nextStation = connectionObj.getString(currentStation);

                        // Agregar nodo solo si no fue agregado previamente
                        if (!addedStations.contains(currentStation)) {
                            addStationToGraph(currentStation);
                            addedStations.add(currentStation);
                        }
                        // Agregar conexión con la siguiente estación en la transferencia
                        addEdgeIfNotExists(currentStation, nextStation);
                    } else {
                        // Estación normal (sin transferencia)
                        currentStation = (String) currentStationObj;
                    }

                    if (j < stations.length() - 1) {
                        // Obtener la siguiente estación
                        Object nextStationObj = stations.get(j + 1);
                        if (nextStationObj instanceof JSONObject) {
                            JSONObject nextConnectionObj = (JSONObject) nextStationObj;
                            nextStation = nextConnectionObj.keys().next();
                        } else {
                            nextStation = (String) nextStationObj;
                        }

                        // Conectar la estación actual con la siguiente
                        addEdgeIfNotExists(currentStation, nextStation);
                    }
                }
            }
            graphStreamGraph.display();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // Agregar estación al grafo si no existe
    private void addStationToGraph(String station) {
        if (graphStreamGraph.getNode(station) == null) {
            graphStreamGraph.addNode(station);
            graphStreamGraph.getNode(station).setAttribute("ui.label", station);
        }
    }

    // Agrega una arista si no existe entre dos estaciones
    private void addEdgeIfNotExists(String station1, String station2) {
        if (!addedStations.contains(station1)) {
            addStationToGraph(station1);
            addedStations.add(station1);
        }

        if (!addedStations.contains(station2)) {
            addStationToGraph(station2);
            addedStations.add(station2);
        }

        // Verifica si la arista ya existe en alguna dirección
        String edgeId = station1 + "-" + station2;
        if (graphStreamGraph.getEdge(edgeId) == null && graphStreamGraph.getEdge(station2 + "-" + station1) == null) {
            graphStreamGraph.addEdge(edgeId, station1, station2);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new GUI().setVisible(true);
            }
        });
    }

}

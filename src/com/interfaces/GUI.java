/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.interfaces;

import com.graph.NetworkTrain;
import com.graph.LinkedList;
import com.graph.Node;
import com.graph.BreadthFirstSearch;
import com.graph.BFSListener;
import com.graph.Station;
import com.graph.DepthFirstSearch;

import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Paths;
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
    private LinkedList <String> stations;
    

    public GUI() {
        setTitle("Supermarket Location Planner");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        stations = new LinkedList<>();
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

    // Muestra el grafo con las estaciones y conexiones
    private void showNetworkTrain(JSONObject jsonObject) {
        System.setProperty("org.graphstream.ui", "swing");
        graphStreamGraph = new SingleGraph("Metro Network");

        try {
            // Muestra un cuadro de diálogo para ingresar la estación inicial
            String startStationName = JOptionPane.showInputDialog(this, "Ingrese el nombre de la estación de inicio:");

            if (startStationName != null && !startStationName.trim().isEmpty()) {
                // Muestra un cuadro de diálogo para seleccionar el algoritmo
                String[] algorithms = {"BFS", "DFS"};
                String selectedAlgorithm = (String) JOptionPane.showInputDialog(
                        this,
                        "Seleccione el algoritmo a usar:",
                        "Algoritmo",
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        algorithms,
                        algorithms[0]
                );

                // Cargar y visualizar la red en GraphStream
                String networkName = jsonObject.keys().next();
                JSONArray metroLines = jsonObject.getJSONArray(networkName);

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
                            JSONObject connectionObj = (JSONObject) currentStationObj;
                            currentStation = connectionObj.keys().next();
                            nextStation = connectionObj.getString(currentStation);

                            addStationToGraph(currentStation);
                            addEdgeIfNotExists(currentStation, nextStation);

                            if (j > 0) {
                                Object prevStationObj = stations.get(j - 1);
                                String previousStation;
                                if (prevStationObj instanceof JSONObject) {
                                    previousStation = ((JSONObject) prevStationObj).keys().next();
                                } else {
                                    previousStation = (String) prevStationObj;
                                }
                                addEdgeIfNotExists(currentStation, previousStation);
                            }
                        } else {
                            currentStation = (String) currentStationObj;
                            addStationToGraph(currentStation);
                        }

                        if (j < stations.length() - 1) {
                            Object nextStationObj = stations.get(j + 1);
                            if (nextStationObj instanceof JSONObject) {
                                JSONObject nextConnectionObj = (JSONObject) nextStationObj;
                                nextStation = nextConnectionObj.keys().next();
                            } else {
                                nextStation = (String) nextStationObj;
                            }
                            addEdgeIfNotExists(currentStation, nextStation);
                        }
                    }
                }

                // Mostrar el grafo
                graphStreamGraph.display();

                // Ejecutar el algoritmo seleccionado
                Station startStation = networkTrain.getStationByName(startStationName);
                if (startStation != null) {
                    if ("BFS".equals(selectedAlgorithm)) {
                        runBFS(startStation);
                    } else if ("DFS".equals(selectedAlgorithm)) {
                        runDFS(startStation);
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Estación no encontrada: " + startStationName);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Debe ingresar un nombre de estación válido.");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void runDFS(Station startStation) {
        // Instancia de la clase DFS
        DepthFirstSearch dfs = new DepthFirstSearch(networkTrain); 

        // Obtener las estaciones visitadas
        LinkedList<String> visited = dfs.dfs(startStation.getName());

        // Cambiar el color de las estaciones en el grafo a verde
        for (String stationName : visited) {
            graphStreamGraph.getNode(stationName).setAttribute("ui.style", "fill-color: green;");
        }

        // Mostrar las estaciones visitadas
        System.out.println("Estaciones visitadas en DFS: " + visited.toString());
    }

    // Método para ejecutar el BFS y colorear las estaciones
    private void runBFS(Station startStation) {
        BreadthFirstSearch bfs = new BreadthFirstSearch(startStation, new LinkedList<Station>());
        //Guardar las estaciones visitadas en una LinkedList
        LinkedList<Station> visitedStations = new LinkedList<>();

        bfs.traverse(new BFSListener() {
            @Override
            public void stationVisited(Station station) {
                // Verifica si la estación ya ha sido visitada
                if (!visitedStations.contains(station)) {
                    visitedStations.add(station); // Marcar estación como visitada
                    if (graphStreamGraph.getNode(station.getName()) != null) {
                        graphStreamGraph.getNode(station.getName()).setAttribute("ui.style", "fill-color: green;");
                    }
                }
            }
        });

        // Mostrar las estaciones visitadas
        Node<Station> aux = bfs.getVisitedStations().getHead();
        while (aux != null) {
            Station station = aux.getData();
              Station visitedStation = aux.getData();
            System.out.println("Estación recorrida en BFS: " + visitedStation.getName()); // Debug
            aux = aux.getNext();
        }   
    }

// Agrega una arista si no existe entre dos estaciones
    private void addEdgeIfNotExists(String station1, String station2) {
        // Asegúrate de que las estaciones sean diferentes
        if (station1.equals(station2)) {
            return; // No agregar conexión si son la misma estación
        }

        addStationToGraph(station1);
        addStationToGraph(station2);

        // Crear un identificador único para la conexión
        String edgeId = station1.compareTo(station2) < 0 ? station1 + "-" + station2 : station2 + "-" + station1;

        // Verifica si la arista ya existe
        if (graphStreamGraph.getEdge(edgeId) == null) {
            graphStreamGraph.addEdge(edgeId, station1, station2);
//            System.out.println("Conexión agregada entre: " + station1 + " y " + station2); // Debug
        }
    }

    // Agregar estación al grafo si no existe
    private void addStationToGraph(String station) {
        if (graphStreamGraph.getNode(station) == null) {
            graphStreamGraph.addNode(station);
            graphStreamGraph.getNode(station).setAttribute("ui.label", station);
        }
    }

    
    // Verificación de que la estación no se repite
    public void verificateStations(String station){
            if (stations.contains(station)) {
                JOptionPane.showMessageDialog(this, "La estación ya existe", "Aviso", JOptionPane.WARNING_MESSAGE);
            } else {
                stations.add(station);
                JOptionPane.showMessageDialog(this, "Estación añadida exitosamente", "Aviso", JOptionPane.INFORMATION_MESSAGE);
                addStationToGraph(station);  // Lógica para agregar al grafo
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

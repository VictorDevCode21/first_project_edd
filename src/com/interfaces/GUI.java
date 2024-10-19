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
import org.json.JSONObject;
import org.json.JSONTokener;

/**
 *
 * @author carlos
 */
public class GUI extends JFrame {

    private NetworkTrain networkTrain;
    private Graph graphStreamGraph;

    public GUI() {
        setTitle("Supermarket Location Planner");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
                        showNetworkTrain();
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

    // Método para mostrar la red en GraphStream
    private void showNetworkTrain() {
        System.setProperty("org.graphstream.ui", "swing");
        graphStreamGraph = new SingleGraph("Metro Network");

        Node stationNode = networkTrain.getStations().getHead();

        // Primero, agregar todos los nodos de estaciones al grafo
        while (stationNode != null) {
            Station station = (Station) stationNode.getData();
            String stationName = station.getName();

            // Agregar el nodo de la estación solo si no existe
            if (graphStreamGraph.getNode(stationName) == null) {
                graphStreamGraph.addNode(stationName);
                graphStreamGraph.getNode(stationName).setAttribute("ui.label", stationName);
            }

            stationNode = stationNode.getNext();  // Avanzar a la siguiente estación
        }

        // Luego, agregar todas las conexiones
        stationNode = networkTrain.getStations().getHead(); // Reiniciar el nodo de estaciones
        while (stationNode != null) {
            Station station = (Station) stationNode.getData();
            addConnectionsToGraph(station); // Agregar conexiones de cada estación

            stationNode = stationNode.getNext();  // Avanzar a la siguiente estación
        }

        graphStreamGraph.display();
    }

// Método auxiliar para agregar conexiones al grafo
    private void addConnectionsToGraph(Station station) {
        LinkedList<Connection> connections = station.getConnections(); // Obtener conexiones de la estación actual

        for (Node<Connection> connectionNode = connections.getHead(); connectionNode != null; connectionNode = connectionNode.getNext()) {
            Connection connection = connectionNode.getData();

            String station1Name = connection.getStation1().getName();
            String station2Name = connection.getStation2().getName();

            // Comprobar si ambos nodos existen en el grafo
            if (graphStreamGraph.getNode(station1Name) != null && graphStreamGraph.getNode(station2Name) != null) {
                String edgeId = station1Name + "-" + station2Name;

                // Verifica si la arista ya existe antes de agregarla
                boolean edgeExists = graphStreamGraph.getEdge(edgeId) != null
                        || graphStreamGraph.getEdge(station2Name + "-" + station1Name) != null;

                if (!edgeExists) {
                    graphStreamGraph.addEdge(edgeId, station1Name, station2Name);
                }
            } else {
                System.out.println("Una de las estaciones no existe: " + station1Name + " o " + station2Name);
            }
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

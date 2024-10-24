package com.interfaces;

import com.graph.NetworkTrain;
import com.graph.LinkedList;
import com.graph.Node;
import com.graph.BreadthFirstSearch;
import com.graph.BFSListener;
import com.graph.Station;
import com.graph.Stack;
import com.graph.Queue;

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
    private LinkedList<String> stations;
    private LinkedList<Station> branches; // Lista para almacenar sucursales


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
                        branches = new LinkedList<>();

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

    public LinkedList<Station> getBranches() {
        // Supongamos que tienes una lista de sucursales en tu clase GUI
        return this.branches; // Devuelve la lista de sucursales como una lista de Strings
    }

    public NetworkTrain getNetworkTrain() {
        return this.networkTrain;  // Donde networkTrain es la instancia de tu grafo
    }

    public void removeBranch(Station branchName) {
        branches.remove(branchName);  // Remueve la sucursal de la lista
        // Actualiza la visualización del grafo, si corresponde.
        updateGraph();
    }

    public void updateGraph() {
        if (graphStreamGraph == null || networkTrain == null) {
            return;  // Si el grafo o la red no están cargados, no hay nada que actualizar
        }

        // Limpiar el grafo actual para volver a cargar la red desde el estado actual de `networkTrain`
        graphStreamGraph.clear();

        // Volver a agregar todas las estaciones y conexiones desde la red de transporte
        LinkedList<Station> allStations = networkTrain.getStations();

        // Recorrer todas las estaciones de la red y añadirlas al grafo de GraphStream
        for (Station station : allStations) {
            addStationToGraph(station.getName());  // Añadir la estación al grafo

            // Obtener los vecinos (conexiones) de la estación actual
            LinkedList<Station> neighbors = networkTrain.getNeighbors(station);

            for (Station neighbor : neighbors) {
                addEdgeIfNotExists(station.getName(), neighbor.getName());  // Añadir las conexiones si no existen
            }
        }

        // Colorear las sucursales en verde para diferenciarlas
        for (Station branch : branches) {
            if (graphStreamGraph.getNode(branch.getName()) != null) {
                graphStreamGraph.getNode(branch.getName()).setAttribute("ui.style", "fill-color: green;");
                graphStreamGraph.getNode(branch.getName()).setAttribute("ui.label", branch.getName());  // Mostrar el nombre de la estación
            }
        }

        // Volver a mostrar el grafo en la interfaz
        graphStreamGraph.display();
    }

    // Muestra el grafo con las estaciones y conexiones
    private void showNetworkTrain(JSONObject jsonObject) {
        System.setProperty("org.graphstream.ui", "swing");
        graphStreamGraph = new SingleGraph("Metro Network");

        try {
            // Muestra un cuadro de diálogo para ingresar la estación inicial
            String startStationName = JOptionPane.showInputDialog(this, 
                    "Ingrese el nombre de la estación de inicio:");

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

                int T = Integer.parseInt(JOptionPane.showInputDialog(this, "Ingrese la distancia T entre sucursales:"));

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
                        runBFS(startStation, T);
                    } else if ("DFS".equals(selectedAlgorithm)) {
                        runDFS(startStation, T);
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

    private int calculateDistance(Station from, Station to) {
        if (from.equals(to)) {
            return 0; // La distancia a sí misma es 0
        }

        // Mapa para rastrear las distancias desde la estación 'from'
        Map<Station, Integer> distances = new HashMap<>();
        Queue<Station> queue = new Queue<>();
        LinkedList<Station> visited = new LinkedList<>(); // Usar LinkedList para rastrear estaciones visitadas

        queue.enqueue(from);
        distances.put(from, 0);
        visited.add(from);

        while (!queue.isEmpty()) {
            Station current = queue.dequeue();
            int currentDistance = distances.get(current);

            // Obtener estaciones vecinas
            LinkedList<Station> neighbors = networkTrain.getNeighbors(current);

            for (Station neighbor : neighbors) {
                // Verifica si la estación ya ha sido visitada
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    distances.put(neighbor, currentDistance + 1);
                    queue.enqueue(neighbor);

                    // Si encontramos la estación objetivo, devolvemos la distancia
                    if (neighbor.equals(to)) {
                        return distances.get(neighbor);
                    }
                }
            }
        }

        // Si no se encuentra el destino, retornar -1 o alguna señal de error
        return -1;
    }

<
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
                    JOptionPane.showMessageDialog(null, "La estación ha sido agregada exitosamente", 
                             "Aviso", JOptionPane.INFORMATION_MESSAGE);
                    if (graphStreamGraph.getNode(station.getName()) != null) {
                        graphStreamGraph.getNode(station.getName()).setAttribute("ui.style", "fill-color: green;");
                    }
                }else{
                    JOptionPane.showMessageDialog(null, "La estación ya existe", 
                             "Aviso", JOptionPane.WARNING_MESSAGE);

    private void runDFS(Station startStation, int T) {
        // Inicializamos la primera sucursal
        branches.add(startStation);
        Map<Station, Integer> distances = new HashMap<>();
        distances.put(startStation, 0); // La distancia de la estación inicial es 0

        // Colorear la primera sucursal en verde
        if (graphStreamGraph.getNode(startStation.getName()) != null) {
            graphStreamGraph.getNode(startStation.getName()).setAttribute("ui.style", "fill-color: green;");
            graphStreamGraph.getNode(startStation.getName()).setAttribute("ui.label", startStation.getName() + " 0");
        }

        // Usamos una pila para implementar DFS manualmente
        Stack<Station> stack = new Stack<>();
        Set<Station> visited = new HashSet<>();
        stack.push(startStation);
        visited.add(startStation);

        // Realizamos el recorrido DFS
        while (!stack.isEmpty()) {
            Station currentStation = stack.pop();
            int currentDistance = distances.get(currentStation);

            // Obtener estaciones vecinas
            LinkedList<Station> neighbors = networkTrain.getNeighbors(currentStation);

            for (Station neighbor : neighbors) {
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    distances.put(neighbor, currentDistance + 1);
                    stack.push(neighbor);

                    // Mostrar el nombre de la estación y su distancia desde la estación inicial en el grafo
                    if (graphStreamGraph.getNode(neighbor.getName()) != null) {
                        graphStreamGraph.getNode(neighbor.getName()).setAttribute("ui.label", neighbor.getName() + " " + distances.get(neighbor));
                    }

                    // Calcular la distancia desde la sucursal a las estaciones
                    int distanceToBranch = calculateDistance(neighbor, startStation);

                    // Verificar si la distancia es divisible por T
                    if (distances.get(neighbor) % T == 0) {
                        boolean canPlaceBranch = true; // Variable para verificar la colocación
                        Station conflictingBranch = null; // Para guardar la sucursal que causa conflicto

                        // Iterar sobre las sucursales ya creadas
                        for (Station branch : branches) {
                            // Comprobar si la sucursal ya creada está a menos de T estaciones
                            int distanceToExistingBranch = calculateDistance(neighbor, branch);
                            if (distanceToExistingBranch < T) {
                                canPlaceBranch = false; // Encontramos una sucursal dentro de T estaciones
                                conflictingBranch = branch; // Guardamos la sucursal conflictiva
                                break;
                            }
                        }

                        // Colocar la sucursal si cumple con las condiciones
                        if (canPlaceBranch) {
                            branches.add(neighbor);

                            // Cambiar el color de la nueva sucursal a verde
                            if (graphStreamGraph.getNode(neighbor.getName()) != null) {
                                graphStreamGraph.getNode(neighbor.getName()).setAttribute("ui.style", "fill-color: green;");
                            }

                            // Imprimir la sucursal creada
                            System.out.println("Sucursal creada en: " + neighbor.getName() + ", distancia: " + distances.get(neighbor));
                        } else {
                            // Si ya hay una sucursal cercana, registrar el intento de colocar la sucursal
                            System.out.println("No se colocó la sucursal en " + neighbor.getName() + " a distancia " + distances.get(neighbor)
                                    + " porque está a menos de T de la sucursal " + conflictingBranch.getName() + ".");
                        }
                    }

                }
            }
        }

        // Mostrar las estaciones visitadas
        Node<Station> aux = bfs.getVisitedStations().getHead();
        while (aux != null) {
            Station station = aux.getData();
              Station visitedStation = aux.getData();
            System.out.println("Estación recorrida en BFS: " + visitedStation.getName()); // Debug
            aux = aux.getNext();
        }  
        // Mostrar todas las distancias y los nombres de las estaciones
        System.out.println("Distancias desde la estación inicial:");
        for (Map.Entry<Station, Integer> entry : distances.entrySet()) {
            Station station = entry.getKey();
            Integer distance = entry.getValue();
            System.out.println("Estación: " + station.getName() + ", Distancia: " + distance);
        }

        // Mostrar las sucursales creadas
        System.out.println("Sucursales creadas (DFS): " + branches.toString());
    }

    private void runBFS(Station startStation, int T) {
        // Inicializar la distancia de la estación inicial
        Queue<Station> queue = new Queue<>();
        branches.add(startStation); // Sucursal inicial
        Set<Station> visitedStations = new HashSet<>();
        Map<Station, Integer> distances = new HashMap<>();
        distances.put(startStation, 0);

        // Colorear la estación inicial en verde
        if (graphStreamGraph.getNode(startStation.getName()) != null) {
            graphStreamGraph.getNode(startStation.getName()).setAttribute("ui.style", "fill-color: green;");
        }

        queue.enqueue(startStation);
        visitedStations.add(startStation);

        while (!queue.isEmpty()) {
            Station current = queue.dequeue();
            LinkedList<Station> neighbors = networkTrain.getNeighbors(current); // Usar el método getNeighbors

            for (Station neighbor : neighbors) {
                if (!visitedStations.contains(neighbor)) {
                    visitedStations.add(neighbor);
                    queue.enqueue(neighbor);
                    distances.put(neighbor, distances.get(current) + 1);

                    // Aquí decides cuándo agregar una nueva sucursal
                    if (distances.get(neighbor) % T == 0) {
                        branches.add(neighbor);

                        // Colorear la nueva sucursal en verde
                        if (graphStreamGraph.getNode(neighbor.getName()) != null) {
                            graphStreamGraph.getNode(neighbor.getName()).setAttribute("ui.style", "fill-color: green;");
                        }
                    }
                }
            }
        }

        // Mostrar las estaciones y sus distancias
        for (Map.Entry<Station, Integer> entry : distances.entrySet()) {
            Station station = entry.getKey();
            Integer distance = entry.getValue();

            // Mostrar el nombre de la estación y su distancia en el grafo
            if (graphStreamGraph.getNode(station.getName()) != null) {
                graphStreamGraph.getNode(station.getName()).setAttribute("ui.label", station.getName() + " " + distance);
            }

//            System.out.println("Estación: " + station.getName() + ", Distancia: " + distance);
        }

        System.out.println("Sucursales creadas (BFS): " + branches.toString());
    }

    // Agregar una estación al grafo
//    private void addStationToGraph(String stationName) {
//        if (graphStreamGraph.getNode(stationName) == null) {
//            graphStreamGraph.addNode(stationName).setAttribute("ui.label", stationName);
//        }
//    }
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
                JOptionPane.showMessageDialog(this, "La estación ya existe", 
                        "Aviso", JOptionPane.WARNING_MESSAGE);
            } else {
                stations.add(station);
                JOptionPane.showMessageDialog(this, "Estación añadida exitosamente", 
                        "Aviso", JOptionPane.INFORMATION_MESSAGE);
                addStationToGraph(station);  // Lógica para agregar al grafo
           }
        }
                  
    // Verificación de que la estación no se repite
    public void verificateStations(String station) {
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

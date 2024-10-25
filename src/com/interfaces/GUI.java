package com.interfaces;

import com.graph.AlgorithmSelectionListener;
import com.graph.NetworkTrain;
import com.graph.LinkedList;
import com.graph.Node;
import com.graph.BreadthFirstSearch;
import com.graph.BFSListener;
import com.graph.BranchListener;
import com.graph.Station;
import com.graph.Stack;
import com.graph.Queue;

import com.graph.DepthFirstSearch;
import com.graph.NetworkTrainListener;
import com.graph.StationLoadListener;
import com.graph.TValueListener;

import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

/**
 *
 * @author carlos
 */
public class GUI extends JFrame implements BranchListener, AlgorithmSelectionListener, NetworkTrainListener {

    private NetworkTrain networkTrain;
    private Graph graphStreamGraph;
    private LinkedList<String> stations;
    private LinkedList<Station> branches; // Lista para almacenar sucursales
    private int T; // Para guardar el T que representa distancia entre estaciones
    private LinkedList<StationLoadListener> listeners = new LinkedList(); // Lista para agregar estaciones
    private String selectedAlgorithm;  // Variable para almacenar el algoritmo seleccionado
    private String startStationName;   // Variable para almacenar la estación inicial
    private boolean isNetworkWindowOpen = false; // Controlar si la ventana de red está abierta
    private LinkedList<TValueListener> tValueListeners = new LinkedList<>();
    private LinkedList<BranchListener> bListeners;  // Lista de listeners
    private boolean algorithmSelected; // Variable para mantener el algoritmo seleccionado
    private LinkedList<AlgorithmSelectionListener> aListeners = new LinkedList<>();

    public GUI(NetworkTrain networkTrain) {
        this.networkTrain = networkTrain;
        this.branches = new LinkedList();
        setTitle("Supermarket Location Planner");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        stations = new LinkedList<>();
        bListeners = new LinkedList<>();  // Inicializar la lista de listeners
        networkTrain.addListener(this);
        initUI();
    }
    
    
    @Override
    public void onStationAdded(Station station) {
        System.out.println("Nueva estación agregada desde GUI: " + station.getName());
        // Lógica para actualizar la interfaz gráfica con la nueva estación
        updateGraph();
    }

    @Override
    public void onConnectionAdded(Station station1, Station station2) {
        System.out.println("Nueva conexión agregada desde GUI: " + station1.getName() + " y " + station2.getName());
        // Lógica para actualizar la interfaz gráfica con la nueva conexión
        updateGraph();
    }
    
    

    @Override
    public void onAlgorithmSelected(boolean isBFS) {
        algorithmSelected = isBFS; // Actualiza el estado según la selección
    }

    // Método para añadir un listener
    public void addAlgorithmSelectionListener(AlgorithmSelectionListener aListener) {
        aListeners.add(aListener);
    }

    // Método para eliminar un listener
    public void removeAlgorithmSelectionListener(AlgorithmSelectionListener aListener) {
        aListeners.remove(aListener);
    }

    // Método para notificar a los listeners
    public void notifyAlgorithmSelection(boolean useBFS) {
        for (AlgorithmSelectionListener aListener : aListeners) {
            aListener.onAlgorithmSelected(useBFS);
        }
    }

    public boolean isDFSSelected() {
        return algorithmSelected; // Método para obtener el estado del algoritmo
    }

    @Override
    public void onBranchChanged() {
        // Lógica para actualizar el estado de la GUI cuando cambian las sucursales
        checkTotalCoverage();  // Verifica la cobertura total
        suggestNewBranches(algorithmSelected);  // Sugiere nuevas sucursales
    }

    // Método para añadir una sucursal
    public void addBranch(Station branch) {
        branches.add(branch);
        notifyListeners();  // Notificar a los listeners
    }

    // Método para registrar un listener
    public void addBranchListener(BranchListener bListener) {
        bListeners.add(bListener);
    }

    // Método para notificar a todos los listeners
    private void notifyListeners() {
        for (BranchListener bListener : bListeners) {
            bListener.onBranchChanged();  // Llamar al método de cambio
        }
    }

    public void addTValueListener(TValueListener listener) {
        tValueListeners.add(listener);
    }

    private void notifyTValueChanged(int newT) {
        for (TValueListener listener : tValueListeners) {
            listener.onTValueChanged(newT);
        }
    }

    public void setT(int newT) {
        this.T = newT;  // Actualiza el valor de T
        notifyTValueChanged(newT);
        updateGraph();  // Llama a updateGraph para redibujar el grafo y crear las sucursales
    }

    public void addStationLoadListener(StationLoadListener listener) {
        listeners.add(listener);
    }

    // Método para notificar a los oyentes
    private void notifyStationsLoaded() {
        for (StationLoadListener listener : listeners) {
            listener.onStationsLoaded(branches); // Notifica con la lista de estaciones cargadas
        }
    }

    private void initUI() {
        // Paneles y componentes
        JButton loadButton = new JButton("Cargar Red de Transporte");
        loadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Deshabilitar el botón para evitar múltiples cargas
                loadButton.setEnabled(false);
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

                        loadButton.setEnabled(false); // Deshabilita el botón para evitar múltiples cargas

                        // Mostrar la red en GraphStream
                        showNetworkTrain(jsonObject);

                        // Notificar a los oyentes que las estaciones han sido cargadas
                        notifyStationsLoaded();
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
        if (isNetworkWindowOpen) {
            // Si la ventana ya está abierta, no hacemos nada
            return;
        }
//        System.out.println("showNetworkTrain called");
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

//                T = Integer.parseInt(JOptionPane.showInputDialog(this, "Ingrese la distancia T entre sucursales:"));
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
                isNetworkWindowOpen = true; // Marcar que la ventana está abierta

                this.startStationName = startStationName;  // Guardar el nombre de la estación inicial
                this.selectedAlgorithm = selectedAlgorithm;  // Guardar el algoritmo seleccionado

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

    // Método para verificar la cobertura total
    public boolean checkTotalCoverage() {
        boolean useBFS = algorithmSelected;

        LinkedList<Station> allStations = networkTrain.getStations(); // Obtén todas las estaciones de la red
        LinkedList<Station> coveredStations = new LinkedList<>(); // Lista de estaciones cubiertas

        // Obtener la cobertura usando el algoritmo correspondiente
        for (Station branch : branches) {
            if (useBFS) {
                addCoveredStations(coveredStations, getCoveredStationsBFS(branch));
            } else {
                addCoveredStations(coveredStations, getCoveredStationsDFS(branch));
            }
        }

        // Verificar si todas las estaciones están cubiertas
        return coveredStations.containsAll(allStations); // Devuelve true si todas las estaciones están cubiertas
    }

    // Método auxiliar para agregar estaciones cubiertas sin duplicados
    private void addCoveredStations(LinkedList<Station> coveredStations, LinkedList<Station> newStations) {
        for (int i = 0; i < newStations.size(); i++) {
            Station station = newStations.get(i);
            if (!coveredStations.contains(station)) { // Verifica si la estación ya está cubierta
                coveredStations.add(station); // Agrega la estación solo si no está ya en la lista
            }
        }
    }

    // Método para obtener las estaciones no cubiertas
    public LinkedList<Station> getUncoveredStations(boolean useBFS) {
        LinkedList<Station> allStations = networkTrain.getStations(); // Todas las estaciones de la red
        LinkedList<Station> coveredStations = new LinkedList<>(); // Lista de estaciones cubiertas

        // Obtener la cobertura de las sucursales actuales
        for (Station branch : branches) {
            if (useBFS) {
                coveredStations.addAll(getCoveredStationsBFS(branch));
            } else {
                coveredStations.addAll(getCoveredStationsDFS(branch));
            }
        }

        // Lista para las estaciones no cubiertas
        LinkedList<Station> uncoveredStations = new LinkedList<>();

        if (useBFS) {
            // Obtener la cobertura usando BFS
            for (int i = 0; i < branches.size(); i++) {
                Station branch = branches.get(i);
                coveredStations.addAll(getCoveredStationsBFS(branch));
            }
        } else {
            // Obtener la cobertura usando DFS
            for (int i = 0; i < branches.size(); i++) {
                Station branch = branches.get(i);
                coveredStations.addAll(getCoveredStationsDFS(branch));
            }
        }

        // Agregar estaciones no cubiertas a la lista
        for (Station station : allStations) {
            if (!coveredStations.contains(station)) {
                uncoveredStations.add(station);
            }
        }

        // Encontrar las estaciones que no están en la lista de estaciones cubiertas
        for (int i = 0; i < allStations.size(); i++) {
            Station station = allStations.get(i);
            if (!coveredStations.contains(station)) {
                uncoveredStations.add(station); // Agregar a las no cubiertas
            }
        }

        return uncoveredStations; // Devolver las estaciones no cubiertas
    }

    public String suggestNewBranches(boolean useBFS) {
        int maxDistance = T;
        LinkedList<Station> uncoveredStations = getUncoveredStations(useBFS);
        Map<Station, LinkedList<Station>> coverageMap = new HashMap<>();
        Set<Station> suggestedBranches = new HashSet<>();

        for (Station station : uncoveredStations) {
            LinkedList<Station> coverage;

            // Elegir DFS o BFS
            if (useBFS) {
                coverage = getCoveredStationsBFS(station, maxDistance);
            } else {
                coverage = getCoveredStationsDFS(station, maxDistance);
            }

            coverageMap.put(station, coverage);
        }

        // Verificar cuál estación cubriría más estaciones
        Station bestStation = null;
        int maxCoverage = 0;
        LinkedList<Station> bestCoverageStations = new LinkedList<>();

        for (Station station : coverageMap.keySet()) {
            LinkedList<Station> coverage = coverageMap.get(station);
            int coverageCount = coverage.size();

            if (coverageCount > maxCoverage) {
                maxCoverage = coverageCount;
                bestStation = station;
                bestCoverageStations = coverage;
            }
        }

        // Construir el resultado
        if (bestStation != null) {
            StringBuilder suggestion = new StringBuilder();
            suggestion.append("Sugerencia: Colocar una sucursal en ").append(bestStation.getName())
                    .append(" cubriría ").append(maxCoverage).append(" estaciones no cubiertas.\n");

            suggestion.append("Estaciones cubiertas: ");
            for (Station station : bestCoverageStations) {
                suggestion.append(station.getName()).append(", ");
            }
            suggestion.setLength(suggestion.length() - 2); // Remover la última coma

            return suggestion.toString();
        } else {
            return "No se encontraron estaciones para sugerir.";
        }
    }

    public void cleanBranches() {
        LinkedList<Station> uniqueBranches = new LinkedList<>(); // Crear una nueva lista para sucursales únicas

        for (Station branch : branches) {
            if (!uniqueBranches.contains(branch)) { // Verifica si la sucursal ya está en la lista de únicas
                uniqueBranches.add(branch); // Si no está, añádela
            }
        }

        branches.clear(); // Limpiar la lista original de sucursales
        branches.addAll(uniqueBranches); // Agregar de nuevo solo las sucursales únicas
    }


    // Método para obtener estaciones cubiertas por varias sucursales usando DFS
    public LinkedList<Station> getCoveredStationsDFS(Station start, int maxDistance) {
        LinkedList<Station> coveredStations = new LinkedList<>();
        Set<Station> visited = new HashSet<>();
        runDFS(start, coveredStations, visited, 0, maxDistance);
        return coveredStations;
    }

    // Método auxiliar para ejecutar getCoveredStationsDFS
    private void runDFS(Station current, LinkedList<Station> coveredStations, Set<Station> visited, int depth, int maxDistance) {
        if (depth > maxDistance || visited.contains(current) || branches.contains(current)) {
            return; // Parar si superamos la distancia máxima, ya fue visitada, o es sucursal
        }
        visited.add(current);
        coveredStations.add(current);

        // Recorrer los vecinos
        for (Station neighbor : getNeighbors(current)) {
            if (!visited.contains(neighbor)) {
                runDFS(neighbor, coveredStations, visited, depth + 1, maxDistance);
            }
        }
    }

    // Método para obtener estaciones cubiertas por varias sucursales usando BFS
    public LinkedList<Station> getCoveredStationsBFS(Station start, int maxDistance) {
        LinkedList<Station> coveredStations = new LinkedList<>();
        Set<Station> visited = new HashSet<>();
        Queue<Station> queue = new Queue<>();
        Map<Station, Integer> distances = new HashMap<>();

        queue.enqueue(start);
        distances.put(start, 0);

        while (!queue.isEmpty()) {
            Station current = queue.dequeue();
            int currentDistance = distances.get(current);

            if (currentDistance > maxDistance || branches.contains(current)) {
                continue; // Saltar si estamos fuera del rango o si es una sucursal
            }

            coveredStations.add(current);
            visited.add(current);

            // Recorrer los vecinos
            for (Station neighbor : getNeighbors(current)) {
                if (!visited.contains(neighbor) && !distances.containsKey(neighbor)) {
                    distances.put(neighbor, currentDistance + 1);
                    queue.enqueue(neighbor);
                }
            }
        }

        return coveredStations;
    }

    public void updateGraph() {
        if (graphStreamGraph == null || networkTrain == null) {
            return;  // Si el grafo o la red no están cargados, no hay nada que actualizar
        }

        // Limpiar el grafo actual
        graphStreamGraph.clear(); // Asegúrate de que esto se maneje correctamente.

        // Volver a agregar todas las estaciones y conexiones desde la red de transporte
        LinkedList<Station> allStations = networkTrain.getStations();

        // Añadir todas las estaciones al grafo
        for (Station station : allStations) {
            addStationToGraph(station.getName());  // Añadir la estación al grafo
        }

        // Añadir las conexiones entre las estaciones
        for (Station station : allStations) {
            LinkedList<Station> neighbors = networkTrain.getNeighbors(station);
            for (Station neighbor : neighbors) {
                addEdgeIfNotExists(station.getName(), neighbor.getName());  // Añadir las conexiones si no existen
            }
        }

        // Ejecutar el algoritmo seleccionado de nuevo para recalcular las sucursales
        Station startStation = networkTrain.getStationByName(startStationName);   // Obtiene la estación inicial
        if (startStation != null) {
            if ("BFS".equals(selectedAlgorithm)) {
                runBFS(startStation);
            } else if ("DFS".equals(selectedAlgorithm)) {
                runDFS(startStation);
            }
        }

        // Colorear las sucursales en verde para diferenciarlas
        for (Station branch : branches) {
            if (graphStreamGraph.getNode(branch.getName()) != null) {
                graphStreamGraph.getNode(branch.getName()).setAttribute("ui.style", "fill-color: green;");
                graphStreamGraph.getNode(branch.getName()).setAttribute("ui.label", branch.getName());  // Mostrar el nombre de la estación
            }
        }

        // Aquí no llamamos a display() nuevamente
        // graphStreamGraph.display();  // Esto es innecesario
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

    // Verificar si networkTrain ha sido cargado para validaciones de otros botones en la WelcomeInterface   
    public boolean isNetworkLoaded() {
        return networkTrain != null;
    }

    // Devuelve una lista de las sucursales creadas    
    public LinkedList<Station> getBranches() {
        // Supongamos que tienes una lista de sucursales en tu clase GUI
        return this.branches; // Devuelve la lista de sucursales como una lista de Strings
    }

    public NetworkTrain getNetworkTrain() {
        return this.networkTrain;  // Donde networkTrain es la instancia de tu grafo
    }

    public void removeBranch(Station branchName) {
        branches.remove(branchName);  // Remueve la sucursal de la lista
        notifyListeners();  // Notificar a los listeners

        // Remueve la visualización de la sucursal en el grafo
        if (graphStreamGraph.getNode(branchName.getName()) != null) {
            graphStreamGraph.getNode(branchName.getName()).setAttribute("ui.style", "fill-color: grey;"); // Cambia el color para mostrar que ya no es una sucursal
        }

        updateGraph2();
    }

    // Método para obtener las estaciones cubiertas usando DFS
    public LinkedList<Station> getCoveredStationsDFS(Station startStation) {
        LinkedList<Station> coveredStations = new LinkedList<>();
        // Llamar al método DFS existente
        runDFS(startStation, coveredStations, 0, T);
        return coveredStations; // Devuelve la lista de estaciones cubiertas
    }

    // Método para obtener las estaciones cubiertas usando BFS
    public LinkedList<Station> getCoveredStationsBFS(Station startStation) {
        LinkedList<Station> coveredStations = new LinkedList<>();
        // Llamar al método BFS existente
        runBFS(startStation, coveredStations, T);
        return coveredStations; // Devuelve la lista de estaciones cubiertas
    }

    // Sobrecarga de metodo para usar el dfs y poder saber la cobertura de sucursal 
    private void runDFS(Station current, LinkedList<Station> coveredStations, int depth, int maxDistance) {
        if (depth > maxDistance) {
            return; // No continuar si se ha superado el límite
        }
        coveredStations.add(current);
        for (Station neighbor : getNeighbors(current)) {
            if (!coveredStations.contains(neighbor)) {
                runDFS(neighbor, coveredStations, depth + 1, maxDistance);
            }
        }
    }

    // Sobrecarga de metodo para usar el bfs y poder saber la cobertura de sucursal 
    private void runBFS(Station start, LinkedList<Station> coveredStations, int maxDistance) {
        Queue<Station> queue = new Queue<>();
        Map<Station, Integer> distances = new HashMap<>();
        queue.enqueue(start);
        distances.put(start, 0);

        while (!queue.isEmpty()) {
            Station current = queue.dequeue();
            int currentDistance = distances.get(current);

            if (currentDistance <= maxDistance) {
                coveredStations.add(current);
                for (Station neighbor : getNeighbors(current)) {
                    if (!distances.containsKey(neighbor)) {
                        distances.put(neighbor, currentDistance + 1);
                        queue.enqueue(neighbor);
                    }
                }
            }
        }
    }

    // Método para obtener los vecinos de una estación
    public LinkedList<Station> getNeighbors(Station station) {
        return networkTrain.getNeighbors(station);
    }

    private void runDFS(Station startStation) {
        int T = this.T;
        // Inicializamos la primera sucursal
        branches.clear();
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

        // Mostrar todas las distancias y los nombres de las estaciones
        System.out.println("Distancias desde la estación inicial:");
        for (Map.Entry<Station, Integer> entry : distances.entrySet()) {
            Station station = entry.getKey();
            Integer distance = entry.getValue();
            System.out.println("Estación: " + station.getName() + ", Distancia: " + distance);
        }

        // Mostrar las sucursales creadas
//        System.out.println("Sucursales creadas (DFS): " + branches.toString());
    }

    private void runBFS(Station startStation) {
        int T = this.T;
        // Inicializar la distancia de la estación inicial
        Queue<Station> queue = new Queue<>();
        branches.clear();
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

                    // Verificar si la distancia desde la sucursal inicial es divisible entre T
                    if (distances.get(neighbor) % T == 0) {
                        // Verificar si la nueva estación genera un conflicto
                        boolean conflict = checkForConflict(neighbor, T);

                        if (conflict) {
                            // Si hay conflicto, no se agrega la sucursal y se muestra el mensaje
                            System.out.println("Conflicto encontrado: no se puede agregar la sucursal " + neighbor.getName());
                        } else {
                            // Agregar la sucursal si no hay conflictos
                            branches.add(neighbor);

                            // Colorear la nueva sucursal en verde
                            if (graphStreamGraph.getNode(neighbor.getName()) != null) {
                                graphStreamGraph.getNode(neighbor.getName()).setAttribute("ui.style", "fill-color: green;");
                            }
//                            System.out.println("Sucursal agregada: " + neighbor.getName() + " (Distancia desde inicio: "
//                                    + distances.get(neighbor) + ")");
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
        }

        System.out.println("Sucursales creadas (BFS): " + branches.toString());
    }

    /**
     * Método auxiliar que verifica si una sucursal entra en conflicto con otra
     * ya existente
     *
     * @param newBranch la estación que se quiere convertir en sucursal
     * @param T la distancia máxima permitida entre sucursales
     * @return true si hay conflicto, false si no lo hay
     */
    private boolean checkForConflict(Station newBranch, int T) {
        Queue<Station> queue = new Queue<>();
        Set<Station> visitedStations = new HashSet<>();
        Map<Station, Integer> distances = new HashMap<>();
        distances.put(newBranch, 0);

        queue.enqueue(newBranch);
        visitedStations.add(newBranch);

        while (!queue.isEmpty()) {
            Station current = queue.dequeue();
            LinkedList<Station> neighbors = networkTrain.getNeighbors(current); // Obtener vecinos

            for (Station neighbor : neighbors) {
                if (!visitedStations.contains(neighbor)) {
                    visitedStations.add(neighbor);
                    distances.put(neighbor, distances.get(current) + 1);

                    // Verificar si estamos a una distancia mayor que T
                    if (distances.get(neighbor) >= T) {
                        return false; // No hay conflicto
                    }

                    // Verificar si el vecino es una sucursal existente
                    if (branches.contains(neighbor)) {
//                        System.out.println("Conflicto encontrado: no se puede agregar la sucursal " + newBranch.getName());
//                        System.out.println("Suc. Conflictiva: " + neighbor.getName() + " (Distancia desde "
//                                + newBranch.getName() + ": " + distances.get(neighbor) + ")");
                        return true; // Hay conflicto
                    }

                    queue.enqueue(neighbor);
                }
            }
        }

        return false; // No hay conflicto
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
    public void verificateStations(String station) {
        if (stations.contains(station)) {
            JOptionPane.showMessageDialog(this, "La estación ya existe", "Aviso", JOptionPane.WARNING_MESSAGE);
        } else {
            stations.add(station);
            JOptionPane.showMessageDialog(this, "Estación añadida exitosamente", "Aviso", JOptionPane.INFORMATION_MESSAGE);
            addStationToGraph(station);  // Lógica para agregar al grafo
        }
    }

    // Método para obtener el valor de T
    public int getT() {
        return this.T;
    }

    public void updateGraph2() {
        if (graphStreamGraph == null || networkTrain == null) {
            return;  // Si el grafo o la red no están cargados, no hay nada que actualizar
        }

        // Obtén todas las estaciones de la red
        LinkedList<Station> allStations = networkTrain.getStations();

        // Recorrer todas las estaciones de la red
        for (Station station : allStations) {
            // Verifica si la estación ya está en el grafo antes de agregarla
            if (graphStreamGraph.getNode(station.getName()) == null) {
                addStationToGraph(station.getName());  // Añadir la estación al grafo si no está
            }

            // Obtener los vecinos (conexiones) de la estación actual
            LinkedList<Station> neighbors = networkTrain.getNeighbors(station);

            // Recorrer todos los vecinos (conexiones) de la estación actual
            for (Station neighbor : neighbors) {
                // Verificar si la conexión ya existe antes de añadirla
                if (graphStreamGraph.getEdge(station.getName() + "-" + neighbor.getName()) == null
                        && graphStreamGraph.getEdge(neighbor.getName() + "-" + station.getName()) == null) {
                    addEdgeIfNotExists(station.getName(), neighbor.getName());  // Añadir la conexión si no existe
                }
            }
        }

        // Actualizar las estaciones que son sucursales
        for (Station branch : branches) {
            if (graphStreamGraph.getNode(branch.getName()) != null) {
                // Colorear en verde las estaciones que son sucursales
                graphStreamGraph.getNode(branch.getName()).setAttribute("ui.style", "fill-color: green;");
                graphStreamGraph.getNode(branch.getName()).setAttribute("ui.label", branch.getName());  // Mostrar el nombre de la estación
            }
        }

        // Si quieres actualizar la interfaz gráfica, puedes seguir mostrando el grafo
//        graphStreamGraph.display();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                // Crear una instancia de NetworkTrain
                NetworkTrain networkTrain = new NetworkTrain();
                new GUI(networkTrain).setVisible(true);
            }
        });
    }
}

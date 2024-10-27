package com.interfaces;

import com.graph.AlgorithmSelectionListener;
import com.graph.NetworkTrain;
import com.graph.LinkedList;
import com.graph.BranchListener;
import com.graph.MapsList;
import com.graph.Station;
import com.graph.Stack;
import com.graph.Queue;

import com.graph.NetworkTrainListener;
import com.graph.SetList;
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
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

/**
 *
 * @author carlos
 * 
 */
public class GUI extends JFrame implements BranchListener, AlgorithmSelectionListener, NetworkTrainListener {

    private NetworkTrain networkTrain;        // Clase que representa las estaciones de la red de estaciones
    private Graph graphStreamGraph;           // Grafo visual para mostrar la red de estaciones
    private LinkedList<String> stations;      // Lista de nombres de las estaciones
    private LinkedList<Station> branches;     // Lista para almacenar sucursales
    private int T;                            // Para guardar el T que representa distancia entre estaciones
    private LinkedList<StationLoadListener> listeners = new LinkedList(); // Lista para agregar estaciones
    private String selectedAlgorithm;         // Variable para almacenar el algoritmo seleccionado
    private String startStationName;          // Variable para almacenar la estación inicial
    private boolean isNetworkWindowOpen = false; // Controlar si la ventana de red está abierta
    private LinkedList<TValueListener> tValueListeners = new LinkedList<>();  //Lista de listeners para cambios en el valor de T
    private LinkedList<BranchListener> bListeners;  // Lista de listeners
    private boolean algorithmSelected;        // Variable para mantener el algoritmo seleccionado
    private LinkedList<AlgorithmSelectionListener> aListeners = new LinkedList<>(); //Lista de listeners para la selección de algoritmos
    private SetList<Station> eliminatedStations = new SetList<>(); //Lista de estaciones eliminadas
    private MapsList<String, Station> stationMap; //Mapa(lista) de nombres de estaciones a objetos de estación

    
/**
 * Constructor de GUI
 * @param networkTrain 
 */
    public GUI(NetworkTrain networkTrain) {
        this.networkTrain = networkTrain;
        this.branches = new LinkedList();
        setTitle("Supermarket Location Planner");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        stations = new LinkedList<>();
        bListeners = new LinkedList<>();  // Inicializar la lista de listeners
        networkTrain.addListener(this);
        stationMap = new MapsList<>();
        initUI();
    }
    
    /**
     * Se llama cuando se añade una nueva estación desde la interfaz gráfica.
     * @param station La estación que ha sido añadida.
     */
    @Override
    public void onStationAdded(Station station) {
        System.out.println("Nueva estación agregada desde GUI: " + station.getName());
        // Lógica para actualizar la interfaz gráfica con la nueva estación
        updateGraph();
    }

    /**
     * Se llama cuando se añade una nueva conexión entre dos estaciones desde la interfaz gráfica.
     * @param station1 La primera estación de la conexión.
     * @param station2 La segunda estación de la conexión.
     */
    @Override
    public void onConnectionAdded(Station station1, Station station2) {
        System.out.println("Nueva conexión agregada desde GUI: " + station1.getName() + " y " + station2.getName());
        // Lógica para actualizar la interfaz gráfica con la nueva conexión
        updateGraph();
    }
    
    /**
     * Se llama cuando se selecciona un algoritmo (BFS o DFS) en la interfaz gráfica.
     * @param isBFS {@code true} Si se seleccionó BFS.
     *              {@code false} Si se seleccionó DFS.
     */
    @Override
    public void onAlgorithmSelected(boolean isBFS) {
        algorithmSelected = isBFS; // Actualiza el estado según la selección
    }

    /**
     * Añade un listener para la selección del algoritmo.
     * @param aListener El listener a añadir.
     */
    // Método para añadir un listener
    public void addAlgorithmSelectionListener(AlgorithmSelectionListener aListener) {
        aListeners.add(aListener);
    }

    /**
     * Elimina un listener de la selección del algoritmo.
     * @param aListener El listener a eliminar.
     */
    // Método para eliminar un listener
    public void removeAlgorithmSelectionListener(AlgorithmSelectionListener aListener) {
        aListeners.remove(aListener);
    }

    /**
     * Notifica a todos los listeners sobre la selección del algoritmo.
     * @param useBFS {@code true} Si se está utilizando BFS.
     *               {@code false} Si se está utilizando DFS.
     */
    // Método para notificar a los listeners
    public void notifyAlgorithmSelection(boolean useBFS) {
        for (AlgorithmSelectionListener aListener : aListeners) {
            aListener.onAlgorithmSelected(useBFS);
        }
    }

    /**
     * Devuelve el estado de si se ha seleccionado DFS.
     * @return {@code true} Si se está utilizando DFS.
     *         {@code false} Si se está utilizando BFS.
     */
    public boolean isDFSSelected() {
        return algorithmSelected; // Método para obtener el estado del algoritmo
    }

    /**
     * Se llama cuando cambian las sucursales.
     * Actualiza la GUI y verifica la cobertura total de las estaciones.
     */
    @Override
    public void onBranchChanged() {
        // Lógica para actualizar el estado de la GUI cuando cambian las sucursales
        checkTotalCoverage();  // Verifica la cobertura total
        suggestNewBranches(algorithmSelected);  // Sugiere nuevas sucursales
    }

    /**
     * Añade una nueva sucursal a la lista de sucursales.
     * @param branch La estación que se añadirá como sucursal.
     */
    // Método para añadir una sucursal
    public void addBranch(Station branch) {
        branches.add(branch);
        notifyListeners();  // Notificar a los listeners
    }

    /**
     * Añade un listener para los cambios en las sucursales.
     * @param bListener El listener a añadir.
     */
    // Método para registrar un listener
    public void addBranchListener(BranchListener bListener) {
        bListeners.add(bListener);
    }

    /**
     * Notifica a todos los listeners sobre cambios en las sucursales.
     */
    // Método para notificar a todos los listeners
    private void notifyListeners() {
        for (BranchListener bListener : bListeners) {
            bListener.onBranchChanged();  // Llamar al método de cambio
        }
    }

    /**
     * Añade un listener para cambios en el valor de T.
     * @param listener El listener a añadir.
     */
    public void addTValueListener(TValueListener listener) {
        tValueListeners.add(listener);
    }

    /**
     * Notifica a todos los listeners sobre cambios en el valor de T.
     * @param newT El nuevo valor de T. 
     */
    private void notifyTValueChanged(int newT) {
        for (TValueListener listener : tValueListeners) {
            listener.onTValueChanged(newT);
        }
    }
    
    /**
     * Actualiza el valor de T y notifica a los listeners. 
     * @param newT El nuevo valor de T.
     */
    public void setT(int newT) {
        this.T = newT;  // Actualiza el valor de T
        notifyTValueChanged(newT);
        updateGraph();  // Llama a updateGraph para redibujar el grafo y crear las sucursales
    }

    /**
     * Añade un listener para la carga de estaciones.
     * @param listener El listener a añadir.
     */
    public void addStationLoadListener(StationLoadListener listener) {
        listeners.add(listener);
    }

    /**
     * Notifica a todos los listeners que las estaciones han sido cargadas.
     */
    // Método para notificar a los oyentes
    private void notifyStationsLoaded() {
        for (StationLoadListener listener : listeners) {
            listener.onStationsLoaded(branches); // Notifica con la lista de estaciones cargadas
        }
    }
    
    /**
     * Añade una estación a la lista de estaciones eliminadas.
     * @param station La estación a eliminar.
     */
    public void addEliminatedStations(Station station){
        eliminatedStations.add(station);
    }
    
    /**
     * Verifica si una estación ha sido eliminada.
     * @param station La estación a verificar.
     * @return {@code true} Si la estación ha sido eliminada.
     *         {@code false} Si la estación no ha sido eliminada.
     */
    public boolean stationEliminated(Station station){
        return eliminatedStations.contains(station);
    }

    /**
     * Inicializa la interfaz de usuario para la aplicación.
     * Crea y configura los paneles y componentes necesarios,
     * incluyendo el botón para cargar la red de transporte.
     * El botón de carga permite al usuario seleccionar un archivo JSON
     * que contiene la información de la NetworkTrain. Al cargar
     * el archivo, se deshabilita el botón para evitar múltiples cargas
     * simultáneas. Si se produce un error durante la carga del archivo,
     * se muestra un mensaje de error.
     */
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
                        
                        // Convierte todas las claves y valores de texto a minúsculas
                        JSONObject lowerCaseJson = toLowerCaseJson(jsonObject);
                        
                        // Cargar los datos desde el objeto JSONObject
                        networkTrain = new NetworkTrain();
                        networkTrain.loadFromJson(lowerCaseJson); 
                        branches = new LinkedList<>();

                        loadButton.setEnabled(false); // Deshabilita el botón para evitar múltiples cargas

                        // Mostrar la red en GraphStream
                        showNetworkTrain(lowerCaseJson); 

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
    
    /**
     * Convierte todas las claves y valores de texto a minúsculas en un objeto JSON.
     * Este método recorre todas las claves del objeto JSON proporcionado y transforma
     * las claves y los valores de tipo String a minúsculas. Si un valor es un objeto JSON
     * o un arreglo JSON, también se procesará recursivamente.
     * @param jsonObject El objeto JSON que se desea convertir.
     * @return Un nuevo objeto JSON con todas las claves y valores de texto en minúsculas.
     */
    // Convierte todas las claves y valores de texto a minúsculas en un JSONObject
    private JSONObject toLowerCaseJson(JSONObject jsonObject) {
        JSONObject result = new JSONObject();
        
        for (String key : jsonObject.keySet()) {
            Object value = jsonObject.get(key);

            String lowerCaseKey = key.toLowerCase();

            if (value instanceof JSONObject) {
                result.put(lowerCaseKey, toLowerCaseJson((JSONObject) value));
            } else if (value instanceof JSONArray) {
                result.put(lowerCaseKey, toLowerCaseJsonArray((JSONArray) value));
            } else if (value instanceof String) {
                result.put(lowerCaseKey, ((String) value).toLowerCase());
            } else {
                result.put(lowerCaseKey, value);
            }
        }

        return result;
    }
    
    /**
     * Convierte todos los elementos de texto a minúsculas en un arreglo JSON.
     * Este método recorre cada elemento del arreglo JSON proporcionado y transforma
     * los valores de tipo String a minúsculas. Si un elemento es un objeto JSON,
     * se procesará utilizando el método toLowerCaseJson.
     * @param array  El arreglo JSON que se desea convertir.
     * @return Un nuevo arreglo JSON con todos los elementos de texto en minúsculas.
     */
    // Convierte todos los elementos de texto a minúsculas en un JSONArray
    private JSONArray toLowerCaseJsonArray(JSONArray array) {
        JSONArray result = new JSONArray();
        for (int i = 0; i < array.length(); i++) {
            Object value = array.get(i);
            if (value instanceof JSONObject) {
                result.put(toLowerCaseJson((JSONObject) value));
            } else if (value instanceof String) {
                result.put(((String) value).toLowerCase());
            } else {
                result.put(value);
            }
        }
        return result;
    }

    /**
     * Muestra la red de trenes en una ventana utilizando GraphStream.
     * Este método permite al usuario ingresar el nombre de una estación inicial y seleccionar un 
     * algoritmo (BFS o DFS) para visualizar la red de trenes. Si la ventana de visualización ya 
     * está abierta, no se realiza ninguna acción. 
     * El objeto JSON contendrá la estructura de la red, incluyendo las estaciones y sus conexiones. 
     * Este método agrega nodos y aristas al grafo y finalmente muestra la red.
     * @param jsonObject El objeto JSON que representa la red de trenes, que contiene información 
     *                   sobre las estaciones y las conexiones entre ellas.
     */
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
            while (true) {
            // Muestra un cuadro de diálogo para ingresar la estación inicial
                String startStationName = JOptionPane.showInputDialog(this, "Ingrese el nombre de la estación de inicio:");

                if (startStationName != null && !startStationName.trim().isEmpty() 
                        && startStationName.toLowerCase().matches("^[a-z0-9\\s]+$")) {
                    //Matches: Regex que en este caso valida letras,numeros y espacios en blanco
                        this.startStationName = startStationName.toLowerCase(); // Guarda el nombre 
                        break;
                } else {
                    JOptionPane.showMessageDialog(this, "Debe ingresar un nombre de estación válido (solo letras y números).");
                }
            } 
                    
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
                this.selectedAlgorithm = selectedAlgorithm;
               
                
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
                Station startStation = networkTrain.getStationByName(this.startStationName);
                if (startStation != null) {
                    if ("BFS".equals(this.selectedAlgorithm)) {
                        runBFS(startStation);
                    } else if ("DFS".equals(this.selectedAlgorithm)) {
                        runDFS(startStation);
                      }
                } else {
                    JOptionPane.showMessageDialog(this, "Estación no encontrada: " + startStationName);
                }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**}
     * Verifica si todas las estaciones de la red están cubiertas por las sucursales existentes.
     * Utiliza el algoritmo de búsqueda seleccionado (BFS o DFS) para determinar la cobertura total
     * de las estaciones a partir de las sucursales.
     * @return {@code true}si todas las estaciones están cubiertas.
     *         {@code false} Si todas las estaciones no est[an cubiertas.
     */
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

    /**
     * Agrega estaciones cubiertas a la lista sin duplicados.
     * Este método toma una lista de estaciones cubiertas existentes y una nueva 
     * lista de estaciones cubiertas, asegurando que no se agreguen duplicados.
     * @param coveredStations Lista de estaciones cubiertas donde se agregarán nuevas estaciones.
     * @param newStations Lista de estaciones nuevas que se desean agregar a la lista de cubiertas.
     */
    // Método auxiliar para agregar estaciones cubiertas sin duplicados
    private void addCoveredStations(LinkedList<Station> coveredStations, LinkedList<Station> newStations) {
        for (int i = 0; i < newStations.size(); i++) {
            Station station = newStations.get(i);
            if (!coveredStations.contains(station)) { // Verifica si la estación ya está cubierta
                coveredStations.add(station); // Agrega la estación solo si no está ya en la lista
            }
        }
    }

    /**
     * Obtiene una lista de estaciones que no están cubiertas por las sucursales existentes.
     * Utiliza el algoritmo de búsqueda seleccionado (BFS o DFS) para calcular 
     * las estaciones cubiertas y luego determina cuáles no están cubiertas.
     * @param useBFS Indica si se debe usar la búsqueda en amplitud (BFS) o búsqueda en profundidad (DFS) 
     *               para calcular las estaciones cubiertas.
     * @return Una lista de estaciones que no están cubiertas por las sucursales.
     */
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

    /**
     * Sugiere una nueva sucursal para cubrir la mayor cantidad de estaciones no cubiertas.
     * Este método evalúa cada estación no cubierta y determina cuál, al ser convertida en sucursal,
     * permitiría cubrir la mayor cantidad de estaciones utilizando el algoritmo especificado (BFS o DFS).
     * @param useBFS Indica si se debe usar la búsqueda en amplitud (BFS) o búsqueda en profundidad (DFS) 
     *               para calcular las estaciones cubiertas.
     * @return Una sugerencia de sucursal que cubre el mayor número de estaciones no cubiertas, junto con
     *         la lista de estaciones cubiertas, o un mensaje indicando que no se encontraron estaciones
     *         para sugerir.
     */
    public String suggestNewBranches(boolean useBFS) {
        int maxDistance = T;
        LinkedList<Station> uncoveredStations = getUncoveredStations(useBFS);
        //Map<Station, LinkedList<Station>> coverageMap = new HashMap<>();
        MapsList<Station, LinkedList<Station>> coverageMap = new MapsList<>();
        SetList<Station> suggestedBranches = new SetList<>();

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

    /**
     * Limpia la lista de sucursales, eliminando duplicados.
     * Este método recorre la lista de sucursales actuales y asegura que cada sucursal sea única,
     * eliminando cualquier duplicado antes de actualizar la lista original de sucursales.
     */
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


    /**
     * Obtiene las estaciones cubiertas por varias sucursales utilizando un algoritmo DFS.
     * Este método inicia la búsqueda desde la estación de inicio proporcionada y 
     * registra las estaciones alcanzadas hasta una distancia máxima especificada. 
     * @param start Estación desde la cual se comienza la búsqueda.
     * @param maxDistance Distancia máxima a la que se buscarán estaciones cubiertas.
     * @return Lista de estaciones cubiertas por varias sucursales dentro de la distancia máxima.
     */
    // Método para obtener estaciones cubiertas por varias sucursales usando DFS
    public LinkedList<Station> getCoveredStationsDFS(Station start, int maxDistance) {
        LinkedList<Station> coveredStations = new LinkedList<>();
        SetList<Station> visited = new SetList<>();
        runDFS(start, coveredStations, visited, 0, maxDistance);
        return coveredStations;
    }

    /**
     * Método auxiliar para ejecutar la búsqueda en profundidad (DFS) en la red de trenes.
     * Este método es llamado recursivamente para explorar las estaciones vecinas hasta 
     * alcanzar la distancia máxima o si se encuentran estaciones que ya han sido visitadas o que son sucursales.
     * @param current Estación actualmente en evaluación.
     * @param coveredStations  Lista que almacena las estaciones cubiertas hasta el momento.
     * @param visited Conjunto de estaciones que ya han sido visitadas durante la búsqueda.
     * @param depth Profundidad actual en la búsqueda.
     * @param maxDistance Distancia máxima permitida para la búsqueda.
     */
    // Método auxiliar para ejecutar getCoveredStationsDFS
    private void runDFS(Station current, LinkedList<Station> coveredStations, SetList<Station> visited, int depth, int maxDistance) {
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

    /**
     * Obtiene las estaciones cubiertas por varias sucursales utilizando un algoritmo 
     * de búsqueda en amplitud (BFS). Este método inicia la búsqueda desde la estación de 
     * inicio proporcionada y registra las estaciones alcanzadas hasta una distancia máxima especificada.
     * @param start Estación desde la cual se comienza la búsqueda.
     * @param maxDistance Distancia máxima a la que se buscarán estaciones cubiertas.
     * @return Lista de estaciones cubiertas por varias sucursales dentro de la distancia máxima.
     */
    // Método para obtener estaciones cubiertas por varias sucursales usando BFS
    public LinkedList<Station> getCoveredStationsBFS(Station start, int maxDistance) {
        LinkedList<Station> coveredStations = new LinkedList<>();
        SetList<Station> visited = new SetList<>();
        Queue<Station> queue = new Queue<>();
        //Map<Station, Integer> distances = new HashMap<>();
        MapsList<Station, Integer> distances = new MapsList<>();
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

    /**
     * Actualiza el grafo visual para reflejar el estado actual de la NetworkTrain.
     *  Si la instancia del grafo o la red de trenes no están cargadas, el método termina sin realizar cambios.
     * Primero limpia el grafo, luego añade todas las estaciones y sus conexiones basadas en la red de trenes
     * y finalmente recalcula y colorea las sucursales basadas en el algoritmo seleccionado.
     */
    public void updateGraph() {
        if (graphStreamGraph == null || networkTrain == null) {
            return;  // Si el grafo o la red no están cargados, no hay nada que actualizar
        }

        // Limpiar el grafo actual
        graphStreamGraph.clear(); 
        //Validación por si el grafo no está limpio.
        if(graphStreamGraph.getNodeCount() != 0 || graphStreamGraph.getEdgeCount()!= 0){
            JOptionPane.showMessageDialog(this, "El grafo no se ha limpiado correctamente", "Aviso", JOptionPane.WARNING_MESSAGE);
            graphStreamGraph.clear();
        }

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

    /**
     * Calcula la distancia mínima en estaciones entre dos estaciones dadas.
     * Utiliza BFS para encontrar el camino más corto entre las estaciones.
     * @param from La estación de inicio desde la cual calcular la distancia.
     * @param to La estación de destino hasta la cual calcular la distancia.
     * @return La distancia en número de estaciones entre las dos estaciones. 
     *         Retorna 0 si ambas estaciones son iguales, y -1 si no hay camino entre ellas.
     */
    private int calculateDistance(Station from, Station to) {
        if (from.equals(to)) {
            return 0; // La distancia a sí misma es 0
        }

        // Mapa para rastrear las distancias desde la estación 'from'
        //Map<Station, Integer> distances = new HashMap<>();
        MapsList<Station, Integer> distances = new MapsList<>();
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

    /**
     * Verifica si la NetworkTrain ha sido agregada, lo cual ayuda a habilitar o deshabilitar otras funciones.
     * @return {@code true} Si la instancia de Networktrain no es nula, está cargada.
     *         {@code false} Si la instancia de Networktrain es nula, no está cargada.
     */  
    public boolean isNetworkLoaded() {
        return networkTrain != null;
    }

    /**
     * Obtiene una lista de las sucursales creadas.
     * @return Lista enlazada con todas las estaciones marcadas como sucursales.
     */
    public LinkedList<Station> getBranches() {
        // Supongamos que tienes una lista de sucursales en tu clase GUI
        return this.branches; // Devuelve la lista de sucursales como una lista de Strings
    }

    /**
     * Obtiene la instancia de la NetworkTrain asociada a GUI.
     * @return La instancia actual de {@link NetworkTrain}, que representa el 
     *         grafo de la red de estaciones.
     */
    public NetworkTrain getNetworkTrain() {
        return this.networkTrain;  // Donde networkTrain es la instancia de tu grafo
    }

    /**
     * Elimina una sucursal específica de la lista de sucursales y actualiza 
     * su visualización en el grafo.
     * @param branchName La sucursal que se desea eliminar de la lista de sucursales.
     */
    public void removeBranch(Station branchName) {
        branches.remove(branchName);  // Remueve la sucursal de la lista
        notifyListeners();  // Notificar a los listeners

        // Remueve la visualización de la sucursal en el grafo
        if (graphStreamGraph.getNode(branchName.getName()) != null) {
            graphStreamGraph.getNode(branchName.getName()).setAttribute("ui.style", "fill-color: grey;"); // Cambia el color para mostrar que ya no es una sucursal
        }

        updateGraph2();
    }

    /**
     * Obtiene las estaciones que están dentro de la cobertura desde una estación 
     * de inicio utilizando un algoritmo de DFS.
     * @param startStation Estación de inicio desde la cual se comienza la búsqueda de estaciones cubiertas.
     * @return Lista enlazada de estaciones que están dentro de la cobertura 
     *         de acuerdo con la distancia máxima especificada por el valor de T.
     */
    public LinkedList<Station> getCoveredStationsDFS(Station startStation) {
        LinkedList<Station> coveredStations = new LinkedList<>();
        // Llamar al método DFS existente
        runDFS(startStation, coveredStations, 0, T);
        return coveredStations; // Devuelve la lista de estaciones cubiertas
    }

    /**
     * Obtiene las estaciones que están dentro de la cobertura desde una 
     * estación inicial utilizando un algoritmo de BFS
     * @param startStation La estación de inicio desde la cual se comienza 
     *                     la búsqueda de estaciones cubiertas.
     * @return Lista enlazada de estaciones que están dentro de la cobertura 
     *         de acuerdo con la distancia máxima especificada por el valor de T.
     */
    public LinkedList<Station> getCoveredStationsBFS(Station startStation) {
        LinkedList<Station> coveredStations = new LinkedList<>();
        // Llamar al método BFS existente
        runBFS(startStation, coveredStations, T);
        return coveredStations; // Devuelve la lista de estaciones cubiertas
    }

    /** 
     * Realiza una búsqueda en profundidad DFS a partir de una estación actual
     * para identificar estaciones dentro de una distancia máxima desde la 
     * estación de inicio. Las estaciones cubiertas se agregan a la lista proporcionada.
     * @param current         La estación actual desde la cual se realiza la búsqueda DFS.
     * @param coveredStations Lista enlazada con las estaciones dentro 
     *                        del rango de cobertura agregadas durante el recorrido.
     * @param depth           Distancia actual desde la estación de inicio, el valor 
     *                        se incrementa con cada llamada recursiva.
     * @param maxDistance     Distancia máxima permitida desde la estación de 
     *                        inicio para considerar una estación como cubierta. 
     */
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

    /**
     * Realiza una búsqueda en achura BFS desde una estación de inicio para
     * encontrar y marcar estaciones dentro de una distancia máxima. Las estaciones
     * cubiertas se agregan a la lista proporcionada.
     * @param start Estación de inicio desde la cual se inicia el recorrido BFS
     * @param coveredStations Lista enlazada donde se agregarán las estaciones dentro del rango de cobertura.
     * @param maxDistance Distancia máxima permitida desde la estación de inicio para considerar una estación como cubierta.
     */
    private void runBFS(Station start, LinkedList<Station> coveredStations, int maxDistance) {
        Queue<Station> queue = new Queue<>(); //Cola para realizar la busqueda de anchura.
        //Map<Station, Integer> distances = new HashMap<>();
        MapsList<Station, Integer> distances = new MapsList<>(); //Mapa de distancias desde la estación de inicio.
        queue.enqueue(start);
        distances.put(start, 0); //Distancia de inicio en 0

        while (!queue.isEmpty()) {
            Station current = queue.dequeue();
            int currentDistance = distances.get(current);

            // Si la distancia actual está dentro del máximo, la estación se considera cubierta.
            if (currentDistance <= maxDistance) {
                coveredStations.add(current);
                // Recorre los vecinos de la estación actual.
                for (Station neighbor : getNeighbors(current)) {
                    // Si el vecino aún no tiene una distancia registrada, se establece y se agrega a la cola.
                    if (!distances.containsKey(neighbor)) {
                        distances.put(neighbor, currentDistance + 1);
                        queue.enqueue(neighbor);
                    }
                }
            }
        }
    }

    /**
     * Obtiene los vecinos de una estación específico (estaciones conectadas).
     * Delega la llamada al método en networktrain {@link NetworkTrain#getNeighbors(Station)}.
     * @param station Estación para la cual se desean obtener los vecinos.
     * @return Lista enlazada de estaciones vecinas conectadas.
     */
    public LinkedList<Station> getNeighbors(Station station) {
        return networkTrain.getNeighbors(station);
    }

    /**
     * Realiza un recorrido en profundidad DFS a partir de una estación inicial.
     * Inicializa las estructuras necesarias, colorea la estación inicial en verde y 
     * recorre las estaciones adyacentes agregándolas a la pila si no han sido visitadas.
     * Verifica y maneja conflictor basador en la distancia de la estación inicial.
     * @param startStation Estación de inicio para el recorrido en profundidad.
     */
    private void runDFS(Station startStation) {
        int T = this.T;
        // Inicializamos la primera sucursal
        branches.clear();
        branches.add(startStation);
        //Map<Station, Integer> distances = new HashMap<>();
        MapsList<Station, Integer> distances = new MapsList<>();
        distances.put(startStation, 0); // La distancia de la estación inicial es 0

        // Colorear la primera sucursal en verde
        if (graphStreamGraph.getNode(startStation.getName()) != null) {
            graphStreamGraph.getNode(startStation.getName()).setAttribute("ui.style", "fill-color: green;");
            graphStreamGraph.getNode(startStation.getName()).setAttribute("ui.label", startStation.getName() + " 0");
        }

        // Usamos una pila para implementar DFS manualmente
        Stack<Station> stack = new Stack<>();
        SetList<Station> visited = new SetList<>();
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
        for (MapsList.Entry<Station, Integer> entry : distances.entrySet()) {
            Station station = entry.getKey();
            Integer distance = entry.getValue();
            System.out.println("Estación: " + station.getName() + ", Distancia: " + distance);
        }

        // Mostrar las sucursales creadas
//        System.out.println("Sucursales creadas (DFS): " + branches.toString());
    }

    /**
     *  Realiza un recorrido en anchura BFS a partir de la estación inicial.
     * Inicializa las estructuras necesarias, colorea la estación inicial en verde,
     * recorre las estaciones adyacentes agregándolas a la cola si no han sido aún visitadas y
     * verifica y maneja conflictos en base a la distancia de la estación inicial.
     * @param startStation La estación de inicio para el recorrido de anchura
     */
    private void runBFS(Station startStation) {
        int T = this.T;
        // Inicializar la distancia de la estación inicial
        Queue<Station> queue = new Queue<>();
        branches.clear();
        branches.add(startStation); // Sucursal inicial
        SetList<Station> visitedStations = new SetList<>();
        //Map<Station, Integer> distances = new HashMap<>();
        MapsList<Station, Integer> distances = new MapsList<>();
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
        for (MapsList.Entry<Station, Integer> entry : distances.entrySet()) {
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
        SetList<Station> visitedStations = new SetList<>();
        //Map<Station, Integer> distances = new HashMap<>();
        MapsList<Station, Integer> distances = new MapsList<>();
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
    
    /**
     * Agrega en el grafo una arista entre dos estaciones si no existe.
     * Verifica si las estaciones son diferentes y crea un identificador único para la conexión
     * @param station1 Nombre de la primera estación
     * @param station2 Nombre de la segunda estación
     */
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

    /**
     * Agrega estaciones al grafo si no existe. Verifica si la estación ya está 
     * presente en el grafo y, si no, la añade y configura su etiqueta.
     * @param station Instancia de la estación que se va a agregar
     */
    private void addStationToGraph(String station) {
        if (graphStreamGraph.getNode(station) == null) {
            graphStreamGraph.addNode(station);
            graphStreamGraph.getNode(station).setAttribute("ui.label", station);
        }
    }

    /**
     * Verifica que la estación no se repita en la lista de estaciones.
     * Si existe, muestra un mensaje de advertencia
     * Si no existe, la añade a la lista, al grafo y emite un mensaje de confirmación.
     * @param station Nombre de la estación que se verifica y agrega.
     */
    public void verificateStations(String station) {
        if (stations.contains(station)) {
            JOptionPane.showMessageDialog(this, "La estación ya existe", "Aviso", JOptionPane.WARNING_MESSAGE);
        } else {
            stations.add(station);
            JOptionPane.showMessageDialog(this, "Estación añadida exitosamente", "Aviso", JOptionPane.INFORMATION_MESSAGE);
            addStationToGraph(station);  // Lógica para agregar al grafo
        }
    }

    /**
     * Obtiene el valor de T.
     * @return valor de T.
     */
    public int getT() {
        return this.T;
    }

    /**
    * Actualiza el grafo a partir de las estaciones y conexiones presentes en la networktrain.
    * Si el grafo o la red no están cargados, no se hace ninguna actualización.
    * Recorre todas las estaciones y conexiones, añadiéndolas al grafo si no existen,
    * y colorea en verde las estaciones que son sucursales.
     */
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

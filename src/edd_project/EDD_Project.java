package edd_project;

import com.EDD.JsonLoader;
import com.EDD.Grafo;
import java.io.IOException;
import com.interfaces.WelcomeInterface;
import com.graph.NetworkTrain;
import com.graph.Station;
import com.graph.Connection;
import com.graph.BFSListener;
import com.graph.BreadthFirstSearch;
import com.graph.LinkedList;
import com.graph.Node;

/**
 *
 * @author PC
 */
public class EDD_Project {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        WelcomeInterface interface1 = new WelcomeInterface();
        interface1.show();
        
//        Crear la red de trenes
//        NetworkTrain networkTrain = new NetworkTrain();
//
//        // Agregar estaciones
//        networkTrain.addStation("A");
//        networkTrain.addStation("B");
//        networkTrain.addStation("C");
//        networkTrain.addStation("D");
//        networkTrain.addStation("E");
//        networkTrain.addStation("F");
//        networkTrain.addStation("G");
//
//        // Agregar conexiones
//        networkTrain.addConnection("A", "B");
//        networkTrain.addConnection("A", "C");
//        networkTrain.addConnection("B", "D");
//        networkTrain.addConnection("C", "D");
//        networkTrain.addConnection("D", "E");
//        networkTrain.addConnection("E", "F");
//        networkTrain.addConnection("C", "G");
//
//        // Ejecutar BFS desde la estación A
//        System.out.println("Iniciando BFS desde la estación: A");
//        BreadthFirstSearch bfs = new BreadthFirstSearch(networkTrain.getStation("A"), networkTrain.getStations());
//        bfs.traverse(new BFSListener() {
//            @Override
//            public void stationVisited(Station station) {
//                System.out.println("Estación visitada: " + station.getName());
//            }
//        });
//
//        // Mostrar las estaciones visitadas
//        System.out.println("Estaciones visitadas en orden:");
//        Node<Station> aux = bfs.getVisitedStations().getHead();
//        while (aux != null) {
//            Station station = aux.getData();
//            System.out.println("Estación recorrida: " + station.getName());
//            aux = aux.getNext();
//        }
    }

}

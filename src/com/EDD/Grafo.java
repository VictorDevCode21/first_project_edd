/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.EDD;

import java.util.HashMap;
import java.util.Map;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;

/**
 *
 * @author carlos
 */

public class Grafo {
    
    /** Mapa de nodos que representan las paradas del grafo. */
    private Map<String, Nodo> paradas;
    /** Grafo de GraphStream para visualización. */
    private Graph graph;

    /**
     * Constructor que inicializa un nuevo grafo vacío.
     */
    public Grafo() {
        this.paradas = new HashMap<>();
        this.graph = new SingleGraph("Red de Transporte");
        this.graph.setStrict(false);
        this.graph.setAutoCreate(true);
    }

    /**
     * Añade una nueva parada al grafo.
     * 
     * @param id El identificador de la nueva parada.
     */
    public void addParada(String id) {
        if (!paradas.containsKey(id)) {
            Nodo nodo = new Nodo(id);
            paradas.put(id, nodo);
            Node graphNode = graph.addNode(id);
            graphNode.setAttribute("ui.label", id);
        }
    }

    /**
     * Conecta dos paradas en el grafo.
     * 
     * @param id1 El identificador de la primera parada.
     * @param id2 El identificador de la segunda parada.
     */
    public void conectarParadas(String id1, String id2) {
        Nodo nodo1 = paradas.get(id1);
        Nodo nodo2 = paradas.get(id2);
        if (nodo1 != null && nodo2 != null) {
            nodo1.getParadasAdyacentes().insertar(nodo1, nodo2);
            nodo2.getParadasAdyacentes().insertar(nodo2, nodo1); // Asumiendo grafo no dirigido
            graph.addEdge(id1 + "-" + id2, id1, id2);
        }
    }

    /**
     * Realiza una búsqueda en profundidad para determinar la cobertura desde una sucursal.
     * 
     * @param id El identificador de la parada desde la cual se inicia la búsqueda.
     * @param t El número máximo de paradas a considerar.
     * @return Un mapa de paradas alcanzables y su distancia desde la parada inicial.
     */
    public Map<String, Integer> profundidad(String id, int t) {
        Map<String, Integer> cobertura = new HashMap<>();
        boolean visitados[] = new boolean[paradas.size()];
        Nodo nodoInicial = paradas.get(id);
        if (nodoInicial != null) {
            profundidadRecursiva(nodoInicial, cobertura, visitados, 0, t);
        }
        return cobertura;
    }

    private void profundidadRecursiva(Nodo nodo, Map<String, Integer> cobertura, boolean[] visitados, int distancia, int t) {
        cobertura.put(nodo.getId(), distancia);
        visitados[Integer.parseInt(nodo.getId())] = true;
        if (distancia < t) {
            AristaC arista = nodo.getParadasAdyacentes().getpFirst();
            while (arista != null) {
                Nodo adyacente = arista.getNodoB();
                if (!visitados[Integer.parseInt(adyacente.getId())]) {
                    profundidadRecursiva(adyacente, cobertura, visitados, distancia + 1, t);
                }
                arista = arista.pNext;
            }
        }
    }

    /**
     * Realiza una búsqueda en amplitud para determinar la cobertura desde una sucursal.
     * 
     * @param id El identificador de la parada desde la cual se inicia la búsqueda.
     * @param t El número máximo de paradas a considerar.
     * @return Un mapa de paradas alcanzables y su distancia desde la parada inicial.
     */
    public Map<String, Integer> amplitud(String id, int t) {
        Map<String, Integer> cobertura = new HashMap<>();
        boolean visitados[] = new boolean[paradas.size()];
        Cola cola = new Cola();
        Nodo nodoInicial = paradas.get(id);
        if (nodoInicial != null) {
            cola.encolar(nodoInicial);
            cobertura.put(nodoInicial.getId(), 0);
            visitados[Integer.parseInt(nodoInicial.getId())] = true;
            while (!cola.isEmpty()) {
                Nodo nodo = cola.desencolar();
                int distancia = cobertura.get(nodo.getId());
                if (distancia < t) {
                    AristaC arista = nodo.getParadasAdyacentes().getpFirst();
                    while (arista != null) {
                        Nodo adyacente = arista.getNodoB();
                        if (!visitados[Integer.parseInt(adyacente.getId())]) {
                            cola.encolar(adyacente);
                            cobertura.put(adyacente.getId(), distancia + 1);
                            visitados[Integer.parseInt(adyacente.getId())] = true;
                        }
                        arista = arista.pNext;
                    }
                }
            }
        }
        return cobertura;
    }

    /**
     * Imprime las aristas de cada parada en el grafo.
     */
    public void imprimir() {
        for (Nodo nodo : paradas.values()) {
            nodo.getParadasAdyacentes().mostrarAristas();
        }
    }

    /**
     * Muestra el grafo utilizando GraphStream.
     */
    public void mostrarGrafo() {
        System.setProperty("org.graphstream.ui", "swing");
        graph.display();
    }
    
}

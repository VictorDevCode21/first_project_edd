/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.graph;
import java.util.*;

/**
 *
 * @author PC
 */
public class Graph {
    // Creamos un atributo para la lista de adyacencia
    private Map<String, List<String>> adjacencyList;
    private Map<String, Boolean> isSucursal;  // Para identificar las sucursales

    public Graph() {
        adjacencyList = new HashMap<>();
        isSucursal = new HashMap<>();
    }

    // Agregar parada al grafo
    public void addNode(String node) {
        adjacencyList.putIfAbsent(node, new ArrayList<>());
        isSucursal.put(node, false);
    }

    // Agregar arista entre dos paradas
    public void addEdge(String node1, String node2) {
        adjacencyList.get(node1).add(node2);
        adjacencyList.get(node2).add(node1);  // Asumimos grafo no dirigido
    }

    // Establecer sucursal en una parada
    public void setSucursal(String node) {
        isSucursal.put(node, true);
    }

    // Método DFS para revisar cobertura comercial
    public Set<String> dfs(String startNode, int t) {
        Set<String> visited = new HashSet<>();
        dfsRecursive(startNode, t, 0, visited);
        return visited;
    }

    private void dfsRecursive(String node, int t, int depth, Set<String> visited) {
        if (depth > t || visited.contains(node)) {
            return;
        }
        visited.add(node);
        for (String neighbor : adjacencyList.get(node)) {
            dfsRecursive(neighbor, t, depth + 1, visited);
        }
    }

    // Método BFS para revisar cobertura comercial
    public Set<String> bfs(String startNode, int t) {
        Set<String> visited = new HashSet<>();
        Queue<String> queue = new LinkedList<>();
        Map<String, Integer> depth = new HashMap<>();

        queue.add(startNode);
        depth.put(startNode, 0);

        while (!queue.isEmpty()) {
            String node = queue.poll();
            int currentDepth = depth.get(node);

            if (currentDepth > t || visited.contains(node)) {
                continue;
            }

            visited.add(node);
            for (String neighbor : adjacencyList.get(node)) {
                if (!visited.contains(neighbor)) {
                    queue.add(neighbor);
                    depth.put(neighbor, currentDepth + 1);
                }
            }
        }
        return visited;
    }
}

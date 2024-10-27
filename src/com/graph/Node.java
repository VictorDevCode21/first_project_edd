/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.graph;

/**
 *
 * @author PC
 */

/**
 * Clase genérica que representa un nodo en una lista enlazada.
 * 
 * @param <T> El tipo de dato que almacenará el nodo.
 */

public class Node<T> {
    private T data;            // Data que se guardara en el nodo
    private Node<T> next;      // Puntero hacia el nodo siguiente
    private Integer headQuarterDistance; // Distancia de la sucursal principal

    /**
     * Constructor para inicializar el nodo con el dato proporcionado.
     * 
     * @param data El dato que se almacenará en el nodo.
     */
    
    public Node(T data) {
        this.data = data;
        this.next = null;
    }

    /**
     * Obtiene el dato almacenado en el nodo.
     * 
     * @return El dato almacenado en el nodo.
     */
    
    public T getData() {
        return data;
    }
    

    /**
     * Establece el dato a almacenar en el nodo.
     * 
     * @param data El dato a almacenar en el nodo.
     */
    public void setData(T data) {
        this.data = data;
    }

    /**
     * Obtiene el nodo siguiente en la lista enlazada.
     * 
     * @return El nodo siguiente.
     */
    
    public Node<T> getNext() {
        return next;
    }

    /**
     * Establece el nodo siguiente en la lista enlazada.
     * 
     * @param next El nodo siguiente a establecer.
     */
    
    public void setNext(Node<T> next) {
        this.next = next;
    }
}

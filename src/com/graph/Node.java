/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.graph;

/**
 *
 * @author PC
 */

    // Clase Nodo generica para implementar lista enlazada
public class Node<T> {
    private T data;            // Data que se guardara en el nodo
    private Node<T> next;      // Puntero hacia el nodo siguiente
    private Integer headQuarterDistance; // Distancia de la sucursal principal

    // Constructor para inicializar el nodo
    public Node(T data) {
        this.data = data;
        this.next = null;
    }

    /**
     * @return the data
     */
    public T getData() {
        return data;
    }

    /**
     * @param data the data to set
     */
    public void setData(T data) {
        this.data = data;
    }

    /**
     * @return the next
     */
    public Node<T> getNext() {
        return next;
    }

    /**
     * @param next the next to set
     */
    public void setNext(Node<T> next) {
        this.next = next;
    }
}

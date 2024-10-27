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
 * Clase genérica que representa una cola (Queue) para la búsqueda BFS.
 * 
 * @param <T> El tipo de dato que almacenará la cola.
 */
public class Queue<T> {

    private LinkedList<T> list;
    
    
    /**
     * Constructor que inicializa la cola.
     */

    public Queue() {
        list = new LinkedList<>();
    }
    
    
    /**
     * Agrega un elemento al final de la cola.
     * 
     * @param item El elemento a agregar.
     */

    public void enqueue(T item) {
        list.add(item);
    }
    
    
    /**
     * Elimina y retorna el primer elemento de la cola.
     * 
     * @return El primer elemento de la cola, o null si la cola está vacía.
     */

    public T dequeue() {
        if (!isEmpty()) {
            return list.removeFirst();
        }
        return null;
    }
    
    
    /**
     * Verifica si la cola está vacía.
     * 
     * @return true si la cola está vacía, false en caso contrario.
     */

    public boolean isEmpty() {
        return list.isEmpty();
    }
}

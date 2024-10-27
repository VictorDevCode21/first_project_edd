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
 * Clase genérica que simula una pila (Stack) de Java usando una LinkedList como base.
 * 
 * @param <T> El tipo de dato que almacenará la pila.
 */
 public class Stack<T> {

    private LinkedList list;

    /**
     * Constructor que inicializa la pila.
     */
    
    public Stack() {
        list = new LinkedList();
    }

    /**
     * Agrega un elemento al tope de la pila.
     * 
     * @param value El elemento a agregar.
     */
    
    public void push(T value) {
        list.addFirst(value); // Agrega el elemento al inicio de la lista
    }

    /**
     * Remueve y retorna el último elemento agregado (el tope de la pila).
     * 
     * @return El elemento removido, o null si la pila está vacía.
     */
    
    public T pop() {
        if (!list.isEmpty()) {
            return (T) list.removeFirst(); 
        }
        return null;  // Devolver null si la lista está vacía
    }
    
   
    /**
     * Verifica si la pila está vacía.
     * 
     * @return true si la pila está vacía, false en caso contrario.
     */
    
    public boolean isEmpty() {
        return list.isEmpty();
    }

    
    /**
     * Retorna el elemento en el tope de la pila sin eliminarlo.
     * 
     * @return El elemento en el tope de la pila, o null si la pila está vacía.
     */
    
    public String peek() {
        if (!list.isEmpty()) {
            return list.getHead().toString();
        }
        return null;
    }
}

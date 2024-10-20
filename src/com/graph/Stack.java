/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.graph;

/**
 *
 * @author PC
 */
//  Clase Stack que simula un Stack de Java usando una LinkedList como base. 
public class Stack<T> {

    private LinkedList list;

//    Constructor
    public Stack() {
        list = new LinkedList();
    }

//    Agrega un elemento al stack
    public void push(T value) {
        list.addFirst(value); // Agrega el elemento al inicio de la lista
    }

//    Remueve el ultimo elemento agregado, o sea el primero de la lista
    public Station pop() {
        if (!list.isEmpty()) {
            return (Station) list.removeFirst(); 
        }
        return null;  // Devolver null si la lista está vacía
    }

//    Retorna true si el stack esta vacio, de lo contrario retorna false
    public boolean isEmpty() {
        return list.isEmpty();
    }

    // Retorna el elemento en el tope sin eliminarlo
    public String peek() {
        if (!list.isEmpty()) {
            return list.getHead().toString();
        }
        return null;
    }
}

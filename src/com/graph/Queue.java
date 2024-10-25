/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.graph;

/**
 *
 * @author PC
 */
// Clase Queue (Cola) que usaremos para la busqueda BFS
public class Queue<T> {

    private LinkedList<T> list;

    public Queue() {
        list = new LinkedList<>();
    }

    public void enqueue(T item) {
        list.add(item);
    }

    public T dequeue() {
        if (!isEmpty()) {
            return list.removeFirst();
        }
        return null;
    }

    public boolean isEmpty() {
        return list.isEmpty();
    }
}

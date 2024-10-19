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
public class Queue {
    LinkedList list; 
    
    
    public Queue() {
        this.list = new LinkedList();
    }
    
    public void enqueue(Object data) {
        list.add(data);
    }
    
    public Object dequeue() {
        if (!list.isEmpty()) {
            Object data = list.getHead().getData();
            list.setHead(list.getHead().getNext());
            return data;
        }
        return null;
    }
    
    public boolean isEmpty() {
        return list.isEmpty();
    }
    
    
    
}

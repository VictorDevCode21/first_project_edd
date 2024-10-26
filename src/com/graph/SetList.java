/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.graph;

/**
 *
 * @author Joao
 */
public class SetList<E> {

    private LinkedList<E> elements;
    
    //Inicializar lista de elementos
    public SetList(){
        elements = new LinkedList<>();
    }
    
    
    // Agregar elementos al set (sin duplicados)
    public void add(E element) {
        if (!elements.contains(element)) {
            elements.add(element);
        }
    }

    // Agrega todos los elementos de una LinkedList, eliminando los duplicados
    public void addAll(LinkedList<E> list) {
        for (E item : list) {
            add(item);  
        }
    }

    // Método para verificar si contiene un elemento
    public boolean contains(E element) {
        return elements.contains(element);
    }

    // Método para obtener todos los elementos como una LinkedList
    public LinkedList<E> getElements() {
        return elements;
    }
    
        // Método para obtener el tamaño del set
    public int size() {
        return elements.size();
    }

    @Override
    public String toString() {
        return elements.toString();
    }
    
}


}

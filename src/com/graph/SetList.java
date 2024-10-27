/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.graph;

/**
 *
 * @author Joao
 */

/**
 * La clase SetList representa una lista de elementos que no permite duplicados.
 * Utiliza una LinkedList para almacenar los elementos.
 * 
 * @param <E> el tipo de elementos en esta lista.
 */
public class SetList<E> {

    private LinkedList<E> elements;
    
    /**
     * Inicializa una nueva instancia de SetList con una lista vacía.
     */
    public SetList(){
        elements = new LinkedList<>();
    }
    
    
    /**
     * Agrega un elemento al set si no está ya presente.
     * 
     * @param element el elemento a agregar.
     */
    public void add(E element) {
        if (!elements.contains(element)) {
            elements.add(element);
        }
    }

    /**
     * Agrega todos los elementos de una LinkedList al set, eliminando duplicados.
     * 
     * @param list la lista de elementos a agregar.
     */
    public void addAll(LinkedList<E> list) {
        for (E item : list) {
            add(item);  
        }
    }

    /**
     * Verifica si el set contiene un elemento específico.
     * 
     * @param element el elemento a verificar.
     * @return true si el set contiene el elemento, false en caso contrario.
     */
    public boolean contains(E element) {
        return elements.contains(element);
    }

    
    /**
     * Obtiene todos los elementos del set como una LinkedList.
     * 
     * @return una LinkedList que contiene todos los elementos del set.
     */
    public LinkedList<E> getElements() {
        return elements;
    }
    
    /**
     * Obtiene el tamaño del set.
     * 
     * @return el número de elementos en el set.
     */
    public int size() {
        return elements.size();
    }

    /**
     * Devuelve una representación en cadena de los elementos del set.
     * 
     * @return una cadena que representa los elementos del set.
     */
    @Override
    public String toString() {
        return elements.toString();
    }
    
}




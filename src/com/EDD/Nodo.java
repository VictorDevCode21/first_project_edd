/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.EDD;

/**
 *
 * @author carlos
 */

//Vertice
public class Nodo {
    
    /** El identificador de la parada. */
    private String id;
    
    /** El siguiente nodo en la estructura de datos. */
    private Nodo pNext;
    
    /** Lista de aristas adyacentes a esta parada. */
    private ListaAristas paradasAdyacentes;
    
    
    // Distancia medida en matriz de negocio    
    private int matrixDistance;

    /**
     * Constructor que inicializa una parada con un identificador específico.
     * 
     * @param id El identificador de la parada.
     */
    public Nodo(String id) {
        this.id = id;
        this.pNext = null;
        this.paradasAdyacentes = new ListaAristas();
    }

    /**
     * Constructor alternativo que inicializa una parada con una lista de aristas adyacentes.
     * 
     * @param listaInterna La lista de aristas adyacentes a la parada.
     */
    public Nodo(ListaAristas listaInterna) {
        this.paradasAdyacentes = listaInterna;
    }

    /**
     * Obtiene el identificador de la parada.
     * 
     * @return El identificador de la parada.
     */
    public String getId() {
        return id;
    }

    /**
     * Establece el identificador de la parada.
     * 
     * @param id El nuevo identificador para la parada.
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Obtiene el siguiente nodo en la estructura de datos.
     * 
     * @return El siguiente nodo.
     */
    public Nodo getpNext() {
        return pNext;
    }

    /**
     * Establece el siguiente nodo en la estructura de datos.
     * 
     * @param pNext El nodo que se establecerá como el siguiente.
     */
    public void setpNext(Nodo pNext) {
        this.pNext = pNext;
    }

    /**
     * Obtiene la lista de aristas adyacentes a esta parada.
     * 
     * @return La lista de aristas adyacentes.
     */
    public ListaAristas getParadasAdyacentes() {
        return paradasAdyacentes;
    }

    /**
     * Establece la lista de aristas adyacentes a esta parada.
     * 
     * @param paradasAdyacentes La nueva lista de aristas adyacentes.
     */
    public void setParadasAdyacentes(ListaAristas paradasAdyacentes) {
        this.paradasAdyacentes = paradasAdyacentes;
    }
    
}

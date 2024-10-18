/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.EDD;

/**
 *
 * @author carlos
 */
public class AristaC {
    
    /** El primer nodo de la arista. */
    public Nodo nodoA;
    
    /** El segundo nodo de la arista. */
    public Nodo nodoB;
    
    /** El siguiente elemento en la lista enlazada de aristas. */
    public AristaC pNext;

    /**
     * Constructor para crear una nueva arista que conecta dos nodos.
     * 
     * @param nodoA El primer nodo que esta arista conectará.
     * @param nodoB El segundo nodo que esta arista conectará.
     */
    public AristaC(Nodo nodoA, Nodo nodoB) {
        this.nodoA = nodoA;
        this.nodoB = nodoB;
        this.pNext = null;
    }

    /**
     * Obtiene el primer nodo de la arista.
     * 
     * @return El primer nodo de la arista.
     */
    public Nodo getNodoA() {
        return nodoA;
    }

    /**
     * Establece el primer nodo de la arista.
     * 
     * @param nodoA El nuevo primer nodo de la arista.
     */
    public void setNodoA(Nodo nodoA) {
        this.nodoA = nodoA;
    }

    /**
     * Obtiene el segundo nodo de la arista.
     * 
     * @return El segundo nodo de la arista.
     */
    public Nodo getNodoB() {
        return nodoB;
    }

    /**
     * Establece el segundo nodo de la arista.
     * 
     * @param nodoB El nuevo segundo nodo de la arista.
     */
    public void setNodoB(Nodo nodoB) {
        this.nodoB = nodoB;
    }
    
    
}

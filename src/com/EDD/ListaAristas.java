/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.EDD;

/**
 *
 * @author carlos
 */
public class ListaAristas {
    
    /** La primera arista en la lista enlazada. */
    private AristaC pFirst;
    
    /** La última arista en la lista enlazada. */
    private AristaC pLast;

    /**
     * Constructor que inicializa una nueva lista de aristas vacía.
     */
    public ListaAristas() {
        this.pFirst = null;
        this.pLast = null;
    }

    /**
     * Obtiene la primera arista de la lista.
     * 
     * @return La primera arista en la lista enlazada.
     */
    public AristaC getpFirst() {
        return pFirst;
    }

    /**
     * Establece la primera arista de la lista.
     * 
     * @param pFirst La arista que se establecerá como primera.
     */
    public void setpFirst(AristaC pFirst) {
        this.pFirst = pFirst;
    }

    /**
     * Obtiene la última arista de la lista.
     * 
     * @return La última arista en la lista enlazada.
     */
    public AristaC getpLast() {
        return pLast;
    }

    /**
     * Establece la última arista de la lista.
     * 
     * @param pLast La arista que se establecerá como última.
     */
    public void setpLast(AristaC pLast) {
        this.pLast = pLast;
    }

    /**
     * Comprueba si la lista de aristas está vacía.
     * 
     * @return Verdadero si la lista está vacía, falso en caso contrario.
     */
    public boolean isEmpty() {
        return pFirst == null;
    }

    /**
     * Inserta una nueva arista al final de la lista.
     * 
     * @param nodoA El primer nodo de la nueva arista.
     * @param nodoB El segundo nodo de la nueva arista.
     */
    public void insertar(Nodo nodoA, Nodo nodoB) {
        AristaC nuevaArista = new AristaC(nodoA, nodoB);
        if (this.isEmpty()) {
            this.setpLast(nuevaArista);
            this.setpFirst(nuevaArista);
        } else {
            this.pLast.pNext = nuevaArista;
            this.setpLast(nuevaArista);
        }
    }

    /**
     * Busca un nodo específico en la lista de aristas.
     * 
     * @param nodoA El nodo a buscar en la lista.
     * @return Verdadero si el nodo está presente en alguna arista, falso en caso contrario.
     */
    public boolean buscarNodo(Nodo nodoA) {
        AristaC aux = pFirst;
        while (aux != null && (aux.getNodoA() != nodoA && aux.getNodoB() != nodoA)) {
            aux = aux.pNext;
        }
        return aux != null;
    }

    /**
     * Muestra las aristas de la lista en la consola.
     */
    public void mostrarAristas() {
        AristaC aux = pFirst;
        if (aux != null) {
            System.out.print(aux.getNodoA().getId());
            while (aux != null) {
                System.out.print(" - " + aux.getNodoB().getId());
                aux = aux.pNext;
            }
            System.out.println("");
        }
    }
    
    
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.EDD;

/**
 *
 * @author carlos
 */
public class Cola {
    /** El primer nodo de la cola. */
    public Nodo pHead;
    
    /** El último nodo de la cola. */
    public Nodo pLast;

    /**
     * Constructor que inicializa una nueva cola vacía.
     */
    public Cola() {
        this.pHead = null;
        this.pLast = null;
    }

    /**
     * Comprueba si la cola está vacía.
     * 
     * @return Verdadero si la cola está vacía, falso en caso contrario.
     */
    public boolean isEmpty() {
        return pHead == null;
    }

    /**
     * Añade un nuevo nodo al final de la cola.
     * 
     * @param nuevo El nuevo nodo a encolar.
     */
    public void encolar(Nodo nuevo) {
        if (this.isEmpty()) {
            this.pHead = nuevo;
            this.pLast = nuevo;
        } else {
            this.pLast.setpNext(nuevo);
            this.pLast = nuevo;
        }
    }

    /**
     * Elimina y devuelve el primer nodo de la cola.
     * 
     * @return El nodo desencolado o null si la cola está vacía.
     */
    public Nodo desencolar() {
        if (!this.isEmpty()) {
            Nodo aux = pHead;
            pHead = pHead.getpNext();
            if (pHead == null) {
                pLast = null;
            }
            return aux;
        }
        return null;
    }
}

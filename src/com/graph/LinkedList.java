/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this auxlate
 */
package com.graph;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 *
 * @author PC
 */
// Lista enlazada generica para poder guardar las conexiones(aristas) y sucursales
public class LinkedList<T> implements Iterable<T> {

    private Node<T> head;
    private Node<T> last;
    private int size;

//  Constructor de la clase LinkedList  
    public LinkedList() {
        this.head = null;
        this.last = null;
        this.size = 0;
    }

    // Metodo para agregar elementos a la lista
    public void add(T data) {
        Node<T> newNode = new Node<>(data);  // Crea un nuevo nodo con la data pasada por params

        if (getHead() == null) {
            setHead(newNode);  // Si la lista esta vacia, el nuevo nodo es la head
        } else {
            Node<T> aux = getHead();
            while (aux.getNext() != null) {  // Recorre los nodos
                aux = aux.getNext();
            }
            aux.setNext(newNode);  // Agrega el nuevo nodo al final
        }
    }

//    Agrega un nuevo nodo al inicio
    public void addFirst(T data) {
        Node<T> newNode = new Node<>(data);

        if (getHead() == null) {
            setHead(newNode);
            last = newNode;
        } else {
            Node<T> aux = getHead();
            setHead(newNode);
            newNode.setNext(aux);
        }
        size++;
    }

    public T removeFirst() {
        if (isEmpty()) {
            return null;  // Lista vacía
        }
        T data = head.getData();  // Obtener los datos del primer nodo
        head = head.getNext();  // Mover la cabeza al siguiente nodo
        size--;
        if (isEmpty()) {
            last = null;  // Si la lista queda vacía, ajustar el tail a null
        }
        return data;  // Devolver los datos del nodo eliminado
    }

    // Muestra los elementos de la lista
    public void display() {
        Node<T> aux = getHead();
        while (aux != null) {
            System.out.print(aux.getData() + " -> ");
            aux = aux.getNext();
        }
        System.out.println("null");
    }

    // Metodo remove para borrar algun nodo
    public void remove(T key) {
        if (getHead() == null) {
            return;  // Si la lista esta vacia no hace nada
        }
        // Si el nodo a borrar es el head, mueve el head al siguiente 
        if (getHead().getData().equals(key)) {
            setHead(getHead().getNext());
            return;
        }

        Node<T> aux = getHead();
        // Recorre la lista para encontrar el nodo a borrar 
        while (aux.getNext() != null && !aux.getNext().getData().equals(key)) {
            aux = aux.getNext();
        }

        // Si encuentra la key, borra el nodo
        if (aux.getNext() != null) {
            aux.setNext(aux.getNext().getNext());
        }
    }

    // Metodo para saber si contiene la key o data que estamos buscando
    public boolean contains(T key) {
        Node<T> aux = getHead();
        while (aux != null) {
            if (aux.getData().equals(key)) {
                return true;  // Si lo encuentra retorna true 
            }
            aux = aux.getNext();
        }
        return false;  // Si no lo encuentra retorna false
    }

    // Metodo para obtener el size de la lista
    public int size() {
        int count = 0;
        Node<T> aux = getHead();
        while (aux != null) {
            count++;
            aux = aux.getNext();
        }
        return count;
    }

    // Método para obtener el iterador
    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private Node<T> current = head;

            @Override
            public boolean hasNext() {
                return current != null;
            }

            @Override
            public T next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                T data = current.getData();
                current = current.getNext();
                return data;
            }
        };
    }

//    Obtiene un elemento por su indice
    public T get(int index) {
        if (index < 0 || index >= size()) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size());
        }

        Node<T> current = head;
        for (int i = 0; i < index; i++) {
            current = current.getNext();
        }
        return current.getData();
    }

    /**
     * @return the head
     */
    public Node<T> getHead() {
        return head;
    }

    /**
     * @param head the head to set
     */
    public void setHead(Node<T> head) {
        this.head = head;
    }

    // Retorna true si la lista esta vacia, de lo contrario retorna false
    public boolean isEmpty() {
        return head == null;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        Node<T> current = head;

        sb.append("[");
        while (current != null) {
            sb.append(current.getData());
            current = current.getNext();
            if (current != null) {
                sb.append(", ");  // Añadir coma entre elementos
            }
        }
        sb.append("]");

        return sb.toString();  // Retornar la representación como String
    }
}

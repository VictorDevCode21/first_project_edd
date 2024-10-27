package com.graph;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 *
 * @author PC
 */

/**
 * Lista enlazada genérica para poder guardar las conexiones (aristas) y sucursales.
 *
 * @param <T> el tipo de elementos en la lista
 */

public class LinkedList<T> implements Iterable<T> {

    private Node<T> head; /** Nodo cabeza de la lista. */
    private Node<T> last; /** Último nodo de la lista. */
    private int size;     /** Tamaño de la lista. */

    /**
     * Constructor de la clase LinkedList.
     */
    
    public LinkedList() {
        this.head = null;
        this.last = null;
        this.size = 0;
    }

    /**
     * Método para agregar elementos a la lista.
     *
     * @param data el dato a agregar
     */
    
    public void add(T data) {
        Node<T> newNode = new Node<>(data);  // Crea un nuevo nodo con la data pasada por params

        if (getHead() == null) {
            setHead(newNode);  // Si la lista esta vacia, el nuevo nodo es el head
        } else {
            Node<T> aux = getHead();
            while (aux.getNext() != null) {  // Recorre los nodos
                aux = aux.getNext();
            }
            aux.setNext(newNode);  // Agrega el nuevo nodo al final
        }
    }

    /**
     * Método para agregar una lista de elementos a la lista.
     *
     * @param stations la lista de estaciones a agregar
     */
    
    public void addAll(LinkedList<? extends T> stations) {
        Node<? extends T> current = stations.getHead(); // Obtener la cabeza de la lista de estaciones
        while (current != null) {
            add(current.getData()); // Agrega cada estación usando el método add
            current = current.getNext(); // Mover al siguiente nodo en la lista de estaciones
        }
    }

    /**
     * Agrega un nuevo nodo al inicio de la lista.
     *
     * @param data el dato a agregar
     */
    
    public void addFirst(T data) {
        
        
        Node<T> newNode = new Node<>(data); // Crear un nuevo nodo con el dato proporcionado

        // Si la lista está vacía, el nuevo nodo se convierte en la cabeza y el último nodo
        if (getHead() == null) {
            setHead(newNode);
            last = newNode;
            } 
        
        // Si la lista no está vacía, insertar el nuevo nodo al inicio
        else {
            Node<T> aux = getHead(); // Guardar la referencia al nodo actual de la cabeza
            setHead(newNode);        // Establecer el nuevo nodo como la cabeza
            newNode.setNext(aux);    // Enlazar el nuevo nodo con el antiguo nodo de la cabeza
        }
        size++; // Incrementar el tamaño de la lista
    }

    
    
   /**
    * Elimina y retorna el primer elemento de la lista.
    *
    * @return el dato del primer nodo eliminado, o null si la lista está vacía
    */
    
    public T removeFirst() {
        // Verificar si la lista está vacía
        if (isEmpty()) {
            return null;  // Lista vacía
        }
        T data = head.getData();  // Obtener los datos del primer nodo
        head = head.getNext();  // Mover la cabeza al siguiente nodo
        size--; // Disminuir el tamaño de la lista
        if (isEmpty()) {
            last = null;  // Si la lista queda vacía, ajustar el tail a null
        }
        return data;  // Devolver los datos del nodo eliminado
    }

    
   /**
    * Muestra los elementos de la lista.
    */
    
    public void display() {
        Node<T> aux = getHead(); // Obtener la cabeza de la lista
        
        // Recorrer la lista y mostrar cada elemento
        while (aux != null) {
            System.out.print(aux.getData() + " -> ");
            aux = aux.getNext();
        }
        System.out.println("null"); // Indicar el final de la lista
    }

    
    /**
     * Método para borrar un nodo de la lista.
     *
     * @param key el dato del nodo a borrar
     */
    
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

    
   
    /**
     * Método para verificar si la lista contiene un dato específico.
     *
     * @param key el dato a buscar
     * @return true si la lista contiene el dato, false en caso contrario
     */
    
    public boolean contains(T key) {
        Node<T> aux = getHead(); // Obtener la cabeza de la lista
        
        // Recorrer la lista
        while (aux != null) {
            // Verificar si el dato del nodo actual es igual al dato buscado
            if (aux.getData().equals(key)) {
                return true;  // Si lo encuentra retorna true 
            }
            aux = aux.getNext(); // Mover al siguiente nodo
        }
        return false;  // Si no lo encuentra retorna false
    }

    
    /**
     * Método para obtener el tamaño de la lista.
     *
     * @return el tamaño de la lista
     */
    
    public int size() {
        int count = 0;  //variable donde se guardará el numero de nodos contados
        Node<T> aux = getHead(); // Obtener la cabeza de la lista
        
        // Recorrer la lista y contar los nodos
        while (aux != null) {
            count++;
            aux = aux.getNext();
        }
        return count;
    }

    
    /**
     * Método para obtener el iterador de la lista.
     *
     * @return el iterador de la lista
     */
    
    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private Node<T> current = head;
               
            
            /**
             * Verifica si hay más elementos en la lista.
             *
             * @return true si hay más elementos, false en caso contrario
             */
            
            @Override
            public boolean hasNext() {
                return current != null;
            }
            
            
            /**
             * Retorna el siguiente elemento en la lista.
             *
             * @return el siguiente elemento en la lista
             * @throws NoSuchElementException si no hay más elementos en la lista
             */

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

    
    /**
     * Obtiene un elemento por su índice.
     *
     * @param index el índice del elemento a obtener
     * @return el dato del nodo en el índice especificado
     * @throws IndexOutOfBoundsException si el índice está fuera de los límites de la lista
     */
    
    
    public T get(int index) {
        // Verificar si el índice está fuera de los límites de la lista
        if (index < 0 || index >= size()) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size());
        }

        Node<T> current = head; // Obtener la cabeza de la lista
        // Recorrer la lista hasta el índice especificado
        for (int i = 0; i < index; i++) {
            current = current.getNext();
        }
        return current.getData(); // Retornar el dato del nodo en el índice especificado
    }

    /**
    * Obtiene el nodo cabeza de la lista.
    *
    * @return el nodo cabeza de la lista
    */
    
    public Node<T> getHead() {
        return head;
    }

    /**
     * Establece el nodo cabeza de la lista.
     *
     * @param head el nodo cabeza a establecer
     */
    
    public void setHead(Node<T> head) {
        this.head = head;
    }

    /**
     * Retorna true si la lista está vacía, de lo contrario retorna false.
     *
     * @return true si la lista está vacía, false en caso contrario
     */
    
    public boolean isEmpty() {
        return head == null;
    }

    /**
     * Elimina todos los nodos y punteros de la lista.
     */

     public void clear() {
        head = null;  // Limpiar la lista
        last = null;  // Opcional
        size = 0;     // Restablecer tamaño
    }

    /**
     * Método para eliminar todos los elementos de otra lista.
     *
     * @param other la lista cuyos elementos se eliminarán de la lista actual
     */
     
    public void removeAll(LinkedList<T> other) {
        Node<T> current = other.getHead(); // Obtener la cabeza de la lista "other"
        // Recorrer la lista "other"
        while (current != null) {
            remove(current.getData()); // Elimina cada elemento de "other" de la lista actual
            current = current.getNext(); // Mover al siguiente nodo en la lista "other"
        }
    }

    /**
     * Método para verificar si la lista contiene todos los elementos de otra lista.
     *
     * @param other la lista cuyos elementos se verificarán en la lista actual
     * @return true si la lista contiene todos los elementos de "other", false en caso contrario
     */
    
    public boolean containsAll(LinkedList<T> other) {
        Node<T> current = other.getHead(); // Obtener la cabeza de la lista "other"
        // Recorrer la lista "other"
        while (current != null) {
            // Verificar si la lista actual contiene el dato del nodo actual de "other"
            if (!contains(current.getData())) { // Usa el método contains
                return false; // Si no contiene algún elemento, retorna false
            }
            // Mover al siguiente nodo en la lista "other"
            current = current.getNext();
        }
        return true; // Si contiene todos, retorna true
    }

    
    /**
    * Retorna una representación en cadena de la lista.
    *
    * @return una cadena que representa la lista
    */
    
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        Node<T> current = head;

        sb.append("[");
        // Recorrer la lista y agregar cada elemento a la cadena
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

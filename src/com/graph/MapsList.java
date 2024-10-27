/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.graph;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Joao
 */

/**
 * La clase MapsList representa una lista de entradas clave-valor.
 * Utiliza una LinkedList para almacenar las entradas.
 * 
 * @param <K> el tipo de las claves mantenidas por esta MapsList.
 * @param <V> el tipo de los valores mantenidos por esta MapsList.
 */
public class MapsList<K, V>  {
    private LinkedList<Entry<K, V>> entries;

    /**
     * Constructor que inicializa una nueva instancia de MapsList con una lista vacía.
     */
    public MapsList() {
        this.entries = new LinkedList<>(); // Para almacenar las entradas clave-valor
    }

    /**
     * Agrega o actualiza una entrada en la MapsList.
     * Si la clave ya existe, actualiza su valor. De lo contrario, agrega una nueva entrada.
     * 
     * @param key la clave de la entrada.
     * @param value el valor de la entrada.
     */
    public void put(K key, V value) {
        // Buscar si la clave ya existe
        for (Entry<K, V> entry : entries) {
            if (entry.getKey().equals(key)) {
                entry.setValue(value); //Actualizamos valor
                return;
            }
        }
        // Si no existe, agregamos una nueva entrada
        entries.add(new Entry<>(key, value));
    }
      
    /**
     * Obtiene el valor asociado a una clave.
     * 
     * @param key la clave cuyo valor asociado se desea obtener.
     * @return el valor asociado a la clave, o null si la clave no se encuentra.
     */
    public V get(K key) {
        for (Entry<K, V> entry : entries) {
            if (entry.getKey().equals(key)) {
                return entry.getValue(); // Retornar si encuentra
            }
        }
        return null; //null si no se encuentra
    }
    
    /**
     * Elimina una entrada basada en la clave.
     * 
     * @param key la clave de la entrada a eliminar.
     */
    public void remove(K key) {
        LinkedList<Entry<K, V>> toRemove = new LinkedList<>();
        for (Entry<K, V> entry : entries) {
            if (entry.getKey().equals(key)) {
                toRemove.add(entry);
            }
        }
        entries.removeAll(toRemove);
    }

    /**
     * Verifica si una clave existe en la MapsList.
     * 
     * @param key la clave a verificar.
     * @return true si la clave existe, false en caso contrario.
     */
    public boolean containsKey(K key) {
        for (Entry<K, V> entry : entries) {
            if (entry.getKey().equals(key)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Obtiene un conjunto de todas las entradas en la MapsList.
     * 
     * @return una lista de todas las entradas en la MapsList.
     */
    public List<Entry<K, V>> entrySet() {
        return entries;
    }
      
    /**
     * Obtiene el tamaño de la MapsList.
     * 
     * @return el número de entradas en la MapsList.
     */
    public int size() {
        return entries.size();
    }
    

    /**
     * Obtiene todas las claves en la MapsList.
     * 
     * @return una LinkedList de todas las claves en la MapsList.
     */
    public LinkedList<K> keySet() {
        LinkedList<K> keys = new LinkedList<>();
        for (Entry<K, V> entry : entries) {
            keys.add(entry.getKey());
        }
        return keys;
    }

    /**
     * Obtiene todos los valores en la MapsList.
     * 
     * @return una LinkedList de todos los valores en la MapsList.
     */
    public LinkedList<V> values() {
        LinkedList<V> values = new LinkedList<>();
        for (Entry<K, V> entry : entries) {
            values.add(entry.getValue());
        }
        return values;
    }

    
    /**
     * La clase Entry representa una entrada clave-valor en la MapsList.
     * 
     * @param <K> el tipo de las claves mantenidas por esta entrada.
     * @param <V> el tipo de los valores mantenidos por esta entrada.
     */
    public static class Entry<K, V> {
        private K key;
        private V value;

        
        /**
         * Constructor que inicializa una nueva entrada con la clave y el valor especificados.
         * 
         * @param key la clave de la entrada.
         * @param value el valor de la entrada.
         */
        public Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }

        
        
        /**
         * Obtiene la clave de la entrada.
         * 
         * @return la clave de la entrada.
         */
        public K getKey() {
            return key;
        }

        /**
         * Establece la clave de la entrada.
         * 
         * @param key la nueva clave de la entrada.
         */
        public void setKey(K key) {
            this.key = key;
        }

        /**
         * Obtiene el valor de la entrada.
         * 
         * @return el valor de la entrada.
         */
        public V getValue() {
            return value;
        }

        /**
         * Establece el valor de la entrada.
         * 
         * @param value el nuevo valor de la entrada.
         */
        public void setValue(V value) {
            this.value = value;
        }
    }
}

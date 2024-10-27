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
public class MapsList<K, V>  {
    private LinkedList<Entry<K, V>> entries;

    //Constructor
    public MapsList() {
        this.entries = new LinkedList<>(); // Para almacenar las entradas clave-valor
    }

    // Método para agregar o actualizar una entrada en la MapsList
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
      
    // Método para obtener el valor asociado a una clave
    public V get(K key) {
        for (Entry<K, V> entry : entries) {
            if (entry.getKey().equals(key)) {
                return entry.getValue(); // Retornar si encuentra
            }
        }
        return null; //null si no se encuentra
    }
    
    // Método para eliminar una entrada basada en la clave
    public void remove(K key) {
        LinkedList<Entry<K, V>> toRemove = new LinkedList<>();
        for (Entry<K, V> entry : entries) {
            if (entry.getKey().equals(key)) {
                toRemove.add(entry);
            }
        }
        entries.removeAll(toRemove);
    }

    // Método para verificar si una clave existe en la MapsList
    public boolean containsKey(K key) {
        for (Entry<K, V> entry : entries) {
            if (entry.getKey().equals(key)) {
                return true;
            }
        }
        return false;
    }
    
    // Método para obtener un conjunto de todas las entradas
    public List<Entry<K, V>> entrySet() {
        return entries;
    }
      
    // Método para obtener el tamaño de MapsList
    public int size() {
        return entries.size();
    }
    

    // Método para obtener todas las claves en MapsList
    public LinkedList<K> keySet() {
        LinkedList<K> keys = new LinkedList<>();
        for (Entry<K, V> entry : entries) {
            keys.add(entry.getKey());
        }
        return keys;
    }

    // Método para obtener todas los valores en MapsList
    public LinkedList<V> values() {
        LinkedList<V> values = new LinkedList<>();
        for (Entry<K, V> entry : entries) {
            values.add(entry.getValue());
        }
        return values;
    }

    public static class Entry<K, V> {
        private K key;
        private V value;

        public Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }

        // Método para obtener la clave
        public K getKey() {
            return key;
        }

        // Método para establecer la clave
        public void setKey(K key) {
            this.key = key;
        }

        // Método para obtener el valor
        public V getValue() {
            return value;
        }

        // Método para establecer el valor
        public void setValue(V value) {
            this.value = value;
        }
    }
}

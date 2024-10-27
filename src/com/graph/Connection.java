/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.graph;

/**
 *
 * @author PC
 */

/**
 * Clase que representa una conexión (aristas)  entre dos estaciones en la red de metro.
 */
public class Connection {

    private Station station1; // Estacion de origen
    private Station station2; // Estacion de destino 
    private String type; // Atributo para el tipo de conexion

    /**
     * Constructor para la clase conexión donde se requieren dos estaciones.
     * 
     * @param station1 La estación de origen.
     * @param station2 La estación de destino.
     */
    
    public Connection(Station station1, Station station2) {
        this.station1 = station1;
        this.station2 = station2;
        this.type = "regular"; // Por defecto sera del tipo regular
    }
    
    /**
     * Constructor para la clase conexión donde se requieren dos estaciones y un tipo de conexión.
     * 
     * @param station1 La estación de origen.
     * @param station2 La estación de destino.
     * @param type El tipo de conexión.
     */

    public Connection(Station station1, Station station2, String type) {
        this.station1 = station1;
        this.station2 = station2;
        this.type = type; // Por defecto sera del tipo regular
    }
    

    /**
     * Retorna la estación de origen.
     * 
     * @return La estación de origen.
     */
    
    public Station getStation1() {
        return station1;
    }
    
    /**
     * Retorna la estación de destino.
     * 
     * @return La estación de destino.
     */
    
    public Station getStation2() {
        return station2;
    }

    /**
     * Establece la estación de origen.
     * 
     * @param station1 La estación de origen a establecer.
     */
    public void setStation1(Station station1) {
        this.station1 = station1;
    }

    /**
     * Establece la estación de destino.
     * 
     * @param station2 La estación de destino a establecer.
     */
    
    public void setStation2(Station station2) {
        this.station2 = station2;
    }

    /**
     * Retorna el tipo de conexión.
     * 
     * @return El tipo de conexión.
     */
    
    public String getType() {
        return type;
    }

    /**
     * Establece el tipo de conexión.
     * 
     * @param type El tipo de conexión a establecer.
     */
    
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Método para verificar si la conexión es peatonal.
     *
     * @return true si la conexión es peatonal, false en caso contrario.
     */
    public boolean isPedestrian() {
        return "peatonal".equalsIgnoreCase(type); // Compara el tipo ignorando mayúsculas y minúsculas
    }
    
    
    /**
     * Verifica si esta conexión es igual a otra conexión.
     * 
     * @param other La otra conexión a comparar.
     * @return true si las conexiones son iguales, false en caso contrario.
     */

    public boolean isEqual(Connection other) {
        if (other == null) {
            return false; // Controla si el objeto comparado es nulo
        }
        return (this.station1 != null && this.station2 != null)
                && ((this.station1.equals(other.station1) && this.station2.equals(other.station2))
                || (this.station1.equals(other.station2) && this.station2.equals(other.station1)));
    }

    
    /**
     * Método para obtener la estación vecina a partir de una estación dada.
     *
     * @param station La estación de la cual se quiere conocer la estación
     * vecina.
     * @return La estación vecina si es parte de esta conexión, null en caso
     * contrario.
     */
    public Station getNeighbors(Station station) {
        if (station.equals(station1)) {
            return station2; // Si la estación dada es station1, retorna station2
        } else if (station.equals(station2)) {
            return station1; // Si la estación dada es station2, retorna station1
        }
        return null; // Si la estación no es parte de esta conexión, retorna null
    }
}

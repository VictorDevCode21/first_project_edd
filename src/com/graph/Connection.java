/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.graph;

/**
 *
 * @author PC
 */
// Clase para establecer las conexiones (aristas)
/* Se aclara que se utilizara grafo no dirigido por lo que las conexiones seran 
    Bidireccionales
 */
public class Connection {

    private Station station1; // Estacion de origen
    private Station station2; // Estacion de destino 
    private String type; // Atributo para el tipo de conexion

    //  Constructor para la clase conexion donde se requieren 2 estaciones
    public Connection(Station station1, Station station2) {
        this.station1 = station1;
        this.station2 = station2;
        this.type = "regular"; // Por defecto sera del tipo regular
    }

    public Connection(Station station1, Station station2, String type) {
        this.station1 = station1;
        this.station2 = station2;
        this.type = type; // Por defecto sera del tipo regular
    }

    // Retorna cual es la estacion de origen   
    public Station getStation1() {
        return station1;
    }

    // Retorna la estacion de destino    
    public Station getStation2() {
        return station2;
    }

    /**
     * @param station1 the station1 to set
     */
    public void setStation1(Station station1) {
        this.station1 = station1;
    }

    /**
     * @param station2 the station2 to set
     */
    public void setStation2(Station station2) {
        this.station2 = station2;
    }

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type the type to set
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

    public boolean isEqual(Connection other) {
        if (other == null) {
            return false; // Controla si el objeto comparado es nulo
        }
        return (this.station1 != null && this.station2 != null)
                && ((this.station1.equals(other.station1) && this.station2.equals(other.station2))
                || (this.station1.equals(other.station2) && this.station2.equals(other.station1)));
    }
}

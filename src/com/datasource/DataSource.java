/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.datasource;

import com.graph.Graph;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author PC
 */
public class DataSource {

    private Map<String, List<Map<String, List<Object>>>> networkData;

    // Constructor
    public DataSource() {
        this.networkData = new HashMap<>();
    }

    // Método para cargar el archivo JSON
     public void loadNetworkFromFile(String filePath) throws IOException {
        Gson gson = new Gson();
        try (Reader reader = new FileReader(filePath)) {
            Type type = new TypeToken<Map<String, List<Map<String, List<Object>>>>>() {}.getType();
            networkData = gson.fromJson(reader, type);
        } catch (IOException e) {
            System.err.println("Error reading the JSON file: " + e.getMessage());
            throw e;
        }
    }

    // Método para obtener los datos de la red cargada
    public Map<String, List<Map<String, List<Object>>>> getNetworkData() {
        return networkData;
    }
}

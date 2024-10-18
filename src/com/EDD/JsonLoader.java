/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.EDD;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.json.JSONException;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 *
 * @author carlos
 */
public class JsonLoader {

    /**
     * Carga una red de transporte desde un archivo JSON.
     * 
     * @param filePath La ruta del archivo JSON.
     * @return Un objeto Grafo que representa la red de transporte.
     * @throws IOException Si ocurre un error al leer el archivo.
     */
    public static Grafo cargarRedDesdeJson(String filePath) throws IOException {
        Grafo grafo = new Grafo();
        try (InputStream is = new FileInputStream(filePath)) {
            JSONTokener tokener = new JSONTokener(is);
            JSONObject jsonObject = new JSONObject(tokener);

            for (String key : jsonObject.keySet()) {
                JSONArray lineas = jsonObject.getJSONArray(key);
                for (int i = 0; i < lineas.length(); i++) {
                    JSONObject linea = lineas.getJSONObject(i);
                    for (String nombreLinea : linea.keySet()) {
                        JSONArray paradas = linea.getJSONArray(nombreLinea);
                        String paradaAnterior = null;
                        for (int j = 0; j < paradas.length(); j++) {
                            Object paradaObj = paradas.get(j);
                            String paradaActual;
                            if (paradaObj instanceof JSONObject) {
                                JSONObject paradaJson = (JSONObject) paradaObj;
                                paradaActual = paradaJson.keys().next();
                                String paradaConectada = paradaJson.getString(paradaActual);
                                grafo.addParada(paradaActual);
                                grafo.addParada(paradaConectada);
                                grafo.conectarParadas(paradaActual, paradaConectada);
                            } else {
                                paradaActual = (String) paradaObj;
                                grafo.addParada(paradaActual);
                                if (paradaAnterior != null) {
                                    grafo.conectarParadas(paradaAnterior, paradaActual);
                                }
                            }
                            paradaAnterior = paradaActual;
                        }
                    }
                }
            }
        } catch (JSONException e) {
            throw new IOException("Error al procesar el archivo JSON: " + e.getMessage(), e);
        }
        return grafo;
    }
}

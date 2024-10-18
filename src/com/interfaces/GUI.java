/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.interfaces;
import com.EDD.Grafo;
import com.EDD.JsonLoader;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
/**
 *
 * @author carlos
 */
public class GUI extends JFrame{
    
    private Grafo grafo;

    public GUI() {
        setTitle("Supermarket Location Planner");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        initUI();
    }

    private void initUI() {
        // Paneles y componentes
        JButton loadButton = new JButton("Cargar Red de Transporte");
        loadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int result = fileChooser.showOpenDialog(null);
                if (result == JFileChooser.APPROVE_OPTION) {
                    String filePath = fileChooser.getSelectedFile().getAbsolutePath();
                    try {
                        grafo = JsonLoader.cargarRedDesdeJson(filePath);
                        grafo.mostrarGrafo();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(null, "Error al cargar el archivo JSON: " + ex.getMessage());
                    } catch (org.json.JSONException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(null, "Formato de archivo JSON incorrecto: " + ex.getMessage());
                    }
                }
            }
        });

        // Agregar componentes al frame
        add(loadButton, BorderLayout.NORTH);
        // Otros componentes y configuraciones
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new GUI().setVisible(true);
            }
        });
    }
    
}

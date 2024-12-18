package com.interfaces;

import com.graph.AlgorithmSelectionListener;
import com.graph.BranchListener;
import com.graph.LinkedList;
import com.graph.NetworkTrain;
import com.graph.PanelChangeListener;
import com.graph.Station;

/**
 *
 * @author Joao
 */
public class ShowSuggestedBranches extends javax.swing.JPanel implements AlgorithmSelectionListener, BranchListener {

    private PanelChangeListener listener; // Referencia al listener
    private GUI gui;
    private boolean selectedAlgorithm;
    private AlgorithmSelectionListener algorithmListener;

    /**
     * Creates new form Page5
     */
    public ShowSuggestedBranches(PanelChangeListener listener, GUI gui) {
        this.listener = listener;
        this.algorithmListener = algorithmListener;
        this.gui = gui;
        initComponents();

        // Registra este panel como un listener
        gui.addAlgorithmSelectionListener(this);
        gui.addBranchListener(this);
    }

    @Override
    public void onAlgorithmSelected(boolean useBFS) {
        this.selectedAlgorithm = useBFS;
        showSuggestedBranches();
    }
    
    @Override
    public void onBranchChanged() {
        // Lógica para actualizar las sucursales sugeridas cuando cambian las sucursales
        showSuggestedBranches();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        closeButton = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        inputSuggestedBranches = new javax.swing.JTextArea();
        jLabel2 = new javax.swing.JLabel();
        selectDFSButton = new javax.swing.JButton();
        selectBFSButton = new javax.swing.JButton();

        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        closeButton.setBackground(new java.awt.Color(153, 153, 153));
        closeButton.setText("Close");
        closeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                closeButtonActionPerformed(evt);
            }
        });
        jPanel3.add(closeButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 110, -1, 20));

        jLabel1.setForeground(new java.awt.Color(0, 0, 0));
        jLabel1.setText("Seleccione un algoritmo:");
        jPanel3.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 20, 140, 20));

        inputSuggestedBranches.setBackground(new java.awt.Color(153, 153, 153));
        inputSuggestedBranches.setColumns(20);
        inputSuggestedBranches.setRows(5);
        jScrollPane2.setViewportView(inputSuggestedBranches);

        jPanel3.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 40, 180, 70));

        jLabel2.setForeground(new java.awt.Color(0, 0, 0));
        jLabel2.setText("Suggested Branches:");
        jPanel3.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 60, 110, 20));

        selectDFSButton.setText("DFS");
        selectDFSButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectDFSButtonActionPerformed(evt);
            }
        });
        jPanel3.add(selectDFSButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 10, -1, -1));

        selectBFSButton.setText("BFS");
        selectBFSButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectBFSButtonActionPerformed(evt);
            }
        });
        jPanel3.add(selectBFSButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 10, -1, -1));

        add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 370, 140));
    }// </editor-fold>//GEN-END:initComponents

    private void closeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_closeButtonActionPerformed

        if (listener != null) {
            // Asegúrate de tener una instancia de Page5
            Page2 page2 = new Page2(gui, listener); // O usa una referencia existente si ya fue creada

            listener.onChangePanel(page2); // Llama al método para cambiar a Page2
        }
    }//GEN-LAST:event_closeButtonActionPerformed

    private void selectDFSButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectDFSButtonActionPerformed
        selectedAlgorithm = false;  // DFS seleccionado
        gui.notifyAlgorithmSelection(selectedAlgorithm); // Notificar selección
        showSuggestedBranches();
    }//GEN-LAST:event_selectDFSButtonActionPerformed

    private void selectBFSButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectBFSButtonActionPerformed
        selectedAlgorithm = true;  // BFS seleccionado
        gui.notifyAlgorithmSelection(selectedAlgorithm); // Notificar selección
        showSuggestedBranches();
    }//GEN-LAST:event_selectBFSButtonActionPerformed

    private void showSuggestedBranches() {
        // Obtener las estaciones no cubiertas y sugerir nuevas sucursales
        String suggestBranch = gui.suggestNewBranches(selectedAlgorithm);
        inputSuggestedBranches.setText(suggestBranch);
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton closeButton;
    private javax.swing.JTextArea inputSuggestedBranches;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JButton selectBFSButton;
    private javax.swing.JButton selectDFSButton;
    // End of variables declaration//GEN-END:variables
}

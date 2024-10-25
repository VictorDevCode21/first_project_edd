package com.interfaces;

import com.graph.LinkedList;
import com.graph.PanelChangeListener;
import com.graph.Station;

/**
 *
 * @author Joao
 */
public class Page2 extends javax.swing.JPanel {

    private PanelChangeListener listener; // Referencia al listener
    private GUI gui;

    /**
     * Creates new form Page2
     */
    public Page2(GUI gui, PanelChangeListener listener) {
        this.listener = listener;
        this.gui = gui;
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel3 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        checkTotalCoverageButton = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        suggestedBranchesButton = new javax.swing.JButton();

        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel2.setForeground(new java.awt.Color(0, 0, 0));
        jLabel2.setText("Revisar cobertura total:");
        jPanel3.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 26, -1, 30));

        checkTotalCoverageButton.setBackground(new java.awt.Color(153, 153, 153));
        checkTotalCoverageButton.setText("check");
        checkTotalCoverageButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkTotalCoverageButtonActionPerformed(evt);
            }
        });
        jPanel3.add(checkTotalCoverageButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 30, -1, -1));

        jLabel1.setForeground(new java.awt.Color(0, 0, 0));
        jLabel1.setText("Ver sucursales sugeridas:");
        jPanel3.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 90, -1, 20));

        suggestedBranchesButton.setBackground(new java.awt.Color(153, 153, 153));
        suggestedBranchesButton.setText("see");
        suggestedBranchesButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                suggestedBranchesButtonActionPerformed(evt);
            }
        });
        jPanel3.add(suggestedBranchesButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 90, -1, -1));

        add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 370, 140));
    }// </editor-fold>//GEN-END:initComponents

    private void checkTotalCoverageButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkTotalCoverageButtonActionPerformed
        // Mostrar la interfaz correspondiente
        ShowTotalCoverage showTotalCoverage = new ShowTotalCoverage(listener, gui);
        if (listener != null) {
            listener.onChangePanel(showTotalCoverage);
        }


    }//GEN-LAST:event_checkTotalCoverageButtonActionPerformed

    private void suggestedBranchesButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_suggestedBranchesButtonActionPerformed
        // Crear y mostrar la interfaz ShowSuggestedBranches, pasando la sugerencia
        ShowSuggestedBranches showSuggestedBranches = new ShowSuggestedBranches(listener, gui);
        if (listener != null) {
            listener.onChangePanel(showSuggestedBranches);
        }
    }//GEN-LAST:event_suggestedBranchesButtonActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton checkTotalCoverageButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JButton suggestedBranchesButton;
    // End of variables declaration//GEN-END:variables
}

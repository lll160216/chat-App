/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import status.RequestSever;

/**
 *
 * @author ASUS
 */
public class Onegame extends javax.swing.JFrame {

    /**
     * Creates new form TicTacToe
     */
    private Socket connection;
//    private ObjectOutputStream yourTurn;
//    private ObjectInputStream opponentTurn;
    private ChatGUI.RoomGuest parentGuest;
    private ChatGUI.RoomHost parentHost;
    private String hostTurn;
    private String guestTurn;

    public Onegame() {
        initComponents();
    }

    public Onegame(ChatGUI.RoomGuest parent, String name) {
        this.parentGuest = parent;
        initComponents();
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
//        this.addWindowListener();
        setTitle(name);
    }

    public Onegame(ChatGUI.RoomHost parent, String name) throws IOException {
        initComponents();
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.parentHost = parent;
        setTitle(name);
//        yourTurn = new ObjectOutputStream(connection.getOutputStream());
//        opponentTurn = new ObjectInputStream(connection.getInputStream());
    }

    public void setHostTurn(String hostTurn) throws InterruptedException, Exception {
        this.hostTurn = hostTurn;
        if (guestTurn != null) {
            isWin();
        }
    }

    public synchronized void setGuestTurn(String guestTurn) throws Exception {
        if (parentHost != null) {
            this.guestTurn = guestTurn;
            if (hostTurn != null) {
                isWin();
            }
        } else {
            parentGuest.sendRequest(new RequestSever(RequestSever.SEND_GUEST_TURN, guestTurn));
        }
    }

    public synchronized String getHostTurn() throws InterruptedException {
        return hostTurn;
    }

    private void isWin() throws Exception {
        if (hostTurn.equals(guestTurn)) {
            Client.request("DRAW", false);
            dispose();
            parentHost.sendRequest(new RequestSever(RequestSever.SEND_RESULT, "0"));
        } else if (hostTurn.equals("KEO") && guestTurn.equals("BAO")) {
            Client.request("WIN", false);
            dispose();
            parentHost.sendRequest(new RequestSever(RequestSever.SEND_RESULT, "1"));
        } else if (hostTurn.equals("BAO") && guestTurn.equals("BUA")) {
            Client.request("WIN", false);
            dispose();
            parentHost.sendRequest(new RequestSever(RequestSever.SEND_RESULT, "1"));
        } else if (hostTurn.equals("BUA") && guestTurn.equals("KEO")) {
            Client.request("WIN", false);
            dispose();
            parentHost.sendRequest(new RequestSever(RequestSever.SEND_RESULT, "1"));
        } else if (hostTurn.equals("KEO") && guestTurn.equals("BUA")) {
            Client.request("LOSE", false);
            dispose();
            parentHost.sendRequest(new RequestSever(RequestSever.SEND_RESULT, "-1"));
        } else if (hostTurn.equals("BAO") && guestTurn.equals("KEO")) {
            Client.request("LOSE", false);
            dispose();
            parentHost.sendRequest(new RequestSever(RequestSever.SEND_RESULT, "-1"));
        } else if (hostTurn.equals("BUA") && guestTurn.equals("BAO")) {
            Client.request("LOSE", false);
            dispose();
            parentHost.sendRequest(new RequestSever(RequestSever.SEND_RESULT, "-1"));
        }
    }

    /**
     * .
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jComboBox1 = new javax.swing.JComboBox<>();
        btnBua = new javax.swing.JButton();
        btnKeo = new javax.swing.JButton();
        btnBao = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        btnBua.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/bua.png"))); // NOI18N
        btnBua.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuaActionPerformed(evt);
            }
        });

        btnKeo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/keo.png"))); // NOI18N
        btnKeo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnKeoActionPerformed(evt);
            }
        });

        btnBao.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/bao1.png"))); // NOI18N
        btnBao.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBaoActionPerformed(evt);
            }
        });

        jLabel1.setText("KE??O");

        jLabel2.setText("BU??A");

        jLabel3.setText("BAO");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnKeo, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 38, Short.MAX_VALUE)
                .addComponent(btnBua, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(41, 41, 41)
                .addComponent(btnBao, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20))
            .addGroup(layout.createSequentialGroup()
                .addGap(48, 48, 48)
                .addComponent(jLabel1)
                .addGap(133, 133, 133)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel3)
                .addGap(49, 49, 49))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnBua)
                        .addComponent(btnKeo))
                    .addComponent(btnBao))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel3)))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnBaoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBaoActionPerformed
        if (parentHost != null) {
            try {
                setHostTurn("BAO");
                dispose();
            } catch (Exception ex) {
            }
        }
        if (parentGuest != null) {
            try {     
                setGuestTurn("BAO");
                dispose();
            } catch (Exception ex) {
                Logger.getLogger(Onegame.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }//GEN-LAST:event_btnBaoActionPerformed

    private void btnKeoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnKeoActionPerformed
        if (parentHost != null) {
            try {
                setHostTurn("KEO");
                dispose();
            } catch (Exception ex) {
            }
        }
        if (parentGuest != null) {
            try {
                setGuestTurn("KEO");
                dispose();
            } catch (Exception ex) {
                Logger.getLogger(Onegame.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_btnKeoActionPerformed

    private void btnBuaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuaActionPerformed
        if (parentHost != null) {
            try {
                setHostTurn("BUA");
                dispose();
            } catch (Exception ex) {
            }
        }
        if (parentGuest != null) {
            try {
                setGuestTurn("BUA");
                dispose();
            } catch (Exception ex) {
                Logger.getLogger(Onegame.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_btnBuaActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Onegame.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Onegame.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Onegame.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Onegame.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Onegame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBao;
    private javax.swing.JButton btnBua;
    private javax.swing.JButton btnKeo;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    // End of variables declaration//GEN-END:variables
}
